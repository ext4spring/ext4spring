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
package org.ext4spring.parameter.converter.simple;

import org.ext4spring.parameter.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CharacterConverter implements Converter {

	@Override
	public String toStringValue(Object typedValue) {
		return (typedValue != null) ? typedValue.toString() : "";
	}

	@Override
	public <T> T toTypedValue(String stringValue, Class<T> type) {
		return (stringValue != null) ? type.cast(Character.valueOf(stringValue
				.charAt(0))) : null;
	}

	@Override
	public Class<Character> getHandledClass() {
		return Character.class;
	}
}
