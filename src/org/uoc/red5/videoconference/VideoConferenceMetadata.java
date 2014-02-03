/**
 * Create a XML file, with metadatas by videoconference.
 */
package org.uoc.red5.videoconference;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.demos.fitc.Application;
import org.uoc.red5.videoconference.utils.Utils;
import org.w3c.dom.*;

public class VideoConferenceMetadata {

	private static final String VIDEO_TAG = "vid:video";
	private static final String VIDEO_NAME = "vid:name";
	private static final String AUTHOR_TAG = "vid:autor";
	private static final String FULL_NAME_TAG = "vid:full-name";
	private static final String EMAIL_TAG = "vid:e-mail";
	//private static final String TOTAL_VIDEOS_TAG = "vid:total";
	private static final String VIDEO_TIME_START_TAG = "vid:start-videoconference";
	private static final String VIDEO_TIME_END_TAG = "vid:end-videoconference";
	private static final String VIDEO_DATE_TAG = "vid:date-videoconference";
	private static final String VIDEO_TOPIC_TAG = "vid:topic-videoconference";
	private static final String VIDEO_DESCRIPTION_TAG = "vid:description-videoconference";
	
	private String fileName = "metadades.xml";
	private Document doc = null;
	private boolean nouDocument = false;
	private String videoName = "";
	private String fullName = "";
	private String email = "";
	private String topic = "";
	private String description = "";
	private static final Log log = LogFactory.getLog(Application.class);
	
		
	public VideoConferenceMetadata(String path) {
		fileName = path + File.separator + fileName;
		log.warn("Videoconference "+fileName);
	}

	public VideoConferenceMetadata(String path, String topic, String description) {
		this.topic = topic;
		this.description = description;
		fileName = path + File.separator + fileName;
		log.warn("Videoconference with topic "+fileName);
	}

	/**
	 * Create a videoconference xml file. With metadata params.
	 * @param videoName
	 * @param fullName
	 * @param email
	 */
	public void createVideoconferenceXML(String videoName, String fullName, String email, String topic, String description) {
		try {
			//System.out.println("----- entrem a VideoConferenceMetadata ----");
			initialize(videoName, fullName, email, topic, description);
			//mirar si s'ha de crear el document
			// o si es pot parsejar i posar en mem�ria ja que existeix
			doc = parseXmlFile();
			if (doc == null) {
				nouDocument = true;
				log.warn("Videoconferenc creating document"+ fileName);
				createDocument();
			}	
			createVideo();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(FileNotFoundException fnfe) {
		}
	}

	public void createVideoconferenceXML() {
		try {
			doc = parseXmlFile();
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
	}

	/**
	 * Initialize metadata.
	 * @param videoName
	 * @param fullName
	 * @param email
	 */
	private void initialize(String videoName, String fullName, String email, String topic, String description) {
		this.videoName = videoName;
		this.fullName = fullName;
		this.email = email;
		this.topic = topic;
		this.description = description;
	}
	
	/**
	 * Create document if it is not exist.
	 */
	private void createDocument() {
		
		try {
			  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			  DocumentBuilder builder = factory.newDocumentBuilder();
			  DOMImplementation impl = builder.getDOMImplementation();
			  
			  //DocumentType svgDOCTYPE = impl.createDocumentType("vid:videos", 
			  // "-//W3C//DTD SVG 1.0//EN", 
			  // "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd");
			  doc = impl.createDocument(
			   "http://www.example.org/metadades", "vid:videos", null
			  );  
			  log.warn("Videoconference createddocument"+ fileName);

			}
			catch (FactoryConfigurationError e) { 
			  log.error(
			   "Could not locate a JAXP DocumentBuilderFactory class", e); 
			}
			catch (ParserConfigurationException e) { 
			  log.error(
			   "Could not locate a JAXP DocumentBuilder class", e); 
			}

	}
		
	/**
	 * If the document exist, parse and put in memory (DOM).
	 * @return
	 * @throws FileNotFoundException
	 */
	private Document parseXmlFile() throws FileNotFoundException{
		Document doc;
		try {
			// Obtain Factory instance
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// Request parser to ignore white space in the document
			dbf.setIgnoringElementContentWhitespace(true);
			// Obtain DocumentBuilder instance from the factory object
			DocumentBuilder db = dbf.newDocumentBuilder();
			// Parse the input file
			log.warn("*********************** filenameMeta "+fileName);
			doc = db.parse(new File(fileName));
			// return reference to the root node
			return doc;
		} catch (FileNotFoundException fex) {
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
			return null;
	}
	
	/**
	 * Create new video in XML file.
	 * Nota: Originalment teniem el número de videos en els metadades.xml
	 * però s'ha inhabilitat, ja que no és necessari.
	 * @throws TransformerException
	 */ 
	private void createVideo() throws TransformerException {
		  Element root = doc.getDocumentElement();  
		  Element videoElement = createTagVideo();
		  //Element numeroVideos = createTagTotalVideos();
		  Element videoTopic = createTagTopic();
		  Element videoDescription = createTagDescription();
		  Element videoStartTime = createTagStartTime();
		  Element videoDate = createTagDate();
		  Element videoEndTime = createTagEndTime();
		  if (nouDocument) root.appendChild(videoTopic);
		  if (nouDocument) root.appendChild(videoDescription);
		  if (nouDocument) root.appendChild(videoDate);
		  if (nouDocument) root.appendChild(videoStartTime);
		  if (nouDocument) root.appendChild(videoEndTime);
		  //root.appendChild(numeroVideos);
		  root.appendChild(videoElement);
		  printXML();
	}

	public void updateEndVideoTime() {
		try {
			doc = parseXmlFile();
			Element root = doc.getDocumentElement();
			NodeList nl = doc.getElementsByTagName(VIDEO_TIME_END_TAG);
			Element oldElement = (Element)nl.item(0);
			Element videoEndTime = createTagEndTime();
			root.replaceChild(videoEndTime,oldElement);			
			printXML();
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Create date videoconference tag.
	 * @return
	 */
	private Element createTagDate() {
		Element videoDateElement = doc.createElement(VIDEO_DATE_TAG);
		Text videoDateElementTextNode = doc.createTextNode(String.valueOf(Utils.getActualDate()));
		videoDateElement.appendChild(videoDateElementTextNode);
		
		return videoDateElement;
	}

	/**
	 * Create time start videoconference tag.
	 * @return
	 */
	private Element createTagStartTime() {
		Element videoTimeStartElement = doc.createElement(VIDEO_TIME_START_TAG);
		Text videoTimeStartElementTextNode = doc.createTextNode(String.valueOf(Utils.getActualTime()));
		videoTimeStartElement.appendChild(videoTimeStartElementTextNode);
		
		return videoTimeStartElement;
	}

	/**
	 * Create time end videoconference tag.
	 * @return
	 */
	private Element createTagEndTime() {
		Element videoTimeEndElement = doc.createElement(VIDEO_TIME_END_TAG);
		Text videoTimeEndElementTextNode = doc.createTextNode(String.valueOf(Utils.getActualTime()));
		videoTimeEndElement.appendChild(videoTimeEndElementTextNode);

		return videoTimeEndElement;
	}

	
	
/*	*//**
	 * Create total videos tag.
	 * @return
	 *//*
	private Element createTagTotalVideos() {
		int totalVideos = 0;
		if (!nouDocument) {
			NodeList nTotalVideos = doc.getElementsByTagName(FULL_NAME_TAG);
			totalVideos = nTotalVideos.getLength();
			NodeList nl = doc.getElementsByTagName(TOTAL_VIDEOS_TAG);
			Node n = nl.item(0);
			Element root = doc.getDocumentElement();
			root.removeChild(n);			
		}
		totalVideos = totalVideos + 1;
		Element totalVideosElement = doc.createElement(TOTAL_VIDEOS_TAG);
		Text totalVideosTextNode = doc.createTextNode(String.valueOf(totalVideos));
		totalVideosElement.appendChild(totalVideosTextNode);
		
		return totalVideosElement;
	}
*/
	/**
	 * Create topic of the videoconference tag.
	 * @return
	 */
	private Element createTagTopic() {
		Element videoTopicElement = doc.createElement(VIDEO_TOPIC_TAG);
		Text videoTopicElementTextNode = doc.createTextNode(topic);
		videoTopicElement.appendChild(videoTopicElementTextNode);
		
		return videoTopicElement;
	}
	
	/**
	 * Create description of the videoconference tag.
	 * @return
	 */
	private Element createTagDescription() {
		Element videoTopicElement = doc.createElement(VIDEO_DESCRIPTION_TAG);
		Text videoTopicElementTextNode = doc.createTextNode(description);
		videoTopicElement.appendChild(videoTopicElementTextNode);
		
		return videoTopicElement;
	}

	/**
	 * Create video tag.
	 * @return
	 */
	private Element createTagVideo() {
		Element videoElement = doc.createElement(VIDEO_TAG);
		Element nameElement = doc.createElement(VIDEO_NAME);
		Element autorElement = doc.createElement(AUTHOR_TAG);
		Text nameTextNode = doc.createTextNode(videoName);
		nameElement.appendChild(nameTextNode);
		// add video name
		videoElement.appendChild(nameElement);
		autorElement = createTagAutor();
		// add author
		videoElement.appendChild(autorElement);
		
		return videoElement;
	}
	
	/**
	 * Create author tag.
	 * @return
	 */
	private Element createTagAutor() {
		Element autorElement = doc.createElement(AUTHOR_TAG);
		Element fullNameElement = doc.createElement(FULL_NAME_TAG);
		Text fullNameTextNode = doc.createTextNode(fullName);
		fullNameElement.appendChild(fullNameTextNode);
		autorElement.appendChild(fullNameElement);
		
		Element emailElement = doc.createElement(EMAIL_TAG);
		Text emailTextNode = doc.createTextNode(email);
		emailElement.appendChild(emailTextNode);
		autorElement.appendChild(emailElement);

		return autorElement;
	}

	/**
	 * Make XML file.
	 * @throws TransformerException
	 */
	private void printXML() throws TransformerException {
		TransformerFactory tranFactory = TransformerFactory.newInstance(); 
	    Transformer aTransformer = tranFactory.newTransformer(); 
	    aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

	    Source src = new DOMSource(doc); 
	    //Result dest = new StreamResult(System.out); 
	    //aTransformer.transform(src, dest); 
	    Result dest2 = new StreamResult(new File(fileName)); 
	    aTransformer.transform(src, dest2);
	}

	/**
	 * 
	 * @return
	 */
	public String getTextMetadata() {
		
		// description videoconference
		NodeList nl = doc.getElementsByTagName(VIDEO_TOPIC_TAG);
		Node n = nl.item(0);
		String description = n.getTextContent();

		// date time videoconference
		nl = doc.getElementsByTagName(VIDEO_DATE_TAG);
		n = nl.item(0);
		String date = n.getTextContent();

		// start time videoconference
		nl = doc.getElementsByTagName(VIDEO_TIME_START_TAG);
		n = nl.item(0);
		String start = n.getTextContent();
		
		// users videoconference
		nl = doc.getElementsByTagName(EMAIL_TAG);
		String users = "(";
		for (int i=0; i<nl.getLength(); i++) {
			n = nl.item(i);
			StringTokenizer user = new StringTokenizer(n.getTextContent(),"@");
			users = users + user.nextToken();
			if (i<nl.getLength()-1)
				users = users + " ,";
		}
		users = users + ")";
		
		String textMetadata = description + " - " + date + " - " + start + " - " + users;
		System.out.println(textMetadata);
		
		return textMetadata;
	}

	public void updateTopicVideo() {
		try {
			doc = parseXmlFile();
			Element root = doc.getDocumentElement();
			NodeList nl = doc.getElementsByTagName(VIDEO_TOPIC_TAG);
			Element oldElement = (Element)nl.item(0);
			Element videoTopic = createTagTopic();
			root.replaceChild(videoTopic,oldElement);			
			printXML();
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateDescriptionVideo() {
		try {
			doc = parseXmlFile();
			Element root = doc.getDocumentElement();
			NodeList nl = doc.getElementsByTagName(VIDEO_DESCRIPTION_TAG);
			Element oldElement = (Element)nl.item(0);
			Element videoDescription = createTagDescription();
			root.replaceChild(videoDescription,oldElement);			
			printXML();
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
