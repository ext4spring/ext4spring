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
package org.ext4spring.parameter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ext4spring.parameter.converter.Converter;

/**
 * Configures a single parameter
 * 
 * @author Peter Borbas
 * 
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Parameter {

	public static final String UNDEFINED = "__*%$#@-undefined_value";

	/**
	 * Overrides the default parameter name
	 * 
	 * @return
	 */
	String name() default UNDEFINED;

	/**
	 * Gives custom default value for the parameter if its not found in the
	 * repository
	 * 
	 * @return
	 */
	String defaultValue() default UNDEFINED;
	
	/**
	 * If specified, this converter class will be used to convert values instead of the one that ConverterFactory creates
	 * @return
	 */
	Class<? extends Converter>[] converter() default {};
	
	/**
	 * By default if its not in any of the repositories and the method returns null, it throws a ParameterException
	 * If its true, null default value is allowed
	 */
	boolean optional() default false;
}
