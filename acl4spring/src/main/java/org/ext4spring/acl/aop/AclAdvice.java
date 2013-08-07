package org.ext4spring.acl.aop;

import org.ext4spring.acl.model.Behavior;
import org.ext4spring.acl.model.ProtectionType;

/**
 * It takes the aclIdArg argument of the method invocation or the methods return
 * value and passes it to the aclIdResolver spring bean to get the ID of the
 * domain object of type aclType. After that it takes the user name from the
 * spring security context and check if the user has any of the permissions for
 * the domain object with the resolved id. if it hasn't it filters it or throws
 * Exception.
 * 
 * @see AclPermissionAllowed
 * @author borbasp
 * 
 */

public class AclAdvice {
	//TODO: implement
	//TODO: unit test
	//TODO: userId resolver with default Spring Security impl.
    
        
    /**
     * Entry point of the AOP advice
     * @return
     */
        public Object intercept() {
            //TODO get annotation
            AclPermissionAllowed aclPermissionAllowed = null;
            if (aclPermissionAllowed.protectionType().equals(ProtectionType.METHOD_CALL)) {
                return this.protectMethodCall();
            } else {
                return this.protectReturnValue();
            }
        }
        
        private Object protectMethodCall() {
            return null;
        }
        
        private Object protectReturnValue() {
            return null;
        }
}
