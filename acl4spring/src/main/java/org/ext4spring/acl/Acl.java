package org.ext4spring.acl;

import java.util.List;
import java.util.Map;

import org.ext4spring.acl.model.AclException;
import org.ext4spring.acl.model.AclPermission;

/**
 * Main API interface
 */
public interface Acl {

	/**
	 * Checks if the user has any of the given permissions to the given type and
	 * id It also check global permissions (all id and all user combinations)
	 * 
	 * @param type
	 *            Domain object type
	 * @param id
	 *            Id of the domain object
	 * @param userId
	 *            Unique user id (like user name)
	 * @param permissions
	 *            Array of permissions
	 * @return
	 * @throws AclException
	 */
	boolean hasPermissions(Class<?> type, String id, String userId, AclPermission... permissions) throws AclException;

	/**
	 * Grants the given permissions to the specified user to the given type and
	 * id
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
	void grant(Class<?> type, String id, String userId, AclPermission... permissions) throws AclException;

	/**
	 * Removes all permissions from the given type and id from all users
	 * 
	 * @param type
	 *            Domain object type
	 * @param id
	 *            Id of the domain object
	 * @throws AclException
	 */
	void remove(Class<?> type, String id) throws AclException;

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
	void remove(Class<?> type, String id, String userId) throws AclException;

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
	void remove(Class<?> type, String id, String userId, AclPermission... permissions) throws AclException;

	/**
	 * Lists permissions of the user on the given type and id
	 * 
	 * @param type
	 *            Domain object type
	 * @param id
	 *            Id of the domain object
	 * @param userId
	 *            Unique user id (like user name)
	 * @throws AclException
	 */
	List<AclPermission> queryPermissions(Class<?> type, String id, String userId) throws AclException;

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
	Map<String, List<AclPermission>> queryPermissions(Class<?> type, String id) throws AclException;

}
