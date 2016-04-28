package com.capgemini.executor;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class NewParallelExecutioner {

	@Parameters({ "host", "browser", "testName" })
	@Test
	public void executer(String host, String browser, String testName) {
		String[] parameters = { host, browser, testName };
		New_Executioner.testNG=true;
		New_Executioner.main(parameters);

	}

	
	@BeforeTest
	public void beforeTest() {
		System.out.println("running before test...");
	}

	@AfterTest
	public void afterTest() {
		System.out.println("running after test...");
	}

}
