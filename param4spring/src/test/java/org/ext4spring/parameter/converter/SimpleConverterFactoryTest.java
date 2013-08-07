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
package org.ext4spring.parameter.converter;

import org.ext4spring.parameter.converter.simple.SimpleConverterFactory;
import org.ext4spring.parameter.exception.ParameterConverterException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testAutoproxyContext.xml")
public class SimpleConverterFactoryTest {

	@Autowired
	SimpleConverterFactory autowiredConverterFactory;

	@Test
	public void testGetTheRightConverterForSupportedTypes() {
		Assert.assertTrue(Integer.class.getName().equals(
				autowiredConverterFactory.getConverter(Integer.class)
						.getHandledClass().getName()));
		Assert.assertTrue(Long.class.getName().equals(
				autowiredConverterFactory.getConverter(Long.class)
						.getHandledClass().getName()));
		Assert.assertTrue(Float.class.getName().equals(
				autowiredConverterFactory.getConverter(Float.class)
						.getHandledClass().getName()));
		Assert.assertTrue(Double.class.getName().equals(
				autowiredConverterFactory.getConverter(Double.class)
						.getHandledClass().getName()));
		Assert.assertTrue(String.class.getName().equals(
				autowiredConverterFactory.getConverter(String.class)
						.getHandledClass().getName()));
		try {
			autowiredConverterFactory.getConverter(StringBuffer.class);
			Assert.assertTrue(
					"converter factory should throw exception when converter not found",
					false);
		} catch (ParameterConverterException e) {
			// should happen
		}
	}

}
