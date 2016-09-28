package com.capgemini.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;

import jxl.read.biff.BiffException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.capgemini.driver.CreateDriver;
import com.capgemini.driver.ScriptExecutor;
import com.capgemini.driver.StepExecutor;
import com.capgemini.executor.ExecutionRowNumber;
import com.capgemini.executor.WriteMaster;
import com.capgemini.utilities.ReadExcel;
import com.capgemini.utilities.Reporter;
import com.capgemini.utilities.Utilities;
import com.capgemini.utilities.Verification;

public class MSM {
	
	public String TestCase="MSM";
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
	private Dashboard dashboard = new Dashboard ();
	private String StrExecutionStartTime = null;
	private long executionStartTime = 0;
	Map<String, String> DataMap = new HashMap();
	Boolean sExecutionStatus;
	ReadExcel readExcel = new ReadExcel(reporter);
	int rownumber = 0;
	private static String strAbsolutepath = new File("").getAbsolutePath();
	private static String strDataPath = strAbsolutepath + "/data/";
	String strDataFileName = utils.getDataFile("MSM");
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
			
			stepExecutor.clickButton("findElementById", "cookie_submit", webDriver,"MSM");
			
			LoginDetails();
			
			/*WebElement hometitle = webDriver.findElement(By.cssSelector(".logo"));
			String home_title =hometitle.getText();
			System.out.println(home_title);
			
			Assert.assertEquals("MailshotMaker", home_title);*/
			
			
			//System.out.println("User Name is  :" +UserName );
			
			// trying to use wait conditions 
			
			//waituntilconditions(60, ".//a[contains(text(),'Mailshots')]");
			
						
			Thread.sleep(6000);			
		/*	WebDriverWait wait1 = new WebDriverWait(webDriver, 60); 
			wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//a[contains(text(),'Mailshots')]")));*/
			
						
			stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Mailshots')]", webDriver,"MSM");
			
			//Thread.sleep(5000);
			
				
			waituntilconditions(60, "html/body/div[1]/campaign-list-component/div/div/span");
			
			stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/campaign-list-component/div/div/span", webDriver,"MSM");
			//stepExecutor.clickButton("findElementByXPath", ".//span[contains(text(),'New Mailshot']", webDriver, "MSM");
            Thread.sleep(9000);
			stepExecutor.enterTextValue("findElementById", "campaignname", DataMap,"Untitled_mailshot", webDriver, "MSM");
			
			//click on Ok button
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'OK')]", webDriver,"MSM");
			
			Thread.sleep(5000);
			
			scrollwindow (0, 250);
						
			//click on Create a design
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Create a design')]", webDriver,"MSM");
			System.out.println("click on  Create a design button sucessfully");
			Thread.sleep(4000);
			
            
			// click on Choose postcard
			stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Choose Postcard')]", webDriver,"MSM");
			//stepExecutor.clickButton("findElementByXPath", ".//*[@id='format_chooser']/div/div[1]/article[1]/a", webDriver,"MSM");
						
			// switch to bars
			
			scrollwindow (0, 250);
	
			// click on Quick start button
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Quick start')]", webDriver,"MSM");
			
			Thread.sleep(8000);
		/*	waituntilconditions(60, "html/body/editor-component/options-component/div/span[2]/span");
			System.out.println("click on Quick start button sucessfully");*/
			
			// click on done 
			//stepExecutor.clickButton("findElementByXPath", ".//span[contains(text(),'Done')]", webDriver,"MSM");
			
			stepExecutor.clickButton("findElementByXPath", "html/body/editor-component/options-component/div/span[2]/span", webDriver,"MSM");
			Thread.sleep(8000);
			
			scrollwindow (0, 200);
					
			//click on Add address details button
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Add address details')]", webDriver,"MSM");
			
			//stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div/div[1]/div[2]/div[1]/div[2]/div/div/form/button", webDriver,"MSM");
			
		    Thread.sleep(3000);
			
			
			scrollwindow (0, 270);
		
			if (verify.verifyElementIsPresentCheck(webDriver, ".//a[contains(text(),'Create a new list')]", "xpath")){
			List<WebElement> CustList = webDriver.findElements(By.xpath(".//*[@class='lists__my-lists']//li"));
			//List<WebElement> CustList = webDriver.findElements(By.cssSelector("lists__my-lists--lists lists__my-lists__list"));
			//System.out.println(CustList.size());
					
			if (CustList.size() >0)
				
			{
				stepExecutor.clickByCss(".lists__my-lists--lists.lists__my-lists__list>li:nth-child(1)>div>span", webDriver);
				
				Thread.sleep(2000);
								
				stepExecutor.clickButton("findElementByXPath", ".//button[contains(text(),'Add to mailshot')]", webDriver,"MSM");
				
				System.out.println("Add to mailshot");
				//stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div[2]/lists-component/div/batch-tray-component/div/div/div/div[2]/button", webDriver,"MSM");
				
				
				Thread.sleep(6000);
				
				scrollwindow (0, 270);
								
								
	            stepExecutor.clickByCss("#AgreesToTermsAndConditions", webDriver);
				
				System.out.println("Agrees To Terms And Conditions");	
				
				//stepExecutor.clickButton("findElementByXPath", ".//button[contains(text(),'Checkout')]", webDriver,"MSM");
				
				//System.out.println("Click on Checkout button");	
				stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div/div[1]/div[2]/div[4]/div/div/form/div/button", webDriver,"MSM");
												
				Thread.sleep(6000);
					
				PaypalPaymentDetails ();
				
				//WebElement selectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
		/*		webDriver.switchTo().frame(webDriver.findElement(By.cssSelector("#injectedUnifiedLogin>iframe")));
				
				stepExecutor.enterTextValue("findElementById", "email", DataMap,"Paypal_username", webDriver, "MSM");
				System.out.println("pay pal email");
				
				stepExecutor.enterTextValue("findElementByXPath", ".//*[@id='password']", DataMap,"Paypal_Password", webDriver, "MSM");
				System.out.println("pay pal password");
				
				stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Log In')]", webDriver,"MSM");
				
				System.out.println("click on pay pal button");*/
				
				webDriver.switchTo().defaultContent();
				
				Thread.sleep(6000);
				//stepExecutor.clickByCss("#confirmButtonTop", webDriver);
				
				//stepExecutor.clickButton("findElementById", ".//*[@id='Pay Now']", webDriver, "MSM");
				
				stepExecutor.clickButton("findElementByXPath", "html/body/div[2]/div/div/div/div/div/div/div/div/div/div/section/div[1]/div[1]/form/div[4]/input", webDriver, "MSM");
				
				System.out.println("Transaction done sucessfully");
				
				Thread.sleep(6000);
							
				LogoutAplication();
				LoginDetails();
				// Image Functionality 
			    Images();
			     
				 Thread.sleep(3000);
				 LogoutAplication();
				 LoginDetails();
				 
				 //Lists Functionality 
					  
				 Lists();
				 
				 Thread.sleep(3000);
				 LogoutAplication();
				 LoginDetails();
				 DashBoard();
					 
				 Thread.sleep(3000);
				 
				 System.out.println("The Process completed sucessfully ");
				 
				 
				//stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Go to my dashboard')]", webDriver,"Dashboard");
										
			} 
			}else {
				//#ToDO
				
				stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Add customer details')]", webDriver,"MSM");
				System.out.println("Add customer details");
				//stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div[2]/lists-component/div/div[2]/my-lists-component/section/div/div/a", webDriver,"MSM");
				
		
				//Enter Address Title
				stepExecutor.enterTextValue("findElementById", "listname", DataMap,"NewAddress_Title", webDriver, "MSM");
						
				System.out.println("Enter Address Title ");
				//click on ok button
				stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/create-list-component/div[1]/div[1]/div/div/form/span/button", webDriver,"MSM");
				
				scrollwindow (0, 270);
				
				
				stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Add contact details individually')]", webDriver,"MSM");
				System.out.println("clicked on Add contact details individually via contains");
				
				stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/create-list-component/div[1]/div[2]/upload-data-component/div/div/div[1]/a", webDriver,"MSM");
				
				System.out.println("clicked on Add contact details individually");

		        String winHandleBefore = webDriver.getWindowHandle();

		        for(String winHandle : webDriver.getWindowHandles()){
		        	webDriver.switchTo().window(winHandle);
		        	        	
		        }
		        	        
				//Entering Value in Title
		        stepExecutor.enterTextValue("findElementById", "editTitle", DataMap,"EditTitle", webDriver, "MSM");
		       // Entering Value in First name
		        stepExecutor.enterTextValue("findElementById", "editFirstName", DataMap,"EditFirstName", webDriver, "MSM");
		        //Entering Value in Last name
		        stepExecutor.enterTextValue("findElementById", "editSurname", DataMap,"EditSurname", webDriver, "MSM");
		         //Entering Value in Flat id
		        stepExecutor.enterTextValue("findElementById", "editFlatId", DataMap,"EditFlatId", webDriver, "MSM");
		       // Entering Value in House name
		        stepExecutor.enterTextValue("findElementById", "editHouseName", DataMap,"EditHouseName", webDriver, "MSM");
		        //Entering Value in House No
		        stepExecutor.enterTextValue("findElementById", "editHouseNumber", DataMap,"EditHouseNumber", webDriver, "MSM");
		        //Entering Value in Address one
		        stepExecutor.enterTextValue("findElementById", "editAddress1", DataMap,"EditAddress1", webDriver, "MSM");
			       // Entering Value in Post Code
			    stepExecutor.enterTextValue("findElementById", "editPostCode", DataMap,"EditPostCode", webDriver, "MSM");
			     
			    stepExecutor.clickButton("findElementByXPath", ".//*[@class='btn btn--green confirm-add']", webDriver,"MSM");
			    
			    //stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/create-list-component/add-contact-component/div/div[1]/div[12]/button[3]", webDriver,"MSM");
			    Thread.sleep(5000);
			    		  
			    
			    webDriver.switchTo().window(winHandleBefore);
			  
			    stepExecutor.clickButton("findElementByXPath", ".//span[contains(text(),'Back to Add data page')]", webDriver,"MSM");
				
				Thread.sleep(5000);
				
				stepExecutor.clickByCss(".lists__my-lists--lists.lists__my-lists__list>li:nth-child(1)>div>span", webDriver);
				
             //   stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Add to mailshot')]", webDriver,"MSM");
                
                stepExecutor.clickButton("findElementByXPath", ".//button[contains(text(),'Add to mailshot')]", webDriver,"MSM");
				
				
				stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div[2]/lists-component/div/batch-tray-component/div/div/div/div[2]/button", webDriver,"MSM");
				
				
				Thread.sleep(6000);
				
				scrollwindow (0, 270);
									
								
	           stepExecutor.clickByCss("#AgreesToTermsAndConditions", webDriver);
				
								
				//stepExecutor.clickButton("findElementByXPath", ".//button[contains(text(),'Checkout')]", webDriver,"MSM");
				
				//System.out.println("Click on Checkout button");	
				stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div/div[1]/div[2]/div[4]/div/div/form/div/button", webDriver,"MSM");
				System.out.println("Click on Checkout button1");
				
				Thread.sleep(6000);
							
				PaypalPaymentDetails();
				//WebElement selectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
				/*webDriver.switchTo().frame(webDriver.findElement(By.cssSelector("#injectedUnifiedLogin>iframe")));
				stepExecutor.enterTextValue("findElementById", "email", DataMap,"Paypal_username", webDriver, "MSM");
				
				stepExecutor.enterTextValue("findElementByXPath", ".//*[@id='password']", DataMap,"Paypal_Password", webDriver, "MSM");
				
				stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Log In')]", webDriver,"MSM");
												
				//webDriver.switchTo().defaultContent();*/
				Thread.sleep(6000);
				//stepExecutor.clickByCss("#confirmButtonTop", webDriver);
				stepExecutor.clickButton("findElementByXPath", "html/body/div[2]/div/div/div/div/div/div/div/div/div/div/section/div[1]/div[1]/form/div[4]/input", webDriver, "MSM");
				System.out.println("Transaction done sucessfully");
				
				Thread.sleep(6000);
				LogoutAplication();
				LoginDetails();
				Images();
				LogoutAplication();
				LoginDetails();
								
				Thread.sleep(3000);
				
				Lists();
							
                Thread.sleep(3000);
            	LogoutAplication();
				LoginDetails();
			    DashBoard();
					 
			    Thread.sleep(3000);
				 
				 System.out.println("The Process completed sucessfully ");
					
			  										
			}
			
			} catch (Exception e) {
			e.printStackTrace();
		}
				
				
	}
	
	public void scrollwindow (int hScrollwidth, int vScrollhight)
	{
		
		JavascriptExecutor jse = (JavascriptExecutor)webDriver;
		jse.executeScript("scroll("+hScrollwidth+", "+vScrollhight+")"); // if the element is on bottom.
		
	}
	
	public void waituntilconditions( int timeinsec, String elementpath)
	{
		WebDriverWait wait = new WebDriverWait(webDriver, timeinsec); 
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//a[contains(text(),'Mailshots')]")));
		
	}
	
		
	public void DashBoard ()
	{
		try {
			
			
	stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Dashboard')]", webDriver,"Dashboard");
	Thread.sleep(5000);
	

	stepExecutor.clickButton("findElementByXPath", ".//span[contains(text(),'Start new mailshot')]", webDriver,"Dashboard");
	 
	Thread.sleep(9000);
	
	stepExecutor.enterTextValue("findElementById", "campaignname", DataMap,"Untitled_mailshot_dashboard", webDriver, "Dashboard");
	
	//click on Ok button
	stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'OK')]", webDriver,"Dashboard");
	Thread.sleep(5000);
	
	scrollwindow (0, 250);

	//click on Create a design
	stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Create a design')]", webDriver,"Dashboard");
	System.out.println("click on  Create a design button sucessfully");
	Thread.sleep(4000);
	
	scrollwindow (0, 250);
	
	//stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Choose Postcard')]", webDriver,"Dashboard");
	// click on Choose postcard
	stepExecutor.clickButton("findElementByXPath", ".//*[@id='format_chooser']/div/div[1]/article[1]/a", webDriver,"Dashboard");
				
	// switch to bars
	scrollwindow (0, 250);
	
	// click on Quick start button
	stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Quick start')]", webDriver,"Dashboard");
	Thread.sleep(8000);
	
	System.out.println("click on Quick start button sucessfully");
	
	// click on done 
	//stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Done')]", webDriver,"Dashboard");
	
	stepExecutor.clickButton("findElementByXPath", "html/body/editor-component/options-component/div/span[2]/span", webDriver,"Dashboard");
	Thread.sleep(8000);
	
	scrollwindow (0, 200);
	
	
	//click on Add address details button
	stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Add address details')]", webDriver,"Dashboard");
	
	//stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div/div[1]/div[2]/div[1]/div[2]/div/div/form/button", webDriver,"Dashboard");
	
	Thread.sleep(3000);
	
	scrollwindow (0, 270);
	
	if (verify.verifyElementIsPresentCheck(webDriver, ".//a[contains(text(),'Create a new list')]", "xpath")){
		List<WebElement> CustList2 = webDriver.findElements(By.xpath(".//*[@class='lists__my-lists']//li"));
		//List<WebElement> CustList = webDriver.findElements(By.cssSelector("lists__my-lists--lists lists__my-lists__list"));
		
				
		if (CustList2.size() >0)
		{
			stepExecutor.clickByCss(".lists__my-lists--lists.lists__my-lists__list>li:nth-child(1)>div>span", webDriver);
											
			Thread.sleep(2000);
			
			//Click on Add to mailshot		
			
			stepExecutor.clickButton("findElementByXPath", ".//button[contains(text(),'Add to mailshot')]", webDriver,"MSM");
			
			System.out.println("Add to mailshot");
			//stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div[2]/lists-component/div/batch-tray-component/div/div/div/div[2]/button", webDriver,"MSM");
			
			
			Thread.sleep(6000);
			
			scrollwindow (0, 270);
							
							
            stepExecutor.clickByCss("#AgreesToTermsAndConditions", webDriver);
			
			System.out.println("Agrees To Terms And Conditions");	
			
			//stepExecutor.clickButton("findElementByXPath", ".//button[contains(text(),'Checkout')]", webDriver,"MSM");
			
			//System.out.println("Click on Checkout button");	
			stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div/div[1]/div[2]/div[4]/div/div/form/div/button", webDriver,"MSM");
			
			System.out.println("Agrees To check the checkout button");					
			Thread.sleep(6000);
						
			
			//WebElement selectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
//			webDriver.switchTo().frame(webDriver.findElement(By.cssSelector("#injectedUnifiedLogin>iframe")));
//			
//			stepExecutor.enterTextValue("findElementById", "email", DataMap,"Paypal_username_dash", webDriver, "MSM");
//			
//			
//			stepExecutor.enterTextValue("findElementByXPath", ".//*[@id='password']", DataMap,"Paypal_Password_dash", webDriver, "MSM");
//			
//			
//			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Log In')]", webDriver,"MSM");
								
			//webDriver.switchTo().defaultContent();
			
		//	Thread.sleep(6000);
			//stepExecutor.clickByCss("#confirmButtonTop", webDriver);
			
			//stepExecutor.clickButton("findElementById", ".//*[@id='Pay Now']", webDriver, "MSM");
			
			stepExecutor.clickButton("findElementByCss", "#confirmButtonTop", webDriver, "MSM");
			
			System.out.println("Transaction done sucessfully");
			
			Thread.sleep(6000);
			
						
			
			
			stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Go to my dashboard')]", webDriver,"Dashboard");
			
			System.out.println("go to my dashboard");
			
		} 
		}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void Images ()
	
	{
		
		try {
			
			stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/header/nav/ul/li[4]/a", webDriver,"MSM");
			
			Thread.sleep(5000);
			
		
			stepExecutor.clickButton("findElementByXPath", ".//span[contains(text(),'Upload a new image')]", webDriver,"MSM");
												 
			Thread.sleep(3000);
			
			 String winHandleBeforeimage = webDriver.getWindowHandle();

		        for(String winHandle : webDriver.getWindowHandles()){
		        	webDriver.switchTo().window(winHandle);
		        	        	
		        }
		        
		        			        
		        String FilePath = scriptExecutor.readDataFile(strDataFileName, "MSM", rownumber, "Path");
		        
		        webDriver.findElement(By.id("image-upload")).sendKeys(FilePath);
		        		        
		       // stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div/div/my-images-component/upload-image-component/div[2]/div[2]", webDriver,"Images");
		        						  
				  webDriver.switchTo().window(winHandleBeforeimage);
				  stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Use this image')]", webDriver,"MSM");
				  Thread.sleep(6000);
				  System.out.println("Image Uploaded Sucessfully");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	
	public void Lists ()
	{
		
		try {
			stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Lists')]", webDriver,"Lists");
			Thread.sleep(5000);
			
			//ArrayList<WebElement> CustList = (ArrayList<WebElement>)webDriver.findElements(By.cssSelector("lists__my-lists--lists lists__my-lists__list"));
			//System.out.println(CustList.size());
			
		    List<WebElement> CustList1 = webDriver.findElements(By.xpath(".//*[@class='lists__my-lists--lists lists__my-lists__list']//li"));
			System.out.println(CustList1.size());
			
			if (CustList1.size() >0)
			{
				//stepExecutor.clickByCss(".lists__my-lists--lists lists__my-lists__list>li:nth-child(1)>div>span", webDriver);
				stepExecutor.clickByCss(".lists__my-lists--lists.lists__my-lists__list>li:nth-child(1)>div>span", webDriver);
				System.out.println("List checked");
				Thread.sleep(5000);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	public void PaypalPaymentDetails ()
	{
		
		try {
			
			webDriver.switchTo().frame(webDriver.findElement(By.cssSelector("#injectedUnifiedLogin>iframe")));
			
			stepExecutor.enterTextValue("findElementById", "email", DataMap,"Paypal_username", webDriver, "MSM");
			System.out.println("pay pal email");
			
			stepExecutor.enterTextValue("findElementByXPath", ".//*[@id='password']", DataMap,"Paypal_Password", webDriver, "MSM");
			System.out.println("pay pal password");
			
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Log In')]", webDriver,"MSM");
			
			System.out.println("click on pay pal button");
			
			
		} catch (Exception e) {
		
			e.printStackTrace();
		}
	}
	
	
	public void LoginDetails ()
	{
		try {
			
			//Click on Login 
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Login')]", webDriver,"MSM");
			
			Thread.sleep(5000);
			// Enter user name
			stepExecutor.enterTextValue("findElementById", "Email", DataMap,"username", webDriver, "MSM");
			//Enter Password
			stepExecutor.enterTextValue("findElementById", "Password", DataMap,"password", webDriver, "MSM");
			
			//Click on Login button
			stepExecutor.clickButton("findElementByXPath", ".//*[@id='login']/div/form/div[4]/button", webDriver,"MSM");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void LogoutAplication()
	
	{
		stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Logout')]", webDriver, "MSM");
		
	}
	
	/*public String navigatebacklogin(WebDriverWait wait) throws InterruptedException {

	//	wait.until(ExpectedConditions.elementToBeClickable("pageheader"));
	//	return pageheader.getText();
		
		wait.until(ExpectedConditions.visibilityOf(element);

	} */
	
	
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
