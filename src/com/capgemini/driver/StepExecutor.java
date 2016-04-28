package com.capgemini.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.capgemini.executor.ExecutionRowNumber;
import com.capgemini.utilities.CreateResult;
import com.capgemini.utilities.Reporter;
import com.capgemini.utilities.Utilities;
import com.capgemini.utilities.Verification;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;

/**
 * StepExecutor --- Class for the executing the steps in the script
 * 
 * @author Sunil Kumar
 */

public class StepExecutor {
	Reporter reporter;
	Utilities utils;
	private Verification verify;
	ScriptExecutor scriptExecutor = new ScriptExecutor();
	ExecutionRowNumber executionRowNumber = new ExecutionRowNumber();
	private String strAbsolutepath = new File("").getAbsolutePath();
	private String strDataPath = strAbsolutepath + "/data/";
	private boolean sFlag;

	public StepExecutor(Reporter reporter) {

		this.reporter = reporter;
		utils = new Utilities(reporter);

		verify = new Verification(reporter);

	}

	// CreateDriver driver = new CreateDriver();
	// RemoteWebDriver webDriver = driver.getWebDriver();

	/*
	 * // Object for calling verification functions VerificationFunctions
	 * verificationFunctions = new VerificationFunctions();
	 */

	/*
	 * public void launchApplication(String strColumnName, String
	 * strDataFileName, RemoteWebDriver webDriver){ String strDetails =
	 * utils.getDataFileInfo(); int rowNumber =
	 * executionRowNumber.getExecutionRowNumber(); String strData =
	 * scriptExecutor.readDataFile(strDataFileName, rowNumber, strColumnName);
	 * String [] arrDetails = strDetails.split("_"); webDriver.get(strData);
	 * 
	 * reporter.writeStepResult(arrDetails[1].toUpperCase(),
	 * "Launch Application URL", strData, "Pass",
	 * "Launched Application URL successfully", true, webDriver); }
	 */

	// Launching the specific URL
	public Boolean launchApplication(String strColumnName,
			Map<String, String> DataMap, RemoteWebDriver webDriver) {
		/* String strDetails = utils.getDataFileInfo(); */
		/* int rowNumber = executionRowNumber.getExecutionRowNumber(); */
		Boolean sFlag = true;
		String strData = null;
		String strKey = strColumnName;
		System.out.println(strKey);
		Map<String, String> dataMapLocal = DataMap;
		if (dataMapLocal.containsKey(strKey)) {
			strData = dataMapLocal.get(strKey);
		} else {
			sFlag = false;
			return sFlag;
		}
		webDriver.manage().window().maximize();
		webDriver.get(strData);

		reporter.writeStepResult("LAUNCHAPPLICATION", "Lauch Application URL",
				strData, "Pass", "Lauched Application URL successfully", true,
				webDriver);
		return sFlag;
	}

	public void clickElement(String strDriverMethod, String strAttribute,
			RemoteWebDriver webDriver, String sAppname) {

		try {
			if (strDriverMethod.equals("findElementByName")) {
				webDriver.findElementByName(strAttribute).click();
			} else if (strDriverMethod.equals("findElementById")) {
				webDriver.findElementById(strAttribute).click();
			} else if (strDriverMethod.equals("findElementByCss")) {
				webDriver.findElementByCssSelector(strAttribute).click();
			}else if (strDriverMethod.equals("findElementByClassName")) {
				webDriver.findElementByClassName(strAttribute).click();
			} else if (strDriverMethod.equals("findElementByXPath"))  {
				webDriver.findElementByXPath(strAttribute).click();
			}
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Click on element", "", "Pass",
					"Clicked element successfully", true, webDriver);
		} catch (WebDriverException w1) {
			System.out.println(w1.getMessage());
			System.out.println(w1.getCause());
			System.out.println(w1.getLocalizedMessage());
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Click on element", "", "Fail", w1.getMessage(), true,
					webDriver);
		} catch (Exception e1) {
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Click on element", "", "Fail",
					"Not able to click on the element", true, webDriver);
		}
	}

	public Boolean selectListValue(String strDriverMethod, String strAttribute,
			Map<String, String> DataMap, String strKey,
			RemoteWebDriver webDriver, String sAppname)
			throws InterruptedException {
		Boolean sFlag = true;
		Map<String, String> dataMapLocal = DataMap;
		String strData = null;
		if (dataMapLocal.containsKey(strKey)) {
			strData = dataMapLocal.get(strKey);
			strData = strData.trim();
		} else {
			sFlag = false;
			return sFlag;
		}
		Select dd = null;
		if (strData.isEmpty())
			return sFlag;
		try {
			if (strDriverMethod.equals("findElementByName")) {
				dd = new Select(webDriver.findElementByName(strAttribute));
			} else if (strDriverMethod.equals("findElementById")) {
				dd = new Select(webDriver.findElementById(strAttribute));
			} else if (strDriverMethod.equals("findElementByXpath")) {
				dd = new Select(webDriver.findElementByXPath(strAttribute));
			} else if (strDriverMethod.equals("findElementByCss")) {
				dd = new Select(
						webDriver.findElementByCssSelector(strAttribute));
			}
			dd.selectByVisibleText(strData);
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Select Value in dropdown field", strData, "Pass",
					"Value selected successfully", true, webDriver);
		} catch (WebDriverException w1) {
			// String strErrorMessage = w1.getMessage();
			// String [] arrMessages = strErrorMessage.split("(");
			sFlag = false;
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Select Value in dropdown field", strData, "Fail",
					"Unable to select value", true, webDriver);
		} catch (Exception e1) {
			sFlag = false;
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Select Value in dropdown field", strData, "Fail",
					"Unable to select value", true, webDriver);
		}
		Thread.sleep(3000);
		return sFlag;

		/*String strData = null;
		if (dataMapLocal.containsKey(strKey)) {
			strData = dataMapLocal.get(strKey);
		} else {
			sFlag = false;
			return sFlag;
		}
		Select dd = null;
		WebElement element=null;
		Actions action = new Actions(webDriver);
		if (strData.isEmpty())
			return sFlag;
		try {
			if (strDriverMethod.equals("findElementByName")) {
				
				dd = new Select(webDriver.findElementByName(strAttribute));
			} else if (strDriverMethod.equals("findElementById")) {
				element= webDriver.findElement(By.id(strAttribute));
				new Select(element).selectByValue(strData);
			} else if (strDriverMethod.equals("findElementByXpath")) {
				element= webDriver.findElement(By.xpath(strAttribute));
				new Select(element).selectByValue(strData);
			} else if (strDriverMethod.equals("findElementByCss")) {
				element= webDriver.findElement(By.cssSelector(strAttribute));
				new Select(element).selectByValue(strData);
			}
			
			action.perform();
			//dd.selectByVisibleText(strData);
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Select Value in dropdown field", strData, "Pass",
					"Value selected successfully", true, webDriver);
		catch (WebDriverException w1) {
			// String strErrorMessage = w1.getMessage();
			// String [] arrMessages = strErrorMessage.split("(");
			sFlag = false;
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Select Value in dropdown field", strData, "Fail",
					"Unable to select value", true, webDriver);
		} catch (Exception e1) {
			sFlag = false;
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Select Value in dropdown field", strData, "Fail",
					"Unable to select value", true, webDriver);
		}
		Thread.sleep(3000);
		return sFlag;*/
	}

	/*
	 * public void enterTextValue(String strDriverMethod, String strAttribute,
	 * Map <String, String> DataMap, String strKey, RemoteWebDriver webDriver){
	 * String strDetails = utils.getDataFileInfo(); int rowNumber =
	 * executionRowNumber.getExecutionRowNumber(); String strData =
	 * scriptExecutor.readDataFile(strDataFileName, rowNumber, strElement);
	 * String [] arrDetails = strDetails.split("_");
	 * 
	 * try{ if(strDriverMethod.equals("findElementByName")){
	 * webDriver.findElementByName(strAttribute).sendKeys(strData); }else
	 * if(strDriverMethod.equals("findElementById")){
	 * webDriver.findElementById(strAttribute).sendKeys(strData); }else{
	 * webDriver.findElementByXPath(strAttribute).sendKeys(strData); }
	 * reporter.writeStepResult(arrDetails[1].toUpperCase(),
	 * "Enter Value in text field", strData, "Pass",
	 * "Value entered successfully", true, webDriver); }catch(WebDriverException
	 * w1){ //String strErrorMessage = w1.getMessage(); //String [] arrMessages
	 * = strErrorMessage.split("(");
	 * reporter.writeStepResult(arrDetails[1].toUpperCase(),
	 * "Enter Value in text field", strData, "Fail", "Unable to enter value",
	 * true, webDriver); } catch(Exception e1){
	 * reporter.writeStepResult(arrDetails[1].toUpperCase(),
	 * "Enter Value in text field", strData, "Fail", "Unable to enter value",
	 * true, webDriver); } }
	 */

	//added by harsha: selecting as value ofcombobox using value input from datasheet
	public Boolean selectListValueByContainsValue(String strDriverMethod, String strAttribute,
			 String strKey,
			RemoteWebDriver webDriver, String sAppname, int expectedRowNumber)
			throws InterruptedException 
	{
		String strExcelDataFileName = strDataPath + "MasterSheet.xls";
		POIFSFileSystem fs;
		String strCellValue = null;
		String strData = null;
		try {
			fs = new POIFSFileSystem(new FileInputStream(
					strExcelDataFileName));
		
		HSSFWorkbook workbook = new HSSFWorkbook(fs);
		HSSFSheet dataSheet = workbook.getSheet(sAppname);
		HSSFRow dataRow = dataSheet.getRow(0);
		
		int totalCells = dataRow.getLastCellNum();
		for (int i = 0; i < totalCells; i++) {
			strCellValue = dataRow.getCell(i).toString();
			if (strCellValue.equals(strKey)) {
				dataRow = dataSheet.getRow(expectedRowNumber);
				if (dataRow.getCell(i) != null) {
					strCellValue = dataRow.getCell(i).toString();
				} else {
					strCellValue = "";
				}
				break;
			}
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Select dd = null;
		try {
			if (strDriverMethod.equals("findElementByName")) {
				dd = new Select(webDriver.findElementByName(strAttribute));
			} else if (strDriverMethod.equals("findElementById")) {
				dd = new Select(webDriver.findElementById(strAttribute));
			} else if (strDriverMethod.equals("findElementByXpath")) {
				dd = new Select(webDriver.findElementByXPath(strAttribute));
			} else if (strDriverMethod.equals("findElementByCss")) {
				dd = new Select(
						webDriver.findElementByCssSelector(strAttribute));
			}
			List<WebElement> allOptions = dd.getOptions();
			for (WebElement webElement : allOptions)
			{
					if (webElement.getText().contains(strCellValue)){
						strData = webElement.getText();
						dd.selectByVisibleText(strData);
						System.out.println(strData);
					}else{
						System.out.println("Value not found in dropdown field");
					}
			}
						
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Select Value in dropdown field", strData, "Pass",
					"Value selected successfully", true, webDriver);
		} catch (WebDriverException w1) {
			sFlag = false;
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Select Value in dropdown field", strData, "Fail",
					"Unable to select value", true, webDriver);
		} catch (Exception e1) {
			sFlag = false;
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Select Value in dropdown field", strData, "Fail",
					"Unable to select value", true, webDriver);
		}
		Thread.sleep(3000);
		return sFlag;
	}
	
	
	
	public Boolean selectListValueRownumberwise(String strDriverMethod, String strAttribute,
			 String strKey,
			RemoteWebDriver webDriver, String sAppname, int expectedRowNumber)
			throws InterruptedException 
	{
		String strExcelDataFileName = strDataPath + "MasterSheet.xls";
		POIFSFileSystem fs;
		String strCellValue = null;
		String strData = null;
		try {
			fs = new POIFSFileSystem(new FileInputStream(
					strExcelDataFileName));
		
		HSSFWorkbook workbook = new HSSFWorkbook(fs);
		HSSFSheet dataSheet = workbook.getSheet("TWG");
		HSSFRow dataRow = dataSheet.getRow(0);
		
		int totalCells = dataRow.getLastCellNum();
		for (int i = 0; i < totalCells; i++) {
			strCellValue = dataRow.getCell(i).toString();
			if (strCellValue.equals(strKey)) {
				dataRow = dataSheet.getRow(expectedRowNumber);
				if (dataRow.getCell(i) != null) {
					strCellValue = dataRow.getCell(i).toString();
				} else {
					strCellValue = "";
				}
				break;
			}
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Select dd = null;
		try {
			if (strDriverMethod.equals("findElementByName")) {
				dd = new Select(webDriver.findElementByName(strAttribute));
			} else if (strDriverMethod.equals("findElementById")) {
				dd = new Select(webDriver.findElementById(strAttribute));
			} else if (strDriverMethod.equals("findElementByXpath")) {
				dd = new Select(webDriver.findElementByXPath(strAttribute));
			} else if (strDriverMethod.equals("findElementByCss")) {
				dd = new Select(
						webDriver.findElementByCssSelector(strAttribute));
			}
			dd.selectByVisibleText(strData);
								
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Select Value in dropdown field", strData, "Pass",
					"Value selected successfully", true, webDriver);
		} catch (WebDriverException w1) {
			sFlag = false;
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Select Value in dropdown field", strData, "Fail",
					"Unable to select value", true, webDriver);
		} catch (Exception e1) {
			sFlag = false;
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Select Value in dropdown field", strData, "Fail",
					"Unable to select value", true, webDriver);
		}
		Thread.sleep(3000);
		return sFlag;
	}
	
	
	// Entering name using corporate id
	public void EnterName(Map<String, String> DataMap,
			RemoteWebDriver webDriver, WebDriverWait wait) {
		try {

			String mainwind = webDriver.getWindowHandle();
			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector(".ardbnz3Btn_SearchHPDCustomer .btnimgdiv")));
			clickElementByCSS(".ardbnz3Btn_SearchHPDCustomer .btnimgdiv",
					webDriver);

			Set<String> winids = webDriver.getWindowHandles();
			if (!winids.isEmpty()) {
				for (String windowId : winids) {
					try {

						if (webDriver.switchTo().window(windowId).getTitle()
								.equalsIgnoreCase("People Search")) {

							/*
							 * wait.until(ExpectedConditions.elementToBeClickable
							 * (By.cssSelector(".ardbnCorporateID .text")));
							 * 
							 * enterTextValue("findElementByCss",
							 * ".ardbnCorporateID .text", DataMap,
							 * "CorporateId", webDriver, "New Incident");
							 * 
							 * wait.until(ExpectedConditions.elementToBeClickable
							 * (By.cssSelector(".ardbnz3BtnSearch")));
							 * clickElementByCSS(".ardbnz3BtnSearch",webDriver);
							 * 
							 * 
							 * webDriver.findElementByXPath(
							 * "//table[@id='T301394438']/tbody/tr[2]/td[1]"
							 * ).click();
							 */

							wait.until(ExpectedConditions
									.elementToBeClickable(By
											.cssSelector(".ardbnz3BtnSelect")));
							clickElementByCSS(".ardbnz3BtnSelect", webDriver);
							Thread.sleep(1000);
							break;
						}

					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}

			winids = webDriver.getWindowHandles();

			if (!winids.isEmpty()) {
				for (String windowId : winids) {
					try {
						if (webDriver.switchTo().window(windowId).getTitle()
								.equalsIgnoreCase("People")) {
							wait.until(ExpectedConditions
									.elementToBeClickable(By
											.cssSelector(".ardbnz3BtnClose")));
							clickElementByCSS(".ardbnz3BtnClose", webDriver);
							break;
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
			webDriver.switchTo().window(mainwind);
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Entering name using email id
	public void EnterNameByEmail(Map<String, String> DataMap,
			RemoteWebDriver webDriver, WebDriverWait wait) {
		try {

			/*
			 * wait.until(ExpectedConditions.elementToBeClickable(By
			 * .id("arid_WIN_3_303530000")));
			 * webDriver.findElementById("arid_WIN_3_303530000").clear();
			 * enterTextValue("findElementById", "arid_WIN_3_303530000",
			 * DataMap, "Name", webDriver, "New Incident"); Thread.sleep(5000);
			 */

			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector("#WIN_3_304248190 > div.btnimgdiv")));
			clickElementByCSS("#WIN_3_304248190 > div.btnimgdiv", webDriver);

			String mainwind = null;
			Set<String> winids = webDriver.getWindowHandles();
			int count = winids.size();
			String[] tabwind = new String[count];
			System.out.println(count);
			if (count > 1) {
				Iterator<String> iter = winids.iterator();
				mainwind = iter.next();
				for (int i = 1; i < count; i++) {
					tabwind[i] = iter.next();
					System.out.println("Tabbed window" + tabwind[i]);
				}
			}
			webDriver.switchTo().window(tabwind[1]);
			Thread.sleep(5000);

			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector(".ardbnInternetE-mail .sr")));
			/*enterTextValue("findElementByCss", ".ardbnInternetE-mail .sr",
					DataMap, "CorporateId", webDriver, "New Incident");
			*/// webDriver.findElementById("arid_WIN_0_1000000054").sendKeys("bschaps");

			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector("#WIN_0_301867800>div.btntextdiv>div.f1")));
			clickElementByCSS("#WIN_0_301867800>div.btntextdiv>div.f1",
					webDriver);
			Thread.sleep(3000);
			webDriver.findElementByXPath(
					"//table[@id='T301394438']/tbody/tr[2]/td[1]").click();

			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector("#WIN_0_301912800>div.btntextdiv>div.f1")));
			clickElementByCSS("#WIN_0_301912800>div.btntextdiv>div.f1",
					webDriver);
			Thread.sleep(3000);

			webDriver.switchTo().window(mainwind);
			Thread.sleep(3000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Entering name by first name and last name
	public void EnterNameByFirstNameLastName(Map<String, String> DataMap,
			RemoteWebDriver webDriver, WebDriverWait wait) {
		try {

			/*
			 * wait.until(ExpectedConditions.elementToBeClickable(By
			 * .id("arid_WIN_3_303530000")));
			 * webDriver.findElementById("arid_WIN_3_303530000").clear();
			 * enterTextValue("findElementById", "arid_WIN_3_303530000",
			 * DataMap, "Name", webDriver, "New Incident"); Thread.sleep(5000);
			 */

			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector("#WIN_3_304248190 > div.btnimgdiv")));
			clickElementByCSS("#WIN_3_304248190 > div.btnimgdiv", webDriver);

			String mainwind = null;
			Set<String> winids = webDriver.getWindowHandles();
			int count = winids.size();
			String[] tabwind = new String[count];
			System.out.println(count);
			if (count > 1) {
				Iterator<String> iter = winids.iterator();
				mainwind = iter.next();
				for (int i = 1; i < count; i++) {
					tabwind[i] = iter.next();
					System.out.println("Tabbed window" + tabwind[i]);
				}
			}
			webDriver.switchTo().window(tabwind[1]);
			Thread.sleep(5000);
			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector(".ardbnFirstName .text ")));
			/*enterTextValue("findElementByCss", ".ardbnFirstName .text",
					DataMap, "Customer_FirstName", webDriver, "New Incident");*/
			Thread.sleep(3000);
			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector(".ardbnLastName  .text")));
			/*enterTextValue("findElementByCss", ".ardbnLastName .text", DataMap,
					"Customer_LastName", webDriver, "New Incident");
*/
			// webDriver.findElementById("arid_WIN_0_1000000054").sendKeys("bschaps");

			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector("#WIN_0_301867800>div.btntextdiv>div.f1")));
			clickElementByCSS("#WIN_0_301867800>div.btntextdiv>div.f1",
					webDriver);
			Thread.sleep(3000);
			webDriver.findElementByXPath(
					"//table[@id='T301394438']/tbody/tr[2]/td[1]").click();

			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector("#WIN_0_301912800>div.btntextdiv>div.f1")));
			clickElementByCSS("#WIN_0_301912800>div.btntextdiv>div.f1",
					webDriver);
			Thread.sleep(3000);

			webDriver.switchTo().window(mainwind);
			Thread.sleep(3000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Entering text in text box
	public Boolean enterTextValue(String strDriverMethod, String strAttribute,
			Map<String, String> DataMap, String strKey,
			RemoteWebDriver webDriver, String sAppname) {
		Boolean sFlag = true;
		Map<String, String> dataMapLocal = DataMap;
		String strData = null;
		if (dataMapLocal.containsKey(strKey)) {
			strData = dataMapLocal.get(strKey);
		} else {
			sFlag = false;
			return sFlag;
		} 
		

		try {
			if (strDriverMethod.equals("findElementByName")) {
				webDriver.findElementByName(strAttribute).sendKeys(strData);
			
			} else if (strDriverMethod.equals("findElementById")) {
				webDriver.findElementById(strAttribute).clear();
				webDriver.findElementById(strAttribute).sendKeys(strData);
			} else if (strDriverMethod.equals("findElementByXPath")) {
				webDriver.findElementByXPath(strAttribute).clear();
				webDriver.findElementByXPath(strAttribute).sendKeys(strData);
			}else if (strDriverMethod.equals("findElementByCss")) {

				webDriver.findElementByCssSelector(strAttribute).sendKeys(
						strData);
			}
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Enter Value in text field", strData, "Pass",
					"Value entered successfully", true, webDriver);
		} catch (WebDriverException w1) {
			// String strErrorMessage = w1.getMessage();
			// String [] arrMessages = strErrorMessage.split("(");
			sFlag = false;
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Enter Value in text field", strData, "Fail",
					"Unable to enter value", true, webDriver);
		} catch (Exception e1) {
			sFlag = false;
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Enter Value in text field", strData, "Fail",
					"Unable to enter value", true, webDriver);
		}
		return sFlag;
	}

	public Boolean enterTextValue1(String strDriverMethod, String strAttribute,
			Map<String, String> DataMap, String strKey,
			RemoteWebDriver webDriver, String sAppname) {
		Boolean sFlag = true;
		Map<String, String> dataMapLocal = DataMap;
		String strData = null;
		if (dataMapLocal.containsKey(strKey)) {
			strData = dataMapLocal.get(strKey);
		} else {
			sFlag = false;
			return sFlag;
		} 
		

		try {
			if (strDriverMethod.equals("findElementByName")) {
				webDriver.findElementByName(strAttribute).sendKeys(strData);
			} else if (strDriverMethod.equals("findElementById")) {
				webDriver.findElementById(strAttribute).clear();
				webDriver.findElementById(strAttribute).sendKeys(strData);
			} else if (strDriverMethod.equals("findElementByXPath")) {
				webDriver.findElementById(strAttribute).sendKeys(strData);
			}else if (strDriverMethod.equals("findElementByCss")) {

				webDriver.findElementByCssSelector(strAttribute).sendKeys(
						strData);
			}
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Enter Value in text field", strData, "Pass",
					"Value entered successfully", true, webDriver);
		} catch (WebDriverException w1) {
			// String strErrorMessage = w1.getMessage();
			// String [] arrMessages = strErrorMessage.split("(");
			sFlag = false;
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Enter Value in text field", strData, "Fail",
					"Unable to enter value", true, webDriver);
		} catch (Exception e1) {
			sFlag = false;
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Enter Value in text field", strData, "Fail",
					"Unable to enter value", true, webDriver);
		}
		return sFlag;
	}

	
	// Clicking button
	public void clickButton(String strDriverMethod, String strAttribute,
			RemoteWebDriver webDriver, String sAppname) {

		try {
			if (strDriverMethod.equals("findElementByName")) {
				webDriver.findElementByName(strAttribute).click();
			} else if (strDriverMethod.equals("findElementById")) {
				webDriver.findElementById(strAttribute).click();
			} else if (strDriverMethod.equals("findElementByCss")) {
				webDriver.findElementByCssSelector(strAttribute).click();
			} else if (strDriverMethod.equals("findElementByXPath")) {
				webDriver.findElementByXPath(strAttribute).click();
			}
			reporter.writeStepResult(sAppname.toUpperCase(), "Click on button",
					"", "Pass", "Clicked button successfully", true, webDriver);
		} catch (WebDriverException w1) {
			System.out.println(w1.getMessage());
			System.out.println(w1.getCause());
			System.out.println(w1.getLocalizedMessage());
			reporter.writeStepResult(sAppname.toUpperCase(), "Click on button",
					"", "Fail", w1.getMessage(), true, webDriver);
		} catch (Exception e1) {
			reporter.writeStepResult(sAppname.toUpperCase(), "Click on button",
					"", "Fail", "Not able to click on  button", true, webDriver);
		}
	}

	// Entering text in the second element as the first element is hidden(So we
	// are using a list)
	public WebElement enterValue(String strDriverMethod, String strAttribute,
			String val, RemoteWebDriver webDriver) {
		List<WebElement> list = null;
		try {

			if (strDriverMethod.equals("findElementByCss")) {
				list = webDriver.findElementsByCssSelector(strAttribute);
				list.get(1).sendKeys(val);
			} else if (strDriverMethod.equals("findElementById")) {
				list = webDriver.findElementsById(strAttribute);
				list.get(1).sendKeys(val);
			} else if (strDriverMethod.equals("findElementByName")) {
				list = webDriver.findElementsByName(strAttribute);
				list.get(1).sendKeys(val);
			} else {
				list = webDriver.findElementsByXPath(strAttribute);
				list.get(1).sendKeys(val);
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return list.get(1);
	}

	/*
	 * public WebElement clickButton(String strDriverMethod, String
	 * strAttribute, RemoteWebDriver webDriver) { List<WebElement> list = null;
	 * try {
	 * 
	 * if (strDriverMethod.equals("findElementByCss")) { list =
	 * webDriver.findElementsByCssSelector(strAttribute); list.get(1).click(); }
	 * else if (strDriverMethod.equals("findElementById")) { list =
	 * webDriver.findElementsById(strAttribute); list.get(1).click(); } else if
	 * (strDriverMethod.equals("findElementByName")) { list =
	 * webDriver.findElementsByName(strAttribute); list.get(1).click(); } else {
	 * list = webDriver.findElementsByXPath(strAttribute); list.get(1).click();
	 * }
	 * 
	 * } catch (Exception e1) { e1.printStackTrace(); } return list.get(1); }
	 */

	// Clicking second element found in the list as first button is hidden
	public WebElement clickElementByTwoPresence(String strDriverMethod,
			String strAttribute, RemoteWebDriver webDriver) {
		List<WebElement> list = null;
		Actions builder = new Actions(webDriver);
		try {

			if (strDriverMethod.equals("findElementByCss")) {
				list = webDriver.findElementsByCssSelector(strAttribute);
				builder.moveToElement(list.get(1)).click().build().perform();
			} else if (strDriverMethod.equals("findElementById")) {
				list = webDriver.findElementsById(strAttribute);
				list.get(1).click();
			} else if (strDriverMethod.equals("findElementByName")) {
				list = webDriver.findElementsByName(strAttribute);
				list.get(1).click();
			} else {
				list = webDriver.findElementsByXPath(strAttribute);
				list.get(1).click();
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return list.get(1);
	}

	// Checking SLM status whether it is OnHOLD,Ok or MET
	public boolean checkSLMstatus(String option, Map<String, String> DataMap,
			RemoteWebDriver webDriver) {
		boolean sFlag = false;
		try {
			if (option.equals("ON HOLD")) {
				verify.verifyElementTextPresent(webDriver,
						".ardbnzTrim_PendingOK .f9", "css", option);
				sFlag = true;

			} else if (option.equals("OK")) {
				verify.verifyElementTextPresent(webDriver,
						".ardbnzTrim_OKBar .f9", "css", option);
				sFlag = true;
			} else if (option.equals("MET")) {
				verify.verifyElementTextPresent(webDriver,
						".ardbnzTrim_MetBar .f9", "css", option);
				sFlag = true;
			}
			reporter.writeStepResult(DataMap.get("ListVal1"),
					"SLM status verification", "", "Pass",
					"SLM status verified successfully", true, webDriver);
		} catch (WebDriverException w1) {
			reporter.writeStepResult(DataMap.get("ListVal1"),
					"Click on button", "", "Fail", w1.getMessage(), true,
					webDriver);
		} catch (Exception e) {
			reporter.writeStepResult(DataMap.get("ListVal1"),
					"Click on button", "", "Fail",
					"Not able to click on  button", true, webDriver);
		}
		return sFlag;
	}

	// Changing check box status
	public boolean changeCheckboxStatus(String strDriverMethod,
			String strAttribute, Map<String, String> DataMap, String strKey,
			RemoteWebDriver webDriver, String sAppname) {
		Boolean sFlag = true;
		Map<String, String> dataMapLocal = DataMap;
		String strData = null;
		if (dataMapLocal.containsKey(strKey)) {
			strData = dataMapLocal.get(strKey);
		} else {
			sFlag = false;
			return sFlag;
		}
		try {
			if (strDriverMethod.equals("findElementByName")) {
				if (strData.equalsIgnoreCase("uncheck")
						&& webDriver.findElementByName(strAttribute)
								.isSelected()) {
					webDriver.findElementByName(strAttribute).click();
				}
				if (strData.equalsIgnoreCase("check")
						&& !webDriver.findElementByName(strAttribute)
								.isSelected()) {
					webDriver.findElementByName(strAttribute).click();
				}
			} else if (strDriverMethod.equals("findElementById")) {
				if (strData.equalsIgnoreCase("uncheck")
						&& webDriver.findElementById(strAttribute).isSelected()) {
					webDriver.findElementById(strAttribute).click();
				}
				if (strData.equalsIgnoreCase("check")
						&& !webDriver.findElementById(strAttribute)
								.isSelected()) {
					webDriver.findElementById(strAttribute).click();
				}
			} else {
				if (strData.equalsIgnoreCase("uncheck")
						&& webDriver.findElementByXPath(strAttribute)
								.isSelected()) {
					webDriver.findElementByXPath(strAttribute).click();
				}
				if (strData.equalsIgnoreCase("check")
						&& !webDriver.findElementByXPath(strAttribute)
								.isSelected()) {
					webDriver.findElementByXPath(strAttribute).click();
				}
			}
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Change checkbox status", strData, "Pass", "Checkbox is "
							+ strData.toUpperCase() + "ED", true, webDriver);
		} catch (WebDriverException w1) {
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Change checkbox status", strData, "Fail", w1.getMessage(),
					true, webDriver);
		} catch (Exception e1) {
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Change checkbox status", strData, "Fail",
					"Not able to perform expected action", true, webDriver);
		}
		return sFlag;
	}

	// Changing radio button status
	public boolean changeRadioButtonStatus(String strDriverMethod,
			String strAttribute, Map<String, String> DataMap, String strKey,
			RemoteWebDriver webDriver, String sAppname) {
		Boolean sFlag = true;
		Map<String, String> dataMapLocal = DataMap;
		String strData = null;
		if (dataMapLocal.containsKey(strKey)) {
			strData = dataMapLocal.get(strKey);
		} else {
			sFlag = false;
			return sFlag;
		}
		try {
			if (strDriverMethod.equals("findElementByName")) {
				if (strData.equalsIgnoreCase("deselect")
						&& webDriver.findElementByName(strAttribute)
								.isSelected()) {
					webDriver.findElementByName(strAttribute).click();
				}
				if (strData.equalsIgnoreCase("select")
						&& !webDriver.findElementByName(strAttribute)
								.isSelected()) {
					webDriver.findElementByName(strAttribute).click();
				}
			} else if (strDriverMethod.equals("findElementById")) {
				if (strData.equalsIgnoreCase("deselect")
						&& webDriver.findElementById(strAttribute).isSelected()) {
					webDriver.findElementById(strAttribute).click();
				}
				if (strData.equalsIgnoreCase("select")
						&& !webDriver.findElementById(strAttribute)
								.isSelected()) {
					webDriver.findElementById(strAttribute).click();
				}
			} else {
				if (strData.equalsIgnoreCase("deselect")
						&& webDriver.findElementByXPath(strAttribute)
								.isSelected()) {
					webDriver.findElementByXPath(strAttribute).click();
				}
				if (strData.equalsIgnoreCase("select")
						&& !webDriver.findElementByXPath(strAttribute)
								.isSelected()) {
					webDriver.findElementByXPath(strAttribute).click();
				}
			}
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Change radio button status", strData, "Pass",
					"Radio button is " + strData.toUpperCase() + "ED", true,
					webDriver);
		} catch (WebDriverException w1) {
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Change radio button status", strData, "Fail",
					w1.getMessage(), true, webDriver);
		} catch (Exception e1) {
			reporter.writeStepResult(sAppname.toUpperCase(),
					"Change radio button status", strData, "Fail",
					"Not able to perform expected action ", true, webDriver);
		}
		return sFlag;
	}

	/*
	 * public boolean selectListValue(String strDriverMethod, String
	 * strAttribute, Map <String, String> DataMap, String strKey,
	 * RemoteWebDriver webDriver,String sAppname){ Boolean sFlag = true ; Map
	 * <String, String> dataMapLocal = DataMap; String strData = null ; if
	 * (dataMapLocal.containsKey(strKey)) { strData = dataMapLocal.get(strKey);
	 * } else { sFlag= false; return sFlag; } try{ WebElement elementList1 =
	 * null; if(strDriverMethod.equals("findElementByName")){ elementList1 =
	 * webDriver.findElementByName(strAttribute); }else
	 * if(strDriverMethod.equals("findElementByName")){ elementList1 =
	 * webDriver.findElementById(strAttribute); }else{ elementList1 =
	 * webDriver.findElementByXPath(strAttribute); }
	 * 
	 * List<WebElement> options1 =
	 * elementList1.findElements(By.tagName("option")); String strValue1 =
	 * scriptExecutor.readDataFile(strDataFileName, rowNumber, strElement);
	 * boolean isPresent = false; for(WebElement option : options1){
	 * if(option.getText().equals(strValue1)){ elementList1.click();
	 * option.click(); isPresent = true; break; } } if(isPresent){
	 * reporter.writeStepResult(sAppname.toUpperCase(),
	 * "Select value from Listbox", strData, "Pass",
	 * "Expected value is selected", true, webDriver); }else{
	 * reporter.writeStepResult(sAppname.toUpperCase(),
	 * "Select value from Listbox", strData, "Fail",
	 * "Expected value is not prsenet in the listbox", true, webDriver); }
	 * 
	 * }catch(WebDriverException w1){
	 * reporter.writeStepResult(sAppname.toUpperCase(),
	 * "Select value from Listbox", strData, "Fail", w1.getMessage(), true,
	 * webDriver); }catch(Exception e1){
	 * reporter.writeStepResult(sAppname.toUpperCase(),
	 * "Select value from Listbox", strData, "Fail",
	 * "Not able to select expected value", true, webDriver); }
	 * 
	 * }
	 */

	public void clickImage(String strDriverMethod, String strAttribute,
			RemoteWebDriver webDriver, String sAppname) {

		try {

			if (strDriverMethod.equals("findElementByXPath")) {
				webDriver.findElementByXPath(strAttribute).click();
			} else if (strDriverMethod.equals("findElementById")) {
				webDriver.findElementById(strAttribute).click();
			} else  if (strDriverMethod.equals("findElementByCss")) {
				webDriver.findElementByCssSelector(strAttribute).click() ;
			}else  if (strDriverMethod.equals("findElementByClassName")) {
				webDriver.findElementByCssSelector(strAttribute).click() ;
			}else
			{
				webDriver.findElementByName(strAttribute).click();
			}
			reporter.writeStepResult(sAppname.toUpperCase(), "Click on Image","", "Pass", "Clicked on image successfully", true,webDriver);
		} catch (WebDriverException w1) {
			reporter.writeStepResult(sAppname.toUpperCase(), "Click on Image",
					"", "Fail", w1.getMessage(), true, webDriver);
		} catch (Exception e1) {
			reporter.writeStepResult(sAppname.toUpperCase(), "Click on Image",
					"", "Fail", "Not able to click on Image", true, webDriver);
		}
	}

	public Boolean clickLinkValue(String strDriverMethod, String strAttribute,
			Map<String, String> DataMap, String strKey,
			RemoteWebDriver webDriver, String sAppname) {
		Boolean sFlag = true;
		Map<String, String> dataMapLocal = DataMap;
		String strData = null;
		if (dataMapLocal.containsKey(strKey)) {
			strData = dataMapLocal.get(strKey);
		} else {
			sFlag = false;
			return sFlag;
		} 

		try {

			if (strDriverMethod.equals("findElementByLinkText")) {
				webDriver.findElementByLinkText(strAttribute).click();
			} else if (strDriverMethod.equals("findElementByName")) {
				webDriver.findElementByName(strAttribute).click();
			} else if (strDriverMethod.equals("findElementById")) {
				webDriver.findElementById(strAttribute).click();
			} else  if (strDriverMethod.equals("findElementByXPath")){
				webDriver.findElementByXPath(strAttribute).click();
			}
			reporter.writeStepResult(sAppname.toUpperCase(), "Click on Link",
					"", "Pass", "Clicked on Link successfully", true, webDriver);
		} catch (WebDriverException w1) {
			reporter.writeStepResult(sAppname.toUpperCase(), "Click on Link",
					"", "Fail", w1.getMessage(), true, webDriver);
		} catch (Exception e1) {
			reporter.writeStepResult(sAppname.toUpperCase(), "Click on Link",
					"", "Fail", "Not able to click on Link", true, webDriver);
		}
		return sFlag;
	}

	
	
	public void clickLink(String strDriverMethod, String strAttribute,
			RemoteWebDriver webDriver, String sAppname) {

		try {

			if (strDriverMethod.equals("findElementByLinkText")) {
				webDriver.findElementByLinkText(strAttribute).click();
			} else if (strDriverMethod.equals("findElementByName")) {
				webDriver.findElementByName(strAttribute).click();
			} else if (strDriverMethod.equals("findElementById")) {
				webDriver.findElementById(strAttribute).click();
			} else if (strDriverMethod.equals("findElementByXPath")) {
				webDriver.findElementByXPath(strAttribute).click();
			}
			reporter.writeStepResult(sAppname.toUpperCase(), "Click on Link",
					"", "Pass", "Clicked on Link successfully", true, webDriver);
		} catch (WebDriverException w1) {
			reporter.writeStepResult(sAppname.toUpperCase(), "Click on Link",
					"", "Fail", w1.getMessage(), true, webDriver);
		} catch (Exception e1) {
			reporter.writeStepResult(sAppname.toUpperCase(), "Click on Link",
					"", "Fail", "Not able to click on Link", true, webDriver);
		}
	}

	public void executeSQLQuery(String strQuery) {
		String driverName = null;// "sun.jdbc.odbc.JdbcOdbcDriver"
		String serverName = "127.0.0.1";
		String portNumber = "1521";
		String sid = "mydatabase";
		String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":"
				+ sid;
		Statement stmt = null;

		try {
			Class.forName(driverName); // Or any other driver
		} catch (Exception x) {
			System.out.println("Unable to load the driver class!");
		}

		try {
			Connection dbConnection = DriverManager.getConnection(url,
					"loginName", "Password");
			stmt = dbConnection.createStatement();
			int rows = stmt.executeUpdate(strQuery);
			if (rows > 0) {

			} else {

			}
		} catch (SQLException x) {
			System.out.println("Couldn’t get connection!");
		}

	}

	// Selecting List value by using css Selector
	public boolean selectListValueByCss(String strAttribute,
			Map<String, String> DataMap, String strKey,
			RemoteWebDriver webDriver, String sAppName) {
		/*
		 * String strDetails = utils.getDataFileInfo(); int rowNumber =
		 * executionRowNumber.getExecutionRowNumber(); String strData =
		 * scriptExecutor.readDataFile(strDataFileName, rowNumber, strElement);
		 * String [] arrDetails = strDetails.split("_");
		 */
		boolean sFlag = false;
		Map<String, String> dataMapLocal = DataMap;
		String strData = null;
		if (dataMapLocal.containsKey(strKey)) {
			strData = dataMapLocal.get(strKey);
		} else {

			return sFlag;
		}
		try {
			WebElement elementList1 = null;
			Thread.sleep(500);
			List<WebElement> impactDropdown = webDriver.findElements(By
					.cssSelector(strAttribute));
			boolean isPresent = false;
			for (WebElement dropdownItem : impactDropdown) {
				if (dropdownItem.getText().equals(strData)) {
					/*
					 * Actions builder = new Actions(webDriver);
					 * builder.moveToElement
					 * (dropdownItem).click().build().perform();
					 */

					// dropdownItem.click();
					Locatable hoverItem = (Locatable) dropdownItem;
					Mouse mouse = ((HasInputDevices) webDriver).getMouse();
					Thread.sleep(500);
					mouse.mouseMove(hoverItem.getCoordinates());
					JavascriptExecutor executor = (JavascriptExecutor) webDriver;
					executor.executeScript("arguments[0].click();",
							dropdownItem);
					Thread.sleep(500);
					isPresent = true;
					sFlag = true;
					break;

				}
			}

			if (isPresent) {
				reporter.writeStepResult(sAppName.toUpperCase(),
						"Select value from Listbox", strData, "Pass",
						"Expected value is selected", true, webDriver);
			} else {
				reporter.writeStepResult(sAppName.toUpperCase(),
						"Select value from Listbox", strData, "Fail",
						"Expected value is not prsenet in the listbox", true,
						webDriver);
			}

		} catch (WebDriverException w1) {
			reporter.writeStepResult(sAppName.toUpperCase(),
					"Select value from Listbox", strData, "Fail",
					w1.getMessage(), false, webDriver);
		} catch (Exception e1) {
			reporter.writeStepResult(sAppName.toUpperCase(),
					"Select value from Listbox", strData, "Fail",
					"Not able to select expected value", false, webDriver);
		}
		return sFlag;

	}

	public boolean selectListValueByCss1(String strAttribute,
			Map<String, String> DataMap, String strKey,
			RemoteWebDriver webDriver, String sAppName) {
		/*
		 * String strDetails = utils.getDataFileInfo(); int rowNumber =
		 * executionRowNumber.getExecutionRowNumber(); String strData =
		 * scriptExecutor.readDataFile(strDataFileName, rowNumber, strElement);
		 * String [] arrDetails = strDetails.split("_");
		 */
		boolean sFlag = false;
		Map<String, String> dataMapLocal = DataMap;
		String strData = null;
		if (dataMapLocal.containsKey(strKey)) {
			strData = dataMapLocal.get(strKey);
		} else {

			return sFlag;
		}
		try {
			WebElement elementList1 = null;

			List<WebElement> impactDropdown = webDriver.findElements(By
					.cssSelector(strAttribute));
			boolean isPresent = false;
			for (WebElement dropdownItem : impactDropdown) {
				if (dropdownItem.getText().equals("New Incident")) {
					/*
					 * Actions builder = new Actions(webDriver);
					 * builder.moveToElement
					 * (dropdownItem).click().build().perform();
					 */

					// dropdownItem.click();
					JavascriptExecutor executor = (JavascriptExecutor) webDriver;

					/*
					 * Locatable hoverItem = (Locatable) dropdownItem ; Mouse
					 * mouse = ((HasInputDevices) webDriver).getMouse();
					 * mouse.mouseMove(hoverItem.getCoordinates());
					 */
					// mouse.click(hoverItem.getCoordinates());*/
					executor.executeScript("arguments[0].click();",
							dropdownItem);

					Thread.sleep(500);
					isPresent = true;
					sFlag = true;
					break;

				}
			}

			if (isPresent) {
				reporter.writeStepResult(sAppName.toUpperCase(),
						"Select value from Listbox", strData, "Pass",
						"Expected value is selected", true, webDriver);
			} else {
				reporter.writeStepResult(sAppName.toUpperCase(),
						"Select value from Listbox", strData, "Fail",
						"Expected value is not prsenet in the listbox", true,
						webDriver);
			}

		} catch (WebDriverException w1) {
			reporter.writeStepResult(sAppName.toUpperCase(),
					"Select value from Listbox", strData, "Fail",
					w1.getMessage(), false, webDriver);
		} catch (Exception e1) {
			reporter.writeStepResult(sAppName.toUpperCase(),
					"Select value from Listbox", strData, "Fail",
					"Not able to select expected value", false, webDriver);
		}
		return sFlag;

	}

	// Clicking element by css Selector

	@SuppressWarnings("finally")
	public boolean clickByCss(String strAttribute, RemoteWebDriver webDriver) {
		boolean sFalg = true;
		try {
			/*
			 * WebElement element =
			 * webDriver.findElement(By.cssSelector(strAttribute));
			 * element.click();
			 */

			JavascriptExecutor js = ((JavascriptExecutor) webDriver);
			WebElement element = (WebElement) js.executeScript("return $(\""
					+ strAttribute + "\")[0];");

			/* highlightElement(webDriver, element); */
			element.click();

			/* webDriver.findElementByCssSelector(strAttribute).click(); */

			reporter.writeStepResult(System.getProperty("Test_Scenario_Name"),
					"Click on Element", "", "Pass",
					"Clicked on Element successfully", true, webDriver);

			sFalg = false;
			/*
			 * } catch (WebDriverException w1) {
			 * reporter.writeStepResult("NewChange", "Click on Element", "",
			 * "Fail", w1.getMessage(), false, webDriver);
			 */
		} catch (Exception e1) {
			sFalg = false;
			reporter.writeStepResult(System.getProperty("Test_Scenario_Name"),
					"Click on Element", "", "Fail",
					"Not able to click on Element", false, webDriver);
		} finally {
			return sFalg;
		}

	}

	// Select Menu Item using css Selector
	public boolean SelectMenuItem(String sCssName, Map<String, String> DataMap,
			String strKey, RemoteWebDriver webDriver) {

		// Get the Elements to Click
		boolean sFlag = false;
		Map<String, String> dataMapLocal = DataMap;
		String strData = null;
		if (dataMapLocal.containsKey(strKey)) {
			strData = dataMapLocal.get(strKey);
		} else {

			return sFlag;
		}
		String[] Elements = strData.split(",");
		int iLen = Elements.length;

		for (int i = 0; i < iLen; i++) {
			sFlag = SelectInternalMenuItem(sCssName, Elements[i], DataMap,
					webDriver);
			if (sFlag == false) {
				reporter.writeStepResult(DataMap.get("ListVal1"),
						"Select Menu item", "", "Fail",
						"Not able to select Menu item '" + DataMap.get(strKey)
								+ "'", true, webDriver);
				// webDriver.findElementById("WIN_0_301583700").click();

				break;
			}

		}
		return sFlag;
	}

	@SuppressWarnings("finally")
	public boolean SelectInternalMenuItem(String sCssName, String sSelection,
			Map<String, String> DataMap, RemoteWebDriver webDriver) {
		boolean bFlag = false;
		JavascriptExecutor executor = (JavascriptExecutor) webDriver;

		try {
			Boolean bNeedSelection = false;
			int selectionNumber = 1;
			int conter = 1;
			String[] aOptionSelection = sSelection.split("@");
			if (aOptionSelection.length > 1) {
				sSelection = aOptionSelection[0];
				bNeedSelection = true;
				selectionNumber = Integer.parseInt(aOptionSelection[1]);
			}

			List<WebElement> applicationMenuItems = webDriver.findElements(By
					.cssSelector(sCssName));
			for (WebElement menuItem : applicationMenuItems) {
				if (menuItem.getText().equals(sSelection)) {
					if (bNeedSelection) {
						if (conter == selectionNumber) {
							// executor.executeScript("arguments[0].click();",
							// menuItem);
							menuItem.click();
							bFlag = true;
							break;
						} else {
							conter = conter + 1;
						}
					} else {
						menuItem.click();
						// executor.executeScript("arguments[0].click();",
						// menuItem);
						bFlag = true;
						break;
					}
				}

			}
		} catch (WebDriverException w1) {
			reporter.writeStepResult(DataMap.get("ListVal1"),
					"Select Menu item", "", "Fail", w1.getMessage(), true,
					webDriver);
		} catch (Exception e1) {
			reporter.writeStepResult(DataMap.get("ListVal1"),
					"Select Menu item", "", "Fail",
					"Not able to select Menu item '" + sSelection + "'", true,
					webDriver);
		} finally {
			return bFlag;
		}
	}

	public void highlightElement(RemoteWebDriver driver, WebElement element) {
		for (int i = 0; i < 2; i++) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(
					"arguments[0].setAttribute('style', arguments[1]);",
					element, "color: yellow; border: 2px solid yellow;");
			js.executeScript(
					"arguments[0].setAttribute('style', arguments[1]);",
					element, "");
		}
	} // - See more at:
		// http://selenium.polteq.com/en/highlight-elements-with-selenium-webdriver/#sthash.qYdW9TYC.dpuf

	public void SelectIncidentCategorization(RemoteWebDriver webDriver,
			Map<String, String> DataMap, FluentWait<WebDriver> wait)
			throws InterruptedException {

		wait.until(ExpectedConditions.elementToBeClickable(By
				.xpath("//a[.='Categorization']")));
		clickButton("findElementByXPath", "//a[.='Categorization']", webDriver,
				"NEW CHANGE");

		String sExpectedProductCategorizationTier1 = DataMap.get("PrTier1");
		String sExpectedProductCategorizationTier2 = DataMap.get("PrTier2");
		String sExpectedProductCategorizationTier3 = DataMap.get("PrTier3");
		String sExpectedCategorizationTier1 = DataMap.get("OpTier1");
		String sExpectedCategorizationTier2 = DataMap.get("OpTier2");
		String sExpectedCategorizationTier3 = DataMap.get("OpTier3");

		// Operation Cat Tier 1
		if (!sExpectedCategorizationTier1.equals("")) {
			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector(".ardbnCategorizationTier1 .btn3d")));
			clickElementByCSS(".ardbnCategorizationTier1 .btn3d", webDriver);
			Thread.sleep(1000);
			if (SelectMenuItem(".MenuEntryName", DataMap, "OpTier1", webDriver)) {
				reporter.writeStepResult("NEWCHANGE", "OpTier1 selection",
						DataMap.get("OpTier1"), "Pass",
						"OpTier1 selected successfully", true, webDriver);
			}
		}
		// Operation Cat Tier 2
		if (!sExpectedCategorizationTier2.equals("")) {
			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector(".ardbnCategorizationTier2 .btn3d")));
			clickElementByCSS(".ardbnCategorizationTier2 .btn3d", webDriver);
			Thread.sleep(1000);
			if (SelectMenuItem(".MenuEntryName", DataMap, "OpTier2", webDriver)) {
				reporter.writeStepResult("NEWCHANGE", "OpTier2 selection",
						DataMap.get("OpTier2"), "Pass",
						"OpTier2 selected successfully", true, webDriver);
			}
		}
		// Operation Cat Tier 3
		if (!sExpectedCategorizationTier3.equals("")) {
			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector(".ardbnCategorizationTier3 .btn3d")));
			clickElementByCSS(".ardbnCategorizationTier3 .btn3d", webDriver);
			Thread.sleep(1000);
			if (SelectMenuItem(".MenuEntryName", DataMap, "OpTier3", webDriver)) {
				reporter.writeStepResult("NEWCHANGE", "OpTier3 selection",
						DataMap.get("OpTier3"), "Pass",
						"OpTier3 selected successfully", true, webDriver);
			}
		}
		// Product Cat Tier 1
		if (!sExpectedProductCategorizationTier1.equals("")) {
			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector(".ardbnProductCategorizationTier1 .btn3d")));
			clickElementByCSS(".ardbnProductCategorizationTier1 .btn3d",
					webDriver);
			Thread.sleep(1000);
			if (SelectMenuItem(".MenuEntryName", DataMap, "PrTier1", webDriver)) {
				reporter.writeStepResult("NEWCHANGE", "PrTier1 selection",
						DataMap.get("PrTier1"), "Pass",
						"PrTier1 selected successfully", true, webDriver);
			}
		}
		// Product Cat Tier 2
		if (!sExpectedProductCategorizationTier2.equals("")) {
			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector(".ardbnProductCategorizationTier2 .btn3d")));
			clickElementByCSS(".ardbnProductCategorizationTier2 .btn3d",
					webDriver);
			Thread.sleep(1000);
			if (SelectMenuItem(".MenuEntryName", DataMap, "PrTier2", webDriver)) {
				reporter.writeStepResult("NEWCHANGE", "PrTier2 selection",
						DataMap.get("PrTier2"), "Pass",
						"PrTier2 selected successfully", true, webDriver);
			}
		}
		// Product Cat Tier 3
		if (!sExpectedProductCategorizationTier3.equals("")) {
			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector(".ardbnProductCategorizationTier3 .btn3d")));
			clickElementByCSS(".ardbnProductCategorizationTier3 .btn3d",
					webDriver);
			Thread.sleep(1000);
			if (SelectMenuItem(".MenuEntryName", DataMap, "PrTier3", webDriver)) {
				reporter.writeStepResult("NEWCHANGE", "PrTier3 selection",
						DataMap.get("PrTier3"), "Pass",
						"PrTier3 selected successfully", true, webDriver);
			}
		}

		// Product
		if (!DataMap.get("Product").equals("")) {
			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector(".ardbnProductName .btn3d")));
			clickElementByCSS(".ardbnProductName .btn3d", webDriver);
			Thread.sleep(1000);
			if (SelectMenuItem(".MenuEntryName", DataMap, "Product", webDriver)) {
				reporter.writeStepResult("NEWCHANGE", "Product selection",
						DataMap.get("Product"), "Pass",
						"Product selected successfully", true, webDriver);
			}
		}

		// Environment
		if (!DataMap.get("Environment").equals("")) {
			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector(".ardbnEnvironment .btn3d")));
			clickElementByCSS(".ardbnEnvironment .btn3d", webDriver);
			Thread.sleep(1000);
			if (SelectMenuItem(".MenuEntryName", DataMap, "Environment",
					webDriver)) {
				reporter.writeStepResult("NEWCHANGE", "Environment selection",
						DataMap.get("Environment"), "Pass",
						"Environment selected successfully", true, webDriver);
			}
		}
	}

	public void SelectResolutionIncidentCategorization(
			RemoteWebDriver webDriver, Map<String, String> DataMap,
			WebDriverWait wait) {
		try {

			clickLink("findElementByPartialLinkText",
					"Resolution Categorization", webDriver, "NEW INCIDENT");
			String ResolutionCategorizationTier1 = DataMap.get("ResTier1");
			String ResolutionCategorizationTier2 = DataMap.get("ResTier2");
			String ResolutionCategorizationTier3 = DataMap.get("ResTier3");

			// Resolution Cat Tier 1
			if (!ResolutionCategorizationTier1.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnResolutionCategory .btn3d")));
				clickElementByCSS(".ardbnResolutionCategory .btn3d", webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "ResTier1",
						webDriver)) {
					reporter.writeStepResult("NEWCHANGE",
							"Resolution Tier1 selection",
							DataMap.get("ResTier1"), "Pass",
							"Resolution Tier1 selected successfully", true,
							webDriver);
				}
			}

			// Resolution Cat Tier 2
			if (!ResolutionCategorizationTier2.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnResolutionCategoryTier2 .btn3d")));
				clickElementByCSS(".ardbnResolutionCategoryTier2 .btn3d",
						webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "ResTier2",
						webDriver)) {
					reporter.writeStepResult("NEWCHANGE",
							"Resolution Tier2 selection",
							DataMap.get("ResTier2"), "Pass",
							"Resolution Tier2 selected successfully", true,
							webDriver);
				}
			}
			// Resolution Cat Tier 3
			if (!ResolutionCategorizationTier3.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnResolutionCategoryTier3 .btn3d")));

				clickElementByCSS(".ardbnResolutionCategoryTier3 .btn3d",
						webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "ResTier3",
						webDriver)) {
					reporter.writeStepResult("NEWCHANGE",
							"Resolution Tier3 selection",
							DataMap.get("ResTier3"), "Pass",
							"Resolution Tier3 selected successfully", true,
							webDriver);
				}
			}

			String ResolutionPrCategorizationTier1 = DataMap
					.get("ResProductTier1");
			String ResolutionPrCategorizationTier2 = DataMap
					.get("ResProductTier2");
			String ResolutionPrCategorizationTier3 = DataMap
					.get("ResProductTier3");
			// Resolution Product Cat Tier 1
			if (!ResolutionPrCategorizationTier1.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnClosureProductCategoryTier1 .btn3d")));

				clickElementByCSS(".ardbnClosureProductCategoryTier1 .btn3d",
						webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap,
						"ResProductTier1", webDriver)) {
					reporter.writeStepResult("NEWCHANGE",
							"Resolution Product Tier1 selection",
							DataMap.get("ResProductTier1"), "Pass",
							"Resolution Product Tier1 selected successfully",
							true, webDriver);
				}
			}

			// Resolution Product Cat Tier 2
			if (!ResolutionPrCategorizationTier2.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnClosureProductCategoryTier2 .btn3d")));
				clickElementByCSS(".ardbnClosureProductCategoryTier2 .btn3d",
						webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap,
						"ResProductTier2", webDriver)) {
					reporter.writeStepResult("NEWCHANGE",
							"Resolution Product Tier2 selection",
							DataMap.get("ResProductTier2"), "Pass",
							"Resolution Product Tier2 selected successfully",
							true, webDriver);
				}
			}

			// Resolution Product Cat Tier 3
			if (!ResolutionPrCategorizationTier3.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnClosureProductCategoryTier3 .btn3d")));

				clickElementByCSS(".ardbnClosureProductCategoryTier3 .btn3d",
						webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap,
						"ResProductTier3", webDriver)) {
					reporter.writeStepResult("NEWCHANGE",
							"Resolution Product Tier3 selection",
							DataMap.get("ResProductTier3"), "Pass",
							"Resolution Product Tier3 selected successfully",
							true, webDriver);
				}
			}

			// Resolution Method
			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector(".ardbnResolutionMethod .btn3d")));
			clickElementByCSS(".ardbnResolutionMethod .btn3d", webDriver);
			Thread.sleep(1000);
			if (SelectMenuItem(".MenuEntryName", DataMap, "ResolutionMethod",
					webDriver)) {
				reporter.writeStepResult("NEWCHANGE", "Cause selection",
						DataMap.get("ResolutionMethod"), "Pass",
						"ResolutionMethod selected successfully", true,
						webDriver);
			}

			// Enter Cause
			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector(".ardbnGenericCategorizationTier1 .btn3d")));
			clickElementByCSS(".ardbnGenericCategorizationTier1 .btn3d",
					webDriver);
			Thread.sleep(1000);
			if (SelectMenuItem(".MenuEntryName", DataMap, "Cause", webDriver)) {
				reporter.writeStepResult("NEWCHANGE", "Cause selection",
						DataMap.get("Cause"), "Pass",
						"Cause selected successfully", true, webDriver);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SelectChangeCategorization(RemoteWebDriver webDriver,
			Map<String, String> DataMap, WebDriverWait wait) {
		try {

			String sExpectedProductCategorizationTier1 = DataMap.get("PrTier1");
			String sExpectedProductCategorizationTier2 = DataMap.get("PrTier2");
			String sExpectedProductCategorizationTier3 = DataMap.get("PrTier3");
			String sExpectedCategorizationTier1 = DataMap.get("OpTier1");
			String sExpectedCategorizationTier2 = DataMap.get("OpTier2");
			String sExpectedCategorizationTier3 = DataMap.get("OpTier3");

			// Operation Cat Tier 1
			if (!sExpectedCategorizationTier1.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnCategorizationTier1 .btn3d")));
				clickElementByCSS(".ardbnCategorizationTier1 .btn3d", webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "OpTier1",
						webDriver)) {
					reporter.writeStepResult("NEWCHANGE", "OpTier1 selection",
							DataMap.get("OpTier1"), "Pass",
							"OpTier1 selected successfully", true, webDriver);
				}
			}
			// Operation Cat Tier 2
			if (!sExpectedCategorizationTier2.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnCategorizationTier2 .btn3d")));
				clickElementByCSS(".ardbnCategorizationTier2 .btn3d", webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "OpTier2",
						webDriver)) {
					reporter.writeStepResult("NEWCHANGE", "OpTier2 selection",
							DataMap.get("OpTier2"), "Pass",
							"OpTier2 selected successfully", true, webDriver);
				}
			}
			// Operation Cat Tier 3
			if (!sExpectedCategorizationTier3.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnCategorizationTier3 .btn3d")));
				clickElementByCSS(".ardbnCategorizationTier3 .btn3d", webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "OpTier3",
						webDriver)) {
					reporter.writeStepResult("NEWCHANGE", "OpTier3 selection",
							DataMap.get("OpTier3"), "Pass",
							"OpTier3 selected successfully", true, webDriver);
				}
			}
			// Product Cat Tier 1
			if (!sExpectedProductCategorizationTier1.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnProductCatTier1 .btn3d")));
				clickElementByCSS(".ardbnProductCatTier1 .btn3d", webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "PrTier1",
						webDriver)) {
					reporter.writeStepResult("NEWCHANGE", "PrTier1 selection",
							DataMap.get("PrTier1"), "Pass",
							"PrTier1 selected successfully", true, webDriver);
				}
			}
			// Product Cat Tier 2
			if (!sExpectedProductCategorizationTier2.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnProductCatTier2 .btn3d")));
				clickElementByCSS(".ardbnProductCatTier2 .btn3d", webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "PrTier2",
						webDriver)) {
					reporter.writeStepResult("NEWCHANGE", "PrTier2 selection",
							DataMap.get("PrTier2"), "Pass",
							"PrTier2 selected successfully", true, webDriver);
				}
			}
			// Product Cat Tier 3
			if (!sExpectedProductCategorizationTier3.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnProductCatTier3 .btn3d")));
				clickElementByCSS(".ardbnProductCatTier3 .btn3d", webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "PrTier3",
						webDriver)) {
					reporter.writeStepResult("NEWCHANGE", "PrTier3 selection",
							DataMap.get("PrTier3"), "Pass",
							"PrTier3 selected successfully", true, webDriver);
				}
			}

			// Select Product
			String Product = DataMap.get("Product");
			if (!Product.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".arfid1000002268 .btn3d")));
				clickElementByCSS(".arfid1000002268 .btn3d", webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "Product",
						webDriver)) {
					reporter.writeStepResult("NEWCHANGE", "Product selection",
							DataMap.get("Product"), "Pass",
							"Product selected successfully", true, webDriver);
				}

			}

			// Verify Manufacturer
			String Manufacturer = DataMap.get("Manufacturer");
			if (!Manufacturer.equals("")) {
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.cssSelector(".arfid1000002270 .text")));
				verify.verifyElementTextPresent(webDriver,
						".arfid1000002270 .text", "css", Manufacturer);
			}

			// Select Environment
			String Environment = DataMap.get("Environment");
			if (!Environment.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnEnvironment .btn3d")));
				clickElementByCSS(".ardbnEnvironment .btn3d ", webDriver);
				if (SelectMenuItem(".MenuEntryName", DataMap, "Environment",
						webDriver)) {
					reporter.writeStepResult("NEWCHANGE",
							"Environment selection",
							DataMap.get("Environment"), "Pass",
							"Environment selected successfully", true,
							webDriver);
				}
			}

			// Select BusinessJustification
			String BusinessJustification = DataMap.get("BusinessJustification");
			if (!BusinessJustification.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnBusinessJustification .btn3d")));
				clickElementByCSS(".ardbnBusinessJustification .btn3d ",
						webDriver);
				if (SelectMenuItem(".MenuEntryName", DataMap,
						"BusinessJustification", webDriver)) {
					reporter.writeStepResult("NEWCHANGE",
							"BusinessJustification selection",
							DataMap.get("BusinessJustification"), "Pass",
							"Business Justification selected successfully",
							true, webDriver);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SelectProblemCategorization(RemoteWebDriver webDriver,
			Map<String, String> DataMap, WebDriverWait wait) {
		try {

			// Links
			wait.until(ExpectedConditions.elementToBeClickable(By
					.xpath("//a[.='Categorization']")));
			clickButton("findElementByXPath", "//a[.='Categorization']",
					webDriver, "NEW CHANGE");

			String sExpectedProductCategorizationTier1 = DataMap.get("PrTier1");
			String sExpectedProductCategorizationTier2 = DataMap.get("PrTier2");
			String sExpectedProductCategorizationTier3 = DataMap.get("PrTier3");
			String sExpectedCategorizationTier1 = DataMap.get("OpTier1");
			String sExpectedCategorizationTier2 = DataMap.get("OpTier2");
			String sExpectedCategorizationTier3 = DataMap.get("OpTier3");

			// Operation Cat Tier 1
			if (!sExpectedCategorizationTier1.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnCategorizationTier1 .btn3d")));
				clickElementByCSS(".ardbnCategorizationTier1 .btn3d", webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "OpTier1",
						webDriver)) {
					reporter.writeStepResult("NEWCHANGE", "OpTier1 selection",
							DataMap.get("OpTier1"), "Pass",
							"OpTier1 selected successfully", true, webDriver);
				}
			}
			// Operation Cat Tier 2
			if (!sExpectedCategorizationTier2.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnCategorizationTier2 .btn3d")));
				clickElementByCSS(".ardbnCategorizationTier2 .btn3d", webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "OpTier2",
						webDriver)) {
					reporter.writeStepResult("NEWCHANGE", "OpTier2 selection",
							DataMap.get("OpTier2"), "Pass",
							"OpTier2 selected successfully", true, webDriver);
				}
			}
			// Operation Cat Tier 3
			if (!sExpectedCategorizationTier3.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnCategorizationTier3 .btn3d")));
				clickElementByCSS(".ardbnCategorizationTier3 .btn3d", webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "OpTier3",
						webDriver)) {
					reporter.writeStepResult("NEWCHANGE", "OpTier3 selection",
							DataMap.get("OpTier3"), "Pass",
							"OpTier3 selected successfully", true, webDriver);
				}
			}
			// Product Cat Tier 1
			if (!sExpectedProductCategorizationTier1.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnProductCategorizationTier1 .btn3d")));
				clickElementByCSS(".ardbnProductCategorizationTier1 .btn3d",
						webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "PrTier1",
						webDriver)) {
					reporter.writeStepResult("NEWCHANGE", "PrTier1 selection",
							DataMap.get("PrTier1"), "Pass",
							"PrTier1 selected successfully", true, webDriver);
				}
			}
			// Product Cat Tier 2
			if (!sExpectedProductCategorizationTier2.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnProductCategorizationTier2 .btn3d")));
				clickElementByCSS(".ardbnProductCategorizationTier2 .btn3d",
						webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "PrTier2",
						webDriver)) {
					reporter.writeStepResult("NEWCHANGE", "PrTier2 selection",
							DataMap.get("PrTier2"), "Pass",
							"PrTier2 selected successfully", true, webDriver);
				}
			}
			// Product Cat Tier 3
			if (!sExpectedProductCategorizationTier3.equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnProductCategorizationTier3 .btn3d")));
				clickElementByCSS(".ardbnProductCategorizationTier3 .btn3d",
						webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "PrTier3",
						webDriver)) {
					reporter.writeStepResult("NEWCHANGE", "PrTier3 selection",
							DataMap.get("PrTier3"), "Pass",
							"PrTier3 selected successfully", true, webDriver);
				}
			}

			// Product Name
			if (!DataMap.get("ProductName").equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnProductName .btn3d")));
				clickElementByCSS(".ardbnProductName .btn3d", webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "ProductName",
						webDriver)) {
					reporter.writeStepResult("NEWINCIDENT",
							"Product Name selection",
							DataMap.get("ProductName"), "Pass",
							"Product Name selected successfully", true,
							webDriver);
				}
			}

			// Verify Manufacturer
			String Manufacturer = DataMap.get("Manufacturer");
			if (!Manufacturer.equals("")) {
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.cssSelector(".ardbnManufacturer .text")));
				verify.verifyElementTextPresent(webDriver,
						".ardbnManufacturer .text", "css", Manufacturer);
			}

			// Root Cause
			if (!DataMap.get("RootCause").equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".ardbnGenericCategorizationTier1 .btn3d")));
				clickElementByCSS(".ardbnGenericCategorizationTier1 .btn3d",
						webDriver);
				Thread.sleep(1000);
				if (SelectMenuItem(".MenuEntryName", DataMap, "RootCause",
						webDriver)) {
					reporter.writeStepResult("NEWINCIDENT",
							"Root Cause selection", DataMap.get("RootCause"),
							"Pass", "Root Cause selected successfully", true,
							webDriver);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectProblemLocation(Map<String, String> DataMap,
			RemoteWebDriver webDriver, WebDriverWait wait) {

		try {

			String mainwind = webDriver.getWindowHandle();
			// Click on Company Location
			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector(".ardbnz3Btn_PbmLocationLookUp .btnimgdiv")));
			clickElementByCSS(".ardbnz3Btn_PbmLocationLookUp .btnimgdiv",
					webDriver);

			Thread.sleep(2000);
			Set<String> winids = webDriver.getWindowHandles();
			if (!winids.isEmpty()) {
				for (String windowId : winids) {
					try {
						if (webDriver.switchTo().window(windowId).getTitle()
								.equalsIgnoreCase("Site Details")) {

							// Select Company
							wait.until(ExpectedConditions.elementToBeClickable(By
									.cssSelector(".ardbnCompany .btn3d")));
							clickElementByCSS(".ardbnCompany .btn3d", webDriver);
							Thread.sleep(1000);
							if (SelectMenuItem(".MenuEntryName", DataMap,
									"Company", webDriver)) {
								reporter.writeStepResult("NEWPROBLEM",
										"Company selection",
										DataMap.get("Company"), "Pass",
										"Company selected successfully", true,
										webDriver);
							}

							// Select Site
							String Site = DataMap.get("Site");
							if (!Site.equals("")) {
								wait.until(ExpectedConditions.elementToBeClickable(By
										.cssSelector(".ardbnSite .btn3d")));
								clickElementByCSS(".ardbnSite .btn3d",
										webDriver);
								Thread.sleep(500);
								if (SelectMenuItem(".MenuEntryName", DataMap,
										"Site", webDriver)) {
									reporter.writeStepResult("NEWPROBLEM",
											"Site selection",
											DataMap.get("Site"), "Pass",
											"Site selected successfully", true,
											webDriver);
								}
							}

							// CLick OK
							wait.until(ExpectedConditions.elementToBeClickable(By
									.cssSelector(".ardbnz3BtnOK .btntextdiv .f1")));
							clickElementByCSS(".ardbnz3BtnOK .btntextdiv .f1",
									webDriver);
							break;
						}
					} catch (Exception e) {

					}
				}

			}
			// Switching to main window
			webDriver.switchTo().window(mainwind);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void ChangeLocation(RemoteWebDriver webDriver,
			Map<String, String> DataMap, WebDriverWait wait)
			throws InterruptedException {
		String mainwind = webDriver.getWindowHandle();
		// Click on Company Location
		wait.until(ExpectedConditions.elementToBeClickable(By
				.cssSelector(".ardbnz3Btn_ChgLocationLookUp .btnimgdiv")));
		clickElementByCSS(".ardbnz3Btn_ChgLocationLookUp .btnimgdiv", webDriver);
		Thread.sleep(1000);

		Set<String> winids = webDriver.getWindowHandles();
		if (!winids.isEmpty()) {
			for (String windowId : winids) {
				try {
					System.out.println(webDriver.switchTo().window(windowId)
							.getTitle());
					if (webDriver.switchTo().window(windowId).getTitle()
							.equalsIgnoreCase("Site Details")) {
						Thread.sleep(1000);

						// Select Company
						wait.until(ExpectedConditions.elementToBeClickable(By
								.cssSelector(".ardbnCompany .btn3d")));
						clickElementByCSS(".ardbnCompany .btn3d", webDriver);
						Thread.sleep(1000);
						if (SelectMenuItem(".MenuEntryName", DataMap,
								"Company", webDriver)) {
							reporter.writeStepResult("NEWCHANGE",
									"Company selection",
									DataMap.get("Company"), "Pass",
									"Company selected successfully", true,
									webDriver);
						}

						// Select Site
						String Site = DataMap.get("Site");
						if (!Site.equals("")) {
							wait.until(ExpectedConditions
									.elementToBeClickable(By
											.cssSelector(".ardbnSite .btn3d")));
							clickElementByCSS(".ardbnSite .btn3d", webDriver);
							Thread.sleep(1000);
							if (SelectMenuItem(".MenuEntryName", DataMap,
									"Site", webDriver)) {
								reporter.writeStepResult("NEWCHANGE",
										"Site selection", DataMap.get("Site"),
										"Pass", "Site selected successfully",
										true, webDriver);
							}
						}

						// CLick OK
						wait.until(ExpectedConditions.elementToBeClickable(By
								.cssSelector(".ardbnz3BtnOK .btntextdiv .f1")));
						clickElementByCSS(".ardbnz3BtnOK .btntextdiv .f1",
								webDriver);
						break;
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		// Switching to main window
		webDriver.switchTo().window(mainwind);

	}

	public void VerifyAlert(RemoteWebDriver webDriver) {
		try {
			Thread.sleep(2000);
			Alert alert = webDriver.switchTo().alert();
			// alert.accept();
			if (alert != null) {
				alert.dismiss();
				webDriver.navigate().refresh();
			}
			webDriver.switchTo().defaultContent();
		}

		catch (Exception e) {
			webDriver.switchTo().defaultContent();
		}
	}

	public void clickElementByCSS(String strAttribute, RemoteWebDriver webDriver) {
		/*
		 * String strDetails = utils.getDataFileInfo(); String [] arrDetails =
		 * strDetails.split("_");
		 */
		/*
		 * WebElement element=webDriver.findElementByCssSelector(strAttribute);
		 * Actions action = new Actions(webDriver);
		 * action.moveToElement(element).click().perform();
		 */

		JavascriptExecutor js = ((JavascriptExecutor) webDriver);
		WebElement element = webDriver.findElementByCssSelector(strAttribute);

		/* highlightElement(webDriver, element); */
		js.executeScript("arguments[0].click();", element);

		/*
		 * WebElement element =
		 * webDriver.findElementByCssSelector(strAttribute);
		 * 
		 * JavascriptExecutor executor = (JavascriptExecutor) webDriver;
		 * executor.executeScript("arguments[0].click();", element);
		 */
		// webDriver.findElementByCssSelector(strAttribute).click();

		/*
		 * boolean sFalg = true;
		 * 
		 * while (sFalg) {
		 * 
		 * sFalg = clickByCss(strAttribute, webDriver);
		 * 
		 * }
		 */
	}

	public void VerifyFrame(RemoteWebDriver webDriver) {
		/*
		 * try{ Thread.sleep(2000); List<WebElement> frames =
		 * webDriver.findElementsByTagName("iframe"); if(frames.size()>0){
		 * webDriver.switchTo().frame(0);
		 * webDriver.findElementByXPath("//a[.='OK']").click(); }
		 * Thread.sleep(1000); webDriver.switchTo().defaultContent(); }
		 * 
		 * catch(Exception e){ System.out.println(); }
		 */
		try {
			List<WebElement> frames = webDriver.findElementsByTagName("iframe");
			if (frames.size() > 0) {
				webDriver.switchTo().frame(1);
				try {
					/*
					 * if
					 * (webDriver.findElementByCssSelector(".ardbnText .trimJustc"
					 * ).getText().equals(
					 * "Please select whether Off Pending Date is Required")){
					 * webDriver
					 * .findElementByXPath("//fieldset/div/span[2]/label"
					 * ).click();
					 * webDriver.findElementByCssSelector(".ardbnOk .f1"
					 * ).click(); Thread.sleep(4000); }
					 */

					webDriver.findElementByCssSelector(".ardbnOk .f1").click();
					Thread.sleep(4000);
				} catch (Exception ex) {
					// System.out.println();
				}
			}

			webDriver.switchTo().defaultContent();
			frames = webDriver.findElementsByTagName("iframe");

			if (frames.size() > 0) {
				webDriver.switchTo().frame(1);
				try {
					webDriver.findElementByXPath("//a[.='Yes']").click();
				} catch (Exception ex) {
					// System.out.println();
				}
			}

			Thread.sleep(1000);
			webDriver.switchTo().defaultContent();
		} catch (Exception e) {
			// System.out.println();
		} finally {
			VerifyAlert(webDriver);
			webDriver.switchTo().defaultContent();
		}
	}

	public WebElement moveToElementByTwoPresence(String strDriverMethod,
			String strAttribute, RemoteWebDriver webDriver) {
		Actions builder = new Actions(webDriver);

		List<WebElement> list = null;
		try {

			if (strDriverMethod.equals("findElementByCss")) {
				list = webDriver.findElementsByCssSelector(strAttribute);
				builder.moveToElement(list.get(1)).build().perform();

			} else if (strDriverMethod.equals("findElementById")) {
				list = webDriver.findElementsById(strAttribute);
				builder.moveToElement(list.get(1)).build().perform();
			} else if (strDriverMethod.equals("findElementByName")) {
				list = webDriver.findElementsByName(strAttribute);
				builder.moveToElement(list.get(1)).build().perform();
			} else {
				list = webDriver.findElementsByXPath(strAttribute);
				builder.moveToElement(list.get(1)).build().perform();
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return list.get(1);
	}

	public WebElement moveToElement(String strDriverMethod,
			String strAttribute, RemoteWebDriver webDriver) {
		// TODO Auto-generated method stub
		Actions builder = new Actions(webDriver);
		WebElement ele = null;

		try {

			if (strDriverMethod.equals("findElementByCss")) {
				ele = webDriver.findElementByCssSelector(strAttribute);
				builder.moveToElement(ele).click().build().perform();

			} else if (strDriverMethod.equals("findElementById")) {
				ele = webDriver.findElementById(strAttribute);
				builder.moveToElement(ele).click().build().perform();
			} else if (strDriverMethod.equals("findElementByName")) {
				ele = webDriver.findElementByName(strAttribute);
				builder.moveToElement(ele).click().build().perform();
			} else if (strDriverMethod.equals("findElementByLinkText")) {
				ele = webDriver.findElementByLinkText(strAttribute);
				builder.moveToElement(ele).click().build().perform();
			}

			else {
				ele = webDriver.findElementByXPath(strAttribute);
				builder.moveToElement(ele).click().build().perform();
			}
		

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return ele;
	}
	
	public void clickMenu(String strDataFileName, String strColumnName, Selenium selenium){
		String strDetails = utils.getDataFileInfo();
		int rowNumber = executionRowNumber.getExecutionRowNumber();
		String strData = scriptExecutor.readDataSheetFile(strDataFileName, rowNumber, strColumnName);
		String [] arrDetails = strDetails.split("_");
		String [] strMenuItems = strData.split(";");
		int totalMenuItems = strMenuItems.length;
		try{
			for(int i = 0; i < totalMenuItems; i++){
				selenium.mouseOver(strMenuItems[i]);
				Thread.sleep(500);
			}
			selenium.click(strMenuItems[totalMenuItems -1]);
			reporter.writeStepResult(arrDetails[1].toUpperCase(), "Click on Menu", strData, "Pass", "Click on expecetd menu item successfully", true, selenium);	
		}catch(SeleniumException sel1){
			reporter.writeStepResult(arrDetails[1].toUpperCase(), "Click on Menu", strData, "Fail", sel1.getMessage(), true, selenium);
		}
		catch(Exception e1){
			reporter.writeStepResult(arrDetails[1].toUpperCase(), "Click on Menu", strData, "Fail", "Not able to click on Menu", true, selenium);
		}
	}

	public void  getWhenVisible(By locator, int timeout,RemoteWebDriver webDriver ) {
		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(webDriver, timeout);
		element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		//return element;
		}

	
	
}
