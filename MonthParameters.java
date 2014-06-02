import java.util.Arrays;
import java.util.Locale;
import org.joda.time.DateTime;
//import java.util.*;


class MonthParameters {

	static final int MONDAY = 1;
	static final int TUESDAY = 2;
	static final int WEDNESDAY = 3;
	static final int THURSDAY = 4;
	static final int FRIDAY = 5;
	static final int SATURDAY = 6;
	static final int SUNDAY = 7;
	
	private final String[] rusMonthes={"Январь", "Февраль", "Март", "Апрель",
						"Май", "Июнь", "Июль", "Август", "Сентябрь",
						"Октябрь", "Ноябрь", "Декабрь"};
	private int monthNumber=-1;
	private int[] daysOff=null;
	private int[] extraWorkDays = null;
	
	MonthParameters(int monthNum, int[] daysOff, int[] extraWorkDays)
	{
		monthNumber=monthNum;
		this.daysOff=daysOff;
		this.extraWorkDays=extraWorkDays;
		Arrays.sort(this.daysOff);//in case I use binarySearch later
		Arrays.sort(this.extraWorkDays);//in case I use binarySearch later
	}
	
	
	static String getDayOfWeekInWeek(int dayInWeek)
	{		
		DateTime aDate = new DateTime().withDayOfWeek(dayInWeek);
		return aDate.dayOfWeek().getAsShortText(new Locale("ru"));	
		
	}
	
	static int getCurrentMonthNumber()
	{
		DateTime aDate = new DateTime();
		return aDate.getMonthOfYear();
	}
	
	static int getCurrentYear()
	{
		DateTime aDate = new DateTime();
		return aDate.getYear();
	}
	
	
	String getMonthName()
	{
		return rusMonthes[monthNumber-1];
	}
	
	int getMonthNumber()
	{
		return this.monthNumber;
	}
	
	int[] getDaysOff()
	{
		return this.daysOff;
	}
	
	int[] getExtraWorkDays()
	{
		return this.extraWorkDays;
	}
	
	
	int getDaysInMonth()
	{
		DateTime aDate = new DateTime(2014, monthNumber, 1, 1, 1);	
		return aDate.dayOfMonth().getMaximumValue();
	}
	
	
	String getDayOfWeekInMonth(int dayInMonth)
	{
		DateTime aDate = new DateTime(2014, this.monthNumber, dayInMonth, 1, 1);
		return aDate.dayOfWeek().getAsShortText(new Locale("ru"));
	}
	
	

}