package com.capgemini.scripts;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import jxl.read.biff.BiffException;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.capgemini.driver.CreateDriver;
import com.capgemini.driver.ScriptExecutor;
import com.capgemini.driver.StepExecutor;
import com.capgemini.executor.ExecutionRowNumber;
import com.capgemini.executor.New_Executioner;
import com.capgemini.executor.WriteMaster;
import com.capgemini.utilities.ReadExcel;
import com.capgemini.utilities.ReadPDF;
import com.capgemini.utilities.Reporter;
import com.capgemini.utilities.Utilities;
import com.capgemini.utilities.Verification;
import com.itextpdf.text.pdf.PdfFileSpecification;

import java.awt.AWTException; 
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.openqa.selenium.remote.DesiredCapabilities;

public class TWG_Connect {
	
	public String TestCase="TWG_Connect";
	DesiredCapabilities capabilities = new DesiredCapabilities();
	Reporter reporter = new Reporter(this.getClass().getSimpleName());
	CreateDriver driver = new CreateDriver();
	RemoteWebDriver webDriver = null;
	private Utilities utils = new Utilities(reporter);
	private ScriptExecutor scriptExecutor = new ScriptExecutor();
	private ExecutionRowNumber executionRowNumber = new ExecutionRowNumber();
	// Object for calling verification functions
	private Verification verify = new Verification(reporter);
	WebDriverWait wait = null;
	private StepExecutor stepExecutor = new StepExecutor(reporter);
	private String StrExecutionStartTime = null;
	private long executionStartTime = 0;
	Map<String, String> DataMap = new HashMap();
	Boolean sExecutionStatus;
	ReadExcel readExcel = new ReadExcel(reporter);
	ReadPDF readpdf = new ReadPDF(reporter);
	private Verification verification;
	private static String strAbsolutepath = new File("").getAbsolutePath();
	private static String strDataPath = strAbsolutepath + "/data/";
	public static String strDataFileName = strDataPath + "MasterSheet.xls";
	private boolean acceptNextAlert = true;
	int rownumber = 0;
	String strStopTime;
	String PdfFilePath =strDataPath;
	String typeofquote= null;
	public String getExecutionStartTime() {
		return StrExecutionStartTime;
	}

	public String getStartTime() {
		return String.valueOf(executionStartTime);
	}
	
	public void executeTestcase(String browserName) throws Exception {
		int iNumberOfRows = 0;
		readExcel.setInputFile(System.getProperty("File"));
		readExcel.setSheetName(TestCase);
		Map<Integer, Integer> seqMap = readExcel.getiNOfRowsSeq();
		iNumberOfRows = readExcel.getiNOfRows();
		
		reporter.start(reporter.calendar);
		StrExecutionStartTime = reporter.strStartTime;
		executionStartTime = reporter.startTime;
		
		reporter.ReportGenerator("Cafe#"+browserName);
		for (int i = 1; i <= iNumberOfRows; i++) {
			if(seqMap.get(i)==1){
			webDriver = driver.getWebDriver();
			wait = new WebDriverWait(webDriver, 10);
			readExcel.readByIndex(i);
			rownumber = i;
			String startesttime = GetTestStartTime();
			DataMap=readExcel.loadDataMap(rownumber);
			strDataFileName = strDataPath + "MasterSheet.xls";
			scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"StartTime",startesttime);
			System.out.println(capabilities.getBrowserName());
			reporter.setStrBrowser(capabilities.getBrowserName());
			reporter.addIterator(i);
			testcaseMain();
			//NextFunctionCall
			WriteMaster.updateNextURL(TestCase,webDriver.getCurrentUrl());
			reporter.closeIterator();
			System.out.println("\t \t \t \t \t Row number: " + i);
			webDriver.quit();
			strStopTime = reporter.stop();
			scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"EndTime",strStopTime);
			}
		}
		reporter.strStopTime = strStopTime;
		float timeElapsed = reporter.getElapsedTime();
		reporter.timeElapsed = timeElapsed;
		reporter.CreateSummary("Cafe#"+browserName);
		//System.exit(1);

	}
	
	public void executeTestcase(RemoteWebDriver rdriver,String host,String browser) throws Exception {		
		int iNumberOfRows = 0;
		readExcel.setInputFile(System.getProperty("File"));
		readExcel.setSheetName(TestCase);
		
		iNumberOfRows = readExcel.getiNOfRows();
		reporter.start(reporter.calendar);
		StrExecutionStartTime = reporter.strStartTime;
		executionStartTime = reporter.startTime;

		reporter.ReportGenerator(browser);
		
		for (int i = 1; i <= iNumberOfRows; i++) {
			rdriver = new CreateDriver().getWebDriver(host, browser);
			webDriver = rdriver;
			wait = new WebDriverWait(webDriver, 10);
			//readExcel.readByIndex(i);
			//webDriver.switchTo().activeElement();
			//System.out.println(capabilities.getBrowserName());
			//reporter.setStrBrowser(capabilities.getBrowserName());
			DataMap=readExcel.loadDataMap(i);
			reporter.addIterator(i);
			testcaseMain(); 
			//NextFunctionCall
			reporter.closeIterator();
			System.out.println("\t \t \t \t \t Row number: "+i);
			webDriver.quit();
		}
		
		String strStopTime = reporter.stop();
		reporter.strStopTime = strStopTime;
		float timeElapsed = reporter.getElapsedTime();
		reporter.timeElapsed = timeElapsed;
		reporter.CreateSummary(browser);
	}
	
	public void testcaseMain() throws InterruptedException, BiffException,Exception {
		
		stepExecutor.launchApplication("URL", DataMap, webDriver);

		try 
		{
			WebDriverWait wait = new WebDriverWait(webDriver, 60);
			// Code for textboxes starts
			stepExecutor.enterTextValue("findElementByName", "username", DataMap,  "username", webDriver,"TWG_Connect");
			// Code for textboxes starts
			stepExecutor.enterTextValue("findElementByName", "password", DataMap,  "password", webDriver,"TWG_Connect");
			// Code for buttons starts
			stepExecutor.clickButton("findElementByName", "submit", webDriver,"TWG_Connect");
			Thread.sleep(10000);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='navigation']/ul/li[7]/a/span")));
			//Click on Dealer select button button
			stepExecutor.clickLink("findElementByXPath", "//div[@id='navigation']/ul/li[7]/a/span", webDriver, TestCase);
		//	Thread.sleep(5000);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='_DealerSearch_WAR_DealerSearchPortlet_INSTANCE_Lchu_dealerNumber']")));
			//Enter Dealer Name and Dealer Number and click on dealer name searched
			stepExecutor.enterTextValue("findElementByXPath", "//input[@id='_DealerSearch_WAR_DealerSearchPortlet_INSTANCE_Lchu_dealerNumber']", DataMap,"Client", webDriver, TestCase);
		//	Thread.sleep(5000);	
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//input[@id='_DealerSearch_WAR_DealerSearchPortlet_INSTANCE_Lchu_dealerName']")));
			stepExecutor.enterTextValue("findElementByXPath", ".//input[@id='_DealerSearch_WAR_DealerSearchPortlet_INSTANCE_Lchu_dealerName']", DataMap,"DealerAccountName", webDriver, TestCase);
		//	Thread.sleep(5000);	
			wait.until(ExpectedConditions.elementToBeClickable(By.id("_DealerSearch_WAR_DealerSearchPortlet_INSTANCE_Lchu_search")));
			stepExecutor.clickButton("findElementById", "_DealerSearch_WAR_DealerSearchPortlet_INSTANCE_Lchu_search", webDriver,TestCase);
			Thread.sleep(5000);		
			String DealarNumber = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Client");
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'"+DealarNumber+"')]")));
			stepExecutor.clickLink("findElementByXPath", "//a[contains(text(),'"+DealarNumber+"')]", webDriver, TestCase);
			//Thread.sleep(1000);
			//Select 'sales' or 'quickquote' depending on user-input in datasheet
			typeofquote = scriptExecutor.readDataFile(strAbsolutepath, TestCase, rownumber, "SelectTypeOfQuote");
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='navigation']/ul/li[1]/a/span")));
			//Click on Sales/Quick quote Link
			if (typeofquote.contains("Sales"))
			{
				stepExecutor.clickLink("findElementByXPath", "//div[@id='navigation']/ul/li[1]/a/span", webDriver, TestCase);
			}
			if (typeofquote.contains("Quick Quote"))
			{
				stepExecutor.clickLink("findElementByXPath", "//div[@id='navigation']/ul/li[4]/a/span", webDriver, TestCase);
			}
			Thread.sleep(10000);
			//Select product and subproductandcontracttypes
			String ProductType = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "ProductToSelect");
			String SubProductTypeSelection = scriptExecutor.readDataFile(strDataFileName, TestCase,rownumber, "SubProductandContractTypes");
			//Code for checkboxes starts for Product selection for entered dealer in textbox
			if (FindDescendantsofCheckbox(ProductType, SubProductTypeSelection)){
				reporter.writeStepResult("Product selection", "Select Product",ProductType, "Pass", "Product selected", true, webDriver);
			}else{
				reporter.writeStepResult("Product selection", "Select Product",ProductType, "fail", "Product not selected", true, webDriver);
			}
			// Enter 17 characters VIN
			RadioButtonYesNoClick();
			
			stepExecutor.enterTextValue("findElementById","vehicleOdometer", DataMap,"Odometer", webDriver, TestCase);
			if (typeofquote.contains("Sales"))
			{
				stepExecutor.enterTextValue("findElementById","firstName", DataMap,"First Name", webDriver,  TestCase);
				stepExecutor.enterTextValue("findElementById","lastName", DataMap,"Last Name", webDriver,  TestCase);
			}	
			//Click on Get Rates button
			stepExecutor.clickButton("findElementByXPath", "//a[@id='getRatesButtonId']/span", webDriver,TestCase);
			String tmonths = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Terms (Months)");
			Thread.sleep(23000);
			String termsmonths = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Terms (Months)");
			if (termsmonths.contains(".0")){
				termsmonths = termsmonths.replace(".0","");
			}
			String miles = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Terms (Miles)");
			if (miles.contains(".0")){
				miles = miles.replace(".0","");
			}
			miles = miles +",000";
			String coverage = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Coverage");
			String Deductible = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Deductible");
			String price =scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "SRP Cost");
			price = price.replace(".00", "");
			String billedclientcost =scriptExecutor.readDataFile(strAbsolutepath, TestCase, rownumber, "Billed Client Cost");
			billedclientcost = billedclientcost.replace(".00","");
			String retailcost = null, dealercost = null;
			String retailcost2 = null, dealercost2 = null;
			if (typeofquote.contains("Quick Quote"))
			{
				coverage = SelectCoverage(coverage);
				String coverage_SCRN = webDriver.findElement(By.xpath("//*[contains(text(),'"+coverage+"')]")).getText();
				//coverage_SCRN = webDriver.findElement(By.xpath("//*[@id='_RatePortlet_WAR_RatePortlet_INSTANCE_pVDN_rateGridContainer']/div[2]/h1")).getText();
				System.out.println(coverage_SCRN);
				//	retailcost2 = webDriver.findElementByXPath(".//*[contains(text(),'"+coverage+"')]/parent::div/following-sibling::div//*[contains(text(),'"+miles+"')]/following-sibling::td[1]").getText();
				if (coverage_SCRN.contains("Platinum"))
				{
					coverage = SelectCoverage(coverage);
					retailcost = webDriver.findElementByXPath(".//*[text()='"+coverage+"']/parent::div/following-sibling::div//*[text()='"+miles+"']/following-sibling::td[1]").getText();
					dealercost = webDriver.findElementByXPath(".//*[text()='"+coverage+"']/parent::div/following-sibling::div//*[text()='"+miles+"']/following-sibling::td[2]").getText();
				}
				else if (coverage_SCRN.contains("Gold"))
				{
					coverage = SelectCoverage(coverage);
					retailcost = webDriver.findElementByXPath(".//*[text()='"+coverage+"']/parent::div/following-sibling::div//*[text()='"+miles+"']/following-sibling::td[1]").getText();
					dealercost = webDriver.findElementByXPath(".//*[text()='"+coverage+"']/parent::div/following-sibling::div//*[text()='"+miles+"']/following-sibling::td[2]").getText();
		
				}
				else if (coverage_SCRN.contains("Silver"))
				{
					coverage = SelectCoverage(coverage);
					retailcost = webDriver.findElementByXPath(".//*[text()='"+coverage+"']/parent::div/following-sibling::div//*[text()='"+miles+"']/following-sibling::td[1]").getText();
					dealercost = webDriver.findElementByXPath(".//*[text()='"+coverage+"']/parent::div/following-sibling::div//*[text()='"+miles+"']/following-sibling::td[2]").getText();
		
				}
				else if (coverage_SCRN.contains("Bronze"))
				{
					coverage = SelectCoverage(coverage);
					retailcost = webDriver.findElementByXPath(".//*[text()='"+coverage+"']/parent::div/following-sibling::div//*[text()='"+miles+"']/following-sibling::td[1]").getText();
					dealercost = webDriver.findElementByXPath(".//*[text()='"+coverage+"']/parent::div/following-sibling::div//*[text()='"+miles+"']/following-sibling::td[2]").getText();
		
				} else
				{
					retailcost = webDriver.findElementByXPath(".//*[text()='"+coverage+"']/parent::div/following-sibling::div//*[text()='"+miles+"']/following-sibling::td[1]").getText();
					dealercost = webDriver.findElementByXPath(".//*[text()='"+coverage+"']/parent::div/following-sibling::div//*[text()='"+miles+"']/following-sibling::td[2]").getText();

				}

				System.out.println(dealercost + retailcost); 
				if(dealercost.contains(","))
				{
					dealercost = dealercost.replace(",", "");
				}
				dealercost = dealercost.replace("$", "");
				if(retailcost.contains(","))
				{
					retailcost = retailcost.replace(",",""); 
				}
				retailcost = retailcost.replace("$","");
				retailcost = retailcost.trim();
				dealercost = dealercost.trim();
				scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "Connect_MSRP", retailcost);
				scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "Connect_DealerCost", dealercost);
				if (billedclientcost.equals(dealercost)) {
					reporter.writeStepResult(
							System.getProperty("Test_Scenario_Name"),
							"Verify dealer cost is present in the element", "Expected: "
									+ billedclientcost, "Pass", "Expected text  is present",
							true, webDriver);
					scriptExecutor.WriteExcelDataFile(strDataFileName,  TestCase,rownumber, "Connect_DealerCostResults", "PASS");
				} else {
					reporter.writeStepResult(
							System.getProperty("Test_Scenario_Name"),
							"Verify retail cost is present in the element", "Expected: "
									+ billedclientcost, "Fail",
							"Expected text  is not present (Actual: "
									+ dealercost + ")", true, webDriver);
					scriptExecutor.WriteExcelDataFile(strDataFileName,  TestCase,rownumber, "Connect_DealerCostResults", "FAIL");
				}
				if (price.equals(retailcost)) {
					reporter.writeStepResult(
							System.getProperty("Test_Scenario_Name"),
							"Verify retail cost is present in the element", "Expected: "
									+ price, "Pass", "Expected text  is present",
							true, webDriver);
					scriptExecutor.WriteExcelDataFile(strDataFileName,  TestCase,rownumber, "Connect_SRPCostResults", "PASS");
				} else {
					reporter.writeStepResult(
							System.getProperty("Test_Scenario_Name"),
							"Verify retail cost is present in the element", "Expected: "
									+ price, "Fail",
							"Expected text  is not present (Actual: "
									+ retailcost + ")", true, webDriver);
					scriptExecutor.WriteExcelDataFile(strDataFileName,  TestCase,rownumber, "Connect_SRPCostResults", "FAIL");
				}
			}
		
			if (typeofquote.contains("Sales")) 
			{
				Select dd = null;
				//verify whether coverage is clickable , if clickable,select coverage
				if (verify.verifyElementIsPresentCheck(webDriver,"_RatePortlet_WAR_RatePortlet_INSTANCE_p2cm_coverageDropdown","id"))
				{	
					coverage = SelectCoverage(coverage);
					dd = new Select(webDriver.findElementById("_RatePortlet_WAR_RatePortlet_INSTANCE_p2cm_coverageDropdown"));
					List <WebElement> alloptions =  dd.getOptions();
					System.out.println(dd.getOptions().size());
					if (!alloptions.isEmpty())
					{
						for (WebElement webElement : alloptions)
						{
							String strData = coverage;
							strData = strData.toUpperCase();
							if (webElement.getText().contains(strData)){
								strData = webElement.getText();
								dd.selectByVisibleText(coverage);
								
							}else{
								System.out.println("Value not found in dropdown field");
							}
						}
					}else{
						reporter.writeStepResult("Sales", "Coverage", "", "PASS", "No coverage value for selection", true, webDriver);
						//String Coverage_SFscreen = verification.verifyandstoreElementTextPresent(webDriver, "_RatePortlet_WAR_RatePortlet_INSTANCE_p2cm_coverageDropdown", "id");
					//	scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "Coverage_ConnectSCRN", Coverage_SFscreen);
					}
				}else{
					reporter.writeStepResult("Sales", "Coverage", "", "PASS", "No coverage value for selection", true, webDriver);
				}
				
				if (verify.verifyElementIsPresentCheck(webDriver,"_RatePortlet_WAR_RatePortlet_INSTANCE_p2cm_termmonthDropdown","id"))
				{	//verify whether months/miles is clickable , if clickable,select  months/miles
					if (miles.contains(",")){
						miles = miles.replace(",","");
					}
					miles = termsmonths +"/" +miles;
					
					dd = new Select(webDriver.findElementById("_RatePortlet_WAR_RatePortlet_INSTANCE_p2cm_termmonthDropdown"));
					//ADDED NOW
					List <WebElement> alloptions3 =  dd.getOptions();
					System.out.println(dd.getOptions().size());
					if (!alloptions3.isEmpty())
					{
						for (WebElement webElement : alloptions3)
						{
								String strData = miles;
								if (webElement.getText().contains(strData)){
								strData = webElement.getText();
								System.out.println(strData);
								dd.selectByVisibleText(miles);
								
							}else{
								System.out.println("Value not found in dropdown field");
							}
						}
					} else{
					reporter.writeStepResult("Sales", "Miles", "", "PASS", "No Miles value for selection", true, webDriver);
					}
				}else{
					reporter.writeStepResult("Sales", "Miles", "", "PASS", "No Miles value for selection", true, webDriver);
				}
			//verify whether deductible is clickable , if clickable,select deductible
			if (verify.verifyElementIsPresentCheck(webDriver,"_RatePortlet_WAR_RatePortlet_INSTANCE_p2cm_deductibleDropdown","id"))
			{	
				List <WebElement> deductibles = webDriver.findElementsById("_RatePortlet_WAR_RatePortlet_INSTANCE_p2cm_deductibleDropdown");
				dd = new Select(webDriver.findElementById("_RatePortlet_WAR_RatePortlet_INSTANCE_p2cm_deductibleDropdown"));
				//ADDED NOW
				List <WebElement> alloptions2 =  dd.getOptions();
				System.out.println(dd.getOptions().size());
				if (!alloptions2.isEmpty())
				{
					for (WebElement webElement : alloptions2)
					{
							String strData = Deductible;
							if (webElement.getText().contains(strData)){
							strData = webElement.getText();
							System.out.println(strData);
							dd.selectByVisibleText(Deductible);
							
						}else{
							System.out.println("Value not found in dropdown field");
						}
					}
				}else{
						reporter.writeStepResult("Sales", "Deductible", "", "PASS", "No Deductible value for selection", true, webDriver);
					}
			}else{
				reporter.writeStepResult("Sales", "Deductible", "", "PASS", "No Deductible value for selection", true, webDriver);
			}
			if (verify.verifyElementIsPresentCheck(webDriver,"_RatePortlet_WAR_RatePortlet_INSTANCE_p2cm_price","id"))
			{
					String Price_SCRN = webDriver.findElement(By.id("_RatePortlet_WAR_RatePortlet_INSTANCE_p2cm_price")).getText();
					System.out.println(Price_SCRN);
					scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"Connect_MSRP",Price_SCRN);
					if (price.equals(Price_SCRN)) {
						reporter.writeStepResult(
								System.getProperty("Test_Scenario_Name"),
								"Verify retail cost is present in the element", "Expected: "
										+ price, "Pass", "Expected text  is present",
								true, webDriver);
						scriptExecutor.WriteExcelDataFile(strDataFileName,  TestCase,rownumber, "Connect_SRPCostResults", "PASS");
					} else {
						reporter.writeStepResult(
								System.getProperty("Test_Scenario_Name"),
								"Verify retail cost is present in the element", "Expected: "
										+ price, "Fail",
								"Expected text  is not present (Actual: "
										+ Price_SCRN + ")", true, webDriver);
						scriptExecutor.WriteExcelDataFile(strDataFileName,  TestCase,rownumber, "Connect_SRPCostResults", "FAIL");
					}
				}
				//Click on continue button
				stepExecutor.clickButton("findElementByXPath", ".//*[@id='_SaveAllRatesPortlet_WAR_SaveAllRatesPortlet_INSTANCE_oh0m__savebtn']/span", webDriver, TestCase);
				Thread.sleep(10000);
				//store quote id
				String quoteid = webDriver.findElement(By.xpath("//td[@id='_TransactionSummaryPortlet_WAR_TransactionSummaryPortlet_INSTANCE_9G1E_quoteid']")).getText();
				System.out.println(quoteid);
				scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber, "QuoteID", quoteid);
				Thread.sleep(10000);
				//Enter Address,city,country,state and zipcode
				stepExecutor.enterTextValue("findElementById",
						"ciaddress1", DataMap,
						"Address", webDriver, TestCase);
				stepExecutor.enterTextValue("findElementById",
						"cicity", DataMap,
						"City", webDriver, TestCase);
				
				stepExecutor.selectListValue("findElementByXpath", ".//select[@id='cicountry']",DataMap,"Country", webDriver, TestCase);	
				String StateAbbr = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "State");
				StateAbbr = GetProperties(StateAbbr).trim();
				new Select(webDriver.findElementById("cistate")).selectByVisibleText(StateAbbr);
				
				stepExecutor.enterTextValue ("findElementById",
						"cizip", DataMap,
						"Zip Code", webDriver, TestCase);
				
				//Select type of contract
				String TypeOfContract =scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber,"TypeOfContract" );
				stepExecutor.selectListValue("findElementByXpath", ".//select[@id='cifinanceType']",DataMap,"TypeOfContract", webDriver, TestCase);
				//Select lienholder information
				if ((TypeOfContract.contains("Finance")) || (TypeOfContract.contains("Lease")))
				{
					stepExecutor.selectListValue("findElementById", "cilenderInfoId",DataMap,"SelectLienholder", webDriver, TestCase);
					Thread.sleep(2000);
					EnterLeaseDetails();
					
					
				}
		
				String DealerType =scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber,"DealerType" );
				if (DealerType.contains("Submit"))
				{
					String agreementnumber = scriptExecutor.readDataFile(strDataFileName, TestCase,rownumber, "Agreement Number");
					if (agreementnumber.contains(".00")){
						agreementnumber = agreementnumber.replace(".00","");
					}
					String purchasedate = scriptExecutor.readDataFile(strDataFileName, TestCase,rownumber, "Issue Date");
					purchasedate = convertStringToDate(purchasedate );
					String retailprice = webDriver.findElement(By.id("_TransactionSummaryPortlet_WAR_TransactionSummaryPortlet_INSTANCE_9G1E_summarytotal")).getText();
					retailprice = retailprice.replace("$", "");
					retailprice = retailprice.replace(",", "");
					System.out.println (agreementnumber + purchasedate + retailprice);
					webDriver.findElement(By.id("contractno1")).sendKeys(agreementnumber);
					webDriver.findElement(By.id("purchasedate1")).sendKeys(purchasedate);	
					webDriver.findElement(By.id("retailprice1")).sendKeys(retailprice);	
					webDriver.findElement(By.id("retailprice1")).sendKeys(Keys.TAB);
					//Click on continue to recap button
					stepExecutor.clickButton("findElementByXPath", ".//a[@id='continueButtonId']/span", webDriver, TestCase);
					Thread.sleep(6000);	
					//click on Purchase/submit/remit button
					stepExecutor.clickButton("findElementByXPath", ".//a[@id='purchaseBtn']/span[2]", webDriver, TestCase);
					Thread.sleep(2000);
				}
				if (DealerType.contains("Purchase") || DealerType.contains("Remit"))
				{
					//Click on continue to recap button
					stepExecutor.clickButton("findElementByXPath", ".//a[@id='continueButtonId']/span", webDriver, TestCase);
					Thread.sleep(8000);	
					//click on Purchase/submit/remit button
					stepExecutor.clickButton("findElementByXPath", ".//a[@id='purchaseBtn']/span[1]", webDriver, TestCase);
					Thread.sleep(30000);
				}
				webDriver.findElement(By.xpath("(//div[@id='_CustomerRecapPortlet_WAR_CustomerRecapPortlet_INSTANCE_nB7T__custrecap_confirm_div']/div[2]/a/span)[3]")).click();
				Thread.sleep(10000);
	
				//capture the contract number created
				String contractnumber= webDriver.findElement(By.xpath(".//td[@id='_PurchasePortlet_WAR_PurchasePortlet_INSTANCE_rP0t__contractno1']")).getText();
				System.out.println(contractnumber);
				scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"ContractNumber",contractnumber);
			
				System.out.println("PDF Verification starts");
				//open PDF
				String pdfurl = null;
				if (webDriver.findElement(By.xpath("//td[@id='_PurchasePortlet_WAR_PurchasePortlet_INSTANCE_rP0t__pdf1']/a")).isDisplayed())
				{
					stepExecutor.clickImage("findElementByXPath","//td[@id='_PurchasePortlet_WAR_PurchasePortlet_INSTANCE_rP0t__pdf1']/a",webDriver,"open_pdf");
					System.out.println(webDriver.getWindowHandles().size() + capabilities.getBrowserName());
					//if (webDriver.getWindowHandles().size() >1)
				//	{
						/*ArrayList<String> tabs2 = new ArrayList<String> (webDriver.getWindowHandles());
						;
						webDriver.switchTo().window(tabs2.get(1));*/
				//	}
					
					//pdf download in IE
					//webDriver.switchTo().alert();
						
					//	webDriver.switchTo().alert().accept();
				/*		Robot r = new Robot();
					
						r.keyPress(KeyEvent.VK_TAB);
						r.keyPress(KeyEvent.VK_TAB);
						r.keyPress(KeyEvent.VK_TAB);
						r.keyPress(KeyEvent.VK_TAB);
						r.keyPress(KeyEvent.VK_TAB);
						r.keyPress(KeyEvent.VK_TAB); 
						r.keyPress(KeyEvent.VK_ENTER);
					
						System.out.println(webDriver.getWindowHandles().size());*/
					 Thread.sleep(60000);	
					//https://uat.connect.thewg.com/PurchasePortlet/pdf.do?quoteid=27337&prodid=WAR&lang=EN
					//read PDF
				//	String Verificationpoints = readpdf.openPDFfromfilelocation(PdfFilePath);
					String Verificationpoints = readpdf.readPDFFromFileLocation(PdfFilePath+readpdf.getFilename(PdfFilePath));
					System.out.println(Verificationpoints);
					tmonths = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "Terms (Months)");
					if (tmonths.contains(".0")){
						tmonths  = tmonths .replace(".0","");
					}
					String tmiles = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "Terms (Miles)");
					if (tmiles.contains(".0")){
						tmiles = tmiles.replace(".0","");
					}
					String tmiles1 = tmiles;
					tmiles = tmiles +",000";
					tmiles1 = tmiles1 +"000";
					String deductible = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "Deductible");
					coverage = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "Coverage");
					coverage = SelectCoverage(coverage);
					price = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "SRP Cost");
					String price1 = price.substring(0, 1);
					String price2 = price.substring(1, price.length());
					price = price1 + "," + price2;
					System.out.println(price+ tmiles+tmonths);
					//verify PDF
					if (readpdf.checkPDFContent(Verificationpoints,tmonths)){
						scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"PDF_TermsMonthsResults","PASS");
					}else {
						scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"PDF_TermsMonthsResults","FAIL");
					}
					
					if (readpdf.checkPDFContent(Verificationpoints,tmiles)){
						scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"PDF_TermsMilesResults","PASS");
					}else {
						if (readpdf.checkPDFContent(Verificationpoints,tmiles1)){
							scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"PDF_TermsMilesResults","PASS");
						}else{
						scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"PDF_TermsMilesResults","FAIL");
						}
					}
					if (readpdf.checkPDFContent(Verificationpoints,coverage)){
						scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"PDFCoverageResults","PASS");
					}else {
						scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"PDFCoverageResults","FAIL");
					}
					if (readpdf.checkPDFContent(Verificationpoints,deductible)){
						scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"PDFDeductibleResults","PASS");
					}else {
						scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"PDFDeductibleResults","FAIL");
					}
					if (readpdf.checkPDFContent(Verificationpoints,price)){
						scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"PDFPriceResults","PASS");
					}else {
						scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"PDFPriceResults","FAIL");
					}
					System.out.println("PDF Verification completed successfully");

		}
	
			}	
				
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//close pdf document
			File f=new File(strDataPath+"outputPDF.txt");
	        if(f.exists())
	        {  
	        	f.delete();
	        }
			if (typeofquote.contains("Sales"))
			{
				FileInputStream fileToParse= new FileInputStream(new File(PdfFilePath+readpdf.getFilename(PdfFilePath)));
				PDFParser parser = new PDFParser(fileToParse);
				parser.parse();
				parser.getPDDocument().close();
				//delete pdf document
				parser.clearResources();
				String PDFfile = readpdf.getFilename(strDataPath);
				if (PDFfile != null)
	        	{	File f1 = new File(strDataPath+PDFfile);
	        		if(f1.exists())
	        		{	
	        			f1.delete();
	        		
	        		}
	        	}
			}
		}
	}

	
	private void EnterLeaseDetails() 
	{
		//Enter Amount Financed/Capitalized Cost
		stepExecutor.enterTextValue("findElementById",
				"ciloanAmount", DataMap,
				"FinanceAmount", webDriver, TestCase);
		//APR / Money Factor %
		stepExecutor.enterTextValue("findElementById",
				"ciannualPrctRate", DataMap,
				"APR_MoneyFactorPercentage", webDriver, TestCase);
		//Total Of Payments
		stepExecutor.enterTextValue("findElementById",
				"citotalAmount", DataMap,
				"TotalOfPayments", webDriver, TestCase);
		//Balloon Amt/Residual Value
		stepExecutor.enterTextValue("findElementById",
				"ciresidBallonAmt", DataMap,
				"BallonAmt_ResidualValue", webDriver, TestCase);
		//Enter date of payment
		String dateofpayment = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "dateofpayment");
		dateofpayment= convertStringToDate(dateofpayment);
		System.out.println(dateofpayment);
		webDriver.findElementById("cifirstPayment").clear();
		webDriver.findElementById("cifirstPayment").sendKeys(dateofpayment);
	
		
	}

	public String GetProperties(String StateAbbreviation) throws IOException
	{
		String State=null;
		Properties prop = new Properties();
		String propFileName = strDataPath +"States.properties";
 
		prop.load(new FileInputStream(propFileName));

		State = prop.getProperty(StateAbbreviation);
	
		return State;
		
	}

	//NextFuncBody		
	public String GetTestStartTime()
	{
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.println( sdf.format(cal.getTime()) );
    	return (sdf.format(cal.getTime()));
    	
	}
	
	private boolean FindDescendantsofCheckbox(String ProductType,String SubProductTypeSelection) throws InterruptedException
	{

		Boolean isFlag = false;
		try
		{
			
			if (webDriver.findElementByXPath("//td[@id='productSelectionTdId']/div[2]/div/label[contains(text(),'Vehicle Service Contract')]//preceding-sibling::input[@type='checkbox']").isSelected()){
				webDriver.findElementByXPath("//td[@id='productSelectionTdId']/div[2]/div/label[contains(text(),'Vehicle Service Contract')]//preceding-sibling::input[@type='checkbox']").click();
				Thread.sleep(5000);	
			}
			if (webDriver.findElementByXPath("//td[@id='productSelectionTdId']/div[2]/div/label[contains(text(),'"+ProductType+"')]//preceding-sibling::input[@type='checkbox']").isSelected())
			{
				reporter.writeStepResult("Product already selected", "Select Product",ProductType, "Pass", "", true, webDriver);
			}else{
				webDriver.findElementByXPath("//td[@id='productSelectionTdId']/div[2]/div/label[contains(text(),'"+ProductType+"')]//preceding-sibling::input[@type='checkbox']").click();
				Thread.sleep(5000);	
				reporter.writeStepResult("Product selected by user", "Select Product",ProductType, "Pass", "", true, webDriver);
			}
			if (webDriver.findElementByXPath("//td[@id='productSelectionTdId']/div[2]/div/label[contains(text(),'"+ProductType+"')]//preceding-sibling::input[@type='checkbox']").isSelected())
			{
				if (ProductType.contains("Service Contract"))
				{
					new Select(webDriver.findElementByXPath(".//select[@id='warTypeDrpDn']")).selectByVisibleText(SubProductTypeSelection);
					Thread.sleep(5000);	
					isFlag = true;
				}
				if (ProductType.contains("GAP"))
				{
					new Select(webDriver.findElementByXPath(".//select[@id='gapPaymentTypeSelectId']")).selectByVisibleText(SubProductTypeSelection);
					Thread.sleep(5000);	
					String Terms = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Terms (Months)");
					if (Terms.contains(".0")){
						Terms = Terms.replace(".0", "");
					}
					webDriver.findElement(By.xpath(".//input[@id='gapTerm']")).clear();
					webDriver.findElement(By.xpath(".//input[@id='gapTerm']")).sendKeys(Terms);
					isFlag = true;
				}
				if (SubProductTypeSelection == null){
					System.out.println("No sub-options are present for selected product");
					
				}
				isFlag = true;
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return isFlag;
		
	}
	
	@SuppressWarnings("unused")
	private void RadioButtonYesNoClick() throws InterruptedException
	{
		//Boolean isFlag = false;
		String VIN = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "VIN");
		String VINDecoding = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "VINDecoding");
		if (VINDecoding.contains("Yes"))
		{
			WebElement radiobutton = webDriver.findElement(By.id("vehicleInfoVinModeId"));
			if (webDriver.findElement(By.id("vehicleInfoVinModeId")).isDisplayed())
			{
				radiobutton.isSelected();
				if (true){
					webDriver.findElementByXPath(".//input[@id='vinNumberField']").sendKeys(VIN);
				    // Press "TAB" key
					webDriver.findElementByXPath(".//input[@id='vinNumberField']").sendKeys(Keys.TAB);
					stepExecutor.clickButton("findElementByXPath", ".//*[@id='vinDecodeButtonId']/span", webDriver, TestCase);
					
				}else {
					radiobutton.click();
					webDriver.findElementByXPath(".//input[@id='vinNumberField']").sendKeys(VIN);
				    // Press "TAB" key
					webDriver.findElementByXPath(".//input[@id='vinNumberField']").sendKeys(Keys.TAB);
					stepExecutor.clickButton("findElementByXPath", ".//*[@id='vinDecodeButtonId']/span", webDriver, TestCase);

				}
			}
		}
		if (VINDecoding.contains("No"))
		{															
			WebElement radiobutton1 = webDriver.findElement(By.id("vehicleInfoNormalModeId"));
			if (webDriver.findElement(By.id("vehicleInfoNormalModeId")).isDisplayed())
			{

				radiobutton1.isSelected();
				if (true)
				{
				// Click "No" on VIN Decoding button
				radiobutton1.click();
				
				// Enter 17 characters VIN
				webDriver.findElementByXPath(".//input[@id='vinNumberField']").sendKeys(VIN);
			    // Press "TAB" key
				webDriver.findElementByXPath(".//input[@id='vinNumberField']").sendKeys(Keys.TAB);
				// Select Model year
				stepExecutor.selectListValue("findElementByXpath",
						".//select[@id='vehModelYearIdSelect']",DataMap,
						 "Mod Yr", webDriver, TestCase);
				
				// Select Make of vehicle
				stepExecutor.selectListValue("findElementByXpath",
						".//*[@id='vehMakeIdSelect']",DataMap,
						"Make", webDriver,TestCase);
				Thread.sleep(30000);
			
				// Select Model of vehicle
				stepExecutor.selectListValueByContainsValue("findElementByXpath",
						".//*[@id='vehModelIdSelect']",
						 "Vehicle Code", webDriver,TestCase,rownumber);
				
				}
			} 
		}

	}
	
	private String SelectCoverage(String Coverage)
	{
		//Coverage = Coverage.substring(0, 3);
		if ((Coverage.equalsIgnoreCase("GLD200")) ||(Coverage.contains("GLD"))) {
			Coverage ="Gold";
			
		}
		else if (Coverage.contains("GLD")){
			Coverage ="Gold";
			
		}
		else if (Coverage.equalsIgnoreCase("GLD202")){
			Coverage ="Gold Plus";
			
		}
		
		else if (Coverage.contains("PLT")){
			Coverage ="Platinum";
			
		}
		else if (Coverage.contains("PTN")){
			Coverage ="Powertrain";
			
		}
		else if (Coverage.contains("SLV")){
			Coverage ="Silver";
			
		}
		else if (Coverage.contains("BRZ")){
			Coverage ="Bronze";
			
		}else
		{
			Coverage = Coverage;
		}
		return Coverage;
		
	}
	
	private String SelectDeductibleValue(String Deductibles)
	{

		if (Deductibles.contains("100")){
			Deductibles ="$100 Disappearing Deductible";
			
		}
		if (Deductibles.contains("200")){
			Deductibles ="$200 Reducing to $100 Deductible";
			
		}
		if (Deductibles.contains("400")){
			Deductibles ="$400 Reducing to $200 Deductible";
			
		}
		return Deductibles;
		
	}

	public String convertStringToDate(String InServiceDate) 
	{
		 String indate = null;
		  int len = InServiceDate.length();
		  if (len == 8)
		  {
			  String month = InServiceDate.substring(0, 2);
		      String date = InServiceDate.substring(2,4);
		
		      String year = InServiceDate.substring(4, InServiceDate.length());
			  SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			 
			    indate = month + "/" + date + "/" + year;
			    try {
			        
			        Date theDate = dateFormat.parse(indate);            
			        indate = dateFormat.format(theDate);
			    } catch (ParseException e) {
		            e.printStackTrace();
		        }
		  }
		  if (len==6)
		  {
			 
			  String ModelYear = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Mod Yr");
			  ModelYear = ModelYear.substring(0, 2);
			  String month = InServiceDate.substring(0, 2);
		      String date = InServiceDate.substring(2,4);
		
		      String year = InServiceDate.substring(4, InServiceDate.length());
		      year = ModelYear+year;
		      System.out.println(year);
			  SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			 
			    indate = month + "/" + date + "/" + year;
			    try {
			        
			        Date theDate = dateFormat.parse(indate);            
			        indate = dateFormat.format(theDate);
			    } catch (ParseException e) {
		            e.printStackTrace();
		        }
		  }
	        return indate;
    }

	
	public void SwitchHandleToNewWindow(WebDriver driver, String windowTitle)
	{
		ArrayList<String> tabs2 = new ArrayList<String> (webDriver.getWindowHandles());
		webDriver.switchTo().window(tabs2.get(1));
		//webDriver.navigate().to(windowTitle);
		webDriver.manage().timeouts().implicitlyWait(600,TimeUnit.SECONDS);
	}
	
	
}