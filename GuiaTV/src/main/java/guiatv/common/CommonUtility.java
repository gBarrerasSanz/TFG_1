package guiatv.common;

import guiatv.persistence.EventServiceTests;
import guiatv.xmltv.datatypes.Event;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.log4j.Logger;


public class CommonUtility {
	
	private static final Logger log = Logger.getLogger(EventServiceTests.class);
	
	final int msToSecsConv = 1000;
	final int msToMinsConv = 1000*60;
	final int msToHoursConv = 1000*60*60;
	
	private enum TimeUnit {
		SEC, MIN, HOUR
	}
	
	
	
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
	
	public Date getFutureRandomDate(TimeUnit unit, int minTimeUnits, int maxTimeUnits) {
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
	
	public Date sumTwoHours (Date date) {
		Date newDate = new Date();
		newDate.setTime(date.getTime() + 2 * msToHoursConv);
		return newDate;
	}
	
	public String getFileString (File f) {
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
	
	public String lEvtToStr(List<Event> lEvt) {
		StringBuilder sb = new StringBuilder();
		for (Event e: lEvt) {
			sb.append(e.toString()+"\n");
		}
		return sb.toString();
	}
	
	public TimeUnit getTimeUnit(String str){
		TimeUnit unit = null;
		try {
			unit = TimeUnit.valueOf(str.toUpperCase());
		}
		catch(IllegalArgumentException e){
			e.printStackTrace();
		}
		return unit;
	}
	
}
