package org.uoc.podcast;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.demos.fitc.Application;

/**
 * Servlet implementation class for Servlet: Conversor
 *
 */
 public class Conversor extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	 
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String TIPUS_VIDEO = "video";
	private final String TIPUS_AUDIO = "audio";
	private static final Log log = LogFactory.getLog(Application.class);
 
	
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public Conversor() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.info("######################## HEM ENTRAT A CONVERSOR ###################");
		String resultat = "Error";
		// arxiu a convertir
		String arxiu = request.getParameter("file");
		// agafa la primera part per veure si audio o video
		if (arxiu != null && !arxiu.equals("")) {
			StringTokenizer st = new StringTokenizer(arxiu,"_");
			String tipus = (String)st.nextToken();
			if (convert(tipus, arxiu)){
				resultat = arxiu + " generat correctament";
			} else {
				resultat = arxiu + " no s'ha generat correctament";
			}
		} else {
			resultat = "Falta parametre file a la URL";
		}
		log.info("######################## HEM ENTRAT A CONVERSOR ###################");
		// escriu el resultat
		PrintWriter pw = response.getWriter();
        response.setContentType("text/html");
        pw.print("<html><head/><body><h1>" + resultat + "</h1></body></html>");			
		
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}   	  	    

	private boolean convert(String tipus, String arxiu) {

		boolean resultat = false;
		
		try {
			
			// depenent del tipus converteix a mp3 o mp4
			if (tipus.equals(TIPUS_VIDEO)) {
				String comanda = ConfigurationProperties.getInstance().getProperty("shell.video") + " " + arxiu;
				Process p = Runtime.getRuntime().exec(comanda);
				p.waitFor();
				int result = p.exitValue();
			    if (result == 0) {
			    	resultat = true;
			    	log.info("######################## " + arxiu + " convertit a MP4");			
			    } else {
			    	resultat = false;	
			    	throw new Exception("Executant: " + comanda + " Codi error: " + result);
			    }
			} else if (tipus.equals(TIPUS_AUDIO)) {
				String comanda = ConfigurationProperties.getInstance().getProperty("shell.audio") + " " + arxiu;
				Process p = Runtime.getRuntime().exec(comanda);
				p.waitFor();
				int result = p.exitValue();
			    if (result == 0) {
			    	resultat = true;
			    	log.info("######################## " + arxiu + " convertit a MP4");			
			    } else {
			    	resultat = false;
			    	throw new Exception("Executant: " + comanda + " Codi error: " + result);
			    }
			} else {
				log.info("######################## l'arxiu ha de tenir format audio_xxxx o video_xxxx");			
			}

	    } catch (Exception err) {
	    	err.printStackTrace();
	    }
	    
	    return resultat;
	}
 
 }