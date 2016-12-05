package com.capgemini.utilities;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.capgemini.driver.StepExecutor;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReadPDF {
	public String fileName;
	RemoteWebDriver driver;
	
	List<Integer> lRowIndex = new ArrayList<Integer>();
	RemoteWebDriver webDriver = null;
	//public static int counter;
	private Reporter reporter;
	static int strBrowsercount=0;
	List<String> strIdList=new ArrayList<String>();
	private StepExecutor stepExecutor = new StepExecutor(reporter);
	public String  strId ;
	private static String strAbsolutepath = new File("").getAbsolutePath();
	private static String strDataPath = strAbsolutepath + "//data//";
	
	public ReadPDF(Reporter reporter) {
		this.reporter = reporter;
	}

	public ReadPDF() {
		
	}
	
	public static Map<String, String> DataMap = new HashMap();

	public Map<String, String> getDataMap() {
		return DataMap;
	}

	public void setDataMap(Map<String, String> dataMap) {
		DataMap = dataMap;
	}

	public void setInputPDFFile(String inputPDFFile) {
		this.fileName = inputPDFFile;
	}
	
	public String openPDFinnewTab(String strpdfElement) {
		
		//Open PDF 
		String pdfurl = null;
		try {
			String currenturl = webDriver.getCurrentUrl();
			if (webDriver.findElement(By.xpath(strpdfElement)).isDisplayed())
			{
				stepExecutor.clickImage("findElementByXPath",strpdfElement,webDriver,"open_pdf");
				webDriver.manage().timeouts().implicitlyWait(60,TimeUnit.SECONDS);
				Thread.sleep(60000);
				SwitchHandleToNewWindow(webDriver, "https://dev.forms.thewarrantygroup.com/FormsService/pdf/");
				 pdfurl = webDriver.getCurrentUrl();
				System.out.println(pdfurl);
			/*	String Filepath = "C:\\Users\\srinivas\\Downloads";
				File  file1 = new File(Filepath);
				 // get all the files from a directory
				 File directory = new File(Filepath);

			    File[] fList = directory.listFiles();

			    for (File file : fList) {
			    	if (file.getName().endsWith(".pdf")) {
			            System.out.println(file.getAbsolutePath());
			            pdfurl = file.getAbsolutePath(); 
			        } */
				
			    }
			//	webDriver.get("https://dev.forms.thewarrantygroup.com/FormsService/pdf/19974");
				//System.out.println(pdfurl);
			
				/*String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,Keys.RETURN); 
				webDriver.navigate().to(pdfurl);
				webDriver.findElement(By.linkText(pdfurl)).sendKeys(selectLinkOpeninNewTab);
				System.out.println("harsha3");*/
			
				
			    
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return pdfurl;
	
	}
	
	public void SwitchHandleToNewWindow(WebDriver driver, String windowTitle)
	{
		ArrayList<String> tabs2 = new ArrayList<String> (webDriver.getWindowHandles());
		webDriver.switchTo().window(tabs2.get(1));
		//webDriver.navigate().to(windowTitle);
		webDriver.manage().timeouts().implicitlyWait(600,TimeUnit.SECONDS);
	}
	
	
	
	public String  openPDFfromfilelocation(String PDFFilePath) {
		
		//Open PDF 
		String pdfurl = null;
		try {
			    //open PDF file
			File file = new File(PDFFilePath);
			String filename = getFilename(PDFFilePath);
		    if (file.toString().endsWith(".pdf")) 
		    {    
		    	filename = getFilename(PDFFilePath);
		    	//Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file);
		    	
		    	pdfurl = file.getAbsolutePath() + filename;
			}
		else {
		        Desktop desktop = Desktop.getDesktop();
		        File file1 = new File(strDataPath + filename);
				//desktop.open(file1);
		}
				
			    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pdfurl;
	
	}
	
	public String getFilename (String PDFFilepath){
		File folder = new File(PDFFilepath);
		File[] listOfFiles = folder.listFiles();
		String filename = null;
		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        //System.out.println(listOfFiles[i].getName());
		        if (listOfFiles[i].getName().endsWith(".pdf") || listOfFiles[i].getName().endsWith(".PDF"))
		        {
		        	 System.out.println(listOfFiles[i].getName());
		        	 filename= listOfFiles[i].getName();
		        }
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		      }
		    }
			return filename;
	}
	
	

	public String readPDFFromFileLocation(String PDFurl) {
		String output = null;
		try {
		//	webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		//	URL url = new URL(webDriver.getCurrentUrl());
			
			FileInputStream fileToParse= new FileInputStream(new File(PDFurl));
			PDFParser parser = new PDFParser(fileToParse);
			parser.parse();
			System.setProperty("org.apache.pdfbox.baseParser.pushBackSize", "990000");
			output = new PDFTextStripper().getText(parser.getPDDocument());
			System.out.println(output);
			
			Thread.sleep(5000);
			writePDFContenttotextfile(output);
			parser.getPDDocument().close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	
	public String readPDF(String PDFurl) {
		String output = null;
		try {
			webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			URL url = new URL(webDriver.getCurrentUrl());
			BufferedInputStream fileToParse = new BufferedInputStream(url.openStream());
			PDFParser parser = new PDFParser(fileToParse);
			parser.parse();
			System.setProperty("org.apache.pdfbox.baseParser.pushBackSize", "990000");
			output = new PDFTextStripper().getText(parser.getPDDocument());
			webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			writePDFContenttotextfile(output);
			
			webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			parser.getPDDocument().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	public String ExtractPDFValue(String FileName,String KeyName) throws IOException
	{
		String KeyValue= null;
		URL url = new URL(webDriver.getCurrentUrl());
		BufferedInputStream fileToParse = new BufferedInputStream(url.openStream());
		PdfReader reader = new PdfReader(fileToParse);
	    int n = reader.getNumberOfPages();
	      
	    String str=PdfTextExtractor.getTextFromPage(reader, 1); //Extracting the content from a particular page.
	    CreateOutputfile();
        writePDFContenttotextfile(str);
        BufferedReader in = new BufferedReader(new FileReader(FileName));
        String output = in.readLine();
	    while (!output.contains(KeyName))
	      {
		   output = in.readLine();
 
	      }
	  //  output = in.readLine();
	  //  output = in.readLine();
	    KeyValue = output;
	  
	    KeyValue = KeyValue.substring(KeyValue.lastIndexOf(" ") + 1 , KeyValue.length());
	    KeyValue = KeyValue.trim();
	    System.out.println(KeyValue);
		return KeyValue;
		
		
	}
	
	
	public Boolean checkPDFContent(String output,String checkValue) throws IOException
	{Boolean result = false;
	int resultcount=0;
	  if(output.contains(checkValue))
	   {
			//System.out.println(output + checkValue);
			reporter.writeStepResult("PDF Verification","", checkValue, "Pass", "Expected text  is present in PDF file",true, webDriver);
			result = true;
	   } else {
		  // output = in.readLine();
		   reporter.writeStepResult(
					"PDF Verification",
					"", checkValue
							 , "Fail", "Expected text  is not present in PDF file",
					true, webDriver);
			
			result = false;

	   }

	  resultcount++;
	   return result;
	} 
	
		
	public String ExtractTextWithPattern(String pattern) throws FileNotFoundException{
		
		String filepath =strDataPath+"outputPDF.txt";
		String output = null;
		BufferedReader in = new BufferedReader(new FileReader(filepath));
	    try {
			  output = in.readLine();
		      while (!output.startsWith(pattern))
		      {
		    	  output = in.readLine();
		    	  
		      }
		 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
		return output;
		
	}
	
	public void CreateOutputfile()
	{
		try{
	          File f=new File(strDataPath+"outputPDF.txt");
	          if (!f.exists()){
	        	  f.createNewFile();
	          }else {
	        	  f.delete();
	        	  f.createNewFile();
	          }
	        }
	        catch(Exception e){ 
	                System.out.println(e);
	        }
	}
	
	public void DeleteOutputfile()
	{
		try{
	          File f=new File(strDataPath+"outputPDF.txt");
	          f.delete();
	        }
	        catch(Exception e){ 
	                System.out.println(e);
	        }
	}
	
	public void	writePDFContenttotextfile(String output)
	{
		try 
		{
		
			CreateOutputfile();
			BufferedWriter out = new BufferedWriter(new FileWriter(strDataPath+"outputPDF.txt"));
		    
		    out.write(output);  
		    out.close();		
				
		}catch (IOException e)
		{
		    System.out.println("Exception ");

		}
	}
	

	//Extract PageIndex from PDF file
	public static void getPdfIndexPages(File file){
        PDFParser parser;
        COSDocument cosDoc = null;
        PDDocument pdDoc = null ;
        try {
            parser = new PDFParser(new FileInputStream(file));
            parser.parse();
            cosDoc = parser.getDocument();
            pdDoc = new PDDocument(cosDoc);
            // logic to extract page indexes and page names
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (cosDoc != null)
                    cosDoc.close();
                if (pdDoc != null)
                    pdDoc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}
	
	

	
}
