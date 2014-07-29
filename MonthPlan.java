import java.awt.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

import org.jopendocument.dom.spreadsheet.*;

/**
 * This class gets Year Plan object and fills new Month Plan sheet
 * @author Anton Lukashchuk
 *
 */
public class MonthPlan {

	private YearPlan yearPlanObject=null;
	MonthParameters theMonthParameters = null;
		
	private File monthFile=null;
	private SpreadSheet monthSpreadSheet=null;
	private Sheet monthSheet=null;

	
	/**
	 * 
	 * @param yearPlanObject 
	 * @param theMonthParmeters object contains data obtained from user interface
	 */
	public MonthPlan(YearPlan yearPlanObject, MonthParameters theMonthParmeters)
	{	
		this.yearPlanObject=yearPlanObject;
		this.theMonthParameters=theMonthParmeters;
		this.monthFile=new File(yearPlanObject.getPathToFile());
		
		try {
			this.monthSpreadSheet=SpreadSheet.createFromFile(this.monthFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		int size[] = new int[]{100, 100}; //must calculate plan table size later, not store as constant	
		this.monthSheet = monthSpreadSheet.addSheet(this.theMonthParameters.getMonthName());
		this.monthSheet.setColumnCount(size[0]);
		this.monthSheet.setRowCount(size[1]);
		this.saveSpreadSheet();
		
		this.fill(); //fill month plan
		
		
	}//constructor
	
	
	
	/**
	 * Fills new sheet with Month Plan data.
	 * Plan consists of Header - TO data table - Trailer.
	 */
	private void fill()
	{
						
		int currentColumn=1; //first non-empty column
		int currentRow=1;	 //first non-empty row
		int tableTitleRow=0;  //will be set later after writing document header
		final int rowOffSet=2; //offset between table title and table filling	(must be >=2)	
		
		currentRow=this.writeHeader(theMonthParameters.getMonthName(), currentRow);

		//write equipment name column
		this.monthSheet.setValueAt("Наименование оборудования", currentColumn, ++currentRow);
		tableTitleRow=currentRow; //now I can set where the title of my table is
		this.monthSheet.getColumn(currentColumn).setWidth(60);
		this.fillTableColumn(currentColumn, tableTitleRow+rowOffSet, this.yearPlanObject.getEquipmentNameList());
		currentColumn++;
		
		//write serials column
		this.monthSheet.setValueAt("Серийный номер", currentColumn, tableTitleRow);
		this.monthSheet.getColumn(currentColumn).setWidth(35);
		this.fillTableColumn(currentColumn, tableTitleRow+rowOffSet, this.yearPlanObject.getSerialsList());
		currentColumn++;
		
		int placesForBigTOmaxCount=this.yearPlanObject.getEquipmentNameList().size();		
		int[] recommendedPlacesForBigTO = new int[placesForBigTOmaxCount];

		int daysInMonth = theMonthParameters.getDaysInMonth();
		for(int i=1; i<=daysInMonth; i++)
		{
			String dayName = theMonthParameters.getDayOfWeekInMonth(i);
			this.monthSheet.getColumn(currentColumn).setWidth(10);
			this.monthSheet.setValueAt(dayName, currentColumn, tableTitleRow);
			if(dayName.equals(DateUtils.getDayOfWeekInWeek(DateUtils.SATURDAY)) ||
					dayName.equals(DateUtils.getDayOfWeekInWeek(DateUtils.SUNDAY)))
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
				this.fillTableColumn(currentColumn, tableTitleRow+rowOffSet,
						this.yearPlanObject.getWeekPlanMap().get(DateUtils.getDayOfWeekInWeek(5)));
			}
			else if(Arrays.binarySearch(theMonthParameters.getExtraWorkDays(), i+1)>=0)
			{
				this.fillTableColumn(currentColumn, tableTitleRow+rowOffSet,
						this.yearPlanObject.getWeekPlanMap().get(DateUtils.getDayOfWeekInWeek(4)));
			}
			else
			{
				this.fillTableColumn(currentColumn, tableTitleRow+rowOffSet, this.yearPlanObject.getWeekPlanMap().get(dayName));
				if(placesForBigTOmaxCount>0 && !dayName.equals(DateUtils.getDayOfWeekInWeek(6)) &&
						!dayName.equals(DateUtils.getDayOfWeekInWeek(7)))
				{				  
				  recommendedPlacesForBigTO[--placesForBigTOmaxCount]=currentColumn;				  
				}  
			}
			currentColumn++;			
		}
		
		this.setBigTO(this.yearPlanObject.getBigTOforThisMonth(theMonthParameters.getMonthNumber()), recommendedPlacesForBigTO, tableTitleRow+rowOffSet);
		currentRow=tableTitleRow+rowOffSet+this.yearPlanObject.getEquipmentNameList().size();
		this.writeTrailer(currentRow);
		this.saveSpreadSheet();
		System.out.println("Month plan for \""+this.theMonthParameters.getMonthName()+"\" ready");
	}
	

	/**
	 * Places big TO types read from Year Plan 
	 * @param bigTO ArrayList with TO names
	 * @param recommendedPlacesForBigTO array with column numbers for big TO
	 * @param firstRow where to start placing TO
	 */
	private void setBigTO(List<String> bigTO, int[] recommendedPlacesForBigTO, int firstRow)
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
	

	/**
	 * Writes header of the Month plan
	 * @param monthName
	 * @param currentRow
	 * @return number of Row where the header ended
	 */
	private int writeHeader(String monthName, int currentRow)
	{
		
		String year=this.yearPlanObject.getYear();
		
		this.monthSheet.getCellAt(25, currentRow).setStyleName(this.yearPlanObject.getBorderedStyle());

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
	
	/**
	 * Writes trailer of the Month plan
	 * @param currentRow
	 */
	private void writeTrailer(int currentRow)
	{
		currentRow+=3;	
		
		this.monthSheet.setValueAt("Обозначения: ", 5, currentRow);
		currentRow++;
		
		List<String> smallTOname = new LinkedList<String>();
		List<String> smallTOdesc = new LinkedList<String>();
		smallTOname.add("ТО1");
		smallTOdesc.add("Выполняется ежедневно");
		smallTOname.add("ОТО");
		smallTOdesc.add("Выполняется два раза в неделю");
		smallTOname.add("EТО");
		smallTOdesc.add("Выполняется ежедневно");
		this.fillTableColumn(5, currentRow, smallTOname);
		this.fillTableColumn(7, currentRow, smallTOdesc);
		currentRow+=3;

		ArrayList<String> legendNames=this.yearPlanObject.getLegend().get("name");
		ArrayList<String> legendDesc=this.yearPlanObject.getLegend().get("description");
		for(int i=currentRow; i<currentRow+legendNames.size(); i++)
		{
			this.monthSheet.getCellAt(5, i).setBackgroundColor(Color.orange);
		}
		this.fillTableColumn(5, currentRow, legendNames);
		this.fillTableColumn(7, currentRow, legendDesc);
		currentRow+=legendNames.size()+2;

		
		this.monthSheet.setValueAt("Ведущий инженер ", 5, currentRow);


			
	}
	
	/**
	 * Places "filling" in the specified COLUMN starting from startRow
	 * @param COLUMN number of column to fill
	 * @param startRow
	 * @param filling List of data to be placed in COLUMN starting from startRow
	 */
	private void fillTableColumn (final int COLUMN, int startRow, List<String> filling)
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
	
	
	
	/**
	 * Saves changes to *.ods file
	 */
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