import java.io.*;
import java.util.*;

import org.jopendocument.dom.spreadsheet.*;

/**
 * This class gets path to Year plan file, checks if there's a valid plan
 * in this file and reads all data from it.
 * @author Lukashchuk Anton
 *
 */
public class YearPlan {

	final private String nameOfEquipment = "наименование оборудования";
	final private String nameOfDoc = "график технического обслуживания";
	final private String nameOfWeekPlan = "стандартный недельный план";
	final private String legend = "обозначения";
	
	private String pathToYearPlan=null;
	private SpreadSheet yearSpreadSheet=null;
	private Sheet yearSheet=null;
	
	//obtained year plan data -----------------------------------------------------------------
	private List<String> equipmentList = new ArrayList<String>();
	private List<String> serialsList = new ArrayList<String>();
	private Map<String, ArrayList<String>> weekPlanMap = new HashMap<String, ArrayList<String>>();
	private Map<String, ArrayList<String>> legendMap = new HashMap<String, ArrayList<String>>();
	//-----------------------------------------------------------------------------------------
	//necessary cell styles--------------------------------------------------------------------
	private String cellStyle = null;
	private String headerStyle = null;
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
			this.readEquipmentNameSerials();
			this.readWeekPlan();
			this.readLegend();
		}
		else
		{
			System.out.println("Not a valid year plan");
			//System.exit(0);
		}
	}
	
	
	/**
	 * Returns column of big TO for requested month
	 * @param month number (1-12) to get TO plan for 
	 * @return column of TO for a concrete month
	 */
	public List<String> getBigTOforThisMonth(int month)
	{
		String[] monthName = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
		Pair coordinates=this.searchCellByText(monthName[month-1], true);
		//System.out.println(monthName[month-1]+" "+Arrays.toString(tableXY));
		
		int column = coordinates.getX();
		int row = coordinates.getY()+1; //start after the header;
		List<String> toReturn = new ArrayList<String>();
		for(int i=0; i<this.equipmentList.size(); i++)
		{
			toReturn.add(this.yearSheet.getImmutableCellAt(column, row).getTextValue());
			row++;	
		}	
		//System.out.println(toReturn);
		return toReturn;
	}
	
	
	/**
	 * Gets the column with equipment list
	 * @return ArrayList of names
	 */
	public List<String> getEquipmentNameList()
	{
		return this.equipmentList;
	}
	
	/**
	 * Gets serial numbers
	 * @return ArrayList of serials stored as strings
	 */
	public List<String> getSerialsList()
	{
		return this.serialsList;
	}	
	
	/**
	 * Gets standard TO plan for a week
	 * @return Map where key is day name, value is ArrayList of TO names
	 */
	public Map<String, ArrayList<String>> getWeekPlanMap()
	{
		return this.weekPlanMap;
	}
	
	/**
	 * Gets TO names with description
	 * @return
	 */
	public Map<String, ArrayList<String>> getLegend()
	{
		return this.legendMap;
	}
	
	/**
	 * 
	 * @return year specified in plan header
	 */
	public String getYear()
	{
		Pair coordinates = this.searchCellByText("График", false);
		String header = this.yearSheet.getImmutableCellAt(coordinates.getX(), coordinates.getY()).getTextValue();
		String year = header.substring(header.indexOf("год")-5, header.indexOf("год")-1);
		//this.headerStyle=this.yearSheet.getCellAt(coordinates.getX(), coordinates.getY()).getStyleName();
		return year;
	}
	
	/**
	 * 
	 * @return path to Year plan file
	 */
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
	 * Checks if there's a valid yearPlan on the sheet
	 * @return if the *.ods file contains YearPlan
	 */
	public boolean checkIfYearPlan()
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
	
	public String getHeaderStyle()
	{
		return this.headerStyle;
	}
	
	public String getBorderedStyle()
	{
		return this.cellStyle;
	}
	
	/**
	 * 
	 * @return two ArrayList of Equipment names, and their Serials
	 */
	private void readEquipmentNameSerials()
	{		
		Pair coordinates = this.searchCellByText(this.nameOfEquipment, false);					
		
		int column = coordinates.getX();
		int row = coordinates.getY()+1; //start after the header;
		
		this.cellStyle=this.yearSheet.getCellAt(column, row+2).getStyleName();
		
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
	
	
	
	
	/**
	 * fills Map where key is day name, value is ArrayList of TO names
	 */
	private void readWeekPlan() //call getEquipmentList first
	{
		//String[] weekDays = {"Mon", "Tue", "Wed", "Thu", "Fri"};
		final int workingDays=5;
		Pair coordinates = this.searchCellByText(this.nameOfWeekPlan, false);				
		int column = coordinates.getX();
		int row = coordinates.getY()+2; //start after the header;
		
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
			row=coordinates.getY()+2;
			this.weekPlanMap.put(DateUtils.getDayOfWeekInWeek(i+1), dayTO);
		}
		
		ArrayList<String> emptyDay = new ArrayList<String>();
		for(int i=0; i<workingDays; i++)
		{
			emptyDay.add("");
		}
		
		this.weekPlanMap.put(DateUtils.getDayOfWeekInWeek(DateUtils.SATURDAY), emptyDay); //Saturday is empty
		this.weekPlanMap.put(DateUtils.getDayOfWeekInWeek(DateUtils.SUNDAY), emptyDay); //Sunday is empty
		
	}
	
	
	
	/**
	 * Reads TO names and their description
	 */
	private void readLegend()
	{
		Pair legendStart = searchCellByText(this.legend, false);
		int column = legendStart.getX();
		int row = legendStart.getY()+1; //skip the name of a legend
		ArrayList<String> TOname = new ArrayList<String>();
		ArrayList<String> TOdesc = new ArrayList<String>();

		while(!this.yearSheet.getCellAt(column, row).isEmpty())
		{
			TOname.add(this.yearSheet.getCellAt(column, row).getTextValue());
			TOdesc.add(this.yearSheet.getCellAt(column+2, row).getTextValue());
			row++;
		}
		
		
		this.legendMap.put("name", TOname);
		this.legendMap.put("description", TOdesc);
	}
	
	
	
	
	/**
	 * Searches a cell coordinates by textValue, case insensitive
	 * @param String to search for
	 * @param boolean strict = true for String.equals, = false for String.contains 
	 * @return int[]{row, column};
	 */
	private Pair searchCellByText(String text, boolean strict)
	{
		int rowCount=this.yearSheet.getRowCount();
		int columnCount=this.yearSheet.getColumnCount();	
		if(rowCount>100){rowCount=100;};
		if(columnCount>100){columnCount=100;};
		Pair toReturn=new Pair();
			
		for(int x=0; x<columnCount; x++)
		{
			for(int y=0; y<rowCount; y++)
			{
				String cellText = this.yearSheet.getImmutableCellAt(x, y).getTextValue().toLowerCase();
				

				if( (cellText.equals(text.toLowerCase()) & strict)   ||					//#################### check this #######################
				    (cellText.contains(text.toLowerCase()) & !strict)       )
				{
				    toReturn.setX(x);
				    toReturn.setY(y); 
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
	
	

}//class