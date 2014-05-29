import java.io.*;
import java.util.*;

import org.jopendocument.dom.spreadsheet.*;

public class YearPlan {

	final private String nameOfEquipment = "наименование оборудования";
	final private String nameOfDoc = "график технического обслуживания";
	final private String nameOfWeekPlan = "стандартный недельный план";
	
	private String pathToYearPlan=null;
	private SpreadSheet yearSpreadSheet=null;
	private Sheet yearSheet=null;
	
	//obtained year plan data -----------------------------------------------------------------
	private ArrayList<String> equipmentList = new ArrayList<String>();
	private ArrayList<String> serialsList = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> weekPlanMap = new HashMap<String, ArrayList<String>>();
	//-----------------------------------------------------------------------------------------
	//necessary cell styles--------------------------------------------------------------------
	/*String borderedStyle = null;
	String docTitleStyle = null;*/
	//-----------------------------------------------------------------------------------------

	
	/**
	 * 
	 * @param String path to *.ods file with Year plan 
	 */
	public YearPlan(String pathToYearPlan)
	{
		try
		{
			this.pathToYearPlan=pathToYearPlan;
			this.yearSpreadSheet=SpreadSheet.createFromFile(new File(pathToYearPlan));			
			this.yearSheet = this.yearSpreadSheet.getSheet(0);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(this.checkIfYearPlan())
		{
			System.out.println("Year plan OK");
		}
		else
		{
			System.out.println("Not a valid year plan");
			System.exit(0);
		}
		
		
		this.readEquipmentNameSerials();
		this.readWeekPlan();	
		//this.getStyles();
	}
	
	
	/**
	 * Returns column of TO for requested month
	 * @param month number (1-12) to get TO plan for 
	 * @return column of TO for a concrete month
	 */
	public ArrayList<String> getBigTOforThisMonth(int month)
	{
		//month = 1;
		String[] monthName = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
		int tableXY[]=this.searchCellByText(monthName[month-1], true);
		//System.out.println(monthName[month-1]+" "+Arrays.toString(tableXY));
		
		int column = tableXY[0];
		int row = tableXY[1]+1; //start after the header;
		ArrayList<String> toReturn = new ArrayList<String>();
		for(int i=0; i<this.equipmentList.size(); i++)
		{
			toReturn.add(this.yearSheet.getImmutableCellAt(column, row).getTextValue());
			row++;	
		}	
		//System.out.println(toReturn);
		return toReturn;
	}
	
	
	
	public ArrayList<String> getEquipmentNameList()
	{
		return this.equipmentList;
	}
	
	public ArrayList<String> getEquipmentSerialsList()
	{
		return this.serialsList;
	}	
	
	public HashMap<String, ArrayList<String>> getWeekPlanMap()
	{
		return this.weekPlanMap;
	}
	
	public String getYear()
	{
		int[] XY = this.searchCellByText("График", false);
		String header = this.yearSheet.getImmutableCellAt(XY[0], XY[1]).getTextValue();
		String year = header.substring(header.indexOf("год")-5, header.indexOf("год")-1);
		return year;
	}
	
	
	public String getPathToFile()
	{
		return this.pathToYearPlan;
	}
	
/*	public SpreadSheet getSpreadSheet()
	{
		return this.yearSpreadSheet;
	}*/
	
/*	public File getFile()
	{
		return this.yearPlanFile; 
	}*/
	
	/**
	 * 
	 * @return two ArrayList of Equipment names, and their Serials
	 */
	private void readEquipmentNameSerials()
	{		
		int[] tableXY = this.searchCellByText(this.nameOfEquipment, false);					
		
		int column = tableXY[0];
		int row = tableXY[1]+1; //start after the header;
		
		if(this.yearSheet.getImmutableCellAt(column, row).isEmpty())
		{
			row++;
		}
		
		do
		{		
			this.equipmentList.add(this.yearSheet.getImmutableCellAt(column, row).getTextValue());
			this.serialsList.add(this.yearSheet.getImmutableCellAt(column+1, row).getTextValue());
			row++;	
		}while (!this.yearSheet.getImmutableCellAt(column, row).isEmpty());
				
	}
	
	
	
	
	
	private void readWeekPlan() //call getEquipmentList first
	{
		//String[] weekDays = {"Mon", "Tue", "Wed", "Thu", "Fri"};
		final int workingDays=5;
		int[] tableXY = this.searchCellByText(this.nameOfWeekPlan, false);				
		int column = tableXY[0];
		int row = tableXY[1]+2; //start after the header;
		
		//MyDate theMonth = new MyDate();
		for(int i=0; i<workingDays; i++)
		{
			ArrayList<String> dayTO = new ArrayList<String>();		
			for(int j=0; j<this.equipmentList.size(); j++)
			{
				dayTO.add(this.yearSheet.getImmutableCellAt(column, row).getTextValue());
				row++;	
			}
			column++;
			row=tableXY[1]+2;
			this.weekPlanMap.put(MonthParameters.getDayOfWeekInWeek(i+1), dayTO);
		}
		
		ArrayList<String> emptyDay = new ArrayList<String>();
		for(int i=0; i<workingDays; i++)
		{
			emptyDay.add("");
		}
		
		this.weekPlanMap.put(MonthParameters.getDayOfWeekInWeek(MonthParameters.SATURDAY), emptyDay); //Saturday is empty
		this.weekPlanMap.put(MonthParameters.getDayOfWeekInWeek(MonthParameters.SUNDAY), emptyDay); //Sunday is empty
		
	}
	
	
	/**
	 * Searches a cell coordinates by textValue, case insensitive
	 * @param String to search for
	 * @param boolean strict = true for String.equals, = false for String.contains 
	 * @return int[]{row, column};
	 */
	private int[] searchCellByText(String text, boolean strict)
	{
		int rowCount=this.yearSheet.getRowCount();
		int columnCount=this.yearSheet.getColumnCount();	
		if(rowCount>100){rowCount=100;};
		if(columnCount>100){columnCount=100;};
		int[] toReturn={-1,-1};
			
		for(int x=0; x<columnCount; x++)
		{
			for(int y=0; y<rowCount; y++)
			{
				String cellText = this.yearSheet.getImmutableCellAt(x, y).getTextValue().toLowerCase();
				

				if( (cellText.equals(text.toLowerCase()) & strict)   ||					//#################### check this #######################
				    (cellText.contains(text.toLowerCase()) & !strict)       )
				{
				    toReturn[0]=x;
				    toReturn[1]=y; 
				}
			}
		}
		
		return toReturn;
	}
	

	
/*	private void getStyles()
	{
		int[] XY = this.searchCellByText(this.nameOfDoc, false);
		this.docTitleStyle = this.yearSheet.getStyleAt(XY[0], XY[1]);
		
		XY = this.searchCellByText(this.nameOfWeekPlan, false);
		this.borderedStyle = this.yearSheet.getStyleAt(XY[0], XY[1]);
	}*/
	
	
	/**
	 * Checks if there's a valid yearPlan on the sheet
	 * @return if the *.ods file contains YearPlan
	 */
	private boolean checkIfYearPlan()
	{
		int rowCount=this.yearSheet.getRowCount();
		int columnCount=this.yearSheet.getColumnCount();
		if(rowCount>100){rowCount=100;};
		if(columnCount>100){columnCount=100;};
		
		int hitCount=0;
		for(int x=0; x<columnCount; x++)
		{
			for(int y=0; y<rowCount; y++)
			{
				String cellText = this.yearSheet.getImmutableCellAt(x, y).getTextValue().toLowerCase();
				if(cellText.contains(this.nameOfDoc) ||
				    cellText.contains(this.nameOfEquipment)	 ||
					cellText.contains(this.nameOfWeekPlan))
				{
					hitCount++;
				}
			}
		}
	
		return hitCount==3?true:false;
	}

}//class