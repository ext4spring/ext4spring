package org.ext4spring.acl;

import java.util.List;
import java.util.Map;

import org.ext4spring.acl.model.AclException;
import org.ext4spring.acl.model.AclPermission;
/**
 * Persistence of ACLs
 * @author borbasp
 *
 */
public interface AclDao {
	/**
	 * Lists permissions of the user on the given type and id
	 * 
	 * @param type
	 *            Domain object type
	 * @param id
	 *            Id of the domain object
	 * @return <user name, permission list>
	 * @throws AclException
	 */	
	Map<String, List<AclPermission>> query(Class<?> type, String id);

	/**
	 * Insert a new entry into the ACL
	 * @param entry
	 */
	void insertEntry(Class<?> type, String id, String userId, AclPermission permission);
	
	/**
	 * Removes all permissions from the given type and id from all users
	 * 
	 * @param type
	 *            Domain object type
	 * @param id
	 *            Id of the domain object
	 * @throws AclException
	 */
	void remove(Class<?> type, String id);

	/**
	 * Remove all permissions of the specified user from the given type and id
	 * 
	 * @param type
	 *            Domain object type
	 * @param id
	 *            Id of the domain object
	 * @param userId
	 *            Unique user id (like user name)
	 * @throws AclException
	 */
	void remove(Class<?> type, String id, String userId);

	/**
	 * Remove the given permissions of the specified user from the given type
	 * and id
	 * 
	 * @param type
	 *            Domain object type
	 * @param id
	 *            Id of the domain object
	 * @param userId
	 *            Unique user id (like user name)
	 * @param permissions
	 *            Array of permissions
	 * @throws AclException
	 */
	void remove(Class<?> type, String id, String userId, AclPermission... permissions);
}
