package org.ext4spring.acl;

import javax.sql.DataSource;

/**
 * Configuration interface of the ACL library.
 * This should be initialized by the applications spring context
 * @author borbasp
 *
 */
public interface AclContext {
	void setDataSource(DataSource dataSource);
	DataSource getDataSource();
	
	//TODO: user resolver
	//TODO: autocreate schema
	
	
}
