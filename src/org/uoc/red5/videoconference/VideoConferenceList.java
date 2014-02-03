/**
 * Parse a XML file, with a scope list by videoconference.
 */
package org.uoc.red5.videoconference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import org.red5.server.service.ServiceInvoker;
import org.uoc.red5.videoconference.utils.FileUtils;
import org.uoc.red5.videoconference.utils.VideoConferenceUtils;
import org.w3c.dom.*;

public class VideoConferenceList { 
 
	/**
     * Logger 
     */
    private static final Log log = LogFactory.getLog(ServiceInvoker.class);
    
	private static final String VIDEO_TAG = "vid:video"; 
	private static final String URL_VIDEO_TAG = "url-video"; 
	private static final String INFO_VIDEO_TAG = "info-video";
	private Document doc = null; 
	private String fileName = "";  
	
	/**
	 * Create a videoconference xml file if crear is true. 
	 */ 
	public VideoConferenceList(String path, boolean crear) {
		try {
			
			// codi per posar tots els permisos a la carpeta
			// on està la gravació, ja que afegirem el list.xml
			try {
				String comanda = "chmod -R 777 " + path;
				Process p = Runtime.getRuntime().exec(comanda);
				p.waitFor();
				int result = p.exitValue();
				log.info("chmod -R 777 al path: " + path);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.fileName = path + File.separator + "list.xml";
			//log.warn("**********************************************1"+this.fileName); 
			// parsejar i posar en memoria el xml
			if (!crear) { 
				doc = parseXmlFile();
				//log.warn("**********************************************3"+doc); 
			} else {  
				File file = new File(this.fileName);
				//file.delete(); 
				createDocument();				
			}
		} catch(FileNotFoundException fnfe) {
		}
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
			   "http://www.example.org/list", "vid:videos", null
			  );  
			}
			catch (FactoryConfigurationError e) { 
			  System.out.println(
			   "Could not locate a JAXP DocumentBuilderFactory class"); 
			}
			catch (ParserConfigurationException e) { 
			  System.out.println(
			   "Could not locate a JAXP DocumentBuilder class"); 
			}

	}

	public void deteleDocument() {
		File file = new File(this.fileName);
		file.delete();
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
			//log.warn("**********************************************2.1"+this.fileName); 
			doc = db.parse(new File(this.fileName));
			//log.warn("**********************************************2.2"+doc); 
			// return reference to the root node
			return doc;
		} catch (FileNotFoundException fex) {
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
			return null;
	}
	
	private String[] getUrls() {
		
		String[] urls;
		
		NodeList nl = doc.getElementsByTagName(URL_VIDEO_TAG);
		urls = new String[nl.getLength()];
		for (int i=0; i<nl.getLength(); i++) {
			Node n = nl.item(i);
			String text = n.getTextContent();
			urls[i] = text;
		}
		
		return urls;
	}

	private String[] getInfos() {
		
		String[] info;
		
		NodeList nl = doc.getElementsByTagName(INFO_VIDEO_TAG);
		info = new String[nl.getLength()];
		for (int i=0; i<nl.getLength(); i++) { 
			Node n = nl.item(i);
			String text = n.getTextContent();
			info[i] = text;
		}
		
		return info;
	}

	public Map getListOfAvailableUOCFLVs() {
	
		Map<String, Map> filesMap = new HashMap<String, Map>();
		Map<String, Object> fileInfo;
		
		String[] urls = {};
		String[] info = {};
		if (doc != null) {
			urls = this.getUrls();
			info = this.getInfos(); 			
		}
			
		for (int i=0; i<urls.length; i++) {
			fileInfo = new HashMap<String, Object>();
			fileInfo.put("urlvideo", urls[i]);
			fileInfo.put("infovideo", info[i]);
			filesMap.put(String.valueOf(i), fileInfo);
		}
		return filesMap;
	}
	
	/**
	 * Make XML file.
	 * @throws TransformerException
	 */
	public void printXML() throws TransformerException {
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
	 * Create video tag.
	 * @return
	 */
	public void createTagVideo(Map<String,Map> mapInfo) {
		for (int i=0; i<mapInfo.size(); i++) {
			Map<String, Object> mapInfo2 = new HashMap<String, Object>();
			mapInfo2 = (HashMap)mapInfo.get(String.valueOf(i));
			String url = (String)mapInfo2.get("url-video");
			String info = (String)mapInfo2.get("info-video");
			//if(doc!=null){  
				Element root = doc.getDocumentElement(); 
				Element videoElement = doc.createElement(VIDEO_TAG);
				Element urlElement = doc.createElement(URL_VIDEO_TAG);
				Element infoElement = doc.createElement(INFO_VIDEO_TAG);
				Text urlTextNode = doc.createTextNode(url);
				Text infoTextNode = doc.createTextNode(info);
				urlElement.appendChild(urlTextNode);
				infoElement.appendChild(infoTextNode);
				// add url video
				videoElement.appendChild(urlElement);
				// add info video
				videoElement.appendChild(infoElement);
				root.appendChild(videoElement);
			//}
		}
	}

}
