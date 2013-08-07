package org.ext4spring.acl.model;
/**
 * Thrown by ext4spring acl api calls
 * @author borbasp
 *
 */
public class AclException extends Exception {

	private static final long serialVersionUID = -3748961076355871718L;

	public AclException(String message, Throwable cause) {
		super(message, cause);
	}

	public AclException(String message) {
		super(message);
	}

	public AclException(Throwable cause) {
		super(cause);
	}

}
