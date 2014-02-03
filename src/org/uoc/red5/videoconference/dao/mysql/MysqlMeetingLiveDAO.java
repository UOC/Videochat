package org.uoc.red5.videoconference.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uoc.red5.videoconference.dao.MeetingDAOException;
import org.uoc.red5.videoconference.pojo.Meeting;
import org.uoc.red5.videoconference.pojo.MeetingLive;
import org.uoc.red5.videoconference.servlets.Front;

public class MysqlMeetingLiveDAO implements org.uoc.red5.videoconference.dao.MeetingLiveDAO { 

	protected static Log log = LogFactory.getLog(Front.class.getName());

	public static String INSERT_LIVE_MEETING_STATEMENT
	  = "Insert into live_meeting (id_meeting, id_context, n_participants, blocked)"
	  + " values (?, ?, ?, ?)";
	public static String FIND_BY_ID_LIVE_MEETING_STATEMENT
	  = "select id_meeting, id_context, n_participants, blocked, recording, participants from live_meeting where id_meeting=?"; 
	public static String UPDATE_LIVE_MEETING_STATEMENT_1
	  = "update live_meeting set n_participants = ? where id_meeting = ?";
	public static String UPDATE_LIVE_MEETING_STATEMENT_2
	  = "update live_meeting set blocked = ? where id_meeting = ?";
	public static String UPDATE_LIVE_MEETING_STATEMENT_3
	  = "update live_meeting set recording = ? where id_meeting = ?";
	public static String UPDATE_LIVE_MEETING_STATEMENT_4
	  = "update live_meeting set participants = ? where id_meeting = ?";
	public static String DELETE_LIVE_MEETING_STATEMENT = "delete from live_meeting where id_meeting = ?";
	public static String COUNT_LIVE_MEETING_STATEMENT = "select count(*) from live_meeting where participants is not null and participants <> ''";
	
	
	public Meeting[] findByContext(
			String context) throws MeetingDAOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public MeetingLive findByPrimaryKey(String id) throws MeetingDAOException {
		
		MeetingLive meetingLive = null;
		PreparedStatement pstmt;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(FIND_BY_ID_LIVE_MEETING_STATEMENT);
			pstmt.setString(1, id);
			// obtain the meeting
		    ResultSet rs = pstmt.executeQuery();
		    if (rs.next()) {
		    	meetingLive = new MeetingLive();
		    	meetingLive.setId(rs.getString("id_meeting"));
		    	meetingLive.setId_context(rs.getString("id_context"));
		    	meetingLive.setN_participants(rs.getInt("n_participants"));
		    	meetingLive.setBlocked(rs.getBoolean("blocked"));
		    	meetingLive.setRecording(rs.getBoolean("recording"));
		    	meetingLive.setParticipants(rs.getString("participants"));
		    }
		} catch (SQLException e) {
			throw new MeetingDAOException("error en findByPrimaryKey en live_meeting table " + e.getMessage());
		} finally {
			try {
				if (connection!=null) {
					connection.close();
				}
			}
			catch (SQLException e){
				log.error("Error closing connection ", e);
			}
		}
		return meetingLive;
	}

	public void insert(MeetingLive meetingLive) throws MeetingDAOException {
		
		PreparedStatement pstmt;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(INSERT_LIVE_MEETING_STATEMENT);
			pstmt.setString(1, meetingLive.getId());
			pstmt.setString(2, meetingLive.getId_context());
			pstmt.setInt(3, meetingLive.getN_participants());
			pstmt.setBoolean(4, meetingLive.isBlocked());
			// Insert the row
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MeetingDAOException("error en insert en live_meeting table " + e.getMessage());
		} finally {
			try {
				if (connection!=null) {
					connection.close();
				}
			}
			catch (SQLException e){
				log.error("Error closing connection ", e);
			}
		}
	}

	public void updateNumParticipants(String id_meeting, int n_participants) throws MeetingDAOException {

		PreparedStatement pstmt;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(UPDATE_LIVE_MEETING_STATEMENT_1);
			pstmt.setInt(1, n_participants);
			pstmt.setString(2, id_meeting);
			// Update the row
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MeetingDAOException("error en updateNumParticipants en live_meeting table " + e.getMessage());
		} finally {
			try {
				if (connection!=null) {
					connection.close();
				}
			}
			catch (SQLException e){
				log.error("Error closing connection ", e);
			}
		}
	}

	public void updateBlocked(String id_meeting, boolean oper) throws MeetingDAOException {

		PreparedStatement pstmt;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(UPDATE_LIVE_MEETING_STATEMENT_2);
			pstmt.setBoolean(1, oper);
			pstmt.setString(2, id_meeting);
			// Update the row
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MeetingDAOException("error en updateBlocked en live_meeting table " + e.getMessage());
		} finally {
			try {
				if (connection!=null) {
					connection.close();
				}
			}
			catch (SQLException e){
				log.error("Error closing connection ", e);
			}
		}
	}

	public void updateRecording(String id_meeting, boolean oper) throws MeetingDAOException {

		PreparedStatement pstmt;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(UPDATE_LIVE_MEETING_STATEMENT_3);
			pstmt.setBoolean(1, oper);
			pstmt.setString(2, id_meeting);
			// Update the row
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MeetingDAOException("error en updaterecording en live_meeting table " + e.getMessage());
		} finally {
			try {
				if (connection!=null) {
					connection.close();
				}
			}
			catch (SQLException e){
				log.error("Error closing connection ", e);
			}
		}
	}

	public void updateParticipants(String id_meeting, String participants) throws MeetingDAOException {

		PreparedStatement pstmt;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(UPDATE_LIVE_MEETING_STATEMENT_4);
			pstmt.setString(1, participants);
			pstmt.setString(2, id_meeting);
			// Update the row
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MeetingDAOException("error en updateParticipants en live_meeting table " + e.getMessage());
		} finally {
			try {
				if (connection!=null) {
					connection.close();
				}
			}
			catch (SQLException e){
				log.error("Error closing connection ", e);
			}
		}

	}

	public void delete(String id_meeting) throws MeetingDAOException {

		PreparedStatement pstmt;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(DELETE_LIVE_MEETING_STATEMENT);
			pstmt.setString(1, id_meeting);
			// Update the row
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MeetingDAOException("error en delete en live_meeting table " + e.getMessage());
		} finally {
			try {
				if (connection!=null) {
					connection.close();
				}
			}
			catch (SQLException e){
				log.error("Error closing connection ", e);
			}
		}
	}

	public int countLiveMeetings() throws MeetingDAOException {

		int resultat = 0;
		PreparedStatement pstmt;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(COUNT_LIVE_MEETING_STATEMENT);
			ResultSet rs = pstmt.executeQuery();
		    if (rs.next()) {
		    	resultat = rs.getInt(1);
		    }	
		} catch (SQLException e) {
			throw new MeetingDAOException("error en countLiveMeetings en live_meeting table " + e.getMessage());
		} finally {
			try {
				if (connection!=null) {
					connection.close();
				}
			}
			catch (SQLException e){
				log.error("Error closing connection ", e);
			}
		}
		return resultat;
	}


}