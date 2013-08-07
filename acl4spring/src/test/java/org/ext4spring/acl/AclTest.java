package org.ext4spring.acl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ext4spring.acl.model.AclPermission;
import org.ext4spring.acl.testapp.Contract;
import org.ext4spring.acl.testapp.ContractPermission;
import org.ext4spring.acl.testapp.Project;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AclTest {

    private static final String CONTRACT_ID="c1";
    private static final String USER_NOT_EXISTS="userNotExists";
    private static final String USER_NO_PERMISSION="userNoPerm";
    private static final String USER_READ_PERMISSION="userR";
    private static final String USER_READ_CONFIRM="userRC";
    
    private AclImpl acl;
    private AclDao mockAclDao;
    
    @Before
    public void init() {
        acl=new AclImpl();
        mockAclDao=Mockito.mock(AclDao.class);
        acl.setAclDao(mockAclDao);
        
        Map<String, List<AclPermission>> aclMap=new HashMap<String, List<AclPermission>>();
        aclMap.put(USER_NO_PERMISSION, Arrays.asList(new AclPermission[]{}));
        aclMap.put(USER_READ_PERMISSION, Arrays.asList(new AclPermission[]{ContractPermission.READ}));
        aclMap.put(USER_READ_CONFIRM, Arrays.asList(new AclPermission[]{ContractPermission.READ, ContractPermission.CONFIRM}));        
        Mockito.when(mockAclDao.query(Contract.class, CONTRACT_ID)).thenReturn(aclMap);               
    }
    
    @Test
    public void testHasPermission() throws Exception {        
        Assert.assertFalse(acl.hasPermissions(Contract.class, CONTRACT_ID, USER_NOT_EXISTS, ContractPermission.READ));
        Assert.assertFalse(acl.hasPermissions(Contract.class, CONTRACT_ID, USER_NO_PERMISSION, ContractPermission.READ));
        Assert.assertFalse(acl.hasPermissions(Contract.class, CONTRACT_ID, USER_READ_PERMISSION, ContractPermission.CONFIRM));
        Assert.assertFalse(acl.hasPermissions(Contract.class, CONTRACT_ID, USER_READ_CONFIRM, ContractPermission.SIGN));
        Assert.assertFalse(acl.hasPermissions(Project.class, "p1", USER_READ_CONFIRM, ContractPermission.SIGN));        
        
        Assert.assertTrue(acl.hasPermissions(Contract.class, CONTRACT_ID, USER_READ_CONFIRM, ContractPermission.CONFIRM));
        Assert.assertTrue(acl.hasPermissions(Contract.class, CONTRACT_ID, USER_READ_CONFIRM, ContractPermission.SIGN, ContractPermission.CONFIRM));
        Assert.assertTrue(acl.hasPermissions(Contract.class, CONTRACT_ID, USER_READ_PERMISSION, ContractPermission.READ));
    }
    
    @Test
    public void testQueryPermissionWithoutUser() throws Exception {
        Map<String, List<AclPermission>> aclMap = acl.queryPermissions(Contract.class, CONTRACT_ID);
        Assert.assertEquals(3, aclMap.size());
        Assert.assertTrue(aclMap.containsKey(USER_NO_PERMISSION));
        Assert.assertTrue(aclMap.containsKey(USER_READ_PERMISSION));
        Assert.assertTrue(aclMap.containsKey(USER_READ_CONFIRM));
        Assert.assertTrue(aclMap.get(USER_NO_PERMISSION).size()==0);
        Assert.assertTrue(aclMap.get(USER_READ_PERMISSION).size()==1);
        Assert.assertTrue(aclMap.get(USER_READ_PERMISSION).contains(ContractPermission.READ));
        Assert.assertTrue(aclMap.get(USER_READ_CONFIRM).size()==2);
        Assert.assertTrue(aclMap.get(USER_READ_CONFIRM).contains(ContractPermission.READ));
        Assert.assertTrue(aclMap.get(USER_READ_CONFIRM).contains(ContractPermission.CONFIRM));
    }
    
    @Test
    public void testQueryPermissionWithUser() throws Exception {
        List<AclPermission> permissions = acl.queryPermissions(Contract.class, CONTRACT_ID, USER_NO_PERMISSION);
        Assert.assertEquals(0, permissions.size());
        permissions = acl.queryPermissions(Project.class, CONTRACT_ID, USER_NO_PERMISSION);
        Assert.assertEquals(0, permissions.size());
        permissions = acl.queryPermissions(Contract.class, CONTRACT_ID, USER_READ_PERMISSION);
        Assert.assertEquals(1, permissions.size());
        Assert.assertTrue(permissions.contains(ContractPermission.READ));
        permissions = acl.queryPermissions(Contract.class, CONTRACT_ID, USER_READ_CONFIRM);
        Assert.assertEquals(2, permissions.size());
        Assert.assertTrue(permissions.contains(ContractPermission.READ));
        Assert.assertTrue(permissions.contains(ContractPermission.CONFIRM));
    }    
    
    @Test
    public void testGrantNotExistingPermission() throws Exception {
        acl.grant(Contract.class, CONTRACT_ID, USER_NO_PERMISSION, ContractPermission.READ);
        Mockito.verify(mockAclDao).insertEntry(Contract.class, CONTRACT_ID, USER_NO_PERMISSION, ContractPermission.READ);        
    }

    @Test
    public void testGrantNotExistingClass() throws Exception {
        acl.grant(Project.class, CONTRACT_ID, USER_NO_PERMISSION, ContractPermission.READ);
        Mockito.verify(mockAclDao).insertEntry(Project.class, CONTRACT_ID, USER_NO_PERMISSION, ContractPermission.READ);        
    }   
    @Test
    public void testGrantExistingPermission() throws Exception {
        acl.grant(Contract.class, CONTRACT_ID, USER_READ_PERMISSION, ContractPermission.READ);
        Mockito.verify(mockAclDao, Mockito.never()).insertEntry(Contract.class, CONTRACT_ID, USER_READ_PERMISSION, ContractPermission.READ);        
    }

    @Test
    public void testRemovePermission() throws Exception {
        acl.remove(Contract.class, CONTRACT_ID);
        Mockito.verify(mockAclDao).remove(Contract.class, CONTRACT_ID);        
        acl.remove(Contract.class, CONTRACT_ID,USER_READ_CONFIRM);
        Mockito.verify(mockAclDao).remove(Contract.class, CONTRACT_ID,USER_READ_CONFIRM);        
        acl.remove(Contract.class, CONTRACT_ID,USER_READ_CONFIRM,ContractPermission.READ);
        Mockito.verify(mockAclDao).remove(Contract.class, CONTRACT_ID,USER_READ_CONFIRM,ContractPermission.READ);        
    }
    
    
}
