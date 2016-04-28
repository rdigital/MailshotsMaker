package com.capgemini.executor;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.capgemini.driver.ScriptExecutor;
import com.capgemini.utilities.Reporter;
import com.capgemini.utilities.Utilities;



public class New_Executioner extends JFrame implements ActionListener{
	public static boolean testNG=false;
	Utilities utils = new Utilities();
	public static String strExecutionStartTime = null;
	public static String strExecutionStopTime = null;
	public static float totalExecutionTime = 0;
	public static String StrCBTReportFile = null;
	public static int totalScannedPages = 0;
	public static String strExecutionBrowser = null;
	private static String browser;
	
	
	ScriptExecutor executor = new ScriptExecutor();
	String strBrowserType = null;
	
	JLayeredPane jp;
	
	ImageIcon icon;
	JLabel thumb;
	ImageIcon create;
	ImageIcon execute;
	ImageIcon exit;
	JButton createBtn;
	JButton executeBtn;
	JButton exitBtn;
	JLabel title;
	Font f= new Font("Arial", Font.BOLD , 30);
	Properties properties = new Properties();
	
	public New_Executioner(){
		super("WELCOME TO CAFÉ SELENIUM!!");
		jp = new JLayeredPane();
		title=new JLabel("CAFÉ Selenium");
		title.setFont(f);
		
		icon = new ImageIcon("./data/icons/newcafe-blank.jpg"); 
		thumb = new JLabel();
		create = new ImageIcon("./data/icons/create.jpg");
		execute = new ImageIcon("./data/icons/execute.jpg");
		exit = new ImageIcon("./data/icons/exit.jpg");
		createBtn= new JButton(create);
		executeBtn= new JButton(execute);
		exitBtn= new JButton(exit);
		thumb.setIcon(icon);
		jp.setLayout(null);
		jp.add(thumb).setBounds(0, 0, 600, 300);
		
		jp.add(title).setBounds(270, 100, 300, 60);
		jp.add(createBtn).setBounds(220, 220, 100, 30);
		jp.add(executeBtn).setBounds(340, 220, 100, 30);
		jp.add(exitBtn).setBounds(460, 220, 100, 30);
		
		jp.setLayer(title, 1);
		jp.setLayer(createBtn,1);
		jp.setLayer(executeBtn,1);
		jp.setLayer(exitBtn,1);
		jp.setLocation(350, 150);
		
		createBtn.addActionListener(this);
		executeBtn.addActionListener(this);
		exitBtn.addActionListener(this);
		
		
		this.add(jp);
		this.pack();
		this.setSize(610, 330);
		this.setLocation(350, 150);
		this.setResizable(false);
		
	}
	
	public static void main(String[] args) {
		New_Executioner ne=new New_Executioner();
		// TODO Auto-generated method stub
		if(testNG){
			ne.executeParallel(args);
		}else{
			ne.setVisible(true);
		}
	}
	
	public void executeParallel(String[] args){
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("./data/config.properties"));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		RemoteWebDriver driver = null;
		strBrowserType = args[1];
		browser=strBrowserType;
		Reporter.browser =browser;
		strExecutionBrowser = strBrowserType;
		if (strBrowserType == null) {
			strBrowserType = "Internet Explorer";
		}
		try {
			String path = properties.getProperty("file_path");
			System.setProperty("File", path);
			executor.executeScriptMultiple(driver, args[0], args[1],args[2]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Properties properties = new Properties();

		try {
			properties.load(new FileInputStream("./data/config.properties"));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		if(e.getSource()==createBtn){
			Cafe_UI recorderUi = new Cafe_UI();

			recorderUi.createUI();
			recorderUi.viewUI();
		}
		else if(e.getSource()==executeBtn){
			Object[] options = { "Serial" , "Parallel"  };
			int i = JOptionPane.showOptionDialog(null,
					"What action you want to do?",
					"Please Select Execution Type! ",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			
			String MasterReport;
			try {
				MasterReport = readFile("./results/MasterReport.html");
		
			int end=0;
			end=MasterReport.lastIndexOf("<!--STARTOFROWS-->")+18;
			MasterReport=MasterReport.substring(0, end);
			FileWriter aWriter = null;
			aWriter= new FileWriter("./results/MasterReport.html");
			aWriter.write(MasterReport);
			aWriter.flush();
			aWriter.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			if(i==0){				
				try {
					String path = properties.getProperty("file_path");
					System.setProperty("File", path);
					executor.executeScriptMultiple();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else{
				
				String[] temp =null;
				ParallelRun.main(temp);
			}
		}
		
		else if(e.getSource()==exitBtn){
			System.exit(0);
		}
		this.setVisible(false);
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
	
}


