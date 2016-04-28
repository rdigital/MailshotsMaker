package com.capgemini.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * ExcelDataCreation --- Class for the genearting Execl data files using text
 * data files
 * 
 * @author Prasad Joshi
 */

public class ExcelDataCreation {

	private String strAbsolutepath = new File("").getAbsolutePath();
	// private String strExcelDataFilePath = strAbsolutepath +
	// "/src/com/capgemini/data/";
	private String strExcelDataFilePath = strAbsolutepath + "/data/";
	Utilities utils = new Utilities();

	private HSSFCellStyle setHeaderStyle(HSSFWorkbook sampleWorkBook) {
		HSSFFont font = sampleWorkBook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setColor(IndexedColors.PLUM.getIndex());
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle cellStyle = sampleWorkBook.createCellStyle();
		cellStyle.setFont(font);
		return cellStyle;
	}

	public void createDataWorkbook() {
		String strExcelDataFileName = null;
		List<String> lstComponents = new ArrayList<String>(0);
		Utilities utils = new Utilities();

		try {
			File dataFolder = new File(strAbsolutepath + "/data/"
					+ utils.getConfigValues("Application Name"));
			if (!dataFolder.exists()) {
				dataFolder.mkdir();
			}
			strExcelDataFileName = strExcelDataFilePath
					+ utils.getConfigValues("Application Name") + "/"
					+ utils.getConfigValues("Application Name") + "_Data.xls";
			FileOutputStream fileOut = new FileOutputStream(
					strExcelDataFileName, false);
			HSSFWorkbook workbook = new HSSFWorkbook();
			lstComponents = utils.getComponentNames();
			HSSFSheet s1 = null;

			s1 = workbook.createSheet("Scenario");
			HSSFRow headerRow = s1.createRow(0);
			HSSFRow dataRow = null;

			// Modified for GP
			dataRow = s1.createRow(1);
			dataRow.createCell(0).setCellValue("launchApplication");
			dataRow.createCell(1).setCellValue("1");
			dataRow.createCell(2).setCellValue("No");
			// End of modification

			for (int x = 0; x < lstComponents.size(); x++) {
				dataRow = s1.createRow(x + 2); // Modified for GP (dataRow =
												// s1.createRow(x+1))
				dataRow.createCell(0).setCellValue(lstComponents.get(x));
				dataRow.createCell(1).setCellValue("1");
				dataRow.createCell(2).setCellValue("Yes");
			}

			HSSFCellStyle cellStyle = setHeaderStyle(workbook);

			HSSFCell firstHeaderCell = headerRow.createCell(0);
			firstHeaderCell.setCellStyle(cellStyle);
			firstHeaderCell.setCellValue(new HSSFRichTextString("Scenario"));
			HSSFCell secondHeaderCell = headerRow.createCell(1);
			secondHeaderCell.setCellStyle(cellStyle);
			secondHeaderCell.setCellValue(new HSSFRichTextString("Row"));
			HSSFCell thirdHeaderCell = headerRow.createCell(2);
			thirdHeaderCell.setCellStyle(cellStyle);
			thirdHeaderCell.setCellValue(new HSSFRichTextString("Execute"));
			HSSFCell fourthHeaderCell = headerRow.createCell(3);
			fourthHeaderCell.setCellStyle(cellStyle);
			fourthHeaderCell.setCellValue(new HSSFRichTextString(
					"Result Status"));

			HSSFCell headerCell = null;
			// Modified for GP
			s1 = workbook.createSheet("launchApplication");
			headerRow = s1.createRow(0);
			firstHeaderCell = headerRow.createCell(0);
			firstHeaderCell.setCellStyle(cellStyle);
			firstHeaderCell.setCellValue(new HSSFRichTextString("URL"));
			dataRow = s1.createRow(1);
			dataRow.createCell(0).setCellValue(
					utils.getConfigValues("Application URL"));
			// End of modification
			for (int i = 0; i < lstComponents.size(); i++) {
				s1 = workbook.createSheet(lstComponents.get(i));
			}

			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			System.out.println("Failed");
			System.out.println(e.getMessage());
		}
	}

	public void writeExcelData(String[] arrValues, String strSheetName) {
		String strExcelDataFileName = null;
		List<String> lstComponents = new ArrayList<String>(0);

		File dataFolder = new File(strAbsolutepath + "/data/"
				+ utils.getConfigValues("Application Name"));
		if (!dataFolder.exists()) {
			dataFolder.mkdir();
		}

		strExcelDataFileName = strExcelDataFilePath + "/"
				+ utils.getConfigValues("Application Name") + "/"
				+ utils.getConfigValues("Application Name") + "_Data.xls";

		try {
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(
					strExcelDataFileName));
			HSSFWorkbook workbook = new HSSFWorkbook(fs);
			lstComponents = utils.getComponentNames();
			HSSFSheet dataSheet = null;

			dataSheet = workbook.getSheet(strSheetName);

			if (dataSheet == null) {
				dataSheet = workbook.createSheet(strSheetName);
			}

			HSSFRow headerRow = dataSheet.createRow(0);
			HSSFCellStyle cellStyle = setHeaderStyle(workbook);

			for (int i = 0; i < arrValues.length; i++) {
				headerRow.createCell(i).setCellValue(arrValues[i]);
			}
			FileOutputStream fileOut = new FileOutputStream(
					strExcelDataFileName);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
			System.out.println(e1.getCause());
		}

	}
}
