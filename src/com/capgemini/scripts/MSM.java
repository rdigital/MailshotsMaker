package com.capgemini.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
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
import org.dom4j.ElementPath;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.capgemini.driver.CreateDriver;
import com.capgemini.driver.ScriptExecutor;
import com.capgemini.driver.StepExecutor;
import com.capgemini.executor.ExecutionRowNumber;
import com.capgemini.executor.WriteMaster;
import com.capgemini.utilities.Element;
import com.capgemini.utilities.ReadExcel;
import com.capgemini.utilities.Reporter;
import com.capgemini.utilities.Utilities;
import com.capgemini.utilities.Verification;
import com.itextpdf.text.html.simpleparser.ElementFactory;
import com.opera.core.systems.scope.protos.Esdbg6Protos.ExamineList;


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
	
	/*public void initilize(WebDriver webDriver,int timeinsec){
		
		wait = new WebDriverWait(webDriver, timeinsec);
	}*/
	
	public void testcaseMain() throws InterruptedException, BiffException,
	Exception {
		
		
		stepExecutor.launchApplication("URL", DataMap, webDriver);
				
		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		CookieSubmitfunaction();
		
		//stepExecutor.clickButton("findElementById", "cookie_submit", webDriver, "MSM");
			
		try {
			
			
			LoginDetails();
									
			Thread.sleep(1000);
			
			String Act_home_title = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Act_home_title");
			String Exp_home_title = webDriver.findElement(By.cssSelector(".logo")).getText();
			
			WriteExcelDataFile(strDataFileName, rownumber, "Exp_home_title", Exp_home_title);
			
			if(Act_home_title.equals(Exp_home_title)){
				reporter.writeStepResult("MAILSHOTMAKER", "Verify Home Title is present in the page", "Expected Value :" + Exp_home_title, "Pass", "Expected text is present", true, webDriver);
				WriteExcelDataFile(strDataFileName, rownumber, "Home_title_Results", "Pass");
			} else
			{
				reporter.writeStepResult("HomeTitle", "Verify Home Title is present in the page", "Expected Value is:" + Exp_home_title, "Pass", "Expected text is not present", true, webDriver);
				WriteExcelDataFile(strDataFileName, rownumber, "Home_title_Results", "Fail");	
			}
			
								
			//Thread.sleep(4000);
			
						
			webDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

			(new WebDriverWait(webDriver, 30))
			.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//a[contains(text(),'Mailshots')]")));
			
			webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
			
		
			highlightelelements(webDriver, ".//a[contains(text(),'Mailshots')]");
			
			stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Mailshots')]", webDriver, "MailshotMaker");
			
			Thread.sleep(3000);
			
						
			//click on  New mailshot
		  List<WebElement> MailshotList = webDriver.findElements(By.xpath(".//*[@class='content-box list__empty']"));
			//System.out.println("Element found");
			
	  	if (MailshotList.size() > 0)
	  		
		     {			
				highlightelelements(webDriver, ".//span[contains(text(), 'Create your first mailshot')]");
				
				stepExecutor.clickButton("findElementByXPath", ".//span[contains(text(), 'Create your first mailshot')]", webDriver, "MailshotMaker");
				
				//Funaction included Mailshots name, create design, quick design  steps
				FormatSelection();
				
				stepExecutor.clickButton("findElementByXPath", "html/body/editor-component/onboarding-component/div/div/div[2]/a", webDriver, "MailshotMaker");
				
				stepExecutor.clickButton("findElementByXPath", "html/body/editor-component/onboarding-component/div/div/div[2]/a", webDriver, "MailshotMaker");
				
				stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Got it')]", webDriver, "MailshotMaker");
				
				//Funaction include back and done button click steps 
				FormatSelection_afterNextsteps();
				
				stepExecutor.clickButton("findElementByXPath", ".//*[@class='onboarding-button']/a", webDriver, "MailshotMaker");
				
                 
				//click on Add address details button
				Clickon_AddressDetails_Button();
				
			}else {
				
				
				highlightelelements(webDriver, ".//span[contains( text(),'New Mailshot')]");
				stepExecutor.clickButton("findElementByXPath", ".//span[contains(text(),'New Mailshot')]", webDriver, "MailshotMaker");
				
				FormatSelection();
				
				FormatSelection_afterNextsteps();
				

				//click on Add address details button
				Clickon_AddressDetails_Button();
				
			}
								
								
		    Thread.sleep(3000);
						
			scrollwindow (0, 270);
		
			if (verify.verifyElementIsPresentCheck(webDriver, ".//a[contains(text(),'Create a new list')]", "xpath")){
			List<WebElement> CustList = webDriver.findElements(By.xpath(".//*[@class='lists__my-lists']//li"));
			//List<WebElement> CustList = webDriver.findElements(By.cssSelector("lists__my-lists--lists lists__my-lists__list"));
			//System.out.println(CustList.size());
					
			if (CustList.size() > 0)
				
			{
				stepExecutor.clickByCss(".lists__my-lists--lists.lists__my-lists__list>li:nth-child(1)>div>span", webDriver,"MailshotMaker");
				
				Thread.sleep(2000);
					//click on Add to mailshot		
				highlightelelements(webDriver, ".//button[contains(text(),'Add to mailshot')]");
				stepExecutor.clickButton("findElementByXPath", ".//button[contains(text(),'Add to mailshot')]", webDriver,"MailshotMaker");
				
				System.out.println("Click on Add to mailshot");
				//stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div[2]/lists-component/div/batch-tray-component/div/div/div/div[2]/button", webDriver,"MSM");
				
				
				Thread.sleep(6000);
				//Click on See checklist button
				highlightelelements(webDriver, ".//*[contains(text(),'See checklist')]");
				stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'See checklist')]", webDriver,"MailshotMaker");
				
				Thread.sleep(2000);
				//Click on Back to mailshot button
				stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Back to mailshot')]", webDriver,"MailshotMaker");
				
				
				scrollwindow (0, 270);
				
				//Drop down value 
				
		        String PrintDeliveryvalue = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "PrintDelivery");
				
		    	new Select(webDriver.findElementByXPath(".//*[@id='postalOptions']")).selectByVisibleText(PrintDeliveryvalue);
				
				Thread.sleep(4000);
				
									
				//click on Agrees To Terms And Conditions 				
	            stepExecutor.clickByCss("#AgreesToTermsAndConditions", webDriver,"MailshotMaker");
				
				System.out.println("Agrees To Terms And Conditions");	
				
				//stepExecutor.clickButton("findElementByXPath", ".//button[contains(text(),'Checkout')]", webDriver,"MSM");
				
				//Click on Proceed to payment");	
				stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div/div[1]/div[2]/div[4]/div/div/form/div/button", webDriver,"MailshotMaker");
				
												
			     Thread.sleep(6000);
					
			     PaypalPaymentDetails();

		    	//webDriver.switchTo().defaultContent();
				
			  //  Thread.sleep(6000);
				
			//stepExecutor.clickButton("findElementByCss", "#confirmButtonTop", webDriver, "MSM");
			//	stepExecutor.clickByCss("#confirmButtonTop", webDriver);
				
			//	stepExecutor.clickButton("findElementByXPath", ".//*[@value ='Pay Now']", webDriver, "MSM");
				
				//stepExecutor.clickButton("findElementByXPath", "html/body/div[2]/div/div/div/div/div/div/div/div/div/div/section/div[1]/div[1]/form/div[4]/input", webDriver, "MSM");
			     
				PayPal_PayNowClickButton();
				
												
			} 
			}else {
				
				//#ToDO
				
				stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Add customer details')]", webDriver,"MailshotMaker");
				
				System.out.println("Add customer details");
				//stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div[2]/lists-component/div/div[2]/my-lists-component/section/div/div/a", webDriver,"MSM");
				
		
				//Enter Address Title
				stepExecutor.enterTextValue("findElementById", "listname", DataMap,"NewAddress_Title", webDriver, "MailshotMaker");
						
				System.out.println("Enter Address Title ");
				
				//click on ok button
				stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/create-list-component/div[1]/div[1]/div/div/form/span/button", webDriver,"MailshotMaker");
				
				scrollwindow (0, 260);
				
				Thread.sleep(3000);
				
				stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Add contact details individually')]", webDriver,"MailshotMaker");
				System.out.println("clicked on Add contact details individually via contains");
				
			//	stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/create-list-component/div[1]/div[2]/upload-data-component/div/div/div[1]/a", webDriver,"MSM");
				
				//System.out.println("clicked on Add contact details individually");

		        String winHandleBefore = webDriver.getWindowHandle();

		        for(String winHandle : webDriver.getWindowHandles())
		        	
		        {
		        	webDriver.switchTo().window(winHandle);
		        	        	
		           }
		        	
		       // Capturing add address details function 
		        AddressDetails();
		        
				//stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/create-list-component/add-contact-component/div/div[1]/div[12]/button[3]", webDriver,"MSM");
			    Thread.sleep(5000);
			    		  
			    
			    webDriver.switchTo().window(winHandleBefore);
			  
			    stepExecutor.clickButton("findElementByXPath", ".//span[contains(text(),'Back to Add data page')]", webDriver,"MailshotMaker");
				
				Thread.sleep(5000);
				//click on Got it button
				stepExecutor.clickButton("findElementByXPath", ".//*[@class='onboarding-button']/a", webDriver, "MailshotMaker");
				
				Thread.sleep(3000);
				
				stepExecutor.clickByCss(".lists__my-lists--lists.lists__my-lists__list>li:nth-child(1)>div>span", webDriver,"MailshotMaker");
				
             //   stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Add to mailshot')]", webDriver,"MSM");
                
                stepExecutor.clickButton("findElementByXPath", ".//button[contains(text(),'Add to mailshot')]", webDriver,"MailshotMaker");
				
				
				stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div[2]/lists-component/div/batch-tray-component/div/div/div/div[2]/button", webDriver,"MailshotMaker");
				
				
				Thread.sleep(6000);
				
				scrollwindow (0, 270);
				
	            String PrintDeliveryvalue = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "PrintDelivery");
				
		    	new Select(webDriver.findElementByXPath(".//*[@id='postalOptions']")).selectByVisibleText(PrintDeliveryvalue);
				
				Thread.sleep(4000);
									
								
	           stepExecutor.clickByCss("#AgreesToTermsAndConditions", webDriver,"MailshotMaker");
				
								
				//stepExecutor.clickButton("findElementByXPath", ".//button[contains(text(),'Checkout')]", webDriver,"MSM");
				
				//System.out.println("Click on Checkout button");	
			   stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div/div[1]/div[2]/div[4]/div/div/form/div/button", webDriver,"MailshotMaker");
			   
			   				
				Thread.sleep(6000);
				// Payment details 			
				PaypalPaymentDetails();
				
				Thread.sleep(6000);
				
				PayPal_PayNowClickButton();
				/*stepExecutor.clickByCss("#confirmButtonTop", webDriver);
				
				stepExecutor.clickButton("findElementByXPath", "html/body/div[2]/div/div/div/div/div/div/div/div/div/div/section/div[1]/div[1]/form/div[4]/input", webDriver, "MSM");*/
			
			  							
			}
			
			} catch (Exception e) {
			e.printStackTrace();
		}
						
	}
	

	public void highlightelelements (WebDriver webDriver, String elementxpath)
	
	{
		 
		WebElement element_node = webDriver.findElement(By.xpath(elementxpath));
									
		JavascriptExecutor js = (JavascriptExecutor) webDriver;

		js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element_node, "color: yellow; border: 2px solid yellow;");
		
		

	}
	
public void highlightelelements1 (WebDriver webDriver, String elementxpath) throws InterruptedException
	
	{
	
	try {
		
		WebElement element_node1 = webDriver.findElement(By.id(elementxpath));
		
		JavascriptExecutor js = (JavascriptExecutor) webDriver;

		js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element_node1, "color: black; border: 3px solid yellow;");
		
		

		//js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
		 
	
		
	}
	
	public void scrollwindow (int hScrollwidth, int vScrollhight) throws InterruptedException
	{
		try {
			
			JavascriptExecutor jse = (JavascriptExecutor)webDriver;
			jse.executeScript("scroll("+hScrollwidth+", "+vScrollhight+")"); // if the element is on bottom.
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	public void FormatSelection () throws InterruptedException
	
	{
		try {
								
		           Thread.sleep(10000);
            
					stepExecutor.enterTextValue("findElementById", "campaignname", DataMap,"Untitled_mailshot", webDriver, "MailshotMaker");
					
					//click on Ok button
					highlightelelements(webDriver, ".//*[contains(text(),'OK')]");
					stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'OK')]", webDriver,"MailshotMaker");
					
					Thread.sleep(5000);
								
					scrollwindow (0, 250);
					//waitcondition(wait, ".//*[contains(text(),'Create a design')]");
								
					//click on Create a design
					highlightelelements(webDriver, ".//*[contains(text(),'Create a design')]");
					stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Create a design')]", webDriver,"MailshotMaker");
					
					System.out.println("click on  Create a design button sucessfully");
					
					Thread.sleep(4000);
					
					 String ForamtSelection = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Format");
					            
			
			// click on Choose postcard
						
			
			 if (ForamtSelection.equals("CP")) {
				 highlightelelements(webDriver, ".//a[contains(text(),'Choose Postcard')]");
				 stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Choose Postcard')]", webDriver,"MailshotMaker");
			} else if (ForamtSelection.equals("CR")) {
				
				highlightelelements(webDriver, ".//a[contains(text(),'Choose Letter')]");
				stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Choose Letter')]", webDriver,"MailshotMaker");
			}else {
				highlightelelements(webDriver, ".//a[contains(text(),'Choose Sealed mailer')]");
				stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Choose Sealed mailer')]", webDriver,"MailshotMaker");
			}

									
			// switch to bars
			
			scrollwindow (0, 250);
	
			// click on Quick start button
			highlightelelements(webDriver, ".//*[contains(text(),'Quick start')]");
			
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Quick start')]", webDriver,"MailshotMaker");
			System.out.println("click on Quick start Sucessfully");
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	public void FormatSelection_afterNextsteps() throws InterruptedException
	
	{
		try {
			
			Thread.sleep(8000);
			 String ForamtSelection_back = scriptExecutor.readDataFile(strDataFileName, TestCase, rownumber, "Format");
		
			 if (ForamtSelection_back.equals("CP") || ForamtSelection_back.equals("SP")) {
				 
				//click on Back
					highlightelelements(webDriver, "html/body/editor-component/div[1]/div[3]/sidepicker-component[2]/div/div");
					//Design Back button
					stepExecutor.clickButton("findElementByXPath", "html/body/editor-component/div[1]/div[3]/sidepicker-component[2]/div/div", webDriver,"MailshotMaker");
					//stepExecutor.clickButton("findElementByXPath", ".//div[text()='Back']", webDriver,"MSM");
					Thread.sleep(2000);
					
					System.out.println("click on back");
				 
			 }
						 
			 SaveMailshots_afterDone ();
			/*//click on Done button
			stepExecutor.clickButton("findElementByXPath", "html/body/editor-component/options-component/div/span[2]/span", webDriver,"MailshotMaker");
			
			Thread.sleep(9000);
			
			scrollwindow (0, 200);*/
					
				
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	public void SaveMailshots_afterDone () throws InterruptedException 
	{
		
		try {
			
			//click on Done button
			//stepExecutor.clickButton("findElementByXPath", "html/body/editor-component/options-component/div/span[2]/span", webDriver,"MailshotMaker");
			stepExecutor.clickByCss(".icon-tick", webDriver, "MailshotMaker");
			
			Thread.sleep(5500);
					
		/*	webDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

			(new WebDriverWait(webDriver, 30))
			.until(ExpectedConditions.visibilityOfElementLocated(By.className("notification-text")));
			webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);*/

			WebElement Savingmailshots = webDriver.findElement(By.className("notification-text"));
			
			String SaveMailshotsvaluetaken = Savingmailshots.getText();
			
			System.out.println("The Value of" + SaveMailshotsvaluetaken);
			
	       //  WebElement ReturnMerchant = webDriver.findElement(By.xpath(".//*[contains(text(),'Return to Merchant')]"));
			
			//String valueReturnMerchant = ReturnMerchant.getText();
			
			//System.out.println("Value\t" + valueReturnMerchant);
			
			if(SaveMailshotsvaluetaken.contains("Error saving mailshot"))
			{
				System.out.println("Might may be there was an error to saving the mailshot");
				reporter.writeStepResult("MailshotMaker", "Mailshots Status", "", "Fail", "there was an error to saving the mailshot", true, webDriver);
				webDriver.close();
			    System.exit(0);
				
			} else
			{
				
				Thread.sleep(9000);
				
				scrollwindow (0, 200);
			}
			
		
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	

	public void waitcondition (WebDriverWait wait, String elementpath)
	{
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementpath)));
	
	}
	
	public void DashBoard () throws InterruptedException
	{
		try {
			
	highlightelelements(webDriver,".//a[contains(text(),'Dashboard')]");
	stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Dashboard')]", webDriver,"Dashboard");
	Thread.sleep(5000);
	
	highlightelelements(webDriver,".//span[contains(text(),'Start new mailshot')]");
	stepExecutor.clickButton("findElementByXPath", ".//span[contains(text(),'Start new mailshot')]", webDriver,"Dashboard");
	 
	Thread.sleep(9000);
	
	stepExecutor.enterTextValue("findElementById", "campaignname", DataMap,"Untitled_mailshot_dashboard", webDriver, "Dashboard");
	
	//click on Ok button
	highlightelelements(webDriver,".//*[contains(text(),'OK')]");
	stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'OK')]", webDriver,"Dashboard");
	Thread.sleep(5000);
	
	scrollwindow (0, 250);

	//click on Create a design
	highlightelelements(webDriver,".//*[contains(text(),'Create a design')]");
	stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Create a design')]", webDriver,"Dashboard");
	System.out.println("click on  Create a design button sucessfully");
	Thread.sleep(4000);
	
	scrollwindow (0, 250);
	
	// click on Choose postcard
	highlightelelements(webDriver,".//a[contains(text(),'Choose Postcard')]");
	stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Choose Postcard')]", webDriver,"Dashboard");

	//stepExecutor.clickButton("findElementByXPath", ".//*[@id='format_chooser']/div/div[1]/article[1]/a", webDriver,"Dashboard");
				
	// switch to bars
	scrollwindow (0, 250);
	
	// click on Quick start button
	highlightelelements(webDriver,".//*[contains(text(),'Quick start')]");
	stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Quick start')]", webDriver,"Dashboard");
	Thread.sleep(8000);
	
	System.out.println("click on Quick start button sucessfully");
	
	// click on done 
	//stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Done')]", webDriver,"Dashboard");
	highlightelelements(webDriver,"html/body/editor-component/options-component/div/span[2]/span");
	stepExecutor.clickButton("findElementByXPath", "html/body/editor-component/options-component/div/span[2]/span", webDriver,"Dashboard");
	Thread.sleep(8000);
	
	scrollwindow (0, 200);
	
	
	//click on Add address details button
	
	Clickon_AddressDetails_Button();
	
	
	Thread.sleep(3000);
	
	scrollwindow (0, 270);
	
	if (verify.verifyElementIsPresentCheck(webDriver, ".//a[contains(text(),'Create a new list')]", "xpath")){
		List<WebElement> CustList2 = webDriver.findElements(By.xpath(".//*[@class='lists__my-lists']//li"));
		//List<WebElement> CustList = webDriver.findElements(By.cssSelector("lists__my-lists--lists lists__my-lists__list"));
		
				
		if (CustList2.size() >0)
		{
			stepExecutor.clickByCss(".lists__my-lists--lists.lists__my-lists__list>li:nth-child(1)>div>span", webDriver,"Dashboard");
											
			Thread.sleep(2000);
			
			//Click on Add to mailshot		
			highlightelelements(webDriver,".//button[contains(text(),'Add to mailshot')]");
			stepExecutor.clickButton("findElementByXPath", ".//button[contains(text(),'Add to mailshot')]", webDriver,"Dashboard");
			
			System.out.println("Add to mailshot");
			//stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/div[2]/lists-component/div/batch-tray-component/div/div/div/div[2]/button", webDriver,"MSM");
			
			
			Thread.sleep(6000);
			//Click on See checklist button
			highlightelelements(webDriver,".//*[contains(text(),'See checklist')]");
			
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'See checklist')]", webDriver,"Dashboard");
			Thread.sleep(2000);
			//Click on Back to mailshot button
			highlightelelements(webDriver,".//a[contains(text(),'Back to mailshot')]");
			
			stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Back to mailshot')]", webDriver,"Dashboard");
			
			scrollwindow (0, 270);
							
							
            stepExecutor.clickByCss("#AgreesToTermsAndConditions", webDriver,"Dashboard");
			
		//	System.out.println("Agrees To Terms And Conditions");	
			
			//stepExecutor.clickButton("findElementByXPath", ".//button[contains(text(),'Checkout')]", webDriver,"MSM");
			System.out.println("Dashboard opreation done sucessfully");
			
			
		} 
		
		}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void Images () throws InterruptedException
	
	{
		
		try {
			
			highlightelelements(webDriver,"html/body/div[1]/header/nav/ul/li[4]/a");
			
	    	stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/header/nav/ul/li[4]/a", webDriver,"Image");
			
			Thread.sleep(4000);
	    	
	    	/*(new WebDriverWait(webDriver, 50))
			.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@class='library-image-inner']")));*/
			
		  List<WebElement> Imagelist = webDriver.findElements(By.xpath(".//*[@class='library-image-inner']"));
		
		if (Imagelist.size() >0)
	    {
			//this is for when existing image attached
			highlightelelements(webDriver,".//div[contains(text(),'Upload a new image')]");
			stepExecutor.clickButton("findElementByXPath", ".//div[contains(text(),'Upload a new image')]", webDriver,"Image");
	   	}else{
			
			//this is for when no  image attached
			highlightelelements(webDriver,".//span[contains(text(),'Upload a new image')]");
			stepExecutor.clickButton("findElementByXPath", ".//span[contains(text(),'Upload a new image')]", webDriver,"Image");
		}
				
													 
			Thread.sleep(3000);
			
			 String winHandleBeforeimage = webDriver.getWindowHandle();

		        for(String winHandle : webDriver.getWindowHandles()){
		        	webDriver.switchTo().window(winHandle);
		        	        	
		        }
		        
		        			        
		        String FilePath = scriptExecutor.readDataFile(strDataFileName, "MSM", rownumber, "Path");
		        
		        webDriver.findElement(By.id("image-upload")).sendKeys(FilePath);
		         
		         						  
				  webDriver.switchTo().window(winHandleBeforeimage);
				  
				  highlightelelements(webDriver,".//a[contains(text(),'Use this image')]");
				  
				  stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Use this image')]", webDriver,"Image");
				  
				  Thread.sleep(8000);
				  
				  System.out.println("Image Uploaded Sucessfully");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	
	public void Lists () throws InterruptedException
	{
		
		try {
			// New List Creation funaction included new address details.
			
			CreateNewList();
			
			/*stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Lists')]", webDriver,"Lists");
			//Thread.sleep(5000);
			webDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

			(new WebDriverWait(webDriver, 30))
			.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@class='lists__my-lists--lists lists__my-lists__list']//li")));
			webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			//ArrayList<WebElement> CustList = (ArrayList<WebElement>)webDriver.findElements(By.cssSelector("lists__my-lists--lists lists__my-lists__list"));
			//System.out.println(CustList.size());*/
			
		//   List<WebElement> CustList1 = webDriver.findElements(By.xpath(".//*[@class='lists__my-lists--lists lists__my-lists__list']//li"));
			
			
	/*	if (CustList1.size() >0)
			{
				
				stepExecutor.clickByCss(".lists__my-lists--lists.lists__my-lists__list>li:nth-child(1)>div>span", webDriver);
				System.out.println("List checked");
				Thread.sleep(5000);
				
			}*/
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	
	private void CreateNewList () throws InterruptedException 
	
	{
		try {
			
			
			stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Lists')]", webDriver,"Lists");
			//Thread.sleep(5000);
			webDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

			(new WebDriverWait(webDriver, 30))
			.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[contains(text(),'Create a new list')]")));
			webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Create a new list')]", webDriver,"Lists");
			
			Thread.sleep(4000);
			
			
			//Enter Address Title
			stepExecutor.enterTextValue("findElementById", "listname", DataMap,"NewAddress_Title", webDriver, "Lists");
					
			System.out.println("Enter Address Title");
			
			Thread.sleep(4000);
			
			//click on ok button to give the list name
		    stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/create-list-component/div[1]/div[1]/div/div/form/span/button", webDriver,"Lists");
			//stepExecutor.clickButton("findElementById", "listname", webDriver,"Lists");
		//	stepExecutor.clickByCss(".validation-msg invalid-campaign-name", webDriver, "Lists");
			
						
			scrollwindow (0, 260);
			
			Thread.sleep(3000);
			
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Add contact details individually')]", webDriver,"Lists");
			
			System.out.println("clicked on Add contact details individually via contains");
			
		//	stepExecutor.clickButton("findElementByXPath", "html/body/div[1]/create-list-component/div[1]/div[2]/upload-data-component/div/div/div[1]/a", webDriver,"MSM");
			
			//System.out.println("clicked on Add contact details individually");

	        String winHandleBefore = webDriver.getWindowHandle();

	        for(String winHandle : webDriver.getWindowHandles())
	        {
	        	webDriver.switchTo().window(winHandle);
	        	        	
	           }
	        	
	       // Capturing add address details function 
	        AddressDetails();
	        
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void PaypalPaymentDetails () throws InterruptedException
	
	{
		
		try {
			
			webDriver.switchTo().frame(webDriver.findElement(By.cssSelector("#injectedUnifiedLogin>iframe")));
			
			stepExecutor.enterTextValue("findElementById", "email", DataMap,"Paypal_username", webDriver, "PayPal");
			
			System.out.println("Entered pay pal email sucessfully");
			
			highlightelelements(webDriver, ".//*[@id='password']");
			stepExecutor.enterTextValue("findElementByXPath", ".//*[@id='password']", DataMap,"Paypal_Password", webDriver, "PayPal");
			System.out.println("Entered pay pal password sucessfully");
			
			//highlightelelements(webDriver, ".//*[contains(text(),'Log In')]");
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Log In')]", webDriver,"PayPal");
						
		    webDriver.switchTo().defaultContent();
			
			Thread.sleep(9000);
			
			System.out.println("click on Pay Pal login button sucessfully");
		
						
		} catch (Exception e) {
		
			e.printStackTrace();
		}
	}
	
	
	public void PayPal_PayNowClickButton () throws InterruptedException
	{
	try {
				
		PaypalButtonVisible();
		
	   
		Thread.sleep(6000);
		
		WebElement aftvalue = webDriver.findElement(By.xpath(".//*[@class='heading02 heading02_no-margin-top']"));
		
		String valuetaken = aftvalue.getText();
		
       //  WebElement ReturnMerchant = webDriver.findElement(By.xpath(".//*[contains(text(),'Return to Merchant')]"));
		
		//String valueReturnMerchant = ReturnMerchant.getText();
		
		//System.out.println("Value\t" + valueReturnMerchant);
		
		if(!valuetaken.contains("Thank you,"))
		{
			System.out.println("Might may be there was an error from the PayPal side");
			reporter.writeStepResult("MailshotMaker", "Transaction Status", "", "Fail", "Transaction Failed Due to PayPal Issue", true, webDriver);
			
		}/*else if (!valueReturnMerchant.contains("Return to Merchant"))
		{
			System.out.println("Might may be there was an error from the PayPal side");
			
						
		}*/else {
			
			WebElement Orderid = webDriver.findElement(By.xpath(".//*[@class='heading06']/b[2]"));
			
			String Ordidvalue = Orderid.getText();
			
			System.out.println("Generated Order id:\t "+ Ordidvalue);
						
			WriteExcelDataFile(strDataFileName, rownumber, "OrderNumber", Ordidvalue);
			
			reporter.writeStepResult("MAILSHOTMAKER", "Order number generated", "Order number :" + Ordidvalue, "Pass", "Transaction done with Order number", true, webDriver);
			WriteExcelDataFile(strDataFileName, rownumber, "Home_title_Results", "Pass");
			System.out.println("Order number generated and transaction complated sucessfully");
				
			Thread.sleep(6000);
			
			LogoutAplication();
			
			LoginDetails();
				
			Images();
				
			LogoutAplication();
			
			LoginDetails();
											
			Thread.sleep(3000);
				
			//Lists();
			DashBoard();
							
	        Thread.sleep(3000);
	           
	     	LogoutAplication();
	        	
			LoginDetails();
				
		   //  DashBoard();
		 	Lists();
		 	
		 	Thread.sleep(3000);
		     MyProfile_MyOrders();
					 
		     Thread.sleep(3000);
			    
	         LogoutAplication();
					
				 
			 System.out.println("Sucessfully logout from the application ");
			 
			
		}
		
								
	} catch (Exception e) {
		// TODO: handle exception
		
		e.printStackTrace();
	}
	}
	
	public void CookieSubmitfunaction () throws InterruptedException
	
	{
		try {
			
				WebElement e1 = webDriver.findElementById("cookie_submit");
			
				stepExecutor.clickButton("findElementById", "cookie_submit", webDriver, "MailshotMaker");
				
		} catch (Exception e) {
			
		}
	}
	
	public void PaypalButtonVisible() throws InterruptedException 
	
	{
		try {
			
			Thread.sleep(6000);
			
            WebElement LabelWelcomeback = webDriver.findElement(By.xpath(".//P[@id='reviewUserInfo']"));
			
			String Welcomebackvalue = LabelWelcomeback.getText();
			
						
			if(!Welcomebackvalue.contains("Welcome back, Cap!"))
			{
				System.out.println("Might may be there was an error......");
				reporter.writeStepResult("PayPal", "Transaction Status", "", "Fail", "Transaction Failed Due to error", true, webDriver);
				
			}else {
				Thread.sleep(7000);
				
			/*	webDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

				(new WebDriverWait(webDriver, 40))
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#confirmButtonTop")));
				 webDriver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);*/
				
				stepExecutor.clickButton("findElementByCss", "#confirmButtonTop", webDriver, "PayPal");
				
				System.out.println("PayNow done......");
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

		
	public void LoginDetails () throws InterruptedException
	{
		try {
			
			//Click on Login 
			highlightelelements(webDriver,".//*[contains(text(),'Login')]");
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Login')]", webDriver,"MailshotMaker");
			
		//	Thread.sleep(5000);
			webDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			(new WebDriverWait(webDriver, 10))
			.until(ExpectedConditions.visibilityOfElementLocated(By.id("Email")));
			webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			
			// Enter user name
			highlightelelements1(webDriver,"Email");
			stepExecutor.enterTextValue("findElementById", "Email", DataMap,"username", webDriver, "MailshotMaker");
			
			//Enter Password
			highlightelelements1(webDriver,"Password");
			stepExecutor.enterTextValue("findElementById", "Password", DataMap,"password", webDriver, "MailshotMaker");
			
			//Click on Login button
			//stepExecutor.clickButton("findElementByXPath", ".//*[@id='login']/div/form/div[4]/button", webDriver,"MSM");
			highlightelelements(webDriver,".//button[contains(text(),'Log in')]");
			stepExecutor.clickButton("findElementByXPath", ".//button[contains(text(),'Log in')]", webDriver,"MailshotMaker");
			
			System.out.println("Log on to application sucessfully");
			
						
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void LogoutAplication() throws InterruptedException
	
	{
		
		try {
			highlightelelements(webDriver, ".//a[contains(text(),'Logout')]");
			stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Logout')]", webDriver, "MailshotMaker");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
	}
	
	//click on Add address details button
	
	public void Clickon_AddressDetails_Button () throws InterruptedException
	{
		try {
			
            highlightelelements(webDriver, ".//*[contains(text(),'Add address details')]");
			
			stepExecutor.clickButton("findElementByXPath", ".//*[contains(text(),'Add address details')]", webDriver,"MailshotMaker");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	///Funaction 
	public void AddressDetails () throws InterruptedException
	{
		try {
			
			//Entering Value in Title
	        stepExecutor.enterTextValue("findElementById", "editTitle", DataMap,"EditTitle", webDriver, "Lists");
	       // Entering Value in First name
	        stepExecutor.enterTextValue("findElementById", "editFirstName", DataMap,"EditFirstName", webDriver, "Lists");
	        //Entering Value in Last name
	        stepExecutor.enterTextValue("findElementById", "editSurname", DataMap,"EditSurname", webDriver, "Lists");
	         //Entering Value in Flat id
	        stepExecutor.enterTextValue("findElementById", "editFlatId", DataMap,"EditFlatId", webDriver, "Lists");
	       // Entering Value in House name
	        stepExecutor.enterTextValue("findElementById", "editHouseName", DataMap,"EditHouseName", webDriver, "Lists");
	        //Entering Value in House No
	        stepExecutor.enterTextValue("findElementById", "editHouseNumber", DataMap,"EditHouseNumber", webDriver, "Lists");
	        //Entering Value in Address one
	        stepExecutor.enterTextValue("findElementById", "editAddress1", DataMap,"EditAddress1", webDriver, "Lists");
		       // Entering Value in Post Code
		    stepExecutor.enterTextValue("findElementById", "editPostCode", DataMap,"EditPostCode", webDriver, "Lists");
		     
		    stepExecutor.clickButton("findElementByXPath", ".//*[@class='btn btn--green confirm-add']", webDriver,"Lists");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
public void MyProfile_MyOrders() throws InterruptedException
	
	
{
	try {
		
		  highlightelelements(webDriver, ".//a[contains(text(),'My orders')]");
		    stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'My orders')]", webDriver, "MyOrder");
		    
		    Thread.sleep(3000);
			/*(new WebDriverWait(webDriver, 30))
			.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//a[contains(text(),'Profile')]")));*/
		    
		    highlightelelements(webDriver, ".//a[contains(text(),'Profile')]");
			stepExecutor.clickButton("findElementByXPath", ".//a[contains(text(),'Profile')]", webDriver, "MyProfile");
					
		
	} catch (Exception e) {
		// TODO: handle exception
		
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
