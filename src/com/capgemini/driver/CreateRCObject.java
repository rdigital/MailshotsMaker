package com.capgemini.driver;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;

import com.capgemini.executor.Executioner;
import com.capgemini.utilities.Utilities;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

/**
 * CreateRCObject --- Class for the creation of Selenium RC object
 * @author   Prasad Joshi
 */

public class CreateRCObject {

	Utilities util = new Utilities();
	
	/**
	   * After successful execution of this method an object of Selenium class gets created 
	   * @param 1. Application URL
	   * @return Object og DefaultSelenium class.
	   */
	
	public Selenium getRCObject(String strApplicationURL){
		
		String strBrowserType = null;
		String strAppURL = null;
		strBrowserType = Executioner.strExecutionBrowser;
		strAppURL = strApplicationURL;
		
		//startSeleniumServer();
		/*SeleniumServer _server;
		try {
			_server = new SeleniumServer();
			_server.boot();
			_server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	
		if(strBrowserType.equalsIgnoreCase("IE") || strBrowserType.equalsIgnoreCase("Internet Explorer")){
			strBrowserType = "*iexplore";
			//strBrowserType = "*iehta";
		}
		if(strBrowserType.equalsIgnoreCase("FF") || strBrowserType.equalsIgnoreCase("Firefox")){
			strBrowserType = "*firefox";
		}
		if (strBrowserType.equalsIgnoreCase("Chrome")){
			strBrowserType = "*chrome";
		}
		Selenium selenium = new DefaultSelenium("localhost", 4444, strBrowserType, strAppURL);
		return selenium;
	}
	
	/**
	   * After successful execution of this method Selenium server starts running 
	   * @param . No Param
	   * @return Void.
	   */
	
	public void startSeleniumServer(){
		SeleniumServer server = null;
		try { 
			//DesiredCapabilities  DesiredCapability = new DesiredCapabilities();
			//DesiredCapability.setBrowserName("safari");
			//ServerSocket serverSocket = new ServerSocket(RemoteControlConfiguration.DEFAULT_PORT);  
			ServerSocket serverSocket = new ServerSocket(4444);
			serverSocket.close();  
		
			  //Server not up, start it  

			try {  
					RemoteControlConfiguration rcc = new RemoteControlConfiguration();  
					rcc.setPort(RemoteControlConfiguration.DEFAULT_PORT);  
					//rcc.setPort(4445);
					server = new SeleniumServer(false, rcc);  
			} catch (Exception e) {  
				System.err.println("Could not create Selenium Server because of: " 
									+ e.getMessage());  
				e.printStackTrace();  
			}
		
			try	{  
				server.start();  
				System.out.println("Server started");  
			} catch (Exception e) {  
				System.err.println("Could not start Selenium Server because of: " 
									+ e.getMessage());  
				e.printStackTrace();  
			}
		
		} catch (BindException e) {  
			System.out.println("Selenium server already up, will reuse...");  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	} 
}
