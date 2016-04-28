package com.capgemini.scripts;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.graphics.predictor.Sub;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.capgemini.driver.CreateDriver;
import com.capgemini.driver.ScriptExecutor;
import com.capgemini.driver.StepExecutor;
import com.capgemini.executor.ExecutionRowNumber;
import com.capgemini.executor.WriteMaster;
import com.capgemini.utilities.ReadExcel;
import com.capgemini.utilities.ReadPDF;
import com.capgemini.utilities.Reporter;
import com.capgemini.utilities.Utilities;
import com.capgemini.utilities.Verification;

import org.openqa.selenium.remote.DesiredCapabilities;

public class GLOW_Production {

	public String TestCase = "GLOW_Production";
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
	private static String strAbsolutepath = new File("").getAbsolutePath();
	private static String strDataPath = strAbsolutepath + "/data/";
	String strDataFileName = utils.getDataFile("GLOW_Production");
	private boolean acceptNextAlert = true;
	int rownumber = 0;
	String strStopTime;
	
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
			strDataFileName = strDataPath + "MasterSheet.xls";
			WriteExcelDataFile(strDataFileName,rownumber,"StartTime",StrExecutionStartTime);
			DataMap=readExcel.loadDataMap(rownumber);
			strDataFileName = strDataPath + "MasterSheet.xls";
			System.out.println(capabilities.getBrowserName());
			reporter.setStrBrowser(capabilities.getBrowserName());
			reporter.addIterator(i);
			CreateOutputfile();
			testcaseMain();
			//NextFunctionCall
			WriteMaster.updateNextURL(TestCase,webDriver.getCurrentUrl());
			reporter.closeIterator();
			System.out.println("\t \t \t \t \t Row number: " + i);
			webDriver.quit();
			strStopTime = reporter.stop();
			WriteExcelDataFile(strDataFileName,rownumber,"EndTime",strStopTime);
		}
		}
		reporter.strStopTime = strStopTime;
		float timeElapsed = reporter.getElapsedTime();
		reporter.timeElapsed = timeElapsed;
		reporter.CreateSummary("Cafe#"+browserName);
		// System.exit(1);
	}

	public void executeTestcase(RemoteWebDriver rdriver, String host,
			String browser) throws Exception {
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
			rownumber=i;
			DataMap = readExcel.loadDataMap(rownumber);
			// readExcel.readByIndex(i);
			// webDriver.switchTo().activeElement();
			// System.out.println(capabilities.getBrowserName());
			// reporter.setStrBrowser(capabilities.getBrowserName());
			reporter.addIterator(i);
			testcaseMain();

			// NextFunctionCall
			reporter.closeIterator();
			System.out.println("\t \t \t \t \t Row number: " + i);
			webDriver.quit();
		}

		String strStopTime = reporter.stop();
		reporter.strStopTime = strStopTime;
		float timeElapsed = reporter.getElapsedTime();
		reporter.timeElapsed = timeElapsed;
		reporter.CreateSummary(browser);
	}

	public void testcaseMain() throws InterruptedException, BiffException,
			Exception {
		stepExecutor.launchApplication("URL", DataMap, webDriver);
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {
			WebDriverWait wait = new WebDriverWait(webDriver, 60);
			
			Login();
		
			// Enter Dealer account number for example 707j* in search text box and click on search button
			strDataFileName = strDataPath + "MasterSheet.xls";
			String dealeraccountnumber = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "Client");
			wait.until(ExpectedConditions.elementToBeClickable(By.id("phSearchInput")));
			stepExecutor.enterTextValue("findElementById", "phSearchInput",
					DataMap, "Client", webDriver, "GLOW_Production");
			webDriver.findElementById("phSearchInput").sendKeys(Keys.RETURN);
			Thread.sleep(5000);

			// Click on Dealer Account name as matching to Dealer account number as in MR Data
			String dealername = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "Dealer Account Name");
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='Account_body']/table/tbody/tr[2]/th/a")));
			stepExecutor.clickLinkValue("findElementByXPath",
					".//*[@id='Account_body']/table/tbody/tr[2]/th/a", DataMap,
					"Dealer Account Name", webDriver, "GLOW_Production");
			Thread.sleep(10000);
			JavascriptExecutor executor = (JavascriptExecutor)webDriver;
			String DealerType = scriptExecutor.readDataFile(strDataFileName,TestCase,
					rownumber, "DealerType");
			List<WebElement> e= webDriver.findElements(By.id("00Nd0000007JtRx_ileinner"));
			for(WebElement c : e)
			{
				
				try{
					executor.executeScript("arguments[0].click();", c);

				}
				
				catch(WebDriverException e1){
					 ((JavascriptExecutor) webDriver).executeScript(
	                         "arguments[0].scrollIntoView(true);", c);
					 
					 executor.executeScript("arguments[0].click();", c);

					((JavascriptExecutor) webDriver).executeScript(
                     "scroll(0,-250);");
					
				}
			}
			
			// verify that dealertype is purchase/remit/submit and take snapshot of page
		//	webDriver.findElement(By.id("00Nd0000007JtRx_ileinner")).click();
		//	webDriver.findElement(By.id("00Nd0000007JtRx_ileinner")).click();
			verify.verifyTextValue(webDriver, "00Nd0000007JtRx_ileinner","id", DealerType);
			Thread.sleep(1000);
			String dealerype_sfscrn= webDriver.findElementByXPath(".//*[@id='00Nd0000007JtRx_ileinner']").getText();

			if(dealerype_sfscrn.equalsIgnoreCase(DealerType))
			{
				// click on Quotes and new quotes for selected dealer searched'001d000001R7BbV_00Nd0000007KYFN_link
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='new00Nd0000007KYFN']")));
				stepExecutor.clickElement("findElementByXPath","//input[@name='new00Nd0000007KYFN']", webDriver, "GLOW_Production");
	
				// Select the type of quote
				wait.until(ExpectedConditions.elementToBeClickable(By.name("j_id0:j_id1:j_id2:j_id29:j_id30")));
				stepExecutor.selectListValue("findElementByName",
						"j_id0:j_id1:j_id2:j_id29:j_id30", DataMap,
						"SelectTypeOfQuote", webDriver, "GLOW_Production");
				// click on Next button
				stepExecutor.clickButton("findElementByName", "j_id0:j_id1:j_id2:j_id32", webDriver, "GLOW_Production");
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='pg:frm:ProductBlock:dealername']")));
				Thread.sleep(5000);
				
				// Search for dealer lookup, product selection and create a quote//input[@id='pg:frm:ProductBlock:dealername']
				WebElement textbox = webDriver.findElement(By.xpath(".//input[@id='pg:frm:ProductBlock:dealername']"));
				webDriver.findElementByXPath(".//input[@id='pg:frm:ProductBlock:dealername']").sendKeys(dealername);
				textbox.sendKeys(Keys.TAB);
				Thread.sleep(5000);
	
				String Parent_Window_Handle = webDriver.getWindowHandle();
				if (webDriver.findElementsByCssSelector("div.errorMsg").size() != 0)
				{	
					if ((webDriver.findElement(By.cssSelector("div.errorMsg")).getText()).equalsIgnoreCase("Error: Multiple items found. Select from drop-down or click icon to refine search.")) 
					{
						webDriver.findElementByXPath("//img[@alt='Account Lookup (New Window)']").click();
						Thread.sleep(10000);
						String Child_Window_Handle = null;
						String Child_window_title;
						Set<String> s = webDriver.getWindowHandles();
						Iterator<String> itr = s.iterator();
						while(itr.hasNext())
						{
							String temp_Handle=itr.next();
							if(temp_Handle.equalsIgnoreCase(Parent_Window_Handle))
							{
								System.out.println("Not the child WH");
							}
							else
							{
								Child_Window_Handle = temp_Handle;
							}
						}
						webDriver.switchTo().window(Child_Window_Handle);
						String handle = webDriver.getWindowHandle();
						Child_window_title = webDriver.getTitle();
						if (webDriver.getTitle().contains("Search"))
				        {
				        	webDriver.switchTo().frame("resultsFrame");
				        	verify.verifyElementPresent(webDriver, "//tr[td[contains(text(),'"+dealeraccountnumber+"')]]", "xpath");
				        	List<WebElement> tablerow = webDriver.findElementsByXPath("//tr[td[contains(text(),'"+dealeraccountnumber+"')]]");
				        	List<String> colValues = new ArrayList<String>();
				        	Iterator<WebElement> i = tablerow.iterator();
				            while (i.hasNext()) {
				                 WebElement row = i.next();
				                int colIndex=1;
								WebElement colElement;
								if (row.findElements(By.tagName("th")).size() > 0) {
				                     colElement = row.findElement(By.xpath(".//th[" + colIndex + "]"));
				                     
				                 } else {
				                     colElement = row.findElement(By.xpath(".//td[" + colIndex + "]"));
				                 }
				                 colValues.add(colElement.getText().trim());
				                // System.out.println(colElement.getText());
				                 colElement.click();
				             }
							Thread.sleep(15000);
				            webDriver.switchTo().window(Parent_Window_Handle);
				        }
					}
				}
				// select product type
				String ProductType = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "ProductToSelect");
				String SubProductTypeSelection = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "SubProductandContractTypes");
				if (FindDescendantsofCheckbox(ProductType, SubProductTypeSelection)){
					reporter.writeStepResult("Product selection", "Select Product",ProductType, "Pass", "Product selected", true, webDriver);
				}else{
					reporter.writeStepResult("Product selection", "Select Product",ProductType, "fail", "Product not selected", true, webDriver);
				}
		
				// Click "No" on VIN Decoding button
			    RadioButtonYesNoClick();
			    wait.until(ExpectedConditions.elementToBeClickable(By.id("pg:frm:VehicleBlock:j_id257:j_id264:j_id268")));
				// Enter Odometer value
				stepExecutor.enterTextValue("findElementById",
						"pg:frm:VehicleBlock:j_id257:j_id264:j_id268", DataMap,
						"Odometer", webDriver, "GLOW_Production");
				wait.until(ExpectedConditions.elementToBeClickable(By.id("pg:frm:VehicleBlock:j_id283:j_id284:j_id289")));
				// Enter first name and last name
				stepExecutor.enterTextValue("findElementById",
						"pg:frm:VehicleBlock:j_id283:j_id284:j_id289", DataMap,
						"First Name", webDriver, "GLOW_Production");
				stepExecutor.enterTextValue("findElementById",
						"pg:frm:VehicleBlock:j_id283:LastNameRegion:j_id300",
						DataMap, "Last Name", webDriver, "GLOW_Production");
	
				// click on Get Rates button
				stepExecutor.clickButton("findElementById",
						"pg:frm:GetRatesButton", webDriver, "GLOW_Production");
				webDriver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
				Thread.sleep(10000);
				// select coverage
				String coverage = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Coverage");
				coverage = coverage.trim();
				coverage = GetCoverageProperties(coverage).trim();
				Select dd = null;
				System.out.println(coverage);
				
			//	new Select(webDriver.findElementByXPath(".//*[@id='pg:frm:QuoteDP:j_id352:0:j_id355']/select")).selectByVisibleText(Coverage);
				dd = new Select(webDriver.findElementByXPath(".//*[@id='pg:frm:QuoteDP:j_id352:0:j_id355']/select"));
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
							dd.selectByVisibleText(strData);
							
						}else{
							System.out.println("Value not found in dropdown field");
						}
					}
				}else{
					System.out.println("No Value found in coverage dropdown field");
				
				}
/*				
				//new Select(webDriver.findElementByXPath(".//*[@id='pg:frm:QuoteDP:j_id352:0:j_id355']/select")).selectByVisibleText(Coverage);
				
				// select coverage
				/*stepExecutor.selectListValue("findElementByXpath",
						".//*[@id='pg:frm:QuoteDP:j_id352:0:j_id355']/select",DataMap,
						 "Coverage", webDriver, "GLOW_Production");*/
				Thread.sleep(4000);
				// select terms: months/miles
				String termsmonths = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "Terms (Months)");
				if (termsmonths.contains(".0")){
					termsmonths = termsmonths.replace(".0","");
				}
				String termsmiles = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "Terms (Miles)");
				if (termsmiles.contains(".0")){
					termsmiles = termsmiles.replace(".0","");
				}
				termsmiles = termsmiles +"000";
				String terms = termsmonths +"/" + termsmiles;
				Select s2 = new Select(webDriver.findElementByXPath(".//*[@id='pg:frm:QuoteDP:j_id352:0:j_id358']/select"));
				s2.selectByVisibleText(terms);
				Thread.sleep(6000);	
				//new Select(webDriver.findElementByXPath(".//*[@id='pg:frm:QuoteDP:j_id352:0:j_id358']/select")).selectByVisibleText(terms);
				Thread.sleep(8000);
				
				// select deductible interval
				String deductibles = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "Deductibles");
				if (deductibles.contains(".0")){
					deductibles = deductibles.replace(".0", "");
				}
				deductibles = SelectDeductibleValue(deductibles);
				System.out.println(deductibles);
				webDriver.findElementByXPath(".//*[@id='pg:frm:QuoteDP:j_id352:0:j_id358']/select").sendKeys(Keys.TAB);
				Select s1 = new Select(webDriver.findElement(By.xpath(".//*[@id='pg:frm:QuoteDP:j_id352:0:j_id361']/select")));
				s1.selectByVisibleText(deductibles);
			//	new Select(webDriver.findElementByXPath(".//*[@id='pg:frm:QuoteDP:j_id352:0:j_id361']/select")).selectByVisibleText(deductibles);
				/*stepExecutor.selectListValue("findElementByXpath",
						".//*[@id='pg:frm:QuoteDP:j_id352:0:j_id361']/select", DataMap,"Deductibles",
						webDriver, "GLOW_Production");*/
				Thread.sleep(8000);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='pg:frm:QuoteDP:j_id352:0:j_id367']")));
				// capture dealer cost in string
				String dcost  = null;
				dcost = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "Billed Client Cost");
				dcost = "$"+dcost;
				String actualdealercost = webDriver.findElementByXPath(
						".//*[@id='pg:frm:QuoteDP:j_id352:0:j_id367']").getText();
				actualdealercost = actualdealercost.replace(",","");
				actualdealercost = actualdealercost +".00";
				WriteExcelDataFile(strDataFileName, rownumber, "Salesforce_Dealercost", actualdealercost);
				if (dcost.equals(actualdealercost)) {
					reporter.writeStepResult(
							System.getProperty("Test_Scenario_Name"),
							"Verify dealer cost is present in the element", "Expected: "
									+ dcost, "Pass", "Expected text  is present",
							true, webDriver);
					WriteExcelDataFile(strDataFileName, rownumber, "Salesforce_DealercostResults", "PASS");
				} else {
					reporter.writeStepResult(
							System.getProperty("Test_Scenario_Name"),
							"Verify dealercost is present in the element", "Expected: "
									+ dcost, "Fail",
							"Expected text  is not present (Actual: "
									+ actualdealercost + ")", true, webDriver);
					WriteExcelDataFile(strDataFileName, rownumber, "Salesforce_DealercostResults", "FAIL");
				}
				// capture retail cost in string value
				String rcost  = null;
				rcost = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "SRP Cost");
				rcost = "$"+rcost;
				String actualretailcost = webDriver.findElementByXPath(
						".//*[@id='pg:frm:QuoteDP:j_id352:0:j_id371']").getText();
				actualretailcost =actualretailcost.replace(",", "");
				WriteExcelDataFile(strDataFileName, rownumber, "Salesforce_Retailcost", actualretailcost);
				if (rcost.equals(actualretailcost)) {
					reporter.writeStepResult(
							System.getProperty("Test_Scenario_Name"),
							"Verify retail cost is present in the element", "Expected: "
									+ rcost, "Pass", "Expected text  is present",
							true, webDriver);
				WriteExcelDataFile(strDataFileName, rownumber, "Salesforce_Retailcostresults", "PASS");
				} else {
					reporter.writeStepResult(
							System.getProperty("Test_Scenario_Name"),
							"Verify retail cost is present in the element", "Expected: "
									+ rcost, "Fail",
							"Expected text  is not present (Actual: "
									+ actualretailcost + ")", true, webDriver);
					WriteExcelDataFile(strDataFileName, rownumber, "Salesforce_Retailcostresults", "FAIL");
				}
				// enter customer cost
				String customercost = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "CustomerCost");
				String ccost = verify.verifyandstoreElementTextPresent(webDriver, ".//input[@id='pg:frm:QuoteDP:j_id352:0:xyz']","xpath");
				//ccost =ccost.replace(",", "");
				WriteExcelDataFile(strDataFileName, rownumber, "Salesforce_customercost", ccost);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='pg:frm:savebtn']")));
				// Click on Save and Continue button
				stepExecutor.clickButton("findElementByXPath",
						".//*[@id='pg:frm:savebtn']", webDriver, "GLOW_Production");
				Thread.sleep(6000);
				wait.until(ExpectedConditions.elementToBeClickable(By.id("pg:frmCon:contactPB:contactPBs:pbsBA1:ba1")));
				// Enter City,address,phone number, zip and email, Select State
				stepExecutor.enterTextValue("findElementById",
						"pg:frmCon:contactPB:contactPBs:pbsBA1:ba1", DataMap,
						"Address", webDriver, "GLOW_Production");
				stepExecutor.enterTextValue("findElementById",
						"pg:frmCon:contactPB:contactPBs:pbsbCity:bCity", DataMap,
						"City", webDriver, "GLOW_Production");
				String StateAbbr = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "State");
				StateAbbr = GetProperties(StateAbbr).trim();
				new Select(webDriver.findElementById("pg:frmCon:contactPB:contactPBs:pbsBState:bState")).selectByVisibleText(StateAbbr);
				//Select Country
				stepExecutor.selectListValue("findElementByXpath", ".//select[@id='pg:frmCon:contactPB:contactPBs:pbsBCountry:bCountry']",DataMap,"Country", webDriver, "GLOW_Production");
				
				stepExecutor.enterTextValue("findElementById",
						"pg:frmCon:contactPB:contactPBs:pbsbZip:bZip", DataMap,
						"Zip Code", webDriver, "GLOW_Production");
				
				stepExecutor.enterTextValue("findElementById",
						"pg:frmCon:contactPB:contactPBs:pbsPh1:bph1", DataMap,
						"Phone", webDriver, "GLOW_Production");
				stepExecutor.enterTextValue("findElementById",
						"pg:frmCon:contactPB:contactPBs:pbsEmail1:bEmail1",
						DataMap, "Email", webDriver, "GLOW_Production");
				
				//Select Type of contract = "Cash"
				stepExecutor.selectListValue("findElementByXpath", ".//select[@id='pg:frmCon:finance:pgsf:pbsif:conType']",DataMap,"TypeOfContract", webDriver, "GLOW_Production");
				String  TypeOfContract = scriptExecutor.readDataFile(strDataFileName, "GLOW_Production", rownumber, "TypeOfContract");
				if (TypeOfContract.contains("Finance"))
				{
					stepExecutor.selectListValue("findElementByXpath", ".//select[@id='pg:frmCon:finance:pgsf:pbsif:conType']",DataMap,"TypeOfContract", webDriver, "GLOW_Production");
					EnterLienholderDetails();
	
				}
				if (TypeOfContract.contains("Lease"))
				{
					stepExecutor.selectListValue("findElementByXpath", ".//select[@id='pg:frmCon:finance:pgsf:pbsif:conType']",DataMap,"TypeOfContract", webDriver, "GLOW_Production");
					EnterLeasedetails();
					EnterLienholderDetails();
					
	
				}
				// Click on continue button to click on purchase screen page
				String quoteID = null;
				termsmiles = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "Terms (Miles)");
			    if (termsmiles.contains(".0"))
			    {
			    	termsmiles = termsmiles.replace(".0", "");
			    }
				deductibles = deductibles.substring(0, 4);
				termsmiles = termsmiles +",000";
				String price = ccost;
				price = price.replace(".00","");
				price = "$" + price;
				String PDFFilePath = strDataPath;
				if (DealerType.contains("Submit")) {
					String agreementnumber = scriptExecutor.readDataFile(
							strDataFileName, TestCase,rownumber, "Agreement Number");
					if (agreementnumber.contains(".0"))
				    {
					  agreementnumber= agreementnumber.replace(".0", "");
				    }
					webDriver.findElementByXPath(
							".//*[@id='pg:frmCon:j_id883:j_id884:0:j_id890']").clear();
					webDriver.findElementByXPath(
							".//*[@id='pg:frmCon:j_id883:j_id884:0:j_id890']")
							.sendKeys(agreementnumber);
					webDriver.findElementByXPath(
							".//*[@id='pg:frmCon:j_id883:j_id884:0:j_id890']")
							.sendKeys(Keys.TAB);
					String date = webDriver.findElement(By.xpath(".//*[@id='pg:frmCon:j_id883:j_id884:0:j_id898']/span/span/a")).getText();
					webDriver.findElement(By.xpath(".//*[@id='pg:frmCon:j_id883:j_id884:0:j_id900']")).click();
				    webDriver.findElement(By.xpath(".//*[@id='pg:frmCon:j_id883:j_id884:0:j_id900']")).clear();
					//Select purchase date for example: 3/13/2015 format
					webDriver.findElement(By.xpath(".//*[@id='pg:frmCon:j_id883:j_id884:0:j_id900']")).sendKeys(date);
					webDriver.findElement(By.xpath(".//*[@id='pg:frmCon:j_id883:j_id884:0:j_id900']")).sendKeys(Keys.TAB);
					webDriver.findElement(By.xpath(".//*[@id='pg:frmCon:j_id935']")).sendKeys(Keys.RETURN);
					Thread.sleep(5000);
					wait.until(ExpectedConditions.elementToBeClickable(By.id(".//*[@id='pg:j_id938:j_id946']/div/table/tbody/tr[4]/td[2]")));	
					//Capture the quote id
					quoteID = webDriver.findElement(By.xpath(".//*[@id='pg:j_id938:j_id946']/div/table/tbody/tr[4]/td[2]")).getText();
					WriteExcelDataFile(strDataFileName, rownumber, "QuoteID", quoteID);
					System.out.println("Quote creation completed successfully for Submit option");
				}
			
				if (DealerType.contains("Remit")) {
					stepExecutor.clickButton("findElementByXPath","//*[@id='pg:frmCon:j_id935']", webDriver, "GLOW_Production");
					Thread.sleep(5000);
					quoteID = webDriver.findElementByXPath(".//*[@id='pg:j_id938:j_id946']/div/table/tbody/tr[4]/td[2]").getText();
					WriteExcelDataFile(strDataFileName, rownumber, "QuoteID", quoteID);
						
					//Call Function to Open PDF and capture the contract number
					System.out.println("PDF verification starts");
					
					String PdfUrl= openPDF();
					String Verificaionpoints = null;
					if (webDriver.getWindowHandles().size() > 1)
					{
						Verificaionpoints= readPDF(PdfUrl);
					} else {
						//String path = readpdf.openPDFfromfilelocation(strDataPath);
						String filename = readpdf.getFilename(strDataPath);
						Verificaionpoints = readpdf.readPDFFromFileLocation(strDataPath+filename);
						
					}
					System.out.println(Verificaionpoints);
					System.out.println(termsmonths + coverage + price + deductibles);
					if (checkPDFContent(Verificaionpoints,termsmonths)){
						WriteExcelDataFile(strDataFileName,rownumber,"PDF_TermsMonthsResults","PASS");
					}else {
						WriteExcelDataFile(strDataFileName,rownumber,"PDF_TermsMonthsResults","FAIL");
					}
				
					if (checkPDFContent(Verificaionpoints,termsmiles)){
						WriteExcelDataFile(strDataFileName,rownumber,"PDF_TermsMilesResults","PASS");
					}else {
						WriteExcelDataFile(strDataFileName,rownumber,"PDF_TermsMilesResults","FAIL");
					}
					coverage = coverage.toUpperCase();
					if (checkPDFContent(Verificaionpoints,coverage)){
						WriteExcelDataFile(strDataFileName,rownumber,"PDFCoverageResults","PASS");
					}else {
						WriteExcelDataFile(strDataFileName,rownumber,"PDFCoverageResults","FAIL");
					}
				
					if (checkPDFContent(Verificaionpoints,deductibles)){
						WriteExcelDataFile(strDataFileName,rownumber,"PDFDeductibleResults","PASS");
					}else {
						WriteExcelDataFile(strDataFileName,rownumber,"PDFDeductibleResults","FAIL");
					}
	
					if (checkPDFContent(Verificaionpoints,price)){
						WriteExcelDataFile(strDataFileName,rownumber,"PDFPriceResults","PASS");
					}else {
						WriteExcelDataFile(strDataFileName,rownumber,"PDFPriceResults","FAIL");
					}
					System.out.println("PDF verification completed");
					//switch back to purchase screen for Remit scenario
					if (webDriver.getWindowHandles().size() > 1)
					{
						ArrayList<String> tabs4 = new ArrayList<String> (webDriver.getWindowHandles());
						webDriver.switchTo().window(tabs4.get(0));
					}
					
					 System.out.println("Remit Quote creation completed successfully"); 
				} else if (DealerType.contains("Purchase")) 
				{
					stepExecutor.clickButton("findElementByXPath",".//*[@id='pg:frmCon:j_id935']", webDriver, "GLOW_Production");
					Thread.sleep(6000);
					quoteID = webDriver.findElement(By.xpath(".//*[@id='pg:j_id938:j_id946']/div/table/tbody/tr[4]/td[2]")).getText();
					WriteExcelDataFile(strDataFileName, rownumber, "QuoteID", quoteID);
					
					System.out.println("Quote creation completed successfully for Purchase option");
					System.out.println("PDF verification starts");
					/*String PDFurl = openPDF();
				    String PurchaseVerificaionpoints= readPDF(PDFurl);*/
					String PdfUrl= openPDF();
					System.out.println(webDriver.getWindowHandles().size());
					String PurchaseVerificaionpoints = null;
					if (webDriver.getWindowHandles().size() > 1)
					{
						PurchaseVerificaionpoints = readPDF(PdfUrl);
					} else {
						//String path = readpdf.openPDFfromfilelocation(strDataPath);
						String filename = readpdf.getFilename(strDataPath);
						PurchaseVerificaionpoints = readpdf.readPDFFromFileLocation(strDataPath+filename);
						
					}
					System.out.println(PurchaseVerificaionpoints);
					System.out.println(termsmonths + coverage + price + deductibles);
					if (checkPDFContent(PurchaseVerificaionpoints,termsmonths)){
						WriteExcelDataFile(strDataFileName,rownumber,"PDF_TermsMonthsResults","PASS");
					}else {
						WriteExcelDataFile(strDataFileName,rownumber,"PDF_TermsMonthsResults","FAIL");
					}
					if (checkPDFContent(PurchaseVerificaionpoints,termsmiles)){
						WriteExcelDataFile(strDataFileName,rownumber,"PDF_TermsMilesResults","PASS");
					}else {
						WriteExcelDataFile(strDataFileName,rownumber,"PDF_TermsMilesResults","FAIL");
					}
					if (checkPDFContent(PurchaseVerificaionpoints,deductibles)){
						WriteExcelDataFile(strDataFileName,rownumber,"PDFDeductibleResults","PASS");
					}else {
						WriteExcelDataFile(strDataFileName,rownumber,"PDFDeductibleResults","FAIL");
					}
				
					coverage = coverage.toUpperCase();
					if (checkPDFContent(PurchaseVerificaionpoints,coverage)){
						WriteExcelDataFile(strDataFileName,rownumber,"PDFCoverageResults","PASS");
					}else {
						WriteExcelDataFile(strDataFileName,rownumber,"PDFCoverageResults","FAIL");
					}
					if (checkPDFContent(PurchaseVerificaionpoints,price)){
						WriteExcelDataFile(strDataFileName,rownumber,"PDFPriceResults","PASS");
					}else {
						WriteExcelDataFile(strDataFileName,rownumber,"PDFPriceResults","FAIL");
					}
					
					System.out.println("PDF verification completed");
					//switch back to salesforce purchase screen
					if (webDriver.getWindowHandles().size() > 1)
					{
						ArrayList<String> tabs4 = new ArrayList<String> (webDriver.getWindowHandles());
						webDriver.switchTo().window(tabs4.get(0));
					}
					System.out.println("Purchase scenario completed");
					
				}
			
			
		   } else {
		    	System.out.println("DealerType Mismatched");
		    	reporter.writeStepResult("Dealer Mismatch", "Dealer Type mismatch in results","", "Fail", "", true, webDriver);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
		
			File f=new File(strDataPath+"outputPDF.txt");
	        if(f.exists())
	        {  
	        	f.delete();
	        	String PDFfile = readpdf.getFilename(strDataPath);
	        	File f1 = new File(strDataPath+PDFfile);
	        	if(f1.exists())
	        	{
	        		f1.delete();
	        	}
	        }
	        
		}
	}
	// NextFuncBody
	private String SelectDeductibleValue(String Deductibles)
	{
		String DD1 = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "D/D");
		DD1 = DD1.trim();
		if (DD1.equalsIgnoreCase("Y"))
		{
			if (Deductibles.equalsIgnoreCase("0")){
				Deductibles ="$100 Disappearing Deductible";
				
			}
			else if (Deductibles.equalsIgnoreCase("100")){
				Deductibles ="$100 Disappearing Deductible";
				
			}
			else if (Deductibles.equalsIgnoreCase("200")){
				Deductibles ="$200 Disappearing Deductible";
				
			}
			else if (Deductibles.equalsIgnoreCase("400")){
				Deductibles ="$400 Disappearing Deductible";
				
			}else if (Deductibles.equalsIgnoreCase("250")){
				Deductibles ="$250 Disappearing Deductible";
				
			}
			else
				Deductibles = Deductibles;
				
		} else if(DD1.equalsIgnoreCase("N")) 
		{
			if (Deductibles.equalsIgnoreCase("0")){
				Deductibles ="$0 Deductible";
				              
				
			}
			if (Deductibles.equalsIgnoreCase("100")){
				Deductibles ="$100 Deductible";
				
			}
			else if (Deductibles.equalsIgnoreCase("200")){
				Deductibles ="$200 Deductible";
				
			}
			else if (Deductibles.equalsIgnoreCase("400")){
				Deductibles ="$400 Deductible";
				
			}else if (Deductibles.equalsIgnoreCase("250")){
				Deductibles ="$250 Deductible";
				
			}else
				Deductibles = Deductibles;
			
		}
		return Deductibles;
		
	}
		
	public String GetCoverageProperties(String CoverageAbbreviation) throws IOException
	{
		String Coverage =null;
		Properties prop = new Properties();
		String propFileName = strDataPath +"coverage.properties";
 
		prop.load(new FileInputStream(propFileName));

		Coverage = prop.getProperty(CoverageAbbreviation);
		if (Coverage == null){
			Coverage = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Coverage");
		}
		return Coverage;
		
	}
	private void Login() 
	{
		try
		{
			// Code for Single Signon button starts
			stepExecutor.clickButton("findElementByXPath", ".//*[@id='mdom_authconfigs']/button", webDriver,TestCase);
			Thread.sleep(3000);
			
			// Code for textboxes starts for entering username
			stepExecutor.enterTextValue("findElementById", "ctl00_ContentPlaceHolder1_UsernameTextBox", DataMap,"username", webDriver, TestCase);
			
			// Code for textboxes starts for entering password
			stepExecutor.enterTextValue("findElementById", "ctl00_ContentPlaceHolder1_PasswordTextBox", DataMap,"pw", webDriver, TestCase);
				
			//Code for Click Sign In button
			stepExecutor.clickButton("findElementByXPath", ".//input[@id='ctl00_ContentPlaceHolder1_SubmitButton']", webDriver,TestCase);
			Thread.sleep(5000);
			
			
		} catch(Exception e){
			System.out.println(e.getMessage());
		}
		
	}

	private void EnterLeasedetails() 
	{
		//Enter Finance Amount
		stepExecutor.enterTextValue("findElementById",
				"pg:frmCon:finance:pgsf:j_id778:j_id783", DataMap,
				"FinanceAmount", webDriver, "GLOW_Production");
		//APR / Money Factor %
		stepExecutor.enterTextValue("findElementById",
				"pg:frmCon:finance:pgsf:j_id784:j_id789", DataMap,
				"APR_MoneyFactorPercentage", webDriver, "GLOW_Production");
		//Total Of Payments
		stepExecutor.enterTextValue("findElementById",
				"pg:frmCon:finance:pgsf:j_id790:j_id795", DataMap,
				"TotalOfPayments", webDriver, "GLOW_Production");
		//Enter date of payment
		String dateofpayment = webDriver.findElementById("pg:frmCon:finance:pgsf:j_id801:reqfi9").getText();
		System.out.println(dateofpayment);
		if (dateofpayment.contains("[")){
			dateofpayment= dateofpayment.replace("[", "");
			
		}
		if (dateofpayment.contains("]")){
			dateofpayment= dateofpayment.replace("]", "");
			
		}
		webDriver.findElementById("pg:frmCon:finance:pgsf:j_id801:j_id806").clear();
	//	webDriver.findElementById("pg:frmCon:finance:pgsf:j_id801:reqfi9").click();
		webDriver.findElementById("pg:frmCon:finance:pgsf:j_id801:j_id806").sendKeys(dateofpayment);
		//Balloon Amt/Residual Value
		stepExecutor.enterTextValue("findElementById",
				"pg:frmCon:finance:pgsf:j_id807:j_id812", DataMap,
				"BallonAmt_ResidualValue", webDriver, "GLOW_Production");
	}

	public void launchapplication() {
		stepExecutor.launchApplication("URL", DataMap, webDriver);
		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		try {
			webDriver
					.findElement(
							By.cssSelector("div.loginbox_container > div.identity.first"))
					.click();
			// Code for textboxes starts for entering username
			stepExecutor.enterTextValue("findElementById", "username", DataMap,
					"username", webDriver, "GLOW_Production");
			// Code for textboxes starts for entering password
			stepExecutor.enterTextValue("findElementById", "password", DataMap,
					"pw", webDriver, "GLOW_Production");

			// Code for login button starts
			stepExecutor.clickButton("findElementById", "Login", webDriver,
					"GLOW_Production");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String openPDF() {
		
		//Open PDF 
		String pdfurl = null;
		try {
			String currenturl = webDriver.getCurrentUrl();
			if (webDriver.findElement(By.xpath("//td[6]/a/img")).isDisplayed())
			{
				stepExecutor.clickImage("findElementByXPath","//td[6]/a/img",webDriver,"GLOW_Production");
				webDriver.manage().timeouts().implicitlyWait(60,TimeUnit.SECONDS);
				Thread.sleep(60000);
			/*	SwitchHandleToNewWindow(webDriver, "https://dev.forms.thewarrantygroup.com/FormsService/pdf/");
				 pdfurl = webDriver.getCurrentUrl();
				System.out.println(pdfurl);*/
			
			}    
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return pdfurl;
	
	}
	
	public void SwitchHandleToNewWindow(WebDriver driver, String windowTitle)
	{
		ArrayList<String> tabs2 = new ArrayList<String> (webDriver.getWindowHandles());
		webDriver.switchTo().window(tabs2.get(1));
		webDriver.manage().timeouts().implicitlyWait(600,TimeUnit.SECONDS);
	}
			
	public String readPDF(String PDFurl) {
		String output = null;
		try {
			//webDriver.get(PDFurl);
			webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			URL url = new URL(webDriver.getCurrentUrl());
			BufferedInputStream fileToParse = new BufferedInputStream(url.openStream());
			PDFParser parser = new PDFParser(fileToParse);
			parser.parse();
			System.setProperty("org.apache.pdfbox.baseParser.pushBackSize", "990000");
			output = new PDFTextStripper().getText(parser.getPDDocument());
		//	System.out.println(output);
			webDriver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
			writePDFContenttotextfile(output);
			parser.getPDDocument().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	
	public Boolean checkPDFContent(String output ,String checkValue) throws IOException
	{
		Boolean result = false;
		int resultcount=0;
		System.out.println(output);
		  if(output.contains(checkValue))
		   {
				reporter.writeStepResult("PDF Verification","", checkValue, "Pass", "Expected text  is present in PDF file",true, webDriver);
				result = true;
		   } else {
			  // output = in.readLine();
			   reporter.writeStepResult(
						"PDF Verification",
						"", checkValue
								 , "Fail", "Expected text  is not present in PDF file",
						true, webDriver);
				
				result = false;
	
		   }
		   resultcount++;
		   return result;

	} 
		
	public String ExtractTextWithPattern(String pattern) throws FileNotFoundException{
		
		String filepath =strDataPath+"outputPDF.txt";
		String output = null;
		BufferedReader in = new BufferedReader(new FileReader(filepath));
	    try {
			  output = in.readLine();
		      while (!output.startsWith(pattern))
		      {
		    	  output = in.readLine();
		    	  
		      }
		      System.out.println(output);
		 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
		return output;
		
	}
			
	public void	writePDFContenttotextfile(String output)
	{
		try 
		{
		    BufferedWriter out = new BufferedWriter(new FileWriter(strDataPath+"outputPDF.txt"));
		   
		    out.write(output);  
		    out.close();		
				
		}catch (IOException e)
		{
		    System.out.println("Exception ");

		}
	}
	
	public void CreateOutputfile()
	{
		try{
	          File f=new File(strDataPath+"outputPDF.txt");
	          if (!f.exists()){
	        	  f.createNewFile();
	          }else {
	        	  f.delete();
	        	  f.createNewFile();
	          }
	        }
	        catch(Exception e){ 
	                System.out.println(e);
	        }
	}
	
	public void DeleteOutputfile()
	{
		try{
	          File f=new File(strDataPath+"outputPDF.txt");
	          f.delete();
	        }
	        catch(Exception e){ 
	                System.out.println(e);
	        }
	}
	
	
	public void WriteExcelDataFile(String fileName, int expectedRowNumber,
			String expectedToken, String strCellValue)
	{
		try 
		{
//			File dataFolder = new File(strAbsolutepath + "/data");
			//	fileName = strDataPath + "MasterSheet.xls";
				File f = new File(fileName);
				FileInputStream fsIP= new FileInputStream(f);
				 HSSFWorkbook wb = new HSSFWorkbook(fsIP);
		         
		         HSSFSheet dataSheet = wb.getSheet(TestCase);
		         HSSFRow dataRow = dataSheet.getRow(0);
		         Cell cell = null; 
		        // int i= worksheet.getRow(expectedRowNumber).getLastCellNum();
		      //   cell = worksheet.getRow(expectedRowNumber).getCell(i);  
		         int totalCells = dataRow.getLastCellNum();
					for (int i = 0; i < totalCells; i++)
					{
						String strData = dataRow.getCell(i).toString();
						if (strData.equals(expectedToken)) {
							 cell = dataSheet.getRow(expectedRowNumber).getCell(i);  
							 if (cell == null) {
								    // New cell
								 cell = dataSheet.getRow(expectedRowNumber).createCell(i, Cell.CELL_TYPE_STRING);
								    cell.setCellValue(strCellValue);
								}
							 cell.setCellValue(strCellValue);
						}	
		           
		         fsIP.close(); 
					}
		         FileOutputStream output_file =new FileOutputStream(new File(fileName)); 
		         wb.write(output_file); 
		           
		         output_file.close();  
		} catch( Exception e){
			e.printStackTrace();
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = webDriver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}

	private boolean isElementPresent(By by) {
		try {
			webDriver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/// <summary>
	/// An expectation for checking whether an element is visible.
	/// </summary>
	/// <param name="locator">The locator used to find the element.</param>
	/// <returns>The <see cref="IWebElement"/> once it is located, visible and clickable.</returns>
	public void elementclickable(String locator)
	{
	     //Boolean flag = false;
	    {
	        WebElement element = webDriver.findElementById(locator);
	        if (element != null && element.isDisplayed() && element.isEnabled()){
	        	wait.until(ExpectedConditions.elementToBeClickable(By.id(locator)));
	        	
	       // 	return flag;
	     }
	        		   
	}
}
		
	public Boolean FindDescendantsofCheckbox(String ProductType,String SubProductTypeSelection)
	{
		Boolean isFlag = false;
		try
		{
			webDriver.findElementByXPath("//tr[td[contains(text(),'"+ProductType+"')]]/td/input[@type='checkbox']").click();
			if (webDriver.findElementByXPath("//tr[td[contains(text(),'"+ProductType+"')]]/td/input[@type='checkbox']").isSelected()){
				reporter.writeStepResult("Product selection", "Select Product",ProductType, "Pass", "", true, webDriver);
			}else {
				reporter.writeStepResult("Product selection", "Select Product",ProductType, "Fail", "", true, webDriver);
			}
			webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			Thread.sleep(9000);
			if (webDriver.findElementByXPath("//tr[td[contains(text(),'"+ProductType+"')]]/td/input[@type='checkbox']").isSelected())
			{
				if (ProductType.contains("Service Contract"))
				{
					
					try {
						//webDriver.findElementByXPath("//tr[td[contains(text(),'"+ProductType+"')]]/td/input[@type='checkbox']").;
						new Select(webDriver.findElementByXPath("//td[contains(text(),'"+ProductType+"')]/parent::tr/following-sibling::tr[2]//select")).selectByVisibleText(SubProductTypeSelection);
						webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
						Thread.sleep(15000);
						isFlag = true;
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (ProductType.contains("Mechanical Repair"))
				{
					try {
						//new Select(webDriver.findElementById("pg:frm:ProductBlock:rpt:1:selectedVal")).selectByVisibleText(SubProductTypeSelection);
						new Select(webDriver.findElementByXPath("//td[contains(text(),'"+ProductType+"')]/parent::tr/following-sibling::tr[2]//select")).selectByVisibleText(SubProductTypeSelection);
						webDriver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
						Thread.sleep(18000);
						isFlag = true;
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} 
				if (ProductType.contains("GAP"))
				{
					try {
						//new Select(webDriver.findElementById("pg:frm:ProductBlock:rpt:3:selectedVal")).selectByVisibleText(SubProductTypeSelection);
						new Select(webDriver.findElementByXPath("//td[contains(text(),'"+ProductType+"')]/parent::tr/following-sibling::tr[2]//select")).selectByVisibleText(SubProductTypeSelection);
						Thread.sleep(8000);	
						String Terms = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Terms (Months)");
						if (Terms.contains(".0")){
							Terms = Terms.replace(".0", "");
						}
						webDriver.findElement(By.xpath("//td[contains(text(),'"+ProductType+"')]/parent::tr/following-sibling::tr[2]//input")).clear();
						webDriver.findElement(By.xpath("//td[contains(text(),'"+ProductType+"')]/parent::tr/following-sibling::tr[2]//input")).sendKeys(Terms);
						webDriver.findElement(By.xpath("//td[contains(text(),'"+ProductType+"')]/parent::tr/following-sibling::tr[2]//input")).sendKeys(Keys.TAB);
						Thread.sleep(8000);	
						isFlag = true;
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} 
				if (SubProductTypeSelection != null){
					System.out.println("No sub-options are present for selected product");
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return isFlag;
		
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

	public String GetProperties(String StateAbbreviation) throws IOException
	{
		String State=null;
		Properties prop = new Properties();
		String propFileName = strDataPath +"States.properties";
 
		prop.load(new FileInputStream(propFileName));

		State = prop.getProperty(StateAbbreviation);
	
		return State;
		
	}
	
	private void EnterLienholderDetails() throws InterruptedException, IOException
	{
		//Enter Lienholder name, address, city, state, country, zip code,phone
		stepExecutor.selectListValueByContainsValue("findElementByXpath", ".//select[@id='pg:frmCon:pbl:pbls:pbsil:lien']","SelectLienholder", webDriver, "GLOW_Production",rownumber);
		
		stepExecutor.enterTextValue("findElementById",
				"pg:frmCon:pbl:pbls:namelien:namelien1", DataMap,
				"Lienholder", webDriver, "GLOW_Production");
		stepExecutor.enterTextValue("findElementById",
				"pg:frmCon:pbl:pbls:addl1:ladd1", DataMap,
				"LienholderAddress", webDriver, "GLOW_Production");
		stepExecutor.enterTextValue("findElementById",
				"pg:frmCon:pbl:pbls:city:lcity", DataMap,
				"LienholderCity", webDriver, "GLOW_Production");
		String LienholderStateAbbr = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "LienholderState");
		LienholderStateAbbr = GetProperties(LienholderStateAbbr).trim();
		new Select(webDriver.findElementById("pg:frmCon:pbl:pbls:statel:lstate")).selectByVisibleText(LienholderStateAbbr);
		stepExecutor.enterTextValue ("findElementById",
				"pg:frmCon:pbl:pbls:zipl:lzip", DataMap,
				"LienholderZip", webDriver, "GLOW_Production");
		
		stepExecutor.enterTextValue ("findElementById",
				"pg:frmCon:pbl:pbls:countryl:lcountry", DataMap,
				"LienholderCountry", webDriver, "GLOW_Production");
		
		stepExecutor.enterTextValue("findElementById",
				"pg:frmCon:pbl:pbls:tell:lphone", DataMap,
				"Phone", webDriver, "GLOW_Production");
		
		//Click on Save Lienholder button 
		stepExecutor.clickButton("findElementByXPath", ".//input[@id='pg:frmCon:pbl:pbls:savelienholder:j_id853']", webDriver, "GLOW_Production");
		Thread.sleep(8000);
	}

	@SuppressWarnings("unused")
	private void RadioButtonYesNoClick() throws InterruptedException
	{
		//Boolean isFlag = false;
		String VIN = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "VIN");
		String VINDecoding = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "VINDecoding");
		if (VINDecoding.contains("Yes"))
		{
			WebElement radiobutton = webDriver.findElement(By.id("pg:frm:VehicleBlock:vinNopbs:vinRadio:0"));
			if (webDriver.findElement(By.id("pg:frm:VehicleBlock:vinNopbs:vinRadio:0")).isDisplayed())
			{
				radiobutton.isSelected();
				if (true){
					webDriver.findElementByXPath("//input[@id='pg:frm:VehicleBlock:vinBLK:vinId:j_id168']").sendKeys(VIN);
				    // Press "TAB" key
					webDriver.findElementByXPath("//input[@id='pg:frm:VehicleBlock:vinBLK:vinId:j_id168']").sendKeys(Keys.TAB);
				

					stepExecutor.clickButton("findElementByXPath", ".//input[@id='pg:frm:VehicleBlock:vinBLK:vinId:j_id170']", webDriver, "GLOW_Production");
					webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
					Thread.sleep(20000);
					
				}else {
					radiobutton.click();
					webDriver.findElementByXPath("//input[@id='pg:frm:VehicleBlock:vinBLK:vinId:j_id168']").sendKeys(VIN);
				    // Press "TAB" key
					webDriver.findElementByXPath("//input[@id='pg:frm:VehicleBlock:vinBLK:vinId:j_id168']").sendKeys(Keys.TAB);
				

					stepExecutor.clickButton("findElementByXPath", ".//input[@id='pg:frm:VehicleBlock:vinBLK:vinId:j_id170']", webDriver, "GLOW_Production");
					webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
					Thread.sleep(20000);
				}
			}
		}
		if (VINDecoding.contains("No"))
		{
			WebElement radiobutton1 = webDriver.findElement(By.id("pg:frm:VehicleBlock:vinNopbs:vinRadio:1"));
			if (webDriver.findElement(By.id("pg:frm:VehicleBlock:vinNopbs:vinRadio:1")).isDisplayed())
			{

				radiobutton1.isSelected();
			
				// This will check that if the bValue is True means if the first radio button is selected
				if (true)
				{
				// This will select Second radio button, if the first radio button is selected by default
				// Click "No" on VIN Decoding button
				radiobutton1.click();
				Thread.sleep(8000);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='pg:frm:VehicleBlock:DecodeBlock2:NonvinSI:j_id230']")));
				
				// Enter 17 characters VIN
		
				webDriver.findElementByXPath("//input[@id='pg:frm:VehicleBlock:DecodeBlock2:NonvinSI:j_id230']").sendKeys(VIN);
				Thread.sleep(5000);
			    // Press "TAB" key
				webDriver.findElementByXPath("//input[@id='pg:frm:VehicleBlock:DecodeBlock2:NonvinSI:j_id230']").sendKeys(Keys.TAB);
				Thread.sleep(15000);
				wait.until(ExpectedConditions.elementToBeClickable(By.name("pg:frm:VehicleBlock:DecodeBlock2:j_id232:j_id238")));
				

				// Select Model year
				stepExecutor.selectListValue("findElementByName",
						"pg:frm:VehicleBlock:DecodeBlock2:j_id232:j_id238",DataMap,
						 "Mod Yr", webDriver, "GLOW_Production");
				
				// Select Make of vehicle
				stepExecutor.selectListValue("findElementById",
						"pg:frm:VehicleBlock:DecodeBlock2:j_id241:vMake", DataMap,
						"Make", webDriver, "GLOW_Production");
				webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				Thread.sleep(12000);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='pg:frm:VehicleBlock:DecodeBlock2:modelsecItem:j_id252']")));
				
				// Select Model of vehicle
				stepExecutor.selectListValueByContainsValue("findElementByXpath",
						"//select[@name='pg:frm:VehicleBlock:DecodeBlock2:modelsecItem:j_id252']",
						 "Vehicle Code", webDriver, "GLOW_Production",rownumber);
				
				}
			} 
		}

	}

	

}
