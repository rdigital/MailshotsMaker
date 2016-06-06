package com.capgemini.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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

public class Lists {
	
	public String TestCase="Lists";
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
	String strDataFileName = utils.getDataFile("Lists");
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
			
			stepExecutor.clickButton("findElementById", "cookie_submit", webDriver,"Lists");
			
			//Click on Login 
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Login')]", webDriver,"Lists");
			
			Thread.sleep(5000);
			// Enter user name
			stepExecutor.enterTextValue("findElementById", "Email", DataMap,"username", webDriver, "Lists");
			//Enter Password
			stepExecutor.enterTextValue("findElementById", "Password", DataMap,"password", webDriver, "Lists");
			
			//Click on Login button
			stepExecutor.clickButton("findElementByXPath", ".//*[@id='login']/div/form/div[4]/button", webDriver,"Lists");
			//stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'login')]", webDriver,"Lists");
			
			System.out.println("Log on sucessfully to application");
			
			Thread.sleep(5000);
			stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Lists')]", webDriver,"Lists");
			Thread.sleep(5000);
			
			//ArrayList<WebElement> CustList = (ArrayList<WebElement>)webDriver.findElements(By.cssSelector("lists__my-lists--lists lists__my-lists__list"));
			//System.out.println(CustList.size());
			
			
			//List<WebElement> CustList = webDriver.findElements(By.xpath(".//*[@class='lists__my-lists']//li"));
			List<WebElement> CustList = webDriver.findElements(By.xpath(".//*[@class='lists__my-lists']//li"));
			//List<WebElement> CustList = webDriver.findElements(By.cssSelector("lists__my-lists--lists lists__my-lists__list"));
			System.out.println(CustList.size());
			
			if (CustList.size() >0)
			{
				stepExecutor.clickByCss(".lists__my-lists--lists lists__my-lists__list>li:nth-child(1)>div>span", webDriver);
			} else 
			{
				
				//Create a new list address  button
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
				
			}
					 
	/*	for (WebElement element : CustList) {
			System.out.println("ABCD");
			
		  
		}*/
			
		/*	for (int i=0; i<10; i++) {
				webDriver.findElement(By.xpath(".//*[@class='lists__rent-list']/ul/li["+i+"]"));
				System.out.println(i);
		    }*/
			
		}catch (Exception e) {
			e.printStackTrace();
		}
				
				
	}
	
	/*public void scrollwindow ()
	{
	
	        JavascriptExecutor jse = (JavascriptExecutor)webDriver;
			jse.executeScript("scroll(0, 250)"); // if the element is on bottom.
			
		
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
