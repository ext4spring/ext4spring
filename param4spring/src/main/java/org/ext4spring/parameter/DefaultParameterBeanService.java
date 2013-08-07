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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ext4spring.parameter.exception.ParameterException;
import org.ext4spring.parameter.model.Metadata;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DefaultParameterBeanService implements ParameterBeanService, ApplicationContextAware {

	private static final Log LOGGER = LogFactory.getLog(DefaultParameterBeanService.class);

	private ParameterResolver parameterResolver;
	private ParameterService parameterService;
	private ApplicationContext applicationContext;

	private List<Field> getSupportedFields(Class<?> clazz) {
		List<Field> supportedFields=new ArrayList<Field>();
		for (Field field : clazz.getDeclaredFields()) {
			int modifiers=field.getModifiers();
			if (!Modifier.isFinal(modifiers) && !Modifier.isStatic(modifiers)) {
				supportedFields.add(field);
			}
		}
		return supportedFields;
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public <T> T readParameterBean(Class<T> typeClass) throws ParameterException {
		LOGGER.debug("Reading parameters for class:" + typeClass);
		T paramterBean;
		try {
			paramterBean = typeClass.newInstance();
			for (Field field : this.getSupportedFields(typeClass)) {
					// find getter for field;
					for (Method method : paramterBean.getClass().getMethods()) {
						if ((method.getName().startsWith("get") || method.getName().startsWith("is"))
								&& method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
							// set value from repository
							field.setAccessible(true);
							//TODO: support qualifiers for full bean queries
							Metadata metadata = this.parameterResolver.parse(method,null);
							Object value = this.parameterService.read(metadata, field.get(paramterBean));
							field.set(paramterBean, value);
							field.setAccessible(false);
						}
					}
			}
			return paramterBean;
		} catch (Exception e) {
			LOGGER.error("Error happened while reading parameter bean:" + typeClass + "." + e, e);
			throw new ParameterException("Error happened while reading parameter bean:" + typeClass + "." + e, e);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
	public <T> void writeParameterBean(T parameterBean) {
		LOGGER.debug("Writing parameters for bean:" + parameterBean);
		try {
			for (Field field : this.getSupportedFields(parameterBean.getClass())) {
				// find getter for field;
				for (Method method : parameterBean.getClass().getMethods()) {
					if (method.getName().startsWith("set") && method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
						// save value to repository
						field.setAccessible(true);
						Metadata metadata = this.parameterResolver.parse(method,null);
						this.parameterService.write(metadata, field.get(parameterBean));
						field.setAccessible(false);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error happened while writing parameter bean:" + parameterBean + "." + e, e);
			throw new ParameterException("Error happened while writing parameter bean:" + parameterBean + "." + e, e);
		}
	}

	public void setParameterResolver(ParameterResolver parameterResolver) {
		this.parameterResolver = parameterResolver;
	}

	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@PostConstruct
	public void init() {
		if (this.parameterResolver == null) {
			this.parameterResolver = (ParameterResolver) this.applicationContext.getBean(SpringComponents.defaultParameterResolver);
		}
		if (this.parameterService == null) {
			this.parameterService = this.applicationContext.getBean(ParameterService.class);
		}
	}

}
