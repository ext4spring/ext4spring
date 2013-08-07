package org.ext4spring.parameter.converter;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

public class PropertiesConverterTest {

    @Test
    public void testConversion() throws Exception {

        PropertiesConverter propertiesConverter=new PropertiesConverter();
        
        String propertiesString="#this is a comment \nproperty.first=firstValue\nproperty.second=secondValue";
        Properties properties=propertiesConverter.toTypedValue(propertiesString, Properties.class);
        Assert.assertEquals("firstValue", properties.getProperty("property.first"));
        Assert.assertEquals("secondValue", properties.getProperty("property.second"));
        
        propertiesString=propertiesConverter.toStringValue(properties);
        properties=propertiesConverter.toTypedValue(propertiesString, Properties.class);
        Assert.assertEquals("firstValue", properties.getProperty("property.first"));
        Assert.assertEquals("secondValue", properties.getProperty("property.second"));
                
    }
}
