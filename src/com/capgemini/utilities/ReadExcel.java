package com.capgemini.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JFileChooser;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReadExcel {
	private String scenarioName;
	private String scenarioLinks;
	private String scenarioId;
	private String scenarioSheetName;
	private String scenarioBrowserName;
	private String scenarioExecute;
	private String scenarioResult;
	public String fileName;
	public String sheetName;
	public String strSheetsToExecute = "";
	public String strTestCaseNameToExecute = "";
	public int iNOfRows = 0;
	public int iReadCounter = 0;
	List<Integer> lRowIndex = new ArrayList<Integer>();
	//public static int counter;
	private Reporter reporter;
	static int strBrowsercount=0;
	List<String> strIdList=new ArrayList<String>();

	public String  strId ;
	
	public ReadExcel(Reporter reporter) {
		this.reporter = reporter;
	}

	public ReadExcel() {
		
	}

	public int getiNOfRows() throws Exception {
		File inputWorkbook = new File(fileName);
		reporter.counter = 0; // changing int counter to field
		Workbook w;
		System.out.println(sheetName);
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(sheetName);
			
			//System.out.println("before");
			
			iNOfRows = sheet.getRows();
			
			//System.out.println("after");
			for (int k = 1; k < iNOfRows; k++) {
				//if ((sheet.getCell(0, k).getContents().trim()).equals("Yes")) {

					reporter.counter = reporter.counter + 1;
					lRowIndex.add(k);
				//}
			}

		} catch (BiffException e) {
			e.printStackTrace();
		}
		/*
		 * System.clearProperty("NoOfRows"); System.setProperty("NoOfRows",
		 * String.valueOf(counter));
		 */
		return reporter.counter;
	}
	
	public Map<Integer, Integer> getiNOfRowsSeq() throws Exception {
		
		File inputWorkbook = new File(fileName);
		Map<Integer, Integer> tempMap = new HashMap();
		Workbook w;
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(sheetName);
			
			//System.out.println("before");
			
			iNOfRows = sheet.getRows();
			
			//System.out.println("after");
			for (int k = 1; k < iNOfRows; k++) {
				if ((sheet.getCell(0, k).getContents().trim()).equals("Yes")) {
					tempMap.put(k, 1);
				}else{
					tempMap.put(k, 0);
				}
			}

		} catch (BiffException e) {
			e.printStackTrace();
		}
		/*
		 * System.clearProperty("NoOfRows"); System.setProperty("NoOfRows",
		 * String.valueOf(counter));
		 */
		return tempMap;
	}
	public void setiNOfRows(int iNOfRows) {
		this.iNOfRows = iNOfRows;
	}

	public String getStrSheetsToExecute() {
		return strSheetsToExecute;
	}

	public void setStrSheetsToExecute(String strSheetsToExecute) {
		this.strSheetsToExecute = strSheetsToExecute;
	}

	public static Map<String, String> DataMap = new HashMap();

	public Map<String, String> getDataMap() {
		return DataMap;
	}

	public void setDataMap(Map<String, String> dataMap) {
		DataMap = dataMap;
	}

	public void setInputFile(String inputFile) {
		this.fileName = inputFile;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public void readByIndex(int i) throws IOException {
		File inputWorkbook = new File(fileName);
		Workbook w;
		// int noOfRows = Integer.parseInt(System.getProperty("NoOfRows"));
		int noOfRows = reporter.counter;

		try {
			w = Workbook.getWorkbook(inputWorkbook);
			
			//System.out.println(w);
			Sheet sheet = w.getSheet(sheetName);
			//System.out.println("Sheet Name:- " + sheet.getName());
			//System.out.println("readindex");
			for (int j = 0; j < sheet.getColumns(); j++) {
				//System.out.println("in for loop readindex");
				/*
				 * if ((sheet.getCell(0,
				 * l.get(counter)).getContents().trim()).equals("Yes")) {
				 * System.
				 * out.println("Cell :"+DataMap.containsKey((sheet.getCell(j,
				 * 0).getContents().trim())));
				 */
				if (DataMap.containsKey((sheet.getCell(j, 0).getContents()
						.trim()))) {
					DataMap.remove((sheet.getCell(j, 0).getContents().trim()));
				}
				DataMap.put(sheet.getCell(j, 0).getContents().trim(), sheet
						.getCell(j, lRowIndex.get(iReadCounter)).getContents()
						.trim());
			}
			iReadCounter = iReadCounter + 1;
			//System.out.println("done");
		} catch (BiffException e) {
			e.printStackTrace();
		}
	}

	public void read() throws IOException {
		File inputWorkbook = new File(fileName);
		Workbook w;
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(sheetName);
			//System.out.println("Sheet Name:- " + sheet.getName());
			for (int j = 0; j < sheet.getColumns(); j++) {
				DataMap.put(sheet.getCell(j, 0).getContents().trim(), sheet
						.getCell(j, 1).getContents().trim());
			}

		} catch (BiffException e) {
			e.printStackTrace();
		}
	}
	
	public Map<String, String> loadDataMap(int i) throws IOException{
		System.out.println("Readexcel:"+fileName+sheetName);
		File inputWorkbook = new File(fileName);
		Map<String, String> tempDataMap = new HashMap();
		Workbook w;
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(sheetName);
			//System.out.println("Sheet Name:- " + sheet.getName());
			for (int j = 0; j < sheet.getColumns(); j++) {
				tempDataMap.put(sheet.getCell(j, 0).getContents().trim(), sheet
						.getCell(j, i).getContents().trim());
			}

		} catch (BiffException e) {
			e.printStackTrace();
		}
		return tempDataMap;
	}

	public String ReadMasterSheet() throws IOException {
		File inputWorkbook = new File(fileName);
		String strInputfileName = System.getProperty("File");
				Workbook w;
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sMasterSheet = w.getSheet("MasterSheet");
			System.out.println("test"+sMasterSheet.getName());
			for (int i = 1; i < sMasterSheet.getRows(); i++) {
			// Get execute Condition.
				
				// Get execute Condition.
				String sCurrentExecutionCondition = sMasterSheet.getCell(3, i).getContents();
				
						//System.out.println(sCurrentExecutionCondition);
				
				if (sCurrentExecutionCondition.equalsIgnoreCase("Yes")) {
					strSheetsToExecute = strSheetsToExecute + ","
							+ sMasterSheet.getCell(1, i).getContents()+"#"+ sMasterSheet.getCell(2, i).getContents();
				
					strId=sMasterSheet.getCell(0, i).getContents();
					
					strIdList.add(strId);
					for(String s: strIdList )
					{
						System.out.println(s);
					}
				
				
				}
			}
		} catch (BiffException e) {
			e.printStackTrace();

		}
		//System.out.println(strSheetsToExecute);
		return strSheetsToExecute;

	}

	public String ReadMasterSheetTestCaseName() throws IOException {
		File inputWorkbook = new File(fileName);
		Workbook w;
		

		try {
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sMasterSheet = w.getSheet("MasterSheet");
			for (int i = 1; i < sMasterSheet.getRows(); i++) {
				// Get execute Condition.
				String sCurrentExecutionCondition = sMasterSheet.getCell(3, i)
						.getContents();
				
				if (sCurrentExecutionCondition.equalsIgnoreCase("Yes")) {
					strTestCaseNameToExecute = strTestCaseNameToExecute + ","
							+ sMasterSheet.getCell(1, i).getContents()+"#"+ sMasterSheet.getCell(2, i).getContents();
					
					strBrowsercount++;
					
				}
				
			}
		} catch (BiffException e) {
			e.printStackTrace();

		}
		return strTestCaseNameToExecute;

	}

	public void getFile() {

		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.showOpenDialog(null);
		String path = chooser.getSelectedFile().getAbsolutePath();
		//System.out.println(path);
	}
	public String browserName(String testCaseName,String strId) throws BiffException, IOException{
		String strInputfileName = System.getProperty("File");
		File inputWorkbook = new File(strInputfileName);
		Workbook w;
		String browserName = "";
		w = Workbook.getWorkbook(inputWorkbook);
		Sheet sMasterSheet = w.getSheet("MasterSheet");
	
			
			for (int i = 1; i < sMasterSheet.getRows(); i++) {
			// Get execute Condition.
				
			String sCurrentExecutionCondition = sMasterSheet.getCell(3, i).getContents();
			String temp=sMasterSheet.getCell(1, i).getContents();
			String strIdTemp = sMasterSheet.getCell(0, i).getContents();
				if (sCurrentExecutionCondition.equalsIgnoreCase("Yes") && temp.equalsIgnoreCase(testCaseName)&&strIdTemp.equalsIgnoreCase(strId)) {
					browserName = browserName 
							+ sMasterSheet.getCell(2, i).getContents();
					System.out.println(browserName);
					
				}
				
		System.out.println("statiic browser count browserName" + strBrowsercount);
		}
		
		
		return browserName;
		
	}
	public String browserName(String testCaseName) throws BiffException, IOException {
	
		String strInputfileName = System.getProperty("File");
		File inputWorkbook = new File(strInputfileName);
		Workbook w;
		String browserName = "";
		w = Workbook.getWorkbook(inputWorkbook);
		Sheet sMasterSheet = w.getSheet("MasterSheet");
		
			for (int i = 1; i < sMasterSheet.getRows(); i++) {
			// Get execute Condition.
				
			String sCurrentExecutionCondition = sMasterSheet.getCell(3, i).getContents();
			String temp=sMasterSheet.getCell(1, i).getContents();
			
			
		
				if (sCurrentExecutionCondition.equalsIgnoreCase("Yes") && temp.equalsIgnoreCase(testCaseName)) {
					browserName = browserName 
							+ sMasterSheet.getCell(2, i).getContents();
				}
		
		
			}
				
		System.out.println("statiic browser count" + strBrowsercount);
		for(String s:strIdList){
			 System.out.println("BrowserID  \t" +s);
		}
			

			return browserName;
	}
	
	public String getNextURL(String testCaseName) throws BiffException, IOException {

		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("./data/config.properties"));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String strInputfileName = properties.getProperty("file_path");
		File inputWorkbook = new File(strInputfileName);
		Workbook w;
		w = Workbook.getWorkbook(inputWorkbook);
		Sheet sMasterSheet = w.getSheet(testCaseName);
	
			

			// Get execute Condition.
				
			String nextURL = sMasterSheet.getCell(2, 1).getContents();
		

		return nextURL;
	}
	
	public void ReadScenarioName() throws IOException {
		File inputWorkbook = new File(fileName);
		Workbook w;
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sMasterSheet = w.getSheet("MasterSheet");
			for (int i = 1; i < sMasterSheet.getRows(); i++) {
				String sCurrentExecutionCondition = sMasterSheet.getCell(3, i)
						.getContents();
				if (sCurrentExecutionCondition.equalsIgnoreCase("Yes"))
				{
				// Get execute Condition.
				scenarioId=sMasterSheet.getCell(0, i).getContents();
				scenarioName = sMasterSheet.getCell(1, i).getContents();
				scenarioBrowserName=sMasterSheet.getCell(2, i).getContents();
				scenarioExecute=sMasterSheet.getCell(3, i).getContents();
				scenarioResult=sMasterSheet.getCell(4, i).getContents();
				scenarioLinks=sMasterSheet.getCell(5, i).getContents();
				/*System.out.println(scenarioName);
				System.out.println(scenarioSheetName);
				System.out.println(scenarioBrowserName);
				System.out.println(scenarioExecute);
				System.out.println(scenarioResult);
				System.out.println(scenarioLinks);
				System.out.println("inside in loop");*/
				reporter.writeTestSumary(scenarioId, scenarioName, scenarioBrowserName, scenarioExecute, scenarioResult, scenarioLinks);
				
				}
				
			}
		} catch (BiffException e) {
			e.printStackTrace();

		}
		
		
	}

	
}
