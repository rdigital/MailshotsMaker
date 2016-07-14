package com.capgemini.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
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

public class Images {
	
	public String TestCase="Images";
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
	String strDataFileName = utils.getDataFile("Images");
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
		
		//uncomments this while want to run individual 
		//System.exit(0);
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
			
			stepExecutor.clickButton("findElementById", "cookie_submit", webDriver,"Images");
			
			//Click on Login 
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Login')]", webDriver,"Images");
			
			Thread.sleep(5000);
			// Enter user name
			stepExecutor.enterTextValue("findElementById", "Email", DataMap,"username", webDriver, "Images");
			//Enter Password
			stepExecutor.enterTextValue("findElementById", "Password", DataMap,"password", webDriver, "Images");
			
			//Click on Login button
			stepExecutor.clickButton("findElementByXPath", ".//*[@id='login']/div/form/div[4]/button", webDriver,"Images");
			//stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'login')]", webDriver,"Images");
			
			System.out.println("Log on sucessfully to application");
			
			Thread.sleep(5000);
			stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Images')]", webDriver,"Images");
			Thread.sleep(5000);
			
		//	stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/campaign-list-component/div/div/span", webDriver,"Images");
			stepExecutor.clickButton("findElementByXPath", ".//span[contains(text(),'Upload a new image')]", webDriver,"Images");
			 
			Thread.sleep(3000);
			
			 String winHandleBefore = webDriver.getWindowHandle();

		        for(String winHandle : webDriver.getWindowHandles()){
		        	webDriver.switchTo().window(winHandle);
		        	        	
		        }
		        
		        String FilePath = scriptExecutor.readDataFile(strDataFileName, "Images", rownumber, "Path");
		        webDriver.findElement(By.id("image-upload")).sendKeys(FilePath);
		        
		       // stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div/div/my-images-component/upload-image-component/div[2]/div[2]", webDriver,"Images");
		        	  	  
				  
				  webDriver.switchTo().window(winHandleBefore);
				  stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Use this image')]", webDriver,"Images");
				  Thread.sleep(6000);
				  System.out.println("Image Uploaded sucessfully");
		  
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
