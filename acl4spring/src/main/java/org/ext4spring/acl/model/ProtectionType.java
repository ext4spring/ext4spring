package org.ext4spring.acl.model;
/**
 * What to protect with the AclPermissionAllowed annotation
 * @author borbasp
 *
 */
public enum ProtectionType {
	METHOD_CALL, //Method only executed when permissions are sufficient (before advice)
	RETURN_VALUE; //The method gets executed and the return value is checked for permissions (after returning advice)
}
