package com.capgemini.executor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.capgemini.utilities.Utilities;

public class WriteMaster {

	private static String strAbsolutepath = new File("").getAbsolutePath();
	// private String strExcelDataFilePath = strAbsolutepath +
	// "/src/com/capgemini/data/";
	private static String strExcelDataFilePath = null;
	Utilities utils = new Utilities();
	FileInputStream fileIn=null;
	FileOutputStream fileOut=null;
	HSSFWorkbook workbook=null;
	HSSFSheet s1 =null,s2 =null;
	HSSFCellStyle cellStyle=null;
	HSSFSheet newSheet;
	HSSFRow	newRow;
	HSSFRow headerRow=null;
	String[] browser=  {"IE","FF","Chrome","FireFox","Internet Explorer"};
	
	Writer wr= new Writer();
	int numRows;
	

	private HSSFCellStyle setHeaderStyle(HSSFWorkbook sampleWorkBook) {
		HSSFFont font = sampleWorkBook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setColor(IndexedColors.PLUM.getIndex());
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle cellStyle = sampleWorkBook.createCellStyle();
		cellStyle.setFont(font);
		return cellStyle;
	}

	public void createDataWorkbook(String appName,String appUrl) {
		String strExcelDataFileName = null;
		List<String> lstComponents = new ArrayList<String>(0);
		Utilities utils = new Utilities();
		
		try {
			File dataFolder = new File(strAbsolutepath + "/data");
			if (!dataFolder.exists()) {
				dataFolder.mkdir();
			}
			strExcelDataFilePath=strAbsolutepath + "/data";
			
			strExcelDataFileName = strExcelDataFilePath + "/MasterSheet.xls";
			File f = new File(strExcelDataFileName);
			if(f.exists()){ //checking where mastersheet exist of not
			fileIn = new FileInputStream(strExcelDataFileName);
			workbook = new HSSFWorkbook(fileIn);
			s1 = workbook.getSheetAt(0);
			//println("Active Sheet"+s1.getSheetName());
			numRows= s1.getPhysicalNumberOfRows();
			//System.out.println(numRows);
			
			//fileIn.close();
			}
			else{ //if not exist create new
				fileOut = new FileOutputStream(strExcelDataFileName);
				workbook= new HSSFWorkbook();
				s1=workbook.createSheet("MasterSheet");
				
				if(numRows==0){
					headerRow = s1.createRow(0);
					createHeader();
					numRows=1;
				}
			}
			
			//s2=copyMasterSheet();
			//System.out.println("New Sheet Rows:"+s2.getPhysicalNumberOfRows());
			
			
			HSSFRow dataRow = null;
			
			int j=0;
			int i=0;
			
				
	
				dataRow = s1.createRow(numRows);
				dataRow.createCell(0).setCellValue(numRows);
				dataRow.createCell(1).setCellValue(appName);

				dataRow.createCell(2).setCellValue("");
				dataRow.createCell(3).setCellValue("No");
				
				
				newSheet= workbook.createSheet(appName);
			
				newRow=newSheet.createRow(0);
				newRow.createCell(0).setCellValue("Execute");
				newRow.createCell(1).setCellValue("URL");
				newRow.createCell(2).setCellValue("NEXT_URL");
				
				for(int k=3;k<wr.textList.size()+3;k++){
					newRow.createCell(k).setCellValue(wr.textList.get(k-3));
				}
				
				newRow=newSheet.createRow(1);
				newRow.createCell(0).setCellValue("Yes");
				newRow.createCell(1).setCellValue(appUrl);
				newRow.createCell(2).setCellValue("###");
				
				
				fileOut = new FileOutputStream(new File(strExcelDataFileName));
				workbook.write(fileOut);
				fileIn.close();
				fileOut.close();
			//System.out.println("Writer Finished");
		} catch (Exception e) {
			//System.out.println("ExecptionFailed");
			//System.out.println(e.getStackTrace());
		}
	}
	
	public void createHeader(){
		//System.out.println("Header Called");
		cellStyle = setHeaderStyle(workbook);

		HSSFCell firstHeaderCell = headerRow.createCell(0);
		firstHeaderCell.setCellStyle(cellStyle);
		firstHeaderCell.setCellValue(new HSSFRichTextString("TestCase ID"));
		
		
		HSSFCell secondHeaderCell = headerRow.createCell(1);
		secondHeaderCell.setCellStyle(cellStyle);
		secondHeaderCell.setCellValue(new HSSFRichTextString("AppName"));
		
		/*HSSFCell thirdHeaderCell = headerRow.createCell(2);
		thirdHeaderCell.setCellStyle(cellStyle);
		thirdHeaderCell.setCellValue(new HSSFRichTextString("TestCaseName"));*/
		
		HSSFCell fourthHeaderCell = headerRow.createCell(2);
		fourthHeaderCell.setCellStyle(cellStyle);
		fourthHeaderCell.setCellValue(new HSSFRichTextString("BrowserName"));
		
		HSSFCell fifththHeaderCell = headerRow.createCell(3);
		fifththHeaderCell.setCellStyle(cellStyle);
		fifththHeaderCell.setCellValue(new HSSFRichTextString("Execute"));
		
		
		HSSFCell sixthHeaderCell = headerRow.createCell(4);
		sixthHeaderCell.setCellStyle(cellStyle);
		sixthHeaderCell.setCellValue(new HSSFRichTextString("Results"));
		
		
		HSSFCell seventhHeaderCell = headerRow.createCell(5);
		seventhHeaderCell.setCellStyle(cellStyle);
		seventhHeaderCell.setCellValue(new HSSFRichTextString("URL"));
		
		
		
		
		
		
		
	}
	
	public static void updateNextURL(String sheetName,String URL){
		String strExcelDataFileName = null;
		
		try {
			File dataFolder = new File(strAbsolutepath + "/data");
			if (!dataFolder.exists()) {
				dataFolder.mkdir();
			}
			strExcelDataFilePath=strAbsolutepath + "/data";
			
			strExcelDataFileName = strExcelDataFilePath + "/MasterSheet.xls";
			File f = new File(strExcelDataFileName);
			FileInputStream fsIP= new FileInputStream(f);
			 HSSFWorkbook wb = new HSSFWorkbook(fsIP);
             
             HSSFSheet worksheet = wb.getSheet(sheetName);

             Cell cell = null; 
             
             cell = worksheet.getRow(1).getCell(4);  
               
             cell.setCellValue(URL);
               
             fsIP.close(); 
              
             FileOutputStream output_file =new FileOutputStream(new File(strExcelDataFileName)); 
             wb.write(output_file); 
               
             output_file.close();   

		}catch(Exception e){
			e.printStackTrace();
			}

	}
}