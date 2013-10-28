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
            metadata.setAttribute(this.resolveAttributeName(method, "is", Operation.GET));
            metadata.setParameter(this.resolveParameterName(method, "is", Operation.GET));
            metadata.setTypeClass(this.resolveParameterType(method));
        } else if (method.getName().startsWith("get")) {
            metadata.setOperation(Operation.GET);
            metadata.setAttribute(this.resolveAttributeName(method, "get", Operation.GET));
            metadata.setParameter(this.resolveParameterName(method, "get", Operation.GET));
            metadata.setTypeClass(this.resolveParameterType(method));
        } else if (method.getName().startsWith("set")) {
            metadata.setOperation(Operation.SET);
            metadata.setAttribute(this.resolveAttributeName(method, "set", Operation.SET));
            metadata.setParameter(this.resolveParameterName(method, "set", Operation.SET));
            metadata.setTypeClass(method.getParameterTypes()[0]);
        }
        metadata.setConverter(this.resolveConverter(method, metadata.getOperation(), metadata.getAttribute()));
        metadata.setOptional(this.resolveOptional(method, metadata.getOperation(), metadata.getAttribute()));
        metadata.setDefaultValue(this.resolveDefaultValue(method, metadata.getOperation(), metadata.getAttribute()));
        this.resolveQualifier(metadata, method, invocationArgumnets);
        return metadata;
    }

    protected <T extends Annotation> T findAnnotation(Class<T> annotationType, Method method, Operation operation, String paramName) {
        T annotation = method.getAnnotation(annotationType);
        if (annotation == null) {
            //not found on the specified method. for setters it looks for getter annotations
            if (Operation.SET.equals(operation)) {
                for (Method currMethod : method.getDeclaringClass().getMethods()) {
                    if (currMethod.getName().endsWith(paramName)) {
                        if (currMethod.getAnnotation(annotationType) != null) {
                            return currMethod.getAnnotation(annotationType);
                        }
                    }
                }
            }
        }
        return annotation;
    }

    protected void resolveQualifier(Metadata metadata, Method method, Object[] invocationArgumnets) {
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        for (int paramIndex = 0; paramIndex < paramAnnotations.length; paramIndex++) {
            {
                for (Annotation annotation : paramAnnotations[paramIndex]) {
                    if (annotation instanceof ParameterQualifier) {
                        metadata.setQualified(true);
                        if (invocationArgumnets != null && invocationArgumnets[paramIndex] != null) {
                            metadata.setQualifier(invocationArgumnets[paramIndex].toString());
                        }
                    }
                }
            }
        }
    }

    private boolean resolveOptional(Method method, Operation operation, String attributeName) {
        Parameter parameterAnnotation = this.findAnnotation(Parameter.class, method, operation, attributeName);
        if (parameterAnnotation != null) {
            return parameterAnnotation.optional();
        }
        return false;
    }

    private Class<? extends Converter> resolveConverter(Method method, Operation operation, String attributeName) {
        Parameter parameterAnnotation = this.findAnnotation(Parameter.class, method, operation, attributeName);
        if (parameterAnnotation != null && parameterAnnotation.converter().length > 0) {
            return parameterAnnotation.converter()[0];
        }
        return null;
    }

    private String resolveDefaultValue(Method method, Operation operation, String attributeName) {
        Parameter parameterAnnotation = this.findAnnotation(Parameter.class, method, operation, attributeName);
        if (parameterAnnotation != null && !parameterAnnotation.defaultValue().equals(Parameter.UNDEFINED)) {
            return parameterAnnotation.defaultValue();
        }
        return null;
    }

    private String resolveAttributeName(Method method, String prefix, Operation operation) {
        return method.getName().substring(prefix.length());
    }

    private String resolveParameterName(Method method, String prefix, Operation operation) {
        String name = this.resolveAttributeName(method, prefix, operation);
        Parameter parameterAnnotation = this.findAnnotation(Parameter.class, method, operation, name);
        if (parameterAnnotation != null && !parameterAnnotation.name().equals(Parameter.UNDEFINED)) {
            name = parameterAnnotation.name();
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
