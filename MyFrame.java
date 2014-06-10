import javax.swing.*;
//import org.jdesktop.swingx.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;
import java.awt.event.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;

import org.jopendocument.dom.spreadsheet.SpreadSheet;

@SuppressWarnings("serial")
class MyFrame extends JFrame {
	
	private final int WIDTH = 500;
	private final int HEIGHT = 400;
    private JButton delCreatedButton;
    private JTextField forDaysOff;
    private JTextField forExtraWorkDays;
    private JButton getParamsAndStartButton;
    private JButton fileChooserButton;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JPanel jPanel1;
    private org.jdesktop.swingx.JXMonthView jXMonthView1;
    private JComboBox<String> monthChooser;
    private String pathToYearPlan="";
    private int year=MonthParameters.getCurrentYear()-1900; //JXMonthView needds YYYY-1900 as param
	
	MyFrame()
	{
		setTitle("Программа планирования ТО");
		setSize(this.WIDTH, this.HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		initComponents();
	}
	
	
    private void initComponents() 
    {
        jPanel1 = new JPanel();
        forDaysOff = new JTextField();
        forExtraWorkDays = new JTextField();
        monthChooser = new JComboBox<String>();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jXMonthView1 = new org.jdesktop.swingx.JXMonthView();
        fileChooserButton = new JButton();
        delCreatedButton = new JButton();
        getParamsAndStartButton = new JButton();

        forDaysOff.setEditable(true);
        forDaysOff.setToolTipText("<html>Введите даты праздничных и дополнительных<br>"
        						+ "выходных дней через запятую, например:<br>"
        						+ "1, 2, 3, 4</html>");
        forExtraWorkDays.setEditable(true);
        forExtraWorkDays.setToolTipText("<html>Введите даты дополнительных<br>"
        								+ " рабочих суббот через запятую,<br>"
        								+ " например: 1, 7, 14, 21</html>");
        
		String[] rusMonthes={"Январь", "Февраль", "Март", "Апрель", "Май",
								"Июнь", "Июль", "Август", "Сентябрь", "Октябрь",
								"Ноябрь", "Декабрь"};
        monthChooser.setModel(new javax.swing.DefaultComboBoxModel<String>(rusMonthes));
        monthChooser.setToolTipText("Выберите месяц для которого нужно создать план ТО");
        monthChooser.setSelectedIndex(MonthParameters.getCurrentMonthNumber()-1); //starts with 0
        monthChooser.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		monthChooserAction();
        	}
        });//monthChooser listener
	        

        jLabel1.setText("Путь к годовому плану");
        jLabel2.setText("Необходим план ТО на:");
        jLabel3.setText("Доп. выходные дни");
        jLabel4.setText("Доп. рабочие дни");

        jXMonthView1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		jXMonthView1.setFirstDayOfWeek(2);
        
		fileChooserButton.setText("Найти");
		fileChooserButton.setToolTipText("Нажмите, чтобы выбрать файл годового плана");
		fileChooserButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				fileChooserButtonAction();
			}
		});//fileChooserButton listener
        
        delCreatedButton.setText("Delete All");
        delCreatedButton.setToolTipText("Удалить из файла годового плана все листы кроме первого");
        delCreatedButton.setEnabled(false); //until fileChooser wasn't activated
        delCreatedButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		delCreatedButtonAction();
        	}
        });//delCreatedButton listener

        getParamsAndStartButton.setText("OK");
        getParamsAndStartButton.setToolTipText("<html>Создать план на месяц, план будет добавлен<br>"
        								+ "дополнительным листом в файл годового плана</html>");
        getParamsAndStartButton.setEnabled(false); //until fileChooser wasn't activated
        getParamsAndStartButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		getParamsAndStartButtonAction();
        	}
        });//getParamsAndStartButton listener

        this.setLayout();
        pack();
    }//initComponents

	   	   
    private void delCreatedButtonAction()
    {
	   this.delCreatedButton.setEnabled(false);
	   File theFile=null;
	   SpreadSheet spSheetToClean=null;
		try {
			 theFile=new File(this.pathToYearPlan);
			 spSheetToClean = SpreadSheet.createFromFile(theFile);
		} catch (IOException e) {
			System.out.println("No file found to clean");
			e.printStackTrace();
		}
		   
		int sheetCount=spSheetToClean.getSheetCount();
		
		if(sheetCount>1)
		{
			for(int i=sheetCount-1; i>0; i--)
			{
				spSheetToClean.getSheet(i).detach();
			}
		}
		try {
			spSheetToClean.saveAs(theFile);
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
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("OpenOffice Spreadsheets", "ods");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(getParent());
	    if(returnVal == JFileChooser.APPROVE_OPTION)
	    {
	       this.pathToYearPlan=chooser.getSelectedFile().getAbsolutePath();	
	       YearPlan test = new YearPlan(this.pathToYearPlan);
	       if(!test.checkIfYearPlan())
	       {
	    	   this.jLabel1.setText("Некорректный план   ");
	    	   this.jLabel1.setForeground(Color.RED);
	       }
	       else
	       {       
				if(this.pathToYearPlan.length()>20)
				{
					String t = "..."+this.pathToYearPlan.substring(this.pathToYearPlan.length()-20,
																this.pathToYearPlan.length());
					this.jLabel1.setText(t);
				}
				else
				{
					this.jLabel1.setText(this.pathToYearPlan);
				}
				this.jLabel1.setForeground(Color.BLACK);
				this.delCreatedButton.setEnabled(true);
				this.getParamsAndStartButton.setEnabled(true);
				this.year=Integer.parseInt(test.getYear())-1900;
	       }
   	    }//if(returnVal)
   }//fileChooserButtonAction()
	   
	      
   private void getParamsAndStartButtonAction()
   {
	   
		//----get from UI -----------------------
		int monthPlanFor=monthChooser.getSelectedIndex()+1;
		int[] daysOff=parseIntArray(forDaysOff.getText());
		int[] extraWorkDays = parseIntArray(forExtraWorkDays.getText());
		String path=this.pathToYearPlan;
		//---------------------------------------
		
		MonthParameters theMonthParmeters = new MonthParameters(monthPlanFor, daysOff, extraWorkDays);				
		YearPlan yPlan = new YearPlan(path);				
		new MonthPlan(yPlan, theMonthParmeters);
		
		this.delCreatedButton.setEnabled(true); //now there's a sheet to delete  
   }//getParamsAndStartButtonAction()
	   
   
   private void monthChooserAction()
   {
	   
	   Date toDisplay=new Date (this.year, this.monthChooser.getSelectedIndex(), 1);
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
	   
   
   private void setLayout()
   {
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

   }//setLayout()
   
}//class


