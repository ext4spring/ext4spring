package org.ext4spring.acl.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ext4spring.acl.model.Behavior;
import org.ext4spring.acl.model.ProtectionType;

/**
 * Methods which are marked with @AclPermissionAllowed are checked for sufficient permissions by the
 * {@link AclAdvice}.
 * 
 * @see AclAdvice
 * 
 * @author borbasp
 * 
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Inherited
public @interface AclPermissionAllowed {

	/**
	 * What to do when not enough permission?
	 * 
	 * @return THROW_EXCEPTION: throw AclSecurityException. FILTER : don't call
	 *         method return null. for collections, the unauthorized items will
	 *         be removed
	 */
	Behavior behavior() default Behavior.THROW_EXCEPTION;

	/**
	 * Check access to one of the arguments and deny invocation (METHOD_CALL) or
	 * execute the method and check access to the return value (RETURN_VALUE)
	 */
	ProtectionType protectionType() default ProtectionType.METHOD_CALL;

	/**
	 * The required permissions to access the method
	 * 
	 * @return
	 */
	String[] permissions();

	/**
	 * Name of the argument that will be passed to the aclIdResolverBean to get
	 * the ID of the domain object. In ProtectionType.METHOD_CALL mode this is
	 * necessary.
	 * 
	 * @return
	 */
	String aclIdArg() default "";

	/**
	 * Type of the protected domain object
	 * 
	 * @return
	 */
	Class<?> aclType();

	/**
	 * Spring bean name of the AclIdResolver implementation
	 * 
	 * @return if null, the aclIdArg.toString() will be used as id of the domain
	 *         object.
	 */
	String aclIdResolverBean() default "";

}
