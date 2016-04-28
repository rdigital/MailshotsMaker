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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.awt.AWTException; 
import java.awt.Robot;
import java.awt.event.KeyEvent;

import junit.framework.Assert;
import jxl.read.biff.BiffException;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
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
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import org.openqa.selenium.remote.DesiredCapabilities;

public class DealerTrack {
	
	public String TestCase="DealerTrack";
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
	int rownumber=0;
	
	Map<String, String> DataMap = new HashMap();
	Boolean sExecutionStatus;
	ReadExcel readExcel = new ReadExcel(reporter);
	private Verification verifiaction;
	private static String strAbsolutepath = new File("").getAbsolutePath();
	private static String strDataPath = strAbsolutepath + "/data/";
	String 	strDataFileName = strDataPath + "MasterSheet.xls";
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
			String startesttime = GetTestStartTime();
			readExcel.readByIndex(i);
			DataMap=readExcel.loadDataMap(i);
			rownumber =i;
			strDataFileName = strDataPath + "MasterSheet.xls";
			scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"StartTime",startesttime);
			System.out.println(capabilities.getBrowserName());
			reporter.setStrBrowser(capabilities.getBrowserName());
			reporter.addIterator(i);
			testcaseMain();
			//NextFunctionCall
			//WriteMaster.updateNextURL(TestCase,webDriver.getCurrentUrl());
			reporter.closeIterator();
			System.out.println("\t \t \t \t \t Row number: " + i);
			webDriver.quit();
			strStopTime = reporter.stop();
			scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"EndTime",strStopTime);
			}
		}
	//	String strStopTime = reporter.stop();
		reporter.strStopTime = strStopTime;
		float timeElapsed = reporter.getElapsedTime();
		reporter.timeElapsed = timeElapsed;
		reporter.CreateSummary("Cafe#"+browserName);
	//	System.exit(1);
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
			//rownumber = i;
			//strDataFileName = strDataPath + "MasterSheet.xls";
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
		
		
		/*Wait wait = new FluentWait(webDriver)   
	    .withTimeout(30, TimeUnit.SECONDS)    
	    .pollingEvery(5, TimeUnit.SECONDS)   
	    .ignoring(NoSuchElementException.class);*/

		ReadPDF objReadPDf= new ReadPDF(reporter);
		stepExecutor.launchApplication("URL", DataMap, webDriver);
		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		Assert.assertEquals("Dealertrack Technologies Login", webDriver.getTitle());
		//System.out.println(System.getProperty("user.home"));
		System.getProperty("user.home\\Downloads");
		//System.out.println(System.getProperty("user.dir\\data"));
		//System.getProperty("user.dir\\data");
		boolean exists = false;
		try {
			System.setProperty("Test_Scenario_Name", TestCase);
			// Code for textboxes starts
		
			stepExecutor.enterTextValue("findElementByName", "username", DataMap,  "username", webDriver,"DealerTrack");
			Thread.sleep(5000);
			
			// Code for textboxes starts
			System.out.println(webDriver.getTitle());
			stepExecutor.enterTextValue("findElementByName", "password", DataMap,  "password", webDriver,"DealerTrack");
			Thread.sleep(5000);

			stepExecutor.clickButton("findElementByName", "login", webDriver,"DealerTrack");
			Thread.sleep(60000);
	
			//click switch button
			webDriver.switchTo().frame(webDriver.findElementById("iFrm"));
			webDriver.switchTo().frame(webDriver.findElementByName(("nav")));

			//stepExecutor.clickElement("findElementByXPath", "//a[contains(text(),'Switch')]", webDriver, "DealerTrack");
			
			WebElement element=webDriver.findElementByXPath("//a[contains(text(),'Switch')]");
			System.out.println("this is" + element.getText());
			JavascriptExecutor executor = (JavascriptExecutor)webDriver;
			executor.executeScript("arguments[0].click();", element);
			Thread.sleep(10000);
			
			//Enter DealerID
			webDriver.switchTo().defaultContent();
			webDriver.switchTo().frame(webDriver.findElementById(("iFrm")));
			
			webDriver.switchTo().frame(webDriver.findElementByName(("main")));
			
			wait.until(ExpectedConditions.elementToBeClickable(By.name("TxtSwitchDealer")));
			stepExecutor.enterTextValue("findElementByName", "TxtSwitchDealer",DataMap, "Client", webDriver, "DealerTrack");
			Thread.sleep(10000);
			
			//stepExecutor.clickButton("findElementByXPath", ".//input[@id='BtnSwitch']", webDriver, "DealerTrack");
			WebElement button=webDriver.findElementByXPath(".//input[@id='BtnSwitch']");
			executor.executeScript("arguments[0].click();", button);
			Thread.sleep(10000);
		
			//verify dealer name

			String DealerName = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "DealerAccountName");
			System.out.println(DealerName);
			
			webDriver.switchTo().defaultContent();
			webDriver.switchTo().frame(webDriver.findElementById(("iFrm")));
			webDriver.switchTo().frame(webDriver.findElementByName(("main")));
			//webDriver.switchTo.selectFrame("main");
			
			if(verify.verifyElementisPresent(webDriver,"//a[contains(text(),'"+DealerName+"')]", "xpath")){
				scriptExecutor.WriteExcelDataFile(strDataFileName,  TestCase,rownumber, "DealerNameverified","PASS");
			}
			else{
				scriptExecutor.WriteExcelDataFile(strDataFileName,  TestCase,rownumber, "DealerNameverified","FAIL");
			}
			Thread.sleep(3000);
			
			//Click on Emenu Tab
			
			webDriver.switchTo().defaultContent();
			webDriver.switchTo().frame(webDriver.findElementById(("iFrm")));
			webDriver.switchTo().frame(webDriver.findElementById(("nav")));
			//stepExecutor.clickElement("findElementByXPath", "//a[contains(text(),'eMenu')]", webDriver, "DealerTrack");
			
			WebElement element1=webDriver.findElementByXPath("//a[contains(text(),'eMenu')]");
			executor.executeScript("arguments[0].click();", element1);
			Thread.sleep(1000);
			
			//Click on Deals Link
			
			//stepExecutor.clickElement("findElementByXPath", "//a[contains(text(),'Deals')]", webDriver, "DealerTrack");
			
			WebElement element2=webDriver.findElementByXPath("//a[contains(text(),'Deals')]");
			executor.executeScript("arguments[0].click();", element2);
			Thread.sleep(10000);
			
			//Select Deal Template
			
			webDriver.switchTo().defaultContent();
			webDriver.switchTo().frame(webDriver.findElementById(("iFrm")));
			webDriver.switchTo().frame(webDriver.findElementByName(("main")));
			stepExecutor.selectListValue("findElementByXpath", ".//select[@id='ddlNewDealTemplate']", DataMap, "DealTemplate", webDriver, "DealerTrack");
			Thread.sleep(10000);
			
			//Click on create deal button
			
			//stepExecutor.clickButton("findElementByXPath", ".//*[@id='btnCreateDeal']", webDriver, "DealerTrack");
			
			WebElement button1=webDriver.findElementByXPath(".//*[@id='btnCreateDeal']");
			executor.executeScript("arguments[0].click();", button1);
			Thread.sleep(10000);
			
			//Enter First Name, Last Name, Vehicle Type = New, Enter Mileage, enter valid VIN# 
			
			stepExecutor.enterTextValue("findElementByName", "buyerFirstName",  DataMap,  "FirstName",  webDriver, "DealerTrack");
			Thread.sleep(5000);
			
			stepExecutor.enterTextValue("findElementByName",  "buyerLastName", DataMap, "LastName", webDriver, "DealerTrack");
			Thread.sleep(5000);
			
			stepExecutor.selectListValue("findElementByXpath", ".//select[@id='vehicleType']", DataMap, "VehicleType", webDriver, "DealerTrack");
			Thread.sleep(5000);
			
			stepExecutor.enterTextValue("findElementByName",  "mileage", DataMap, "Mileage", webDriver, "DealerTrack");
			Thread.sleep(5000);
			
			stepExecutor.enterTextValue("findElementByName",  "vin", DataMap, "VIN", webDriver, "DealerTrack");
			Thread.sleep(10000);
			
			webDriver.findElementByName("vin").sendKeys(Keys.TAB);
			
			
			
			//click continue button
			
			//stepExecutor.clickButton("findElementByXPath", ".//*[@id='continueButton']", webDriver, "DealerTrack");
			
			WebElement button2=webDriver.findElementByXPath(".//*[@id='continueButton']");
			executor.executeScript("arguments[0].click();", button2);
			Thread.sleep(10000);
	
			//Enter Selling Price,MSRP 
			
			webDriver.findElementByName("tbSellingPrice").clear();
			stepExecutor.enterTextValue("findElementByName",  "tbSellingPrice", DataMap, "SellingPrice", webDriver, "DealerTrack");
			Thread.sleep(5000);
			
			webDriver.findElementByName("tbMsrp").clear();
			stepExecutor.enterTextValue("findElementByName",  "tbMsrp", DataMap, "MSRP", webDriver, "DealerTrack");
			Thread.sleep(5000);
			
			// click edit button
			
			//stepExecutor.clickButton("findElementByXPath", ".//*[@id='dtlProducts_btnEditMenu']", webDriver, "DealerTrack");
			
			WebElement button3=webDriver.findElementByXPath(".//*[@id='dtlProducts_btnEditMenu']");
			executor.executeScript("arguments[0].click();", button3);
			Thread.sleep(5000);
			
			//select power train,
			
			stepExecutor.selectListValue("findElementByName", "Collateral_manu_warrantyDropDown", DataMap, "Powertrain", webDriver, "DealerTrack");
			Thread.sleep(5000);
			
			//Enter IN Service Date
			
			stepExecutor.enterTextValue("findElementByName",  "Collateral_in_service_dateTextBox", DataMap, "InServiceDate", webDriver, "DealerTrack");
			Thread.sleep(5000);
			
			//click radio button
			
			String FactoryCertified=scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "FactoryCertified");

			if(FactoryCertified.equalsIgnoreCase("yes")){
				
			//stepExecutor.clickButton("findElementByXPath", ".//*[@id='Collateral_wrap_ratesRadioButtonList_0']", webDriver, "DealerTrack");
			WebElement button9=webDriver.findElementByXPath(".//*[@id='Collateral_wrap_ratesRadioButtonList_0']");
			executor.executeScript("arguments[0].click();", button9);
			
			Thread.sleep(5000);
			}
			
			else{
				
				stepExecutor.clickButton("findElementByXPath", ".//*[@id='Collateral_wrap_ratesRadioButtonList_1']", webDriver, "DealerTrack");
				Thread.sleep(5000);
			}
			
			String FactoryCertifiedmanufacture=scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "FactoryCertifiedmanufacture");
			
			if(FactoryCertifiedmanufacture.equalsIgnoreCase("No")){

			//click radio button
			
			//stepExecutor.clickButton("findElementByXPath", ".//*[@id='Collateral_extended_eligibilityRadioButtonList_1']", webDriver, "DealerTrack");
			WebElement button10=webDriver.findElementByXPath(".//*[@id='Collateral_extended_eligibilityRadioButtonList_1']");
			executor.executeScript("arguments[0].click();", button10);
			
			Thread.sleep(5000);
			}
			else{
				stepExecutor.clickButton("findElementByXPath", ".//*[@id='Collateral_extended_eligibilityRadioButtonList_0']", webDriver, "DealerTrack");
				Thread.sleep(5000);
			}
			
			//select second owner,Financing source,VSC contract type
			
			stepExecutor.selectListValue("findElementByName", "Collateral_FirstOwnerDropDown", DataMap, "Second owner", webDriver, "DealerTrack");
			Thread.sleep(5000);
			
			stepExecutor.selectListValue("findElementByName", "Financing_lender_nameDropDown", DataMap, "Financing service", webDriver, "DealerTrack");
			Thread.sleep(5000);

			/*if(verify.verifyElementIsPresent(webDriver, ".//*[@id='Product_ppm_contract_typeDropDown']", "Xpath")){
				
				stepExecutor.selectListValue("findElementByName", "Product_ppm_contract_typeDropDown", DataMap, "PPM Contract type", webDriver, "DealerTrack");
				Thread.sleep(5000);
			}*/
				
			if(verify.verifyElementIsPresent(webDriver, ".//*[@id='Product_vsc_contract_typeDropDown']", "Xpath")){
			stepExecutor.selectListValue("findElementByName", "Product_vsc_contract_typeDropDown", DataMap, "VSC Contract type", webDriver, "DealerTrack");
			Thread.sleep(5000);
			}
			
			//click continue button
			
			//stepExecutor.clickButton("findElementByXPath", ".//*[@id='btnContinue']", webDriver, "DealerTrack");
			WebElement button11=webDriver.findElementByXPath(".//*[@id='btnContinue']");
			executor.executeScript("arguments[0].click();", button11);
			
			Thread.sleep(90000);
			
			
			String price2 = webDriver.findElementByXPath(".//*[@id='tdPackage1']//*[contains(@id,'_txtRate1_txtTextBox')]").getAttribute("value");
			System.out.println(price2);
			

			if(price2.equalsIgnoreCase("0.00")){
				webDriver.quit();
			}
			
			
			//selecting checkboxes
			
			
			List<WebElement> e= webDriver.findElements(By.xpath(".//*[contains(@id,'_cbSelect')]"));
			
			for(WebElement i : e){
				
				try{
				      //System.out.println("This is "+i.getAttribute("id")); 
					//JavascriptExecutor executor = (JavascriptExecutor)webDriver;
					executor.executeScript("arguments[0].click();", i);
					 
					 //i.click();
				}
				
				catch(WebDriverException e1){
					 ((JavascriptExecutor) webDriver).executeScript(
	                         "arguments[0].scrollIntoView(true);", i);
					 //JavascriptExecutor executor = (JavascriptExecutor)webDriver;
					 executor.executeScript("arguments[0].click();", i);
				       
					 //i.click();
					
					((JavascriptExecutor) webDriver).executeScript(
                     "scroll(0,-250);");
					
				}
         
				
			}
			
			List<WebElement> c=webDriver.findElements(By.xpath(".//*[contains(text(),'TWG Vehicle Service Contract')]/parent::th/following-sibling::th/input"));
			
			
			for(WebElement i : c){
				
				try{
				       
					executor.executeScript("arguments[0].click();", i);
				}
				catch(Exception e1){
					
				}
				
			}
			
			
			//click delete products button
			
			//stepExecutor.clickButton("findElementByXPath", ".//*[@id='btnRemove']", webDriver, "DealerTrack");
			
			WebElement button14=webDriver.findElementByXPath(".//*[@id='btnRemove']");
			executor.executeScript("arguments[0].click();", button14);
			Thread.sleep(30000);
			
			
			//click radio button

			WebElement button4=webDriver.findElementByXPath(".//*[@id='rbTerm1Payment1']");
			executor.executeScript("arguments[0].click();", button4);
			Thread.sleep(10000);
	
			//select platinum
			String sAppname = null;
			String Program= scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "Program");
			new Select(webDriver.findElementByXPath(".//*[@id='tdPackage1']//*[contains(@id,'_ddlProgram')]")).selectByVisibleText(Program);
			Thread.sleep(10000);
			
			//select term/mile,deductabile
			
			String Term= scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "Term/mile");
			new Select(webDriver.findElementByXPath(".//*[@id='tdPackage1']//*[contains(@name,'ddlTerm/Mile')]")).selectByVisibleText(Term);
			Thread.sleep(10000);
			
			String deductible= scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "Deductibles");
			new Select(webDriver.findElementByXPath(".//*[@id='tdPackage1']//*[contains(@id,'ddlDeductible')]")).selectByVisibleText(deductible);
			Thread.sleep(10000);

			//capturing price from application 
			
			String price = webDriver.findElement(By.xpath(".//*[@id='tdPackage1']//*[contains(@id,'_txtRate1_txtTextBox')]")).getAttribute("value");
			System.out.println("price is" + price);
			if (price.contains(".00"))
			{
				price = price.replace(".00", "");
			}
			price="$" + price;
			scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "Price",price);
	
			//click save button 
			
			//stepExecutor.clickButton("findElementByXPath", ".//*[@id='btnSave']", webDriver, "DealerTrack");
			
			WebElement button5=webDriver.findElementByXPath(".//*[@id='btnSave']");
			executor.executeScript("arguments[0].click();", button5);
			Thread.sleep(10000);
		
			//verify check box is enabled
			
			webDriver.switchTo().defaultContent();
			webDriver.switchTo().frame(webDriver.findElementById(("iFrm")));
			webDriver.switchTo().frame(webDriver.findElementByName(("main")));
			
			//verify.verifyCheckboxStatus(webDriver,".//input[@id='dtlProducts_dgProducts_ctl01_cbProdSelected']", "xpath", "checked");
			
			 boolean checkboxenabled=webDriver.findElementById("dtlProducts_dgProducts_ctl01_cbProdSelected").isEnabled();
			if (checkboxenabled) {
				
				reporter.writeStepResult(System.getProperty("Test_Scenario_Name"),
						"Verify element is present on the page",
						"checkbox is enabled", "Pass",
						"Element is present on the page", true, webDriver);
				scriptExecutor.WriteExcelDataFile(strDataFileName,  TestCase,rownumber, "checkboxenabled","PASS");
				
			} else {
				reporter.writeStepResult(System.getProperty("Test_Scenario_Name"),
						"Verify element is present on the page",
						"checkbox not enabled", "Fail",
						"Element is not present on the page", true, webDriver);
				scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase,rownumber, "checkboxenabled","FAIL");
	
			}
			
			String mainwindow=webDriver.getWindowHandle();
			System.out.println(mainwindow);
			//webDriver.switchTo().window(mainwindow);
			
			//click digital contracting button
				
			//stepExecutor.clickButton("findElementByXPath", ".//*[@id='pushToAmpButton']", webDriver, "DealerTrack");
			
			WebElement button6=webDriver.findElementByXPath(".//*[@id='pushToAmpButton']");
			executor.executeScript("arguments[0].click();", button6);
			Thread.sleep(40000);
			
		
			//Enter street name,street number,Zip,home phone
			
			stepExecutor.enterTextValue("findElementByName",  "cd_street_num", DataMap, "StreetNumber", webDriver, "DealerTrack");
			Thread.sleep(5000);
			
			stepExecutor.enterTextValue("findElementByName",  "cd_street_name", DataMap, "StreetName", webDriver, "DealerTrack");
			Thread.sleep(5000);
			
			stepExecutor.enterTextValue("findElementByName",  "cd_zip", DataMap, "Zip", webDriver, "DealerTrack");
			Thread.sleep(5000);
			
			stepExecutor.enterTextValue("findElementByName",  "buyer_home_phone_dummy", DataMap, "HomePhone", webDriver, "DealerTrack");
			Thread.sleep(5000);
			
			//select check box no vehicle lienholder
			
			String firstowner =scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "LienHolderinfo");
			System.out.println(firstowner);
			if(firstowner.equalsIgnoreCase("yes")){
				
				stepExecutor.selectListValue("findElementByName","lienholder_info",DataMap,"lienholder",webDriver,"DealerTrack");
				Thread.sleep(10000);
				
			}
			
			else{
								
				WebElement element3=webDriver.findElementByXPath(".//*[@id='no_veh_lien_cb']");
				executor.executeScript("arguments[0].click();", element3);
				Thread.sleep(10000);
				
			}
		
			//click next button and switch and print button
			
			//stepExecutor.clickButton("findElementByXPath", ".//*[@id='Next']", webDriver, "DealerTrack");
			
			WebElement button12=webDriver.findElementByXPath(".//*[@id='Next']");
			executor.executeScript("arguments[0].click();", button12);
			Thread.sleep(10000);
			
			webDriver.switchTo().defaultContent();
			webDriver.switchTo().frame(webDriver.findElementById(("iFrm")));
			webDriver.switchTo().frame(webDriver.findElementByName(("main")));
			
			//stepExecutor.clickButton("findElementByXPath", ".//*[@id='btnSubmitPrint']", webDriver, "DealerTrack");
			
			WebElement button7=webDriver.findElementByXPath(".//*[@id='btnSubmitPrint']");
			//JavascriptExecutor executor = (JavascriptExecutor)webDriver;
			executor.executeScript("arguments[0].click();", button7);
			Thread.sleep(10000);
			
			
			//File file = new File("C:\\Selenium\\Workspace\\Cafe3.1.2\\data\\OpenPDF.pdf");

	        /* if(file.delete()){
	             System.out.println(file.getName() + " Was deleted!");
	         }else{
	             System.out.println("Delete Operation Failed. Check: " + file);
	         }*/
			
            //click ok button 
			ArrayList<String> tabs1 = new ArrayList<String> (webDriver.getWindowHandles());
			webDriver.switchTo().window(tabs1.get(1));
			webDriver.manage().timeouts().implicitlyWait(60,TimeUnit.SECONDS);
			
			//stepExecutor.clickButton("findElementByXPath", ".//*[@id='btnSave']", webDriver, "DealerTrack");
			
			WebElement button8=webDriver.findElementByXPath(".//*[@id='btnSave']");
			executor.executeScript("arguments[0].click();", button8);
			
			Thread.sleep(80000);
		
			
			//pdf section
		
	
		if(readExcel.browserName(TestCase).equalsIgnoreCase("IE")){
			    Robot r = new Robot();
			    r.keyPress(KeyEvent.VK_TAB);
			    r.keyRelease(KeyEvent.VK_TAB);
			    r.keyPress(KeyEvent.VK_TAB);
			    r.keyPress(KeyEvent.VK_ENTER);
			    Thread.sleep(20000);

			    r.keyPress(KeyEvent.VK_ESCAPE);
				r.keyRelease(KeyEvent.VK_ESCAPE);
			    r.keyPress(KeyEvent.VK_TAB);
				r.keyRelease(KeyEvent.VK_TAB);
				r.keyPress(KeyEvent.VK_TAB);
				r.keyRelease(KeyEvent.VK_TAB);
				r.keyPress(KeyEvent.VK_TAB);
				r.keyRelease(KeyEvent.VK_TAB);
				r.keyPress(KeyEvent.VK_TAB);
				r.keyPress(KeyEvent.VK_ENTER);
				Thread.sleep(20000);

		}
		System.out.println(readExcel.browserName(TestCase));

			try{
				
				/*String pdfpath = objReadPDf.openPDFfromfilelocation("C:\\Selenium\\Workspace\\Cafe3.1.2\\data");
				FileInputStream fileToParse1= new FileInputStream(new File("C:\\Selenium\\Workspace\\Cafe3.1.2\\data\\OpenPDF.pdf"));*/
				
				System.out.println(objReadPDf.getFilename(strDataPath));
				FileInputStream fileToParse1= new FileInputStream(new File(strDataPath+ objReadPDf.getFilename(strDataPath)));
				PDFParser parser1 = new PDFParser(fileToParse1);
				parser1.parse();
				System.setProperty("org.apache.pdfbox.baseParser.pushBackSize", "990000");
				String output = new PDFTextStripper().getText(parser1.getPDDocument());
				System.out.println(output);
				parser1.getPDDocument().close();
				
				String ContractNumber = ExtractPDFValuefromfile(objReadPDf,strDataPath+"outputPDF.txt","CONTRACT NO","DY",webDriver);
				scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"PDFContractNumber",ContractNumber);
		
				// verification points for PDF
				 
				String vinVerified =scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "VIN");
				
				if (objReadPDf.checkPDFContent(output,vinVerified )){
					scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"PDFVIN","PASS");
				}else {
					scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"PDFVIN","FAIL");
				}
				
				String contractno =scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "PDFContractNumber");
				
				if (objReadPDf.checkPDFContent(output,contractno )){
					scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"ContractNoResults","PASS");
				}else {
					scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"ContractNoResults","FAIL");
				}
				
					
				String terms1 = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "terms");
				if (terms1 .contains(".0")){
					terms1  = terms1 .replace(".0","");
				}
				if (objReadPDf.checkPDFContent(output,terms1)){
					scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"TmonthsResults","PASS");
				}else {
					scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"TmonthsResults","FAIL");
				}
				
				String miles1= scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "miles");
				
				if (objReadPDf.checkPDFContent(output,miles1)){
					scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"MilesResults","PASS");
				}else {
					scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"MilesResults","FAIL");
				}
				
				String deductible1=scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Deductible");
				
				if(objReadPDf.checkPDFContent(output, deductible1)){
					
					scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "DeductiblesResults", "PASS");
					
				}
				
				else{
					scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "DeductiblesResults", "FAIL");
				}
				
				String Mileage=scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Mileage");
				
				if(objReadPDf.checkPDFContent(output, Mileage)){
					
					scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "OdometerResults", "PASS");
					
				}
				
				else{
					scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "OdometerResults", "FAIL");
				}
			

				
				String priceresult =scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "price");
				
				System.out.println(priceresult);
				
				if(objReadPDf.checkPDFContent(output, priceresult)){
					
					scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "priceResults", "PASS");
					
				}
				
				else{
					scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "priceResults", "FAIL");
				}
				
				
			
			
				 scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "PDFResults", "PASS");
	}
			
			catch(Exception e2){
				
				scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "PDFResults", "FAIL");
			}
			finally
			{
				//delete the PDF file
				File f1 = new File(strDataPath+objReadPDf.getFilename(strDataPath));
				if(f1.exists())
	      		{
	      			f1.delete();
	      		}
	
			}
			
			Thread.sleep(20000);
			//click after market, Remittance and pending remittance list
			
			/*ArrayList<String> tabs4 = new ArrayList<String> (webDriver.getWindowHandles());
			System.out.println("the size is" + tabs4.size());
			webDriver.switchTo().window(tabs4.get(0));*/
			webDriver.switchTo().window(mainwindow);
			
			webDriver.switchTo().defaultContent();
			webDriver.switchTo().frame(webDriver.findElementById(("iFrm")));
			webDriver.switchTo().frame(webDriver.findElementById(("nav")));
			//stepExecutor.clickElement("findElementByXPath", "//a[contains(text(),'Aftermarket')]", webDriver, "DealerTrack");
			
			WebElement element12=webDriver.findElementByXPath("//a[contains(text(),'Aftermarket')]");
			executor.executeScript("arguments[0].click();", element12);
			Thread.sleep(20000);
			
			//stepExecutor.clickElement("findElementByXPath", "//a[contains(text(),'Remittance')]", webDriver, "DealerTrack");
			
			WebElement element13=webDriver.findElementByXPath("//a[contains(text(),'Remittance')]");
			executor.executeScript("arguments[0].click();", element13);
			Thread.sleep(1000);
	
			//stepExecutor.clickElement("findElementByXPath", "//a[contains(text(),'Pending Remittance List')]", webDriver, "DealerTrack");
			
			WebElement element14=webDriver.findElementByXPath("//a[contains(text(),'Pending Remittance List')]");
			executor.executeScript("arguments[0].click();", element14);
			Thread.sleep(20000);
		
					
			// select service contract from dropdown
			webDriver.switchTo().defaultContent();
			webDriver.switchTo().frame(webDriver.findElementById(("iFrm")));
			webDriver.switchTo().frame(webDriver.findElementByName(("main")));
			
			stepExecutor.selectListValue("findElementByName", "lstProductCategories", DataMap, "Product", webDriver, "DealerTrack");
			
			String product= scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "Product");
			new Select(webDriver.findElementByXPath(".//*[@name='lstProductCategories")).selectByVisibleText(product);
			
			Thread.sleep(20000);
			
			//select checkbox
			
			//stepExecutor.clickElement("findElementByXPath", ".//*[@id='dgrdDocumentData__ctl2_chkSelect']", webDriver, "DealerTrack");
			
			WebElement element15=webDriver.findElementByXPath(".//*[@id='dgrdDocumentData__ctl2_chkSelect']");
			executor.executeScript("arguments[0].click();", element15);
			Thread.sleep(20000);
			
			//click create remittance sheet 
			
			//stepExecutor.clickButton("findElementByXPath", ".//*[@id='btnCreateRemmitanceSheet']", webDriver, "DealerTrack");
			
			WebElement button15=webDriver.findElementByXPath(".//*[@id='btnCreateRemmitanceSheet']");
			executor.executeScript("arguments[0].click();", button15);
			Thread.sleep(20000);
		}
		
		catch (Exception e4) {
			e4.printStackTrace();
		}
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


public String readPDF(String PDFurl) {
	String output = null;
	try {
	    webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		URL url = new URL(webDriver.getCurrentUrl());
		BufferedInputStream fileToParse = new BufferedInputStream(url.openStream());
		
		//FileInputStream fileToParse= new FileInputStream(new File(PDFurl));
		
		PDFParser parser = new PDFParser(fileToParse);
		parser.parse();
		System.setProperty("org.apache.pdfbox.baseParser.pushBackSize", "990000");
		output = new PDFTextStripper().getText(parser.getPDDocument());
		webDriver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		writePDFContenttotextfile(output);
		
		webDriver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		parser.getPDDocument().close();
		//parser.getDocument().close();
	} catch (Exception e) {
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




public String getFilename (String PDFFilepath){
	File folder = new File(PDFFilepath);
	File[] listOfFiles = folder.listFiles();
	String filename = null;
	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        //System.out.println(listOfFiles[i].getName());
	        if (listOfFiles[i].getName().endsWith(".pdf")){
	        	 System.out.println(listOfFiles[i].getName());
	        	 filename= listOfFiles[i].getName();
	        }
	      } else if (listOfFiles[i].isDirectory()) {
	        System.out.println("Directory " + listOfFiles[i].getName());
	      }
	    }
		return filename;
}

/*public String openPDF() {
	
	//Open PDF 
	String pdfurl = null;
	try {
		String currenturl = webDriver.getCurrentUrl();
		if (webDriver.findElement(By.xpath("//td[8]/a/img")).isDisplayed())
		{
			stepExecutor.clickImage("findElementByXPath","//td[8]/a/img",webDriver,"AN_GLOW");
			webDriver.manage().timeouts().implicitlyWait(60,TimeUnit.SECONDS);
			Thread.sleep(60000);
			System.out.println(webDriver.getWindowHandles().size());
			if (webDriver.getWindowHandles().size() > 1)
			{
				SwitchHandleToNewWindow(webDriver, "https://www.qa.dealertrack.com/AMNSOA/Contracting/OpenPDF");
				pdfurl = webDriver.getCurrentUrl();
				System.out.println(pdfurl);
			} 
					
		}   
	} catch (Exception e) {
		e.printStackTrace();
	}

	return pdfurl;

}*/

public void SwitchHandleToNewWindow(WebDriver driver, String windowTitle)
{
	ArrayList<String> tabs2 = new ArrayList<String> (webDriver.getWindowHandles());
	webDriver.switchTo().window(tabs2.get(1));
	//webDriver.navigate().to(windowTitle);
	webDriver.manage().timeouts().implicitlyWait(600,TimeUnit.SECONDS);
}

public String ExtractPDFValuefromfile(ReadPDF objReadPDf,String FileName,String KeyName,String pattern,WebDriver driver) throws IOException{
	String KeyValue= null;
	PdfReader reader;
	
	
	if (webDriver.getWindowHandles().size() > 2)
	{
		URL url = new URL(webDriver.getCurrentUrl());
		BufferedInputStream fileToParse = new BufferedInputStream(url.openStream());
		reader = new PdfReader(fileToParse);
	}else
	{
		/*String pdfpath = objReadPDf.openPDFfromfilelocation("C:\\Selenium\\Workspace\\Cafe3.1.2\\data");
		
		FileInputStream fileToParse1= new FileInputStream(new File("C:\\Selenium\\Workspace\\Cafe3.1.2\\data\\OpenPDF.pdf"));*/
		
		System.out.println(objReadPDf.getFilename(strDataPath));
		FileInputStream fileToParse1= new FileInputStream(new File(strDataPath+ objReadPDf.getFilename(strDataPath)));
			reader = new PdfReader(fileToParse1);
	
		
	}
    int n = reader.getNumberOfPages();
      
     

    String str = PdfTextExtractor.getTextFromPage(reader, 1);
	 //Extracting the content from a particular page.
    CreateOutputfile();
    writePDFContenttotextfile(str);
    System.out.println(str);
    reader.close();
    BufferedReader in = new BufferedReader(new FileReader(FileName));
  	
  		String output1 = in.readLine();
	
    while (((!output1.contains("CONTRACT NO. DY")) & (!output1.startsWith("DY")))){
    	output1 = in.readLine();
    
    		
      }
  //  output = in.readLine();
  //  output = in.readLine();
    KeyValue = output1;
  
    KeyValue = KeyValue.substring(KeyValue.lastIndexOf(" ") + 1 , KeyValue.length());
    KeyValue = KeyValue.trim();
    System.out.println(KeyValue);
	return KeyValue;


	//return KeyName;
	
	
}
}



	
	
	

