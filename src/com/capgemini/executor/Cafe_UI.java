package com.capgemini.executor;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import jxl.read.biff.BiffException;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.capgemini.utilities.ReadExcel;

//import com.capgemini.browser.Select_Browser;
//import com.capgemini.ui.Generator;

public class Cafe_UI extends JPanel implements ActionListener {

	/**
	 * @param args
	 */
	private JPanel panel=new JPanel();
    private JLabel cglogo = new JLabel(new ImageIcon("data/icons/capgemini.png"));
    private JLabel sglogo = new JLabel(new ImageIcon("data/icons/Sogeti.png"));
    
	public String url_name,combo_text,appName;
	private JLabel h_label;
	private JLabel browser_label;
	
	private JLabel url_label;
	private JTextField url_txt;
	
	private JLabel process_label;
	private JTextField process_txt;
	public static boolean exFlag=false;
	
	
	private JButton open_browser;
	private JButton close_browser;
	List<String> outFinal=new ArrayList<String>();
	List<WebElement> outEle;
	private String[] browsers = { "Select Browser","Internet Explorer", "Chrome", "Firefox" };
	private JComboBox comboBox = new JComboBox();
	private JLabel script_combo_lable;
		
	private JCheckBox script_check;
	private JLabel check_lable;
	private JComboBox script_comboBox = new JComboBox();
	
	private JLabel id_label;
	private JTextField id_txt;
	
	private JLabel name_label;
	private JTextField name_txt;
	
	private JLabel value_label;
	private JTextField value_txt;
	
	private JLabel tag_label;
	private JTextField tag_txt;
	
	private JButton add_ele;
	
	private int count = 0;
	String selected_browser = null;
	String strScriptFile=null;
	String existing="";
	public static String newFunc="";
	WebDriver driver = null;
	WebDriver drv = null;
	String url = null;
	ExecutorService executor;
	List<JSONObject> elementList=new ArrayList<JSONObject>();

	public List<JSONObject> getElementList() {
		return elementList;
	}

	public void setElementList(List<JSONObject> elementList) {
		this.elementList = elementList;
	}

	
	public void createUI() {

		try

		{

			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

		}catch(Exception e){}

		h_label = new JLabel("CAFE Script Recorder");
		h_label.setFont(new Font(h_label.getName(), Font.BOLD, 20));
		
		url_label = new JLabel("Enter Test URL");
		url_txt = new JTextField(4);
		
		process_label = new JLabel("Application Name");
		process_txt = new JTextField(4);
		
		check_lable=new JLabel();
		script_check=new JCheckBox();
		check_lable=new JLabel("Add new method to existing script");
		script_combo_lable=new JLabel("Select Script here");
		//script_comboBox;
		
	
		browser_label = new JLabel("Select Browser");
		for (int i = 0; i < browsers.length; i++) {
			comboBox.addItem(browsers[count++]);
		}

		open_browser = new JButton("START");

		close_browser = new JButton("STOP");
		
		
		setLayout(null);

		add(h_label);
		
		add(url_label);
		add(url_txt);
		
		add(process_label);
		add(process_txt);

		
		add(browser_label);
		add(comboBox);
		
		add(script_check);
		add(check_lable);
		
		add(script_combo_lable);
		add(script_comboBox);
		
		add(open_browser);

		add(close_browser);


		h_label.setBounds(230, 5, 500, 65);
		
		url_label.setBounds(75, 70, 100, 25);
		url_txt.setBounds(240, 70, 375, 30);
		
		process_label.setBounds(75, 110, 150, 25);
		process_txt.setBounds(240, 110, 375, 30);
		
		browser_label.setBounds(75, 160, 150, 25);
		comboBox.setBounds(240, 160, 130, 20);
		
		script_check.setBounds(75, 200, 20,50);
		check_lable.setBounds(100, 200, 150, 50);
		
		script_combo_lable.setBounds(75,250, 150, 20);
		script_comboBox.setBounds(240, 250, 130, 20);
		
		open_browser.setBounds(75, 300, 130, 25);

		close_browser.setBounds(450, 300, 130, 25);

		script_combo_lable.setVisible(false);
		script_comboBox.setVisible(false);
		showFiles();								//showfile called here
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected_browser = (String) ((JComboBox) e.getSource())
						.getSelectedItem();
				
			//System.out.println(url_txt);
				
			}
		});
	
		
/*		open_browser.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				System.out.println("Called");
				
				if(url_txt.getText() != null){
					url = url_txt.getText();
					appName=(String)  process_txt.getText();
					//System.out.println(appName);
					
				}else if(url==null && url.length()>0){
					JOptionPane.showMessageDialog(null, "Please Enter Url");
				}
				if (selected_browser != null) {
					//driver = Select_Browser.createDriver(selected_browser,url);
				}else{
					JOptionPane.showMessageDialog(null, "Please select Browser");
				}
			}
		});*/
		
		
		close_browser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				driver.quit();
			}
		});
		
		
		
		open_browser.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				url_name=url_txt.getText();
				appName=(String)  process_txt.getText();
				if(!existing.equals("")){
					exFlag=true;
					newFunc=appName;
					appName=existing;
					ReadExcel reader=new ReadExcel();
					try {
						url_name=reader.getNextURL(appName);
					} catch (BiffException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
				}
				strScriptFile = "src/com/capgemini/scripts/"+appName+".java";
				combo_text=(String) comboBox.getSelectedItem();
				drv=launch_browser(combo_text);
				Writer wr=new Writer();
				if(!exFlag){
				if(drv!=null){
				drv.get(url_name);
				}
				try {
					wr.generatePre(strScriptFile, appName);
					wr.funcPre(strScriptFile, appName);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				wr.generateCodeForTextboxes(drv, strScriptFile, appName);
				wr.generateCodeForButtons(drv, strScriptFile, appName);
				wr.generateCodeForCheckboxes(drv, strScriptFile, appName);
				wr.generateCodeForRadioButtons(drv, strScriptFile, appName);
				wr.generateCodeForDropdowns(drv, strScriptFile, appName);
				wr.generateCodeForImages(drv, strScriptFile, appName);
				wr.generateCodeForLinks(drv, strScriptFile, appName);
				
				try {
					wr.funcPost(strScriptFile);
					wr.generatePost(strScriptFile);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				WriteMaster wm = new WriteMaster();
				wm.createDataWorkbook(appName,url_name);
				}
				else{
					if(drv!=null){
						drv.get(url_name);
					}
					try {
						wr.updateScript(drv,strScriptFile, newFunc,appName);  //Updating in existing script function call
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				JOptionPane.showMessageDialog(null, "Creation Completed",
						"WELCOME TO SELENIUM CAFE", 1);

			}
		});
		
		script_check.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(script_check.isSelected()){
					script_combo_lable.setVisible(true);
					script_comboBox.setVisible(true);
					url_txt.setEnabled(false);	
					
				}else{
					script_combo_lable.setVisible(false);
					script_comboBox.setVisible(false);
					url_txt.setEnabled(true);
					existing="";	
				}
				
			}
		});
		
		script_comboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				existing=(String)script_comboBox.getSelectedItem();
				
			}
		});
	}
		
		private static void setLookAndFeel(String string) {
		// TODO Auto-generated method stub
		
	}

		public WebDriver launch_browser(String selected_browser)
		{
			switch (selected_browser) {
			case "Internet Explorer":
				System.setProperty("webdriver.ie.driver", "/data/IEDriverServer.exe");
				DesiredCapabilities capab = DesiredCapabilities.internetExplorer();
				capab.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				driver=new InternetExplorerDriver(capab);
				break;
			case "Chrome":
				String chromeDriverLocation = "./data/chromedriver.exe";
				System.setProperty("webdriver.chrome.driver", chromeDriverLocation);
				driver=new ChromeDriver();
				break;
				
			case "Firefox":
				driver=new FirefoxDriver();
				//System.out.println(url);
				break;
			default: driver=null;
				break;
			}
			return driver;
		}
		
		
		public void viewUI() {
			

		panel.setLayout(null);
		panel.setBackground(Color.WHITE);
		panel.add(cglogo).setBounds(0, 0, 250, 63);	
		panel.add(sglogo).setBounds(412, 0, 288, 63);	
		panel.setSize(700, 63);

		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		
		
		ImageIcon img = new ImageIcon("//Cafe_with_UI//data/icons//capgemini.png");
		JFrame frame = new JFrame("CAFE SELENIUM");
		
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.getContentPane().add(this);.
		frame.setLayout(null);
		frame.setBackground(Color.WHITE);

		frame.add(panel).setBounds(0, 0, 700, 63);
		frame.add(this).setBounds(0, 63, 700, 400);
		frame.pack();
		frame.setSize(700,463);
		frame.setLocation(350, 150);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setIconImage(img.getImage());
		
	}
		
		private void showFiles(){
		    String myPath = "src/com/capgemini/scripts";
		    File folder = new File(myPath);
		    File[] listOfFiles = folder.listFiles();
		    for (int i = 0; i < listOfFiles.length; i++) {
		    	String exAppName=listOfFiles[i].getName();
		    	if(exAppName.endsWith(".java"))
		    		script_comboBox.addItem(exAppName.substring(0, exAppName.length()-5));
		    	
		    }
		}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
