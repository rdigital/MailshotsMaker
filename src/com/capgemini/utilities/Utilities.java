package com.capgemini.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

import net.sourceforge.htmlunit.corejs.javascript.ast.SwitchCase;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.capgemini.driver.StepExecutor;
import com.capgemini.executor.Executioner;

/**
 * Utilities --- Class for utility functions
 * 
 * @author Prasad Joshi
 */

public class Utilities {

	/*
	 * private String strAbsolutepath = new File("").getAbsolutePath(); private
	 * String strDataPath = strAbsolutepath + "/src/com/capgemini/data/";
	 * private String strScriptPath = strAbsolutepath +
	 * "/src/com/capgemini/scripts/"; private String strResultPath =
	 * strAbsolutepath + "/src/com/capgemini/results/";
	 */
	private String strAbsolutepath;
	private String strDataPath;
	private String strScriptPath;
	private String strResultPath;
	private int totalPagesScanned;
	private Reporter reporter;

	public Utilities(Reporter reporter) {
		this.reporter = reporter;

		strAbsolutepath = new File("").getAbsolutePath();
		strDataPath = strAbsolutepath + "/data/";
		strScriptPath = strAbsolutepath + "/src/com/capgemini/scripts/";
		strResultPath = strAbsolutepath + "/results/";

	}

	/* public Verification verify = new Verification(); */

	public Utilities() {

		strAbsolutepath = new File("").getAbsolutePath();
		strDataPath = strAbsolutepath + "/data/";
		strScriptPath = strAbsolutepath + "/src/com/capgemini/scripts/";
		strResultPath = strAbsolutepath + "/results/";
	}

	public int getTotalPagesScanned() {
		return totalPagesScanned;
	}

	public void incrementPagesScanned() {
		totalPagesScanned++;
	}

	public String getAbsolutePath() {
		return strAbsolutepath;
	}

	public String getDataPath() {
		return strDataPath;
	}

	public String getScriptPath() {
		return strScriptPath;
	}

	public String getResultPath() {
		return strResultPath;
	}

	// Function for Login in application
	public void Login(StepExecutor stepExecutor, Map<String, String> DataMap,
			RemoteWebDriver webDriver, WebDriverWait wait) {
		try {

			// Enter username
			wait.until(ExpectedConditions.elementToBeClickable(By
					.id("IDToken1")));
			webDriver.findElementById("IDToken1").click();
			stepExecutor.enterTextValue("findElementById", "IDToken1", DataMap,
					"Textbox_User", webDriver, "LOGIN");
			// Enter password

			wait.until(ExpectedConditions.elementToBeClickable(By
					.id("IDToken2")));
			stepExecutor.enterTextValue("findElementById", "IDToken2", DataMap,
					"Textbox_pwd", webDriver, "LOGIN");

			// Code for login button starts
			wait.until(ExpectedConditions.elementToBeClickable(By
					.name("Login.Submit")));
			stepExecutor.clickButton("findElementByName", "Login.Submit",
					webDriver, "Login");

		} catch (Exception e1) {
			// System.out.println(e1.getMessage());
		}
	}

	// Login for new user in one test flow
	public void Login1(StepExecutor stepExecutor, Map<String, String> DataMap,
			RemoteWebDriver webDriver, WebDriverWait wait) {
		try {

			// Enter username
			wait.until(ExpectedConditions.elementToBeClickable(By
					.id("IDToken1")));
			stepExecutor.enterTextValue("findElementById", "IDToken1", DataMap,
					"Textbox_User1", webDriver, "LOGIN");
			// Enter password

			wait.until(ExpectedConditions.elementToBeClickable(By
					.id("IDToken2")));
			stepExecutor.enterTextValue("findElementById", "IDToken2", DataMap,
					"Textbox_pwd1", webDriver, "LOGIN");

			// Code for login button starts
			wait.until(ExpectedConditions.elementToBeClickable(By
					.name("Login.Submit")));
			stepExecutor.clickButton("findElementByName", "Login.Submit",
					webDriver, "Login");

		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
	}

	// Function for Logout from the application
	public void logOut(StepExecutor stepExecutor, RemoteWebDriver webDriver,
			WebDriverWait wait) throws InterruptedException {
		// Click on LOGOUT
		wait.until(ExpectedConditions.elementToBeClickable(By
				.cssSelector("#WIN_0_300000044 > div.btntextdiv > div.f9")));
		stepExecutor.clickElementByCSS(
				"#WIN_0_300000044 > div.btntextdiv > div.f9", webDriver);
		Thread.sleep(3000);
		stepExecutor.VerifyFrame(webDriver);
	}

	public String getConfigValues(String strKey) {
		Map<String, String> configMap = new HashMap();

		String strURL = null;
		String strApplicationName = null;
		String strBrowser = null;
		String strCBTFlag = "false";
		String strConfigFile = "Config File";
		String strDataFileName = "Data File";
		String strDatFilePath = "Data File Path";
		String strScriptFileName = "Script File";
		String strInputData = "Script Input Data File Path";
		String strDataSource = "Text";
		String strExecutionConfigFileName = "Execution Configuration File";
		String strConfigFileName = strDataPath + "Cafe.config";
		// List<String> lstComponents = new ArrayList<String>(0);

		Scanner Scaning;
		try {
			Scaning = new Scanner(new FileReader(new File(strConfigFileName)));

			while (Scaning.hasNextLine()) {
				Scanner scan = new Scanner(Scaning.nextLine());
				scan.useDelimiter("=");
				if (scan.hasNext()) {
					String name = scan.next();
					String value = scan.next();
					if (name.trim().equalsIgnoreCase("Application URL")) {
						strURL = value;
						configMap.put("Application URL", strURL.trim());
					}
					if (name.trim().equalsIgnoreCase("Application Name")) {
						strApplicationName = value;
						configMap.put("Application Name",
								strApplicationName.trim());
					}
					if (name.trim().equalsIgnoreCase("Browser Type")) {
						strBrowser = value;
						configMap.put("Browser Type", strBrowser.trim());
					}
					if (name.trim().equalsIgnoreCase("Data Source")) {
						strDataSource = value;
						configMap.put("Data Source", strDataSource.trim());
					}
					if (name.trim().equalsIgnoreCase("Data Sheet Path")) {
						strInputData = value;
						configMap.put("Data Sheet Path", strDataSource.trim());
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		configMap.put(strDataFileName, strDataPath + strApplicationName.trim()
				+ "_Data.txt");
		configMap.put(strScriptFileName,
				strScriptPath + strApplicationName.trim() + ".java");
		configMap.put(strExecutionConfigFileName, strDataPath + "/"
				+ strApplicationName.trim() + "/" + strApplicationName.trim()
				+ "_Execution.config");
		configMap.put(strConfigFile, strConfigFileName);
		configMap.put(strDatFilePath, strDataPath);

		if (configMap.get("Data Source") == null) {
			configMap.put("Data Source", "Text");
		}

		return configMap.get(strKey);
	}

	public void setDataFile(String strModuleName, String strComponentName) {
		String strDataSource = getConfigValues("Data Source");
		String strDataFile = strDataPath + "DatafileName.txt";
		FileWriter fwo;
		try {
			fwo = new FileWriter(strDataFile);
			BufferedWriter bwObj = new BufferedWriter(fwo);
			bwObj.write(strModuleName + "_" + strComponentName);
			/*
			 * if(strDataSource.equalsIgnoreCase("Excel") ||
			 * strDataSource.equalsIgnoreCase("xls")){ bwObj.write(strModuleName
			 * +"_" + strComponentName); } else{ bwObj.write(strModuleName + "_"
			 * + strComponentName + "_Data.txt"); }
			 */

			bwObj.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getDataFile(String strComponent) {

		String strDataFileNamePath = strDataPath + "DatafileName.txt";
		String strLine = null;
		String strDataFileName = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					strDataFileNamePath));
			while ((strLine = br.readLine()) != null) {
				strDataFileName = strLine;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String strDataFile = null;
		String strDataSource = getConfigValues("Data Source");

		if (strDataSource.equalsIgnoreCase("Excel")
				|| strDataSource.equalsIgnoreCase("xls")) {
			// strDataFile = strDataFileName + "_" + strComponent + "_Data";
			strDataFile = strDataFileName;
		} else {

			// strApplicationName = utils.getConfigValues("Application Name");

			// lstComponents = utils.getComponentNames();
			/*
			 * Modified for GP lstComponents = utils.getExecutionComponent();
			 * String strComponentDataFile = null; String strComponentName =
			 * null; for(int component = 0; component<lstComponents.size();
			 * component++){ strComponentName =
			 * lstComponents.get(component).trim(); //strComponentDataFile =
			 * utils.getConfigValues("Data File Path") + strApplicationName +
			 * "_" + strComponentName + "_Data.txt"; strComponentDataFile =
			 * utils.getConfigValues("Data File Path") + strDataFileName;
			 * configMap.put(strComponentName, strComponentDataFile);
			 * 
			 * } strDataFile = configMap.get(strComponent); Modification ends
			 */
			strDataFile = strDataFileName;

		}
		return strDataFile;
	}

	public String getDataFileInfo() {

		String strDataFileNamePath = strDataPath + "DatafileName.txt";
		String strLine = null;
		String strDataFileName = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					strDataFileNamePath));
			while ((strLine = br.readLine()) != null) {
				strDataFileName = strLine;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return strDataFileName;
	}

	public List<String> getComponentNames() {

		String strConfigFileName = strDataPath + "Cafe.config";
		List<String> lstComponents = new ArrayList<String>(0);

		Scanner Scaning;
		try {
			Scaning = new Scanner(new FileReader(new File(strConfigFileName)));

			while (Scaning.hasNextLine()) {
				Scanner scan = new Scanner(Scaning.nextLine());
				scan.useDelimiter("=");
				if (scan.hasNext()) {
					String name = scan.next();
					String value = scan.next();

					if (name.trim().equalsIgnoreCase("Component Name")) {
						lstComponents.add(value.trim());
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lstComponents;
	}

	public int getRow(String strComponentName) {

		String strKey = null;
		String strValue = null;
		// int expectedTokenNumber = 0;
		String strExecutionConfigFileName = null;
		Map<String, String> executionConfigMap = new HashMap<String, String>();
		int executionRowNumber = 0;
		Utilities utils = new Utilities(reporter);
		String strDelimiter = "##";
		String strRowNumber = "1";

		String strDataSource = utils.getConfigValues("Data Source");

		if (strDataSource.equalsIgnoreCase("Excel")
				|| strDataSource.equalsIgnoreCase("xls")) {
			String strExcelDataFileName = strDataPath
					+ utils.getConfigValues("Application Name") + "_Data.xls";
			POIFSFileSystem fs;
			String strCellValue = null;
			boolean isExpectedComponent = false;

			try {
				fs = new POIFSFileSystem(new FileInputStream(
						strExcelDataFileName));
				HSSFWorkbook workbook = new HSSFWorkbook(fs);

				HSSFSheet dataSheet = workbook.getSheet("Scenario");
				int totalRows = dataSheet.getLastRowNum();
				String strRowData = null;
				HSSFRow row = null;
				// HSSFCell cell = null;
				int totalCells = 0;
				for (int i = 0; i <= totalRows; i++) {
					row = dataSheet.getRow(i);
					totalCells = row.getLastCellNum();
					for (int j = 0; j < totalCells; j++) {
						strCellValue = row.getCell(j).toString();
						if (strCellValue.equals(strComponentName)) {
							isExpectedComponent = true;
							strCellValue = row.getCell(j + 1).toString();
							strRowNumber = strCellValue;
							break;
						}
					}
					if (isExpectedComponent) {
						break;
					}
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				executionRowNumber = Integer.parseInt(strRowNumber);
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
		} else {

			try {
				strExecutionConfigFileName = utils
						.getConfigValues("Execution Configuration File");
				BufferedReader br = new BufferedReader(new FileReader(
						strExecutionConfigFileName));
				String strLine = null;
				StringTokenizer st = null;
				int tokenNumber = 0;
				int rowNumber = 0;
				int counter = 0;

				while ((strLine = br.readLine()) != null) {
					rowNumber++;

					// break comma separated line using delimiter "strDelimiter"
					st = new StringTokenizer(strLine, strDelimiter);

					while (st.hasMoreTokens()) {
						tokenNumber++;
						strKey = st.nextToken();

						if (counter != 0) {
							// strKey = st.nextToken();
							strValue = st.nextToken();
							executionConfigMap.put(strKey, strValue);
							strValue = st.nextToken();
						}
					}
					// reset token number
					counter++;
					tokenNumber = 0;

				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			strRowNumber = executionConfigMap.get(strComponentName);
			try {
				executionRowNumber = Integer.parseInt(strRowNumber);
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
		}

		return executionRowNumber;
	}

	public List<String> getExecutionComponent() {
		String strComponent = null;
		List<String> lstComponents = new ArrayList<String>(0);

		String strExecutionConfigFileName = null;
		String strDelimiter = "##";

		try {
			strExecutionConfigFileName = getConfigValues("Execution Configuration File");
			BufferedReader br = new BufferedReader(new FileReader(
					strExecutionConfigFileName));
			String strLine = null;
			StringTokenizer st = null;
			int tokenNumber = 0;
			int rowNumber = 0;
			int counter = 0;

			while ((strLine = br.readLine()) != null) {
				rowNumber++;

				// break comma separated line using delimiter "strDelimiter"
				st = new StringTokenizer(strLine, strDelimiter);

				while (st.hasMoreTokens()) {
					tokenNumber++;
					strComponent = st.nextToken();
					if (counter != 0) {
						lstComponents.add(strComponent);
						strComponent = st.nextToken();
						strComponent = st.nextToken();
					}

				}
				// reset token number
				counter++;
				tokenNumber = 0;

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lstComponents;
	}

	public String getExecutionFlag(String strComponentName) {

		String strKey = null;
		String strValue = null;
		String strExecute = null;

		String strExecutionConfigFileName = null;
		Map<String, String> executionConfigMap = new HashMap<String, String>();
		Utilities utils = new Utilities(reporter);
		String strDelimiter = "##";

		String strDataSource = utils.getConfigValues("Data Source");

		if (strDataSource.equalsIgnoreCase("Excel")
				|| strDataSource.equalsIgnoreCase("xls")) {
			String strExcelDataFileName = strDataPath
					+ utils.getConfigValues("Application Name") + "_Data.xls";
			POIFSFileSystem fs;
			String strCellValue = null;
			boolean isExpectedComponent = false;

			try {
				fs = new POIFSFileSystem(new FileInputStream(
						strExcelDataFileName));
				HSSFWorkbook workbook = new HSSFWorkbook(fs);

				HSSFSheet dataSheet = workbook.getSheet("Scenario");
				int totalRows = dataSheet.getLastRowNum();
				String strRowData = null;
				HSSFRow row = null;
				// HSSFCell cell = null;
				int totalCells = 0;
				for (int i = 0; i <= totalRows; i++) {
					row = dataSheet.getRow(i);
					totalCells = row.getLastCellNum();
					for (int j = 0; j < totalCells; j++) {
						strCellValue = row.getCell(j).toString();
						if (strCellValue.equals(strComponentName)) {
							isExpectedComponent = true;
							strCellValue = row.getCell(j + 2).toString();
							strExecute = strCellValue;
							break;
						}
					}
					if (isExpectedComponent) {
						break;
					}
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			try {
				strExecutionConfigFileName = utils
						.getConfigValues("Execution Configuration File");
				BufferedReader br = new BufferedReader(new FileReader(
						strExecutionConfigFileName));
				String strLine = null;
				StringTokenizer st = null;
				int tokenNumber = 0;
				int rowNumber = 0;
				int counter = 0;

				while ((strLine = br.readLine()) != null) {
					rowNumber++;

					// break comma separated line using delimiter "strDelimiter"
					st = new StringTokenizer(strLine, strDelimiter);

					while (st.hasMoreTokens()) {
						tokenNumber++;
						strKey = st.nextToken();

						if (counter != 0) {
							// strKey = st.nextToken();
							strValue = st.nextToken();
							strValue = st.nextToken();
							executionConfigMap.put(strKey, strValue);
						}
					}
					// reset token number
					counter++;
					tokenNumber = 0;

				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			strExecute = executionConfigMap.get(strComponentName);
		}

		return strExecute;
	}

	public void verifyElementPresent(RemoteWebDriver webDriver,
			String strElement) {

		boolean exists = false;
		CreateResult result = new CreateResult();

		for (int interval = 0; interval < 30; interval++) {
			if (webDriver.findElementsByName(strElement).size() != 0
					&& webDriver.findElementByName(strElement).isDisplayed()) {
				exists = true;
				break;
			} else if (webDriver.findElementsById(strElement).size() != 0
					&& webDriver.findElementById(strElement).isDisplayed()) {
				exists = true;
				break;
			} else if (webDriver.findElementsByXPath(strElement).size() != 0
					&& webDriver.findElementByXPath(strElement).isDisplayed()) {
				exists = true;
				break;
			} else if (webDriver.findElementsByCssSelector(strElement).size() != 0
					&& webDriver.findElementByCssSelector(strElement)
							.isDisplayed()) {
				exists = true;
				break;
			}

			else {
				exists = false;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// String strBrowserType = utils.getConfigValues("Browser Type");

		if (exists) {
			exists = true;
			result.takeScreenshot("Login", "Verfy element " + strElement,
					"Pass", "Pass", "Verfy element " + strElement, true,
					"Pass", webDriver);
		} else {
			exists = true;
			result.takeScreenshot("Login", "Verfy element " + strElement,
					"Pass", "Pass", "Verfy element " + strElement, true,
					"Fail", webDriver);
		}

	}

	public boolean verifyLinkPresent(RemoteWebDriver webDriver,
			String strElement) {

		boolean exists = false;

		for (int interval = 0; interval < 30; interval++) {
			if (webDriver.findElementsByLinkText(strElement).size() != 0
					&& webDriver.findElementByLinkText(strElement)
							.isDisplayed()) {
				exists = true;
				break;
			} else if (webDriver.findElementsByName(strElement).size() != 0
					&& webDriver.findElementByName(strElement).isDisplayed()) {
				exists = true;
				break;
			} else if (webDriver.findElementsById(strElement).size() != 0
					&& webDriver.findElementById(strElement).isDisplayed()) {
				exists = true;
				break;
			} else if (webDriver.findElementsByXPath(strElement).size() != 0
					&& webDriver.findElementByXPath(strElement).isDisplayed()) {
				exists = true;
				break;
			} else {
				exists = false;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// String strBrowserType = utils.getConfigValues("Browser Type");

		if (exists) {
			return exists;
		} else {
			return exists;
		}

	}

	public boolean verifyElementText(RemoteWebDriver webDriver,
			String strElement, String strElementProperty, String strExpectedText) {

		verifyElementPresent(webDriver, strElement);
		String strActualText = null;

		if (strElementProperty.equalsIgnoreCase("id")) {
			strActualText = webDriver.findElementById(strElement).getText();
		}

		if (strElementProperty.equalsIgnoreCase("name")) {
			strActualText = webDriver.findElementByName(strElement).getText();
		}

		if (strElementProperty.equalsIgnoreCase("xpath")) {
			strActualText = webDriver.findElementByXPath(strElement).getText();
		}

		if (strActualText.equals(strExpectedText)) {
			return true;
		} else {
			return false;
		}

	}

	public boolean verifyListValue(RemoteWebDriver webDriver,
			String strElement, String strElementProperty,
			String strExpectedValue) {

		verifyElementPresent(webDriver, strElement);
		WebElement listbox = null;

		if (strElementProperty.equalsIgnoreCase("id")) {
			listbox = webDriver.findElementById(strElement);
		}

		if (strElementProperty.equalsIgnoreCase("name")) {
			listbox = webDriver.findElementByName(strElement);
		}

		if (strElementProperty.equalsIgnoreCase("xpath")) {
			listbox = webDriver.findElementByXPath(strElement);
		}

		List<WebElement> options = listbox.findElements(By.tagName("option"));
		for (WebElement option : options) {
			if (option.getText().equals(strExpectedValue))
				return true;
		}
		return false;
	}

	public boolean verifyListValues(RemoteWebDriver webDriver,
			String strElement, String strElementProperty,
			String strExpectedValues) {

		verifyElementPresent(webDriver, strElement);
		WebElement listbox = null;
		String[] arrListValues = strExpectedValues.split(";");
		int counter = 0;

		if (strElementProperty.equalsIgnoreCase("id")) {
			listbox = webDriver.findElementById(strElement);
		}

		if (strElementProperty.equalsIgnoreCase("name")) {
			listbox = webDriver.findElementByName(strElement);
		}

		if (strElementProperty.equalsIgnoreCase("xpath")) {
			listbox = webDriver.findElementByXPath(strElement);
		}

		List<WebElement> options = listbox.findElements(By.tagName("option"));
		for (int i = 0; i < arrListValues.length; i++) {
			for (WebElement option : options) {
				if (option.getText().equals(arrListValues[i]))
					counter++;
				break;
			}
		}
		if (counter == arrListValues.length) {
			return true;
		} else {
			return false;
		}
	}

	public boolean verifySelectedListValue(RemoteWebDriver webDriver,
			String strElement, String strElementProperty,
			String strExpectedSelectedValue) {

		verifyElementPresent(webDriver, strElement);
		WebElement listbox = null;

		if (strElementProperty.equalsIgnoreCase("id")) {
			listbox = webDriver.findElementById(strElement);
		}

		if (strElementProperty.equalsIgnoreCase("name")) {
			listbox = webDriver.findElementByName(strElement);
		}

		if (strElementProperty.equalsIgnoreCase("xpath")) {
			listbox = webDriver.findElementByXPath(strElement);
		}

		List<WebElement> options = listbox.findElements(By.tagName("option"));
		for (WebElement option : options) {
			if (option.isSelected())
				return true;
		}
		return false;
	}

	public boolean verifyCheckboxStatus(RemoteWebDriver webDriver,
			String strElement, String strElementProperty, String strExpectedText) {

		verifyElementPresent(webDriver, strElement);
		String strActualStatus = null;

		if (strElementProperty.equalsIgnoreCase("id")) {
			strActualStatus = webDriver.findElementById(strElement)
					.getAttribute("checked");
		}

		if (strElementProperty.equalsIgnoreCase("name")) {
			strActualStatus = webDriver.findElementByName(strElement)
					.getAttribute("checked");
		}

		if (strElementProperty.equalsIgnoreCase("xpath")) {
			strActualStatus = webDriver.findElementByXPath(strElement)
					.getAttribute("checked");
		}

		if (strActualStatus.equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean verifyRadioButtonStatus(RemoteWebDriver webDriver,
			String strElement, String strElementProperty, String strExpectedText) {
		verifyElementPresent(webDriver, strElement);
		String strActualStatus = null;

		if (strElementProperty.equalsIgnoreCase("id")) {
			strActualStatus = webDriver.findElementById(strElement)
					.getAttribute("checked");
		}

		if (strElementProperty.equalsIgnoreCase("name")) {
			strActualStatus = webDriver.findElementByName(strElement)
					.getAttribute("checked");
		}

		if (strElementProperty.equalsIgnoreCase("xpath")) {
			strActualStatus = webDriver.findElementByXPath(strElement)
					.getAttribute("checked");
		}

		if (strActualStatus.equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}

	}

	public static String now() {
		String DATE_FORMAT_NOW = "yyyy-MM-dd-hh.mm.ss";
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int year = cal.get(Calendar.YEAR);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		Random rand = new Random();
		int num = rand.nextInt(1000);
		return sdf.format(cal.getTime());
	}

	public boolean clikOnApplicationMenuWithSeclection(String sFirstMenuOption,
			String sSecondMenuOption, RemoteWebDriver webDriver,
			Map<String, String> DataMap, String sAppName)
			throws InterruptedException {

		boolean sFlag = false;
		boolean sfound = false;
		WebDriverWait wait = new WebDriverWait(webDriver, 5);
		StepExecutor stepExecutor = new StepExecutor(reporter);
		Thread.sleep(10000);
		// CLick on Application
		wait.until(ExpectedConditions.elementToBeClickable(By
				.id("reg_img_304316340")));

		stepExecutor.clickImage("findElementById",
				"reg_img_304316340", webDriver,
				"APPLICATION MENU");

		wait.until(ExpectedConditions.elementToBeClickable(By
				.cssSelector(".navLabel.root")));
		// Hover to Change Management
		List<WebElement> applicationMenuItems = webDriver.findElements(By
				.cssSelector(".navLabel.root"));

		for (WebElement menuItem : applicationMenuItems) {
			if (menuItem.getText().equals(sFirstMenuOption)) {
				sfound = true;
				Executioner exe = new Executioner();
				/*
				 * if(exe.getExecutionBrowser().equals("Internet Explorer")){
				 * onMouseOver(menuItem, webDriver); } else{
				 */
				Actions builder = new Actions(webDriver);
				builder.moveToElement(menuItem).build().perform();

				Thread.sleep(1000);
				// Click on New Change
				sFlag = stepExecutor.selectListValueByCss(".navLabel.lvl1",
						DataMap, sSecondMenuOption, webDriver, sAppName);
			}
		}
		if (sfound == false) {
			reporter.writeStepResult(System.getProperty("Test_Scenario_Name"),
					"Hovering on Menu", sFirstMenuOption, "Fail",
					"Expected value is not present", true, webDriver);
		}

		return sFlag;
	}

	public String vErifyTheErrorDisplayed(RemoteWebDriver webDriver) {

		System.setProperty("Error", "NO");
		WebDriverWait wait = new WebDriverWait(webDriver, 10);
		String Error = null;
		try {
			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector(".prompttexterr")));
			// Get the error
			Error = webDriver.findElementByCssSelector(".prompttexterr")
					.getText();
		} catch (Exception e) {

		}
		System.clearProperty("Error");
		System.setProperty("Error", "YES");
		return Error;

	}

	public void getMonthAndDate(String str, Map<String, String> DataMap,
			RemoteWebDriver webDriver) {
		try {
			String[] str1 = str.split("-");
			String Date = str1[0];
			String Month = str1[1];
			String Year = str1[2];
			int month = Integer.parseInt(Month);
			int date = Integer.parseInt(Date);
			int year = Integer.parseInt(Year);

			switch (month) {
			case 1:
				Month = "January";
				break;
			case 2:
				Month = "February";
				break;
			case 3:
				Month = "March";
				break;
			case 4:
				Month = "April";
				break;
			case 5:
				Month = "May";
				break;
			case 6:
				Month = "June";
				break;
			case 7:
				Month = "July";
				break;
			case 8:
				Month = "August";
				break;
			case 9:
				Month = "September";
				break;
			case 10:
				Month = "October";
				break;
			case 11:
				Month = "November";
				break;
			case 12:
				Month = "December";
				break;
			}
			Date = date + "";
			DataMap.put("Date", Date);
			DataMap.put("Month", Month);
			DataMap.put("Year", Year);
			SelectDateAndTime(webDriver, DataMap);
			DataMap.remove("Date");
			DataMap.remove("Month");
			DataMap.remove("Year");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean SelectDateAndTime(RemoteWebDriver webDriver,
			Map<String, String> DataMap) throws InterruptedException {
		boolean bFound = false;

		WebDriverWait wait = new WebDriverWait(webDriver, 10);
		StepExecutor stepExecutor = new StepExecutor(reporter);
		// Select Month
		wait.until(ExpectedConditions.elementToBeClickable(By
				.cssSelector(".calendarTable .selectionbtn")));
		webDriver.findElementByCssSelector(".calendarTable .selectionbtn")
				.click();
		Thread.sleep(1000);
		stepExecutor.SelectMenuItem(".MenuEntryName", DataMap, "Month",
				webDriver);
		Thread.sleep(500);

		// Select Year
		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.id("popupyear")));
		int cur_yr = Integer.parseInt(webDriver.findElementById("popupyear")
				.getText());
		int yr = Integer.parseInt(DataMap.get("Year"));
		if (yr > cur_yr) {
			int diff = yr - cur_yr;
			for (int i = 0; i < diff; i++)
				webDriver.findElementById("popupyear").sendKeys(Keys.UP);

		}

		// Select Date//
		List<WebElement> weekDays = webDriver
				.findElementsByCssSelector(".weekday");
		List<WebElement> weekEnds = webDriver
				.findElementsByCssSelector(".weekend");
		WebElement calendaron = webDriver
				.findElementByCssSelector(".calendaron");

		if (calendaron.getText().equals(DataMap.get("Date"))) {
			bFound = true;
			calendaron.click();
			Thread.sleep(5);
		}

		// Check date in weekdays
		if (bFound == false) {
			for (WebElement menuItem : weekDays) {
				if (menuItem.getText().equalsIgnoreCase(DataMap.get("Date"))) {
					bFound = true;
					menuItem.click();
					Thread.sleep(5);
					break;
				}
			}
		}

		// check date in Weekend
		if (bFound == false) {
			for (WebElement menuItem : weekEnds) {
				if (menuItem.getText().equals(DataMap.get("Date"))) {
					bFound = true;
					menuItem.click();
					Thread.sleep(5);
					break;
				}
			}

		}

		// Enter Time
		// Enter Hour
		if (DataMap.containsKey("Hour")) {
			if (!DataMap.get("Hour").equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".showTime #hr")));
				webDriver.findElementByCssSelector(".showTime #hr").sendKeys(
						DataMap.get("Hour"));
				Thread.sleep(2);
			}
		}
		// Enter Minutes
		if (DataMap.containsKey("Minutes")) {
			if (!DataMap.get("Minutes").equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".showTime #min")));
				webDriver.findElementByCssSelector(".showTime #min").sendKeys(
						DataMap.get("Minutes"));
				Thread.sleep(2);
			}
		}
		// Enter Seconds
		if (DataMap.containsKey("Seconds")) {
			if (!DataMap.get("Seconds").equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".showTime #sec")));
				webDriver.findElementByCssSelector(".showTime #sec").sendKeys(
						DataMap.get("Seconds"));
				Thread.sleep(2);
			}
		}

		// Enter AMPM
		if (DataMap.containsKey("AMPM")) {
			if (!DataMap.get("AMPM").equals("")) {
				wait.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(".showTime #meridiem")));
				if (DataMap.get("AMPM").equalsIgnoreCase("AM")) {
					webDriver.findElementByCssSelector(".showTime #meridiem")
							.sendKeys("A");
					Thread.sleep(2);
				}
				if (DataMap.get("AMPM").equalsIgnoreCase("PM")) {
					webDriver.findElementByCssSelector(".showTime #meridiem")
							.sendKeys("P");
					Thread.sleep(2);
				}
			}
		}
		// Click on OK
		if (bFound) {
			wait.until(ExpectedConditions.elementToBeClickable(By
					.cssSelector(".DIVPopupButtons #ardivpok")));
			stepExecutor.clickElementByCSS(".DIVPopupButtons #ardivpok",
					webDriver);
		}
		return bFound;

	}

	public boolean SelectServiceTargetByProgress(RemoteWebDriver webDriver,
			String windowName, String progressStatus)
			throws InterruptedException {
		boolean bFound = false;
		Thread.sleep(5000);
		Verification veri = new Verification(reporter);
		webDriver.switchTo().window(windowName);
		// Get the Number of rows
		veri.verifyElementPresent(webDriver,
				"html/body/div[1]/div[5]/div[7]/div[1]/table/tbody/tr/td[1]",
				"xpath");
		String sNumberOfRows = webDriver.findElementByXPath(
				"html/body/div[1]/div[5]/div[7]/div[1]/table/tbody/tr/td[1]")
				.getText();
		if (sNumberOfRows.equals("")) {
			sNumberOfRows = webDriver
					.findElementByXPath(
							"html/body/div[1]/div[5]/div[7]/div[1]/table/tbody/tr/td[1]")
					.getAttribute("value");
		}
		String sRow = Character.toString(sNumberOfRows.toCharArray()[0]);

		int iNumberOfRows = Integer.parseInt(sRow) + 2;

		String sCommonXpath = "html/body/div[1]/div[5]/div[7]/div[2]/div/div/table/tbody/tr[2]/td[7]/nobr/span";
		for (int i = 2; i <= iNumberOfRows; i++) {
			String sNewXpath = "html/body/div[1]/div[5]/div[7]/div[2]/div/div/table/tbody/tr["
					+ i + "]/td[7]/nobr/span";
			WebElement eColoum = webDriver.findElementByXPath(sNewXpath);
			String sColoumValue = eColoum.getText();
			if (progressStatus.equalsIgnoreCase(sColoumValue)) {
				bFound = true;
				eColoum.click();
				break;
			}
		}
		return bFound;
	}

	public static boolean onMouseOver(WebElement element,
			RemoteWebDriver webDriver) {
		boolean result = false;
		try {
			String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
			JavascriptExecutor js = (JavascriptExecutor) webDriver;
			js.executeScript(mouseOverScript, element);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

}
