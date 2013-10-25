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
package org.ext4spring.parameter;

import java.lang.reflect.Method;

import org.ext4spring.parameter.annotation.Parameter;
import org.ext4spring.parameter.annotation.ParameterBean;
import org.ext4spring.parameter.annotation.ParameterQualifier;
import org.ext4spring.parameter.converter.tv.TVConverter;
import org.ext4spring.parameter.model.Metadata;
import org.ext4spring.parameter.model.Operation;
import org.junit.Assert;
import org.junit.Test;

public class DefaultParameterResolverTest extends TestBase {

    @SuppressWarnings("unused")
    private class AllPrimitivesParameterBean {
        public byte getByte() {
            return 0;
        }

        public short getShort() {
            return 0;
        }

        public int getInt() {
            return 0;
        }

        public long getLong() {
            return 0;
        }

        public boolean isBoolean() {
            return false;
        }

        public float getFloat() {
            return 0;
        }

        public double getDouble() {
            return 0;
        }

        public char getChar() {
            return 0;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testResolvePrimitiveTypesToTheirObjectPair() throws Exception {
        DefaultParameterResolver defaultParameterResolver = new DefaultParameterResolver();

        Class notAnnotatedClass = Class.forName(AllPrimitivesParameterBean.class.getName());
        Method method;
        Metadata metadata;

        method = notAnnotatedClass.getMethod("getByte");
        metadata = defaultParameterResolver.parse(method,null);
        Assert.assertEquals(Byte.class, metadata.getTypeClass());

        method = notAnnotatedClass.getMethod("getShort");
        metadata = defaultParameterResolver.parse(method,null);
        Assert.assertEquals(Short.class, metadata.getTypeClass());

        method = notAnnotatedClass.getMethod("getInt");
        metadata = defaultParameterResolver.parse(method,null);
        Assert.assertEquals(Integer.class, metadata.getTypeClass());

        method = notAnnotatedClass.getMethod("getLong");
        metadata = defaultParameterResolver.parse(method,null);
        Assert.assertEquals(Long.class, metadata.getTypeClass());

        method = notAnnotatedClass.getMethod("isBoolean");
        metadata = defaultParameterResolver.parse(method,null);
        Assert.assertEquals(Boolean.class, metadata.getTypeClass());

        method = notAnnotatedClass.getMethod("getFloat");
        metadata = defaultParameterResolver.parse(method,null);
        Assert.assertEquals(Float.class, metadata.getTypeClass());

        method = notAnnotatedClass.getMethod("getDouble");
        metadata = defaultParameterResolver.parse(method,null);
        Assert.assertEquals(Double.class, metadata.getTypeClass());

        method = notAnnotatedClass.getMethod("getChar");
        metadata = defaultParameterResolver.parse(method,null);
        Assert.assertEquals(Character.class, metadata.getTypeClass());
    }

    @SuppressWarnings("unused")
    private class NotAnnotatedParameterBean {
        Long longParam;

        public Long getLongParam() {
            return longParam;
        }

        public void setLongParam(Long longParam) {
            this.longParam = longParam;
        }
    }

    @ParameterBean(domain = "testDomain")
    private class AnnotatedParameterBean {
        Long longParam;

        @Parameter(name = "longer")
        public Long getLongParam() {
            return longParam;
        }

        double doubleParam;

        @Parameter(name = "double", defaultValue = "10.5")
        public double getDoubleParam() {
            return doubleParam;
        }

        String optionalStringParam;

        @Parameter(optional = true)
        public String getOptionalStringParam() {
            return optionalStringParam;
        }
        
        @SuppressWarnings("unused")
        public String getQualifiedParam(@ParameterQualifier String qualifier) {
            return null;
        }
        
        @Parameter(converter=TVConverter.class, defaultValue="true", optional=true, name="differentName")
        public Boolean isValue() {
            return false;
        }
        
        @SuppressWarnings("unused")
        public void setValue(Boolean value) {
            
        }
    }

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testResolveWithoutAnnotationInfo() throws ClassNotFoundException, SecurityException, NoSuchMethodException {
        Class notAnnotatedClass = Class.forName(NotAnnotatedParameterBean.class.getName());
        // getter
        Method notAnnotatedGetLongMethod = notAnnotatedClass.getMethod("getLongParam");
        DefaultParameterResolver defaultParameterResolver = new DefaultParameterResolver();
        Metadata metadata = defaultParameterResolver.parse(notAnnotatedGetLongMethod,null);
        Assert.assertEquals(null, metadata.getDefaultValue());
        Assert.assertEquals(Long.class, metadata.getTypeClass());
        Assert.assertEquals(Operation.GET, metadata.getOperation());
        Assert.assertEquals("LongParam", metadata.getParameter());
        Assert.assertEquals("org.ext4spring.parameter.DefaultParameterResolverTest.NotAnnotatedParameterBean", metadata.getDomain());
        // setter
        Method notAnnotatedSetLongMethod = notAnnotatedClass.getMethod("setLongParam", Long.class);
        metadata = defaultParameterResolver.parse(notAnnotatedSetLongMethod,null);
        Assert.assertEquals(Operation.SET, metadata.getOperation());
        Assert.assertEquals("LongParam", metadata.getParameter());
    }

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testResolveWithAnnotationInfo() throws ClassNotFoundException, SecurityException, NoSuchMethodException {
        Class annotatedClass = Class.forName(AnnotatedParameterBean.class.getName());
        Method annotatedLongMethod = annotatedClass.getMethod("getLongParam");
        DefaultParameterResolver defaultParameterResolver = new DefaultParameterResolver();
        Metadata metadata = defaultParameterResolver.parse(annotatedLongMethod,null);
        Assert.assertEquals(null, metadata.getDefaultValue());
        Assert.assertEquals(Long.class, metadata.getTypeClass());
        Assert.assertEquals(Operation.GET, metadata.getOperation());
        Assert.assertEquals("longer", metadata.getParameter());
        // class annotations
        Assert.assertEquals("testDomain", metadata.getDomain());

        Method annotatedDoubleMethod = annotatedClass.getMethod("getDoubleParam");
        metadata = defaultParameterResolver.parse(annotatedDoubleMethod,null);
        Assert.assertEquals("10.5", metadata.getDefaultValue());
        Assert.assertEquals(Double.class, metadata.getTypeClass());

        Method annotatedOptionalMethod = annotatedClass.getMethod("getOptionalStringParam");
        metadata = defaultParameterResolver.parse(annotatedOptionalMethod,null);
        Assert.assertTrue(metadata.isOptional());
        Assert.assertEquals(String.class, metadata.getTypeClass());

        Method annotatedMethodWithQualifier = annotatedClass.getMethod("getQualifiedParam",String.class);
        metadata = defaultParameterResolver.parse(annotatedMethodWithQualifier,new Object[]{"q1"});
        Assert.assertEquals("QualifiedParam", metadata.getParameter());
        Assert.assertEquals("q1", metadata.getQualifier());
        Assert.assertEquals("QualifiedParam.q1", metadata.getFullParameterName());
    
        Method setterMethodWithAnnotationsOnGetter = annotatedClass.getMethod("setValue",Boolean.class);
        metadata = defaultParameterResolver.parse(setterMethodWithAnnotationsOnGetter, null);
        Assert.assertTrue(metadata.isOptional());
        Assert.assertEquals(Boolean.class, metadata.getTypeClass());
        Assert.assertEquals(TVConverter.class, metadata.getConverter());
        Assert.assertEquals("true", metadata.getDefaultValue());
        Assert.assertEquals("differentName", metadata.getParameter());
    
    }

}
