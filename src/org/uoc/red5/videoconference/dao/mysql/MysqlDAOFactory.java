package org.uoc.red5.videoconference.dao.mysql;

import java.sql.Connection;
import java.sql.DriverManager;

import org.uoc.red5.videoconference.dao.DAOFactory;
import org.uoc.red5.videoconference.dao.LTIApplicationDAO;
import org.uoc.red5.videoconference.dao.MeetingDAO;
import org.uoc.red5.videoconference.dao.MeetingLiveDAO;
import org.uoc.red5.videoconference.dao.RemoteApplicationDAO;

public class MysqlDAOFactory extends DAOFactory{

    public static final String url = DBProperties.getPropertyDB("db.url");
    public static final String dbName = DBProperties.getPropertyDB("db.name");
    public static final String driver = DBProperties.getPropertyDB("db.driver");
    public static final String userName = DBProperties.getPropertyDB("db.username"); 
    public static final String password = DBProperties.getPropertyDB("db.password");
	
	public MysqlDAOFactory() {
		super();
	}
	
	// method to create Cloudscape connections
	public static Connection createConnection() {
		// Use DRIVER and DBURL to create a connection
	    // Recommend connection pool implementation/usage
		Connection con = null;

	    try {
	      Class.forName(driver).newInstance();
	      con = DriverManager.getConnection(url+dbName, userName, password);
	      
	      //if(!con.isClosed())
	        //System.out.println("Successfully connected to " +
	          //"MySQL server using TCP/IP...");

	    } catch(Exception e) {
	      System.err.println("Exception: " + e.getMessage());
	      e.printStackTrace();
	    } 
	    return con;
	}
	
	public MeetingDAO getMeetingDAO() {
	    return new MysqlMeetingDAO();
	}

	public MeetingLiveDAO getMeetingLiveDAO() {
	    return new MysqlMeetingLiveDAO();
	}

	public LTIApplicationDAO getLTIApplicationDAO() {
	    return new MysqlLTIApplicationDAO();
	}
	
	public RemoteApplicationDAO getRemoteApplicationDAO() {
	    return new MysqlRemoteApplicationDAO();
	}

}
