package org.ext4spring.acl.testapp;

import org.ext4spring.acl.model.AclPermission;

public enum ProjectPermission implements AclPermission{
	
	READ,WRITE,OWN;

	@Override
	public String getName() {
		return this.getName();
	}
	
}
