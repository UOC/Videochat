package org.red5.server.webapp.oflaDemo;
/*
 * File RED5 modified by UOC
 *	
 * dcarrascogi@uoc.edu - Daniel Carrasco	
 * marcgener@uoc.edu - Marc Gener
 *
 *
 */

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.springframework.core.io.Resource;
import org.uoc.red5.videoconference.SingletonMeetings;
import org.uoc.red5.videoconference.VideoConferenceList;
import org.uoc.red5.videoconference.pojo.Meeting;
import org.uoc.red5.videoconference.pojo.MeetingLive;
import org.uoc.red5.videoconference.utils.Utils;

public class DemoService {

	protected static Log log = LogFactory.getLog(DemoService.class.getName());

	private String formatDate(Date date) {
		SimpleDateFormat formatter;
		String pattern = "dd/MM/yy H:mm:ss";
		Locale locale = new Locale("en", "US");
		formatter = new SimpleDateFormat(pattern, locale);
		return formatter.format(date);
	}
	
public Object getMeetingMetadata(String id_meeting) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		
		// search a specific meeting
		org.uoc.red5.videoconference.pojo.Meeting meeting = Utils.findMeeting(id_meeting);
		// construct the metadata
		metadata.put("topic", meeting.getTopic());
		metadata.put("text_description", meeting.getDescription());
		metadata.put("context", meeting.getId_context());
		metadata.put("participants", meeting.getParticipants());
		metadata.put("meeting_end", Utils.formatDate(meeting.getEnd()));
		metadata.put("meeting_begin", Utils.formatDate(meeting.getBegin()));
		metadata.put("meeting-path", meeting.getPath());
				
		return metadata;
	}

	
	/** 
	 * Return the information from specific room (scope)
	 *
	 * @return Value for property 'listOfAvailableFLVs'.
	 */ 
	public List getListOfAvailableUOCFLVs(String scope, String id_meeting) {	

		String path = Utils.getProperty("pathStreaming")+File.separator+scope+File.separator+id_meeting;

		//path ="C:"+File.separator+"eclipse-ganimede"+File.separator+"workspace"+File.separator+"red5"+File.separator+"WebContent"+File.separator+"videoconferencia"+File.separator+"videos"+File.separator+scope;
		VideoConferenceList vcl = new VideoConferenceList(path,false);
		Map out = vcl.getListOfAvailableUOCFLVs();
		vcl.deteleDocument();
		//System.out.println("out ----> " + out);
		
		//Li pasem el map ordenat per posicions
		///Map<Integer, Object> out2 = new HashMap<Integer, Object>();
		List<Object> out2 = new ArrayList<Object>();
		for(int i=out.size()-1; i>=0; i--){
			//encruzijada de mapas!!! X
			out2.add(out.get(String.valueOf(i)));
		}
		 
		return out2;
	}
		
	/**
	 * Tanca un meeting amb identificador donat.
	 */
	public static String closeMeeting(String id_meeting, String topic, String description) {
		String url = Utils.closeMeeting(id_meeting, topic, description);
		return url;
	}

	/**
	 * url per visionar el meeting gravat
	 * @param id_meeting
	 * @return
	 */
	public static String urlViewMeeting(String id_meeting) {
		String url = SingletonMeetings.getSingletonMeetings().getViewMeeting(id_meeting);
		return url;
	}
	
	/**
	 * Bloqueja un meeting amb identificador donat.
	 */
	public static void blockMeeting(String id_meeting) {
		Utils.updateBlocked(id_meeting, true);
	}

	/**
	 * Desbloqueja un meeting amb identificador donat.
	 */
	public static void unlockMeeting(String id_meeting) {
		Utils.updateBlocked(id_meeting, false);
	}

	/**
	 * Posa que s'esta fent una gravació actualment en un meeting amb identificador donat.
	 */
	public static void recordMeeting(String id_meeting) {
		Utils.updateRecording(id_meeting, true);
	}

	/**
	 * Posa que no s'esta fent una gravació actualment en un meeting amb identificador donat.
	 */
	public static void stopMeeting(String id_meeting) {
		Utils.updateRecording(id_meeting, false);
	}

	
	/**
	 * Desbloqueja un meeting amb identificador donat.
	 */
	public static void isMeetingFinished(String id_meeting) {
		Utils.isMeetingFinished(id_meeting);
	}
	
	
	/**
	 * Getter for property 'listOfAvailableFLVs'.
	 *
	 * @return Value for property 'listOfAvailableFLVs'.
	 */
	public Map getListOfAvailableFLVs() {
		IScope scope = Red5.getConnectionLocal().getScope();
		Map<String, Map> filesMap = new HashMap<String, Map>();
		Map<String, Object> fileInfo;
		try {
			log.debug("getting the FLV files");
			Resource[] flvs = scope.getResources("streams/*.flv");
			
			if (flvs != null) {
				for (Resource flv : flvs) {
					File file = flv.getFile();
					Date lastModifiedDate = new Date(file.lastModified());
					String lastModified = formatDate(lastModifiedDate);
					String flvName = flv.getFile().getName();
					String flvBytes = Long.toString(file.length());
					if (log.isDebugEnabled()) {
						log.debug("flvName: " + flvName);
						log.debug("lastModified date: " + lastModified);
						log.debug("flvBytes: " + flvBytes);
						log.debug("-------");
					}
					fileInfo = new HashMap<String, Object>();
					fileInfo.put("name", flvName);
					fileInfo.put("lastModified", lastModified);
					fileInfo.put("size", flvBytes);
					filesMap.put(flvName, fileInfo);
				}
			}

			Resource[] mp3s = scope.getResources("streams/*.mp3");
			if (mp3s != null) {
				for (Resource mp3 : mp3s) {
					File file = mp3.getFile();
					Date lastModifiedDate = new Date(file.lastModified());
					String lastModified = formatDate(lastModifiedDate);
					String flvName = mp3.getFile().getName();
					String flvBytes = Long.toString(file.length());
					if (log.isDebugEnabled()) {
						log.debug("flvName: " + flvName);
						log.debug("lastModified date: " + lastModified);
						log.debug("flvBytes: " + flvBytes);
						log.debug("-------");
					}
					fileInfo = new HashMap<String, Object>();
					fileInfo.put("name", flvName);
					fileInfo.put("lastModified", lastModified);
					fileInfo.put("size", flvBytes);
					filesMap.put(flvName, fileInfo);
				}
			}
		} catch (IOException e) {
			log.error(e);
		}
		return filesMap;
	}

	/**
	 * Indica si la videoconferencia ha sigut gravada una vegada almenys.
	 */
	public boolean videoConferenceIniciada(String id_meeting) {
		boolean resultat = Utils.videoconferenceIniciada(id_meeting);
		return resultat;
	}
	
	/**
	 * Indica si la videoconferencia està bloquejada
	 */
	public boolean videoConferenceBloquejada(String id_meeting) {
		boolean resultat = Utils.videoconferenceBloquejada(id_meeting);
		return resultat;
	}
	
	/**
	 * Indica si la videoconferencia està finalitzada
	 */
	public boolean videoConferenceFinished(String id_meeting) {
		boolean resultat = Utils.isMeetingFinished(id_meeting);
		return resultat;
	}
	
	/**
	 * Indica si s'esta gravant en aquell moment la videoconferencia.
	 * @param id_meeting
	 * @return
	 */
	public boolean videoConferenceIsRecording(String id_meeting) {
		boolean resultat = Utils.isMeetingRecording(id_meeting);
		return resultat;
	}
	
	/**
	 * Indica si s'esta gravant, i que s'estiguin generant els 
	 * arxius gravats.
	 * @param id_meeting
	 * @return
	 */
	public boolean videoConferenceIsRecordingCorrect(String id_meeting) {
		boolean isRecordingCorrect = true;
		boolean isRecording = videoConferenceIsRecording(id_meeting);
		//log.info("s'està gravant la sessió: " + id_meeting + " : " + resultat);
		if (isRecording) {
			isRecordingCorrect = Utils.gravacioFuncionaCorrectament(id_meeting);
			//log.info("gravant correctament: " + funciona);
		}  
		return isRecordingCorrect;
	}
	

	/**
	 * inicialitza el meeting pare desopres de fer
	 * un archive and close
	 * @param id_meeting
	 */
	public void videoConferenceCleanRow(String id_meeting) {
		Utils.cleanRowMeeting(id_meeting);
	}

	/**
	 * Recupera els participants de la videoconferencia.
	 * @param id_meeting
	 * @return
	 */
	public String videoConferenceParticipants(String id_meeting) {
		MeetingLive meetingLive = Utils.findLiveMeeting(id_meeting);
		String participants = "";
		if (meetingLive != null) {
			participants = meetingLive.getParticipants();			
		}
		return participants;
	}

	public void copiarArxiuGravat(String id_meeting) {
		Utils.copiaArxiuGravat(id_meeting);
	}

	public boolean sessioVideoconferenciaPlena(String id_meeting) {
		return Utils.sessioVideoconferenciaPlena(id_meeting);
	}

}
