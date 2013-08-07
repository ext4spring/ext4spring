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

import javax.annotation.PostConstruct;

import org.ext4spring.parameter.aop.ParameterAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Creates an AOP proxied version of the parameter bean
 * 
 * @author Peter Borbas
 * 
 */
public class ParameterBeanFactory implements FactoryBean<Object>,
		ApplicationContextAware {

	private ApplicationContext applicationContext;
	private Object parameterBean;
	private ParameterAdvice parameterAdvice;

	@Override
	public Object getObject() throws Exception {
		ProxyFactory proxyFactory = new ProxyFactory(parameterBean);
		proxyFactory.addAdvice(parameterAdvice);
		proxyFactory.setProxyTargetClass(!parameterBean.getClass()
				.isInterface());
		return proxyFactory.getProxy();
	}

	@Override
	public Class<?> getObjectType() {
		return parameterBean.getClass();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Required
	public void setParameterBean(Object parameterBean) {
		this.parameterBean = parameterBean;
	}

	public void setParameterAdvice(ParameterAdvice parameterAdvice) {
		this.parameterAdvice = parameterAdvice;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@PostConstruct
	public void init() {
		if (this.parameterAdvice == null) {
			// try to use default
			this.parameterAdvice = (ParameterAdvice) this.applicationContext
					.getBean(SpringComponents.defaultParameterAdvice);
		}
	}
}
