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

import org.ext4spring.parameter.model.Metadata;
import org.ext4spring.parameter.model.RepositoryMode;

/**
 * Handles the persistent repository of the parameters Can be cached
 * 
 * @author Peter Borbas
 * 
 */
public interface ParameterRepository {

	/**
	 * Checks if the given parameter exists
	 * 
	 * @param domain
	 * @param parameter
	 * @return
	 */
	boolean parameterExists(Metadata metadata);

	/**
	 * Returns the current value of the parameter
	 * 
	 * @param domain
	 * @param parameterName
	 * @return
	 */
	String getValue(Metadata metadata);

	/**
	 * Modifies the current value of the parameter
	 * 
	 * @param domain
	 * @param parameterName
	 * @param value
	 */
	void setValue(Metadata metadata, String value);

	/**
	 * @return The way the repository can handle the specified domain NONE: if
	 *         it dont know about that domain, else the R/W mode
	 */
	RepositoryMode getMode(String domain);

}
