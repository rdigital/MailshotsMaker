package com.capgemini.executor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Writer {
	String imports=null;
	
	Iterator itr;
	FileWriter writer;	
	String fh=null;
	String ft=null;
	char[] chr = new char[4096];
	String strHead=null;
	String strFoot=null;
	Cafe_UI cu;
	private static Map <String, String> objects = new HashMap();
	public static List<String> textList = new ArrayList();
	

	
	public String getElementXPath(WebDriver webDriver, WebElement element) {
	    return (String)((JavascriptExecutor)webDriver).executeScript("gPt=function(c){if(c.id!==''){return'id(\"'+c.id+'\")'}if(c===document.body){return c.tagName}var a=0;var e=c.parentNode.childNodes;for(var b=0;b<e.length;b++){var d=e[b];if(d===c){return gPt(c.parentNode)+'/'+c.tagName+'['+(a+1)+']'}if(d.nodeType===1&&d.tagName===c.tagName){a++}}};return gPt(arguments[0]).toLowerCase();", element);
	    //return (String)((JavascriptExecutor)webDriver).executeScript("gPt=function(c){if(c.id!==''){return'//'+ c.tagName+'[@id=\"'+c.id+'\"]'}if(c===document.body){return c.tagName}var a=0;var e=c.parentNode.childNodes;for(var b=0;b<e.length;b++){var d=e[b];if(d===c){return gPt(c.parentNode)+'/'+c.tagName+'['+(a+1)+']'}if(d.nodeType===1&&d.tagName===c.tagName){a++}}};return gPt(arguments[0]).toLowerCase();", element);
    	//String xPath = webDriver.ge
	}

public void generatePre(String strScriptFile,String appname) throws Exception{
	try{
		String sheetname=null;
		sheetname=appname+"_TC_1";
		writer= new FileWriter(strScriptFile);
		fh="src/com/capgemini/driver/headTemplate.txt";
		strHead =readFile(fh);
		strHead=strHead.replace("APPNAME", appname);
		strHead=strHead.replace("SNAME", sheetname);
		//head template
		

		
		writer.write(strHead);
	}catch (IOException e){
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	finally{
		writer.close();
	}
	
}


public void generatePost(String strScriptFile) throws Exception{
	try{
		writer= new FileWriter(strScriptFile, true);
		ft="src/com/capgemini/driver/footTemplate.txt";
		strFoot =readFile(ft);
		
		writer.write(strFoot);
	}catch (IOException e){
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	finally{
		writer.close();
	}
	
}
public void funcPre(String strScriptFile,String appname) throws Exception{
	try{
		writer= new FileWriter(strScriptFile,true);
		fh="src/com/capgemini/driver/funcHead.txt";
		strHead =readFile(fh);
		if(!cu.exFlag){
		strHead=strHead.replace("FUNCTIONAME", "testcaseMain");
		strHead=strHead.replace("IFMAINFUNCTION", "" + "stepExecutor.launchApplication(\"URL\", DataMap, webDriver);\r\n");
		//head template
		
		}else{
			strHead=strHead.replace("FUNCTIONAME", cu.newFunc);
			strHead=strHead.replace("IFMAINFUNCTION", "");
		}
		writer.write(strHead);
	}catch (IOException e){
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	finally{
		writer.close();
	}
	
}


public void funcPost(String strScriptFile) throws Exception{
	try{
		writer= new FileWriter(strScriptFile, true);
		ft="src/com/capgemini/driver/funcFoot.txt";
		strFoot =readFile(ft);
		writer.write(strFoot);
	}catch (IOException e){
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	finally{
		writer.close();
	}
	
}

public void updateScript(WebDriver webDriver,String strScriptFile,String appname,String existing) throws Exception{
	
	String[] split=null;
	String oldScript=strScriptFile;
	try{
		
		String text = new Scanner( new File(oldScript) ).useDelimiter("\\A").next();
		strScriptFile=strScriptFile+".TMP";
		writer= new FileWriter(strScriptFile);
		text= text.replace("//NextFunctionCall",appname+"();\r\n//NextFunctionCall");
		split=text.split("//NextFuncBody");
		writer.write(split[0]);
	}catch (IOException e){
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	finally{
		writer.close();
	}
	funcPre(strScriptFile, appname);
	generateCodeForTextboxes(webDriver, strScriptFile, existing);
	generateCodeForButtons(webDriver, strScriptFile, existing);
	generateCodeForCheckboxes(webDriver, strScriptFile, existing);
	generateCodeForRadioButtons(webDriver, strScriptFile, existing);
	generateCodeForDropdowns(webDriver, strScriptFile, existing);
	generateCodeForImages(webDriver, strScriptFile, existing);
	generateCodeForLinks(webDriver, strScriptFile, existing);
	funcPost(strScriptFile);
	try{
		writer= new FileWriter(strScriptFile,true);
		writer.write(split[1]);
	}catch (IOException e){
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	finally{
		writer.close();
	}
	File file = new File(oldScript);
	file.delete();
	
	File file1 = new File(strScriptFile);
	file1.renameTo(file);
}

public void generateCodeForTextboxes(WebDriver webDriver, String strScriptFile,String appname){
		
		List<WebElement> allElements = null;
		String [] arrElementTypes = new String [] {"//input[@type='text']", "//input[@type='password']","//input[@type='textarea']","//input[@class='textarea']","//input[@type='email']"};
		BufferedWriter objBufferedWriter = null;
		int lstSize = 0;
		// Adding field names to array for textbox
		String [] arrElements = null;
		String strAttribute = null;
		String strTab = "\t";
		String strDriverMethod = null;
		String strColumnName = null;
		FileWriter fileWriter = null;
		
		//code to clear map for property values
				objects.clear();
				
				try{
					//System.out.println(strScriptFile);
					fileWriter = new FileWriter(strScriptFile, true);
					objBufferedWriter = new BufferedWriter(fileWriter);
					for(int elements=0; elements<arrElementTypes.length;elements++){
						arrElements = null;
						allElements = webDriver.findElements(By.xpath(arrElementTypes[elements]));
						//allElements = webDriver.findElementsByXPath(arrElementTypes[elements]);
						lstSize = allElements.size();
						arrElements = new String[lstSize];
						int i = 0;
						//for(int i=0;i<arrElements.length;i++){
						if (lstSize > 0) {
							objBufferedWriter.newLine();
							objBufferedWriter.write(strTab);
							objBufferedWriter.write(strTab);
							objBufferedWriter.write(strTab);
							objBufferedWriter.write("// Code for textboxes starts");
							objBufferedWriter.newLine();
							objBufferedWriter.newLine();
						}
						for (WebElement textBox : allElements) {
							if (textBox.getAttribute("name") != null && !textBox.getAttribute("name").equalsIgnoreCase("")) {
								strAttribute = textBox.getAttribute("name");
								strColumnName = strAttribute;
								for(int m=0; m<arrElements.length;m++){
									if(strColumnName.equalsIgnoreCase(arrElements[m])){
										strColumnName = strAttribute +m;
									}
								}
								arrElements[i] = strAttribute;
								strDriverMethod = "findElementByName";
							} else if (textBox.getAttribute("id") != null && !textBox.getAttribute("id").equalsIgnoreCase("")) {
								strAttribute = textBox.getAttribute("id");
								arrElements[i] = strAttribute ;
								strDriverMethod = "findElementById";
							} else {
								strAttribute = getElementXPath(webDriver, textBox);
								strAttribute = strAttribute.replaceAll("\"", "'");
								arrElements[i] = "Textbox_Xpath";
								strDriverMethod = "findElementByXPath";
							}

							objBufferedWriter.write(strTab);
							objBufferedWriter.write(strTab);
							objBufferedWriter.write(strTab);
							
							objBufferedWriter.write("stepExecutor.enterTextValue(\"" + strDriverMethod  + "\", \"" + escape(arrElements[i]) + "\", DataMap,  \"" + arrElements[i] + "\", webDriver,\""+appname+"\");");
							
							textList.add(arrElements[i]);
							
							// Add object to Hash Map
							objects.put(arrElements[i], strAttribute);
							objBufferedWriter.newLine();
							
							
							
							objBufferedWriter.newLine();
							i++;
						}
						
						
					}
					objBufferedWriter.close();
				}catch (IOException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
}
	
public void generateCodeForButtons(WebDriver webDriver, String strScriptFile,String appname){
	
	List<WebElement> allElements = null;
	String [] arrElementTypes = new String [] {"//input[@type='button']", "//input[@type='submit']","button"};
	BufferedWriter objBufferedWriter = null;
	int lstSize = 0;
	String strAttribute = null;
	String strTab = "\t";
	String strDriverMethod = null;

	FileWriter fileWriter = null;
	try{
		fileWriter = new FileWriter(strScriptFile, true);
		objBufferedWriter = new BufferedWriter(fileWriter);
		for (int elements = 0; elements < arrElementTypes.length; elements++) {
			if(elements > 1){
				allElements = webDriver.findElements(By.tagName(arrElementTypes[elements]));
			}else{
				allElements = webDriver.findElements(By.xpath(arrElementTypes[elements]));
			}	
			lstSize = allElements.size();
	
			if (lstSize > 0) {
				objBufferedWriter.newLine();
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write("// Code for buttons starts");
			}
			for (WebElement button : allElements) {
				if (button.getAttribute("name") != null && !button.getAttribute("name").equalsIgnoreCase("")) {
					strAttribute = button.getAttribute("name");
					strDriverMethod = "findElementByName";
					//objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					/*objBufferedWriter.write("webDriver." + strDriverMethod
							+ "(\"" + strAttribute + "\")" + ".click();");*/
					objBufferedWriter.write("stepExecutor.clickButton(\"" + strDriverMethod  + "\", \"" + escape(strAttribute) + "\", webDriver,\""+appname+"\");");
					
					
					objBufferedWriter.newLine();
				}else if (button.getAttribute("value") != null && !button.getAttribute("value").equalsIgnoreCase("")) {
					strAttribute = button.getAttribute("value");
					strDriverMethod = "findElementByXPath";
					//objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					if(elements > 1){
						/*objBufferedWriter.write("webDriver." + strDriverMethod
												+ "(\"//button[@value='" + strAttribute + "']\")" + ".click();");*/
						objBufferedWriter.write("stepExecutor.clickButton(\"" + strDriverMethod  + "\", \"//button[@value='" + escape(strAttribute) + "']\", webDriver,\""+appname+"\");");
						
						
					}else{
						
						objBufferedWriter.write("stepExecutor.clickButton(\"" + strDriverMethod  + "\", \"//input[@value='" + escape(strAttribute) + "']\", webDriver,\""+appname+"\");");
						//objBufferedWriter.write("stepExecutor.enterTextValue(\"" + strDriverMethod  + "\", getObjectProperty(\"" + arrElements[i] + "\"), DataMap,  \"" + arrElements[i] + "\", webDriver);");
						
						objBufferedWriter.newLine();
						
					}
					objBufferedWriter.newLine();
				}else if (button.getAttribute("id") != null && !button.getAttribute("id").equalsIgnoreCase("")) {
					strAttribute = button.getAttribute("id");
					strDriverMethod = "findElementById";
					//objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					/*objBufferedWriter.write("webDriver." + strDriverMethod
							+ "(\"" + strAttribute + "\")" + ".click();");*/
					objBufferedWriter.write("stepExecutor.clickButton(\"" + strDriverMethod  + "\", \"" + escape(strAttribute) + "\", webDriver,\""+appname+"\");");
					
					
					objBufferedWriter.newLine();
				} else {
					strAttribute = getElementXPath(webDriver, button);
					strAttribute = strAttribute.replaceAll("\"", "'");
					strDriverMethod = "findElementByXPath";
					//objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					/*objBufferedWriter.write("webDriver." + strDriverMethod
							+ "(\"" + strAttribute + "\")" + ".click();");*/
					objBufferedWriter.write("stepExecutor.clickButton(\"" + strDriverMethod  + "\", \"" + escape(strAttribute) + "\", webDriver,\""+appname+"\");");
					
					objBufferedWriter.newLine();
				}
				
			}
		}
		objBufferedWriter.close();
	}catch (IOException e){
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

public void generateCodeForCheckboxes(WebDriver webDriver, String strScriptFile,String appname){
	
	List<WebElement> allElements = null;
	String [] arrElementTypes = new String [] {"//input[@type='checkbox']"};
	BufferedWriter objBufferedWriter = null;
	int lstSize = 0;
	// Adding field names to array for textbox
	String [] arrElements = null;
	String strAttribute = null;
	String strTab = "\t";
	String strDriverMethod = null;
	//String strDataFileName = null;
	//strDataFileName = utils.getConfigValues("Data File");
	
	FileWriter fileWriter = null;
	try{
		fileWriter = new FileWriter(strScriptFile, true);
		objBufferedWriter = new BufferedWriter(fileWriter);
		for(int elements=0; elements<arrElementTypes.length;elements++){
			allElements = webDriver.findElements(By.xpath(arrElementTypes[elements]));
			lstSize = allElements.size();
			arrElements = new String[lstSize];
			int i = 0;
			int x = 0;

			if(lstSize>0){
				objBufferedWriter.newLine();
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write("// Code for checkboxes starts");
				objBufferedWriter.newLine();
				
			}	
			for (WebElement checkBox : allElements) {
				if (checkBox.getAttribute("name") != null && !checkBox.getAttribute("name").equalsIgnoreCase("")) {
					strAttribute = checkBox.getAttribute("name");
					arrElements[i] = "Checkbox_" + strAttribute + "_Name";
					strDriverMethod = "findElementByName";
				} else if (checkBox.getAttribute("id") != null && !checkBox.getAttribute("id").equalsIgnoreCase("")) {
					strAttribute = checkBox.getAttribute("id");
					arrElements[i] = "Checkbox_" + strAttribute + "_ID";
					strDriverMethod = "findElementById";
				} else {
					strAttribute = getElementXPath(webDriver, checkBox);
					strAttribute = strAttribute.replaceAll("\"", "'");
					arrElements[i] = "Checkbox_Xpath";
					strDriverMethod = "findElementByXPath";
				}
				objBufferedWriter.newLine();
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write("stepExecutor.changeCheckboxStatus(\"" + strDriverMethod  + "\", \"" + escape(strAttribute) + "\", DataMap,  \"" + arrElements[i] + "\", webDriver,\""+appname+"\");");
				
			}
			
		}
		objBufferedWriter.close();
	}catch (IOException e){
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}
	
public void generateCodeForRadioButtons(WebDriver webDriver, String strScriptFile,String appname){
	
	List<WebElement> allElements = null;
	String [] arrElementTypes = new String [] {"//input[@type='radio']"};
	BufferedWriter objBufferedWriter = null;
	int lstSize = 0;
	// Adding field names to array for textbox
	String [] arrElements = null;
	String strAttribute = null;
	String strTab = "\t";
	String strDriverMethod = null;
	//String strDataFileName = null;
	//strDataFileName = utils.getConfigValues("Data File");
	
	FileWriter fileWriter = null;
	try{
		fileWriter = new FileWriter(strScriptFile, true);
		objBufferedWriter = new BufferedWriter(fileWriter);
		for(int elements=0; elements<arrElementTypes.length;elements++){
			allElements = webDriver.findElements(By.xpath(arrElementTypes[elements]));
			lstSize = allElements.size();
			arrElements = new String[lstSize];
			int i = 0;
			int x = 0;
			//for(int i=0;i<arrElements.length;i++){
			if(lstSize>0){
				objBufferedWriter.newLine();
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write("// Code for radio buttons starts");
				objBufferedWriter.newLine();
			}	
			for (WebElement radioButton : allElements) {
				if (radioButton.getAttribute("name") != null && !radioButton.getAttribute("name").equalsIgnoreCase("")) {
					strAttribute = radioButton.getAttribute("name");
					arrElements[i] = "RadioButton_" + strAttribute + "_Name";
					strDriverMethod = "findElementByName";
				} else if (radioButton.getAttribute("id") != null && !radioButton.getAttribute("id").equalsIgnoreCase("")) {
					strAttribute = radioButton.getAttribute("id");
					arrElements[i] = "RadioButton_" + strAttribute + "_ID";
					strDriverMethod = "findElementById";
				} else {
					strAttribute = getElementXPath(webDriver, radioButton);
					strAttribute = strAttribute.replaceAll("\"", "'");
					arrElements[i] = "RadioButton_Xpath";
					strDriverMethod = "findElementByXPath";
				}
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write("stepExecutor.changeRadioButtonStatus(\"" + strDriverMethod  + "\", \"" + escape(strAttribute) + "\", DataMap,  \"" + arrElements[i] + "\", webDriver,\""+appname+"\");");
				objBufferedWriter.newLine();
			}
			/*if (arrElements.length > 0) {
				scriptGenerator.createDataFile(arrElements, strDataFileName);
			}*/
		}
		objBufferedWriter.close();
	}catch (IOException e){
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}	

public void generateCodeForDropdowns(WebDriver webDriver, String strScriptFile,String appname){
	
	List<WebElement> allElements = null;
	String [] arrElementTypes = new String [] {"select"};
	BufferedWriter objBufferedWriter = null;
	int lstSize = 0;
	// Adding field names to array for textbox
	String [] arrElements = null;
	String strAttribute = null;
	String strTab = "\t";
	String strDriverMethod = null;
	//String strDataFileName = null;
	//strDataFileName = utils.getConfigValues("Data File");
	
	FileWriter fileWriter = null;
	try{
		fileWriter = new FileWriter(strScriptFile, true);
		objBufferedWriter = new BufferedWriter(fileWriter);
		for(int elements=0; elements<arrElementTypes.length;elements++){
			arrElements = null;
			allElements = webDriver.findElements(By.tagName(arrElementTypes[elements]));
			lstSize = allElements.size();
			arrElements = new String[lstSize];
			int i = 0;
			int x = 0;
			
			if (lstSize > 0) {
				objBufferedWriter.newLine();
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write("// Code for dropdown starts");
				objBufferedWriter.newLine();
				objBufferedWriter.newLine();
			}
			
			for (WebElement elList : allElements) {
				if (elList.getAttribute("name") != null && !elList.getAttribute("name").equalsIgnoreCase("")) {
					strAttribute = elList.getAttribute("name");
					arrElements[i] = "Listbox_" + strAttribute + "_Name";
					strDriverMethod = "findElementByName";
				} else if (elList.getAttribute("id") != null && !elList.getAttribute("id").equalsIgnoreCase("")){
					strAttribute = elList.getAttribute("id");
					arrElements[i] = "Listbox_" + strAttribute + "_ID";
					strDriverMethod = "findElementById";
				} else {
					strAttribute = getElementXPath(webDriver, elList);
					strAttribute = strAttribute.replaceAll("\"", "'");
					arrElements[i] = "Listbox_Xpath";
					strDriverMethod = "findElementByXPath";
				}
				objBufferedWriter.newLine();
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write("stepExecutor.selectListValue(\"" + strDriverMethod  + "\", \"" + escape(strAttribute) + "\", DataMap,  \"" + arrElements[i] + "\", webDriver,\""+appname+"\");");
				
				textList.add(strAttribute);
				
			/*	
				objBufferedWriter.write("List<WebElement> options" + x + " = elementList"  + x + ".findElements(By.tagName(\"option\"));");
				objBufferedWriter.newLine();
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				
				objBufferedWriter.write("String strValue" + x + " = scriptExecutor.readDataFile(DataMap, rowNumber, \"" + arrElements[i] + "\");");
				objBufferedWriter.newLine();
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				
				
				objBufferedWriter.write("for(WebElement option : options" + x + "){");
				objBufferedWriter.newLine();
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write("if(option.getText().equals(strValue" + x + ")){");
				
				objBufferedWriter.newLine();
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write("elementList" + x + ".click();");
				
				
				objBufferedWriter.newLine();
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write("option.click();");
				objBufferedWriter.newLine();
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write("break;");
				objBufferedWriter.newLine();
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write("}");
				objBufferedWriter.newLine();
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write("}");

				// Coed for RC starts
				
				objBufferedWriter.newLine();
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				
				objBufferedWriter.write("selenium.select(\"" + strAttribute + "\", strValue" + x + ");");
				
				
				//Code for RC ends
				objBufferedWriter.newLine();
				objBufferedWriter.newLine();*/
				i++;
			}
			
			
		}
		objBufferedWriter.close();
	}catch (IOException e){
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}
	
public void generateCodeForImages(WebDriver webDriver, String strScriptFile,String appname){
	
	List<WebElement> allElements = null;
	String [] arrElementTypes = new String [] {"img","//input[@type='image']"};
	BufferedWriter objBufferedWriter = null;
	int lstSize = 0;
	// Adding field names to array for textbox
	//String [] arrElements = null;
	String strAttribute = null;
	String strTab = "\t";
	String strDriverMethod = null;
	String strAltText = null;
	String strSrc = null;

	FileWriter fileWriter = null;
	try{
		fileWriter = new FileWriter(strScriptFile, true);
		objBufferedWriter = new BufferedWriter(fileWriter);
		for (int elements = 0; elements < arrElementTypes.length; elements++) {
			if(elements>0){
				allElements = webDriver
						.findElements(By.xpath(arrElementTypes[elements]));
			}
			else{
				allElements = webDriver
						.findElements(By.tagName(arrElementTypes[elements]));
			}
			lstSize = allElements.size();
			
			if (lstSize > 0) {
				objBufferedWriter.newLine();
				objBufferedWriter.newLine();
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				if(elements==0){
					objBufferedWriter.write("// Code for images starts");
				}	
			}
			for (WebElement image : allElements) {
				strAltText = image.getAttribute("alt");
				strSrc = image.getAttribute("src");
				if (strAltText != null && !strAltText.equalsIgnoreCase("")) {
					strDriverMethod = "findElementByXPath";
					//objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					if(elements==0){
					/*objBufferedWriter.write("webDriver." + strDriverMethod
							+ "(\"//img[@alt='" + strAltText + "']\")" + ".click();");*/
						objBufferedWriter.write("stepExecutor.clickImage(\"" + strDriverMethod  + "\", \"//img[@alt='" + escape(strAltText) + "']\", webDriver,\""+appname+"\");");
						
					}else{
						/*objBufferedWriter.write("webDriver." + strDriverMethod
								+ "(\"//input[@alt='" + strAltText + "']\")" + ".click();");*/
						objBufferedWriter.write("stepExecutor.clickImage(\"" + strDriverMethod  + "\", \"//input[@alt='" + escape(strAltText) + "']\", webDriver,\""+appname+"\");");
						
					}
					objBufferedWriter.newLine();
				} else if(strSrc != null && !strSrc.equalsIgnoreCase("")){
					strAttribute = image.getAttribute("src");
					strDriverMethod = "findElementByXPath";
					//objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					/*objBufferedWriter.write("webDriver." + strDriverMethod
							+ "(\"//img[@src='" + strSrc + "']\")" + ".click();");*/
					objBufferedWriter.write("stepExecutor.clickImage(\"" + strDriverMethod  + "\", \"//img[@src='" + escape(strSrc) + "']\", webDriver,\""+appname+"\");");
					
					objBufferedWriter.newLine();
				} else if(image.getAttribute("id") != null && !image.getAttribute("id").equalsIgnoreCase("")){
					strAttribute = image.getAttribute("id");
					// arrElements[i]="Textbox_"+ strAttribute + "_ID";
					strDriverMethod = "findElementById";
					//objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					/*objBufferedWriter.write("webDriver." + strDriverMethod
							+ "(\"" + strAttribute + "\")" + ".click();");*/
					objBufferedWriter.write("stepExecutor.clickImage(\"" + strDriverMethod  + "\", \"" + escape(strAttribute) + "\", webDriver,\""+appname+"\");");
					
					objBufferedWriter.newLine();
				}else if(image.getAttribute("name")!=null && !image.getAttribute("name").equalsIgnoreCase("")){
					strAttribute = image.getAttribute("name");
					// arrElements[i]="Textbox_"+strAttribute + "_Name";
					strDriverMethod = "findElementByName";
					//objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					/*objBufferedWriter.write("webDriver." + strDriverMethod
							+ "(\"" + strAttribute + "\")" + ".click();");*/
					objBufferedWriter.write("stepExecutor.clickImage(\"" + strDriverMethod  + "\", \"" + escape(strAttribute) + "\", webDriver,\""+appname+"\");");
					
					objBufferedWriter.newLine();
				}else{
					strAttribute = getElementXPath(webDriver, image);
					strAttribute = strAttribute.replaceAll("\"", "'");
					strDriverMethod = "findElementByXPath";
					//objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					/*objBufferedWriter.write("webDriver." + strDriverMethod
							+ "(\"" + strAttribute + "\")" + ".click();");*/
					objBufferedWriter.write("stepExecutor.clickImage(\"" + strDriverMethod  + "\", \"" + escape(strAttribute) + "\", webDriver,\""+appname+"\");");
					
					objBufferedWriter.newLine();		
				}
			}		
		}
		objBufferedWriter.close();
	}catch (IOException e){
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public void generateCodeForLinks(WebDriver webDriver, String strScriptFile,String appname){
	
	List<WebElement> allElements = null;
	String [] arrElementTypes = new String [] {"a","link"};
	BufferedWriter objBufferedWriter = null;
	int lstSize = 0;
	String strAttribute = null;
	String strTab = "\t";
	String strDriverMethod = null;
	String strLinkText = null;
	String strHrefAttribute = null;
	
	FileWriter fileWriter = null;
	try{
		fileWriter = new FileWriter(strScriptFile, true);
		objBufferedWriter = new BufferedWriter(fileWriter);
		for (int elements = 0; elements < arrElementTypes.length; elements++) {
			allElements = webDriver
					.findElements(By.tagName(arrElementTypes[elements]));
			lstSize = allElements.size();
	
			if (lstSize > 0) {
				//objBufferedWriter.newLine();
				objBufferedWriter.newLine();
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				objBufferedWriter.write(strTab);
				if(elements==0){
					objBufferedWriter.write("// Code for links starts");
				}
			}
			for (WebElement link : allElements) {
				strLinkText = link.getText();
				strHrefAttribute = link.getAttribute("href");
				
				if (strLinkText!=null && !strLinkText.equalsIgnoreCase("") && !strLinkText.contains("\n")) {
					strDriverMethod = "findElementByLinkText";
					objBufferedWriter.newLine();
					//objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					/*objBufferedWriter.write("webDriver." + strDriverMethod
							+ "(\"" + strLinkText + "\")" + ".click();");*/
					objBufferedWriter.write("stepExecutor.clickLink(\"" + strDriverMethod  + "\", \"" + escape(strLinkText) + "\", webDriver,\""+appname+"\");");
					
					
					objBufferedWriter.newLine();		
				} else if (strHrefAttribute!=null && !strHrefAttribute.equalsIgnoreCase("")) {
					strAttribute = getElementXPath(webDriver, link);
					strAttribute = strAttribute.replaceAll("\"", "'");
					strDriverMethod = "findElementByXPath";
					objBufferedWriter.newLine();
					//objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					/*objBufferedWriter.write("webDriver." + strDriverMethod
							+ "(\"//a[contains(@href,'" + strHrefAttribute + "']\")" + ".click();");*/
					
					objBufferedWriter.write("stepExecutor.clickLink(\"" + strDriverMethod  + "\", \"//a[contains(@href,'" + escape(strHrefAttribute) + "')]\", webDriver,\""+appname+"\");");
					
					objBufferedWriter.newLine();
				} else if(link.getAttribute("id") != null && !link.getAttribute("id").equalsIgnoreCase("")){
					strAttribute = link.getAttribute("id");
					strDriverMethod = "findElementById";
					objBufferedWriter.newLine();
					//objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					/*objBufferedWriter.write("webDriver." + strDriverMethod
							+ "(\"" + strAttribute + "\")" + ".click();");*/
					objBufferedWriter.write("stepExecutor.clickLink(\"" + strDriverMethod  + "\", \"" + escape(strAttribute) + "\", webDriver,\""+appname+"\");");
					
					objBufferedWriter.newLine();
				}else if(link.getAttribute("name") != null && !link.getAttribute("name").equalsIgnoreCase("")){
					strAttribute = link.getAttribute("name");
					strDriverMethod = "findElementByName";
					objBufferedWriter.newLine();
					//objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					/*objBufferedWriter.write("webDriver." + strDriverMethod
							+ "(\"" + strAttribute + "\")" + ".click();");*/
					objBufferedWriter.write("stepExecutor.clickLink(\"" + strDriverMethod  + "\", \"" + escape(strAttribute) + "\", webDriver,\""+appname+"\");");
					
					objBufferedWriter.newLine();
				}else{
					strDriverMethod = "findElementByXPath";
					objBufferedWriter.newLine();
					//objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					/*objBufferedWriter.write("//webDriver." + strDriverMethod
							+ "(\"//a[contains(text(),'" + strLinkText + "']\"))" + ".click();");*/
					objBufferedWriter.write("stepExecutor.clickLink(\"" + strDriverMethod  + "\", \"//a[contains(text(),'" + escape(strLinkText) + "')]\", webDriver,\""+appname+"\");");
					
				}
			}
		}
		objBufferedWriter.close();
	}catch (IOException e){
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

String readFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
String escape(String text){
	text = text.replace("\\", "\\\\");
	text = text.replace("\"", "\\\"");
	//System.out.println(text);
	return 	text;
}
}


