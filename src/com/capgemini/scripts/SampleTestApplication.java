package com.capgemini.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
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
import org.openqa.selenium.support.ui.WebDriverWait;

import com.capgemini.driver.CreateDriver;
import com.capgemini.driver.ScriptExecutor;
import com.capgemini.driver.StepExecutor;
import com.capgemini.executor.ExecutionRowNumber;
import com.capgemini.executor.WriteMaster;
import com.capgemini.utilities.ReadExcel;
import com.capgemini.utilities.Reporter;
import com.capgemini.utilities.Utilities;
import com.capgemini.utilities.Verification;

public class SampleTestApplication {
	
	public String TestCase="SampleTestApplication";
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
	String strDataFileName = utils.getDataFile("SampleTestApplication");
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
	
		
	public void testcaseMain() throws InterruptedException, BiffException,
	Exception {
		

		stepExecutor.launchApplication("URL", DataMap, webDriver);
		System.out.println("Application URL launch sucessfully");
		
		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		try {
			
			
			String ExpectedTitle_F1 = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "Title_1");
			
			String actualTitle_F1 = webDriver.findElementByXPath(".//h5[text()='Find Web Hosting']").getText();
			
            String ExpectedTitle_F2 = scriptExecutor.readDataFile(strDataFileName,TestCase,rownumber, "Title_2");
			
			String actualTitle_F2 = webDriver.findElementByXPath("//h5[text()='Getting Started']").getText();
			//h5[text()='Find Web Hosting']
			//h5[text()='Getting Started']
						
			System.out.println(actualTitle_F1);
			
			WriteExcelDataFile(strDataFileName, rownumber, "Actual_Title1", actualTitle_F1);
			if (ExpectedTitle_F1.equals(actualTitle_F1)) {
				reporter.writeStepResult("MainTitle",
						"Verify Title is present in the element", "Expected: "
								+ ExpectedTitle_F1, "Pass", "Expected text is present",
						true, webDriver);
				WriteExcelDataFile(strDataFileName, rownumber, "Result_Title1", "PASS");
			} else {
				reporter.writeStepResult("MainTitle", 
						"Verify Title is present in the element", "Expected: "
								+ ExpectedTitle_F1, "Fail",
						"Expected text  is not present (Actual: "
								+ actualTitle_F1 + ")", true, webDriver);
				WriteExcelDataFile(strDataFileName, rownumber, "Result_Title1", "FAIL");
			}
			
			
			WriteExcelDataFile(strDataFileName, rownumber, "Actual_Title2", actualTitle_F2);
			if (ExpectedTitle_F2.equals(actualTitle_F2)) {
				reporter.writeStepResult("MainTitle",
						"Verify Title is present in the element", "Expected: "
								+ ExpectedTitle_F2, "Pass", "Expected text is present",
						true, webDriver);
				WriteExcelDataFile(strDataFileName, rownumber, "Result_Title2", "PASS");
			} else {
				reporter.writeStepResult("MainTitle", 
						"Verify Title is present in the element", "Expected: "
								+ ExpectedTitle_F2, "Fail",
						"Expected text  is not present (Actual: "
								+ actualTitle_F2 + ")", true, webDriver);
				WriteExcelDataFile(strDataFileName, rownumber, "Result_Title2", "FAIL");
			}
			
			
			System.out.println("Application run sucessfully");
			
							  
		} catch (Exception e) {
			e.printStackTrace();
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
	
	public String GetTestStartTime()
	{
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.println( sdf.format(cal.getTime()) );
    	return (sdf.format(cal.getTime()));
    	
	}

	
}
