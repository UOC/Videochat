package org.uoc.red5.videoconference.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class VideoConferenceLanguage {

	public static final String DEFAULT_PROPERTIES_MESSAGE = "org.uoc.red5.videoconference.resources.messages.VCmessages";
	private ResourceBundle bundle = null;

	
	public VideoConferenceLanguage() {
		init("");
	}
	
	public VideoConferenceLanguage(String language) {
		init(language);
	}
	
	
	public void init(String language) {
		try {
			this.bundle = ResourceBundle.getBundle(DEFAULT_PROPERTIES_MESSAGE,new Locale(language));			
		} catch(MissingResourceException me) {
			this.bundle = ResourceBundle.getBundle(DEFAULT_PROPERTIES_MESSAGE,Locale.US);
		}
	}
	
	public String getMessage(String key) {
		if (bundle != null)
			return bundle.getString(key);
		else
			return "";
	}
	
	
}
