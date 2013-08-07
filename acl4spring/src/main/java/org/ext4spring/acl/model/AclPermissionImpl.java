package org.ext4spring.acl.model;
/**
 * Default implementation returned by the ACL API calls
 * @author borbasp
 *
 */
public class AclPermissionImpl implements AclPermission {
	private String name;
	
	
	
	public AclPermissionImpl(String name) {
		super();
		this.name = name;
	}



	@Override
	public int hashCode() {
		return (this.name!=null)?this.name.hashCode():0;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AclPermission))
			return false;
		AclPermission other = (AclPermission) obj;
		if (name == null) {
			if (other.getName() != null)
				return false;
		} else if (!name.equals(other.getName()))
			return false;
		return true;
	}



	@Override
	public String getName() {
		return name;
	}
}
