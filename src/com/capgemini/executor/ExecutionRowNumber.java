package com.capgemini.executor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * ExecutionRowNumber --- Class for the setting and getting the execution row number
 * @author   Prasad Joshi
 */


public class ExecutionRowNumber {
	
	private String strAbsolutepath = new File("").getAbsolutePath();
	private String strDataPath = strAbsolutepath + "/data/";
	
	public void setExecutionRowNumber(int rowNumber){
		String strRowDataFile = strDataPath + "Row.txt";
		FileWriter fwo;
		try {
			fwo = new FileWriter(strRowDataFile);
			BufferedWriter bwObj = new BufferedWriter(fwo);
			bwObj.write(Integer.toString(rowNumber));
			bwObj.close( );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getExecutionRowNumber(){
		
		String strRowDataFile = strDataPath + "Row.txt";
		String strLine = null;
		String strRowNumber = null;
		try {
			BufferedReader br = new BufferedReader( new FileReader(strRowDataFile));
			while ((strLine = br.readLine()) != null)   {
				strRowNumber = strLine;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Integer.parseInt(strRowNumber);
	}
}
