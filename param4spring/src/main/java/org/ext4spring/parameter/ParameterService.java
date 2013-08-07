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

import org.ext4spring.parameter.exception.ParameterException;
import org.ext4spring.parameter.model.Metadata;

/**
 * Parameter related services. Uses a ParameterRepository for persistence
 * 
 * @author Peter Borbas
 */

public interface ParameterService {

    public Object read(Metadata metadata, Object methodReturnValue) throws ParameterException;

    public void write(Metadata metadata, Object value) throws ParameterException;
}
