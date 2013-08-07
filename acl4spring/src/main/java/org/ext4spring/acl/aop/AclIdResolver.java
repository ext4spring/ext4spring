package org.ext4spring.acl.aop;

import java.util.List;

/**
 * Implementations are used by the AclAdvice to resolve domain object IDs from
 * the method argument or the return value
 * 
 * @author borbasp
 * 
 */
public interface AclIdResolver {

	/**
	 * Parses the argument and returns the related domain object IDs
	 * 
	 * @param sourceObject
	 * @return
	 */
	List<String> getId(Object arg);

	//TODO: owner support  List<String> getOwners(Object arg);

}
