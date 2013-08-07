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

import org.ext4spring.parameter.converter.Converter;
import org.ext4spring.parameter.converter.simple.ByteConverter;
import org.ext4spring.parameter.converter.simple.CharacterConverter;
import org.ext4spring.parameter.converter.simple.DoubleConverter;
import org.ext4spring.parameter.converter.simple.FloatConverter;
import org.ext4spring.parameter.converter.simple.IntegerConverter;
import org.ext4spring.parameter.converter.simple.LongConverter;
import org.ext4spring.parameter.converter.simple.ShortConverter;
import org.ext4spring.parameter.converter.simple.SimpleConverterFactory;
import org.ext4spring.parameter.converter.simple.StringConverter;
import org.junit.Assert;
import org.junit.Test;

public class SimpleConversionTest {

    @Test
    public void testStringConverter() {
        Converter converter = new StringConverter();
        Assert.assertEquals("wfwkfjwefj", converter.toTypedValue("wfwkfjwefj", String.class));
        Assert.assertEquals("145", converter.toStringValue(new String("145")));
    }

    @Test
    public void testShortConverter() {
        Converter converter = new ShortConverter();
        Assert.assertEquals(new Short("145"), converter.toTypedValue("145", Short.class));
        Assert.assertEquals("145", converter.toStringValue(new Short("145")));
    }

    @Test
    public void testLongConverter() {
        Converter converter = new LongConverter();
        Assert.assertEquals(new Long("145"), converter.toTypedValue("145", Long.class));
        Assert.assertEquals("145", converter.toStringValue(new Long("145")));
    }

    @Test
    public void testIntegerConverter() {
        Converter converter = new IntegerConverter();
        Assert.assertEquals(new Integer("145"), converter.toTypedValue("145", Integer.class));
        Assert.assertEquals("145", converter.toStringValue(new Integer("145")));
    }

    @Test
    public void testByteConverter() {
        Converter converter = new ByteConverter();
        Assert.assertEquals(new Byte("127"), converter.toTypedValue("127", Byte.class));
        Assert.assertEquals("127", converter.toStringValue(new Byte("127")));
    }

    @Test
    public void testFloatConverter() {
        Converter converter = new FloatConverter();
        Assert.assertEquals(new Float("145.523"), converter.toTypedValue("145.523", Float.class));
        Assert.assertEquals("145.523", converter.toStringValue(new Float("145.523")));
    }

    @Test
    public void testDoubleConverter() {
        Converter converter = new DoubleConverter();
        Assert.assertEquals(new Double("145.523"), converter.toTypedValue("145.523", Double.class));
        Assert.assertEquals("145.523", converter.toStringValue(new Double("145.523")));
    }

    @Test
    public void testCharacterConverter() {
        Converter converter = new CharacterConverter();
        Assert.assertEquals(Character.valueOf("c".charAt(0)), converter.toTypedValue("c", Character.class));
        Assert.assertEquals("c", converter.toStringValue(Character.valueOf("c".charAt(0))));
    }
}
