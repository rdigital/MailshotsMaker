package com.capgemini.driver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.JavascriptExecutor;

import com.capgemini.utilities.ExcelDataCreation;
import com.capgemini.utilities.Utilities;

/**
 * ScriptWriter --- Class for the generating the steps for script file
 * @author   Prasad Joshi
 */

public class ScriptWriter {
	
	BufferedWriter objBufferedWriter = null;
	Utilities utils = new Utilities();
	ScriptGenerator scriptGenerator = new ScriptGenerator();
	
	
	public String getElementXPath(RemoteWebDriver webDriver, WebElement element) {
	    return (String)((JavascriptExecutor)webDriver).executeScript("gPt=function(c){if(c.id!==''){return'id(\"'+c.id+'\")'}if(c===document.body){return c.tagName}var a=0;var e=c.parentNode.childNodes;for(var b=0;b<e.length;b++){var d=e[b];if(d===c){return gPt(c.parentNode)+'/'+c.tagName+'['+(a+1)+']'}if(d.nodeType===1&&d.tagName===c.tagName){a++}}};return gPt(arguments[0]).toLowerCase();", element);
	    //return (String)((JavascriptExecutor)webDriver).executeScript("gPt=function(c){if(c.id!==''){return'//'+ c.tagName+'[@id=\"'+c.id+'\"]'}if(c===document.body){return c.tagName}var a=0;var e=c.parentNode.childNodes;for(var b=0;b<e.length;b++){var d=e[b];if(d===c){return gPt(c.parentNode)+'/'+c.tagName+'['+(a+1)+']'}if(d.nodeType===1&&d.tagName===c.tagName){a++}}};return gPt(arguments[0]).toLowerCase();", element);
    	//String xPath = webDriver.ge
	}
	
	public void generateCodeForTextboxes(RemoteWebDriver webDriver, String strScriptFile, String strDataFileName){
		
		List<WebElement> allElements = null;
		String [] arrElementTypes = new String [] {"//input[@type='text']", "//input[@type='password']","//input[@type='textarea']","//input[@class='textarea']"};
		BufferedWriter objBufferedWriter = null;
		int lstSize = 0;
		// Adding field names to array for textbox
		String [] arrElements = null;
		String strAttribute = null;
		String strTab = "\t";
		String strDriverMethod = null;
		String strColumnName = null;
		FileWriter fileWriter = null;
		
		try{
			fileWriter = new FileWriter(strScriptFile, true);
			objBufferedWriter = new BufferedWriter(fileWriter);
			for(int elements=0; elements<arrElementTypes.length;elements++){
				arrElements = null;
				allElements = webDriver.findElementsByXPath(arrElementTypes[elements]);
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
						strColumnName = "Textbox_" + strAttribute + "_Name";
						for(int m=0; m<arrElements.length;m++){
							if(strColumnName.equalsIgnoreCase(arrElements[m])){
								strColumnName = "Textbox_" + strAttribute + "_Name_"+m;
							}
						}
						arrElements[i] = "Textbox_" + strAttribute + "_Name";
						strDriverMethod = "findElementByName";
					} else if (textBox.getAttribute("id") != null && !textBox.getAttribute("id").equalsIgnoreCase("")) {
						strAttribute = textBox.getAttribute("id");
						arrElements[i] = "Textbox_" + strAttribute + "_ID";
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
					/*objBufferedWriter.write("webDriver." + strDriverMethod + "(\"" + strAttribute + "\")"
									+ ".sendKeys(scriptExecutor.readDataFile(strDataFileName, rowNumber, \"" + arrElements[i] + "\"));");*/
					
					objBufferedWriter.write("stepExecutor.enterTextValue(\"" + strDriverMethod  + "\", \"" + strAttribute + "\", strDataFileName,  \"" + arrElements[i] + "\", webDriver);");
					
					objBufferedWriter.newLine();
					/*objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("stepExecutor.enterTextValue(\"" + strAttribute + "\", strDataFileName,  \"" + arrElements[i] + "\", selenium);");
					//objBufferedWriter.write("selenium.type(\"" + strAttribute + "\", scriptExecutor.readDataFile(strDataFileName, rowNumber, \"" + arrElements[i] + "\"));");
							
					
					//objBufferedWriter.newLine();
					objBufferedWriter.newLine();*/
					i++;
				}
				
				
				if (arrElements.length > 0) {
					scriptGenerator.createDataFile(arrElements, strDataFileName);
					
				}
				// }
			}
			objBufferedWriter.close();
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
		
	public void generateCodeForButtons(RemoteWebDriver webDriver, String strScriptFile, String strDataFileName){
		
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
					allElements = webDriver.findElementsByTagName(arrElementTypes[elements]);
				}else{
					allElements = webDriver.findElementsByXPath(arrElementTypes[elements]);
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
						objBufferedWriter.write("stepExecutor.clickButton(\"" + strDriverMethod  + "\", \"" + strAttribute + "\", webDriver);");
						
						/*objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("stepExecutor.clickButton(\"" + strAttribute + "\", selenium);");
						//objBufferedWriter.write("selenium.click(\"" + strAttribute + "\");");
						*/
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
							objBufferedWriter.write("stepExecutor.clickButton(\"" + strDriverMethod  + "\", \"//button[@value='" + strAttribute + "']\", webDriver);");
							
							/*objBufferedWriter.newLine();
							objBufferedWriter.write(strTab);
							objBufferedWriter.write(strTab);
							objBufferedWriter.write(strTab);
							objBufferedWriter.write("stepExecutor.clickButton(\"//button[@value='" + strAttribute + "']\", selenium);");*/
							//objBufferedWriter.write("selenium.click( \"//button[@value='" + strAttribute + "']\");");
						}else{
							/*objBufferedWriter.write("webDriver." + strDriverMethod
									+ "(\"//input[@value='" + strAttribute + "']\")" + ".click();");*/
							objBufferedWriter.write("stepExecutor.clickButton(\"" + strDriverMethod  + "\", \"//input[@value='" + strAttribute + "']\", webDriver);");
							
							objBufferedWriter.newLine();
							/*objBufferedWriter.write(strTab);
							objBufferedWriter.write(strTab);
							objBufferedWriter.write(strTab);
							objBufferedWriter.write("stepExecutor.clickButton(\"//input[@value='" + strAttribute + "']\", selenium);");*/
							//objBufferedWriter.write("selenium.click( \"//input[@value='" + strAttribute + "']\");");
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
						objBufferedWriter.write("stepExecutor.clickButton(\"" + strDriverMethod  + "\", \"" + strAttribute + "\", webDriver);");
						
						/*objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("stepExecutor.clickButton(\"" + strAttribute + "\", selenium);");*/
						//objBufferedWriter.write("selenium.click( \"" + strAttribute + "\");");
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
						objBufferedWriter.write("stepExecutor.clickButton(\"" + strDriverMethod  + "\", \"" + strAttribute + "\", webDriver);");
						
						/*objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("stepExecutor.clickButton(\"" + strAttribute + "\", selenium);");*/
						//objBufferedWriter.write("selenium.click( \"" + strAttribute + "\");");
						objBufferedWriter.newLine();
					}
					/*objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("webDriver." + strDriverMethod
							+ "(\"" + strAttribute + "\")" + ".click();");
					objBufferedWriter.newLine();*/
				}
			}
			objBufferedWriter.close();
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void generateCodeForCheckboxes(RemoteWebDriver webDriver, String strScriptFile, String strDataFileName){
		
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
				allElements = webDriver.findElementsByXPath(arrElementTypes[elements]);
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
					objBufferedWriter.write("stepExecutor.changeCheckboxStatus(\"" + strDriverMethod  + "\", \"" + strAttribute + "\", strDataFileName,  \"" + arrElements[i] + "\", webDriver);");
					/*objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("stepExecutor.changeCheckboxStatus(\"" + strAttribute + "\", strDataFileName,  \"" + arrElements[i] + "\", selenium);");*/
					/*x = i+1;
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("String strCheckData" + x + " = scriptExecutor" +".readDataFile" +
											"(strDataFileName, rowNumber, \"" + arrElements[i] + "\");");

					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("if(strCheckData" + x + ".equalsIgnoreCase(\"uncheck\") && " +
											"webDriver."+ strDriverMethod + "(\"" + strAttribute+ "\")" + ".isSelected()){");
					
					
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("webDriver." + strDriverMethod + "(\"" + strAttribute + "\")" + ".click();");
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("}");

					//RC code starts
					
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("if(strCheckData" + x + ".equalsIgnoreCase(\"uncheck\") && " +
							"selenium.isChecked(\"" + strAttribute+ "\")){");
					
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("selenium.click(\"" + strAttribute + "\");");
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("}");
					
					//Rc code ends
					
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("if(strCheckData" + x + ".equalsIgnoreCase(\"check\") && " +
											"!webDriver." + strDriverMethod + "(\"" + strAttribute + "\")"+ ".isSelected()){");
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("webDriver." + strDriverMethod
							+ "(\"" + strAttribute + "\")" + ".click();");
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("}");

					objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					
					//RC code starts
					
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("if(strCheckData" + x + ".equalsIgnoreCase(\"check\") && " +
							"!selenium.isChecked(\"" + strAttribute+ "\")){");
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("selenium.click(\"" + strAttribute + "\");");
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("}");

					objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					
					// RC code ends
					
					i++;*/
				}
				if (arrElements.length > 0) {
					scriptGenerator.createDataFile(arrElements, strDataFileName);
				}
			}
			objBufferedWriter.close();
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	public void generateCodeForRadioButtons(RemoteWebDriver webDriver, String strScriptFile, String strDataFileName){
	
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
				allElements = webDriver.findElementsByXPath(arrElementTypes[elements]);
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
					objBufferedWriter.write("stepExecutor.changeRadioButtonStatus(\"" + strDriverMethod  + "\", \"" + strAttribute + "\", strDataFileName,  \"" + arrElements[i] + "\", webDriver);");
					objBufferedWriter.newLine();
					/*objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("stepExecutor.changeRadioButtonStatus(\"" + strAttribute + "\", strDataFileName,  \"" + arrElements[i] + "\", selenium);");*/
					
					/*x = i+1;
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("String strRadioData" + x + " = scriptExecutor" +".readDataFile" +
											"(strDataFileName, rowNumber, \"" + arrElements[i] + "\");");
	
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("if(strRadioData" + x + ".equalsIgnoreCase(\"unselect\") && " +
											"webDriver."+ strDriverMethod + "(\"" + strAttribute+ "\")" + ".isSelected()){");
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("webDriver." + strDriverMethod + "(\"" + strAttribute + "\")" + ".click();");
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("}");
	
					//Code for RC starts
					
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("if(strRadioData" + x + ".equalsIgnoreCase(\"unselect\") && " +
											"selenium.isChecked(\"" + strAttribute+ "\")){");
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("selenium.click(\"" + strAttribute + "\");");
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("}");
					
					// Code for RC ends
					
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("if(strRadioData" + x + ".equalsIgnoreCase(\"select\") && " +
											"!webDriver." + strDriverMethod + "(\"" + strAttribute + "\")"+ ".isSelected()){");
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("webDriver." + strDriverMethod
							+ "(\"" + strAttribute + "\")" + ".click();");
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("}");
	
					objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					
					// Code for RC starts
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("if(strRadioData" + x + ".equalsIgnoreCase(\"select\") && " +
											"!selenium.isChecked(\"" + strAttribute+ "\")){");
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("selenium.click(\"" + strAttribute + "\");");
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("}");
					
					//Code for RC Ends
					i++;*/
				}
				if (arrElements.length > 0) {
					scriptGenerator.createDataFile(arrElements, strDataFileName);
				}
			}
			objBufferedWriter.close();
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}	

	public void generateCodeForDropdowns(RemoteWebDriver webDriver, String strScriptFile, String strDataFileName){
	
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
				allElements = webDriver.findElementsByTagName(arrElementTypes[elements]);
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
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("stepExecutor.selectListValue(\"" + strDriverMethod  + "\", \"" + strAttribute + "\", strDataFileName,  \"" + arrElements[i] + "\", webDriver);");
					/*objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("stepExecutor.selectListValue(\"" + strAttribute + "\", strDataFileName,  \"" + arrElements[i] + "\", selenium);");*/
					/*x = i + 1;
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					
					objBufferedWriter.write("WebElement elementList" + x + "= webDriver." + strDriverMethod + "(\""+ strAttribute + "\");");
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					
					objBufferedWriter.write("elementList" + x + ".click();");
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					
					
					objBufferedWriter.write("List<WebElement> options" + x + " = elementList"  + x + ".findElements(By.tagName(\"option\"));");
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					
					objBufferedWriter.write("String strValue" + x + " = scriptExecutor.readDataFile(strDataFileName, rowNumber, \"" + arrElements[i] + "\");");
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
					objBufferedWriter.newLine();
					i++;*/
				}
				
				if (arrElements.length > 0) {
					scriptGenerator.createDataFile(arrElements, strDataFileName);
				}
			}
			objBufferedWriter.close();
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}
		
	public void generateCodeForImages(RemoteWebDriver webDriver, String strScriptFile, String strDataFileName){
		
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
							.findElementsByXPath(arrElementTypes[elements]);
				}
				else{
					allElements = webDriver
							.findElementsByTagName(arrElementTypes[elements]);
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
							objBufferedWriter.write("stepExecutor.clickImage(\"" + strDriverMethod  + "\", \"//img[@alt='" + strAltText + "']\", webDriver);");
							/*objBufferedWriter.newLine();
							objBufferedWriter.write(strTab);
							objBufferedWriter.write(strTab);
							objBufferedWriter.write(strTab);
							objBufferedWriter.write("stepExecutor.clickImage(\"//img[@alt='" + strAltText + "']\", selenium);");*/
							//objBufferedWriter.write("selenium.click(\"//img[@alt='" + strAltText + "']\");");
						}else{
							/*objBufferedWriter.write("webDriver." + strDriverMethod
									+ "(\"//input[@alt='" + strAltText + "']\")" + ".click();");*/
							objBufferedWriter.write("stepExecutor.clickImage(\"" + strDriverMethod  + "\", \"//input[@alt='" + strAltText + "']\", webDriver);");
							/*objBufferedWriter.newLine();
							objBufferedWriter.write(strTab);
							objBufferedWriter.write(strTab);
							objBufferedWriter.write(strTab);
							objBufferedWriter.write("stepExecutor.clickImage(\"//input[@alt='" + strAltText + "']\", selenium);");*/
							//objBufferedWriter.write("selenium.click(\"//input[@alt='" + strAltText + "']\");");
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
						objBufferedWriter.write("stepExecutor.clickImage(\"" + strDriverMethod  + "\", \"//img[@src='" + strSrc + "']\", webDriver);");
						/*objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("stepExecutor.clickImage(\"//img[@src='" + strSrc + "']\", selenium);");*/
						//objBufferedWriter.write("selenium.click(\"//img[@src='" + strSrc + "']\");");
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
						objBufferedWriter.write("stepExecutor.clickImage(\"" + strDriverMethod  + "\", \"" + strAttribute + "\", webDriver);");
						/*objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("stepExecutor.clickImage(\"" + strAttribute + "\", selenium);");*/
						//objBufferedWriter.write("selenium.click(\"" + strAttribute + "\");");
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
						objBufferedWriter.write("stepExecutor.clickImage(\"" + strDriverMethod  + "\", \"" + strAttribute + "\", webDriver);");
						/*objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("stepExecutor.clickImage(\"" + strAttribute + "\", selenium);");*/
						//objBufferedWriter.write("selenium.click(\"" + strAttribute + "\");");
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
						objBufferedWriter.write("stepExecutor.clickImage(\"" + strDriverMethod  + "\", \"" + strAttribute + "\", webDriver);");
						/*objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("stepExecutor.clickImage(\"" + strAttribute + "\", selenium);");*/
						//objBufferedWriter.write("selenium.click(\"" + strAttribute + "\");");
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

	public void generateCodeForLinks(RemoteWebDriver webDriver, String strScriptFile, String strDataFileName){
		
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
						.findElementsByTagName(arrElementTypes[elements]);
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
					
					if (strLinkText!=null && !strLinkText.equalsIgnoreCase("")) {
						strDriverMethod = "findElementByLinkText";
						objBufferedWriter.newLine();
						//objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						/*objBufferedWriter.write("webDriver." + strDriverMethod
								+ "(\"" + strLinkText + "\")" + ".click();");*/
						objBufferedWriter.write("stepExecutor.clickLink(\"" + strDriverMethod  + "\", \"" + strLinkText + "\", webDriver);");
						/*objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("stepExecutor.clickLink(\"" + strLinkText + "\", selenium);");*/
						//objBufferedWriter.write("selenium.click(\"link=" + strLinkText + "\");");
						
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
						
						objBufferedWriter.write("stepExecutor.clickLink(\"" + strDriverMethod  + "\", \"//a[contains(@href,'" + strHrefAttribute + "'])\", webDriver);");
						/*objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("stepExecutor.clickLink(\"//a[contains(@href,'" + strHrefAttribute + "'])\", selenium);");*/
						//objBufferedWriter.write("selenium.click(\"//a[contains(@href,'" + strHrefAttribute + "']\");");
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
						objBufferedWriter.write("stepExecutor.clickLink(\"" + strDriverMethod  + "\", \"" + strAttribute + "\", webDriver);");
						/*objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("stepExecutor.clickLink(\"" + strAttribute + "\", selenium);");*/
						//objBufferedWriter.write("selenium.click(\"" + strAttribute + "\");");
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
						objBufferedWriter.write("stepExecutor.clickLink(\"" + strDriverMethod  + "\", \"" + strAttribute + "\", webDriver);");
						/*objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("stepExecutor.clickLink(\"" + strAttribute + "\", selenium);");*/
						//objBufferedWriter.write("selenium.click(\"" + strAttribute + "\");");
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
						objBufferedWriter.write("stepExecutor.clickLink(\"" + strDriverMethod  + "\", \"//a[contains(text(),'" + strLinkText + "']\"), webDriver);");
						/*objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("stepExecutor.clickLink(\"//a[contains(text(),'" + strLinkText + "']\"), selenium);");*/
						//objBufferedWriter.write("selenium.click(\"//a[contains(text(),'" + strLinkText + "']\");");
					}
				}
			}
			objBufferedWriter.close();
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
