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

import java.util.LinkedHashSet;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ext4spring.parameter.converter.ConverterFactory;
import org.ext4spring.parameter.dao.ParameterRepository;
import org.ext4spring.parameter.exception.ParameterConverterException;
import org.ext4spring.parameter.exception.ParameterException;
import org.ext4spring.parameter.exception.ParameterUndefinedException;
import org.ext4spring.parameter.exception.RepositoryNotFoundException;
import org.ext4spring.parameter.model.Metadata;
import org.ext4spring.parameter.model.RepositoryMode;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultParameterService implements ParameterService, ApplicationContextAware {

    private static final Log LOGGER = LogFactory.getLog(DefaultParameterService.class);

    private ApplicationContext applicationContext;
    private ConverterFactory converterFactory;

    private LinkedHashSet<ParameterRepository> repositories;

    @Override
    @Cacheable(value = Cache.CACHE_REGION, key = "#metadata")
    public Object read(Metadata metadata, Object methodReturnValue) {
        LOGGER.debug("Reading parameter:" + metadata);
        Object value = null;
        ParameterRepository repository = this.getReadableRepository(metadata);
        if (repository != null) {
            String stringValue = repository.getValue(metadata);
            if (stringValue != null) {
                if (metadata.getConverter() == null) {
                    // value from repository			    
                    value = this.converterFactory.getConverter(metadata.getTypeClass()).toTypedValue(stringValue, metadata.getTypeClass());
                } else {
                    try {
                        value = metadata.getConverter().newInstance().toTypedValue(stringValue, metadata.getTypeClass());
                    } catch (InstantiationException e) {
                        throw new ParameterConverterException(e);
                    } catch (IllegalAccessException e) {
                        throw new ParameterConverterException(e);
                    }
                }
            }
        } else {
            LOGGER.warn("No repository found for: " + metadata);
        }
        if (value == null) {
            if (metadata.getDefaultValue() != null && metadata.getDefaultValue().length() > 0) {
                // default by annotation
                value = this.converterFactory.getConverter(metadata.getTypeClass()).toTypedValue(metadata.getDefaultValue(), metadata.getTypeClass());
            } else {
                // default by real method invocation of parameter bean
                value = methodReturnValue;
            }
        }
        if (value==null && !metadata.isOptional()) {
            throw new ParameterUndefinedException("Parameter not found in any repositories and doesn't have a default value:"+metadata+". Set value or mark as optional.");
        }
        return value;
    }

    @Override
    @CacheEvict(value = Cache.CACHE_REGION, key = "#metadata")
    public void write(Metadata metadata, Object value) {
        ParameterRepository repository = this.getWriteableRepository(metadata);
        if (repository != null) {
            if (metadata.getConverter() == null) {
                repository.setValue(metadata, this.converterFactory.getConverter(metadata.getTypeClass()).toStringValue(value));
            } else {
                //TODO test
                try {
                    repository.setValue(metadata, metadata.getConverter().newInstance().toStringValue(value));
                } catch (InstantiationException e) {
                    throw new ParameterConverterException(e);
                } catch (IllegalAccessException e) {
                    throw new ParameterConverterException(e);
                }
            }

        } else {
            LOGGER.error("Not writeable repository found for:" + metadata);
            throw new RepositoryNotFoundException("No writeable repository found for:" + metadata);

        }
    }

    @Required
    public void setParameterRepositories(LinkedHashSet<ParameterRepository> repositories) {
        this.repositories = repositories;
    }

    protected ParameterRepository getReadableRepository(Metadata metadata) throws ParameterException {
        boolean domainFound = false;
        for (ParameterRepository repository : this.repositories) {
            if (repository.getMode(metadata.getDomain()) != RepositoryMode.NONE) {
                domainFound = true;
                if (repository.parameterExists(metadata)) {
                    return repository;
                }
            }
        }
        if (!domainFound) {
            throw new RepositoryNotFoundException("No repository found for:" + metadata);
        }
        return null;
    }

    protected ParameterRepository getWriteableRepository(Metadata metadata) throws ParameterException {
        for (ParameterRepository repository : this.repositories) {
            switch (repository.getMode(metadata.getDomain())) {
                case WRITE_ALL:
                    return repository;
                case WRITE_EXISTING:
                    if (repository.parameterExists(metadata)) {
                        return repository;
                    }
                    break;
                case READ_ONLY:
                    if (repository.parameterExists(metadata)) {
                        //the parameter had been read from a read only repo, so it cannot be saved
                        throw new ParameterException("The parameter had been read from a read only repo, so it cannot be saved. Repository:" + repository + " Parameter:" + metadata);
                    }
                    break;
                case NONE:
                    break;
                default:
                    break;
            }
        }
        return null;
    }

    public void setConverterFactory(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        if (this.converterFactory == null) {
            // use the default if not set manually
            this.converterFactory = (ConverterFactory) this.applicationContext.getBean(SpringComponents.defaultConverterFactory);
        }
    }

}
