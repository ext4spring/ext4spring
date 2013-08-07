package org.ext4spring.acl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Required;

public class AclContextImpl implements AclContext {

	private DataSource dataSource;

	@Override
	public DataSource getDataSource() {
		return this.dataSource;
	}

	@Override
	@Required
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
