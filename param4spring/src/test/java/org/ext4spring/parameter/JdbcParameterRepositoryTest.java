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

import java.util.Date;

import javax.sql.DataSource;

import org.ext4spring.parameter.converter.json.JSONConverter;
import org.ext4spring.parameter.example.ApplicationSettings;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testHsqlContext.xml")
public class JdbcParameterRepositoryTest extends TestBase {

    @Autowired
    ApplicationSettings applicationSettings;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void testReadExistingParameter() {
        JSONConverter converter = new JSONConverter();
        System.out.println(converter.toStringValue(new Date(1000)));

        TestUtil.assertApplicationSettingsValid(this.applicationSettings);
        TestUtil.assertQualifiedApplicationSettingsValid(applicationSettings);
   }

    @Test
    public void testWriteExistingParameter() {
        Assert.assertEquals("Application name", this.applicationSettings.getName());
        this.applicationSettings.setName("Changed name");
        this.applicationSettings.setReleaseDate(new Date(2000));
        Assert.assertEquals(1, jdbcTemplate.queryForInt("Select count(*) from parameters where data='\"Changed name\"'"));
        Assert.assertEquals(1, jdbcTemplate.queryForInt("Select count(*) from parameters where data='\"1970-01-01T00:00:02.000+0000\"'"));
        Assert.assertEquals("Changed name", this.applicationSettings.getName());
        Assert.assertEquals(new Date(2000), this.applicationSettings.getReleaseDate());
        this.applicationSettings.setName("Application name");
    }
}
