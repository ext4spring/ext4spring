package org.ext4spring.acl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ext4spring.acl.model.AclException;
import org.ext4spring.acl.model.AclPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AclImpl implements Acl {

	//TODO: unit test
	@Autowired
	private AclDao aclDao;
	
	public void setAclDao(AclDao aclDao) {
		this.aclDao = aclDao;
	}
	
	@Override
	public boolean hasPermissions(Class<?> type, String id, String userId, AclPermission... permissions) throws AclException {
		Map<String, List<AclPermission>> aclMap=this.aclDao.query(type, id);
		return this.hasPermission(aclMap, userId, permissions);
	}

	private boolean hasPermission(Map<String, List<AclPermission>> aclMap, String userId, AclPermission... permissions) {
		if (aclMap.containsKey(userId)) {
			for (AclPermission checkedPermission:permissions) {
				if (aclMap.get(userId).contains(checkedPermission)) {
					return true;
				}
			}
		}
		return false;		
	}
	
	@Override
	public void grant(Class<?> type, String id, String userId, AclPermission... permissions) throws AclException {
		Map<String, List<AclPermission>> aclMap=this.aclDao.query(type, id);
		for (AclPermission grantPermission:permissions) {
			if (!hasPermission(aclMap, userId, grantPermission)) {
				aclDao.insertEntry(type, id, userId, grantPermission);
			}
		}
	}

	@Override
	public void remove(Class<?> type, String id) throws AclException {
		this.aclDao.remove(type, id);
	}

	@Override
	public void remove(Class<?> type, String id, String userId) throws AclException {
		this.aclDao.remove(type, id, userId);

	}

	@Override
	public void remove(Class<?> type, String id, String userId, AclPermission... permissions) throws AclException {
		this.aclDao.remove(type, id, userId, permissions);
	}

	@Override
	public List<AclPermission> queryPermissions(Class<?> type, String id, String userId) throws AclException {
		Map<String, List<AclPermission>> aclMap=this.aclDao.query(type, id);
		if (aclMap.containsKey(userId)) {
			return aclMap.get(userId);
		} else {
			return new ArrayList<AclPermission>();
		}
	}

	@Override
	public Map<String, List<AclPermission>> queryPermissions(Class<?> type, String id) throws AclException {
		return this.aclDao.query(type, id);
	}

}
