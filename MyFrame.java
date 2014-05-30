import javax.swing.*;

import java.awt.Color;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdesktop.swingx.*;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

@SuppressWarnings("serial")
class MyFrame extends JFrame {
	
	private final int WIDTH = 500;
	private final int HEIGHT = 400;
    private javax.swing.JButton delCreatedButton;
    private javax.swing.JTextField forDaysOff;
    private javax.swing.JTextField forExtraWorkDays;
    private javax.swing.JButton getParamsAndStartButton;
    private javax.swing.JButton fileChooserButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private org.jdesktop.swingx.JXMonthView jXMonthView1;
    private javax.swing.JComboBox monthChooser;
    private String pathTOYearPlan="";
	
	MyFrame()
	{
		this.setTitle("Программа автоматического планирования ТО");
		this.setSize(this.WIDTH, this.HEIGHT);
		initComponents();
	}
	
	
	   private void initComponents() {

	        jPanel1 = new javax.swing.JPanel();
	        //pathTOYearPlan = new javax.swing.JTextField();
	        forDaysOff = new javax.swing.JTextField();
	        forExtraWorkDays = new javax.swing.JTextField();
	        monthChooser = new javax.swing.JComboBox();
	        jLabel1 = new javax.swing.JLabel();
	        jLabel2 = new javax.swing.JLabel();
	        jLabel3 = new javax.swing.JLabel();
	        jLabel4 = new javax.swing.JLabel();
	        jXMonthView1 = new org.jdesktop.swingx.JXMonthView();
	        fileChooserButton = new JButton();
	        delCreatedButton = new javax.swing.JButton();
	        getParamsAndStartButton = new javax.swing.JButton();


	        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

	       // pathTOYearPlan.setText("YP2014_2.ods");
	        
			String[] rusMonthes={"Январь", "Февраль", "Март", "Апрель", "Май",
									"Июнь", "Июль", "Август", "Сентябрь", "Октябрь",
									"Ноябрь", "Декабрь"};
	        monthChooser.setModel(new javax.swing.DefaultComboBoxModel(rusMonthes));
	        monthChooser.addActionListener(new ActionListener(){
	        	public void actionPerformed(ActionEvent e){
	        		monthChooserAction();
	        	}
	        });
	        

	        jLabel1.setText("Путь к годовому плану");

	        jLabel2.setText("Необходим план ТО на:");

	        jLabel3.setText("Доп. выходные дни");

	        jLabel4.setText("Доп. рабочие дни");

	        jXMonthView1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		    Date firstDay=new Date (112, 0, 1);
			jXMonthView1.setFirstDisplayedDay(firstDay);
			jXMonthView1.setFirstDayOfWeek(2);
	        
			fileChooserButton.setText("Найти");
			fileChooserButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					fileChooserButtonAction();
				}
			});
	        
	        delCreatedButton.setText("Delete All");
	        delCreatedButton.setEnabled(false); //until fileChooser wasn't activated
	        delCreatedButton.addActionListener(new ActionListener(){
	        	public void actionPerformed(ActionEvent e){
	        		delCreatedButtonAction();
	        	}
	        });

	        getParamsAndStartButton.setText("OK");
	        getParamsAndStartButton.setEnabled(false); //until fileChooser wasn't activated
	        getParamsAndStartButton.addActionListener(new ActionListener(){
	        	public void actionPerformed(ActionEvent e){
	        		getParamsAndStartButtonAction();
	        	}
	        });

	        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
	        jPanel1.setLayout(jPanel1Layout);
	        jPanel1Layout.setHorizontalGroup(
	            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(jPanel1Layout.createSequentialGroup()
	                .addGap(12, 12, 12)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                    .addGroup(jPanel1Layout.createSequentialGroup()
	                        .addComponent(jLabel2)
	                        .addGap(0, 0, Short.MAX_VALUE)))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
	                    .addComponent(monthChooser, 0, 120, Short.MAX_VALUE)
	                    .addComponent(fileChooserButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	                .addGap(44, 44, 44))
	            .addGroup(jPanel1Layout.createSequentialGroup()
	                .addContainerGap()
	                .addComponent(jLabel3)
	                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	            .addGroup(jPanel1Layout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
	                    .addGroup(jPanel1Layout.createSequentialGroup()
	                        .addComponent(jLabel4)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                        .addComponent(forExtraWorkDays, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
	                    .addComponent(forDaysOff, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addComponent(jXMonthView1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addGroup(jPanel1Layout.createSequentialGroup()
	                        .addComponent(getParamsAndStartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                        .addComponent(delCreatedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
	                .addGap(0, 0, Short.MAX_VALUE))
	        );
	        jPanel1Layout.setVerticalGroup(
	            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(jPanel1Layout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel1)
	                    .addComponent(fileChooserButton))
	                .addGap(10, 10, 10)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(monthChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addComponent(jLabel2))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addComponent(jXMonthView1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addGap(18, 18, 18)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel3)
	                    .addComponent(forDaysOff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel4)
	                    .addComponent(forExtraWorkDays, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addGap(18, 18, 18)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(getParamsAndStartButton)
	                    .addComponent(delCreatedButton))
	                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	        );

	        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
	        getContentPane().setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	        );

	        pack();
	    }// </editor-fold>  

	   
	   
	   private void delCreatedButtonAction()
	   {
		   File theFile=null;
		   SpreadSheet toClean=null;
			try {
				 theFile=new File(pathTOYearPlan);
				 toClean = SpreadSheet.createFromFile(theFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("No file found to clean");
				e.printStackTrace();
			}
		   
			int sheetCount=toClean.getSheetCount();
			
			if(sheetCount>1)
			{
				for(int i=sheetCount-1; i>0; i--)
				{
					toClean.getSheet(i).detach();
				}
			}
			try {
				toClean.saveAs(theFile);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

	   }//delCreatedButtonAction
	   
	   
	   
	   private void fileChooserButtonAction()
	   {
		    JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		    		"OpenOffice Spreadsheets", "ods");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(getParent());
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       this.pathTOYearPlan=chooser.getSelectedFile().getAbsolutePath();	
		       YearPlan test = new YearPlan(this.pathTOYearPlan);
		       if(!test.checkIfYearPlan())
		       {
		    	   this.jLabel1.setText("Некорректный план   ");
		    	   this.jLabel1.setForeground(Color.RED);
		       }
		       else
		       {
		       
					if(this.pathTOYearPlan.length()>20)
					{
						String t = "..."+this.pathTOYearPlan.substring(this.pathTOYearPlan.length()-20,
																	this.pathTOYearPlan.length());
						this.jLabel1.setText(t);
					}
					else
					{
						this.jLabel1.setText(this.pathTOYearPlan);
					}
					this.jLabel1.setForeground(Color.BLACK);
					delCreatedButton.setEnabled(true);
					getParamsAndStartButton.setEnabled(true);
		       }
	   	    }//if(returnVal)
	   }
	   
	   
	   
	   
	   private void getParamsAndStartButtonAction()
	   {
		   
			//----get from UI later------------------
			int monthPlanFor=monthChooser.getSelectedIndex()+1;
			int[] daysOff=parseIntArray(forDaysOff.getText());
			int[] extraWorkDays = parseIntArray(forExtraWorkDays.getText());
			String path=pathTOYearPlan;
			//---------------------------------------
			MonthParameters theMonthParmeters = new MonthParameters(monthPlanFor, daysOff, extraWorkDays);		
			
			YearPlan yPlan = new YearPlan(path);				
			new MonthPlan(yPlan, theMonthParmeters);
	   
	   }
	   
	   private void monthChooserAction()
	   {
		   Date toDisplay=new Date (112, monthChooser.getSelectedIndex(), 1);
		   jXMonthView1.setFirstDisplayedDay(toDisplay);
	   }
	   
	   
	   private int[] parseIntArray(String s)
	   {
			if(s.equals(""))
			{
				return new int[]{};
			}
			ArrayList<String> intStrings = new ArrayList<String>();
			int intStart=0;
			int intEnd=-1;
			for(int i=0; i<s.length(); i++)
			{

				if(!Character.isDigit(s.charAt(i)) && intStart>=0)
				{
					intEnd=i;
					intStrings.add(s.substring(intStart, intEnd));
					intStart=-1;
				}
				else if(intStart==-1)
				{
					if(Character.isDigit(s.charAt(i)))
					{
						intStart=i;
					}
				}
						
			}
			intStrings.add(s.substring(intStart, s.length()));
			System.out.println(intStrings);
			
			
			int[] toReturn=new int[intStrings.size()];
			

			
			for(int i=0; i<intStrings.size(); i++)
			{
				toReturn[i]=Integer.parseInt(intStrings.get(i));
			}
			
			return toReturn;
		}
}


