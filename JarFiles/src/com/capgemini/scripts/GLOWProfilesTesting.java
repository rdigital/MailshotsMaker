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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
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

public class GLOWProfilesTesting{

	public String TestCase = "GLOWProfilesTesting";
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
	public static String strDataFileName = strDataPath + "MasterSheet.xls";
	private boolean acceptNextAlert = true;
	int rownumber = 0;
	String strStopTime;
	Boolean result = false;
	boolean finalresult = false;
	boolean result1 = false,result2 = false,result3 = false,result4 = false,result5 = false,result6 = false,result7 = false,result8 = false;
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
			scriptExecutor.WriteExcelDataFile(strDataFileName,TestCase,rownumber,"StartTime",StrExecutionStartTime);
			DataMap=readExcel.loadDataMap(rownumber);
			System.out.println(capabilities.getBrowserName());
			reporter.setStrBrowser(capabilities.getBrowserName());
			reporter.addIterator(i);
			readpdf.CreateOutputfile();
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
		 System.exit(1);
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
		System.exit(1);
	}

	public void testcaseMain() throws InterruptedException, BiffException,
			Exception {
		stepExecutor.launchApplication("URL", DataMap, webDriver);
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {
			
		
			WebDriverWait wait = new WebDriverWait(webDriver, 30);
			Login();
			Thread.sleep(5000);
			Assert.assertEquals("The Warranty Group - Dealer Site", webDriver.getTitle());
			String QuoteId = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "QuoteID");
			String ContractNumber = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "ContractNumber");
			if (VisibilityofWebElements("//li[@id='01rd0000000JATw_Tab']/a","Create Quote")){
				//Click on Quotes link
				stepExecutor.clickLink("findElementByXPath","//a[contains(text(),'Quotes')]", webDriver, "CreateQuote");
				Thread.sleep(2000);
				if (VisibilityofWebElements("//input[@name='new']","Create Quote")){
					result1 = CreateNewQuote();
					finalresult = finalresult || result1;
				}
			}
			if (VisibilityofWebElements("//a[contains(text(),'Remit Quotes')]","Void, Remit, Edit Contracts")){
				result2 = EditContract();
				finalresult = finalresult || result2;
			}
			if (VisibilityofWebElements("//a[contains(text(),'Remit Quotes')]","Void, Remit, Edit Contracts")){
				result3=VoidScenario();
				finalresult = finalresult ||result3;
			}
			if (VisibilityofWebElements("//a[contains(text(),'Remit Quotes')]","Void, Remit, Edit Contracts")){
				result4=RemitContractCompletion();
				finalresult = finalresult || result4;
			}
			if (VisibilityofWebElements("//a[contains(text(),'Contract Search')]","SearchContract")){
				result7=SearchContract(ContractNumber);
				finalresult = finalresult || result7;
			}
			if (VisibilityofWebElements("//a[contains(text(),'Contract Search')]","CancelContracts")){
				result8=CancelContracts(ContractNumber);
				finalresult = finalresult || result8;
				
			}
			if (VisibilityofWebElements("//a[contains(text(),'Commissions')]","Set Default Commission")){
				result5=SetDefaultCommissionWitoutValidatingCustomerCost();
				finalresult = finalresult ||result5;
				
			}
			if (VisibilityofWebElements("//li[@id='01rd0000000JATw_Tab']/a","ViewQuote")){
				result6=ViewQuotes(QuoteId);
				System.out.println(result6);
				finalresult = finalresult || result6;
			}
		
	
			if (VisibilityofWebElements("//li[@id='01rd0000000JATw_Tab']/a","Cancel Quote")){
				//perform cancel quote scenarios
				CancelQuote(QuoteId);
				finalresult = finalresult || result8;
			}else
			{
				scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "Cancel Quote", "No");
			}
			
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			System.out.println(finalresult);
			if (!finalresult){
				scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "OverallTCResult", "PASS");		
			}else
			{
				scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "OverallTCResult", "FAIL");
			}
		}
	
	}
	
	
	@SuppressWarnings("finally")
	private Boolean CreateNewQuote() throws InterruptedException, IOException 
	{
		try{
			result= false;
		
			String errormsg= null;
			//click on create new button
			stepExecutor.clickButton("findElementByXPath","//input[@name='new']", webDriver, "CreateQuote");
			Thread.sleep(2000);
			
			String ProductType = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "ProductToSelect");
			String SubProductTypeSelection = scriptExecutor.readDataFile(strDataFileName, TestCase,rownumber, "SubProductandContractTypes");
			//Code for checkboxes starts for Product selection for entered dealer in textbox
			if (FindDescendantsofCheckbox(ProductType, SubProductTypeSelection)){
				reporter.writeStepResult("Product selection", "Select Product",ProductType, "Pass", "Product selected", true, webDriver);
			}else{
				reporter.writeStepResult("Product selection", "Select Product",ProductType, "fail", "Product not selected/displayedto user", true, webDriver);
				errormsg ="Product not selected/displayedto user";
				result = true;
				}
			Thread.sleep(20000);
			
			// Enter 17 characters VIN
			RadioButtonYesNoClick();
			// Enter Odometer value
			stepExecutor.enterTextValue("findElementById","pg:frm:VehicleBlock:j_id257:j_id264:j_id268", DataMap,"Odometer", webDriver, "AN_GLOW");
			Thread.sleep(2000);
			String FirstName = scriptExecutor.readDataFile(strAbsolutepath, TestCase,rownumber, "FirstName");
			String LastName = scriptExecutor.readDataFile(strAbsolutepath, TestCase,rownumber, "LastName");
			webDriver.findElementByXPath(".//input[@id='pg:frm:VehicleBlock:j_id283:j_id284:j_id289']").sendKeys(FirstName);
			webDriver.findElementByXPath(".//input[@id='pg:frm:VehicleBlock:j_id283:j_id284:j_id289']").sendKeys(Keys.TAB);
			webDriver.findElementByXPath(".//input[@id='pg:frm:VehicleBlock:j_id283:j_id290:j_id295']").sendKeys(Keys.TAB);
			webDriver.findElementByXPath(".//input[@id='pg:frm:VehicleBlock:j_id283:LastNameRegion:j_id300']").sendKeys(LastName);
			// Enter first name and last name
			// click on Get Rates button
			stepExecutor.clickButton("findElementByXPath",".//input[@id='pg:frm:GetRatesButton']", webDriver, "Create Quote");
			webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			//verify.verifyElementIsPresentCheck(webDriver, "pg:frm:test1:j_id39:j_id40:0:j_id41:j_id42:j_id45", "id")
			if (verify.verifyElementIsPresentCheck(webDriver, "pg:frm:test1:j_id39:j_id40:0:j_id41:j_id42:j_id45", "id"))
			{
				if(errormsg!=null)
				{
					errormsg = errormsg + webDriver.findElement(By.id("pg:frm:test1:j_id39:j_id40:0:j_id41:j_id42:j_id45")).getText();
					System.out.println(errormsg);
				}else{
					errormsg = webDriver.findElement(By.id("pg:frm:test1:j_id39:j_id40:0:j_id41:j_id42:j_id45")).getText();
					System.out.println(errormsg);
				}
				
				scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "CreateQuote_FailedReason", errormsg);
				result = true;
				
			}else{
				System.out.println("no error found");
				//Click on Continue button
				stepExecutor.clickButton("findElementByXPath",".//input[@id='pg:frm:savebtn']", webDriver, "Create Quote");
				//Enter Address,City,State,ZipCode,Phone,Email,Type of contract and Lienholder details
				stepExecutor.enterTextValue("findElementById",
						"pg:frmCon:contactPB:contactPBs:pbsBA1:ba1", DataMap,
						"Address", webDriver, "TWG");
				stepExecutor.enterTextValue("findElementById",
						"pg:frmCon:contactPB:contactPBs:pbsbCity:bCity", DataMap,
						"City", webDriver, "TWG");
				String StateAbbr = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "State");
				StateAbbr = GetProperties(StateAbbr).trim();
				new Select(webDriver.findElementById("pg:frmCon:contactPB:contactPBs:pbsBState:bState")).selectByVisibleText(StateAbbr);
				//Select Country
				stepExecutor.selectListValue("findElementByXpath", ".//select[@id='pg:frmCon:contactPB:contactPBs:pbsBCountry:bCountry']",DataMap,"Country", webDriver, "Create Quote");
				
				stepExecutor.enterTextValue("findElementById",
						"pg:frmCon:contactPB:contactPBs:pbsbZip:bZip", DataMap,
						"Zip Code", webDriver, "Create Quote");
				
				stepExecutor.enterTextValue("findElementById",
						"pg:frmCon:contactPB:contactPBs:pbsPh1:bph1", DataMap,
						"Phone", webDriver, "Create Quote");
				stepExecutor.enterTextValue("findElementById",
						"pg:frmCon:contactPB:contactPBs:pbsEmail1:bEmail1",
						DataMap, "Email", webDriver, "Create Quote");
				
				//Select Type of contract = "Cash"
				webDriver.findElement(By.id("pg:frmCon:contactPB:contactPBs:pbsEmail1:bEmail1")).sendKeys(Keys.TAB);
				stepExecutor.selectListValue("findElementByXpath", ".//select[@id='pg:frmCon:finance:pgsf:pbsif:conType']",DataMap,"TypeOfContract", webDriver, TestCase);
				String  TypeOfContract = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "TypeOfContract");
				if (TypeOfContract.contains("Finance"))
				{
					stepExecutor.selectListValue("findElementByXpath", ".//select[@id='pg:frmCon:finance:pgsf:pbsif:conType']",DataMap,"TypeOfContract", webDriver, TestCase);
					EnterLienholderDetails();
		
				}
				String ContractNumber = null;
				String DealerType= scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "DealerType");
				if (DealerType.contains("Submit"))
				{
					String agreementnumber = scriptExecutor.readDataFile(
							strDataFileName, TestCase,rownumber, "AgreemenNumber");
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
				}	
				stepExecutor.clickButton("findElementByXPath","//input[@id='pg:frmCon:j_id929']", webDriver, "CreateQuote");
				Thread.sleep(5000);
				if (verify.verifyElementIsPresentCheck(webDriver, "pg:frmCon:j_id621:j_id622:j_id623:0:j_id624:j_id625:j_id627", "id"))
				{
					if(errormsg!=null)
					{
						errormsg = errormsg + webDriver.findElement(By.id("pg:frmCon:j_id621:j_id622:j_id623:0:j_id624:j_id625:j_id627")).getText();
						System.out.println(errormsg);
					}else{
						errormsg = webDriver.findElement(By.id("pg:frmCon:j_id621:j_id622:j_id623:0:j_id624:j_id625:j_id627")).getText();
						System.out.println(errormsg);
					}
					
					scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "CreateQuote_FailedReason", errormsg);
					result = true;
					
				}else
				{
					String PDFFilePath = strDataPath;
					String quoteID = webDriver.findElementByXPath("//*[@id='pg:j_id932:j_id940']/div/table/tbody/tr[4]/td[2]").getText();
					reporter.writeStepResult("QuoteID", "QuoteID capture in results",quoteID, "Pass", "QuoteID written successfully in results", true, webDriver);
					scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "QuoteIDCreated", quoteID);
					if (DealerType.contains("Submit"))
					{
					stepExecutor.clickButton("findElementById","pg:j_id932:detail:warrInfo:submit", webDriver, "CreateQuote");
					Thread.sleep(6000);
				
					System.out.println("no error found");
					ContractNumber = webDriver.findElementByXPath("//td[6]/span").getText();
					scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "ContractNumberCreated",ContractNumber);
					}
				
					//purchase user
					if (DealerType.contains("Purchase"))
					{
						if (verify.verifyElementIsPresentCheck(webDriver, "pg:j_id932:detail:warrInfo:purchase", "id"))
						{
							stepExecutor.clickButton("findElementById","pg:j_id932:detail:warrInfo:purchase", webDriver, "CreateQuote");
							assertTrue(closeAlertAndGetItsText().matches("^Changes can not be made after a contract has been submitted\\. Are you sure you want to submit this contract [\\s\\S]$"));
							Thread.sleep(5000);
							ContractNumber = webDriver.findElementByXPath("//td[6]/span").getText();
							scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "ContractNumberCreated",ContractNumber);
						} else {
							result = true;
							if(errormsg!=null)
							{
								errormsg = errormsg +"Dealer Type Mismatch";
								scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "CreateQuote_FailedReason",errormsg);
							} else {
								errormsg = errormsg +"Dealer Type Mismatch";
								scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "CreateQuote_FailedReason",errormsg);
							}
						}
					}
					if (DealerType.contains("Remit"))
					{
						if (verify.verifyElementIsPresentCheck(webDriver, "pg:j_id932:detail:warrInfo:remit", "id"))
						{
							stepExecutor.clickButton("findElementByXPath","//input[@id='pg:j_id932:detail:warrInfo:remit']", webDriver, "CreateQuote");
							Thread.sleep(5000);
							System.out.println("Capture the contract number from the PDF");
							//webDriver.findElementByXPath("//td[5]/a/img").click();
						//	Thread.sleep(20000);
							openPDF();
							
							//String PurchaseVerificaionpoints = readpdf.openPDFfromfilelocation(PDFFilePath);
							String filename = readpdf.getFilename(PDFFilePath);
							String PurchaseVerificaionpoints = readpdf.readPDFFromFileLocation(PDFFilePath + filename);
							System.out.println(PurchaseVerificaionpoints);
							 ContractNumber = readpdf.ExtractTextWithPattern("SQ");
							 System.out.println(ContractNumber);
							scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "ContractNumberCreated", ContractNumber);
						} else
						{
							result = true;
							if(errormsg!=null)
							{
								errormsg = errormsg +"Dealer Type Mismatch";
								scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "CreateQuote_FailedReason",errormsg);
								result = true;
							} else {
								errormsg = errormsg +"Dealer Type Mismatch";
								scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "CreateQuote_FailedReason",errormsg);
							}
									
						}
						
					}
					
				}
			}
			
		
		}catch(Exception e){
			System.out.println(e.getMessage());
			
		}finally{
			//close pdf document
			String PDFfile = readpdf.getFilename(strDataPath);
        	File f1 = new File(strDataPath+PDFfile);
        	if(f1.exists()){
        		f1.delete();
        	}
        	return result;
		}

		
		
	}

	public String openPDF() {
		
		//Open PDF 
		String pdfurl = null;
		try {
			String currenturl = webDriver.getCurrentUrl();
			if (webDriver.findElement(By.xpath("//td[5]/a/img")).isDisplayed())
			{
				stepExecutor.clickImage("findElementByXPath","//td[5]/a/img",webDriver,"TWG");
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
		stepExecutor.selectListValueByContainsValue("findElementByXpath", ".//select[@id='pg:frmCon:pbl:pbls:pbsil:lien']","SelectLienholder", webDriver, "TWG",rownumber);
		
		stepExecutor.enterTextValue("findElementById",
				"pg:frmCon:pbl:pbls:namelien:namelien1", DataMap,
				"Lienholder", webDriver, "TWG");
		stepExecutor.enterTextValue("findElementById",
				"pg:frmCon:pbl:pbls:addl1:ladd1", DataMap,
				"LienholderAddress", webDriver, "TWG");
		stepExecutor.enterTextValue("findElementById",
				"pg:frmCon:pbl:pbls:city:lcity", DataMap,
				"LienholderCity", webDriver, "TWG");
		String LienholderStateAbbr = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "LienholderState");
		LienholderStateAbbr = GetProperties(LienholderStateAbbr).trim();
		new Select(webDriver.findElementById("pg:frmCon:pbl:pbls:statel:lstate")).selectByVisibleText(LienholderStateAbbr);
		stepExecutor.enterTextValue ("findElementById",
				"pg:frmCon:pbl:pbls:zipl:lzip", DataMap,
				"LienholderZip", webDriver, "TWG");
		
		stepExecutor.enterTextValue ("findElementById",
				"pg:frmCon:pbl:pbls:countryl:lcountry", DataMap,
				"LienholderCountry", webDriver, "TWG");
		
		stepExecutor.enterTextValue("findElementById",
				"pg:frmCon:pbl:pbls:tell:lphone", DataMap,
				"Phone", webDriver, "TWG");
		
		//Click on Save Lienholder button 
		stepExecutor.clickButton("findElementByXPath", ".//input[@id='pg:frmCon:pbl:pbls:savelienholder:j_id853']", webDriver, "TWG");
		Thread.sleep(8000);
	}
	
	private boolean EditContract() throws InterruptedException 
	{
		try 
		{
			result=false;
			stepExecutor.clickLink("findElementByLinkText", "Remit Quotes",webDriver, "Remit");
			Thread.sleep(3000);
			String quoteid = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber,"QuoteId");
			String dealername = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber,"Dealer Name");
			dealername = dealername.trim();
			dealername = dealername.toUpperCase();

			
		//	webDriver.findElement(By.xpath(".//a[contains(text(),'"+dealername+"')]/parent::td/preceding-sibling::td/a")).click();
			stepExecutor.clickLink("findElementByXPath", ".//a[contains(text(),'"+dealername+"')]/parent::td/preceding-sibling::td/a", webDriver, "AN_GLOWProduction");
			//Click on Edit Contract image
			if (verify.verifyElementIsPresentCheck(webDriver, ".//span[contains(text(),'"+quoteid+"')]/parent::td/input[@type='checkbox']", "xpath"))
			{
				stepExecutor.clickElement("findElementByXPath", ".//span[contains(text(),'"+quoteid+"')]/parent::td/input[@type='checkbox']", webDriver, TestCase);
				
				
				//webDriver.findElement(By.xpath("//tr[1]/td[9]/img")).click();
				webDriver.findElementByXPath("//span[text()='"+quoteid+"']/parent::td/input[@type='checkbox']/parent::td/following-sibling::td[7]/img").click();
				Thread.sleep(1000);
				//Edit Lienholder name
			//	stepExecutor.enterTextValue("findElementById", "pg:frm:liename", DataMap, "LienHolderName", webDriver, "EditContract");
				//Edit FirstName
				stepExecutor.enterTextValue("findElementById", "pg:frm:j_id261", DataMap, "FirstName", webDriver, "EditContract");
				//Edit LastName
				stepExecutor.enterTextValue("findElementById", "pg:frm:j_id265", DataMap, "LastName", webDriver, "EditContract");
				//Edit Address
				stepExecutor.enterTextValue("findElementById", "pg:frm:j_id269", DataMap, "Address", webDriver, "EditContract");
				
				//Edit phone
				stepExecutor.enterTextValue("findElementById", "pg:frm:j_id293", DataMap, "Phone", webDriver, "EditContract");
				//Edit zip code
				stepExecutor.enterTextValue("findElementById", "pg:frm:j_id285", DataMap, "ZipCode", webDriver, "EditContract");
				//Click on save button
				stepExecutor.clickButton("findElementByXPath", ".//input[@id='pg:frm:j_id349']", webDriver, "EditContract");
				Thread.sleep(5000);
				//verify the save changes, open edit image icon again
				webDriver.findElementByXPath("//span[text()='"+quoteid+"']/parent::td/input[@type='checkbox']/parent::td/following-sibling::td[7]/img").click();		
				Thread.sleep(1000);
				//webDriver.findElement(By.xpath("//tr[1]/td[9]/img")).click();
				Thread.sleep(1000);
				//String LienholderName = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "LienHolderName");
				String firstName = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "FirstName");
				String LastName = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "LastName");
				String address = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Address");
				String phone = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Phone");
				String zipcode = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "ZipCode");
				//verify.verifyElementTextPresent(webDriver, "pg:frm:liename", "id", LienholderName);
				verify.verifyElementTextPresent(webDriver, "pg:frm:j_id261", "id", firstName);
				verify.verifyElementTextPresent(webDriver, "pg:frm:j_id265", "id", LastName);
				verify.verifyElementTextPresent(webDriver, "pg:frm:j_id269", "id", address);
				verify.verifyElementTextPresent(webDriver, "pg:frm:j_id293", "id", phone);
				verify.verifyElementTextPresent(webDriver, "pg:frm:j_id285", "id", zipcode);
				
				//Click on cancel button
				stepExecutor.clickButton("findElementByXPath", ".//input[@id='pg:frm:j_id351']", webDriver, "EditContract");
				Thread.sleep(2000);
			}else {
				result = true;
				scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "EditContract_FailedReason", "QuoteID not found to Edit contract");
			}
		  
		
		}
		finally{
			return result;
		}
		
	}

	public void identifydealername() throws InterruptedException
	{
		// Enter Dealer account number for example 707j* in search text box and click on search button
		String dealername = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "Dealer Name");
		dealername = dealername.toUpperCase();
		stepExecutor.enterTextValue("findElementById", "phSearchInput",DataMap, "Client", webDriver, "identifydealername");
		webDriver.findElementById("phSearchInput").sendKeys(Keys.RETURN);
		Thread.sleep(4000);
		String dealeraccountnumber = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "Client");
		stepExecutor.clickLink("findElementByLinkText", dealername, webDriver, "IdentifyDealerName");		
		Thread.sleep(10000);
         
	}
	
	// NextFuncBody
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
					"username", webDriver, "LaunchApplication");
			// Code for textboxes starts for entering password
			stepExecutor.enterTextValue("findElementById", "password", DataMap,
					"pw", webDriver, "LaunchApplication");

			// Code for login button starts
			stepExecutor.clickButton("findElementById", "Login", webDriver,
					"LaunchApplication");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Login() throws InterruptedException
	{
		int count = 0; 
		while (count < 4){
		    try {

				Map<String, String> dataMapLocal = DataMap;
				String strData = null;
				if (dataMapLocal.containsKey("username")) {
					strData= dataMapLocal.get("username");
				}
				webDriver.findElement(By.xpath("//input[@id='username']")).clear();
		       WebElement yourSlipperyElement= webDriver.findElement(By.xpath("//input[@id='username']"));
		       yourSlipperyElement.click(); 
		       yourSlipperyElement.sendKeys(strData);
		       if (dataMapLocal.containsKey("pw")) {
					strData = dataMapLocal.get("pw");
				}
		       WebElement yourSlipperyElement1= webDriver.findElement(By.xpath("//input[@id='password']"));
		       yourSlipperyElement1.click(); 
		       yourSlipperyElement1.sendKeys(strData);
		       webDriver.findElement(By.xpath("//input[@id='password']")).sendKeys(Keys.TAB);
		       webDriver.findElement(By.xpath("//button[@id='Login']")).sendKeys(Keys.RETURN);
		     } catch (StaleElementReferenceException e){
		       e.toString();
		       System.out.println("Trying to recover from a stale element :" + e.getMessage());
		       count = count+1;
		     }
		    
		   count = count+4;
		   Thread.sleep(8000);
		}
	}

	public Boolean VisibilityofWebElements(String strElement,String expectedToken)
	{
		Boolean elementexists = false;
		
		if (verify.verifyElementIsPresentCheck(webDriver, strElement, "xpath")){
			scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, expectedToken, "Yes");
			elementexists= true;
		}else{
			scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, expectedToken, "No");
		}
		
		return elementexists;
		
	}
	
	public boolean VoidScenario() throws InterruptedException
	{
		try{
			result = false;
			stepExecutor.clickLink("findElementByLinkText", "Remit Quotes",webDriver,"VoidScenario");
			Thread.sleep(5000);
			
			//Select Quote ID/Contract number
			String quoteid = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber,"QuoteId");
			String dealername = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber,"Dealer Name");
			dealername = dealername.trim();
			dealername = dealername.toUpperCase();
			stepExecutor.clickElement("findElementByXPath", ".//a[contains(text(),'"+dealername+"')]/parent::td/preceding-sibling::td/a", webDriver, "VoidScenario");
			//Click on quote id
			if (verify.verifyElementIsPresentCheck(webDriver, ".//span[contains(text(),'"+quoteid+"')]/parent::td/input[@type='checkbox']", "xpath"))
			{
				stepExecutor.clickElement("findElementByXPath", ".//span[contains(text(),'"+quoteid+"')]/parent::td/input[@type='checkbox']", webDriver, TestCase);
				//Click on Void Button
				stepExecutor.clickButton("findElementByXPath", ".//input[@id='pg:frm:theBlock:j_id72']", webDriver, TestCase);
	
				//Enter the void reason
				 webDriver.findElement(By.id("pg:frm:voidtext")).clear();
				 stepExecutor.enterTextValue("findElementByXPath", ".//*[@id='pg:frm:voidtext']", DataMap, "VoidReason", webDriver,TestCase);
				// webDriver.findElement(By.id("pg:frm:voidtext")).sendKeys("void this quote");
					
				//Click Void Button
				stepExecutor.clickButton("findElementByXPath", ".//input[@id='pg:frm:j_id136']", webDriver,TestCase);
				Thread.sleep(3000);
				
			}else {
				result = true;
				scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "VoidContract_FailedReason", "QuoteID not found to Edit contract");
			}
		}catch (Exception e)
		{
				System.out.println(e.getMessage());
		}finally{
			return result;
		}
	}
	
	public boolean RemitContractCompletion() {
		try {
			result=false;
			stepExecutor.clickLink("findElementByLinkText", "Remit Quotes",webDriver, "Remit");
			Thread.sleep(5000);
			String dealername = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber,"Dealer Name");
			dealername = dealername.trim();
			dealername = dealername.toUpperCase();
	
			stepExecutor.clickElement("findElementByXPath", ".//a[contains(text(),'"+dealername+"')]/parent::td/preceding-sibling::td/a", webDriver, "AN_GLOWProduction");
			//Click on quote id
			//Select Quote ID/Contract number
			String quoteid = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber,"QuoteId");
			if (verify.verifyElementIsPresentCheck(webDriver, ".//span[contains(text(),'"+quoteid+"')]/parent::td/input[@type='checkbox']", "xpath"))
			{
				stepExecutor.clickElement("findElementByXPath", ".//span[contains(text(),'"+quoteid+"')]/parent::td/input[@type='checkbox']", webDriver, TestCase);
				stepExecutor.clickLink("findElementById", "pg:frm:theBlock:j_id71",webDriver, "Remit");
				Thread.sleep(5000);
				stepExecutor.clickLink("findElementById", "pg:frm:j_id172",webDriver, "Remit");
				Thread.sleep(5000);

			}else {
				result = true;
				scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "RemitContract_FailedReason", "QuoteID not found to Edit contract");
			}
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}finally{
			return result;
		}
		

	}


	
	//Search for a Contract	
	public boolean SearchContract(String ContractNumber) throws InterruptedException
	{
		try{	
			result=false;	
			//Click on Contract search link
			stepExecutor.clickLink("findElementByXPath","//a[contains(text(),'Contract Search')]", webDriver, "CancelContracts");
			Thread.sleep(5000);
			//Enter Contract number
			webDriver.findElementByXPath("//input[@id='pg:frm:ContractSearchBlock:PPMContractSearch:contractsearchname:contractname']").clear();
			webDriver.findElementByXPath("//input[@id='pg:frm:ContractSearchBlock:PPMContractSearch:contractsearchname:contractname']").sendKeys(ContractNumber);
			//stepExecutor.enterTextValue("findElementByXPath", "//input[@id='pg:frm:ContractSearchBlock:PPMContractSearch:contractsearchname:contractname']", DataMap, "ContractNumber", webDriver, "CancelContracts");
			webDriver.findElementByXPath(".//*[@id='pg:frm:ContractSearchBlock:PPMContractSearch']/div/table/tbody/tr[9]/td/input").clear();
			//Click on Search button
			stepExecutor.clickButton("findElementByXPath",".//input[@id='pg:frm:ContractSearchBlock:pgsec:searchbtn']", webDriver, "CancelContracts");
			Thread.sleep(2000);
		/*	if (verify.verifyElementIsPresentCheck(webDriver,"pg:frm:j_id14:j_id15:j_id16:0:j_id17:j_id18:j_id20","id"))
			{
				String errormsg = webDriver.findElement(By.id("pg:frm:j_id14:j_id15:j_id16:0:j_id17:j_id18:j_id20")).getText();
				System.out.println(errormsg);
				scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "SearchContract_FailedReason", errormsg);
				result= true;
			}else{*/
				System.out.println("no error message");
				//verify terms and conditions button
				verify.verifyElementPresent(webDriver, "//input[@id='pg:frm:pb:terms1']", "xpath");
				
				//capture purchase mileage from screen
				String purchasemileage = webDriver.findElementByXPath(".//*[@id='pg:frm:pb:NONPPMContract:ContrInfo:j_id250:8:j_id251']").getText();
				purchasemileage = purchasemileage.replace(",", "");
				int mileage = Integer.valueOf(purchasemileage) ;
				mileage = mileage - 10;
				String odometerMileage = Integer.toString(mileage)  ;
				System.out.println(odometerMileage);
				//Enter Mileage less that purchase mileage
				webDriver.findElementByXPath(".//*[@id='pg:frm:ContractSearchBlock:PPMContractSearch']/div/table/tbody/tr[9]/td/input").clear();
				webDriver.findElementByXPath(".//*[@id='pg:frm:ContractSearchBlock:PPMContractSearch']/div/table/tbody/tr[9]/td/input").sendKeys(odometerMileage);
			
				//Click on Search button
				stepExecutor.clickButton("findElementByXPath",".//input[@id='pg:frm:ContractSearchBlock:pgsec:searchbtn']", webDriver, "CancelContracts");
				Thread.sleep(5000);
				mileage = Integer.valueOf(purchasemileage)+1000;
				String error = webDriver.findElement(By.xpath(".//div[@id='pg:frm:j_id14:j_id15:j_id16:0:j_id17:j_id18:j_id20']")).getText();
				error = error.replace("Error:","");
				System.out.println(error);
				String expectedstr ="Breakdown Odometer ("+odometerMileage+") cannot be less than Odometer at time of purchase ("+purchasemileage+"), Cannot cancel or create a Case";
				//Breakdown Odometer (9000) cannot be less than Odometer at time of purchase (10000), Cannot cancel or create a Case
				//Assert.assertEquals("Breakdown Odometer ("+odometerMileage+") cannot be less than Odometer at time of purchase ("+purchasemileage+"), Cannot cancel or create a Case", webDriver.findElement(By.xpath(".//div[@id='pg:frm:j_id14:j_id15:j_id16:0:j_id17:j_id18:j_id20']")).getText());
				if (error.contains(expectedstr)) {
					reporter.writeStepResult(
							System.getProperty("SearchContract"),
							"Verify odometermileage cannot be less than purchase mileage", "Expected: "
									+ expectedstr, "Pass", "Expected text  is present",
							true, webDriver);
				} else {
					reporter.writeStepResult(
							System.getProperty("SearchContract"),
							"Verify odometermileage cannot be less than purchase mileage", "Expected: "
									+ expectedstr, "Fail", "Expected text  is not present"+error,
							true, webDriver);
		
				}

				//Enter Mileage greater than purchase mileage
				mileage = Integer.valueOf(purchasemileage)+1000;
				System.out.println(mileage);
				webDriver.findElementByXPath(".//*[@id='pg:frm:ContractSearchBlock:PPMContractSearch']/div/table/tbody/tr[9]/td/input").clear();
				
				webDriver.findElementByXPath(".//*[@id='pg:frm:ContractSearchBlock:PPMContractSearch']/div/table/tbody/tr[9]/td/input").sendKeys(Integer.toString(mileage));
				//Click on Search button
				stepExecutor.clickButton("findElementByXPath",".//input[@id='pg:frm:ContractSearchBlock:pgsec:searchbtn']", webDriver, "CancelContracts");
				Thread.sleep(5000);
			
				//verify display of create case button
				verify.verifyElementIsPresent(webDriver, ".//input[@id='pg:frm:pb:createcasetop']", "xpath");
				//verify  cancel contract button display
				verify.verifyElementIsPresent(webDriver, ".//input[@id='pg:frm:pb:cancelquotetop']", "xpath");
				Thread.sleep(5000);
				//Enter Mileage greater than expired mileage
				String ExpiredMileage = webDriver.findElementByXPath(".//*[@id='pg:frm:pb:NONPPMContract:ContrInfo:j_id250:9:j_id251']").getText();
				ExpiredMileage = ExpiredMileage.replace(",","");
				mileage = Integer.valueOf(ExpiredMileage) + 1000;
				String Expiremileage = Integer.toString(mileage);
				webDriver.findElementByXPath(".//*[@id='pg:frm:ContractSearchBlock:PPMContractSearch']/div/table/tbody/tr[9]/td/input").clear();
				webDriver.findElementByXPath(".//*[@id='pg:frm:ContractSearchBlock:PPMContractSearch']/div/table/tbody/tr[9]/td/input").sendKeys(Expiremileage);
				//Click on Search button
				stepExecutor.clickButton("findElementByXPath",".//input[@id='pg:frm:ContractSearchBlock:pgsec:searchbtn']", webDriver, "CancelContracts");
				Thread.sleep(3000);
				error = webDriver.findElement(By.xpath(".//div[@id='pg:frm:j_id14:j_id15:j_id16:0:j_id17:j_id18:j_id20']")).getText();
				error = error.replace("Error:","");
				System.out.println(error);
				expectedstr ="Breakdown Odometer greater than Contract Agreement Miles, Cannot cancel or create a Case";
				if (error.contains(expectedstr)) {
					reporter.writeStepResult(
							System.getProperty("SearchContract"),
							"Breakdown Odometer greater than Contract Agreement Miles, Cannot cancel or create a Case", "Expected: "
									+ expectedstr, "Pass", "Expected text  is present",
							true, webDriver);
				} else {
					reporter.writeStepResult(
							System.getProperty("SearchContract"),
							"Breakdown Odometer greater than Contract Agreement Miles, Cannot cancel or create a Case", "Expected: "
									+ expectedstr, "Fail", "Expected text  is not present"+error,
							true, webDriver);
		
				}
				//verify no display of create case button
				verify.verifyElementAbsent(webDriver, ".//input[@id='pg:frm:pb:createcasetop']", "xpath");
				//verify no  cancel contract button display
				verify.verifyElementAbsent(webDriver, ".//input[@id='pg:frm:pb:cancelquotetop']", "xpath");
				//Assert.assertEquals("Breakdown Odometer greater than Contract Agreement Miles, Cannot cancel or create a Case", webDriver.findElement(By.xpath(".//*[@id='pg:frm:j_id14:j_id15:j_id16:0:j_id17:j_id18:j_id20']")).getText());
			//}
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		finally{
			return result;
		}
			

	
		
	}
	
	//Cancel quote
	public boolean CancelQuote(String QuoteId)
	{
		
		try 
		{
			result=false;
			stepExecutor.enterTextValue("findElementById", "phSearchInput",DataMap, "Dealer Name", webDriver, "CancelQuote");
			webDriver.findElementById("phSearchInput").sendKeys(Keys.RETURN);
			Thread.sleep(5000);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='Account_body']/table/tbody/tr[2]/th/a")));
			//click on dealer name
			stepExecutor.clickElement("findElementByXPath", ".//*[@id='Account_body']/table/tbody/tr[2]/th/a", webDriver,"Cancel Quote");
			Thread.sleep(5000);
		//	wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='001d000001R7C3q_00Nd0000007KYFN_body']/div/a[2]")));
			//Click on Go to list of quotes
			stepExecutor.clickLink("findElementByXPath", ".//*[@id='001d000001T6Kay_00Nd0000007KYFN_body']/div/a[2]", webDriver, "CancelQuote");
			////a[contains(text(),'Q-0002972')]/parent::th/following-sibling::td[1]  -- this will give 
			//Verify Quote id in quoted status
			while (!(webDriver.findElement(By.xpath("//a[contains(text(),'"+QuoteId+"')]")).isDisplayed()))
			{
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='bodyCell']/div[3]/div/div[2]/div/a[2]/img")));
				//click on more records
				stepExecutor.clickElement("findElementByXPath", ".//*[@id='bodyCell']/div[3]/div/div[2]/div/a[2]/img", webDriver, "Cancel Quote");
			}
			verify.verifyElementIsPresent(webDriver, "//a[contains(text(),'"+QuoteId+"')]", "xpath");
			//Verify status of quote
			Thread.sleep(3000);
			String Status = webDriver.findElement(By.xpath("//a[contains(text(),'"+QuoteId+"')]/parent::th/following-sibling::td[1]")).getText();
			if (Status.equals("Quoted")){
				//click on Del button
				if (verify.verifyElementIsPresentCheck(webDriver, "//a[contains(text(),'"+QuoteId+"')]/parent::th/preceding-sibling::td[1]/a[contains(text(),'Del')]", "xpath"))
				{
					stepExecutor.clickElement("findElementByXPath", "//a[contains(text(),'"+QuoteId+"')]/parent::th/preceding-sibling::td[1]/a[contains(text(),'Del')]", webDriver, "CancelQuotes");
					assertTrue(closeAlertAndGetItsText().matches("^Are you sure[\\s\\S]$"));
					Thread.sleep(1000);
					verify.verifyElementAbsent(webDriver, "//a[contains(text(),'"+QuoteId+"')", "xpath");
					scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "Cancel Quote", "Yes");
					result=false;
					
				}else {
					scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "Cancel Quote", "No");
					result=false;
				}

			} else {
				reporter.writeStepResult("Cancel Quote", "",QuoteId, "FAIL", "No quote in quoted status", true, webDriver);
				scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "CancelQuote_FailedReason", "No quote in quoted status");
				result = true;
			}
			
			
			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally{
			return result;
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


	//Cancel contracts
	@SuppressWarnings({ "deprecation", "unused" })
	public boolean CancelContracts(String ContractNumber) throws InterruptedException
	{
		try{
		result=false;
		//Click on Contract search link
		stepExecutor.clickLink("findElementByXPath","//a[contains(text(),'Contract Search')]", webDriver, "CancelContracts");
		Thread.sleep(5000);
		//Enter Contract number
		webDriver.findElementByXPath("//input[@id='pg:frm:ContractSearchBlock:PPMContractSearch:contractsearchname:contractname']").sendKeys(ContractNumber);
		
		//Click on Search button
		stepExecutor.clickButton("findElementByXPath",".//input[@id='pg:frm:ContractSearchBlock:pgsec:searchbtn']", webDriver, "CancelContracts");
		Thread.sleep(5000);
		
/*		if (verify.verifyElementIsPresentCheck(webDriver,"pg:frm:j_id14:j_id15:j_id16:0:j_id17:j_id18:j_id20","id"))
		{
			String errormsg = webDriver.findElement(By.id("pg:frm:j_id14:j_id15:j_id16:0:j_id17:j_id18:j_id20")).getText();
			System.out.println(errormsg);
			scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "SearchContract_FailedReason", errormsg);
			result= true;
		}else
		{*/
			System.out.println("No error message found");
			//verify terms and conditions button
			verify.verifyElementPresent(webDriver, "//input[@id='pg:frm:pb:terms1']", "xpath");
			Assert.assertEquals("The Warranty Group - Dealer Site", webDriver.getTitle());
			
			//capture purchase mileage from screen
			String purchasemileage = webDriver.findElementByXPath(".//*[@id='pg:frm:pb:NONPPMContract:ContrInfo:j_id250:8:j_id251']").getText();
			purchasemileage = purchasemileage.replace(",", "");
			int mileage = Integer.valueOf(purchasemileage) ;
			mileage = mileage + 1000;
			String odometerMileage = Integer.toString(mileage)  ;
			
			//Enter Mileage greater than purchase mileage
			webDriver.findElementByXPath("/.//*[@id='pg:frm:ContractSearchBlock:PPMContractSearch']/div/table/tbody/tr[9]/td/input").clear();
			webDriver.findElementByXPath(".//*[@id='pg:frm:ContractSearchBlock:PPMContractSearch']/div/table/tbody/tr[9]/td/input").sendKeys(odometerMileage);
			//Click on Search button
			stepExecutor.clickButton("findElementByXPath",".//input[@id='pg:frm:ContractSearchBlock:pgsec:searchbtn']", webDriver, "CancelContracts");
				
			//verify display of create case button
			verify.verifyElementIsPresent(webDriver, ".//input[@id='pg:frm:pb:createcasetop']", "xpath");
			//verify  cancel contract button display
			//verify.verifyElementIsPresent(webDriver, ".//input[@id='pg:frm:pb:cancelquotetop']", "xpath");
			Boolean cancelcontract = false;
			if (verify.verifyElementIsPresent(webDriver, ".//input[@id='pg:frm:pb:cancelquotetop']", "xpath")){
				scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "CancelContracts", "Yes");
				cancelcontract = true;
			}else{
				scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "CancelContracts", "No");
			}
			
			if (cancelcontract)
			{
					
				stepExecutor.clickButton("findElementByXPath",".//input[@id='pg:frm:pb:cancelquotetop']", webDriver, "CancelContracts");
				Thread.sleep(2000);
				//String agreementholdercost= webDriver.findElementByXPath(".//*[@id='cancelQuotePg:thisform:pg2:table:0:j_id58']").getText();
				//Click on go to cancellation quote button
				stepExecutor.clickButton("findElementByXPath",".//input[@id='cancelQuotePg:thisform:pg2:j_id29:j_id33']", webDriver, "CancelContracts");
				Thread.sleep(2000);
		
				String cancellationreason = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "SelectCancellationReason").trim();
				String cancellationdesc = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "SelectCancellationReasonDescription");
				new Select(webDriver.findElement(By.id("cancelQuotePg:thisform:pg3:pgtable3:0:j_id75"))).selectByVisibleText("Agreement Holder");
			    webDriver.findElement(By.cssSelector("option[value=\"Agreement Holder\"]")).click();
			    webDriver.findElement(By.id("cancelQuotePg:thisform:pg3:pgtable3:0:j_id78")).click();
			    webDriver.findElement(By.id("cancelQuotePg:thisform:pg3:pgtable3:0:j_id78")).clear();
			    webDriver.findElement(By.id("cancelQuotePg:thisform:pg3:pgtable3:0:j_id78")).sendKeys(cancellationdesc);
		
		
				//verify net refund cost: .//*[@id='cancelQuotePg:thisform:pg3:pgtable3:0:j_id80']
				String clientnetrefundcost = verify.verifyandstoreElementTextPresent(webDriver, ".//*[@id='cancelQuotePg:thisform:pg3:pgtable3:0:j_id83']", "xpath");
				System.out.println(clientnetrefundcost);
				
				String customernetrefundcost = verify.verifyandstoreElementTextPresent(webDriver, ".//*[@id='cancelQuotePg:thisform:pg3:pgtable3:0:j_id80']", "xpath");
				System.out.println(customernetrefundcost);
				
				//compare customer net refund cost with agreement holder cost
				
				
				//click on view details link
				stepExecutor.clickLink("findElementByXPath", ".//a[@id='cancelQuotePg:thisform:pg3:pgtable3:0:custPopUpPg3']", webDriver, "CancelContracts");
				Thread.sleep(2000);
			    verify.verifyElementPresent(webDriver, "//div[@id='cancelQuotePg:thisform:popUpContract1:customercontract1']/div/table/tbody/tr[2]/td[2]", "xpath");
			    webDriver.findElement(By.id("cancelQuotePg:thisform:j_id155")).click();
			    Thread.sleep(5000);
				
				//click on submit button: .//*[@id='cancelQuotePg:thisform:pg3:j_id65:panelpg3']/input[3]
				stepExecutor.clickButton("findElementByXPath",".//*[@id='cancelQuotePg:thisform:pg3:j_id65:panelpg3']/input[3]", webDriver, "CancelContracts");
				Thread.sleep(5000);
				
				//verify green check mark & message: Sucessfully Submitted for Cancellation
				verify.verifyElementIsPresent(webDriver, ".//td[@id='cancelQuotePg:thisform:pg3:pgtable3:0:j_id70']/img", "xpath");
				
				verify.verifyElementIsPresent(webDriver, ".//td[@id='cancelQuotePg:thisform:pg3:pgtable3:0:j_id96']/span", "xpath");
				
				
				//search a contract
				//Click on Go back to contract search button
				stepExecutor.clickButton("findElementByXPath",".//*[@id='cancelQuotePg:thisform:pg3:j_id65:panelpg3']/input", webDriver, "CancelContracts");
				Thread.sleep(2000);
				//Enter Contract number
				webDriver.findElementByXPath("//input[@id='pg:frm:ContractSearchBlock:PPMContractSearch:contractsearchname:contractname']").sendKeys(ContractNumber);
				//stepExecutor.enterTextValue("findElementByXPath", "//input[@id='pg:frm:ContractSearchBlock:PPMContractSearch:contractsearchname:contractname']", DataMap, "ContractNumber", webDriver, "CancelContracts");
				
				//Click on Search button
				stepExecutor.clickButton("findElementByXPath",".//input[@id='pg:frm:ContractSearchBlock:pgsec:searchbtn']", webDriver, "CancelContracts");
				Thread.sleep(2000);
				
				
				//verify contract number is still displayed
				verify.verifyElementPresent(webDriver, ".//*[@id='pg:frm:pb:NONPPMContract:ContrInfo:j_id250:0:j_id251']", "xpath");
			}
			//Enter Mileage greater than purchase mileage , less than expiration mileage
			mileage = Integer.valueOf(purchasemileage) ;
			mileage = mileage + 1000;
			odometerMileage = Integer.toString(mileage)  ;
			webDriver.findElementByXPath(".//*[@id='pg:frm:ContractSearchBlock:PPMContractSearch']/div/table/tbody/tr[9]/td/input").clear();
			webDriver.findElementByXPath(".//*[@id='pg:frm:ContractSearchBlock:PPMContractSearch']/div/table/tbody/tr[9]/td/input").sendKeys(odometerMileage);
			//Click on Search button
			stepExecutor.clickButton("findElementByXPath",".//input[@id='pg:frm:ContractSearchBlock:pgsec:searchbtn']", webDriver, "CancelContracts");
				
			//verify no display of create case button
			verify.verifyElementAbsent(webDriver, ".//input[@id='pg:frm:pb:createcasetop']", "xpath");
			//verify no  cancel contract button display
			verify.verifyElementAbsent(webDriver, ".//input[@id='pg:frm:pb:cancelquotetop']", "xpath");
	//	}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		finally{
			return result;
		}

	}



	//Create Quote	View Quotes	Create Contact	Reset Password	Set Default Lienholders	Set Default Commission	Create/View Reports
	public boolean CreateQuote() throws InterruptedException
	{
		try 
		{
		
			result=false;
			//Click on Quotes link
			stepExecutor.clickLink("findElementByXPath","//a[contains(text(),'Quotes')]", webDriver, "CreateQuote");
			Thread.sleep(2000);
			
			//click on create new button
			stepExecutor.clickButton("findElementByXPath","//input[@name='new']", webDriver, "CreateNewQuote");
			Thread.sleep(2000);
			
			String ProductType = scriptExecutor.readDataFile(strDataFileName,TestCase, rownumber, "ProductToSelect");
			String SubProductTypeSelection = scriptExecutor.readDataFile(strDataFileName, TestCase,rownumber, "SubProductandContractTypes");
			//Code for checkboxes starts for Product selection for entered dealer in textbox
			if (FindDescendantsofCheckbox(ProductType, SubProductTypeSelection)){
				reporter.writeStepResult("Product selection", "Select Product",ProductType, "Pass", "Product selected", true, webDriver);
			}else{
				reporter.writeStepResult("Product selection", "Select Product",ProductType, "fail", "Product not selected", true, webDriver);
				result = true;
			}
			Thread.sleep(20000);
			
			// Enter 17 characters VIN
			RadioButtonYesNoClick();
			// Enter Odometer value
			stepExecutor.enterTextValue("findElementById","pg:frm:VehicleBlock:j_id257:j_id264:j_id268", DataMap,"Odometer", webDriver, "AN_GLOW");
			Thread.sleep(2000);
			String FirstName = scriptExecutor.readDataFile(strAbsolutepath, TestCase,rownumber, "FirstName");
			String LastName = scriptExecutor.readDataFile(strAbsolutepath, TestCase,rownumber, "LastName");
			webDriver.findElementByXPath(".//input[@id='pg:frm:VehicleBlock:j_id283:j_id284:j_id289']").sendKeys(FirstName);
			webDriver.findElementByXPath(".//input[@id='pg:frm:VehicleBlock:j_id283:j_id284:j_id289']").sendKeys(Keys.TAB);
			webDriver.findElementByXPath(".//input[@id='pg:frm:VehicleBlock:j_id283:j_id290:j_id295']").sendKeys(Keys.TAB);
			webDriver.findElementByXPath(".//input[@id='pg:frm:VehicleBlock:j_id283:LastNameRegion:j_id300']").sendKeys(LastName);
			// Enter first name and last name
			// click on Get Rates button
			stepExecutor.clickButton("findElementByXPath",".//input[@id='pg:frm:GetRatesButton']", webDriver, "DefultCommission");
			webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			//Verify paper icon display only when commission is added to product
			verify.verifyElementIsPresent(webDriver, "//*[@id='pg:frm:QuoteDP:j_id352:0:j_id379']/img", "xpath");
			
			//*[@id="pg:frm:QuoteDP:j_id352:0:j_id372"]/span[1]/a/img
		}
		finally{
			return result;
		}
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
					webDriver.findElementByXPath(".//input[@id='pg:frm:VehicleBlock:DecodeBlock2:NonvinSI:j_id230']").sendKeys(VIN);
				    // Press "TAB" key
					webDriver.findElementByXPath(".//input[@id='pg:frm:VehicleBlock:DecodeBlock2:NonvinSI:j_id230']").sendKeys(Keys.TAB);
				

					stepExecutor.clickButton("findElementByXPath", "//input[@id='pg:frmPage1:VehicleBlock:vinBLK:vinId:j_id188']", webDriver, "AN_GLOW");
					webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
					Thread.sleep(15000);
					
				}else {
					radiobutton.click();
					webDriver.findElementByXPath(".//input[@id='pg:frm:VehicleBlock:DecodeBlock2:NonvinSI:j_id230']").sendKeys(VIN);
				    // Press "TAB" key
					webDriver.findElementByXPath(".//input[@id='pg:frm:VehicleBlock:DecodeBlock2:NonvinSI:j_id230']").sendKeys(Keys.TAB);
				

					stepExecutor.clickButton("findElementByXPath", ".//input[@id='pg:frm:VehicleBlock:vinBLK:vinId:j_id170']", webDriver, "AN_GLOW");
					webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
					Thread.sleep(15000);
				}
			}
		}
		if (VINDecoding.contains("No"))
		{															
			WebElement radiobutton1 = webDriver.findElement(By.id("pg:frm:VehicleBlock:vinNopbs:vinRadio:1"));
			if (webDriver.findElement(By.id("pg:frm:VehicleBlock:vinNopbs:vinRadio:1")).isDisplayed())
			{

				radiobutton1.isSelected();
				if (true)
				{
				// Click "No" on VIN Decoding button
				radiobutton1.click();
				Thread.sleep(10000);
				// Enter 17 characters VIN
				webDriver.findElementByXPath(".//input[@id='pg:frm:VehicleBlock:DecodeBlock2:NonvinSI:j_id230']").sendKeys(VIN);
			    // Press "TAB" key
				webDriver.findElementByXPath(".//input[@id='pg:frm:VehicleBlock:DecodeBlock2:NonvinSI:j_id230']").sendKeys(Keys.TAB);
				Thread.sleep(12000);
				// Select Model year
				stepExecutor.selectListValue("findElementByName",
						".//*[@id='pg:frm:VehicleBlock:DecodeBlock2:j_id232:j_id234']/select",DataMap,
						 "Mod Yr", webDriver, TestCase);
				
				// Select Make of vehicle
				stepExecutor.selectListValue("findElementByXpath",
						".//*[@id='pg:frm:VehicleBlock:DecodeBlock2:j_id241:vMake']",DataMap,
						"Make", webDriver,TestCase);
				webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				Thread.sleep(10000);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='pg:frm:VehicleBlock:DecodeBlock2:modelsecItem:j_id252']")));
				// Select Model of vehicle
				stepExecutor.selectListValueByContainsValue("findElementByXpath",
						"//select[@name='pg:frm:VehicleBlock:DecodeBlock2:modelsecItem:j_id252']",
						 "Vehicle Code", webDriver, TestCase,rownumber);
				
				}
			} 
		}

	}


	private boolean FindDescendantsofCheckbox(String ProductType,String SubProductTypeSelection) throws InterruptedException
	{

		Boolean isFlag = false;
		try
		{
			if (verify.verifyElementIsPresentCheck(webDriver,"//tr[td[contains(text(),'"+ProductType+"')]]/td/input[@type='checkbox']","xpath"))
			{
				webDriver.findElementByXPath("//tr[td[contains(text(),'"+ProductType+"')]]/td/input[@type='checkbox']").click();
				reporter.writeStepResult("Product selected by user", "Select Product",ProductType, "Pass", "", true, webDriver);
				if (webDriver.findElementByXPath("//tr[td[contains(text(),'"+ProductType+"')]]/td/input[@type='checkbox']").isSelected())
				{
					if (ProductType.contains("Service Contract") || ProductType.contains("Mechanical Repair") )
					{
						new Select(webDriver.findElementByXPath("//select[@id='pg:frm:ProductBlock:rpt:1:selectedVal']")).selectByVisibleText(SubProductTypeSelection);
						Thread.sleep(15000);
						isFlag = true;
					}
					if (ProductType.contains("Pre-Paid Maintenance"))
					{
						new Select(webDriver.findElementByXPath("//select[@id='pg:frm:ProductBlock:rpt:0:selectedVal']")).selectByVisibleText(SubProductTypeSelection);
						Thread.sleep(15000);
						isFlag = true;
					}
					if (SubProductTypeSelection == null){
						System.out.println("No sub-options are present for selected product");
						
					}
					isFlag = true;
				}
			}else{
				reporter.writeStepResult("Product not displayed to  user", "Select Product not displayed",ProductType, "fail", "", true, webDriver);
				isFlag = false;
				
			}
	
		}catch (Exception e){
			e.printStackTrace();
		}

		return isFlag;
		
	}
	
	
	public  boolean ViewQuotes(String QuoteId) throws InterruptedException
	{

		try 
		{
			result=false;
			stepExecutor.enterTextValue("findElementById", "phSearchInput",DataMap, "QuoteID", webDriver, "ViewQuote");
			webDriver.findElementById("phSearchInput").sendKeys(Keys.RETURN);
			Thread.sleep(1000);
			if (verify.verifyElementIsPresentCheck(webDriver, "//a[contains(text(),'"+QuoteId+"')]", "xpath"))
			{		
				stepExecutor.clickElement("findElementByXPath", "//a[contains(text(),'"+QuoteId+"')]", webDriver, "ViewQuotes");
			
				verify.verifyElementAbsent(webDriver, ".//*[@id='001d000001T6Kay_00Nd0000007KYFN_link']/span", "xpath");
			}else{
				result = true;
				scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "ViewQuote_FailedReason", "QuoteID cannot be viewed by user who had login to the system ");
		
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally{
			return result;
		}
		
	}
	

	public boolean SetDefaultCommissionWitoutValidatingCustomerCost() throws InterruptedException
	{
		try 
		{

			result=false;
			//Click on Contract search link
			stepExecutor.clickLink("findElementByXPath","//a[contains(text(),'Commissions')]", webDriver, "SetDefaultCommissionWitoutValidatingCustomerCost");
			stepExecutor.clickButton("findElementByXPath","//input[@name='new']", webDriver, "CancelContracts");
			String recordtype = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "SelectCommissionRecordType");
		    new Select(webDriver.findElement(By.id("p3"))).selectByVisibleText(recordtype);
		  
			//Select ContinueButton
			stepExecutor.clickButton("findElementByXPath",".//td[@id='bottomButtonRow']/input[1]", webDriver, "DefaultCommission");
			Thread.sleep(1000);
			
			//Enter Commission name
			stepExecutor.enterTextValue("findElementByXPath", "//input[@id='Name']", DataMap, "CommisionName", webDriver, "DefaultCommission");
			//Select dealer by clicking on lookup icon
			LookupWindow();
			
			//Select dealer product
			stepExecutor.enterTextValue("findElementByXPath", "//input[@id='CF00Nd0000007KY3o']", DataMap, "ProductToSelect", webDriver, "DefaultCommission");	
				
			//Validate customer cost
			stepExecutor.changeCheckboxStatus("findElementById", "00Nd0000007jwSv", DataMap, "ValidateCustomerCostCheckbox", webDriver, "DefaultCommission");
			
			//select active check box
			
			if (webDriver.findElementByXPath(".//input[@id='00Nd0000007KY3g']").isSelected()) 
			{
				System.out.println("Active check box is already selected");
			}else{
				webDriver.findElementByXPath(".//input[@id='00Nd0000007KY3g']").click();
			}
			
			
			if (recordtype.contains("Dealer Commission - Percentage")){
				//Enter Minimum amount percentage
				stepExecutor.enterTextValue("findElementByXPath", "//input[@id='00Nd0000007KY3v']", DataMap, "MinimumAmt", webDriver, "DefaultCommission");
				//Enter maximum amount percentage
				stepExecutor.enterTextValue("findElementByXPath", "//input[@id='00Nd0000007KY3t']", DataMap, "MaximumAmt", webDriver, "DefaultCommission");
				
				//Enter default amount percentage
				stepExecutor.enterTextValue("findElementByXPath", "//input[@id='00Nd0000007KY3q']", DataMap, "DefaultAmt", webDriver, "DefaultCommission");
	
			}
			if (recordtype.contains("Dealer Commission - Amount"))
			{
				//Enter Minimum amount
				stepExecutor.enterTextValue("findElementByXPath", "//input[@id='00Nd0000007KY3u']", DataMap, "MinimumAmt", webDriver, "DefaultCommission");
				//Enter maximum amount
				stepExecutor.enterTextValue("findElementByXPath", "//input[@id='00Nd0000007KY3s']", DataMap, "MaximumAmt", webDriver, "DefaultCommission");
				
				//Enter default amount
				stepExecutor.enterTextValue("findElementByXPath", "//input[@id='00Nd0000007KY3p']", DataMap, "DefaultAmt", webDriver, "DefaultCommission");
	
			}
	
			//Click on Save and new button
			stepExecutor.clickButton("findElementByXPath", ".//td[@id='bottomButtonRow']/input[1]", webDriver, "DefaultCommission");
			
			if (verify.verifyElementIsPresentCheck(webDriver, "errorDiv_ep", "id"))
			{
				String errormsg = webDriver.findElement(By.id("errorDiv_ep")).getText();
				scriptExecutor.WriteExcelDataFile(strDataFileName, TestCase, rownumber, "Set Default Commission_FailedReason", errormsg);
				result = true;
			} else
			{
				Thread.sleep(2000);
				//Call create quote scenario
				CreateQuote();
				String Maximumamount = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "MaximumAmt");
				
				if (Maximumamount.contains(".0")){
					Maximumamount = Maximumamount.replace(".0", "");
					
				}
				
				String Minamount = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "MinimumAmt");
				if (Minamount .contains(".0")){
					Minamount  = Minamount .replace(".0", "");
					
				}
				String Dealercost = verify.verifyandstoreElementTextPresent(webDriver, ".//span[@id='pg:frm:QuoteDP:j_id352:0:j_id367']","xpath");
				Dealercost = Dealercost.replace("$","");
				String expectedstr=null;
				int value=0;
				if (recordtype.contains("Dealer Commission - Amount"))
				{
					value = Integer.valueOf(Dealercost) +Integer.valueOf(Maximumamount);
					System.out.println(String.valueOf(value));
					expectedstr = "Customer cost cannot be greater than $"+String.valueOf(value);
					//Enter Customer cost
					webDriver.findElementByXPath(".//input[@id='pg:frm:QuoteDP:j_id352:0:xyz']").clear();
					webDriver.findElementByXPath(".//input[@id='pg:frm:QuoteDP:j_id352:0:xyz']").sendKeys(String.valueOf(value));
					stepExecutor.enterTextValue("findElementByXPath", ".//input[@id='pg:frm:QuoteDP:j_id352:0:xyz']", DataMap, "CustomerCost", webDriver, "SetDefaultCommission");
				
				}
				if (recordtype.contains("Dealer Commission - Percentage"))
				 {			 
					    int maxvalue =  ((Integer.valueOf(Maximumamount))*(Integer.valueOf(Dealercost)))/100;
					    maxvalue = maxvalue + Integer.valueOf(Dealercost);
					    maxvalue =  Integer.valueOf(Dealercost) +maxvalue;
			 			System.out.println(String.valueOf(maxvalue));
			 			expectedstr = "Customer cost cannot be greater than $"+String.valueOf(maxvalue);
			 			//Enter Customer cost
						//stepExecutor.enterTextValue("findElementByXPath", ".//input[@id='pg:frm:QuoteDP:j_id352:0:xyz']", DataMap, "CustomerCost", webDriver, "SetDefaultCommission");
					
			 			webDriver.findElementByXPath(".//input[@id='pg:frm:QuoteDP:j_id352:0:xyz']").clear();
						webDriver.findElementByXPath(".//input[@id='pg:frm:QuoteDP:j_id352:0:xyz']").sendKeys(String.valueOf(maxvalue));
				 }
				String ccostcheckbox= scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "ValidateCustomerCostCheckbox");
				
				//Click on Continue button
				stepExecutor.clickButton("findElementByXPath",".//input[@id='pg:frm:savebtn']", webDriver, "DefultCommission");
				if (ccostcheckbox.equals("check"))
				{
					String error = webDriver.findElement(By.id("pg:frm:test1:j_id39:j_id40:0:j_id41:j_id42:j_id44")).getText();
					error = error.replace("Error:", "");
					System.out.println(error);
					
					if (error.contains(expectedstr)) {
						reporter.writeStepResult(
								System.getProperty("Default commission validate customer cost"),
								"Verify customer cost cannot be greater than dealer cost", "Expected: "
										+ expectedstr, "Pass", "Expected text  is present",
								true, webDriver);
					} else {
						reporter.writeStepResult(
								System.getProperty("Default commission validate customer cost"),
								"Verify customer cost cannot be greater than dealer cost", "Expected: "
										+ expectedstr, "Fail", "Expected text  is not present"+error,
								true, webDriver);
			
					}
					
					 //Verify customer cost is less than  minimum amount of commission
					 if (recordtype.contains("Dealer Commission - Amount"))
					 {			 
						 	int minvalue =  (Integer.valueOf(Minamount)-(Integer.valueOf(Dealercost)));  
						 	System.out.println(String.valueOf(minvalue));
				 			webDriver.findElementByXPath(".//input[@id='pg:frm:QuoteDP:j_id352:0:xyz']").clear();
							webDriver.findElementByXPath(".//input[@id='pg:frm:QuoteDP:j_id352:0:xyz']").sendKeys(String.valueOf(minvalue));
					 }
					 if (recordtype.contains("Dealer Commission - Percentage"))
					 {			 
						    int minvalue =  ((Integer.valueOf(Minamount))*(Integer.valueOf(Dealercost)))/100;
						    minvalue =  Integer.valueOf(Dealercost)-minvalue; 
				 			System.out.println(String.valueOf(minvalue));
				 			webDriver.findElementByXPath(".//input[@id='pg:frm:QuoteDP:j_id352:0:xyz']").clear();
							webDriver.findElementByXPath(".//input[@id='pg:frm:QuoteDP:j_id352:0:xyz']").sendKeys(String.valueOf(minvalue));
					 }
			
					//Click on Continue button
					stepExecutor.clickButton("findElementByXPath",".//input[@id='pg:frm:savebtn']", webDriver, "DefaultCommission");
					Thread.sleep(5000);
					expectedstr = "Customer cost cannot be less than $"+Dealercost;	
					error = webDriver.findElement(By.id("pg:frm:test1:j_id39:j_id40:0:j_id41:j_id42:j_id44")).getText();
					error = error.replace("Error:", "");
					System.out.println(error);
					 if (error.contains(expectedstr)) {
							reporter.writeStepResult(
									System.getProperty("Default commission validate customer cost"),
									"Verify customer cost cannot be less than "+Dealercost, "Expected: "
											+ expectedstr, "Pass", "Expected text  is present",
									true, webDriver);
						} else {
							reporter.writeStepResult(
									System.getProperty("Default commission validate customer cost"),
									"Verify customer cost cannot be less than $"+Dealercost, "Expected: "
											+ expectedstr, "Fail", "Expected text  is not present"+error,
									true, webDriver);
			
						}
					 
				}
				if (ccostcheckbox.equals("uncheck"))
				{
					webDriver.navigate().back();
					Thread.sleep(3000);
					CreateQuote();
					if (recordtype.contains("Dealer Commission - Amount"))
					{
						value = Integer.valueOf(Dealercost) +Integer.valueOf(Maximumamount) ;
						System.out.println(String.valueOf(value));
						webDriver.findElementByXPath(".//input[@id='pg:frm:QuoteDP:j_id352:0:xyz']").clear();
						webDriver.findElementByXPath(".//input[@id='pg:frm:QuoteDP:j_id352:0:xyz']").sendKeys(String.valueOf(value));
					
					}
					if (recordtype.contains("Dealer Commission - Percentage"))
					 {			 
						    int maxvalue =  ((Integer.valueOf(Maximumamount))*(Integer.valueOf(Dealercost)))/100;
						    maxvalue = maxvalue + Integer.valueOf(Dealercost);
						    maxvalue =  Integer.valueOf(Dealercost) + maxvalue;
				 			System.out.println(String.valueOf(maxvalue));
	
				 			webDriver.findElementByXPath(".//input[@id='pg:frm:QuoteDP:j_id352:0:xyz']").clear();
							webDriver.findElementByXPath(".//input[@id='pg:frm:QuoteDP:j_id352:0:xyz']").sendKeys(String.valueOf(maxvalue));
					 }
					
					//Click on Continue button
					stepExecutor.clickButton("findElementByXPath",".//input[@id='pg:frm:savebtn']", webDriver, "DefultCommission");
					
					webDriver.navigate().back();
					Thread.sleep(3000);
				}
			}
			
		} catch(Exception e){
			System.out.println(e.getMessage());
		}
		finally{
			return result;
		}

	}
	
	public void LookupWindow() throws InterruptedException
	{
		String dealeraccountnumber = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Client");
		String Parent_Window_Handle = webDriver.getWindowHandle();
		WebElement element =  webDriver.findElement(By.xpath("//a[@id='CF00Nd0000007KY3n_lkwgt']/img"));
		if (webDriver.findElement(By.xpath("//a[@id='CF00Nd0000007KY3n_lkwgt']/img")).isDisplayed())
		{
			if("a".equals(element.getTagName())){
				   element.sendKeys(Keys.TAB);
				} 
				else{
				   new Actions(webDriver).moveToElement(element).build().perform();
				   element.click();

				}
			//stepExecutor.clickButton("findElementByXPath","//a[@id='CF00Nd0000007KY3n_lkwgt']/img", webDriver, "DefaultCommission");
		}
		
		Thread.sleep(8000);
		String Child_Window_Handle = null;
		String Child_window_title;
		Set<String> s = webDriver.getWindowHandles();
		System.out.println(s.size());
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
				System.out.println("Inside  the child WH");
			}
		}
			webDriver.switchTo().window(Child_Window_Handle);
			String handle = webDriver.getWindowHandle();
			Child_window_title = webDriver.getTitle();
			if (webDriver.getTitle().contains("Search"))
	        
				webDriver.switchTo().frame("searchFrame");
				stepExecutor.enterTextValue("findElementByXPath", ".//input[@id='lksrch']", DataMap, "Dealer Name", webDriver, "DealerLookUp");
				webDriver.findElementByXPath(".//input[@id='lksrch']").sendKeys(Keys.RETURN);
				Thread.sleep(2000);
				webDriver.switchTo().window(Child_Window_Handle);
				webDriver.switchTo().frame("resultsFrame");
	        	verify.verifyElementPresent(webDriver, "//tr[td[contains(text(),'"+dealeraccountnumber+"')]]", "xpath");
	        	List<WebElement> tablerow = webDriver.findElementsByXPath("//tr[td[contains(text(),'"+dealeraccountnumber+"')]]");
	        	List<String> colValues = new ArrayList<String>();
	        	
	        	Iterator<WebElement> i = tablerow.iterator();
	            while (i.hasNext()) {
	                 WebElement row = i.next();
	                 System.out.println(row.getText());
	                int colIndex=1;
					WebElement colElement;
					if (row.findElements(By.tagName("th")).size() > 0) {
	                     colElement = row.findElement(By.xpath(".//th[" + colIndex + "]"));
	                     
	                 } else {
	                     colElement = row.findElement(By.xpath(".//td[" + colIndex + "]"));
	                 }
	                 colValues.add(colElement.getText().trim());
	                 System.out.println(colElement.getText());
	              //   new Actions(webDriver).moveToElement(colElement).build().perform();
	                 colElement.click();
	             	 Thread.sleep(10000);
	             }
			
	            webDriver.switchTo().window(Parent_Window_Handle);
	        }
	}

