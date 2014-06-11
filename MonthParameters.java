import java.util.Arrays;
import java.util.Locale;
import org.joda.time.DateTime;

/**
 * This class allows easy access to month parameters both
 * obtained from user and calculated with jOdaTime lib
 * @author Anton Lukashchuk
 *
 */
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
	
	/**
	 * 
	 * @param dayInWeek number of day in week 1-7
	 * @return short name of this day in rus locale Пн.-Пт.
	 */
	static String getDayOfWeekInWeek(int dayInWeek)
	{		
		DateTime aDate = new DateTime().withDayOfWeek(dayInWeek);
		return aDate.dayOfWeek().getAsShortText(new Locale("ru"));	
		
	}
	
	/**
	 * 
	 * @return number of current month 1-12
	 */
	static int getCurrentMonthNumber()
	{
		DateTime aDate = new DateTime();
		return aDate.getMonthOfYear();
	}
	
	/**
	 * 
	 * @return current year
	 */
	static int getCurrentYear()
	{
		DateTime aDate = new DateTime();
		return aDate.getYear();
	}
	
	/**
	 * 
	 * @return russian name of the month which user chose
	 */
	String getMonthName()
	{
		return rusMonthes[monthNumber-1];
	}
	
	/**
	 * 
	 * @return number of the month which user chose 1-12
	 */
	int getMonthNumber()
	{
		return this.monthNumber;
	}
	
	/**
	 * 
	 * @return array of day numbers to which are not working due to holidays etc.
	 */
	int[] getDaysOff()
	{
		return this.daysOff;
	}
	
	/**
	 * additional work days
	 * @return array of additional work days numbers
	 */
	int[] getExtraWorkDays()
	{
		return this.extraWorkDays;
	}
	
	/**
	 * 
	 * @return how many days in user-chosen month
	 */
	int getDaysInMonth()
	{
		DateTime aDate = new DateTime(2014, monthNumber, 1, 1, 1);	
		return aDate.dayOfMonth().getMaximumValue();
	}
	
	/**
	 * 
	 * @param dayInMonth 1-31
	 * @return short name of this day in week in russian Пн.-Пт.
	 */
	String getDayOfWeekInMonth(int dayInMonth)
	{
		DateTime aDate = new DateTime(2014, this.monthNumber, dayInMonth, 1, 1);
		return aDate.dayOfWeek().getAsShortText(new Locale("ru"));
	}
	
	

}