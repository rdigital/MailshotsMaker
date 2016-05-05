package com.capgemini.executor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.capgemini.driver.ScriptExecutor;
import com.capgemini.utilities.ReadExcel;
import com.capgemini.utilities.Reporter;
//import com.capgemini.ui.Cafe_UI;
import com.capgemini.utilities.Utilities;

/**
 * Executioner --- Starting point class for the triggering the execution
 * 
 * @author Prasad Joshi
 */

public class Executioner {
	public static boolean testNG=false;
	Utilities utils = new Utilities();
	public static String strExecutionStartTime = null;
	public static String strExecutionStopTime = null;
	public static float totalExecutionTime = 0;
	public static String StrCBTReportFile = null;
	public static int totalScannedPages = 0;
	public static String strExecutionBrowser = null;
	private static String browser;
	
	

	static Map<String, String> DataMap = new HashMap();
	public void setExecutionBrowser(String strExecutionBrowser) {
		this.strExecutionBrowser=strExecutionBrowser;// used to set the browser during execution
	}

	public String getExecutionBrowser() {
		return strExecutionBrowser;
	}

	public static void main(String[] args) {
		RemoteWebDriver driver = null;
		ScriptExecutor executor = new ScriptExecutor();
		Executioner exe = new Executioner();
		String strBrowserType = null;
		float timeElapsed = 0;

		try {
			int i=0;
			Properties properties = new Properties();

			properties.load(new FileInputStream("./data/config.properties"));
			
			if(testNG==false){
			Object[] options = { /*"Create" , */"Execute" ,"Cancel" };   //un comment create
			i=1;
//			i = JOptionPane.showOptionDialog(null,
//					"What action you want to do?",
//					"WELCOME TO CAFE SELENIUM!! ",
//					JOptionPane.YES_NO_CANCEL_OPTION,
//					JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
//			}else{
//				i=1;
//			}
//			if(i==0){
//				Cafe_UI recorderUi = new Cafe_UI();
//
//				recorderUi.createUI();
//				recorderUi.viewUI();
//				
//				
//				
			}
			if (i == 1) {
				
				
				
				
				/*
				 * String strAbsolutepath = new File("").getAbsolutePath();
				 * JFileChooser chooser=new JFileChooser();
				 * chooser.setFileSelectionMode
				 * (JFileChooser.FILES_AND_DIRECTORIES);
				 * chooser.showOpenDialog(null); String
				 * path=chooser.getSelectedFile().getAbsolutePath();
				 */
				/*
				 * String path =
				 * "D:\\Users\\hasane\\Documents\\workspace\\BMCITSM_parallel\\data\\Tubelines_Input_Data.xls"
				 * ;
				 */

				String path = properties.getProperty("file_path");
				//System.out.println(path);
				// System.out.println(path);
				System.setProperty("File", path);

				if (args.length > 2 && !args[1].isEmpty()) {
					strBrowserType = args[1];
					browser=strBrowserType;
					Reporter.browser =browser;
				} else {

					//strBrowserType = "Chrome";//exe.getBrowserType();
				}
				//System.out.println("Browser in executioner"+args[1]);
				//reporter.setBrowser(args[1]);
				/* executioner.setExecutionBrowser(strBrowserType); */
				strExecutionBrowser = strBrowserType;

				// executioner.setExecutionBrowser(strBrowserType);

				if (strBrowserType == null) {
					strBrowserType = "Internet Explorer";
				}
				/* executioner.selectBrowser(strBrowserType); */

				// cbtReport.start(cbtReport.calendar);
				// executor.executeScript();
				if (args.length > 2 && !args[2].isEmpty()) {
	//Parallel
					
					// driver = new CreateDriver().getWebDriver(args[0],
					// args[1]);
					executor.executeScriptMultiple(driver, args[0], args[1],args[2]);

				} else {
	//Single
					executor.executeScriptMultiple();
				}

				// code to kill ChromeDriver & IEDriver
				/*
				 * if (strBrowserType.equalsIgnoreCase("Chrome")){
				 * //Runtime.getRuntime().exec("cmd.exe /c " +
				 * strBatFileLocation);
				 * Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe"
				 * );
				 * 
				 * } if (strBrowserType.equalsIgnoreCase("Internet Explorer")){
				 * Runtime
				 * .getRuntime().exec("taskkill /F /IM IEDriverServer.exe"); }
				 */
				if(testNG==false){
					
					JOptionPane.showMessageDialog(null, "Execution Completed",
						"WELCOME TO SELENIUM CAFE", 1);
					
				
				}
				
			}
			/*if (i == 2) {
				String[] test= new String[20];
				ParallelRun.main(test);
			}*/
			if(i == 3)
			{
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

	public void selectBrowser(String strBrowserType) {
		try {
			String strFileName = utils.getConfigValues("Config File");
			String strApplicationName = null;
			String strURL = null;
			String strDataSource = null;
			List<String> lstComponents = new ArrayList<String>(0);
			lstComponents = utils.getComponentNames();

			strApplicationName = utils.getConfigValues("Application Name");
			strURL = utils.getConfigValues("Application URL");
			strDataSource = utils.getConfigValues("Data Source");

			File file = new File(strFileName);
			FileWriter fwo = null;
			BufferedWriter bwObj = null;
			boolean exists = file.exists();

			if (exists) {
				file.delete();
				FileOutputStream out = new FileOutputStream(strFileName);
				fwo = new FileWriter(strFileName, true);
				bwObj = new BufferedWriter(fwo);
				bwObj.write("Application Name = " + strApplicationName
						+ "\nApplication URL = " + strURL + "\nBrowser Type = "
						+ strBrowserType + "\n");
				if (strDataSource != null) {
					bwObj.write("Data Source = " + strDataSource + "\n");
				}

				for (int xx = 0; xx < lstComponents.size(); xx++) {
					bwObj.write("Component Name = " + lstComponents.get(xx));
					if (xx != lstComponents.size() - 1) {
						bwObj.newLine();
					}
				}
				bwObj.close();
			} else {
				fwo = new FileWriter(strFileName, true);
				bwObj = new BufferedWriter(fwo);
				file.createNewFile();
				bwObj.write("Application Name = " + strApplicationName
						+ "\nApplication URL = " + strURL + "\nBrowser Type = "
						+ strBrowserType + "\n");
				if (strDataSource != null) {
					bwObj.write("Data Source = " + strDataSource + "\n");
				}

				for (int xx = 0; xx < lstComponents.size(); xx++) {
					bwObj.write("Component Name = " + lstComponents.get(xx));
					if (xx != lstComponents.size() - 1) {
						bwObj.newLine();
					}
				}
				bwObj.close();
			}
		} catch (Exception ex) {
		}
	}

	public String getBrowserType() {
		String input = null;
		String[] arrBrowsers = new String[] { "--Select Browser Type--",
				"FireFox", "Internet Explorer",

				"Chrome", "Safari", "iPhone", "Android" };

		try {

			input = (String) JOptionPane.showInputDialog(

			new JFrame(),

			"Please select your Browser Type",

			"CAFE Selenium", JOptionPane.INFORMATION_MESSAGE,

			new ImageIcon("java2sLogo.GIF"), arrBrowsers, "");
		} catch (Exception e) {
			//System.out.println("CANCEL");
		}

		while (input == null || input.equals("--Select Browser Type--")) {
			JOptionPane.showMessageDialog(null, "Browset type is not selected",
					"CAFE Selenium", JOptionPane.ERROR_MESSAGE);
			input = (String) JOptionPane.showInputDialog(

			new JFrame(),

			"Please select your Browser Type",

			"CAFE Selenium", JOptionPane.INFORMATION_MESSAGE,

			new ImageIcon("java2sLogo.GIF"), arrBrowsers, "");
		}
		return input;

	}

}
