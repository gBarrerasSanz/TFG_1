package common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CommonUtility {
	
	public Date strToDate(String str) {
		final Locale SPAIN_LOCALE = new Locale("es","ES");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss Z", SPAIN_LOCALE);
		try {
			return format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean checkFileNameDate(String grabFileName, Date realDate) {
		final Locale SPAIN_LOCALE = new Locale("es", "ES");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", SPAIN_LOCALE);
		String dateStr = grabFileName.substring(
				grabFileName.indexOf("_")+1, grabFileName.indexOf("."));
		try {
			Date readDate =  formatter.parse(dateStr);
			// SI la diferencia es como mucho 2 minutos
			if (realDate.getTime() - readDate.getTime() <= 2*(60*1000)) {
				return true;
			}
			else { return false; }
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String getDateString() {
		Date date = new Date();
		final Locale SPAIN_LOCALE = new Locale("es", "ES");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", SPAIN_LOCALE);
		return formatter.format(date);
	}
}
