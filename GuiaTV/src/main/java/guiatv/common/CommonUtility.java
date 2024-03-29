package guiatv.common;

import guiatv.persistence.domain.Schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.google.common.hash.Hashing;


public class CommonUtility {
	
	private static final Logger log = Logger.getLogger(CommonUtility.class);
	
	final static int msToSecsConv = 1000;
	final static int msToMinsConv = 1000*60;
	final static int msToHoursConv = 1000*60*60;
	
	final static String SRC_RESOURCES_URI = "src/main/resources/";
	
	private static enum TimeUnit {
		SEC, MIN, HOUR
	}
	
	public static final String zonelessDateFormat = "yyyy-MM-dd HH:mm:ss";
	public static final String zonedDateFormat = "yyyy-MM-dd-HH:mm:ss, z";
	private static final Locale SPAIN_LOCALE = new Locale("es","ES");
	private static final SimpleDateFormat spainSdf = new SimpleDateFormat(zonedDateFormat, SPAIN_LOCALE);
	private static final SimpleDateFormat zonedGmtSdf = new SimpleDateFormat(zonedDateFormat);
	private static final SimpleDateFormat zonelessGmtSdf = new SimpleDateFormat(zonelessDateFormat);
	
	public static Date xmltvFormatToDate(String str) {
		try {
			SimpleDateFormat spainSdf = new SimpleDateFormat("yyyyMMddHHmmss Z");
			Date dateFromSpain = spainSdf.parse(str);
//			String dateFromSpainStr = spainSdf.format(dateFromSpain);
			
			SimpleDateFormat utcSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			TimeZone utcZone = TimeZone.getTimeZone("UTC");
			utcSdf.setTimeZone(utcZone);
			String utcDateStr = utcSdf.format(dateFromSpain);
			Date utcDate = utcSdf.parse(utcDateStr);
			return utcDate;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String localTimestampToGmtStr(Timestamp ts) {
		SimpleDateFormat utcSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss, z");
		TimeZone utcZone = TimeZone.getTimeZone("UTC");
		utcSdf.setTimeZone(utcZone);
		String utcDateStr = utcSdf.format(ts.getTime());
		return utcDateStr;
//			Date utcDate = utcSdf.parse(utcDateStr);
//			return utcDate;
	}
	
	public static String dateToStr(Date date) {
		return zonedGmtSdf.format(date);
	}
	
	public static boolean validateURL(String stringUrl) {
		try {
			/*
			 * Si empieza por rtmp:// -> Validarlo directamente. 
			 * Si no, la creaci�n de URL lanzar� excepci�n de protocolo desconocido rtmp
			 */
			if (stringUrl.startsWith("rtmp://")) {
				return true;
			}
			new URL(stringUrl);
//		    new URL(stringUrl.split("[\\t\\n\\r ]+")[0]);
		    return true;
		} catch (MalformedURLException e) {
		    // the URL is not in a valid form
			
			return false;
		}
	}
	
	public static String dateToPlainStr(Date date) {
		return zonedGmtSdf.format(date);
	}
	
//	public static Date computeDateFromZonedDate(Date zonedDate) {
//		String newDateStr = zonedSdf.format(zonedDate);
//		Date newDate = null;
//		try {
//			newDate = zonedSdf.parse(newDateStr);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return newDate;
//	}
	
	public static Date utcDateStrToUtcDate(String strDate) {
		try {
			return zonedGmtSdf.parse(strDate);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String timestampToString(Timestamp timestamp) {
		/** IMPORTANTE: FORMATO DE FECHA */
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a z");
		return df.format(new Date(timestamp.getTime()));
	}
	
	public static Timestamp strToTimestamp(String str) {
		return new Timestamp(xmltvFormatToDate(str).getTime()); 
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
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", SPAIN_LOCALE);
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
	
//	public static String lEvtToStr(List<Event_old> lEvt) {
//		StringBuilder sb = new StringBuilder();
//		for (Event_old e: lEvt) {
//			sb.append(e.toString()+"\n");
//		}
//		return sb.toString();
//	}
	
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
	
	/**
	 * Si el comienzo del schedule es anterior al momento actual
	 * -> Entonces devolver TRUE (ha empezado)
	 */
	public static boolean isScheduleStarted(Schedule sched) {
		return sched.getStart().before((new Timestamp(new Date().getTime())));
	}
	
	/**
	 * Si el comienzo del schedule es anterior al momento actual (isScheduleStarted() == TRUE)
	 * y el final del schedule es posterior al momento actual (isScheduleOnTime() == TRUE)
	 * -> Entonces devolver TRUE (est� siendo emitido)
	 * 
	 */
	public static boolean isScheduleBeingEmitedNow(Schedule sched) {
		return isScheduleStarted(sched) 		// ha empezado
				&& isScheduleOnTime(sched);		// todav�a NO ha terminado
	}
	
	public static File getFileFromClassPathUri(String uri) {
		Resource resource = new ClassPathResource(uri);
		try {
			return resource.getFile();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static File getFileFromRelativeUri(String relUri) {
		File file = new File(SRC_RESOURCES_URI+File.separator+relUri);
		return file;
	}
	
	public static void createFileFromClassPathUriIfDoesNotExists(String relUri) {
		try {
			File file = new File(SRC_RESOURCES_URI+File.separator+relUri);
			if ( ! file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String hash(String strIn) {
		return Hashing.murmur3_32().hashString(strIn, StandardCharsets.UTF_8).toString();
	}
	
}
