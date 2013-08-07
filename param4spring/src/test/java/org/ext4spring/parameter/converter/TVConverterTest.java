package org.ext4spring.parameter.converter;

import org.ext4spring.parameter.converter.tv.TVConverterFactory;

public class TVConverterTest extends AbstractConverterTest {

    @Override
    protected ConverterFactory getConverterFactory() {
        return new TVConverterFactory();
    }
    

}
