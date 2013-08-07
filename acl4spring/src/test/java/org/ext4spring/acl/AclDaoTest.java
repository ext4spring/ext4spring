package org.ext4spring.acl;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.ext4spring.acl.model.AclPermission;
import org.ext4spring.acl.model.AclPermissionImpl;
import org.ext4spring.acl.testapp.Contract;
import org.ext4spring.acl.testapp.ContractPermission;
import org.ext4spring.acl.testapp.Project;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testApplicationContext.xml")
@Transactional
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
public class AclDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
		
	@Autowired
	private AclDao aclDao;
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate=new JdbcTemplate(dataSource);
	}
	
	
	@Test
	public void testInsertEntry() {
		this.aclDao.insertEntry(Contract.class, "domain1", "user1", ContractPermission.CONFIRM);
		Assert.assertEquals(1, this.jdbcTemplate.queryForLong("SELECT count(*) FROM EFS_ACL WHERE OBJECT_ID=? AND USER_ID=? AND ACL_PERMISSION=?","domain1","user1","CONFIRM"));
	}

	@Test
	public void testQuery() {
		Map<String, List<AclPermission>> result=this.aclDao.query(Contract.class, "c1");
		Assert.assertTrue(result.containsKey("admin"));
		Assert.assertTrue(result.get("admin").contains(new AclPermissionImpl("READ")));
		Assert.assertTrue(result.get("admin").contains(new AclPermissionImpl("CONFIRM")));
		Assert.assertTrue(result.get("admin").contains(new AclPermissionImpl("SIGN")));
		Assert.assertTrue(result.containsKey("read"));
		Assert.assertTrue(result.get("read").contains(new AclPermissionImpl("READ")));
		Assert.assertFalse(result.get("read").contains(new AclPermissionImpl("CONFIRM")));
		Assert.assertFalse(result.get("read").contains(new AclPermissionImpl("SIGN")));
		Assert.assertTrue(result.containsKey("confirm"));
		Assert.assertFalse(result.get("confirm").contains(new AclPermissionImpl("READ")));
		Assert.assertTrue(result.get("confirm").contains(new AclPermissionImpl("CONFIRM")));
		Assert.assertFalse(result.get("confirm").contains(new AclPermissionImpl("SIGN")));
		Assert.assertTrue(result.containsKey("sign"));
		Assert.assertFalse(result.get("sign").contains(new AclPermissionImpl("READ")));
		Assert.assertFalse(result.get("sign").contains(new AclPermissionImpl("CONFIRM")));
		Assert.assertTrue(result.get("sign").contains(new AclPermissionImpl("SIGN")));
	}

	@Test 
	public void testRemoveAllFromOneDomainObject() {
		Assert.assertEquals(3, this.jdbcTemplate.queryForLong("SELECT count(*) FROM EFS_ACL WHERE OBJECT_TYPE=?","org.ext4spring.acl.testapp.Project"));
		this.aclDao.remove(Project.class, "p1");
		Assert.assertEquals(0, this.jdbcTemplate.queryForLong("SELECT count(*) FROM EFS_ACL WHERE OBJECT_TYPE=?","org.ext4spring.acl.testapp.Project"));
	}
	@Test 
	public void testRemoveAllFromOneDomainObjectAndSpecificUser() {
		Assert.assertEquals(1, this.jdbcTemplate.queryForLong("SELECT count(*) FROM EFS_ACL WHERE OBJECT_TYPE=? AND OBJECT_ID=? AND USER_ID=?","org.ext4spring.acl.testapp.Contract","c1","read"));
		this.aclDao.remove(Contract.class, "c1","read");
		Assert.assertEquals(0, this.jdbcTemplate.queryForLong("SELECT count(*) FROM EFS_ACL WHERE OBJECT_TYPE=? AND OBJECT_ID=? AND USER_ID=?","org.ext4spring.acl.testapp.Contract","c1","read"));
	}
	@Test 
	public void testRemovePermissionFromDomainObjectAndSpecificUser() {
		Assert.assertEquals(3, this.jdbcTemplate.queryForLong("SELECT count(*) FROM EFS_ACL WHERE OBJECT_TYPE=? AND OBJECT_ID=? AND USER_ID=?","org.ext4spring.acl.testapp.Contract","c1","admin"));
		Assert.assertEquals(1, this.jdbcTemplate.queryForLong("SELECT count(*) FROM EFS_ACL WHERE OBJECT_TYPE=? AND OBJECT_ID=? AND USER_ID=? AND ACL_PERMISSION=?","org.ext4spring.acl.testapp.Contract","c1","admin","READ"));
		this.aclDao.remove(Contract.class, "c1","admin", ContractPermission.READ);
		Assert.assertEquals(2, this.jdbcTemplate.queryForLong("SELECT count(*) FROM EFS_ACL WHERE OBJECT_TYPE=? AND OBJECT_ID=? AND USER_ID=?","org.ext4spring.acl.testapp.Contract","c1","admin"));
		Assert.assertEquals(0, this.jdbcTemplate.queryForLong("SELECT count(*) FROM EFS_ACL WHERE OBJECT_TYPE=? AND OBJECT_ID=? AND USER_ID=? AND ACL_PERMISSION=?","org.ext4spring.acl.testapp.Contract","c1","admin","READ"));
	}

	@Test
	public void testModificationMethodsEvictsCache() {
		int count=this.aclDao.query(Contract.class, "c1").get("admin").size();
		this.aclDao.remove(Contract.class, "c1", "admin", ContractPermission.CONFIRM);
		Assert.assertNotEquals(count, this.aclDao.query(Contract.class, "c1").get("admin").size());
		this.aclDao.insertEntry(Contract.class, "c1", "admin", ContractPermission.CONFIRM);
		Assert.assertEquals(count, this.aclDao.query(Contract.class, "c1").get("admin").size());
		this.aclDao.remove(Contract.class, "c1", "admin");
		Assert.assertEquals(null, this.aclDao.query(Contract.class, "c1").get("admin"));
		this.aclDao.insertEntry(Contract.class, "c1", "admin", ContractPermission.CONFIRM);
		Assert.assertEquals(1, this.aclDao.query(Contract.class, "c1").get("admin").size());
		this.aclDao.remove(Contract.class, "c1");
		Assert.assertEquals(0,this.aclDao.query(Contract.class, "c1").size());		
	}
	
	@Test
	public void manualCacheTest() {
		this.aclDao.query(Contract.class, "c1");
		this.aclDao.query(Contract.class, "c1");
	}
}
