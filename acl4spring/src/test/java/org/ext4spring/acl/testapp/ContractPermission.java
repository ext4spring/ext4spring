package org.ext4spring.acl.testapp;

import org.ext4spring.acl.model.AclPermission;

public enum ContractPermission implements AclPermission{
	
	READ,CONFIRM,SIGN;

	@Override
	public String getName() {
		return super.name();
	}
	
}
