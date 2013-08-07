package org.ext4spring.parameter;

import org.junit.BeforeClass;

public class TestBase {
	@BeforeClass
	public static void initLogger() {
		org.apache.log4j.BasicConfigurator.resetConfiguration();
		org.apache.log4j.BasicConfigurator.configure();
	}

}
