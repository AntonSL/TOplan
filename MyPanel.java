//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.awt.event.*;

//import org.jdesktop.swingx.JXDatePicker;
//import org.jdesktop.swingx.JXMonthView;
import javax.swing.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
class MyPanel extends JPanel {
	
	
	
	MyPanel()
	{
		//this.setLayout(new BorderLayout());
		
		JButton delCreatedButton = new JButton();
		delCreatedButton.setText("delCreated");
		delCreatedButton.addActionListener(new SheetDeleter("YP2014_2.ods"));
		delCreatedButton.addActionListener(new ButtonDisabler(delCreatedButton, false));
		this.add(delCreatedButton);
		
		String[] rusMonthes={"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
		JComboBox<String> monthChooser = new JComboBox<String>(rusMonthes);
		this.add(monthChooser);
		
		JTextField pathTOYearPlan = new JTextField(10);
		pathTOYearPlan.setEditable(true);
		pathTOYearPlan.setText("YP2014_2.ods");
		this.add(pathTOYearPlan);		
		
		JTextField forDaysOff = new JTextField(10);
		forDaysOff.setEditable(true);
		this.add(forDaysOff);
		
		JTextField forExtraWorkDays = new JTextField(10);
		forDaysOff.setEditable(true);
		this.add(forExtraWorkDays);
		
		JButton getParamsAndStartButton = new JButton();
		getParamsAndStartButton.setText("getParams");
		getParamsAndStartButton.addActionListener(new paramsGetter(monthChooser, pathTOYearPlan,
																	forDaysOff, forExtraWorkDays));
		getParamsAndStartButton.addActionListener(new ButtonDisabler(delCreatedButton, true));
		this.add(getParamsAndStartButton);
		
		
		
		
	}
	
	
	class paramsGetter implements ActionListener
	{
		JComboBox<String> monthChooser = null;
		JTextField path=null;
		JTextField off=null;
		JTextField extra=null;
		
		paramsGetter(JComboBox<String> JC, JTextField path, JTextField off, JTextField extra)
		{
			this.monthChooser = JC;
			this.path=path;
			this.off=off;
			this.extra=extra;
		}
		
		public void actionPerformed(ActionEvent e)
		{
/*			System.out.println("Month N "+this.monthChooser.getSelectedIndex()+" choosen");
			System.out.println(off.getText() +" "+extra.getText());*/
			//----get from UI later------------------
			int monthPlanFor=monthChooser.getSelectedIndex()+1;
			int[] daysOff=this.parseIntArray(off.getText());
			int[] extraWorkDays = this.parseIntArray(extra.getText());
			String pathToYearPlan=path.getText();
			//---------------------------------------
			MonthParameters theMonthParmeters = new MonthParameters(monthPlanFor, daysOff, extraWorkDays);		
			
			YearPlan yPlan = new YearPlan(pathToYearPlan);				
			new MonthPlan(yPlan, theMonthParmeters);
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

}
