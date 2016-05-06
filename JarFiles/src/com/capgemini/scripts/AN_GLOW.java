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
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.security.acl.Owner;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;

import junit.framework.Assert;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

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
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class AN_GLOW {
	
	public String TestCase="AN_GLOW";
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
	int rownumber = 0;
	private static String strAbsolutepath = new File("").getAbsolutePath();
	private static String strDataPath = strAbsolutepath + "/data/";
	String strDataFileName = utils.getDataFile("AN_GLOW");
	private boolean acceptNextAlert = true;
	String strStopTime;
	public static final String DATE_FORMAT = "MM/dd/yyyy";
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
			rownumber =i;
			strDataFileName = strDataPath + "MasterSheet.xls";
			String startesttime = GetTestStartTime();
			WriteExcelDataFile(strDataFileName,rownumber,"StartTime",startesttime);
			DataMap=readExcel.loadDataMap(rownumber);
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
			rownumber=i;
			DataMap=readExcel.loadDataMap(rownumber);
			//readExcel.readByIndex(i);
			//webDriver.switchTo().activeElement();
			//System.out.println(capabilities.getBrowserName());
			//reporter.setStrBrowser(capabilities.getBrowserName());
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

			
	public void testcaseMain() throws InterruptedException, BiffException,
			Exception {
		
		stepExecutor.launchApplication("URL", DataMap, webDriver);
		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		Assert.assertEquals("AutoNation Login", webDriver.getTitle());
		try
		{
			// Code for textboxes starts for entering username
			stepExecutor.enterTextValue("findElementById", "username", DataMap,"username", webDriver, "AN_GLOW");
			
			// Code for textboxes starts for entering password
			stepExecutor.enterTextValue("findElementById", "password", DataMap,"pw", webDriver, "AN_GLOW");
					
			// Code for login button starts
			stepExecutor.clickButton("findElementByXPath", ".//*[@id='Login']", webDriver,"AN_GLOW");
			Thread.sleep(8000);
			
			//Click on Quotes link tab 
			stepExecutor.clickLinkValue("findElementByLinkText","Quotes", DataMap,"SelectTypeOfQuote", webDriver, "AN_GLOW");
			webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			
			//Click on create new Quote button
			stepExecutor.clickButton("findElementByXPath", ".//*[@id='hotlist']/table/tbody/tr/td[2]/input", webDriver, "AN_GLOW");
			Thread.sleep(8000);
			
			// Search for dealer lookup, product selection and create a quote
			strDataFileName = strDataPath + "MasterSheet.xls";
			String DealerName = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "DealerAccountName");
			
			if (verify.verifyElementIsPresentCheck(webDriver, "//input[@id='pg:frmPage1:ProductBlock:dealername']", "xpath"))
			{
				WebElement textbox = webDriver.findElement(By.xpath("//input[@id='pg:frmPage1:ProductBlock:dealername']"));
				webDriver.findElementByXPath("//input[@id='pg:frmPage1:ProductBlock:dealername']").sendKeys(DealerName);
				textbox.sendKeys(Keys.TAB);
				//textbox.sendKeys(Keys.RETURN);
			}
		
			String ProductType = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "ProductToSelect");
			String SubProductTypeSelection = scriptExecutor.readDataFile(strDataFileName, TestCase,rownumber, "SubProductandContractTypes");
			Thread.sleep(12000);
			//Code for checkboxes starts for Product selection for entered dealer in textbox
			if (FindDescendantsofCheckbox(ProductType, SubProductTypeSelection)){
				reporter.writeStepResult("Product selection", "Select Product",ProductType, "Pass", "Product selected", true, webDriver);
			}else{
				reporter.writeStepResult("Product selection", "Select Product",ProductType, "fail", "Product not selected", true, webDriver);
			}
			webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			Thread.sleep(20000);
			
			// Enter 17 characters VIN
			RadioButtonYesNoClick();
			/*String VIN = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "VIN");
			webDriver.findElementByXPath("//input[@name='pg:frmPage1:VehicleBlock:vinBLK:vinId:j_id187']").sendKeys(VIN);
		    // Press "TAB" key
			webDriver.findElementByXPath("//input[@name='pg:frmPage1:VehicleBlock:vinBLK:vinId:j_id187']").sendKeys(Keys.TAB);
		

			stepExecutor.clickButton("findElementByXPath", "//input[@id='pg:frmPage1:VehicleBlock:vinBLK:vinId:j_id188']", webDriver, "AN_GLOW");
			webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			Thread.sleep(15000);*/

			// Enter Odometer value
			stepExecutor.enterTextValue("findElementById","pg:frmPage1:VehicleBlock:j_id299:j_id306:j_id310", DataMap,"Odometer", webDriver, "AN_GLOW");
			Thread.sleep(3000);
			//Enter in-service date
			String InServiceDate =scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "InServiceDate");; 
			InServiceDate= convertStringToDate(InServiceDate);
			System.out.println(InServiceDate);
			webDriver.findElementByXPath(".//input[@id='pg:frmPage1:VehicleBlock:j_id299:j_id312:serviceDate']").sendKeys(InServiceDate);
			webDriver.findElementByXPath(".//input[@id='pg:frmPage1:VehicleBlock:j_id299:j_id312:serviceDate']").click();
			Thread.sleep(3000);
			//press 'TAB' key
			webDriver.findElementByXPath(".//input[@id='pg:frmPage1:VehicleBlock:j_id325:j_id326:j_id331']").sendKeys(Keys.TAB);
			String FirstName = scriptExecutor.readDataFile(strAbsolutepath, TestCase,rownumber, "First Name");
			String LastName = scriptExecutor.readDataFile(strAbsolutepath, TestCase,rownumber, "Last Name");
			webDriver.findElementByXPath("//input[@id='pg:frmPage1:VehicleBlock:j_id325:j_id326:j_id331']").sendKeys(FirstName);
			webDriver.findElementByXPath(".//input[@id='pg:frmPage1:VehicleBlock:j_id325:j_id326:j_id331']").sendKeys(Keys.TAB);
			webDriver.findElementByXPath(".//*[@id='pg:frmPage1:VehicleBlock:j_id325:j_id332:j_id337']").sendKeys(Keys.TAB);
			webDriver.findElementByXPath("//input[@id='pg:frmPage1:VehicleBlock:j_id325:LastNameRegion:j_id342']").sendKeys(LastName);
			// Enter first name and last name
    		// click on Get Rates button
			stepExecutor.clickButton("findElementByXPath",".//input[@id='pg:frmPage1:GetRatesButton']", webDriver, "AN_GLOW");
			webDriver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
			Thread.sleep(5000);
			// select coverage
			// select coverage
			String coverage = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Coverage");
			coverage = coverage.trim();
			coverage = GetCoverageProperties(coverage).trim();
			Select dd = null;
			System.out.println(coverage);
			
		//	new Select(webDriver.findElementByXPath(".//*[@id='pg:frm:QuoteDP:j_id352:0:j_id355']/select")).selectByVisibleText(Coverage);
			dd = new Select(webDriver.findElementByXPath(".//*[@id='pg:frmPage1:QuoteDP:j_id357:0:j_id360']/select"));
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
			/*String coverage = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Coverage");
			coverage = SelectCoverage(coverage);
			new Select(webDriver.findElementByXPath(".//*[@id='pg:frmPage1:QuoteDP:j_id360:0:j_id363']/select")).selectByVisibleText(coverage);*/
		/*	stepExecutor.selectListValue("findElementByXpath",
					".//*[@id='pg:frmPage1:QuoteDP:j_id360:0:j_id363']/select",DataMap,
					 "Coverage", webDriver, "AN_GLOW");*/
			Thread.sleep(8000);
			
			// select terms: months
	
			String termsmonths = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "Terms (Months)");
			if (termsmonths.contains(".0")){
				termsmonths = termsmonths.replace(".0","");
			}
			new Select(webDriver.findElementByXPath(".//*[@id='pg:frmPage1:QuoteDP:j_id357:0:j_id363']/select")).selectByVisibleText(termsmonths);
			/*stepExecutor.selectListValue("findElementByXpath",
					".//*[@id='pg:frmPage1:QuoteDP:j_id360:0:j_id366']/select", DataMap,
					"Terms (Months)", webDriver, "AN_GLOWProduction");*/
			webDriver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			Thread.sleep(8000);
			// select terms: miles
			String miles = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "Terms (Miles)");
			if (miles.contains(".0")){
				miles = miles.replace(".0","");
				}
			miles = miles +"000";
			
			new Select(webDriver.findElementByXPath(".//*[@id='pg:frmPage1:QuoteDP:j_id357:0:j_id366']/select")).selectByVisibleText(miles);
			Thread.sleep(7000);
			
			// select deductible interval
			String deductibles = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Deductibles");
			deductibles = SelectDeductibleValue(deductibles);
			//stepExecutor.selectListValueByContainsValue("findElementByXpath", "//*[@id='pg:frmPage1:QuoteDP:j_id360:0:j_id372']/select", deductibles, webDriver, "AN_GLOW", rownumber);
			new Select(webDriver.findElementByXPath(".//*[@id='pg:frmPage1:QuoteDP:j_id357:0:j_id369']/select")).selectByVisibleText(deductibles);
		/*	stepExecutor.selectListValue("findElementByXpath",
					".//*[@id='pg:frmPage1:QuoteDP:j_id360:0:j_id372']/select"
					, DataMap,"Deductibles",
					webDriver, "AN_GLOW");*/
			webDriver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			Thread.sleep(8000);
		
			// capture dealer cost=store cost in string
			String dcost = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "Billed Client Cost");
			String actualdealercost = webDriver.findElementByXPath(
					".//*[@id='pg:frmPage1:QuoteDP:j_id357:0:DC2']").getText();
			actualdealercost = actualdealercost.replace("$", "");
			actualdealercost = actualdealercost + ".00";
			WriteExcelDataFile(strDataFileName,rownumber,"Salesforce_StoreCost",actualdealercost);
				if (actualdealercost.equals(dcost)) {
					reporter.writeStepResult(
							System.getProperty("StoreCost"),
							"Verify dealer cost is present in the element", "Expected: "
									+ dcost, "Pass", "Expected text  is present",
							true, webDriver);
					WriteExcelDataFile(strDataFileName,rownumber,"Salesforce_StoreCostResults","PASS");
				} else {
					reporter.writeStepResult(
							System.getProperty("Storecost"),
							"Verify dealercost is present in the element", "Expected: "
									+ dcost, "Fail",
							"Expected text  is not present (Actual: "
									+ actualdealercost + ")", true, webDriver);
					WriteExcelDataFile(strDataFileName,rownumber,"Salesforce_StoreCostResults","FAIL");
			}
			String ccost = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "Default Commission");
			ccost = ccost.replace("$", "");
			ccost = ccost + ".00";
			// capture retail cost = MSRP in string value
			String rcost = scriptExecutor.readDataFile(strDataFileName,TestCase,
					rownumber, "SRP Cost");
			String actualretailcost = webDriver.findElementByXPath(
					"//span[@id='pg:frmPage1:QuoteDP:j_id357:0:RC2']").getText();
			
			actualretailcost = actualretailcost.replace("$", "");
			actualretailcost = actualretailcost + ".00";
			WriteExcelDataFile(strDataFileName,rownumber,"Salesforce_MSRP",actualretailcost);
			if (actualretailcost.equals(ccost)) {
				reporter.writeStepResult(
						System.getProperty("Retail Cost"),
						"Verify retail cost is present in the element", "Expected: "
								+ rcost, "Pass", "Expected text  is present",
						true, webDriver);
				WriteExcelDataFile(strDataFileName,rownumber,"Salesforce_MSRPCostresults","PASS");
			} else {
				reporter.writeStepResult(
						System.getProperty("Retailost"),
						"Verify retail cost is present in the element", "Expected: "
								+ dcost, "Fail",
						"Expected text  is not present (Actual: "
								+ actualretailcost + ")", true, webDriver);
				WriteExcelDataFile(strDataFileName,rownumber,"Salesforce_MSRPCostresults","FAIL");
			}
			//Verify Selling price
		
			String actualccost = verify.verifyandstoreElementTextPresent(webDriver, ".//input[@id='pg:frmPage1:QuoteDP:j_id357:0:xyz']","xpath");
			actualccost =  actualccost.replace(",", "");
			actualccost =  actualccost + ".00";
			WriteExcelDataFile(strDataFileName,rownumber,"Salesforce_SellingPrice",actualccost);
			if (actualccost.equals(ccost )) {
				reporter.writeStepResult(
						System.getProperty("Selling Price"),
						"Verify customer cost is present in the element", "Expected: "
								+ actualccost, "Pass", "Expected text  is present",
						true, webDriver);
				WriteExcelDataFile(strDataFileName,rownumber,"Salesforce_SellingPriceResults","PASS");
			} else {
				reporter.writeStepResult(
						System.getProperty("Selling Price"),
						"Verify customer cost is present in the element", "Expected: "
								+ actualccost, "Fail",
						"Expected text  is not present (Actual: "
								+ actualccost + ")", true, webDriver);
				WriteExcelDataFile(strDataFileName,rownumber,"Salesforce_SellingPriceResults","FAIL");
			}
			
			
			//Click on Save and Continue button to navigate to step2 of creating quote details page
			stepExecutor.clickButton("findElementByXPath",
					".//input[@id='pg:frmPage1:savebtn']", webDriver, "AN_GLOW");
			Thread.sleep(4000);
			
			// Enter City,address,phone number, zip,Country and email, Select State
			stepExecutor.enterTextValue("findElementById",
					"pg:frmPage2:contactPB:contactPBs:pbsBA1:ba1", DataMap,
					"Address", webDriver, "AN_GLOW");
			stepExecutor.enterTextValue("findElementById",
					"pg:frmPage2:contactPB:contactPBs:pbsbCity:bCity", DataMap,
					"City", webDriver, "AN_GLOW");
			String StateAbbr = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "State");
			StateAbbr = GetProperties(StateAbbr);
			StateAbbr = StateAbbr.trim();
			new Select(webDriver.findElementById("pg:frmPage2:contactPB:contactPBs:pbsBState:bState")).selectByVisibleText(StateAbbr);
			/*stepExecutor.selectListValue("findElementById",
					"pg:frmPage2:contactPB:contactPBs:pbsBState:bState", DataMap,
					"State", webDriver, "AN_GLOW");
*/
			stepExecutor.selectListValue("findElementByXpath", ".//select[@id='pg:frmPage2:contactPB:contactPBs:pbsBCountry:bCountry']",DataMap,"Country", webDriver, "AN_GLOW");	
			
			stepExecutor.enterTextValue ("findElementById",
					"pg:frmPage2:contactPB:contactPBs:pbsbZip:bZip", DataMap,
					"Zip Code", webDriver, "AN_GLOW");
			
			stepExecutor.enterTextValue("findElementById",
					"pg:frmPage2:contactPB:contactPBs:pbsPh1:bph1", DataMap,
					"Phone", webDriver, "AN_GLOW");
			stepExecutor.enterTextValue("findElementById",
					"pg:frmPage2:contactPB:contactPBs:pbsEmail1:bEmail1",
					DataMap, "Email", webDriver, "AN_GLOW");
			
			//Select Type of contract = "Cash"
			webDriver.findElement(By.id("pg:frmPage2:contactPB:contactPBs:pbsEmail1:bEmail1")).sendKeys(Keys.TAB);
			webDriver.findElement(By.xpath(".//input[@id='pg:frmPage2:j_id732']")).sendKeys(Keys.TAB);
			stepExecutor.selectListValue("findElementByXpath", "//select[@id='pg:frmPage2:finance:pgsf:pbsif:conType']",DataMap,"TypeOfContract", webDriver, "AN_GLOW");
			String  TypeOfContract = scriptExecutor.readDataFile(strDataFileName, "AN_GLOW", rownumber, "TypeOfContract");
			if (TypeOfContract.contains("Finance"))
			{
				stepExecutor.selectListValue("findElementByXpath", "//select[@id='pg:frmPage2:finance:pgsf:pbsif:conType']",DataMap,"TypeOfContract", webDriver, "AN_GLOW");
				EnterLienholderDetails();
				
			}
			
			
			//Click on Save and Continue button to go to purchase details page
			if (webDriver.findElement(By.xpath("//input[@id='pg:frmPage2:j_id732']")).isDisplayed())
			{
				webDriver.findElement(By.xpath("//input[@id='pg:frmPage2:j_id732']")).click();
			}
			Thread.sleep(9000);
			//stepExecutor.clickButton("findElementById", "pg:frmPage2:j_id735", webDriver, "AN_GLOW");
			
			String Quoteid= verify.verifyandstoreElementTextPresent(webDriver, "//span[@id='pg:frmSummaryInfo:productPanel']/table/tbody/tr/td/table/tbody/tr[4]/td[2]","xpath");
			WriteExcelDataFile(strDataFileName,rownumber,"QuoteID",Quoteid);
			//Assert.assertEquals("Press Submit to SSC to generate product agreement and forward to SSC queue for registration.", webDriver.findElement(By.cssSelector("span.flag.right")).getText());
			
			//Click on Submit to SSC button to purchase the quote
		 stepExecutor.clickButton("findElementByXPath", ".//input[@id='pg:frmPage3:detail:j_id1049:remit']", webDriver, "AN_GLOW");
			//stepExecutor.clickButton("findElementById", "pg:frmPage3:detail:j_id1045:remit", webDriver, "AN_GLOW");
			Thread.sleep(3000);
			//Assert.assertEquals("The VPP agreement has been sent to the AN Shared Service Center for registration. Click the PDF icon to generate the form with a valid VPP agreement number.", webDriver.findElement(By.cssSelector("h4")).getText());

		    //Open PDF
			System.out.println("PDF Verification starts");
			termsmonths = scriptExecutor.readDataFile(strDataFileName, TestCase,rownumber, "Terms (Months)");
			if (termsmonths.contains(".0")){
				termsmonths = termsmonths.replace(".0", "");
			}
			
			String price = 	actualccost;
			if (price.contains(".00"))
			{
				price = price.replace(".00", "");
			}
			price = "$" + price;
			System.out.println(price);
			String Deductibles = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "Deductibles");
			Deductibles = "$" +Deductibles;
			if (Deductibles.contains(".0")){
				Deductibles = Deductibles.replace(".0", "");
			}
			String ExpirationDate = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "ExpirationDate");
			SimpleDateFormat dt1 = new SimpleDateFormat("mm/dd/yyyy"); 
			Date date = dt1.parse(ExpirationDate);  
			SimpleDateFormat dt2 = new SimpleDateFormat("mm/dd/yyyy");
			ExpirationDate =dt2.format(date) ;
			String ExpirationMileage = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "ExpirationMileage");
			if (ExpirationMileage.contains(".0")){
				ExpirationMileage = ExpirationMileage.replace(".0","");
			}
			miles = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "Terms (Miles)");
			if (miles.contains(".0")){
				miles = miles.replace(".0","");
				}
			miles = miles+ ",000";
			String price1 = price.substring(0,2);
			String price2 = price.substring(2, price.length());
			price = price1 +","+price2;
			System.out.println(miles +price +coverage + Deductibles+ExpirationDate+ ExpirationMileage);
			
			String PdfUrl= openPDF();
			System.out.println(webDriver.getWindowHandles().size());
			String Verificationpoints = null;
			if (webDriver.getWindowHandles().size() > 1)
			{
				Verificationpoints = readPDF(PdfUrl);
			} else {
				//String path = readpdf.openPDFfromfilelocation(strDataPath);
				String filename = readpdf.getFilename(strDataPath);
				Verificationpoints = readpdf.readPDFFromFileLocation(strDataPath+filename);
				
			}
			String ContractNumber =ExtractTextWithPattern("0V");
			WriteExcelDataFile(strDataFileName,rownumber,"ContractNumber",ContractNumber);
			if (checkPDFContent(Verificationpoints,termsmonths)){
				WriteExcelDataFile(strDataFileName,rownumber,"PDF_TermsMonthsResults","PASS");
			}else {
				WriteExcelDataFile(strDataFileName,rownumber,"PDF_TermsMonthsResults","FAIL");
			}
			
			if (checkPDFContent(Verificationpoints,miles)){
				WriteExcelDataFile(strDataFileName,rownumber,"PDF_TermsMilesResults","PASS");
			}else {
				WriteExcelDataFile(strDataFileName,rownumber,"PDF_TermsMilesResults","FAIL");
			}
			coverage = coverage.toUpperCase();
			if (checkPDFContent(Verificationpoints,coverage)){
				WriteExcelDataFile(strDataFileName,rownumber,"PDFCoverageResults","PASS");
			}else {
				WriteExcelDataFile(strDataFileName,rownumber,"PDFCoverageResults","FAIL");
			}
			if (checkPDFContent(Verificationpoints,Deductibles)){
				WriteExcelDataFile(strDataFileName,rownumber,"PDFDeductibleResults","PASS");
			}else {
				WriteExcelDataFile(strDataFileName,rownumber,"PDFDeductibleResults","FAIL");
			}
			if (checkPDFContent(Verificationpoints,price)){
				WriteExcelDataFile(strDataFileName,rownumber,"PDFPriceResults","PASS");
			}else {
				WriteExcelDataFile(strDataFileName,rownumber,"PDFPriceResults","FAIL");
			}
			String ExpiredDate = ExtractPDFValue(strDataPath+"outputPDF.txt","Expiration Date");
			WriteExcelDataFile(strDataFileName,rownumber,"PDFExpiredDate",ExpiredDate);
			if (ExpiredDate.equals(ExpirationDate)) {
				reporter.writeStepResult(
						System.getProperty("ExpirationDate"),
						"Verify ExpirationMileage is present in the element", "Expected: "
								+ ExpiredDate, "Pass", "Expected text  is present",
						true, webDriver);
		
				WriteExcelDataFile(strDataFileName,rownumber,"PDFExpirationDateResults","PASS");
				
			} else {
				reporter.writeStepResult(
						System.getProperty("ExpirationMileage"),
						"Verify ExpirationMileage is present in the element", "Expected: "
								+ ExpiredDate, "Fail",
						"Expected text  is not present (Actual: "
								+  ExpiredDate + ")", true, webDriver);
				WriteExcelDataFile(strDataFileName,rownumber,"PDFExpirationDateResults","FAIL");
			}
		/*	if (checkPDFContent(Verificationpoints,ExpirationDate)){
				WriteExcelDataFile(strDataFileName,rownumber,"PDFExpirationDateResults","PASS");
			}else {
				WriteExcelDataFile(strDataFileName,rownumber,"PDFExpirationDateResults","FAIL");
			}*/
			String ExpiredMileage = ExtractPDFValue(strDataPath+"outputPDF.txt","Expiration Mileage");
			ExpiredMileage = ExpiredMileage.replace(",","");
			WriteExcelDataFile(strDataFileName,rownumber,"PDFExpiredMileage",ExpiredMileage);
			if (ExpiredMileage.equals(ExpirationMileage )) {
				reporter.writeStepResult(
						System.getProperty("ExpirationMileage"),
						"Verify ExpirationMileage is present in the element", "Expected: "
								+ ExpiredMileage, "Pass", "Expected text  is present",
						true, webDriver);
				WriteExcelDataFile(strDataFileName,rownumber,"PDFExpirationMileageResults","PASS");
			} else {
				reporter.writeStepResult(
						System.getProperty("ExpirationMileage"),
						"Verify ExpirationMileage is present in the element", "Expected: "
								+ ExpiredMileage, "Fail",
						"Expected text  is not present (Actual: "
								+  ExpiredMileage + ")", true, webDriver);
				WriteExcelDataFile(strDataFileName,rownumber,"PDFExpirationMileageResults","FAIL");
			}
		/*	if (checkPDFContent(Verificationpoints,ExpirationMileage)){
				WriteExcelDataFile(strDataFileName,rownumber,"PDFExpirationMileageResults","PASS");
			}else {
				WriteExcelDataFile(strDataFileName,rownumber,"PDFExpirationMileageResults","FAIL");
			}*/
			System.out.println("PDF Verification completed successfully");
			//switch back to purchase screen for Remit scenario
			if (webDriver.getWindowHandles().size() > 1)
			{
				ArrayList<String> tabs3 = new ArrayList<String> (webDriver.getWindowHandles());
				webDriver.switchTo().window(tabs3.get(0));
			}
			System.out.println(Quoteid);
            RemitQuoteCompletion(Quoteid);
			System.out.println("Remit scenario completed successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			
			File f=new File(strDataPath+"outputPDF.txt");
	        if(f.exists())
	        {  
	        	f.delete();
	        }
	        //delete pdf document
	        String PDFfile = readpdf.getFilename(strDataPath);
	        if (PDFfile != null)
	        {
	        	/*FileInputStream fileToParse= new FileInputStream(new File(strDataPath+PDFfile));
	        	  PdfReader pdffile = new PdfReader(fileToParse);
	  	        pdffile.close();*/
	  	     //   COSDocument pdffile1 = new COSDocument();
	  	     //   pdffile1.close();
	  			/*PDFParser parser = new PDFParser(fileToParse);
	  			parser.parse();
	  			parser.getDocument().close();
	  			parser.getPDDocument().close();
	  	        fileToParse.close();*/
	  	      
	  			//if (PDFfile != null)
	          //	{	
	          		File f1 = new File(strDataPath+PDFfile);
	          		//FileOutputStream fos = new FileOutputStream(strDataPath+PDFfile);
	          		
	          		//fos.close();
	          	//FileUtils.copyDirectory(strDataPath+PDFfile,strDataPath+"results");
	          		if(f1.exists())
	          		{
	          			f1.delete();
	          		}
	          	//}
	  		}
	        }
		
	}
	//NextFuncBody
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
	
	
/*	private String SelectCoverage(String Coverage)
	{
		Coverage = Coverage.substring(0, 3);
		if (Coverage.contains("GLD")){
			Coverage ="GOLD";
			
		}
		if (Coverage.contains("PLT")){
			Coverage ="PLATINUM";
			
		}
		if (Coverage.contains("SLV")){
			Coverage ="SILVER";
			
		}
		return Coverage;
		
	}*/
	
	
	private String SelectDeductibleValue(String Deductibles)
	{

		if (Deductibles.contains("100")){
			Deductibles ="$100 Non-AN / $0 AN Deductible";
			
		}
		else if (Deductibles.contains("200")){
			Deductibles ="$200 Non-AN / $100 AN Deductible";
			
		}
		else if (Deductibles.contains("400")){
			Deductibles ="$400 Non-AN / $200 AN Deductible";
			
		}else
			Deductibles = Deductibles;
		return Deductibles;
		
	}
	
	

	@SuppressWarnings("unused")
	private void RadioButtonYesNoClick() throws InterruptedException
	{
		//Boolean isFlag = false;
		String VIN = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "VIN");
		String VINDecoding = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "VINDecoding");
		if (VINDecoding.contains("Yes"))
		{
			WebElement radiobutton = webDriver.findElement(By.id("pg:frmPage1:VehicleBlock:vinNopbs:vinRadio:0"));
			if (webDriver.findElement(By.id("pg:frmPage1:VehicleBlock:vinNopbs:vinRadio:0")).isDisplayed())
			{
				radiobutton.isSelected();
				if (true){
					webDriver.findElementByXPath("//input[@name='pg:frmPage1:VehicleBlock:vinBLK:vinId:j_id187']").sendKeys(VIN);
				    // Press "TAB" key
					webDriver.findElementByXPath("//input[@name='pg:frmPage1:VehicleBlock:vinBLK:vinId:j_id187']").sendKeys(Keys.TAB);
				

					stepExecutor.clickButton("findElementByXPath", "//input[@id='pg:frmPage1:VehicleBlock:vinBLK:vinId:j_id188']", webDriver, "AN_GLOW");
					webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
					Thread.sleep(15000);
					
				}else {
					radiobutton.click();
					webDriver.findElementByXPath("//input[@name='pg:frmPage1:VehicleBlock:vinBLK:vinId:j_id187']").sendKeys(VIN);
				    // Press "TAB" key
					webDriver.findElementByXPath("//input[@name='pg:frmPage1:VehicleBlock:vinBLK:vinId:j_id187']").sendKeys(Keys.TAB);
				

					stepExecutor.clickButton("findElementByXPath", "//input[@id='pg:frmPage1:VehicleBlock:vinBLK:vinId:j_id188']", webDriver, "AN_GLOW");
					webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
					Thread.sleep(15000);
				}
			}
		}
		if (VINDecoding.contains("No"))
		{
			WebElement radiobutton1 = webDriver.findElement(By.id("pg:frmPage1:VehicleBlock:vinNopbs:vinRadio:1"));
			if (webDriver.findElement(By.id("pg:frmPage1:VehicleBlock:vinNopbs:vinRadio:1")).isDisplayed())
			{

				radiobutton1.isSelected();
			
				// This will check that if the bValue is True means if the first radio button is selected
				if (true)
				{
				// This will select Second radio button, if the first radio button is selected by default
				// Click "No" on VIN Decoding button
				radiobutton1.click();
				Thread.sleep(5000);
				// Enter 17 characters VIN
		
				webDriver.findElementByXPath("//input[@name='pg:frmPage1:VehicleBlock:DecodeBlock2:NonvinSI:j_id246']").sendKeys(VIN);
			    // Press "TAB" key
				webDriver.findElementByXPath("//input[@name='pg:frmPage1:VehicleBlock:DecodeBlock2:NonvinSI:j_id246']").sendKeys(Keys.TAB);
			
				Thread.sleep(5000);
				webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

				// Select Model year
				stepExecutor.selectListValue("findElementByName",
						"pg:frmPage1:VehicleBlock:DecodeBlock2:j_id249:j_id255",DataMap,
						 "Mod Yr", webDriver, "AN_GLOW");
				
				// Select Make of vehicle
				stepExecutor.selectListValueByContainsValue("findElementByXpath",
						".//*[@id='pg:frmPage1:VehicleBlock:DecodeBlock2:j_id259:vMake']",
						"Make", webDriver, "AN_GLOW",rownumber);
				webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				Thread.sleep(8000);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='pg:frmPage1:VehicleBlock:DecodeBlock2:modelsecItem:modelpanel']/select")));
				
				// Select Model of vehicle
				stepExecutor.selectListValueByContainsValue("findElementByXpath",
						".//*[@id='pg:frmPage1:VehicleBlock:DecodeBlock2:modelsecItem:modelpanel']/select",
						 "Vehicle Code", webDriver, "AN_GLOW",rownumber);
				
				}
			} 
		}

	}

	
	
	
	private void EnterLienholderDetails() throws InterruptedException, IOException
	{
		//Enter Lienholder name, address, city, state, country, zip code,phone
		stepExecutor.selectListValueByContainsValue("findElementByXpath", ".//*[@id='pg:frmPage2:pbl:pbls:pbsil:lien']","SelectLienholder", webDriver, "AN_GLOW",rownumber);
		
		stepExecutor.enterTextValue("findElementById",
				"pg:frmPage2:pbl:pbls:namelien:namelien1", DataMap,
				"Lienholder", webDriver, "AN_GLOW");
		stepExecutor.enterTextValue("findElementById",
				"pg:frmPage2:pbl:pbls:addl1:ladd1", DataMap,
				"LienholderAddress", webDriver, "AN_GLOW");
		stepExecutor.enterTextValue("findElementById",
				"pg:frmPage2:pbl:pbls:city:lcity", DataMap,
				"LienholderCity", webDriver, "AN_GLOW");
		
		String LienholderStateAbbr = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "State");
		LienholderStateAbbr = GetProperties(LienholderStateAbbr).trim();
		new Select(webDriver.findElementById("pg:frmPage2:pbl:pbls:statel:lstate")).selectByVisibleText(LienholderStateAbbr);
		/*stepExecutor.selectListValue("findElementById",
				"pg:frmPage2:pbl:pbls:statel:lstate", DataMap,
				"LienholderState", webDriver, "AN_GLOW");*/

		stepExecutor.enterTextValue ("findElementById",
				"pg:frmPage2:pbl:pbls:zipl:lzip", DataMap,
				"LienholderZip", webDriver, "AN_GLOW");
		
		stepExecutor.enterTextValue ("findElementById",
				"pg:frmPage2:pbl:pbls:countryl:lcountry", DataMap,
				"LienholderCountry", webDriver, "AN_GLOW");
		
		stepExecutor.enterTextValue("findElementById",
				"pg:frmPage2:pbl:pbls:tell:lphone", DataMap,
				"Phone", webDriver, "AN_GLOW");
		
		//Click on Save Lienholder button 
		stepExecutor.clickButton("findElementByXPath", ".//input[@id='pg:frmPage2:pbl:pbls:j_id817']", webDriver, "AN_GLOW");
		Thread.sleep(8000);
	}

	private boolean FindDescendantsofCheckbox(String ProductType,String SubProductTypeSelection) throws InterruptedException
	{

		Boolean isFlag = false;
		try
		{
			if (webDriver.findElementByXPath("//tr[td[contains(text(),'"+ProductType+"')]]/td/input[@type='checkbox']").isSelected()){
				reporter.writeStepResult("Product selected", "Select Product",ProductType, "Pass", "", true, webDriver);
			}else {
				webDriver.findElementByXPath("//tr[td[contains(text(),'"+ProductType+"')]]/td/input[@type='checkbox']").click();
				reporter.writeStepResult("Product needs to be selected", "Select Product",ProductType, "pass", "", true, webDriver);
			}
			if (webDriver.findElementByXPath("//tr[td[contains(text(),'"+ProductType+"')]]/td/input[@type='checkbox']").isSelected())
			{
				if (ProductType.contains("AN Vehicle Protection Plan"))
				{
					new Select(webDriver.findElementByXPath(".//*[@id='pg:frmPage1:ProductBlock:rpt:0:selectedVal']")).selectByVisibleText(SubProductTypeSelection);
					webDriver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
					isFlag = true;
				}
				if (ProductType.contains("Service Contract"))
				{
					new Select(webDriver.findElementByXPath(".//*[@id='pg:frmPage1:ProductBlock:rpt:0:selectedVal']")).selectByVisibleText(SubProductTypeSelection);
					webDriver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
					isFlag = true;
				}
				if (SubProductTypeSelection == null){
					System.out.println("No sub-options are present for selected product");
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return isFlag;
		
	}
	
	
	
	public void launchApplication()
	{
		stepExecutor.launchApplication("URL", DataMap, webDriver);
		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		Assert.assertEquals("AutoNation Login", webDriver.getTitle());
		try
		{
			// Code for textboxes starts for entering username
			stepExecutor.enterTextValue("findElementById", "username", DataMap,"username", webDriver, "AN_GLOW");
			
			// Code for textboxes starts for entering password
			stepExecutor.enterTextValue("findElementById", "password", DataMap,"pw", webDriver, "AN_GLOW");
					
			// Code for login button starts
			stepExecutor.clickButton("findElementByXPath", ".//*[@id='Login']", webDriver,"AN_GLOW");
			Thread.sleep(20000);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void RemitQuoteCompletion(String quoteid)
    {
           try 
           {
              // String BackOfficeAdmin = scriptExecutor.readDataFile(strDataFileName, TestCase,rownumber,"BackOfficeAdmin"); 
        	   stepExecutor.clickElement("findElementByXPath", ".//*[@id='01rd0000000JAZT_Tab']/a", webDriver, "AN_GLOW");
                strDataFileName = strDataPath + "MasterSheet.xls";
                String dealername =scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "DealerAccountName");
                dealername = dealername.toUpperCase();
                dealername = dealername.trim();
                //Enter DealerName in search box to complete the remit quote completion step
                System.out.println(dealername);
                webDriver.findElementByXPath(".//input[@id='pg:frm:j_id4:searchDealerName']").sendKeys(dealername);
               // stepExecutor.enterTextValue("findElementByXPath", ".//input[@id='pg:frm:j_id4:searchDealerName']", DataMap, "DealerAccountName", webDriver, "AN_GLOW");
                webDriver.findElementByXPath(".//input[@id='pg:frm:j_id4:searchDealerName']").sendKeys(Keys.ENTER);
                Thread.sleep(5000);
                //stepExecutor.clickButton("findElementByXPath", ".//div[@id='pg:frm:j_id4']/div/input[2]", webDriver, "AN_GLOW");
                //webDriver.findElementByXPath(".//div[@id='pg:frm:j_id4']/div/input[2]").sendKeys(Keys.TAB);
              //  stepExecutor.clickElement("findElementByXPath", ".//a[contains(text(),'"+BackOfficeAdmin+"')]/parent::td/preceding-sibling::td/a", webDriver, "AN_GLOW");
                 stepExecutor.clickElement("findElementByXPath", ".//a[contains(text(),'"+dealername+"')]/parent::td/preceding-sibling::td/a", webDriver, "AN_GLOW");
            //    stepExecutor.clickElement("findElementByXPath", "//td[@id='pg:frm:j_id4:accounts_table:4:j_id8']/a", webDriver, "AN_GLOW");
                System.out.println("Clicked dealer name to remit the quote");
                stepExecutor.clickElement("findElementByXPath", ".//span[contains(text(),'"+quoteid+"')]/parent::td/input[@type='checkbox']", webDriver, "AN_GLOW");
                //click on remit button
                stepExecutor.clickButton("findElementByXPath", ".//*[@id='pg:frm:theBlock:j_id71']", webDriver, "AN_GLOW");
                Thread.sleep(5000);
                webDriver.findElement(By.id("pg:frm:j_id172")).click();
                Thread.sleep(5000);
                  
           } catch (Exception e)
           {
                  e.printStackTrace();
           }
    }

	public String openPDF() {
		
		System.out.println("openPDF executed");
		//Open PDF 
		String pdfurl = null;
		try {
			String currenturl = webDriver.getCurrentUrl();
			if (webDriver.findElement(By.xpath("//td[8]/a/img")).isDisplayed())
			{
					stepExecutor.clickImage("findElementByXPath","//td[8]/a/img",webDriver,"AN_GLOW");
				//webDriver.manage().timeouts().implicitlyWait(60,TimeUnit.SECONDS);
				//Thread.sleep(60000);
				System.out.println(webDriver.getWindowHandles().size());
				System.out.println("webDriver.getWindowHandles() size is: " + webDriver.getWindowHandles().size());
				if (webDriver.getWindowHandles().size() > 1)
				{

				
				   SwitchHandleToNewWindow(webDriver, "https://uat.forms.thewarrantygroup.com/FormsService/pdf/");
					//SwitchHandleToNewWindow(webDriver, "https://uat.forms.thewarrantygroup.com/FormsService/pdf/");
				
					pdfurl = webDriver.getCurrentUrl();
					System.out.println(pdfurl);
				} 
					
			}   
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return pdfurl;
	
	}
	
	
	
	
	public void SwitchHandleToNewWindow(WebDriver driver, String windowTitle)
	{
		
		
		//ArrayList<String> tabs2 = new ArrayList<String> (webDriver.getWindowHandles());
		ArrayList<String> tabs3 = new ArrayList<String> (webDriver.getWindowHandles());
		webDriver.switchTo().window(tabs3.get(1));
		//webDriver.navigate().to(windowTitle);
		webDriver.manage().timeouts().implicitlyWait(600,TimeUnit.SECONDS);
	
	}
	
	public String readPDF(String PDFurl) {
		String output = null;
		try {
			//webDriver.get(PDFurl);
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			URL url = new URL(webDriver.getCurrentUrl());
			BufferedInputStream fileToParse = new BufferedInputStream(url.openStream());
			
			//FileInputStream fileToParse= new FileInputStream(new File(PDFurl));
			
			PDFParser parser = new PDFParser(fileToParse);
			parser.parse();
			System.setProperty("org.apache.pdfbox.baseParser.pushBackSize", "990000");
			output = new PDFTextStripper().getText(parser.getPDDocument());
			webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			writePDFContenttotextfile(output);
			
			webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			parser.getPDDocument().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	
	public String ExtractPDFValue(String FileName,String KeyName) throws IOException
	{
		String KeyValue= null;
		PdfReader reader ;
		if (webDriver.getWindowHandles().size() > 1)
		{
			URL url = new URL(webDriver.getCurrentUrl());
			BufferedInputStream fileToParse = new BufferedInputStream(url.openStream());
			reader = new PdfReader(fileToParse);
		}else
		{
			String pdffilename =readpdf.getFilename(strDataPath);
			FileInputStream fileToParse= new FileInputStream(new File(strDataPath+pdffilename));
			reader = new PdfReader(fileToParse);
		}
	    int n = reader.getNumberOfPages();
	      
	    String str=PdfTextExtractor.getTextFromPage(reader, 1); //Extracting the content from a particular page.
	    CreateOutputfile();
        writePDFContenttotextfile(str);
        System.out.println(str);
        reader.close();
        BufferedReader in = new BufferedReader(new FileReader(FileName));
        String output = in.readLine();
	    while (!output.contains(KeyName))
	      {
		   output = in.readLine();
 
	      }
	  //  output = in.readLine();
	  //  output = in.readLine();
	    KeyValue = output;
	  
	    KeyValue = KeyValue.substring(KeyValue.lastIndexOf(" ") + 1 , KeyValue.length());
	    KeyValue = KeyValue.trim();
	    if (KeyValue.contains("Date"))
	    {
	    		    output = in.readLine();
	    		    output = in.readLine();
	    		    KeyValue = output;	
	    }
	    if ( KeyValue.contains("Mileage")){
	    	output = in.readLine();
		    KeyValue = output;	
	    }
	    System.out.println(KeyValue);
		return KeyValue;
		
		
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
	
	
	
	
	
	
	public Boolean checkPDFContent(String output ,String checkValue) throws IOException
	{
		Boolean result = false;
		int resultcount=0;
	//	String filepath =strDataPath+"outputPDF.txt";
	//	BufferedReader in = new BufferedReader(new FileReader(filepath));
	//    String output = in.readLine();
	    
	    /*while (!output.contains(checkValue))
	      {
		   output = in.readLine();
		//   resultcount++;
    	  // return result;
	      }*/
	    //  while (true)
	    //  {
	    	 // output = in.readLine();
	    	//  System.out.println(checkValue + output);
			  if(output.contains(checkValue))
			   {
					System.out.println(output + checkValue);
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
		//   }
		  resultcount++;
		   return result;

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
			File dataFolder = new File(strAbsolutepath + "/data");
			fileName = strDataPath + "MasterSheet.xls";
			File f = new File(fileName);
			FileInputStream fsIP= new FileInputStream(f);
			 HSSFWorkbook wb = new HSSFWorkbook(fsIP);
	         
	         HSSFSheet dataSheet = wb.getSheet(TestCase);
	         HSSFRow dataRow = dataSheet.getRow(0);
	         Cell cell = null; 
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
				//  fsIP.close(); 
	         FileOutputStream output_file =new FileOutputStream(new File(fileName)); 
	         wb.write(output_file); 
	           
	         output_file.close();  
		} catch( Exception e){
			e.printStackTrace();
		}
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
		 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
		return output;
		
	}

	public String GetTestStartTime()
	{
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.println( sdf.format(cal.getTime()) );
    	return (sdf.format(cal.getTime()));
    	
	}

}

			
	

