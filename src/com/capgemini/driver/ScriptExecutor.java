package com.capgemini.driver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.capgemini.executor.ExecutionRowNumber;
import com.capgemini.executor.Executioner;
import com.capgemini.utilities.CreateResult;
import com.capgemini.utilities.ReadExcel;
import com.capgemini.utilities.Utilities;

/**
 * ScriptExecutor --- Class for the executing the steps in the script
 * 
 * @author Prasad Joshi
 */

public class ScriptExecutor {

	Utilities utils = new Utilities();
	CreateResult result = new CreateResult();
	Executioner exe = new Executioner();
	private String strAbsolutepath = new File("").getAbsolutePath();
	// private String strDataPath = strAbsolutepath +
	// "/src/com/capgemini/data/";
	private String strDataPath = strAbsolutepath + "/data/";

	List<String> lstExecutionModules = new ArrayList<String>(0);
	List<String> lstExecutionModulesFlag = new ArrayList<String>(0);

	List<String> lstExecutionComponents = new ArrayList<String>(0);
	List<String> lstRowNumber = new ArrayList<String>(0);
	List<String> lstExecutionFlag = new ArrayList<String>(0);
	ExecutionRowNumber executionRowNumber = new ExecutionRowNumber();

	ReadExcel readExcel = new ReadExcel();
	public String sCurrentExeSheetName;
	public String sCurrentTestCase;
	public String sAppName;
	public String strBrowserType;

	public String getsCurrentExeSheetName() {
		return sCurrentExeSheetName;
	}

	public void setsCurrentExeSheetName(String sCurrentExeSheetName) {
		this.sCurrentExeSheetName = sCurrentExeSheetName;
	}

	public void getExecutionDetails() throws Exception {

		String sInputFilePath = utils.getConfigValues("Data Sheet Path");
		File inputWorkbook = new File(sInputFilePath);
		Workbook w;
		w = Workbook.getWorkbook(inputWorkbook);
		int iSheets = w.getNumberOfSheets();
		for (int i = 0; i <= iSheets; i++) {
			Sheet sCurrentSheetName = w.getSheet(i);
			String sSheetName = sCurrentSheetName.getName();
			// String sClassToCall = getClassBySheet(sSheetName);
			// System.out.println(sClassToCall);

		}

	}

	/*
	 * public String getClassBySheet(String sSheetName) throws Exception {
	 * 
	 * String sCurrentSheet = sSheetName; System.clearProperty("sheetName");
	 * System.setProperty("sheetName", sCurrentSheet); sCurrentExeSheetName =
	 * sCurrentSheet;
	 * 
	 * Class<?> objClass = Class.forName(readExcel
	 * .ReadMasterSheetTestCaseName());
	 * 
	 * Object obj = objClass.newInstance(); Method method =
	 * objClass.getMethod("launchApplication"); method.invoke(obj);
	 * 
	 * switch (sSheetName) {
	 * 
	 * case "TestCase1": Incident_Status_Assigned bmc1 = new
	 * Incident_Status_Assigned(); bmc1.executeTestcase(); break;
	 * 
	 * case "TestCase2": ChangeStatus bmc2 = new ChangeStatus();
	 * bmc2.executeTestcase(); break;
	 * 
	 * case "TestCase3": ProblemStatus bmc3 = new ProblemStatus();
	 * bmc3.executeTestcase(); break;
	 * 
	 * }
	 * 
	 * return sCurrentSheet;
	 * 
	 * }
	 * 
	 * public String getClassBySheet(RemoteWebDriver driver, String host, String
	 * browser, String sSheetName) throws Exception {
	 * 
	 * String sCurrentSheet = sSheetName; System.clearProperty("sheetName");
	 * System.setProperty("sheetName", sCurrentSheet); sCurrentExeSheetName =
	 * sCurrentSheet;
	 * 
	 * switch (sSheetName) {
	 * 
	 * case "TestCase1": Incident_Status_Assigned bmc1 = new
	 * Incident_Status_Assigned(); bmc1.executeTestcase(driver, host, browser);
	 * break;
	 * 
	 * case "TestCase2": ChangeStatus bmc2 = new ChangeStatus();
	 * bmc2.executeTestcase(driver, host, browser); break;
	 * 
	 * case "TestCase3": ProblemStatus bmc3 = new ProblemStatus();
	 * bmc3.executeTestcase(driver, host, browser); break;
	 * 
	 * }
	 * 
	 * return sCurrentSheet; }
	 */

	public String readDataFile(String fileName,String SheetName, int expectedRowNumber,
			String expectedToken) {

		String strData = null;
		int expectedTokenNumber = 0;
		String strDelimiter = "##";
		int counter = 0;
		String strDataSource = utils.getConfigValues("Data Source");
		String arrFile[] = new String[10];
		arrFile = fileName.split("_");

		if (strDataSource.equalsIgnoreCase("Excel")
				|| strDataSource.equalsIgnoreCase("xls")) {

			// String strExcelDataFileName = strDataPath + arrFile[0] + "/"
			// + arrFile[0] + "_Data.xls";
			String strExcelDataFileName = strDataPath + "MasterSheet.xls";
			POIFSFileSystem fs;
			String strCellValue = null;
			try {
				fs = new POIFSFileSystem(new FileInputStream(
						strExcelDataFileName));
				HSSFWorkbook workbook = new HSSFWorkbook(fs);
				
				// String arrFile [] = new
				// String[10];System.getProperty("sheetName"
				// arrFile = fileName.split("_");
			    //	System.out.println(sAppName);
				
				
				HSSFSheet dataSheet = workbook.getSheet(SheetName);
				HSSFRow dataRow = dataSheet.getRow(0);

				int totalCells = dataRow.getLastCellNum();
				for (int i = 0; i < totalCells; i++) {
					strCellValue = dataRow.getCell(i).toString();
					if (strCellValue.equals(expectedToken)) {
						dataRow = dataSheet.getRow(expectedRowNumber);
						if (dataRow.getCell(i) != null) {
							strCellValue = dataRow.getCell(i).toString();
						} else {
							strCellValue = "";
						}
						if (strCellValue != null) {
							strData = strCellValue;
							counter++;
						}
						break;
					}
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			try {
				String strTextDataFileName = strDataPath + arrFile[0] + "/"
						+ fileName + "_Data.txt";
				BufferedReader br = new BufferedReader(new FileReader(
						strTextDataFileName));
				String strLine = null;
				StringTokenizer st = null;
				int tokenNumber = 0;
				int rowNumber = 0;

				while ((strLine = br.readLine()) != null) {
					rowNumber++;

					// break data line using delimiter "||"
					st = new StringTokenizer(strLine, strDelimiter);

					while (st.hasMoreTokens()) {
						tokenNumber++;
						strData = st.nextToken();
						if (strData.equalsIgnoreCase(expectedToken)) {
							expectedTokenNumber = tokenNumber;
						}
						// if(expectedTokenNumber == tokenNumber && counter !=
						// 0)
						if (expectedTokenNumber == tokenNumber)
							break;

					}
					// reset token number
					if (expectedRowNumber == rowNumber - 1 && counter != 0) {
						break;
					} else {
						counter++;
						tokenNumber = 0;
					}

				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (counter > 0) {
			if (strData == null) {
				strData = "";
				System.out.println("Please provide data in data file");
				return strData;
			} else {
				return strData;
			}
		} else {
			return "";
		}
	}

	public String readDataSheetFile(String fileName, int expectedRowNumber,
			String expectedToken) {

		String strData = null;
		int expectedTokenNumber = 0;
		String strDelimiter = "##";
		int counter = 0;
		String strDataSource = utils.getConfigValues("Data Source");
		String arrFile[] = new String[10];
		arrFile = fileName.split("_");

		if (strDataSource.equalsIgnoreCase("Excel")
				|| strDataSource.equalsIgnoreCase("xls")) {

			// String strExcelDataFileName = strDataPath + arrFile[0] + "/"
			// + arrFile[0] + "_Data.xls";
			String strExcelDataFileName = strDataPath + "MasterSheet.xls";
			POIFSFileSystem fs;
			String strCellValue = null;
			try {
				fs = new POIFSFileSystem(new FileInputStream(
						strExcelDataFileName));
				HSSFWorkbook workbook = new HSSFWorkbook(fs);
				
				// String arrFile [] = new
				// String[10];System.getProperty("sheetName"
				// arrFile = fileName.split("_");
			//	System.out.println(sAppName);
				
				
				HSSFSheet dataSheet = workbook.getSheet("TWG");
				HSSFRow dataRow = dataSheet.getRow(0);

				int totalCells = dataRow.getLastCellNum();
				for (int i = 0; i < totalCells; i++) {
					strCellValue = dataRow.getCell(i).toString();
					if (strCellValue.equals(expectedToken)) {
						dataRow = dataSheet.getRow(expectedRowNumber);
						if (dataRow.getCell(i) != null) {
							strCellValue = dataRow.getCell(i).toString();
						} else {
							strCellValue = "";
						}
						if (strCellValue != null) {
							strData = strCellValue;
							counter++;
						}
						break;
					}
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			try {
				String strTextDataFileName = strDataPath + arrFile[0] + "/"
						+ fileName + "_Data.txt";
				BufferedReader br = new BufferedReader(new FileReader(
						strTextDataFileName));
				String strLine = null;
				StringTokenizer st = null;
				int tokenNumber = 0;
				int rowNumber = 0;

				while ((strLine = br.readLine()) != null) {
					rowNumber++;

					// break data line using delimiter "||"
					st = new StringTokenizer(strLine, strDelimiter);

					while (st.hasMoreTokens()) {
						tokenNumber++;
						strData = st.nextToken();
						if (strData.equalsIgnoreCase(expectedToken)) {
							expectedTokenNumber = tokenNumber;
						}
						// if(expectedTokenNumber == tokenNumber && counter !=
						// 0)
						if (expectedTokenNumber == tokenNumber)
							break;

					}
					// reset token number
					if (expectedRowNumber == rowNumber - 1 && counter != 0) {
						break;
					} else {
						counter++;
						tokenNumber = 0;
					}

				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (counter > 0) {
			if (strData == null) {
				strData = "";
				System.out.println("Please provide data in data file");
				return strData;
			} else {
				return strData;
			}
		} else {
			return "";
		}
	}

	
	
	public void executeScript() {

		String strClassName = null;
		String strExecute = null;
		// List<String> lstComponents = new ArrayList<String>(0);
		String strDataSource = utils.getConfigValues("Data Source");

		try {
			strClassName = "com.capgemini.scripts."
					+ utils.getConfigValues("Application Name");

			Class<?> objClass = Class.forName(strClassName);

			Object obj = objClass.newInstance();
			Method method = objClass.getMethod("launchApplication");
			method.invoke(obj);
			// getExecutionDetails();

			for (int x = 0; x < lstExecutionComponents.size(); x++) {

				if (lstExecutionFlag.get(x).equalsIgnoreCase("Yes")
						|| lstExecutionFlag.get(x).equalsIgnoreCase("Y")) {
					// rowNumber = Integer.parseInt(lstRowNumber.get(x));
					String[] arrRows = new String[5];
					arrRows = lstRowNumber.get(x).split("-");

					if (arrRows.length > 1) {
						int firstElement = Integer.parseInt(arrRows[0].trim());
						int thirdElement = Integer.parseInt(arrRows[1].trim());
						int executionCounter = thirdElement - firstElement;
						List<String> lstRowNumberNew = new ArrayList<String>(0);
						lstRowNumberNew.add(arrRows[0]);
						int lstRowData = 0;
						for (int m = 1; m <= executionCounter; m++) {
							lstRowData = Integer.parseInt(arrRows[0]) + m;
							lstRowNumberNew.add(Integer.toString(lstRowData));
						}
						// executionRowNumber.setExecutionRowNumber(lstRowNumber.get(x),arrRows.length);
						for (int run = 0; run < lstRowNumberNew.size(); run++) {
							executionRowNumber.setExecutionRowNumber(Integer
									.parseInt(lstRowNumberNew.get(run)));
							method = objClass.getMethod(lstExecutionComponents
									.get(x));
							method.invoke(obj);
						}

					} else {
						executionRowNumber.setExecutionRowNumber(Integer
								.parseInt(lstRowNumber.get(x)));
						method = objClass.getMethod(lstExecutionComponents
								.get(x));
						method.invoke(obj);
					}

				}

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

	public void executeScriptMultiple() throws Exception {
		String sInputFilePath = System.getProperty("File");
		ReadExcel read = new ReadExcel();
		read.setInputFile(sInputFilePath);
		String sSheetsToRead = read.ReadMasterSheet();
		String sTestCasesToExecute = read.ReadMasterSheetTestCaseName();// here
																		// will
																		// get
																		// test
																		// case
																		// name
																		// &
																		// browser
		String[] aSheets = sSheetsToRead.split(",");
		String[] aTestCaseNames = sTestCasesToExecute.split(",");
		int iLen = aSheets.length;

		for (int k = 1; k < iLen; k++) {
			// String sCurrentSheet = aSheets[k];

			String[] tempVar = aTestCaseNames[k].split("#");
			sAppName = tempVar[0];
			sCurrentTestCase = tempVar[0];
			strBrowserType = tempVar[1]; // final browser name will get here
			exe.setExecutionBrowser(strBrowserType);
			readExcel.setSheetName(sCurrentTestCase);
			Class<?> objClass = Class.forName("com.capgemini.scripts."
					+ sAppName);
			// System.out.println("Testdsfa");
			Object obj = objClass.newInstance();
			System.out.println("reached");
			Method method = objClass.getMethod("executeTestcase", String.class);
			// System.out.println("method");
			try {
				System.out.println(obj.getClass().getSimpleName());
				Object[] args = { strBrowserType };
				// Call the method via reflection
				method.invoke(obj, args);
				System.out.println(obj.getClass().getSimpleName());
			} catch (InvocationTargetException e) {
				Throwable cause = e.getCause();
				System.err.println("The method threw an exception: " + cause);
			}

			/*
			 * System.clearProperty("TestCaseName");
			 * System.setProperty("TestCaseName", sCurrentTestCase);
			 */
			// String sClassToCall =
			// getClassBySheet(sCurrentSheet);System.getProperty("TestCaseName"
			System.out.println(obj.getClass().getSimpleName());
		}
	}

	public void executeScriptMultiple(RemoteWebDriver driver, String host,
			String browser, String testName) throws Exception {
		// String sInputFilePath = System.getProperty("File");
		/*
		 * ReadExcel read = new ReadExcel(); read.setInputFile(sInputFilePath);
		 * String sSheetsToRead = read.ReadMasterSheet(); String
		 * sTestCasesToExecute = read.ReadMasterSheetTestCaseName(); String[]
		 * aSheets = sSheetsToRead.split(","); String[] aTestCaseNames =
		 * sTestCasesToExecute.split(","); int iLen = aSheets.length;
		 */
		/*
		 * System.clearProperty("TestCaseName");
		 * System.setProperty("TestCaseName", testName);
		 */
		readExcel.setSheetName(testName);
		Class<?> objClass = Class.forName("com.capgemini.scripts." + testName);

		Object obj = objClass.newInstance();
		Method method = objClass.getMethod("executeTestcase",
				RemoteWebDriver.class, String.class, String.class);
		Object[] args = { driver, host, browser };
		try {
			method.invoke(obj, args);
		} catch (Exception e) {
		}
		/*
		 * for (int k = 1; k < iLen; k++) { //String sCurrentSheet = aSheets[k];
		 * sCurrentTestCase = aTestCaseNames[k];
		 * 
		 * Class<?> objClass = Class.forName("com.capgemini.scripts."+testName);
		 * 
		 * Object obj = objClass.newInstance(); Method method =
		 * objClass.getMethod("executeTestcase"); method.invoke(obj);
		 * 
		 * System.clearProperty("TestCaseName");
		 * System.setProperty("TestCaseName", sCurrentTestCase); // String
		 * sClassToCall =
		 * getClassBySheet(sCurrentSheet);System.getProperty("TestCaseName"
		 * System.out.println(obj.getClass()); }
		 */}
	
	public void WriteExcelDataFile(String fileName, String TestSheetName,int expectedRowNumber,
			String expectedToken, String strCellValue)
	{
		try 
		{
		//	File dataFolder = new File(strAbsolutepath + "/data");
		//	fileName = strDataPath + "MasterSheet.xls";
			File f = new File(fileName);
			FileInputStream fsIP= new FileInputStream(f);
			 HSSFWorkbook wb = new HSSFWorkbook(fsIP);
	         
	         HSSFSheet dataSheet = wb.getSheet(TestSheetName);
	         HSSFRow dataRow = dataSheet.getRow(0);
	         Cell cell = null; 
	        // int i= worksheet.getRow(expectedRowNumber).getLastCellNum();
	      //   cell = worksheet.getRow(expectedRowNumber).getCell(i);  
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
	         FileOutputStream output_file =new FileOutputStream(new File(fileName)); 
	         wb.write(output_file); 
	           
	         output_file.close();  
		} catch( Exception e){
			e.printStackTrace();
		}
	}

}
