package com.capgemini.driver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.capgemini.utilities.ExcelDataCreation;
import com.capgemini.utilities.Utilities;

/**
 * ScriptGenerator --- Class for the generating the scripts and data files
 * @author   Prasad Joshi
 */

public class ScriptGenerator{
	
	private String strAbsolutepath = new File("").getAbsolutePath();
	CreateDriver driver = new CreateDriver();
	Utilities utils = new Utilities();
	FileWriter fileWriter = null;
	//ElementWriter elementWriter = new ElementWriter();
	ExcelDataCreation excelDataCreation = new ExcelDataCreation();
	
	public void createExecutionConfigFile(){
		String strExecutionConfigFile = null;
		
		strExecutionConfigFile = utils.getConfigValues("Execution Configuration File");
		//strExecutionConfigFile = strAbsolutepath + "/data/" + utils.getConfigValues("Application Name") + "/" + utils.getConfigValues("Application Name") + ".config";
		String strDelimiter = "##";

		File file = new File(strExecutionConfigFile);
		if(file.exists()){
			file.delete();
		}
		
		
		try{
			FileWriter fwo = new FileWriter(strExecutionConfigFile, true);
			
			BufferedWriter bwObj = new BufferedWriter(fwo);
			
			bwObj.write("Scenario");
			bwObj.write(strDelimiter);
			bwObj.write("Row_Number");
			bwObj.write(strDelimiter);
			bwObj.write("Execute");
			bwObj.newLine();
			
			// Modified for GP
			bwObj.write("launchApplication");
			bwObj.write(strDelimiter);
			bwObj.write("1");
			bwObj.write(strDelimiter);
			bwObj.write("No");
			bwObj.newLine();
			
			List<String> lstComponents = new ArrayList<String>(0);
			lstComponents = utils.getComponentNames();
			
			for(int i=0;i<lstComponents.size();i++){
				bwObj.write(lstComponents.get(i));
				bwObj.write(strDelimiter);
				bwObj.write("1");
				bwObj.write(strDelimiter);
				bwObj.write("Yes");
				
				if(i<lstComponents.size()){
					bwObj.newLine();
				}
			}
		
			bwObj.close( );
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void genearteScriptFile(){
		
		String strScriptFile = null;
		String strApplicationName = null;
		//Get total functions/components
		List<String> lstComponents = new ArrayList<String>(0);
		lstComponents = utils.getComponentNames();
		
		// Creating config file for execution
		createExecutionConfigFile();
		
		BufferedWriter objBufferedWriter = null;
		// Write start up in the file
		writeFileStart();
		
		List<String> lstDataFileNames = new ArrayList<String>(0);
		
		try{
			RemoteWebDriver webDriver = null;
			webDriver =driver.getWebDriver();
			webDriver.get(utils.getConfigValues("Application URL"));
			String strTab = "\t";
			String strComponentName = null;
			strScriptFile = utils.getConfigValues("Script File");
			strApplicationName = utils.getConfigValues("Application Name");
			
			
			try{
				for(int component = 0; component<lstComponents.size(); component++){
					
					
					
					int reply = JOptionPane.showConfirmDialog(null, "Do you want to generate code for this page as " + lstComponents.get(component) + "?", "", JOptionPane.YES_NO_OPTION);
					String strDataFileName = null;
					
					
					
					if (reply == JOptionPane.YES_OPTION) {
						
						for (String handle : webDriver.getWindowHandles()) {
							webDriver.switchTo().window(handle);
						}	
						
						fileWriter = new FileWriter(strScriptFile,true);
						objBufferedWriter = new BufferedWriter(fileWriter);
						strComponentName = lstComponents.get(component).trim();
						//strDataFileName = utils.getConfigValues("Data File Path") + strApplicationName + "_" + strComponentName + "_Data.txt";
						File dataFolder = new File(strAbsolutepath + "/data/" + strApplicationName);
						if(!dataFolder.exists()){
							dataFolder.mkdir();
						}
						strDataFileName = utils.getConfigValues("Data File Path") + "/" +  strApplicationName + "/" + strApplicationName + "_" + strComponentName + "_Data.txt";
						lstDataFileNames.add(strDataFileName);		
						
						objBufferedWriter.newLine();
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("public void "+ strComponentName + "(){");
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("try{");
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						//objBufferedWriter.write("int rowNumber = utils.getRow(\"" + strComponentName + "\");");
						objBufferedWriter.write("int rowNumber = executionRowNumber.getExecutionRowNumber();");
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("String strDataFileName = utils.getDataFile(\"" + strComponentName + "\");");
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("Reporter reporter = new Reporter();");
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("reporter.setStrBrowser(webDriver.getCapabilities().getBrowserName());reporter.start(reporter.calendar);");
						
						objBufferedWriter.newLine();
						objBufferedWriter.newLine();
						objBufferedWriter.close();
						// Call methods to write scripts
						
						ScriptWriter scriptWriter = new ScriptWriter();
						scriptWriter.generateCodeForTextboxes(webDriver, strScriptFile, strDataFileName);
						scriptWriter.generateCodeForButtons(webDriver, strScriptFile,strDataFileName);
						scriptWriter.generateCodeForCheckboxes(webDriver, strScriptFile, strDataFileName);
						scriptWriter.generateCodeForDropdowns(webDriver, strScriptFile, strDataFileName);
						scriptWriter.generateCodeForRadioButtons(webDriver, strScriptFile, strDataFileName);
						scriptWriter.generateCodeForLinks(webDriver, strScriptFile, strDataFileName);
						scriptWriter.generateCodeForImages(webDriver, strScriptFile, strDataFileName);
						
						/*ElementWriter elementWriter = new ElementWriter();
						elementWriter.generateCodeForTextboxes(webDriver, strScriptFile, strDataFileName);
						elementWriter.generateCodeForCheckboxes(webDriver, strScriptFile, strDataFileName);
						elementWriter.generateCodeForDropdowns(webDriver, strScriptFile, strDataFileName);
						elementWriter.generateCodeForRadioButtons(webDriver, strScriptFile, strDataFileName);
						elementWriter.generateCodeForButtons(webDriver, strScriptFile,strDataFileName);
						elementWriter.generateCodeForLinks(webDriver, strScriptFile, strDataFileName);
						elementWriter.generateCodeForImages(webDriver, strScriptFile, strDataFileName);*/
						
						// Call to generate the code for the elements in frame or iFrame
						generateCodeForFrames(webDriver, strScriptFile, strDataFileName);
						
						String strDataSource = utils.getConfigValues("Data Source");
						
						if(strDataSource.equalsIgnoreCase("Excel") || strDataSource.equalsIgnoreCase("xls")){
							File dataFile = new File(strDataFileName);
							if(dataFile.exists()){
								String arrFile [] = new String[10];
								arrFile = strDataFileName.split("_");
								String strData = null;
								int expectedTokenNumber = 0;
								String strDelimiter = "##";
								int counter = 0;
						
								BufferedReader br = new BufferedReader(new FileReader(strDataFileName));
								String line = null;
								String[] values = null;
								while ((line = br.readLine()) != null) {
									values = line.split(strDelimiter);
								}
						      
								excelDataCreation.writeExcelData(values, arrFile[1]);
						      }
						}
						
						fileWriter = new FileWriter(strScriptFile,true);
						objBufferedWriter = new BufferedWriter(fileWriter);
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("String strStopTime = reporter.stop();");
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("Reporter.strStopTime = strStopTime;");
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("float timeElapsed = reporter.getElapsedTime();");
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("Reporter.timeElapsed = timeElapsed;");
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("reporter.stepReportGenerator();");
						objBufferedWriter.newLine();
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("}catch(Exception e1){");
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("System.out.println(e1.getMessage());");
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("}");
						
						if(component==lstComponents.size()-1){
							objBufferedWriter.write("finally{");
							objBufferedWriter.newLine();
							objBufferedWriter.write(strTab);
							objBufferedWriter.write(strTab);
							objBufferedWriter.write(strTab);
							objBufferedWriter.write("webDriver.quit();");
							//objBufferedWriter.write("selenium.stop();");
							objBufferedWriter.newLine();
							objBufferedWriter.write(strTab);
							objBufferedWriter.write(strTab);
							objBufferedWriter.write("}");
							objBufferedWriter.newLine();
							objBufferedWriter.newLine();
							objBufferedWriter.write(strTab);
							objBufferedWriter.write("}");
							objBufferedWriter.close();
							webDriver.quit();
						}else{
						
							objBufferedWriter.newLine();
							objBufferedWriter.write(strTab);
							objBufferedWriter.write("}");
							
							objBufferedWriter.newLine();
							objBufferedWriter.close();
						}
					}
					if(reply == JOptionPane.NO_OPTION){
						
						fileWriter = new FileWriter(strScriptFile,true);
						objBufferedWriter = new BufferedWriter(fileWriter);
						strComponentName = lstComponents.get(component).trim();
						strDataFileName = utils.getConfigValues("Data File Path") + strApplicationName + "_" + strComponentName + "_Data.txt";
												
						objBufferedWriter.newLine();
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("public void "+ strComponentName + "(){");
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("try{");
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						//objBufferedWriter.write("int rowNumber = utils.getRow(\"" + strComponentName + "\");");
						objBufferedWriter.write("int rowNumber = executionRowNumber.getExecutionRowNumber();");
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("String strDataFileName = utils.getDataFile(\"" + strComponentName + "\");");
						
						objBufferedWriter.newLine();
						objBufferedWriter.newLine();
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("}catch(Exception e1){");
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("System.out.println(e1.getMessage());");
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("}");
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("}");
						objBufferedWriter.close();
					}
					
				}
				
				fileWriter = new FileWriter(strScriptFile,true);
				objBufferedWriter = new BufferedWriter(fileWriter);
				objBufferedWriter.newLine();
				objBufferedWriter.write("}");
				objBufferedWriter.close();
				

				
				
			}catch(IOException e1){
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}catch(Exception e1){
			System.out.println(e1.getMessage());
		}
		
	}
	
	public void generateCodeForFrames(RemoteWebDriver webDriver, String strScriptFile, String strDataFileName){
		
		String strFrameIdentifier = null;
		BufferedWriter objBufferedWriter = null;
		String strTab = "\t";
		List<WebElement> allFrameElements = null;
		//ElementWriter elementWriter = new ElementWriter();
		ScriptWriter scriptWriter = new ScriptWriter();
		int frameIndex = 0;
		int iFrameIndex = 0;
		try{
			if(webDriver.findElementsByTagName("frame").size()!=0){
				
				allFrameElements = webDriver.findElementsByTagName("frame");
				for (WebElement frameElement : allFrameElements) {
					strFrameIdentifier = frameElement.getAttribute("name");
					if(strFrameIdentifier!= null){
						if(!strFrameIdentifier.equalsIgnoreCase("")){
							webDriver.switchTo().frame(strFrameIdentifier);
						}	
					}else{
						strFrameIdentifier = frameElement.getAttribute("id");
						if(strFrameIdentifier!= null){
							if(!strFrameIdentifier.equalsIgnoreCase("")){
								webDriver.switchTo().frame(strFrameIdentifier);
							}
						}	
					}
					
					if(strFrameIdentifier==null || strFrameIdentifier.equalsIgnoreCase("")){
						webDriver.switchTo().frame(frameIndex);
					}
					
					fileWriter = new FileWriter(strScriptFile,true);
					objBufferedWriter = new BufferedWriter(fileWriter);
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					
					if(strFrameIdentifier!=null && strFrameIdentifier.equalsIgnoreCase("")){
						objBufferedWriter.write("webDriver.switchTo().frame(\"" + strFrameIdentifier + "\");");
					}else{
						objBufferedWriter.write("webDriver.switchTo().frame(" + frameIndex + ");");
					}
					
					objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					objBufferedWriter.close();
					
					
					scriptWriter.generateCodeForTextboxes(webDriver, strScriptFile, strDataFileName);
					scriptWriter.generateCodeForButtons(webDriver, strScriptFile,strDataFileName);
					scriptWriter.generateCodeForCheckboxes(webDriver, strScriptFile, strDataFileName);
					scriptWriter.generateCodeForDropdowns(webDriver, strScriptFile, strDataFileName);
					scriptWriter.generateCodeForRadioButtons(webDriver, strScriptFile, strDataFileName);
					scriptWriter.generateCodeForLinks(webDriver, strScriptFile, strDataFileName);
					scriptWriter.generateCodeForImages(webDriver, strScriptFile, strDataFileName);
					
					/*elementWriter.generateCodeForTextboxes(webDriver, strScriptFile, strDataFileName);
					elementWriter.generateCodeForCheckboxes(webDriver, strScriptFile, strDataFileName);
					elementWriter.generateCodeForDropdowns(webDriver, strScriptFile, strDataFileName);
					elementWriter.generateCodeForButtons(webDriver, strScriptFile,strDataFileName);
					elementWriter.generateCodeForLinks(webDriver, strScriptFile, strDataFileName);
					elementWriter.generateCodeForImages(webDriver, strScriptFile, strDataFileName);*/
					
					webDriver.switchTo().defaultContent();
					
					fileWriter = new FileWriter(strScriptFile,true);
					objBufferedWriter = new BufferedWriter(fileWriter);
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("webDriver.switchTo().defaultContent();");
					
					objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					objBufferedWriter.close();
					
					frameIndex++;
				}
			}
			
			if(webDriver.findElementsByTagName("iframe").size()!=0){
				
				allFrameElements = webDriver.findElementsByTagName("iframe");
				for (WebElement frameElement : allFrameElements) {
					strFrameIdentifier = frameElement.getAttribute("name");
					if(strFrameIdentifier!= null){
						if(!strFrameIdentifier.equalsIgnoreCase("")){
							webDriver.switchTo().frame(strFrameIdentifier);
						}	
					}else{
						strFrameIdentifier = frameElement.getAttribute("id");
						if(strFrameIdentifier!= null){
							if(!strFrameIdentifier.equalsIgnoreCase("")){
								webDriver.switchTo().frame(strFrameIdentifier);
							}
						}	
					}	
					
					if(strFrameIdentifier==null || strFrameIdentifier.equalsIgnoreCase("")){
						webDriver.switchTo().frame(iFrameIndex);
					}
					fileWriter = new FileWriter(strScriptFile,true);
					objBufferedWriter = new BufferedWriter(fileWriter);
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					
					if(strFrameIdentifier!=null && strFrameIdentifier.equalsIgnoreCase("")){
						objBufferedWriter.write("webDriver.switchTo().frame(\"" + strFrameIdentifier + "\");");
					}else{
						objBufferedWriter.write("webDriver.switchTo().frame(" + iFrameIndex + ");");
					}
					objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					objBufferedWriter.close();
					
					scriptWriter.generateCodeForTextboxes(webDriver, strScriptFile, strDataFileName);
					scriptWriter.generateCodeForButtons(webDriver, strScriptFile,strDataFileName);
					scriptWriter.generateCodeForCheckboxes(webDriver, strScriptFile, strDataFileName);
					scriptWriter.generateCodeForDropdowns(webDriver, strScriptFile, strDataFileName);
					scriptWriter.generateCodeForRadioButtons(webDriver, strScriptFile, strDataFileName);
					scriptWriter.generateCodeForLinks(webDriver, strScriptFile, strDataFileName);
					scriptWriter.generateCodeForImages(webDriver, strScriptFile, strDataFileName);
					/*elementWriter.generateCodeForTextboxes(webDriver, strScriptFile, strDataFileName);
					elementWriter.generateCodeForCheckboxes(webDriver, strScriptFile, strDataFileName);
					elementWriter.generateCodeForDropdowns(webDriver, strScriptFile, strDataFileName);
					elementWriter.generateCodeForButtons(webDriver, strScriptFile,strDataFileName);
					elementWriter.generateCodeForLinks(webDriver, strScriptFile, strDataFileName);
					elementWriter.generateCodeForImages(webDriver, strScriptFile, strDataFileName);
					webDriver.switchTo().defaultContent();*/
					
					fileWriter = new FileWriter(strScriptFile,true);
					objBufferedWriter = new BufferedWriter(fileWriter);
					objBufferedWriter.newLine();
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write(strTab);
					objBufferedWriter.write("webDriver.switchTo().defaultContent();");
					
					objBufferedWriter.newLine();
					objBufferedWriter.newLine();
					objBufferedWriter.close();
					
					iFrameIndex++;
				}
				
			}
		}catch(Exception exce1){
			System.out.println(exce1.getMessage());
		}	


	}
	
	public void writeFileStart(){
		String strTab = "\t";
		String strScriptFile = null; ;
		String strClasssName = null;
				
		strScriptFile = utils.getConfigValues("Script File");
		strClasssName = utils.getConfigValues("Application Name");
		
		File file = new File(strScriptFile);
		if(file.exists()){
			file.delete();
		}
		
		try{
			fileWriter = new FileWriter(strScriptFile,true);
			BufferedWriter objBufferedWriter = new BufferedWriter(fileWriter);
			
			objBufferedWriter.write("package com.capgemini.scripts;");
			objBufferedWriter.newLine();
			objBufferedWriter.newLine();
			objBufferedWriter.write("import com.capgemini.driver.*;");
			objBufferedWriter.newLine();
			objBufferedWriter.write("import com.capgemini.utilities.*;");
			objBufferedWriter.newLine();
			objBufferedWriter.write("import com.capgemini.executor.*;");
			objBufferedWriter.newLine();
			objBufferedWriter.write("import java.util.List;");
			
			objBufferedWriter.newLine();
			objBufferedWriter.write("import org.openqa.selenium.remote.RemoteWebDriver;");
			objBufferedWriter.newLine();
			objBufferedWriter.write("import org.openqa.selenium.By;");
			objBufferedWriter.newLine();
			objBufferedWriter.write("import org.openqa.selenium.WebElement;");
			/*objBufferedWriter.newLine();
			objBufferedWriter.write("import com.thoughtworks.selenium.Selenium;");*/
			objBufferedWriter.newLine();
			objBufferedWriter.newLine();
		
			objBufferedWriter.write("public class " + strClasssName + "{");
			objBufferedWriter.newLine();
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
		
			objBufferedWriter.write("CreateDriver driver = new CreateDriver();");
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("RemoteWebDriver webDriver = driver.getWebDriver();");
			/*objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			
			objBufferedWriter.write("private CreateRCObject objSeleniumRC = new CreateRCObject();");
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("private Selenium selenium = null;");*/
			
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("private Utilities utils = new Utilities();");
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("private ScriptExecutor scriptExecutor = new ScriptExecutor();");
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("private ExecutionRowNumber executionRowNumber = new ExecutionRowNumber();");
			/*objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("// Object for calling reporting functions");
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("CreateResult resultCreator = new CreateResult();");*/
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("// Object for calling verification functions");
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("private VerificationFunctions verificationFunctions = new VerificationFunctions();");
			/*objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("DefaultSelenium selenium = null;");*/
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("private StepExecutor stepExecutor = new StepExecutor();");
			
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("private String StrExecutionStartTime = null;");
			
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("private long executionStartTime = 0;");
			
			objBufferedWriter.newLine();
			objBufferedWriter.newLine();
			
			
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("public String getExecutionStartTime(){");
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write(strTab);
			
			objBufferedWriter.write("return StrExecutionStartTime;");
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("}");
			
			objBufferedWriter.newLine();
			objBufferedWriter.newLine();
			
			
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("public String getStartTime(){");
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write(strTab);
			
			objBufferedWriter.write("return String.valueOf(executionStartTime);");
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("}");
			
			
			objBufferedWriter.newLine();
			objBufferedWriter.newLine();
			
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("public void launchApplication(){");
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write(strTab);
			
			// Modified for GP
						objBufferedWriter.write("int rowNumber = executionRowNumber.getExecutionRowNumber();");
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("String strDataFileName = utils.getDataFile(\"launchApplication\");");
						/*objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("webDriver.get(scriptExecutor.readDataFile(strDataFileName, rowNumber, \"URL\"));");*/
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("Reporter reporter = new Reporter();");
						objBufferedWriter.newLine();
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("reporter.setStrBrowser(webDriver.getCapabilities().getBrowserName());reporter.start(reporter.calendar);");
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("StrExecutionStartTime = reporter.strStartTime;");
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("executionStartTime = reporter.startTime;");
						
						/*objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("selenium = objSeleniumRC.getRCObject(scriptExecutor.readDataFile(strDataFileName, rowNumber, \"URL\"));");
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("stepExecutor.launchApplication(\"URL\", strDataFileName, \"/\", selenium);");
						objBufferedWriter.newLine();*/
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("stepExecutor.launchApplication(\"URL\", strDataFileName, webDriver);");
						objBufferedWriter.newLine();
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("String strStopTime = reporter.stop();");
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("float timeElapsed = reporter.getElapsedTime();");
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("Reporter.timeElapsed = timeElapsed;");
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("Reporter.strStopTime = strStopTime;");
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("Reporter.strStopTime = strStopTime;");
						
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("reporter.stepReportGenerator();");
						
						/*objBufferedWriter.write("selenium.start();");
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("selenium.open(\"/\");");*/
						objBufferedWriter.newLine();
						objBufferedWriter.write(strTab);
						objBufferedWriter.write("}");
			// Modification ends
			
						objBufferedWriter.newLine();
						objBufferedWriter.newLine();
						
						
						
		/*
			objBufferedWriter.write("webDriver.get(utils.getConfigValues(\"Application URL\"));");
			objBufferedWriter.newLine();
			objBufferedWriter.write(strTab);
			objBufferedWriter.write("}");
			objBufferedWriter.newLine();
			objBufferedWriter.newLine();*/
			
			
			objBufferedWriter.close();
		}catch(IOException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
	}
	
	public void createDataFile(String[] arr1, String strFileName){
		String strDelimiter = "##";
		try {
			FileWriter fwo = new FileWriter(strFileName, true);

			BufferedWriter bwObj = new BufferedWriter(fwo);

			for (int i = 0; i < arr1.length; i++) {
				if (arr1[i] != null) {
					bwObj.write(arr1[i]);
				}
				bwObj.write(strDelimiter);
			}

			bwObj.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
}

