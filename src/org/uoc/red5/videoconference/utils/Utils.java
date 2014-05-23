package org.uoc.red5.videoconference.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.server.api.IConnection;
import org.uoc.red5.videoconference.SingletonMeetings;
import org.uoc.red5.videoconference.User;
import org.uoc.red5.videoconference.VideoConferenceMetadata;
import org.uoc.red5.videoconference.dao.DAOFactory;
import org.uoc.red5.videoconference.dao.LTIApplicationDAO;
import org.uoc.red5.videoconference.dao.MeetingDAO;
import org.uoc.red5.videoconference.dao.MeetingDAOException;
import org.uoc.red5.videoconference.dao.MeetingLiveDAO;
import org.uoc.red5.videoconference.dao.RemoteApplicationDAO;
import edu.uoc.lti.LTIApplication;
import org.uoc.red5.videoconference.pojo.Meeting;
import org.uoc.red5.videoconference.pojo.MeetingLive;
import org.uoc.red5.videoconference.pojo.RemoteApplication;

public final class Utils {

	protected static Log log = LogFactory.getLog(Utils.class.getName());
	private static final String SEPARATOR = "_"; 
	private static final String PROPERTIES_PATH = "/org/uoc/red5/videoconference/resources/properties/config.properties";	// properties
	public static String VAR_XAT = "xat.txt";	// name from xat file
	private static Properties p = new Properties();

	
	/**
	 * Make a directory scope/datetime
	 * @param scope
	 * @return
	 */
	public static synchronized String makeVideoConferenceDirectory(String path) { 
		String strDirectory = path + File.separator + generateDirectoryName();
		
		boolean success = FileUtils.makeDirectories(strDirectory);
		
		if (success)
			return strDirectory; 
		else		
			return null;
	}
	
	public static String formatDate(Date date) {
		SimpleDateFormat formatter;
		String pattern = "dd/MM/yy H:mm:ss";
		Locale locale = new Locale("en", "US");
		formatter = new SimpleDateFormat(pattern, locale);
		return formatter.format(date);
	}

	
	/**
	 * Retorna un string amb l'any, mes, dia, hora minuts i segons 
	 * de l'hora actual, concatenas i amb 14 xifres.
	 * @return
	 */
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

		 int h = Integer.parseInt(hour);
		 if (calendar.get(Calendar.AM_PM) == Calendar.PM)
			 h = Integer.parseInt(hour) + 12;

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

	/**
	 * Calculate the actual time, and return a string with pattern xx:yy:zz
	 * @return
	 */
	public static String getActualTime() {

		 Calendar calendar = new GregorianCalendar();
		 Date trialTime = new Date();
		 calendar.setTime(trialTime);		
		 String hour = String.valueOf(calendar.get(Calendar.HOUR));
		 String minute = String.valueOf(calendar.get(Calendar.MINUTE));
		 String second = String.valueOf(calendar.get(Calendar.SECOND));

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

	/**
	 * Calculate the actual date, and return a string with pattern 01/01/2009
	 * @return
	 */
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

	/**
	 * Convert a date of type 2008_12_01_121503 to 01/12/2008 12:15:03
	 * @param name
	 * @return
	 */
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

	
	/**
	 * Return a name from scope
	 * @return
	 */
    public static String getMeetingId(IConnection ic) {
		return ic.getScope().getName();
    }
	
	/**
	 * Separa els participants.
	 * @param participants_
	 * @return
	 */
	public static final String[] getParticipants(String participants_) {
		
		StringTokenizer st = new StringTokenizer(participants_);
		int numParaules = st.countTokens();
		int index = 0;
		String[] participants = new String[numParaules];
		while (st.hasMoreTokens()) {
	    	String participant = st.nextToken();
	    	participants[index] = participant;
	    	index++;
	    }
		return participants;
	}
	
	
	/**
	 * Genera un numero aleatori de 8 xifres
	 * de 00000000 a 99999999
	 * @return
	 */
	public static final String getMeetingId() {
		
		String vnumero = "";
		
		Random randomGenerator = new Random();
		int randomInt1 = randomGenerator.nextInt(9);
		int randomInt2 = randomGenerator.nextInt(9);
		int randomInt3 = randomGenerator.nextInt(9);
		int randomInt4 = randomGenerator.nextInt(9);
		int randomInt5 = randomGenerator.nextInt(9);
		int randomInt6 = randomGenerator.nextInt(9);
		int randomInt7 = randomGenerator.nextInt(9);
		int randomInt8 = randomGenerator.nextInt(9);

		// monta un string de 8 caracters
		vnumero = "" + randomInt1 + "" + randomInt2 + "" + randomInt3 + "" +
			"" + randomInt4 + "" + randomInt5 + "" + randomInt6 + "" +
			randomInt7 + "" + randomInt8;
		
		// si existeix el meeting en genera un altre
		Meeting meeting = findMeeting(vnumero);
		if (meeting != null) {
			getMeetingId();
		} 
		return vnumero;
	}
		
	/**
	 * Si el paràmetre és la cadena "null" retorna null 
	 * @param cad
	 * @return
	 */
	public static final String comprobaNull(String cad) {
		if (cad == null || cad.equals("null")) return null;
		else return cad; 	
	}

	public static final Meeting findMeeting(String id_meeting) {
		
		Meeting meeting = null;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingDAO meetingDAO = 
			mysqlFactory.getMeetingDAO();
		
		// Find a meeting object. 
		try {
			meeting = meetingDAO.findByPrimaryKey(id_meeting);
		} catch (MeetingDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// meeting exist or not
		if (meeting == null) return null;
		else return meeting;
	}

	public static final MeetingLive findLiveMeeting(String id_meeting) {
		
		MeetingLive meetingLive = null;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingLiveDAO meetingLiveDAO = 
			mysqlFactory.getMeetingLiveDAO();
		
		// Find a meeting object. 
		try {
			meetingLive = meetingLiveDAO.findByPrimaryKey(id_meeting);
		} catch (MeetingDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// meeting exist or not
		if (meetingLive == null) return null;
		else return meetingLive;
	}

	public static final boolean meetingExist(String id_meeting, String context) {
			
		Meeting meeting = null;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingDAO meetingDAO = 
			mysqlFactory.getMeetingDAO();
		
		// Find a meeting object. 
		try {
			return meetingDAO.exist(id_meeting, context);
		} catch (MeetingDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// meeting exist or not
		if (meeting == null) return false;
		else return true;
	}

	public static final boolean isMeetingFinished(String id_meeting) {
		
		Meeting meeting = null;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingDAO meetingDAO = 
			mysqlFactory.getMeetingDAO();
		
		// Find a meeting object. 
		try {
			meeting = meetingDAO.findByPrimaryKey(id_meeting);
		} catch (MeetingDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// meeting is not finished
		if (meeting != null && !meeting.isFinish()) return false;
		else return true;
	}

	public static final boolean isBlockedMeeting(String id_meeting) {
		
		MeetingLive meetingLive = null;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingLiveDAO meetingLiveDAO = 
			mysqlFactory.getMeetingLiveDAO();
		
		// Find a meeting object. 
		try {
			meetingLive = meetingLiveDAO.findByPrimaryKey(id_meeting);
		} catch (MeetingDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// meeting is not blocked or not
		if (meetingLive != null && !meetingLive.isBlocked()) return false;
		else if (meetingLive == null) return false;
		else return true;
	}

	public static final boolean isMaximParticipants(String id_meeting) {
		
		MeetingLive meetingLive = null;
		boolean resultat = false;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingLiveDAO meetingLiveDAO = 
			mysqlFactory.getMeetingLiveDAO();
		
		// Find a meeting object. 
		try {
			meetingLive = meetingLiveDAO.findByPrimaryKey(id_meeting);
		} catch (MeetingDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// meeting is in max participants or not
		int numero_actual = 0;
		if (meetingLive != null) numero_actual = meetingLive.getN_participants();
		int num_max = Integer.parseInt(Utils.getProperty("num.max.conexion")); 
		if (meetingLive != null && numero_actual+1>num_max) resultat = true;
		else resultat =  false;
		
		return resultat;
	}
	
	/**
	 * 
	 * @param id_meeting
	 * @return
	 */
	public static final boolean sessioVideoconferenciaPlena(String id_meeting) {
		
		boolean resultat = false;
		int num_users = 0;
		int num_max = Integer.parseInt(Utils.getProperty("num.max.conexion"));
		org.uoc.red5.videoconference.Meeting meeting = SingletonMeetings.getSingletonMeetings().getMeeting(id_meeting);
		if (meeting != null)
			num_users = meeting.getNumUsers();
		else
			num_users = 1;
		System.out.println("número usuaris ====================================----> " + num_users);
		
		if (num_users>num_max)
			resultat = true;

		System.out.println("resultat ====================================----> " + resultat);

		
		return resultat;
	}
	

	/**
	 * Comproba que no s'estigui fent mes videoconferencies simultanies
	 * que el numero configurat a les properies
	 * @return
	 */
	public static final boolean actualRoomsAvailable(String id_meeting) {
		
		int num_rooms = 0;
		boolean resultat = false;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingLiveDAO meetingLiveDAO = 
			mysqlFactory.getMeetingLiveDAO();
		
		// Find a meeting object. 
		try {
			MeetingLive meeting_live = meetingLiveDAO.findByPrimaryKey(id_meeting);
			if (meeting_live == null) { // nou meeting/room a obrir
				num_rooms = meetingLiveDAO.countLiveMeetings() + 1; // sumem la nostre
				int num_max_rooms = Integer.parseInt(Utils.getProperty("num.max.rooms")); 
				if (num_rooms<=num_max_rooms) resultat = true;
				else resultat =  false;
			} else {
				resultat = true; // és un meeting/room que ja esta obert
			}
		} catch (MeetingDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 		
		return resultat;
	}

	public static final boolean actualRoomsAvailable() {
		
		int num_rooms = 0;
		boolean resultat = false;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingLiveDAO meetingLiveDAO = 
			mysqlFactory.getMeetingLiveDAO();
		
		// Find a meeting object. 
		try {
			num_rooms = meetingLiveDAO.countLiveMeetings() + 1; // sumem la nostre
		} catch (MeetingDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int num_max_rooms = Integer.parseInt(Utils.getProperty("num.max.rooms")); 
		if (num_rooms<=num_max_rooms) resultat = true;
		else resultat =  false;

		// 		
		return resultat;
	}

	
	/**
	 * Insereix o actualitza en la taula live_meeting
	 * quant un participant entra en el meeting. També 
	 * insereix el participant a la taula de meeting.
	 * 
	 * @param id_meeting
	 * @param id_context
	 */
	public static final void inMeeting(String id_meeting, String id_context) {
		
		// create the required DAO Factory
		DAOFactory mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingLiveDAO meetingLiveDAO = mysqlFactory.getMeetingLiveDAO();
		 
		try {
			MeetingLive meetingLive = meetingLiveDAO.findByPrimaryKey(id_meeting);
			if (meetingLive == null) {
				meetingLive = new MeetingLive();
				meetingLive.setId(id_meeting);
				meetingLive.setId_context(id_context);
				meetingLive.setBlocked(false);
				meetingLive.setN_participants(0);
				meetingLiveDAO.insert(meetingLive);
			} else {
				int n_participants = meetingLive.getN_participants() + 1;
				meetingLiveDAO.updateNumParticipants(id_meeting, n_participants);
			}	
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Actualitza la la fila de la taula live_meeting, 
	 * amb un nou participant, o borra la fila si es
	 * tanca el meeting. També treu el participant 
	 * de la taula de meeting.
	 * 
	 * @param id_meeting
	 */
	public static final void outMeeting(String id_meeting) {

		// create the required DAO Factory
		DAOFactory mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingLiveDAO meetingLiveDAO = mysqlFactory.getMeetingLiveDAO();
		
		try {
			MeetingLive meetingLive = meetingLiveDAO.findByPrimaryKey(id_meeting);
			if (meetingLive != null) {
				int n_participants = meetingLive.getN_participants();
				if (n_participants == 1) {
					meetingLiveDAO.delete(id_meeting);
				} else {
					n_participants = meetingLive.getN_participants() - 1;
					meetingLiveDAO.updateNumParticipants(id_meeting, n_participants);
				}
			} 
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Actualitza a la taula meeting, la data inici 
	 * de la conferència.
	 * 
	 * @param id_meeting
	 */
	public static final void initRecordMeeting(String context, String id_meeting, Hashtable users) {

		Meeting meeting = null;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingDAO meetingDAO = 
			mysqlFactory.getMeetingDAO();
		
		try {
			updateParticipantsMeeting(id_meeting, users);
			meeting = meetingDAO.findByPrimaryKey(id_meeting);
			if (meeting != null) {
				meetingDAO.updateDataInici(id_meeting);
			}	
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * Actualitza a la taula meeting, la data final 
	 * de la conferència.
	 * 
	 * @param id_meeting
	 */
	public static final void stopRecordMeeting(String id_meeting) {
		
		Meeting meeting = null;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingDAO meetingDAO = 
			mysqlFactory.getMeetingDAO();
		
		try {
			meeting = meetingDAO.findByPrimaryKey(id_meeting);
			if (meeting != null) {
				java.util.Date dataInici = meeting.getBegin();
				meetingDAO.updateDataFinal(id_meeting, dataInici);
			}	
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Esborra la fila de live_meeting, pertanyent al 
	 * meeting que es tanca. finish meeting. quant es selecciona archive and close
	 * 
	 * @param id_meeting
	 * @throws InterruptedException 
	 */
	public static final String closeMeeting(String id_meeting, String topic, String description) {

		// create the required DAO Factory
		DAOFactory mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		String url_video = "";
		
		// Create a DAO
		MeetingLiveDAO meetingLiveDAO = mysqlFactory.getMeetingLiveDAO();
		MeetingDAO meetingDAO = mysqlFactory.getMeetingDAO();
		
		try {
			updateFinishMeeting(id_meeting);
			meetingLiveDAO.delete(id_meeting);
			
			// arxiva la gravació amb un altre id
			// i torna a obrir el meeting (room) 
			String id_arxiu = getMeetingId();
			
			Meeting meeting = meetingDAO.findByPrimaryKey(id_meeting);
			// canvia alguna columna del meeting
			String id_context = meeting.getId_context();
			String path_old = meeting.getPath();
			String path_new = Utils.getProperty("pathStreaming")+File.separator+id_context+File.separator+id_arxiu;
			Date data_inici = meeting.getBegin();
			Date data_final = meeting.getEnd();
			String participants = meeting.getParticipants();
			meeting.setId(id_arxiu);
			meeting.setPath(path_new);
			meeting.setTopic(topic);
			meeting.setDescription(description);
			meeting.setFinish(true);
			meetingDAO.insert(meeting);
			meetingDAO.updateParticipants(id_arxiu, participants);
			meetingDAO.updateDataIniciFinal(id_arxiu, data_inici, data_final);
			
			// url per veure el video
			if(SingletonMeetings.getSingletonMeetings()!=null){
				//url_video = SingletonMeetings.getSingletonMeetings().getUrl_server()+ "/Front?control=play&id_meeting=" +id_arxiu;
				SingletonMeetings.getSingletonMeetings().addViewMeeting(id_meeting, id_arxiu);
				url_video = SingletonMeetings.getSingletonMeetings().getViewMeeting(id_meeting);
			}
			
			// crea directori amb nom id_arxiu
			//FileUtils.makeDirectory(path_new);
			// copia el contingut del directory id_meeting a id_arxiu
			//FileUtils.copyDirectoryFiles(new File(path_old), new File(path_new));
			// esborra el contingut de id_meeting
			//FileUtils.deleteDirectoryFiles(path_old);

			FileUtils.renameDirectory(path_old, path_new);
			
			VideoConferenceMetadata vcm = new VideoConferenceMetadata(path_new, topic, description);	
			// actualitza el topic i la descripcio en el metadata.xml
			vcm.updateTopicVideo();
			vcm.updateDescriptionVideo();
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}

/* 		// de moment inhabilitat
		// genera un retard de 4 segons
		// i reobre la sessió/room per tornar a gravar un altre meeting
		long milisegons_inici = System.currentTimeMillis();
		long milisegons_actual = milisegons_inici ;
		long milisegons_final = milisegons_inici + 4000; // afegeix quatre segons
		while(milisegons_actual < milisegons_final) {
			milisegons_actual = System.currentTimeMillis();;
		}			
		cleanRowMeeting(id_meeting);
*/
		
		return url_video;
	}

	public static void cleanRowMeeting(String id_meeting) {

		// create the required DAO Factory
		DAOFactory mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		MeetingDAO meetingDAO = mysqlFactory.getMeetingDAO();
		// inicia les dades de la fila id_meeting
		try {
			meetingDAO.updateCleanRow(id_meeting);
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Esborra la fila de la taula de MeetingLive
	 * @param id_meeting
	 */
	public static final void deleteMeetingLive(String id_meeting) {

		// create the required DAO Factory
		DAOFactory mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingLiveDAO meetingLiveDAO = mysqlFactory.getMeetingLiveDAO();
		
		try {
			meetingLiveDAO.delete(id_meeting);
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 
	 * @param id_meeting
	 * @param participant
	 */
	private static final void updateParticipantsMeeting(String id_meeting, Hashtable users) {

		Meeting meeting = null;
		String participants = "";
		int cont = 0;
		Enumeration keys = users.keys();
		while (keys.hasMoreElements()) {
			cont++;
			String key = (String)keys.nextElement();
			User user = (User)users.get(key);
			String nom = user.getFullName();
			if (cont>1)
				participants = participants + " , " + nom;
			else
				participants = nom;
		}
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingDAO meetingDAO = 
			mysqlFactory.getMeetingDAO();
		
		try {
			meeting = meetingDAO.findByPrimaryKey(id_meeting);
			if (meeting != null) {
				meetingDAO.updateParticipants(id_meeting, participants);
			}	
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}

	}

	private static final void updateFinishMeeting(String id_meeting) {

		Meeting meeting = null;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingDAO meetingDAO = 
			mysqlFactory.getMeetingDAO();
		
		try {
			meeting = meetingDAO.findByPrimaryKey(id_meeting);
			if (meeting != null) {
				meetingDAO.updateFinish(id_meeting);
			}	
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}
	}
	
	public static final String duracioMeeting(java.util.Date h_inici, java.util.Date h_final) {
				
		long segons = (h_final.getTime() - h_inici.getTime()) / 1000;
		long minuts = 0;
		long hores = 0;
				
		hores =  segons/3600;
		minuts = segons/60 % 60;
		segons = segons % 60;		
		
	/*	String resultat = "";
		java.util.Date time = new java.util.Date();
		time.setTime(h_final.getTime() - h_inici.getTime());
		if (time.getHours() - 1 <0)
			resultat = "0:"+time.getMinutes()+":"+time.getSeconds();
		else
			resultat = (time.getHours() - 1)+":"+time.getMinutes()+":"+time.getSeconds();
		*/
		
		//return resultat;
		return hores + ":" + minuts + ":" + segons;
	}

	public static final void insertMeeting(Meeting meeting) {
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingDAO meetingDAO = 
			mysqlFactory.getMeetingDAO();
		
		try {
			meetingDAO.insert(meeting);
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}
	}

	public static final List<Meeting> findBySearch(String context, String topic, String participant, String dataInici, String dataFinal) {
		
		List<Meeting> resultat = null;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingDAO meetingDAO = 
			mysqlFactory.getMeetingDAO();
		
		try {
			resultat = meetingDAO.findBySearch(context, topic, participant, dataInici, dataFinal);
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}
		return resultat;
	}

	/**
	 * Bloqueja o desbloqueja el meeting amb un identificador donat.
	 * @param id_meeting
	 * @param oper
	 */
	public static final void updateBlocked(String id_meeting, boolean oper) {

		// create the required DAO Factory
		DAOFactory mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingLiveDAO meetingLiveDAO = mysqlFactory.getMeetingLiveDAO();
		
		try {
			meetingLiveDAO.updateBlocked(id_meeting, oper);
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Indica que s'esta produint una gravació en aquell moment en el id_meeting indicat.
	 * @param id_meeting
	 * @param oper
	 */
	public static final void updateRecording(String id_meeting, boolean oper) {

		// create the required DAO Factory
		DAOFactory mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingLiveDAO meetingLiveDAO = mysqlFactory.getMeetingLiveDAO();
		
		try {
			meetingLiveDAO.updateRecording(id_meeting, oper);
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Indica si la videoconferencia ha estat ja gravada alguna vegada.
	 * @param id_meeting
	 * @return
	 */
	public static final boolean videoconferenceIniciada(String id_meeting) {
		boolean dates_iguals = false;

		Meeting meeting = findMeeting(id_meeting); 
		
		if (meeting.getBegin().equals(meeting.getEnd()))
			dates_iguals = true;
		
		if (dates_iguals)
			return false;
		else
			return true;
	}

	public static final boolean videoconferenceBloquejada(String id_meeting) {
		
		boolean resultat = false;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingLiveDAO meetingLiveDAO = mysqlFactory.getMeetingLiveDAO();
		
		try {
			MeetingLive meetingLive = meetingLiveDAO.findByPrimaryKey(id_meeting);
			if (meetingLive != null) {
				resultat = meetingLive.isBlocked();
			} else {
				resultat = false;
			}	
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}
		return resultat;
	}

	public static final boolean isMeetingRecording(String id_meeting) {
		boolean resultat = false;
		
		MeetingLive meetingLive = null;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		MeetingLiveDAO meetingLiveDAO = 
			mysqlFactory.getMeetingLiveDAO();

		// Find a meeting object. 
		try {
			meetingLive = meetingLiveDAO.findByPrimaryKey(id_meeting);
		} catch (MeetingDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// mira si s'esta gravant
		if (meetingLive == null) 
			resultat = false;
		else if (meetingLive.isRecording())
			resultat = true;
		else 
			resultat = false;
		
		return resultat;
	}

	
	
	
	public static final void updateParticipantsMeeting(String id_meeting) {

		Hashtable participants = null;
		String _participants = "";
		if (SingletonMeetings.getSingletonMeetings().getMeeting(id_meeting) != null) {
			participants = SingletonMeetings.getSingletonMeetings().getMeeting(id_meeting).getUsers();
			Collection users = participants.values();
			Iterator user = users.iterator();
			while (user.hasNext()) {
				User user_ = (User)user.next();
				if (_participants.equals("")) {
					_participants = user_.getFullName();					
				} else {
					_participants = _participants + " , " + user_.getFullName();
				}
			}

			// create the required DAO Factory
			DAOFactory mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);

			// Create a DAO
			MeetingLiveDAO meetingLiveDAO = mysqlFactory.getMeetingLiveDAO();
			try {
				meetingLiveDAO.updateParticipants(id_meeting, _participants);
				meetingLiveDAO.updateNumParticipants(id_meeting, users.size());
			} catch (MeetingDAOException e) {
				e.printStackTrace();
			}
		}	
	}

	public static final void updateTopic_Description(String id_meeting, String topic, String description) {

			// create the required DAO Factory
			DAOFactory mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);

			// Create a DAO
			MeetingDAO meetingDAO = mysqlFactory.getMeetingDAO();
			try {
				meetingDAO.updateTopic_Description(id_meeting, topic, description);
			} catch (MeetingDAOException e) {
				e.printStackTrace();
			}
	}

	
	public static final void checkLiveMeeting(String id_meeting) {

		MeetingLive meetingLive = null;
		String participants = "";
		String splitPattern = " , ";
		String tokens[] = null;
		// create the required DAO Factory
		DAOFactory mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		// Create a DAO
		MeetingLiveDAO meetingLiveDAO = mysqlFactory.getMeetingLiveDAO();
		
		try {
			meetingLive = meetingLiveDAO.findByPrimaryKey(id_meeting);
			if (meetingLive != null) {
				participants = meetingLive.getParticipants();
				Pattern p = Pattern.compile(splitPattern);
			    tokens = p.split(participants);
			    int num_participants = tokens.length;
				if (num_participants ==0) 
					meetingLiveDAO.updateNumParticipants(id_meeting, 0);
				else
					meetingLiveDAO.updateNumParticipants(id_meeting, num_participants+1);
			}
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}
	}

	public static final void createMeetingAutomated(String id_meeting, String id_context, String topic, String description) {
		
		boolean exist = Utils.meetingExist(id_meeting, id_context);
		if (!exist) {
			// insereix un objecte Meeting a la bd
			Meeting meeting = new Meeting();
			meeting.setId(id_meeting);
			meeting.setTopic(topic);
			meeting.setDescription(description);
			meeting.setFinish(false);
			meeting.setId_context(id_context);
			meeting.setPath(Utils.getProperty("pathStreaming")+File.separator+id_context+File.separator+id_meeting);
			Utils.insertMeeting(meeting);
			// existeix però es mira que el finish no estigui a 1
		// si és així inicialitza la fila ja que és una room
		// no una gravació d'un meeting. I sempre ha d'estar operativa	
		} else {
			Meeting meeting = Utils.findMeeting(id_meeting);
			if (meeting.isFinish()) {
				Utils.cleanRowMeeting(id_meeting);
			} else {
				meeting.setTopic(topic);
				meeting.setDescription(description);
				Utils.updateTopic_Description(id_meeting, topic.trim(), description.trim());
			}
		}
	}

	/**
	 * Comprova que si s'esta gravant, existeixin els arxius dels participants
	 * de la sessió.
	 * @param id_meeting
	 * @return
	 */
	public static final boolean gravacioFuncionaCorrectament(String id_meeting) {

		boolean funciona = true; 
		Hashtable participants = null;
		boolean gravant = isMeetingRecording(id_meeting);
		if (gravant) {
			if (SingletonMeetings.getSingletonMeetings().getMeeting(id_meeting) != null) {
				String path = SingletonMeetings.getSingletonMeetings().getRecordPath(id_meeting);
				participants = SingletonMeetings.getSingletonMeetings().getMeeting(id_meeting).getUsers();
				Collection users = participants.values();
				Iterator user = users.iterator();
				while (user.hasNext() && funciona) {
					User user_ = (User)user.next();
					String fileName = user_.getVideoName();
					File file = new File(path+File.separator+fileName);
					if (!file.exists()) funciona = false;
				}
			}
			log.info("gravant sessió: " + id_meeting + " és correcte: " + funciona);	
		}
		return funciona;
	}
	

	/**
	 * Busca les aplicacions LTI per un contexte
	 * @param context
	 * @param name
	 * @return
	 */
	public static List<LTIApplication> findLTIApplicationBySearch(String context, String name) {
		
		List<LTIApplication> resultat = null;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		LTIApplicationDAO ltiDAO = 
			mysqlFactory.getLTIApplicationDAO();
		
		try {
			resultat = ltiDAO.findBySearch(context, name);
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}
		return resultat;
	}
	

	/**
	 * Busca les aplicacions LTI per un contexte
	 * @param context
	 * @param name
	 * @return
	 */
	public static List<RemoteApplication> findRemoteApplicationBySearch(String context, String name) {
		
		List<RemoteApplication> resultat = null;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		RemoteApplicationDAO appDAO = 
			mysqlFactory.getRemoteApplicationDAO();
		
		try {
			resultat = appDAO.findBySearch(context, name);
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}
		return resultat;
	}
	/**
	 * Torna l'app lti
	 * @param id
	 * @return
	 */
	public static final LTIApplication findLTIApplicationById(int id) {
		LTIApplication lti = null;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		LTIApplicationDAO ltiDAO = 
			mysqlFactory.getLTIApplicationDAO();
		
		try {
			lti = ltiDAO.findByPrimaryKey(id);
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}
		return lti;		
	}
	

	/**
	 * Return remote app
	 * @param id
	 * @return
	 */
	public static final RemoteApplication findRemoteApplicationById(int id) {
		RemoteApplication app = null;
		
		// create the required DAO Factory
		DAOFactory mysqlFactory =   
		  DAOFactory.getDAOFactory(DAOFactory.MYSQL);

		// Create a DAO
		RemoteApplicationDAO appDAO = 
			mysqlFactory.getRemoteApplicationDAO();
		
		try {
			app = appDAO.findByPrimaryKey(id);
		} catch (MeetingDAOException e) {
			e.printStackTrace();
		}
		return app;		
	}

	/**
	 * Parse the customparameters
	 * @param customparameters
	 * @return
	 */
	public static String[][] parseCustomParameters(String customparameters) {
		String [][] r = null;
		String[] rtemp = new String[0];
		try {
			if (customparameters!=null && !"".equals(customparameters)) {
				customparameters = customparameters.trim();
				rtemp = customparameters.split(";");
			}
			r = new String[rtemp.length][1];
			for (int i=0; i<rtemp.length; i++){
				r[i] = rtemp[i].split("=");
			}
		} catch (Exception e){
			 r = new String[0][0];
		}
		return r;
	}

	public static void copiaArxiuGravat(String id_meeting) {
		
		// create the required DAO Factory
		DAOFactory mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		MeetingDAO meetingDAO = mysqlFactory.getMeetingDAO();
		
		try {
			log.info("---- estem en manteArxiuGravat ----");
			Meeting meeting = meetingDAO.findByPrimaryKey(id_meeting);
			String path_old = meeting.getPath();
			String path_new = path_old + "_" + Utils.getMeetingId();
			log.info("---- path_old ---->>> " + path_old);
			log.info("---- path_new ---->>> " + path_new);
			FileUtils.renameDirectory(path_old, path_new);
		} catch (Exception e){
		}
	
	}

	// agafa les dos primeres lletres
	// exemple: en_EN agafaria en
	// per defecte el language que té en config.properties
	public static String getLocale(String code) {
		String language = "";
		if (code == null || code.equals("")) { 
			language = Utils.getProperty("app.language"); // llenguatge per defecte
		} else {
			language = code.substring(0, 2);
		}
		System.out.println("language**====================-----------------------------------> " + language);
		return language;
	}
}
