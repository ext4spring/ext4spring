package org.ext4spring.acl.model;

/**
 * Behavior of the AclPermissionAllowed annotation
 * 
 * @author borbasp
 * 
 */
public enum Behavior {
	THROW_EXCEPTION, // Throw exception when not enough privileges
	FILTER // Return null when not enough privileges
}
