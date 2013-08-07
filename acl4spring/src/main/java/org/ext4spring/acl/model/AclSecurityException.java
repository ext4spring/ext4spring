package org.ext4spring.acl.model;

/**
 * 
 * Thrown when someone tries to access a domain object without sufficient
 * permissions
 * 
 * @author borbasp
 * 
 */
public class AclSecurityException extends SecurityException {

	private static final long serialVersionUID = -4691503808813928385L;

	public AclSecurityException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AclSecurityException(String arg0) {
		super(arg0);
	}

}
