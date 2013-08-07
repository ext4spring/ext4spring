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
package org.ext4spring.parameter.aop;

import javax.annotation.PostConstruct;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ext4spring.parameter.ParameterResolver;
import org.ext4spring.parameter.ParameterService;
import org.ext4spring.parameter.SpringComponents;
import org.ext4spring.parameter.model.Metadata;
import org.ext4spring.parameter.model.Operation;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * AOP advice for delegate GETTER and SETTER calls to the parameter manager. Can
 * pass as advice parameter for
 * {@link org.springframework.aop.support.DefaultPointcutAdvisor}
 * 
 * @author Peter Borbas
 * 
 */
@Component(SpringComponents.defaultParameterAdvice)
public class DefaultParameterAdvice implements ParameterAdvice,
		ApplicationContextAware {

	private static final Log LOGGER = LogFactory
			.getLog(DefaultParameterAdvice.class);

	private ParameterResolver parameterResolver;
	private ParameterService parameterService;
	private ApplicationContext applicationContext;

	public void setParameterManager(ParameterService parameterManager) {
		this.parameterService = parameterManager;
	}

	public void setParameterParser(ParameterResolver parameterResolver) {
		this.parameterResolver = parameterResolver;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		LOGGER.trace("Parameter Advice invoked for method:"
				+ invocation.getMethod());
		Metadata metadata = this.parameterResolver
				.parse(invocation.getMethod(),invocation.getArguments());
		if (Operation.GET.equals(metadata.getOperation())) {
			return parameterService.read(metadata, invocation.proceed());
		} else if (Operation.SET.equals(metadata.getOperation())) {
			invocation.proceed();
			parameterService.write(metadata, invocation.getArguments()[0]);
		}
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@PostConstruct
	public void init() {
		if (this.parameterResolver == null) {
			this.parameterResolver = (ParameterResolver) this.applicationContext
					.getBean(SpringComponents.defaultParameterResolver);
		}
		if (this.parameterService == null) {
			this.parameterService = this.applicationContext
					.getBean(ParameterService.class);
		}
	}
}
