package dicemc.gnc.testmaterial;

import java.time.LocalDate;
import java.time.Month;

public class HolidayFun {
	public enum Holiday {APRIL_FOOLS, CHRISTMAS, HALLOWEEN, NONE}	
	public static Holiday getCurrentHoliday() {
		LocalDate date = LocalDate.now();
		
		if (date.compareTo(LocalDate.of(date.getYear(), Month.APRIL, 1)) == 0) return Holiday.APRIL_FOOLS;
		return  Holiday.NONE;
	}
}
