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

import java.util.Map;

import org.ext4spring.parameter.exception.ParameterException;

/**
 * Service to handle parameter beans without AOP
 * 
 * @author pborbas
 */
public interface ParameterBeanService {
    /**
     * Reads the parameter bean values from the configured repositories and populates the values into the bean fields
     * 
     * @param typeClass
     * @throws ParameterException
     */
    <T> T readParameterBean(Class<T> typeClass) throws ParameterException;

    /**
     * Reads the parameter bean, that has qualifier values from the configured repositories and populates the values
     * into the bean fields. The qualified parameter have to be java.util.Map. The Map keys will be the parameterQualifiers values.
     * 
     * @param typeClass
     * @param parameterQualifiers list of qualifiers that will be read from the repositories for @ParameterQualifier annotated methods.
     * @throws ParameterException
     */
    <T> T readParameterBean(Class<T> typeClass, String... parameterQualifiers) throws ParameterException;

    /**
     * Writes the parameter bean values into the underlying repositories.
     * @param parameterBean
     * @throws ParameterException
     */
    <T> void writeParameterBean(T parameterBean) throws ParameterException;
}
