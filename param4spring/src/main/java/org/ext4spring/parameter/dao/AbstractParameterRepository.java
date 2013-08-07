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

import java.util.LinkedHashMap;
import java.util.Map;

import org.ext4spring.parameter.model.RepositoryMode;

public abstract class AbstractParameterRepository implements
		ParameterRepository {

	private final LinkedHashMap<String, RepositoryMode> handledDomains = new LinkedHashMap<String, RepositoryMode>();

	public void setHandledDomains(Map<String, String> handledDomainsConfig) {
		for (String domain : handledDomainsConfig.keySet()) {
			this.handledDomains.put(domain,
					RepositoryMode.valueOf(handledDomainsConfig.get(domain)));
		}
	}

	@Override
	public RepositoryMode getMode(String domain) {
		if (handledDomains != null && handledDomains.containsKey(domain)) {
			return handledDomains.get(domain);
		}
		return RepositoryMode.NONE;
	}
}
