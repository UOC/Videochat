package org.uoc.red5.videoconference.utils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.stream.StreamService;
import org.uoc.red5.videoconference.SingletonMeetings;

public class VideoConferenceUtils {

	private static final String SEPARATOR = "_"; 
	private static final String PROPERTIES_PATH = "/org/uoc/red5/videoconference/resources/properties/config.properties";	// properties
	public static String VAR_XAT = "xat.txt";	// name from xat file
	private String num_max_conexion;	// maximum connections permited in rooms
	private static Properties p = new Properties();
	private static final Log logger = LogFactory.getLog(VideoConferenceUtils.class);

	
	/**
	 * Make a directory scope/datetime
	 * @param scope
	 * @return
	 */
/*	public static synchronized String makeVideoConferenceDirectory(String path) { 
		String strDirectory = path + File.separator + generateDirectoryName();
		
		boolean success = FileUtils.makeDirectories(strDirectory);
		
		if (success)
			return strDirectory; 
		else		
			return null;
	}
	
	*//**
	 * Retorna un string amb l'any, mes, dia, hora minuts i segons 
	 * de l'hora actual, concatenas i amb 14 xifres.
	 * @return
	 *//*
	private static String generateDirectoryName() {
		String dataHora = "";
		// create a GregorianCalendar with the Pacific Daylight time zone
		 // and the current date and time
		 Calendar calendar = new GregorianCalendar();
		 Date trialTime = new Date();
		 calendar.setTime(trialTime);

		 // print out a bunch of interesting things
		 String year = String.valueOf(calendar.get(Calendar.YEAR));
		 String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
		 String date = String.valueOf(calendar.get(Calendar.DATE));
		 String hour = String.valueOf(calendar.get(Calendar.HOUR));
		 String minute = String.valueOf(calendar.get(Calendar.MINUTE));
		 String second = String.valueOf(calendar.get(Calendar.SECOND));
		 
		 if (month.length()<2)
			 month = "0"+month;
		 if (date.length()<2)
			 date = "0"+date;

		 //System.out.println("1 --------------------------------------------> " + calendar.get(Calendar.AM_PM));
		 int h = Integer.parseInt(hour);
		 if (calendar.get(Calendar.AM_PM) == Calendar.PM)
			 h = Integer.parseInt(hour) + 12;

		 //System.out.println("h --------------------------------------------> " + h);
		 //System.out.println("hour --------------------------------------------> " + hour);

		 hour = String.valueOf(h);
		 if (hour.length()<2)
			hour = "0"+hour;
		 if (minute.length()<2)
			minute = "0"+minute;
		 if (second.length()<2)
			second = "0"+second;			 
		 
		 dataHora = year + SEPARATOR + month + SEPARATOR + date + SEPARATOR + hour + minute + second;
		 return dataHora;
		 
	}

	*//**
	 * Calculate the actual time, and return a string with pattern xx:yy:zz
	 * @return
	 *//*
	public static String getActualTime() {

		 Calendar calendar = new GregorianCalendar();
		 Date trialTime = new Date();
		 calendar.setTime(trialTime);		
		 String hour = String.valueOf(calendar.get(Calendar.HOUR));
		 String minute = String.valueOf(calendar.get(Calendar.MINUTE));
		 String second = String.valueOf(calendar.get(Calendar.SECOND));

		 //System.out.println("1 --------------------------------------------> " + calendar.get(Calendar.AM_PM));
		 int h = Integer.parseInt(hour);
		 if (calendar.get(Calendar.AM_PM) == Calendar.PM){ 
			 h = Integer.parseInt(hour) + 12;
		 }
		 
		 hour = String.valueOf(h);

		 if (hour.length()<2)
			hour = "0"+hour;
		 if (minute.length()<2)
			minute = "0"+minute;
		 if (second.length()<2)
			second = "0"+second;			 
		 		 
		 return hour + ":" + minute + ":" + second;
	}

	*//**
	 * Calculate the actual date, and return a string with pattern 01/01/2009
	 * @return
	 *//*
	public static String getActualDate() {

		 Calendar calendar = new GregorianCalendar();
		 Date trialTime = new Date();
		 calendar.setTime(trialTime);		
		 String year = String.valueOf(calendar.get(Calendar.YEAR));
		 String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
		 String date = String.valueOf(calendar.get(Calendar.DATE));

		 if (month.length()<2)
			 month = "0"+month;
		 if (date.length()<2)
			 date = "0"+date;

		 return date + "/" + month + "/" + year;
	}

	*//**
	 * Convert a date of type 2008_12_01_121503 to 01/12/2008 12:15:03
	 * @param name
	 * @return
	 *//*
	public static String[] dateTime(String name) {
		
		StringTokenizer st = new StringTokenizer(name, "_");
		String data = "";
		String time = "";
		int comptador = 0;
		String resultat[] = {"",""}; 
		
		while (st.hasMoreElements()) {
			String token = (String)st.nextElement();
			if (comptador > 0 && comptador < 3) 
				data = token + "/" + data; 
			else if (comptador == 0)		
				data = token + data;
			else if (comptador ==3)
				time = token.substring(0, 2) + ":" + token.substring(2, 4) + ":" + token.substring(4, 6);
			comptador++;
		}
		
		resultat[0] = data;
		resultat[1] = time;
		
		return resultat;
	}
	
	public static String getProperty(String key){
		try {
			loadProperties();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p.getProperty(key);
	}

	private static void loadProperties () throws Exception{
		p.load(VideoConferenceUtils.class.getResourceAsStream(PROPERTIES_PATH));
	}

	
	*//**
	 * Return a name from scope
	 * @return
	 *//*
    public static String getMeetingId(IConnection ic) {
    	logger.info("getMeetingId -------------------------------> " + ic.getScope().getName());
		return ic.getScope().getName();
    }
*/}
