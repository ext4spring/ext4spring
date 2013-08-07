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

import java.util.List;

import org.ext4spring.parameter.converter.Converter;
import org.ext4spring.parameter.converter.json.JSONConverter;
import org.ext4spring.parameter.converter.json.JSONConverterFactory;
import org.junit.Assert;
import org.junit.Test;

public class JSONConversionTest extends AbstractConverterTest {

    @Override
    protected ConverterFactory getConverterFactory() {
        return new JSONConverterFactory();
    }

    private Converter converter = new JSONConverter();

    @Test
    public void testStringConverter() {
        String stringValue = "\"afewiwhiwehfewf\"";
        Assert.assertEquals(stringValue, converter.toStringValue(converter.toTypedValue(stringValue, String.class)));
    }

    @Test
    public void testStringListConverter() {
        String stringValue = "[\"afewiwhiwehfewf\",\"hfwhfugh3gh\"]";
        Assert.assertEquals(stringValue, converter.toStringValue(converter.toTypedValue(stringValue, List.class)));
    }

    @Test
    public void testShortConverter() {
        String stringValue = "145";
        Assert.assertEquals(stringValue, converter.toStringValue(converter.toTypedValue(stringValue, Short.class)));
    }

    @Test
    public void testLongConverter() {
        String stringValue = "1455253235";
        Assert.assertEquals(stringValue, converter.toStringValue(converter.toTypedValue(stringValue, Long.class)));
    }

    @Test
    public void testIntegerConverter() {
        String stringValue = "245234324";
        Assert.assertEquals(stringValue, converter.toStringValue(converter.toTypedValue(stringValue, Integer.class)));
    }

    @Test
    public void testByteConverter() {
        String stringValue = "127";
        Assert.assertEquals(stringValue, converter.toStringValue(converter.toTypedValue(stringValue, Byte.class)));
    }

    @Test
    public void testFloatConverter() {
        String stringValue = "145.4647";
        Assert.assertEquals(stringValue, converter.toStringValue(converter.toTypedValue(stringValue, Float.class)));
    }

    @Test
    public void testDoubleConverter() {
        String stringValue = "145.4647";
        Assert.assertEquals(stringValue, converter.toStringValue(converter.toTypedValue(stringValue, Double.class)));
    }

    @Test
    public void testCharacterConverter() {
        String stringValue = "\"c\"";
        Assert.assertEquals(stringValue, converter.toStringValue(converter.toTypedValue(stringValue, Character.class)));
    }
}
