package guiatv.common;

import guiatv.persistence.domain.Event_old;
import guiatv.persistence.domain.Schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.log4j.Logger;


public class CommonUtility {
	
	private static final Logger log = Logger.getLogger(CommonUtility.class);
	
	final static int msToSecsConv = 1000;
	final static int msToMinsConv = 1000*60;
	final static int msToHoursConv = 1000*60*60;
	
	private static enum TimeUnit {
		SEC, MIN, HOUR
	}
	
	
	
	public static Date strToDate(String str) {
		final Locale SPAIN_LOCALE = new Locale("es","ES");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss Z", SPAIN_LOCALE);
		try {
			return format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Timestamp strToTimestamp(String str) {
		return new Timestamp(strToDate(str).getTime()); 
	}
	
	public static boolean checkFileNameDate(String grabFileName, Date realDate) {
		final Locale SPAIN_LOCALE = new Locale("es", "ES");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm", SPAIN_LOCALE);
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
	
	public static String getDateString() {
		Date date = new Date();
		final Locale SPAIN_LOCALE = new Locale("es", "ES");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm", SPAIN_LOCALE);
		return formatter.format(date);
	}
	
	public static Date getFutureRandomDate(TimeUnit unit, int minTimeUnits, int maxTimeUnits) {
		Random rand = new Random();
		Date now = new Date();
		int plusUnits = rand.nextInt(maxTimeUnits)+minTimeUnits;
		int plusTime = 0;
		switch(unit){
		case SEC:
			plusTime = plusUnits * msToSecsConv;
			break;
		case MIN:
			plusTime = plusUnits * msToSecsConv;
			break;
		case HOUR:
			plusTime = plusUnits * msToSecsConv;
			break;
		default:
			log.error("Invalidad TimeUnit");
			break;
		}
		now.setTime(now.getTime() + plusTime);
		return now;
	}
	
	public static Date sumTwoHours (Date date) {
		Date newDate = new Date();
		newDate.setTime(date.getTime() + 2 * msToHoursConv);
		return newDate;
	}
	
	public static String getFileString (File f) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String sCurrentLine = "";
			while ((sCurrentLine = br.readLine()) != null) {
				sb.append(sCurrentLine+"\n");
			}
			br.close();
			return sb.toString();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String lEvtToStr(List<Event_old> lEvt) {
		StringBuilder sb = new StringBuilder();
		for (Event_old e: lEvt) {
			sb.append(e.toString()+"\n");
		}
		return sb.toString();
	}
	
	public static TimeUnit getTimeUnit(String str){
		TimeUnit unit = null;
		try {
			unit = TimeUnit.valueOf(str.toUpperCase());
		}
		catch(IllegalArgumentException e){
			e.printStackTrace();
		}
		return unit;
	}
	/**
	 * Si el final del schedule es posterior al momento actual
	 * -> Entonces devolver TRUE (a tiempo)
	 */
	public static boolean isScheduleOnTime(Schedule sched) {
		return sched.getEnd().after((new Timestamp(new Date().getTime())));
	}
	
}
