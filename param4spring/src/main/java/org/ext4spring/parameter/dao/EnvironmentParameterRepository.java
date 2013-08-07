package org.ext4spring.parameter.dao;

import org.ext4spring.parameter.exception.ParameterException;
import org.ext4spring.parameter.model.Metadata;
import org.ext4spring.parameter.model.RepositoryMode;
//TODO: test repo
public class EnvironmentParameterRepository implements ParameterRepository {

	protected String createKey(Metadata metadata) {
		return metadata.getDomain() + "." + metadata.getFullParameterName();
	}
	
	@Override
	public boolean parameterExists(Metadata metadata) {
		String stringValue = System.getenv(this.createKey(metadata));
        return stringValue!=null;
	}

	@Override
	public String getValue(Metadata metadata) {
		return System.getenv(this.createKey(metadata));
	}

	@Override
	public void setValue(Metadata metadata, String value) {
		throw new ParameterException("Overwriting enviroment variables not allowed. Dont use this repository for writeable parameters. Parameter: "+metadata);
	}

	@Override
	public RepositoryMode getMode(String domain) {
		return RepositoryMode.READ_ONLY;
	}

}
