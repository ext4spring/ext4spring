package org.ext4spring.parameter.converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.ext4spring.parameter.exception.ParameterConverterException;

public class PropertiesConverter implements Converter {

    @Override
    public Class<?> getHandledClass() {
        return Properties.class;
    }

    @Override
    public String toStringValue(Object typedValue) {
        try {
            Properties properties = (Properties) typedValue;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                properties.store(baos, "writen by param4spring");
                return baos.toString();
            } finally {
                baos.close();
            }
        } catch (IOException e) {
            throw new ParameterConverterException("Error while converting Properties to String:" + e, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T toTypedValue(String stringValue, Class<T> type) {
        try {
            Properties properties = new Properties();
            ByteArrayInputStream bais = new ByteArrayInputStream(stringValue.getBytes());
            properties.load(bais);
            return (T) properties;
        } catch (IOException e) {
            throw new ParameterConverterException("Error while converting Properties to type:" + e, e);
        }
    }
}
