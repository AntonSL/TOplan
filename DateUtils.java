import java.util.Locale;

import org.joda.time.DateTime;

/**
 * This class contains only static fields and methods
 * @author Anton Lukashchuk
 *
 */
public class DateUtils {
	
	static final int MONDAY = 1;
	static final int TUESDAY = 2;
	static final int WEDNESDAY = 3;
	static final int THURSDAY = 4;
	static final int FRIDAY = 5;
	static final int SATURDAY = 6;
	static final int SUNDAY = 7;
	
	static final String[] rusMonthes={"Январь", "Февраль", "Март", "Апрель",
			"Май", "Июнь", "Июль", "Август", "Сентябрь",
			"Октябрь", "Ноябрь", "Декабрь"};
	
	/**
	 * 
	 * @param dayInWeek - number of day in week 1 - 7
	 * @return short name of this day in rus locale Пн.- Пт.
	 */
	static String getDayOfWeekInWeek(int dayInWeek)
	{		
		DateTime aDate = new DateTime().withDayOfWeek(dayInWeek);
		return aDate.dayOfWeek().getAsShortText(new Locale("ru"));	
		
	}
	
	/**
	 * 
	 * @return current month number 1-12
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

}
