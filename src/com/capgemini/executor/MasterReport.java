package com.capgemini.executor;

import java.io.IOException;

import com.capgemini.utilities.ReadExcel;
import com.capgemini.utilities.Reporter;

public class MasterReport {
String pass;
	public MasterReport(String pass) {
		// TODO Auto-generated constructor stub
		this.pass=pass;
	}
public MasterReport() {
		// TODO Auto-generated constructor stub
	}
Reporter reporter = new Reporter(pass);
ReadExcel excel = new ReadExcel(reporter);
public void call() throws IOException
{
	reporter.MasterReportGenerator();
	excel.ReadScenarioName();
}

}
