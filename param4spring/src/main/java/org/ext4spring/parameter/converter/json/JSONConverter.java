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

import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.ext4spring.parameter.converter.Converter;
import org.ext4spring.parameter.exception.ParameterConverterException;

public class JSONConverter implements Converter {

    private final ObjectMapper mapper;

    public JSONConverter() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        this.mapper.getSerializationConfig().disable(org.codehaus.jackson.map.SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.mapper.getSerializationConfig().setDateFormat(dateFormat);
        this.mapper.getDeserializationConfig().setDateFormat(dateFormat);
    }

    public void setJacksonFeature(Feature feature, boolean state) {
        this.mapper.configure(feature, state);
    }

    @Override
    public Class<Object> getHandledClass() {
        return Object.class;
    }

    @Override
    public <T> T toTypedValue(String stringValue, Class<T> type) {
        try {
            return mapper.readValue(stringValue, type);
        } catch (Exception e) {
            throw new ParameterConverterException(e);
        }
    }

    @Override
    public String toStringValue(Object typedValue) {
        try {
            return mapper.writeValueAsString(typedValue);
        } catch (Exception e) {
            throw new ParameterConverterException(e);
        }
    }
}
