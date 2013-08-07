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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.ext4spring.parameter.annotation.Parameter;
import org.ext4spring.parameter.annotation.ParameterBean;
import org.ext4spring.parameter.annotation.ParameterQualifier;
import org.ext4spring.parameter.converter.Converter;
import org.ext4spring.parameter.model.Metadata;
import org.ext4spring.parameter.model.Operation;
import org.springframework.stereotype.Component;

@Component(SpringComponents.defaultParameterResolver)
public class DefaultParameterResolver implements ParameterResolver {

    // map for change primitives to objects
    private final Map<String, Class<?>> typeMap = new HashMap<String, Class<?>>();

    public DefaultParameterResolver() {
        typeMap.put("byte", Byte.class);
        typeMap.put("short", Short.class);
        typeMap.put("int", Integer.class);
        typeMap.put("long", Long.class);
        typeMap.put("float", Float.class);
        typeMap.put("double", Double.class);
        typeMap.put("boolean", Boolean.class);
        typeMap.put("char", Character.class);
    }

    @Override
    public Metadata parse(Method method, Object[] invocationArgumnets) {
        Metadata metadata = new Metadata();
        // domain
        metadata.setDomain(this.resolveDomain(method));
        // parameter name and operation
        String methodName = method.getName();
        if (methodName.startsWith("is")) {
            metadata.setOperation(Operation.GET);
            metadata.setParameter(this.resolveParameterName(method, "is"));
            metadata.setTypeClass(this.resolveParameterType(method));
        } else if (method.getName().startsWith("get")) {
            metadata.setOperation(Operation.GET);
            metadata.setParameter(this.resolveParameterName(method, "get"));
            metadata.setTypeClass(this.resolveParameterType(method));
        } else if (method.getName().startsWith("set")) {
            metadata.setOperation(Operation.SET);
            metadata.setParameter(this.resolveParameterName(method, "set"));
            metadata.setTypeClass(method.getParameterTypes()[0]);
        }
        metadata.setConverter(this.resolveConverter(method));
        metadata.setOptional(this.resolveOptional(method));
        metadata.setDefaultValue(this.resolveDefaultValue(method));
        metadata.setQualifier(this.resolveQualifier(method, invocationArgumnets));
        return metadata;
    }

    private String resolveQualifier(Method method, Object[] invocationArgumnets) {
        String qualifier = null;
        if (invocationArgumnets != null) {
            Annotation[][] paramAnnotations = method.getParameterAnnotations();
            for (int paramIndex = 0; paramIndex < paramAnnotations.length; paramIndex++) {
                {
                    for (Annotation annotation : paramAnnotations[paramIndex]) {
                        if (annotation instanceof ParameterQualifier) {
                            if (invocationArgumnets[paramIndex]!=null) {
                                qualifier=invocationArgumnets[paramIndex].toString();
                            }
                        }
                    }
                }
            }
        }
        return qualifier;
    }

    private boolean resolveOptional(Method method) {
        if (method.isAnnotationPresent(Parameter.class)) {
            return method.getAnnotation(Parameter.class).optional();
        }
        return false;
    }

    private Class<? extends Converter> resolveConverter(Method method) {
        if (method.isAnnotationPresent(Parameter.class)) {
            if (method.getAnnotation(Parameter.class).converter().length > 0) {
                return method.getAnnotation(Parameter.class).converter()[0];
            }
        }
        return null;
    }

    private String resolveDefaultValue(Method method) {
        if (method.isAnnotationPresent(Parameter.class)) {
            if (!method.getAnnotation(Parameter.class).defaultValue().equals(Parameter.UNDEFINED)) {
                return method.getAnnotation(Parameter.class).defaultValue();
            }
        }
        return null;
    }

    private String resolveParameterName(Method method, String prefix) {
        String name = null;
        if (method.isAnnotationPresent(Parameter.class) && !method.getAnnotation(Parameter.class).name().equals(Parameter.UNDEFINED)) {
            name = method.getAnnotation(Parameter.class).name();
        } else {
            name = method.getName().substring(prefix.length());
        }
        return name;
    }

    private Class<?> resolveParameterType(Method method) {
        Class<?> type = method.getReturnType();
        if (type.isPrimitive()) {
            return this.typeMap.get(type.getName());
        } else {
            return type;
        }
    }

    private String resolveDomain(Method method) {
        String domain;
        if (method.getDeclaringClass().isAnnotationPresent(ParameterBean.class) && !ParameterBean.UNDEFINED.equals(method.getDeclaringClass().getAnnotation(ParameterBean.class).domain())) {
            domain = method.getDeclaringClass().getAnnotation(ParameterBean.class).domain();
        } else {
            domain = method.getDeclaringClass().getCanonicalName();
        }
        return domain;
    }
}
