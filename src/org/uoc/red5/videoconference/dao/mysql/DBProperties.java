package org.uoc.red5.videoconference.dao.mysql;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public abstract class DBProperties extends Properties {
	
	private static final long serialVersionUID = -4077377200640342480L;

	public static String getPropertyDB(String key) {
		String value = "";
		Properties dbProps = new Properties();
		try {
			dbProps.load(DBProperties.class.getResourceAsStream("db.properties"));
			value = dbProps.getProperty(key);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}
}
