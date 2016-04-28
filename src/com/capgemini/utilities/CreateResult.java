package com.capgemini.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.TakesScreenshot;

/**
 * CreateResult --- Class for generating result file
 * 
 * @author Prasad Joshi
 */

public class CreateResult {

	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd-hh.mm.ss";
	Utilities utils = new Utilities();

	List<String> tempList = new ArrayList<String>(0);
	List<String> tempList_data = new ArrayList<String>(0);
	List<String> tempList_execute = new ArrayList<String>(0);

	static List<String> tempList_scenario_name1 = new ArrayList<String>(0);
	static List<String> tempList_teststep_description = new ArrayList<String>(0);
	static List<String> tempList_test_data = new ArrayList<String>(0);
	static List<String> tempList_snapshot = new ArrayList<String>(0);
	static List<String> tempList_status = new ArrayList<String>(0);

	// Selenium selenium;
	static List<String> tempList_scenario_name = new ArrayList<String>(0);
	static List<String> tempList_exp_result = new ArrayList<String>(0);
	static List<String> tempList_testCase_name = new ArrayList<String>(0);
	static List<String> tempList_description = new ArrayList<String>(0);
	static List<String> tempList_location = new ArrayList<String>(0);
	static List<String> tempList_result = new ArrayList<String>(0);
	static List<String> tempList_act_result = new ArrayList<String>(0);
	static String strAbsolutePath = new File("").getAbsolutePath();

	public static int month;
	public static int day;
	public static int year;

	public static String strStartTime;
	public static String strStopTime;

	public static float timeElapsed;
	public static long startTime;
	public static long stopTime;

	public static String strScreenshot;

	private static int hour;
	private static int min;
	private static int sec;
	private static String am_pm;
	private static boolean running = false;
	public Calendar calendar = new GregorianCalendar();

	public void start(Calendar calander) {

		Long actualStartTime = System.currentTimeMillis();
		hour = calander.get(Calendar.HOUR);
		min = calander.get(Calendar.MINUTE);
		sec = calander.get(Calendar.SECOND);
		if (calander.get(Calendar.AM_PM) == 0)
			am_pm = "AM";
		else
			am_pm = "PM";

		// .currentTimeMillis();
		running = true;
		startTime = actualStartTime;
		strStartTime = "" + hour + ":" + min + ":" + sec + " " + am_pm;
	}

	public String stop() {
		String strStoTime = null;
		Calendar stop = new GregorianCalendar();
		Long actualstopTime = System.currentTimeMillis();
		hour = stop.get(Calendar.HOUR);
		min = stop.get(Calendar.MINUTE);
		sec = stop.get(Calendar.SECOND);
		if (stop.get(Calendar.AM_PM) == 0)
			am_pm = "AM";
		else
			am_pm = "PM";
		// .currentTimeMillis();
		stopTime = actualstopTime;
		strStoTime = "" + hour + ":" + min + ":" + sec + " " + am_pm;

		running = false;
		return strStoTime;
	}

	// elapsed time in milliseconds
	public float getElapsedTime() {
		float elapsedTime = 0;
		if (running) {
			elapsedTime = (System.currentTimeMillis() - startTime);
			// .currentTimeMillis() - startTime);
		} else {
			elapsedTime = (stopTime - startTime);
		}
		return elapsedTime;
	}

	public static String now() {

		// String strScreenshot = null;
		// String strConfigFileName =
		// "/Users/testlab-imac/Documents/workspace/iPhoneTest/src/com/capgemini/results/screenshot/";
		String strScreenshotPath = strAbsolutePath + "/results/screenshot/";

		Calendar cal = Calendar.getInstance();
		month = cal.get(Calendar.MONTH) + 1;
		day = cal.get(Calendar.DAY_OF_MONTH);
		year = cal.get(Calendar.YEAR);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		Random rand = new Random();
		int num = rand.nextInt(1000);
		strScreenshot = (String) (strScreenshotPath + sdf.format(cal.getTime())
				+ num + ".png");
		return sdf.format(cal.getTime());
	}

	// String sce_name,String test_case_name,String exp_result,String
	// Act_result, String Description, boolean Screenshot, String result,
	// AndroidDriver driver
	public void takeScreenshot(String strScenarioName, String strTestCaseName,
			String strExpectedResult, String strActualResult,
			String strResultDescription, boolean isScreenshot, String result,
			RemoteWebDriver webDriver) {
		// public void takeScreenshot(String strScenarioName,int testStep,
		// String strResultDescription, boolean Screenshot, String result,
		// RemoteWebDriver iPhoneDriver) {
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			tempList_scenario_name.add(strScenarioName);
			tempList_testCase_name.add(strTestCaseName);
			tempList_exp_result.add(strExpectedResult);
			tempList_act_result.add(strActualResult);
			tempList_description.add(strResultDescription);

			// List<String> tempList_location = new ArrayList<String>(0);
			// List<String> tempList_result = new ArrayList<String>(0);

			// String screenshot = null;

			// FileUtils util = new FileUtils();
			// String strConfigFileName =
			// "/Users/testlab-imac/Documents/workspace/iPhoneTest/src/com/capgemini/results/screenshot/";
			if (isScreenshot) {
				now();
				Random rand = new Random();
				int num = rand.nextInt(1000);

				try {

					// Copy the file to screenshot folder
					// File tempFile = ((TakesScreenshot)
					// driver).getScreenshotAs(OutputType.FILE);
					// WebDriver augmentedDriver = new
					// Augmenter().augment(webDriver);

					File srcFile = ((TakesScreenshot) webDriver)
							.getScreenshotAs(OutputType.FILE);
					// File srcFile =
					// ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(srcFile, new File(strScreenshot));
					tempList_location.add(strScreenshot);

				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {

				tempList_location.add("No screenshot available");
			}
			tempList_result.add(result);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void writeStepResult(String strScenarioName,
			String strStepDescription, String strstrTestData, String strStatus,
			boolean isScreenshot, RemoteWebDriver webDriver) {
		// public void takeScreenshot(String strScenarioName,int testStep,
		// String strResultDescription, boolean Screenshot, String result,
		// RemoteWebDriver iPhoneDriver) {
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);

			tempList_scenario_name1.add(strScenarioName);
			tempList_teststep_description.add(strStepDescription);
			tempList_test_data.add(strstrTestData);

			/*
			 * tempList_testCase_name.add(strTestCaseName);
			 * tempList_exp_result.add(strExpectedResult);
			 * tempList_act_result.add(strActualResult);
			 * tempList_description.add(strResultDescription);
			 */

			// List<String> tempList_location = new ArrayList<String>(0);
			// List<String> tempList_result = new ArrayList<String>(0);

			// String screenshot = null;

			// FileUtils util = new FileUtils();
			// String strConfigFileName =
			// "/Users/testlab-imac/Documents/workspace/iPhoneTest/src/com/capgemini/results/screenshot/";
			if (isScreenshot) {
				now();
				Random rand = new Random();
				int num = rand.nextInt(1000);

				try {

					// Copy the file to screenshot folder
					// File tempFile = ((TakesScreenshot)
					// driver).getScreenshotAs(OutputType.FILE);
					// WebDriver augmentedDriver = new
					// Augmenter().augment(webDriver);

					File srcFile = ((TakesScreenshot) webDriver)
							.getScreenshotAs(OutputType.FILE);
					// File srcFile =
					// ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(srcFile, new File(strScreenshot));
					// tempList_location.add(strScreenshot);
					tempList_snapshot.add(strScreenshot);

				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {

				// tempList_location.add("No screenshot available");
				tempList_snapshot.add("No screenshot available");
			}
			// tempList_result.add(result);
			tempList_status.add(strStatus);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void reportGenerator() {
		FileWriter aWriter = null;
		String strComponent = null;
		String strBrowser = utils.getConfigValues("BrowserName");

		try {
			strComponent = utils.getConfigValues("Application Name");
			String time = now();

			String strReportFile = utils.getResultPath()
					+ utils.getConfigValues("Browser Type") + "_Report" + "_"
					+ time + ".html";

			aWriter = new FileWriter(strReportFile, true);

			aWriter.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"> ");
			aWriter.write("<html>");
			aWriter.write("<head>");

			aWriter.write("<link type=\"text/css\" href=\"./pages/css/themes/ui-lightness/jquery-ui-1.8.16.custom.css\" rel=\"Stylesheet\" />");
			aWriter.write("<link type=\"text/css\" href=\"./pages/css/myStyle.css\" rel=\"Stylesheet\" />");
			aWriter.write("<script type=\"text/javascript\" src=\"./pages/js/jquery-1.6.2.min.js\"></script>");
			aWriter.write("<script type=\"text/javascript\" src=\"./pages/js/jquery-ui-1.8.16.custom.min.js\"></script>");
			aWriter.write("<script type=\"text/javascript\" src=\"./pages/js/my.js\"></script>");
			aWriter.write("</head>");
			aWriter.write("<script type=\"text/javascript\">");

			aWriter.write("$(document).ready(function(){");
			aWriter.write("$(\"#tabs_environment,#tabs_plan,#tabs_set,#set_content_tabs\").tabs({");
			aWriter.write("selected: 0,");
			aWriter.write("deselectable: true");
			aWriter.write("});");
			aWriter.write("$(\"button\", \".btn\" ).button();");
			aWriter.write("$(\"button\", \".plan_step_list\" ).button();");
			aWriter.write("$(\"#tabs_plan\").hide();");
			aWriter.write("$(\"#tabs_set\").hide();");
			aWriter.write("$(\"#dialog\").dialog({");
			aWriter.write("autoOpen:false,");
			aWriter.write("modal:true,");
			aWriter.write("buttons:{");
			aWriter.write(" Store:function(){");
			aWriter.write("return;");
			aWriter.write("}");
			aWriter.write("},");
			aWriter.write("dialogClass: 'f2',");
			aWriter.write("resizable: true,");
			aWriter.write("show: 'slide',");
			aWriter.write("height:120");
			aWriter.write("});");

			aWriter.write("});");
			aWriter.write("</script>");
			aWriter.write("<body>");
			aWriter.write("<div class=\"page_container\">");
			aWriter.write("<div class=\"head\">");
			aWriter.write("<img alt=\"Capgemini\" src=\"./pages/images/logo160.gif\">");
			aWriter.write("</div>");
			aWriter.write("<div class=\"content\">");
			aWriter.write("<table class=\"content_table\" cellpadding=\"0\" cellspacing=\"0\">");
			aWriter.write("<tr>");
			aWriter.write("<td valign=\"top\">");
			aWriter.write("<div class=\"right_content\">");
			aWriter.write("<div id=\"tabs_environment\">");
			aWriter.write("<ul>");
			/*
			 * aWriter.write("<li><a href=\"#tabs-set-2\" class=\"f2\">" +
			 * strComponent + " Report </a></li>");
			 */
			aWriter.write("<li><a href=\"#tabs-set-2\" class=\"f2\">Functional Report </a></li>");
			aWriter.write("</ul>");
			aWriter.write("<div id=\"tabs-set-1\"  class=\"f2\">");
			aWriter.write("<div style=\"margin-top: 10px;\">");
			aWriter.write("<table id=\"set_table\" width=\"100%\" class=\"f2\" cellpadding=\"\" cellspacing=\"10\" ><tr>");
			aWriter.write("<td><b>Execution Date</b></td>");
			aWriter.write("<td><b>Execution Start Time</b></td>");
			aWriter.write("<td><b>Execution End Time</b></td>");
			aWriter.write("<td><b>Elapsed Time</b></td>");
			aWriter.write("<td><b>Operating System</b></td>");
			aWriter.write("<td><b>Browser Version</b></td>");
			aWriter.write("</tr>");
			aWriter.write("<tr class=\"list_table_tr\">");
			aWriter.write("<td>" + day + "-" + month + "-" + year + "</td>");
			aWriter.write("<td>" + strStartTime + "</td>");
			aWriter.write("<td>" + strStopTime + "</td>");
			aWriter.write("<td>" + Math.round((timeElapsed / (60000)) * 60)
					+ " " + "seconds" + "</td>");
			aWriter.write("<td>Windows - 7</td>");
			aWriter.write("<td>" + strBrowser + "</td>");
			aWriter.write("</tr>");
			aWriter.write(" <tr class=\"list_table_tr\">");
			aWriter.write(" <td></td>");
			aWriter.write(" <td></td>");
			aWriter.write(" <td></td>");
			aWriter.write("<td></td>");
			aWriter.write(" <td></td>");
			aWriter.write("<td></td>");
			aWriter.write("</tr>");
			aWriter.write("<tr class=\"list_table_tr\">");
			aWriter.write("<td><b>Test Scenario Name</b></td>");
			aWriter.write("<td><b>Test Case Name</b></td>");
			aWriter.write("<td><b>Test Case Desc</b></td>");
			aWriter.write("<td><b>Expected Result</b></td>");
			aWriter.write("<td><b>Actual Result</b></td>");
			aWriter.write("<td><b>Status</b></td>");
			aWriter.write("<td><b>ScreenShot</b></td>");
			aWriter.write("</tr>");
			// aWriter.write("<tr class=\"list_table_tr\">");
			// aWriter.write(" <td></td>");
			// aWriter.write(" <td></td>");
			// aWriter.write(" <td></td>");
			// aWriter.write("<td></td>");
			// aWriter.write("<td></td>");
			// aWriter.write("<td></td>");
			// aWriter.write("<td></td>");
			// aWriter.write("</tr>");
			aWriter.write("<tr class=\"list_table_tr\">");
			for (int i = 0; i < tempList_testCase_name.size(); i++) {
				if (i % 2 == 0) {
					aWriter.write("<tr class=\"list_table_tr\"><td >"
							+ tempList_scenario_name.get(i) + "</td>");
					aWriter.write("<td >" + tempList_testCase_name.get(i)
							+ "</td>");
					aWriter.write("<td >" + tempList_description.get(i)
							+ "</td>");
					aWriter.write("<td >" + tempList_exp_result.get(i)
							+ "</td>");
					aWriter.write("<td >" + tempList_act_result.get(i)
							+ "</td>");
					if ((tempList_result.get(i)).equalsIgnoreCase("Pass"))
						aWriter.write("<td><font color=\"Green\">"
								+ tempList_result.get(i) + "</td>\n");
					else
						aWriter.write("<td><font color=\"Red\">"
								+ tempList_result.get(i) + "</td>\n");
					aWriter.write("<td >");
					if (tempList_location.get(i).contentEquals(
							"No screenshot available")) {
						aWriter.write("No Screenshot available");

					} else {
						aWriter.write("<a href =\"");
						aWriter.write("file:///" + tempList_location.get(i));
						aWriter.write("\" target=\"_blank\">Screenshot</td>\n");

					}
				} else {
					aWriter.write("<tr class=\"list_table_tr\"><td >"
							+ tempList_scenario_name.get(i) + "</td>");
					aWriter.write("<td >" + tempList_testCase_name.get(i)
							+ "</td>");
					aWriter.write("<td >" + tempList_description.get(i)
							+ "</td>");
					aWriter.write("<td >" + tempList_exp_result.get(i)
							+ "</td>");
					aWriter.write("<td >" + tempList_act_result.get(i)
							+ "</td>");
					if ((tempList_result.get(i)).equalsIgnoreCase("Pass"))
						aWriter.write("<td><font color=\"Green\">"
								+ tempList_result.get(i) + "</td>\n");
					else
						aWriter.write("<td><font color=\"Red\">"
								+ tempList_result.get(i) + "</td>\n");
					aWriter.write("<td >");
					if (tempList_location.get(i).contentEquals(
							"No screenshot available")) {
						aWriter.write("No Screenshot available");

					} else {
						aWriter.write("<a href =\"");
						aWriter.write("file:///" + tempList_location.get(i));
						aWriter.write("\" target=\"_blank\">Screenshot</td>\n");

					}
				}
			}

			aWriter.write("</table>");
			aWriter.write("</div>");
			aWriter.write("</div>");
			aWriter.write("</div>");
			aWriter.write("</div>");
			aWriter.write("</td>");
			aWriter.write("</tr>");
			aWriter.write("</table>");
			aWriter.write("</div>");
			aWriter.write("</div>");
			aWriter.write("</body>");
			aWriter.write("</html>");
			aWriter.flush();
			aWriter.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void reportGenerator_Prasad() {
		FileWriter aWriter = null;
		String strComponent = null;

		try {
			strComponent = utils.getConfigValues("Application Name");
			String time = now();

			String strReportFile = "/Users/testlab-imac/Documents/workspace/iPhoneTest/src/com/capgemini/results/"
					+ "Report" + "_" + time + ".html";

			aWriter = new FileWriter(strReportFile, true);

			aWriter.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"> ");
			aWriter.write("<html>");
			aWriter.write("<head>");

			aWriter.write("<link type=\"text/css\" href=\"./pages/css/themes/ui-lightness/jquery-ui-1.8.16.custom.css\" rel=\"Stylesheet\" />");
			aWriter.write("<link type=\"text/css\" href=\"./pages/css/myStyle.css\" rel=\"Stylesheet\" />");
			aWriter.write("<script type=\"text/javascript\" src=\"./pages/js/jquery-1.6.2.min.js\"></script>");
			aWriter.write("<script type=\"text/javascript\" src=\"./pages/js/jquery-ui-1.8.16.custom.min.js\"></script>");
			aWriter.write("<script type=\"text/javascript\" src=\"./pages/js/my.js\"></script>");
			aWriter.write("</head>");
			aWriter.write("<script type=\"text/javascript\">");

			aWriter.write("$(document).ready(function(){");
			aWriter.write("$(\"#tabs_environment,#tabs_plan,#tabs_set,#set_content_tabs\").tabs({");
			aWriter.write("selected: 0,");
			aWriter.write("deselectable: true");
			aWriter.write("});");
			aWriter.write("$(\"button\", \".btn\" ).button();");
			aWriter.write("$(\"button\", \".plan_step_list\" ).button();");
			aWriter.write("$(\"#tabs_plan\").hide();");
			aWriter.write("$(\"#tabs_set\").hide();");
			aWriter.write("$(\"#dialog\").dialog({");
			aWriter.write("autoOpen:false,");
			aWriter.write("modal:true,");
			aWriter.write("buttons:{");
			aWriter.write(" Store:function(){");
			aWriter.write("return;");
			aWriter.write("}");
			aWriter.write("},");
			aWriter.write("dialogClass: 'f2',");
			aWriter.write("resizable: true,");
			aWriter.write("show: 'slide',");
			aWriter.write("height:120");
			aWriter.write("});");

			aWriter.write("});");
			aWriter.write("</script>");
			aWriter.write("<body>");
			aWriter.write("<div class=\"page_container\">");
			aWriter.write("<div class=\"head\">");
			aWriter.write("<img alt=\"Capgemini\" src=\"./pages/images/logo160.gif\">");
			aWriter.write("</div>");
			aWriter.write("<div class=\"content\">");
			aWriter.write("<table class=\"content_table\" cellpadding=\"0\" cellspacing=\"0\">");
			aWriter.write("<tr>");
			aWriter.write("<td valign=\"top\">");
			aWriter.write("<div class=\"right_content\">");
			aWriter.write("<div id=\"tabs_environment\">");
			aWriter.write("<ul>");
			aWriter.write("<li><a href=\"#tabs-set-2\" class=\"f2\">"
					+ strComponent + " Report </a></li>");
			aWriter.write("</ul>");
			aWriter.write("<div id=\"tabs-set-1\"  class=\"f2\">");
			aWriter.write("<div style=\"margin-top: 10px;\">");
			aWriter.write("<table id=\"set_table\" width=\"100%\" class=\"f2\" cellpadding=\"\" cellspacing=\"10\" ><tr>");
			aWriter.write("<td><b>Execution Date</b></td>");
			aWriter.write("<td><b>Execution Start Time</b></td>");
			aWriter.write("<td><b>Execution End Time</b></td>");
			aWriter.write("<td><b>Elapsed Time</b></td>");
			aWriter.write("</tr>");
			aWriter.write("<tr class=\"list_table_tr\">");
			aWriter.write("<td>" + day + "-" + month + "-" + year + "</td>");
			aWriter.write("<td>" + strStartTime + "</td>");
			aWriter.write("<td>" + strStopTime + "</td>");
			aWriter.write("<td>" + Math.round((timeElapsed / (60000)) * 60)
					+ " " + "seconds" + "</td>");
			aWriter.write("</tr>");
			aWriter.write(" <tr class=\"list_table_tr\">");
			aWriter.write(" <td></td>");
			aWriter.write(" <td></td>");
			aWriter.write(" <td></td>");
			aWriter.write("<td></td>");
			aWriter.write("</tr>");
			aWriter.write("<tr class=\"list_table_tr\">");
			aWriter.write("<td><b>Test Scenario Name</b></td>");
			aWriter.write("<td><b>Test Case Name</b></td>");

			// aWriter.write("<td><b>Expected Result</b></td>");
			// aWriter.write("<td><b>Actual Result</b></td>");
			aWriter.write("<td><b>Status</b></td>");
			aWriter.write("<td><b>Result Description</b></td>");
			aWriter.write("<td><b>ScreenShot</b></td>");
			aWriter.write("</tr>");
			// aWriter.write("<tr class=\"list_table_tr\">");
			// aWriter.write(" <td></td>");
			// aWriter.write(" <td></td>");
			// aWriter.write(" <td></td>");
			// aWriter.write("<td></td>");
			// aWriter.write("<td></td>");
			// aWriter.write("<td></td>");
			// aWriter.write("<td></td>");
			// aWriter.write("</tr>");
			aWriter.write("<tr class=\"list_table_tr\">");
			for (int i = 0; i < tempList_testCase_name.size(); i++) {
				if (i % 2 == 0) {
					aWriter.write("<tr class=\"list_table_tr\"><td >"
							+ tempList_scenario_name.get(i) + "</td>");
					aWriter.write("<td >" + tempList_testCase_name.get(i)
							+ "</td>");

					/*
					 * aWriter.write("<td >" + tempList_exp_result.get(i) +
					 * "</td>");
					 */
					/*
					 * aWriter.write("<td >" + tempList_act_result.get(i) +
					 * "</td>");
					 */
					if ((tempList_result.get(i)).equalsIgnoreCase("Pass"))
						aWriter.write("<td><font color=\"Green\">"
								+ tempList_result.get(i) + "</td>\n");
					else
						aWriter.write("<td><font color=\"Red\">"
								+ tempList_result.get(i) + "</td>\n");
					aWriter.write("<td >" + tempList_description.get(i)
							+ "</td>");
					aWriter.write("<td >");
					if (tempList_location.get(i).contentEquals(
							"No screenshot available")) {
						aWriter.write("No Screenshot available");

					} else {
						aWriter.write("<a href =\"");
						aWriter.write("file:///" + tempList_location.get(i));
						aWriter.write("\" target=\"_blank\">Screenshot</td>\n");

					}
				} else {
					aWriter.write("<tr class=\"list_table_tr\"><td >"
							+ tempList_scenario_name.get(i) + "</td>");
					aWriter.write("<td >" + tempList_testCase_name.get(i)
							+ "</td>");

					/*
					 * aWriter.write("<td >" + tempList_exp_result.get(i) +
					 * "</td>");
					 */
					/*
					 * aWriter.write("<td >" + tempList_act_result.get(i) +
					 * "</td>");
					 */
					if ((tempList_result.get(i)).equalsIgnoreCase("Pass"))
						aWriter.write("<td><font color=\"Green\">"
								+ tempList_result.get(i) + "</td>\n");
					else
						aWriter.write("<td><font color=\"Red\">"
								+ tempList_result.get(i) + "</td>\n");
					aWriter.write("<td >" + tempList_description.get(i)
							+ "</td>");
					aWriter.write("<td>");

					if (tempList_location.get(i).contentEquals(
							"No screenshot available")) {
						aWriter.write("No Screenshot available</td>\n");

					} else {
						aWriter.write("<a href =\"");
						aWriter.write("file:///" + tempList_location.get(i));
						aWriter.write("\" target=\"_blank\">Screenshot</td>\n");

					}
				}
			}

			aWriter.write("</table>");
			aWriter.write("</div>");
			aWriter.write("</div>");
			aWriter.write("</div>");
			aWriter.write("</div>");
			aWriter.write("</td>");
			aWriter.write("</tr>");
			aWriter.write("</table>");
			aWriter.write("</div>");
			aWriter.write("</div>");
			aWriter.write("</body>");
			aWriter.write("</html>");
			aWriter.flush();
			aWriter.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/*
	 * public void takeScreenshot(String sce_name,String test_case_name,String
	 * exp_result,String Act_result, String Description, boolean Screenshot,
	 * String result, RemoteWebDriver iPhoneDriver) {
	 * 
	 * try { Calendar cal = Calendar.getInstance(); SimpleDateFormat sdf = new
	 * SimpleDateFormat(DATE_FORMAT_NOW); tempList_scenario_name.add(sce_name);
	 * tempList_testCase_name.add(test_case_name);
	 * tempList_exp_result.add(exp_result); tempList_act_result.add(Act_result);
	 * tempList_description.add(Description);
	 * 
	 * //List<String> tempList_location = new ArrayList<String>(0);
	 * //List<String> tempList_result = new ArrayList<String>(0);
	 * 
	 * String screenshot = null;
	 * 
	 * //FileUtils util = new FileUtils(); String strConfigFileName =
	 * "/Users/testlab-imac/Documents/workspace/iPhoneTest/src/com/capgemini/results/screenshot/"
	 * ; if (Screenshot) { now(); Random rand = new Random(); int num =
	 * rand.nextInt(1000); screenshot = (String) (strConfigFileName +
	 * sdf.format(cal.getTime())+num +".png"); File testScreenShot = new
	 * File(strConfigFileName ,sdf.format(cal.getTime())+num + ".png");
	 * 
	 * try {
	 * 
	 * //Copy the file to screenshot folder File srcFile =
	 * ((TakesScreenshot)iPhoneDriver).getScreenshotAs(OutputType.FILE);
	 * FileUtils.copyFile(srcFile, new File("remote_webdriver.png"));
	 * //FileUtils.copyFile(scrFile, testScreenShot);
	 * 
	 * } catch (IOException e) { e.printStackTrace(); }
	 * 
	 * 
	 * tempList_location.add(screenshot); } else {
	 * 
	 * tempList_location.add("No screenshot available"); }
	 * tempList_result.add(result); } catch (Exception ex) {
	 * ex.printStackTrace(); }
	 * 
	 * }
	 */

}
