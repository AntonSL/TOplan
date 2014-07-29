import java.util.Arrays;
import java.util.Locale;
import org.joda.time.DateTime;

/**
 * This class allows easy access to month parameters both
 * obtained from user and calculated with jOdaTime library.
 * An object of this class is created for specific user-chosen month
 * (monthNumber field shows which exactly) and represents
 * only parameters of this specific month, it DOES NOT represent current month date.
 * @author Anton Lukashchuk
 *
 */
class MonthParameters {
	
	private int monthNumber=-1; //number of this month 1-12
	private int[] daysOff=null;
	private int[] extraWorkDays = null;

	
	MonthParameters(int monthNumber, int[] daysOff, int[] extraWorkDays)
	{
		this.monthNumber=monthNumber;
		this.daysOff=daysOff;
		this.extraWorkDays=extraWorkDays;
		Arrays.sort(this.daysOff);//in case I use binarySearch later
		Arrays.sort(this.extraWorkDays);//in case I use binarySearch later
	}
	
	/**
	 * 
	 * @return russian name of the month which user chose
	 */
	String getMonthName()
	{
		//month number is 1-12, array index is 0-11
		int monthIndex = monthNumber-1;
		return DateUtils.rusMonthes[monthIndex];
	}
	
	/**
	 * 
	 * @return number of the month for this object 1-12
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
	 * @return how many days in this month
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