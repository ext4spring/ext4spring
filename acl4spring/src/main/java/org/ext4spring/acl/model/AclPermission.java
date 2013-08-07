package org.ext4spring.acl.model;
/**
 * Represents one permission of a domain object
 * Implementation could be an ENUM
 * 
 * @author borbasp
 *
 */
public interface AclPermission {

	/**
	 * Name of the permission Must be unique for each domain object type
	 * 
	 * @return
	 */
	String getName();
	
	//TODO: owner support (isOwnerPermission)

}
