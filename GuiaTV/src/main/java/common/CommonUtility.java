package common;

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

import xmltv.datatypes.Event;


public class CommonUtility {
	
	final int msToIntConv = 1000*60*60;
	
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
	
	public Date getFutureRandomDate() {
		final int MAX_HOURS = 60;
		Random rand = new Random();
		Date now = new Date();
		int plusHours = rand.nextInt(MAX_HOURS)+1;
		now.setTime(now.getTime() + plusHours * msToIntConv);
		return now;
	}
	
	public Date sumTwoHours (Date date) {
		Date newDate = new Date();
		newDate.setTime(date.getTime() + 2 * msToIntConv);
		return newDate;
	}
	
	public void printFile (File f) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String sCurrentLine = "";
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
			}
			br.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String lEvtToStr(List<Event> lEvt) {
		StringBuilder sb = new StringBuilder();
		for (Event e: lEvt) {
			sb.append(e.toString()+"\n");
		}
		return sb.toString();
	}
}
