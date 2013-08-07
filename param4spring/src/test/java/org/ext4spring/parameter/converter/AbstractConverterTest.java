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
package org.ext4spring.parameter.converter;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ext4spring.parameter.TestBase;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractConverterTest extends TestBase {
    private static final Log LOGGER = LogFactory.getLog(AbstractConverterTest.class);

    protected abstract ConverterFactory getConverterFactory();

    /**
     * All converter should support these
     * @return
     */
    protected Map<Class<?>, Object> getAutotestedTypes() {
        Map<Class<?>, Object> types=new LinkedHashMap<Class<?>, Object>();
        types.put(Long.class, 2352351241241241242L);
        types.put(Short.class, (short)123);
        types.put(Integer.class, (int)252352);
        types.put(Double.class, 252352.112412d);
        types.put(Float.class, 252352.112412f);
        types.put(Byte.class, (byte)127);
        
        types.put(String.class, "Árvíztűrő tükörfúrógép");
        return types;
    }
    
    protected void testConversion(Class<?> type, Object value) {
        Converter converter=this.getConverterFactory().getConverter(type);
        String stringValue=converter.toStringValue(value);
        Object typedValue=converter.toTypedValue(stringValue, type);
        Assert.assertEquals(typedValue, value);
        Assert.assertEquals(stringValue, converter.toStringValue(typedValue));
     }
    
    
    @Test
    public void testRepeatableConversion() {
        Map<Class<?>, Object> types=this.getAutotestedTypes();
        for (Class<?> type:types.keySet()) {
            LOGGER.info("testing type:"+type+" with object:"+types.get(type));
            this.testConversion(type, types.get(type));
        }
    }
}
