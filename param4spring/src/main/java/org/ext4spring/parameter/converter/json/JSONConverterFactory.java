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
package org.ext4spring.parameter.converter.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.JsonParser.Feature;
import org.ext4spring.parameter.SpringComponents;
import org.ext4spring.parameter.converter.Converter;
import org.ext4spring.parameter.converter.ConverterFactory;
import org.ext4spring.parameter.exception.ParameterConverterException;
import org.springframework.stereotype.Component;

@Component(SpringComponents.defaultConverterFactory)
public class JSONConverterFactory implements ConverterFactory {

	private DateFormat dateFormat;
	private Map<String, Boolean> jacksonFeatures;

	private JSONConverter converter=new JSONConverter();

	public void setDateFormat(String pattern) {
		this.dateFormat = new SimpleDateFormat(pattern);
	}

	public void setJacksonFeatures(Map<String, Boolean> jacksonFeatures) {
		this.jacksonFeatures = jacksonFeatures;
	}

	@PostConstruct
	public void init() {		
		// TODO: test date and feature settings
		if (this.dateFormat != null) {
			this.converter.setDateFormat(this.dateFormat);
		}
		if (this.jacksonFeatures != null) {
			for (String featureName : jacksonFeatures.keySet()) {
				this.converter.setJacksonFeature(Feature.valueOf(featureName),
						this.jacksonFeatures.get(featureName));
			}
		}
	}

	@Override
	public Converter getConverter(Class<?> clazz)
			throws ParameterConverterException {
		return this.converter;
	}

}
