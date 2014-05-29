import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import org.jopendocument.dom.spreadsheet.*;


public class MonthPlan {

	private YearPlan yearPlanObject=null;
	MonthParameters theMonthParameters = null;
		
	private File monthFile=null;
	private SpreadSheet monthSpreadSheet=null;
	private Sheet monthSheet=null;
	
	public MonthPlan(YearPlan yearPlanObject, MonthParameters theMonthParmeters)
	{	
		this.yearPlanObject=yearPlanObject;
		this.theMonthParameters=theMonthParmeters;
		this.monthFile=new File(yearPlanObject.getPathToFile());
		
		int size[] = new int[]{100, 100}; //must calculate plan table size later, not store as constant

		try {
			this.monthSpreadSheet=SpreadSheet.createFromFile(this.monthFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			
		this.monthSheet = monthSpreadSheet.addSheet(this.theMonthParameters.getMonthName());
		this.monthSheet.setColumnCount(size[0]);
		this.monthSheet.setRowCount(size[1]);
		this.saveSpreadSheet();
		
		this.fill(); //fill month plan
		
		
	}//constructor
	
	
	
	
	private void fill()
	{
						
		int currentColumn=1; //first non-empty column
		int currentRow=1;	 //first non-empty row
		int tableTitleRow=0;  //will be set later after writing document header
		final int rowOffSet=2; //offset between table title and table filling	(must be >=2)	
		//MyDate monthInQuestion = new MyDate(this.theMonthParmeters.monthNumber);
		
		currentRow=this.writeHeader(theMonthParameters.getMonthName(), currentRow);

		//write equipment name column
		this.monthSheet.setValueAt("Наименование оборудования", currentColumn, ++currentRow);
		tableTitleRow=currentRow; //now I can set where the title of my table is
		this.monthSheet.getColumn(currentColumn).setWidth(50);
		this.fillTableColumn(currentColumn, tableTitleRow+rowOffSet, this.yearPlanObject.getEquipmentNameList());
		currentColumn++;
		
		//write serials column
		this.monthSheet.setValueAt("Серийный номер", currentColumn, tableTitleRow);
		this.monthSheet.getColumn(currentColumn).setWidth(35);
		this.fillTableColumn(currentColumn, tableTitleRow+rowOffSet, this.yearPlanObject.getEquipmentSerialsList());
		currentColumn++;
		
		int placesForBigTOmaxCount=this.yearPlanObject.getEquipmentNameList().size();		
		int[] recommendedPlacesForBigTO = new int[placesForBigTOmaxCount];

		int daysInMonth = theMonthParameters.getDaysInMonth();
		for(int i=1; i<=daysInMonth; i++)
		{
			String dayName = theMonthParameters.getDayOfWeekInMonth(i);
			this.monthSheet.getColumn(currentColumn).setWidth(10);
			this.monthSheet.setValueAt(dayName, currentColumn, tableTitleRow);
			if(dayName.equals(MonthParameters.getDayOfWeekInWeek(MonthParameters.SATURDAY)) || dayName.equals(MonthParameters.getDayOfWeekInWeek(MonthParameters.SUNDAY)))
			{
				this.monthSheet.getCellAt(currentColumn, tableTitleRow).setBackgroundColor(Color.GREEN);
				this.monthSheet.getColumn(currentColumn).setWidth(7);
			}			
			this.monthSheet.setValueAt(new Integer(i).toString(), currentColumn, tableTitleRow+1);

			if(Arrays.binarySearch(theMonthParameters.getDaysOff(), i)>=0)
			{
				this.monthSheet.getColumn(currentColumn).setWidth(7);
			}
			else if(Arrays.binarySearch(theMonthParameters.getExtraWorkDays(), i)>=0)
			{
				this.fillTableColumn(currentColumn, tableTitleRow+rowOffSet, this.yearPlanObject.getWeekPlanMap().get(MonthParameters.getDayOfWeekInWeek(5)));
			}
			else if(Arrays.binarySearch(theMonthParameters.getExtraWorkDays(), i+1)>=0)
			{
				this.fillTableColumn(currentColumn, tableTitleRow+rowOffSet, this.yearPlanObject.getWeekPlanMap().get(MonthParameters.getDayOfWeekInWeek(4)));
			}
			else
			{
				this.fillTableColumn(currentColumn, tableTitleRow+rowOffSet, this.yearPlanObject.getWeekPlanMap().get(dayName));
				if(placesForBigTOmaxCount>0 && !dayName.equals(MonthParameters.getDayOfWeekInWeek(6)) && !dayName.equals(MonthParameters.getDayOfWeekInWeek(7)))
				{				  
				  recommendedPlacesForBigTO[--placesForBigTOmaxCount]=currentColumn;				  
				}  
			}
			currentColumn++;			
		}
			
		//System.out.println(Arrays.toString(placesForBigTO));
		//return recommendedPlacesForBigTO;
		
		this.setBigTO(this.yearPlanObject.getBigTOforThisMonth(theMonthParameters.getMonthNumber()), recommendedPlacesForBigTO, tableTitleRow+rowOffSet);
		currentRow=tableTitleRow+rowOffSet+this.yearPlanObject.getEquipmentNameList().size();
		this.writeTrailer(currentRow);
		this.saveSpreadSheet();
		System.out.println("Month plan for \""+this.theMonthParameters.getMonthName()+"\" ready");
	}
	

	
	private void setBigTO(ArrayList<String> bigTO, int[] recommendedPlacesForBigTO, int firstRow)
	{
		int[] placesForBigTO = recommendedPlacesForBigTO;
		
		for(int i=1; i<this.yearPlanObject.getEquipmentNameList().size(); i++)
		{
			String currentName=this.yearPlanObject.getEquipmentNameList().get(i).toLowerCase();
			String previousName=this.yearPlanObject.getEquipmentNameList().get(i-1).toLowerCase();
			if(currentName.equals(previousName))
			{
				placesForBigTO[i]=recommendedPlacesForBigTO[i-1];
			}
		}
				
		for(int i=0; i<bigTO.size(); i++)
		{
			if(!bigTO.get(i).equalsIgnoreCase(""))
			{
				this.monthSheet.setValueAt(bigTO.get(i), placesForBigTO[i], i+firstRow);
				
				if(!this.monthSheet.getImmutableCellAt(placesForBigTO[i], i+firstRow).isEmpty())
				{
					this.monthSheet.getCellAt(placesForBigTO[i], i+firstRow).setBackgroundColor(Color.ORANGE);
				}  
			}
		}
		this.saveSpreadSheet();
	}
	

	
	private int writeHeader(String monthName, int currentRow)
	{
		
		String year=this.yearPlanObject.getYear();

		this.monthSheet.setValueAt("УТВЕРЖДАЮ ", 25, currentRow);
		currentRow++;
		
		this.monthSheet.setValueAt("Заместитель директора по СНН ", 25, currentRow);
		currentRow+=2;
		
		this.monthSheet.setValueAt("___________________ С. П. Мышанский ", 25, currentRow);
		currentRow+=2;	
		
		this.monthSheet.setValueAt("\"_______\" _______________ "+year+" г.", 25, currentRow);
		currentRow+=2;
		
		this.monthSheet.setValueAt("План-гарфик технического обслуживания на "+monthName+" "+year+" года.", 5, currentRow);		
		//this.monthSheet.getCellAt(25, currentRow).setStyleName(this.yearPlanObject.);
		this.monthSheet.getCellAt(5, currentRow).merge(15, 1);
		currentRow++;	
		
		return currentRow;
	}
	
	
	private void writeTrailer(int currentRow)
	{
		currentRow+=3;
		this.monthSheet.setValueAt("Ведущий инженер ", 5, currentRow);
			
	}
	
	
	private void fillTableColumn (final int COLUMN, int startRow, ArrayList<String> filling)
	{
		for(int i=0; i<filling.size(); i++)
		{
			this.monthSheet.setValueAt(filling.get(i), COLUMN, startRow);
			startRow++;
		}
	}
	
	
	
/*	private void fillPlanRow (int startColumn, final int ROW, ArrayList<String> filling)
	{
		for(int i=0; i<filling.size(); i++)
		{
			this.monthSheet.setValueAt(filling.get(i), startColumn, ROW);
			startColumn++;
		}
	}*/
	
	
	
	
	private void saveSpreadSheet()
	{
		try
		{
		   this.monthSpreadSheet.saveAs(this.monthFile);
		}
		catch(IOException e)
		{
		   System.out.println("Exception while saving spreadsheet in MonthPlan.java/saveSpreadSheet()");
		   System.exit(0);
		}	
	}
	
	

}