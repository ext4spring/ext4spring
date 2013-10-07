/*******************************************************************************
 * Copyright 2013 the original author
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.ext4spring.parameter.dao;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ext4spring.parameter.exception.ParameterException;
import org.ext4spring.parameter.model.Metadata;
import org.ext4spring.parameter.model.RepositoryMode;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

/**
 * Uses a property file as repository
 * 
 * @author Peter Borbas
 * 
 */
public class PropertyParameterRepository extends AbstractParameterRepository {

	private Properties properties;
	private Resource propertyResource;
        private static final Log LOGGER = LogFactory.getLog(PropertyParameterRepository.class);

	protected String createKey(Metadata metadata) {
		return metadata.getDomain() + "." + metadata.getFullParameterName();
	}

	@Override
	public String getValue(Metadata metadata) {
		return this.properties.getProperty(this.createKey(metadata));
	}

	@Override
	public boolean parameterExists(Metadata metadata) {
		return this.properties.containsKey(this.createKey(metadata));
	};

	@Override
	public void setValue(Metadata metadata, String value) {
		throw new ParameterException("Write not allowed on PropertyRepository. Parameter:"+metadata);
	}

	@Required
	public void setPropertyResource(Resource propertyResource) {
		this.propertyResource = propertyResource;
	}

	@PostConstruct
	public void init() throws IOException {
		this.properties = new Properties();
		this.properties.load(this.propertyResource.getInputStream());
	}

	@Override
	public RepositoryMode getMode(String domain) {
	    RepositoryMode currentMode=super.getMode(domain);
		if (!RepositoryMode.NONE.equals(currentMode)) {
			if (!RepositoryMode.READ_ONLY.equals(currentMode)) {
			    LOGGER.warn("Parameter repository only supports READ_ONLY mode, but configured as:"+currentMode);
			}
			return RepositoryMode.READ_ONLY;
		}
		return RepositoryMode.NONE;
	}
}
