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

import org.ext4spring.parameter.example.ApplicationSettings;
import org.junit.Assert;

public class TestUtil {
    /**
     * Validates if the values returned by getters are the ones that are int the test parameter stores and the default
     * values
     * 
     * @param applicationSettings
     * @return
     */
    public static void assertApplicationSettingsValid(ApplicationSettings applicationSettings) {
        Assert.assertEquals(ApplicationSettings.NAME, applicationSettings.getName());
        Assert.assertEquals(ApplicationSettings.RELEAS_DATE, applicationSettings.getReleaseDate());
        // default value by annotation
        Assert.assertEquals(ApplicationSettings.PRICE, applicationSettings.getPrice(), 0.0);
        // default value by return value
        Assert.assertEquals(ApplicationSettings.SIZE, applicationSettings.getSize());
        Assert.assertEquals(ApplicationSettings.SUPPORTED, applicationSettings.isSupported());
        Assert.assertEquals(ApplicationSettings.XML_DATA, applicationSettings.getXmlConfig());
        Assert.assertEquals(ApplicationSettings.USER_COLOR_DEFAULT, applicationSettings.getUserColor("default"));
    }

    public static void assertQualifiedApplicationSettingsValid(ApplicationSettings applicationSettings) {
        Assert.assertEquals(ApplicationSettings.USER_COLOR_DEFAULT, applicationSettings.getUserColor("default"));
        Assert.assertEquals(ApplicationSettings.USER_COLOR_USER1, applicationSettings.getUserColor("user1"));
        Assert.assertEquals(ApplicationSettings.USER_COLOR_USER2, applicationSettings.getUserColor("user2"));
    }

}
