package com.capgemini.executor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFName;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.capgemini.utilities.Utilities;

/**
 * WriteTestData --- Class for the writing the test data from excel files to text data files
 * @author   Prasad Joshi
*/

public class WriteTestData {
	
	private String strAbsolutepath = new File("").getAbsolutePath();
	//private String strDataPath = strAbsolutepath + "/src/com/capgemini/data/";
	private String strDataPath = strAbsolutepath + "/data/";
	Utilities utils = new Utilities();
	
	public void updateTextDataFile(){
		
		FileWriter fwo = null;
		String strTextDataFile = null;
		BufferedWriter bwObj = null;
		BufferedReader br = null;
		
		String strExcelDataFileName = strDataPath
				+ utils.getConfigValues("Application Name") + "_Data.xls";
		POIFSFileSystem fs;
		String strCellValue = "";
		boolean isExpectedComponent = false;
		int totalSheets = 0;
		try {
			fs = new POIFSFileSystem(new FileInputStream(strExcelDataFileName));
			HSSFWorkbook workbook = new HSSFWorkbook(fs);
			HSSFSheet dataSheet = null;
			HSSFRow row = null;
			HSSFCell cell = null;
			//HSSFName sheetName = null;
			totalSheets = workbook.getNumberOfSheets();
			String strDatSheetName = null;
			int totalRows = 0;
			int totalCells = 0;
			String strData = null;
			
			for (int x = 0; x < totalSheets; x++) {
				strDatSheetName = workbook.getSheetName(x);
				
				//System.out.println("Sheet Name--" + strDatSheetName);
				if (!strDatSheetName.equals("Scenario")) {
					dataSheet = workbook.getSheet(strDatSheetName);
					totalRows = dataSheet.getLastRowNum();
					
					for (int i = 0; i <= totalRows; i++) {
						row = dataSheet.getRow(i);
						if(row!=null){
							totalCells = row.getLastCellNum();
							strData = "";
							for (int j = 0; j < totalCells; j++) {
								cell = row.getCell(j);
								if(cell==null){
									strCellValue=" ";
								}else{
									strCellValue = row.getCell(j).toString();
								}
								if (strCellValue != null) {
									strData = strData + strCellValue;
									if(j!=totalCells - 1){
										strData = strData + "##";
									}
								}
								
							}
							strTextDataFile = strDataPath
									+ utils.getConfigValues("Application Name")
									+ "_" + strDatSheetName + "_Data.txt";
							
							if(i==0){
								fwo = new FileWriter(strTextDataFile);
							}else{
								fwo = new FileWriter(strTextDataFile, true);
							}
							bwObj = new BufferedWriter(fwo);
							if(i!=0){
								bwObj.newLine();
							}
							bwObj.write(strData);
							bwObj.close();
						}
					}
					
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
