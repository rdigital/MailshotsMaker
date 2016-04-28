package com.capgemini.executor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class ParallelRun {

	static JComboBox script_comboBox;
	
	public static void main(String[] a) {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //private JButton go_button;
    String[] columnTitles = { "Host","Testcase" ,"Browser Name"};
    Object[][] dataEntries = { { "", "", "", "", new Boolean(true) } };
    //TableModel model = new EditableTableModel(columnTitles, dataEntries);
    final DefaultTableModel model = new DefaultTableModel(dataEntries,columnTitles);
    final JTable table = new JTable(model);
    table.createDefaultColumnsFromModel();
    
    String[] browserName = { "Chrome", "Firefox", "IE"};
    JComboBox comboBox = new JComboBox(browserName);
    script_comboBox = new JComboBox();
    showFiles();
    table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(script_comboBox));
    table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBox));
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JButton jbinsert=new JButton("Insert");
    JButton jbdelete=new JButton("Delete");
    JButton jbexecute=new JButton("Execute");
    frame.setLayout(null);
    frame.add(new JScrollPane(table)).setBounds(10, 10, 500, 400);
    
    
    
    frame.add(jbinsert).setBounds(10,420,100,30);
    frame.add(jbdelete).setBounds(120,420,100,30);
    frame.add(jbexecute).setBounds(230,420,100,30);
    
    frame.pack();
	frame.setSize(520,490);
	frame.setLocation(350, 150);
	frame.setVisible(true);
	frame.setResizable(false);
	frame.setTitle("Parallel Run...");
	
	jbinsert.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			model.insertRow(table.getRowCount(), new Object[]{"","",""});
			
		}
	});
	
	jbdelete.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int SelectedRow = table.getSelectedRow();
			if(SelectedRow!=-1){
				model.removeRow(SelectedRow);
			}
		}
	});
	
	jbexecute.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int noRows=table.getRowCount();
			if(noRows!=0){
				XmlSuite suite = new XmlSuite();
				suite.setName("Suite");
				suite.setParallel(suite.PARALLEL_TESTS);
				 for(int i=0;i<noRows;i++){
					XmlTest test = new XmlTest(suite);
					test.setName("Test"+i);
					Map<String,String> param = new HashMap<String,String>();
					String temp=table.getValueAt(i, 0).toString();
					temp=(temp.equals("localhost")) ? "": "http://"+temp+"/wd/hub";
					param.put("host", temp);
					param.put("browser", table.getValueAt(i, 2).toString());
					param.put("testName", table.getValueAt(i, 1).toString());
					test.setParameters(param);
					List<XmlClass> classes = new ArrayList<XmlClass>();
					classes.add(new XmlClass("com.capgemini.executor.NewParallelExecutioner"));
					test.setXmlClasses(classes) ;
				 }
				 List<XmlSuite> suites = new ArrayList<XmlSuite>();
				 suites.add(suite);
				 TestNG tng = new TestNG();
				 tng.setXmlSuites(suites);
				 tng.run();

			}
		}
	});
   
    
  }
  
  private static void showFiles(){
	    String myPath = "src/com/capgemini/scripts";
	    File folder = new File(myPath);
	    File[] listOfFiles = folder.listFiles();
	    for (int i = 0; i < listOfFiles.length; i++) {
	    	String exAppName=listOfFiles[i].getName();
	    	if(exAppName.endsWith(".java"))
	    		script_comboBox.addItem(exAppName.substring(0, exAppName.length()-5));
	    	
	    }
	}

}

class EditableTableModel extends AbstractTableModel {
  String[] columnTitles;

  Object[][] dataEntries;

  int rowCount;

  public EditableTableModel(String[] columnTitles, Object[][] dataEntries) {
    this.columnTitles = columnTitles;
    this.dataEntries = dataEntries;
  }

  public int getRowCount() {
    return dataEntries.length;
  }

  public int getColumnCount() {
    return columnTitles.length;
  }

  public Object getValueAt(int row, int column) {
    return dataEntries[row][column];
  }

  public String getColumnName(int column) {
    return columnTitles[column];
  }

  public Class getColumnClass(int column) {
    return getValueAt(0, column).getClass();
  }

  public boolean isCellEditable(int row, int column) {
    return true;
  }

  public void setValueAt(Object value, int row, int column) {
    dataEntries[row][column] = value;
  }
}