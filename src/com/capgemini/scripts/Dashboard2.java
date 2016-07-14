package com.capgemini.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import jxl.read.biff.BiffException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

public class Dashboard2 {
	
	public String TestCase="Dashboard";
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
	int rownumber = 0;
	private static String strAbsolutepath = new File("").getAbsolutePath();
	private static String strDataPath = strAbsolutepath + "/data/";
	String strDataFileName = utils.getDataFile("Dashboard");
	private boolean acceptNextAlert = true;
	String strStopTime;
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	public String getExecutionStartTime() {
		return StrExecutionStartTime;

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
		//	CreateOutputfile();
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
		System.exit(0);
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
		System.exit(2);
	}
	
	
	public void testcaseMain() throws InterruptedException, BiffException,
	Exception {
		

		stepExecutor.launchApplication("URL", DataMap, webDriver);
		
		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		try {
			
			stepExecutor.clickButton("findElementById", "cookie_submit", webDriver,"Dashboard");
			
			//Click on Login 
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Login')]", webDriver,"Dashboard");
			
			Thread.sleep(5000);
			// Enter user name
			stepExecutor.enterTextValue("findElementById", "Email", DataMap,"username", webDriver, "Dashboard");
			//Enter Password
			stepExecutor.enterTextValue("findElementById", "Password", DataMap,"password", webDriver, "Dashboard");
			
			//Click on Login button
			stepExecutor.clickButton("findElementByXPath", ".//*[@id='login']/div/form/div[4]/button", webDriver,"Dashboard");
			//stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'login')]", webDriver,"Dashboard");
			
			System.out.println("Log on sucessfully to application");
			
			Thread.sleep(5000);
			stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Dashboard')]", webDriver,"Dashboard");
			Thread.sleep(5000);
			
		//	stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/campaign-list-component/div/div/span", webDriver,"Dashboard");
			stepExecutor.clickButton("findElementByXPath", ".//span[contains(text(),'Start new mailshot')]", webDriver,"Dashboard");
			 
			Thread.sleep(9000);
			
			stepExecutor.enterTextValue("findElementById", "campaignname", DataMap,"Untitled_mailshot", webDriver, "Dashboard");
			
			//click on Ok button
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'OK')]", webDriver,"Dashboard");
			Thread.sleep(5000);
			
			JavascriptExecutor jse = (JavascriptExecutor)webDriver;
			jse.executeScript("scroll(0, 250)"); // if the element is on bottom.
			
			//click on Create a design
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Create a design')]", webDriver,"Dashboard");
			System.out.println("click on  Create a design button sucessfully");
			Thread.sleep(4000);
			
			JavascriptExecutor jse0 = (JavascriptExecutor)webDriver;
			jse0.executeScript("scroll(0, 250)"); // if the element is on bottom.
			
			//stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Choose Postcard')]", webDriver,"Dashboard");
			// click on Choose postcard
			stepExecutor.clickButton("findElementByXPath", ".//*[@id='format_chooser']/div/div[1]/article[1]/a", webDriver,"Dashboard");
						
			// switch to bars
			JavascriptExecutor jse1 = (JavascriptExecutor)webDriver;
			jse1.executeScript("scroll(0, 250)"); // if the element is on bottom.
			
			// click on Quick start button
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Quick start')]", webDriver,"Dashboard");
			Thread.sleep(8000);
			
			System.out.println("click on Quick start button sucessfully");
			
			// click on done 
			//stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Done')]", webDriver,"Dashboard");
			
			stepExecutor.clickButton("findElementByXPath", "html/body/editor-component/options-component/div/span[2]/span", webDriver,"Dashboard");
			Thread.sleep(8000);
			
			JavascriptExecutor jse3 = (JavascriptExecutor)webDriver;
			jse3.executeScript("scroll(0, 200)"); // if the element is on bottom.
			
			
			//click on Add address details button
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Add address details')]", webDriver,"Dashboard");
			
			//stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div/div[1]/div[2]/div[1]/div[2]/div/div/form/button", webDriver,"Dashboard");
			
			Thread.sleep(3000);
			JavascriptExecutor jse4 = (JavascriptExecutor)webDriver;
			jse4.executeScript("scroll(0, 270)"); // if the element is on bottom.
			
			//stepExecutor.clickElement("findElementByXPath", "html/body/div[2]/div[2]/lists-component/div/div[2]/my-lists-component/section/ul/li[1]/div/span", webDriver, "TWG");
			
			//stepExecutor.clickElement("findElementByXPath", ".//input[@type='checkbox'])[position()=1]", webDriver, "TWG");
			stepExecutor.clickByCss(".lists__my-lists--lists.lists__my-lists__list>li:nth-child(1)>div>span", webDriver);
			
		
			/*System.out.println("check box checked");
			
			JavascriptExecutor jse9 = (JavascriptExecutor)webDriver;
			jse9.executeScript("scroll(0, 270)"); // if the element is on bottom.*/
			
			//click on Add to mailshot
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Add to mailshot')]", webDriver,"Dashboard");
			
			System.out.println("Add to mailshot");
			stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div[2]/lists-component/div/batch-tray-component/div/div/div/div[2]/button", webDriver,"Dashboard");
			
			System.out.println("Add to mailshot1");
			Thread.sleep(6000);
			
			JavascriptExecutor jse8 = (JavascriptExecutor)webDriver;
			jse8.executeScript("scroll(0, 270)"); // if the element is on bottom.
			
			System.out.println("moved on bottom");
			
           stepExecutor.clickByCss("#AgreesToTermsAndConditions", webDriver);
			
			System.out.println("Agrees To Terms And Conditions");	
			
			//stepExecutor.clickButton("findElementByXPath", ".//button[contains(text(),'Checkout')]", webDriver,"Dashboard");
			
			//System.out.println("Click on Checkout button");	
			stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div/div[1]/div[2]/div[4]/div/div/form/div/button", webDriver,"Dashboard");
			System.out.println("Click on Checkout button1");
			
			Thread.sleep(6000);
						
			//stepExecutor.launchApplication("Paypal_URL", DataMap, webDriver);
			
			//WebElement selectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
			webDriver.switchTo().frame(webDriver.findElement(By.cssSelector("#injectedUnifiedLogin>iframe")));
			stepExecutor.enterTextValue("findElementById", "email", DataMap,"Paypal_username", webDriver, "Dashboard");
			System.out.println("pay pal email");
			stepExecutor.enterTextValue("findElementByXPath", ".//*[@id='password']", DataMap,"Paypal_Password", webDriver, "Dashboard");
			System.out.println("pay pal password");
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Log In')]", webDriver,"Dashboard");
			
			System.out.println("click on pay pal button");
			
			webDriver.switchTo().defaultContent();
			Thread.sleep(6000);
			//stepExecutor.clickByCss("#confirmButtonTop", webDriver);
			stepExecutor.clickButton("findElementByXPath", "html/body/div[2]/div/div/div/div/div/div/div/div/div/div/section/div[1]/div[1]/form/div[4]/input", webDriver, "Dashboard");
			System.out.println("Transaction done sucessfully");
			
			Thread.sleep(5000);
			
			stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Go to my dashboard')]", webDriver,"Dashboard");
			
			System.out.println("Go to my dashboard");
			//stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/header/nav/div/a[2]", webDriver,"Dashboard");
			
			//stepExecutor.clickButton("findElementById", "confirmButtonTop", webDriver,"Dashboard");
			
		/*	//Create a new list address  button
			//stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Create a new list')]", webDriver,"Dashboard");
			stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div[2]/lists-component/div/div[2]/my-lists-component/section/div/a", webDriver,"Dashboard");
			
			System.out.println("new list address  button");
			
			//Enter Address Title
			stepExecutor.enterTextValue("findElementById", "listname", DataMap,"NewAddress_Title", webDriver, "Dashboard");
					
			System.out.println("Enter Address Title ");
			//click on ok button
			stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/create-list-component/div[1]/div[1]/div/div/form/span/button", webDriver,"Dashboard");
			
			JavascriptExecutor jse5 = (JavascriptExecutor)webDriver;
			jse5.executeScript("scroll(0, 270)"); // if the element is on bottom.
			
			//stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Add contact details individually')]", webDriver,"Dashboard");
			
			stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/create-list-component/div[1]/div[2]/upload-data-component/div/div/div[1]/a", webDriver,"Dashboard");
			System.out.println("clicked on Add contact details individually");

	        String winHandleBefore = webDriver.getWindowHandle();

	        for(String winHandle : webDriver.getWindowHandles()){
	        	webDriver.switchTo().window(winHandle);
	        	        	
	        }
	        	        
			//Entering Value in Title
	        stepExecutor.enterTextValue("findElementById", "editTitle", DataMap,"EditTitle", webDriver, "Dashboard");
	       // Entering Value in First name
	        stepExecutor.enterTextValue("findElementById", "editFirstName", DataMap,"EditFirstName", webDriver, "Dashboard");
	        //Entering Value in Last name
	        stepExecutor.enterTextValue("findElementById", "editSurname", DataMap,"EditSurname", webDriver, "Dashboard");
	         //Entering Value in Flat id
	        stepExecutor.enterTextValue("findElementById", "editFlatId", DataMap,"EditFlatId", webDriver, "Dashboard");
	       // Entering Value in House name
	        stepExecutor.enterTextValue("findElementById", "editHouseName", DataMap,"EditHouseName", webDriver, "Dashboard");
	        //Entering Value in House No
	        stepExecutor.enterTextValue("findElementById", "editHouseNumber", DataMap,"EditHouseNumber", webDriver, "Dashboard");
	        //Entering Value in Address one
	        stepExecutor.enterTextValue("findElementById", "editAddress1", DataMap,"EditAddress1", webDriver, "Dashboard");
		       // Entering Value in Post Code
		    stepExecutor.enterTextValue("findElementById", "editPostCode", DataMap,"EditPostCode", webDriver, "Dashboard");
		     
		   // stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Save and close')]", webDriver,"Dashboard");
		    
		    stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/create-list-component/add-contact-component/div/div[1]/div[12]/button[3]", webDriver,"Dashboard");
		    
		    		  
		  System.out.println("RT993");
		  webDriver.switchTo().window(winHandleBefore);
		  
		  stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Back to Add data page')]", webDriver,"Dashboard");
		  System.out.println("RT998");
		  stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Find new customers')]", webDriver,"Dashboard");
	      	  		
			System.out.println("Test case run sucessfully ");*/
		  
		} catch (Exception e) {
			e.printStackTrace();
		}
				
				
	}
	
	/*public void scrollwindow ()
	{
		
	}*/
	
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
	

	public String GetTestStartTime()
	{
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.println( sdf.format(cal.getTime()) );
    	return (sdf.format(cal.getTime()));
    	
	}
	
}
