package org.uoc.red5.videoconference.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uoc.red5.videoconference.dao.MeetingDAOException;
import org.uoc.red5.videoconference.pojo.Meeting;
import org.uoc.red5.videoconference.servlets.Front;
import org.uoc.red5.videoconference.utils.Utils;

public class MysqlMeetingDAO implements org.uoc.red5.videoconference.dao.MeetingDAO {

	protected static Log log = LogFactory.getLog(Front.class.getName());

	public String INSERT_MEETING_STATEMENT
	  = "Insert into meeting (id, id_context, topic, description, path, finish, participants, begin, end)"
	  + " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public String FIND_BY_ID_MEETING_STATEMENT
	  = "select id, id_context, topic, description, path, finish, participants, begin, end from meeting where id=?"; 
	public String UPDATE_MEETING_STATEMENT_1
	  = "update meeting set begin = ? where id = ?";
	public String UPDATE_MEETING_STATEMENT_2
	  = "update meeting set begin = ? , end = ? where id = ?";
	public String UPDATE_MEETING_STATEMENT_3
	  = "update meeting set participants = ? where id = ?";
	public String UPDATE_MEETING_STATEMENT_4
	  = "update meeting set finish = ? , begin = ? , end = ? where id = ?";
	public String UPDATE_MEETING_STATEMENT_5
	  = "update meeting set finish = ? , begin = ? , end = ? , participants = ? where id = ?";
	public String UPDATE_MEETING_STATEMENT_6
	  = "update meeting set begin = ? , end = ? where id = ?";
	public String UPDATE_MEETING_STATEMENT_7
	  = "update meeting set topic = ? , description = ? where id = ?";
	public String FIND_MEETING_BY_SEARCH_STATEMENT
	  = "select id, id_context, topic, description, path, finish, participants, begin, end from meeting where id_context=? and finish=true"; 
	public String EXIST_MEETING_STATEMENT
	  = "select id from meeting where id = ? and id_context=?"; 

	
	public void delete(String id) throws MeetingDAOException {
		
	}

	public Meeting[] findByContext(
			String context) throws MeetingDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Cerca els meetings d'un context (aula) que acompleixen unes certes condicions
	 */
	public List<Meeting> findBySearch(String context, String topic, String participants, String dataInici, String dataFinal) throws MeetingDAOException {

		Meeting meeting = null;
		List<Meeting> meetings = new ArrayList<Meeting>();
		
		java.sql.Timestamp di = null;
		java.sql.Timestamp df = null;
		
		// participants
		String cadena = "";
		if (participants != null && !participants.equals("")) {
			String participants_[] = Utils.getParticipants(participants);
			String cadenalike = "participants like ";
			for (int i=0; i<participants_.length; i++) {
				if (i==0) cadena = "'%"+participants_[i]+"%'";
				else cadena = cadena + " or " + "'%"+participants_[i]+"%'";
			}
			cadena = cadenalike + cadena;
		}
		
		// formateja data inici i final
		try {
			if (dataInici != null && !dataInici.equals("") && dataFinal != null && !dataFinal.equals("")) {
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				java.util.Date date = (java.util.Date)formatter.parse(dataInici);
				long time = date.getTime();
				di = new java.sql.Timestamp(time);
				date = (java.util.Date)formatter.parse(dataFinal);
				time = date.getTime();
				df = new java.sql.Timestamp(time);
			}
		} catch(ParseException pe) {
			throw new MeetingDAOException("error en findBySearch en meeting table: " + pe.getMessage());
		}
			
		Connection connection = null;
		// monta la cadena de cerca i fa la cerca
		if (context != null) {
			PreparedStatement pstmt;
			try {
				if (topic != null && topic != "")
					FIND_MEETING_BY_SEARCH_STATEMENT = FIND_MEETING_BY_SEARCH_STATEMENT + " and topic like '%" + topic +"%'";
				if (participants != null && participants != "")
					FIND_MEETING_BY_SEARCH_STATEMENT = FIND_MEETING_BY_SEARCH_STATEMENT + " and " + cadena;
				if (dataInici != null && dataInici != "" && dataFinal != null && dataFinal != "")
					FIND_MEETING_BY_SEARCH_STATEMENT = FIND_MEETING_BY_SEARCH_STATEMENT + " and begin >= ?" + " and end <= ?";
				
				FIND_MEETING_BY_SEARCH_STATEMENT = FIND_MEETING_BY_SEARCH_STATEMENT + " order by begin desc, topic";
				
				connection = MysqlDAOFactory.createConnection();
				pstmt = connection.prepareStatement(FIND_MEETING_BY_SEARCH_STATEMENT);
				pstmt.setString(1, context);
				if (di != null && df != null) {
					pstmt.setTimestamp(2, di);
					pstmt.setTimestamp(3, df);
				}
				// obtain the meeting
			    ResultSet rs = pstmt.executeQuery();
			    while (rs.next()) {
			    	meeting = new Meeting();
			    	meeting.setId(rs.getString("id"));
			    	meeting.setId_context(rs.getString("id_context"));
			    	meeting.setTopic(rs.getString("topic"));
			    	meeting.setDescription(rs.getString("description"));
			    	meeting.setBegin(rs.getTimestamp("begin"));
			    	meeting.setEnd(rs.getTimestamp("end"));
			    	meeting.setFinish(rs.getBoolean("finish"));
			    	meeting.setParticipants(rs.getString("participants"));
			    	meeting.setPath(rs.getString("path"));
			    	meetings.add(meeting);
			    }
			} catch (SQLException e) {
				throw new MeetingDAOException("error en findBySearch en meeting table: " + e.getMessage());
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
		return meetings;
	}

	public boolean exist(String id, String context) throws MeetingDAOException {
		
		PreparedStatement pstmt;
		boolean exist = false;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(EXIST_MEETING_STATEMENT);
			pstmt.setString(1, id);
			pstmt.setString(2, context);
			// obtain the meeting
		    ResultSet rs = pstmt.executeQuery();
		    exist = rs.next();
		} catch (SQLException e) {
			throw new MeetingDAOException("error en exist en meeting table " + e.getMessage());
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
		return exist;
	}

	public Meeting findByPrimaryKey(String id) throws MeetingDAOException {
		
		Meeting meeting = null;
		PreparedStatement pstmt;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(FIND_BY_ID_MEETING_STATEMENT);
			pstmt.setString(1, id);
			// obtain the meeting
		    ResultSet rs = pstmt.executeQuery();
		    if (rs.next()) {
		    	meeting = new Meeting();
		    	meeting.setId(rs.getString("id"));
		    	meeting.setId_context(rs.getString("id_context"));
		    	meeting.setTopic(rs.getString("topic"));
		    	meeting.setDescription(rs.getString("description"));
		    	meeting.setBegin(rs.getTimestamp("begin"));
		    	meeting.setEnd(rs.getTimestamp("end"));
		    	meeting.setFinish(rs.getBoolean("finish"));
		    	meeting.setParticipants(rs.getString("participants"));
		    	meeting.setPath(rs.getString("path"));
		    }
		} catch (SQLException e) {
			throw new MeetingDAOException("error en findByPrimaryKey en meeting table " + e.getMessage());
		} finally {
			try {
				if (connection !=null) {
					connection.close();
				}
			}
			catch (SQLException e){
				log.error("Error closing connection ", e);
			}
		}
		return meeting;
	}

	public void insert(Meeting meeting) throws MeetingDAOException {
		
		PreparedStatement pstmt;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(INSERT_MEETING_STATEMENT);
			pstmt.setString(1, meeting.getId());
			pstmt.setString(2, meeting.getId_context());
			pstmt.setString(3, meeting.getTopic());
			pstmt.setString(4, meeting.getDescription());
			pstmt.setString(5, meeting.getPath());
			pstmt.setBoolean(6, meeting.isFinish());
			pstmt.setString(7, null);
			pstmt.setTimestamp(8, null);
			pstmt.setTimestamp(9, null);
			// Insert the row
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MeetingDAOException("error en insert en meeting table " + e.getMessage());
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

	public void updateDataInici(String id_meeting) throws MeetingDAOException {

		java.util.Date today = new java.util.Date();
		PreparedStatement pstmt;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(UPDATE_MEETING_STATEMENT_1);
			pstmt.setTimestamp(1, new java.sql.Timestamp(today.getTime()));
			pstmt.setString(2, id_meeting);
			// Update the row
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MeetingDAOException("error en update en meeting table " + e.getMessage());
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

	public void updateDataFinal(String id_meeting, java.util.Date dataInici) throws MeetingDAOException {

		java.util.Date today = new java.util.Date();
		PreparedStatement pstmt;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(UPDATE_MEETING_STATEMENT_2);
			pstmt.setTimestamp(1, new java.sql.Timestamp(dataInici.getTime()));			
			pstmt.setTimestamp(2, new java.sql.Timestamp(today.getTime()));
			pstmt.setString(3, id_meeting);
			// Update the row
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MeetingDAOException("error en update en meeting table " + e.getMessage());
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
			pstmt = connection.prepareStatement(UPDATE_MEETING_STATEMENT_3);
			pstmt.setString(1, participants);
			pstmt.setString(2, id_meeting);
			// Update the row
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MeetingDAOException("error en update en meeting table " + e.getMessage());
		} finally {
			try {
				if (connection !=null) {
					connection.close();
				}
			}
			catch (SQLException e){
				log.error("Error closing connection ", e);
			}
		}
	}

	public void updateFinish(String id_meeting) throws MeetingDAOException {

		PreparedStatement pstmt;
		Connection connection = null;
		try {
			Meeting meeting = findByPrimaryKey(id_meeting);
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(UPDATE_MEETING_STATEMENT_4);
			pstmt.setBoolean(1, true);
			pstmt.setTimestamp(2, new java.sql.Timestamp(meeting.getBegin().getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(meeting.getEnd().getTime()));
			pstmt.setString(4, id_meeting);
			// Update the row
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MeetingDAOException("error en update en meeting table " + e.getMessage());
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

	public void updateCleanRow(String id_meeting) throws MeetingDAOException {

		PreparedStatement pstmt;
		java.util.Date today = new java.util.Date();
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(UPDATE_MEETING_STATEMENT_5);
			pstmt.setBoolean(1, false);
			pstmt.setTimestamp(2, new java.sql.Timestamp(today.getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(today.getTime()));
			pstmt.setString(4, "");
			pstmt.setString(5, id_meeting);
			// Update the row
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MeetingDAOException("error en update en meeting table " + e.getMessage());
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

	public void updateDataIniciFinal(String id_meeting, java.util.Date dataInici, java.util.Date dataFinal) throws MeetingDAOException {

		PreparedStatement pstmt;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(UPDATE_MEETING_STATEMENT_6);
			pstmt.setTimestamp(1, new java.sql.Timestamp(dataInici.getTime()));
			pstmt.setTimestamp(2, new java.sql.Timestamp(dataFinal.getTime()));
			pstmt.setString(3, id_meeting);
			// Update the row
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MeetingDAOException("error en update en meeting table " + e.getMessage());
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

	public void updateTopic_Description(String id_meeting, String topic, String description) throws MeetingDAOException {

		PreparedStatement pstmt;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(UPDATE_MEETING_STATEMENT_7);
			pstmt.setString(1, topic);
			pstmt.setString(2, description);
			pstmt.setString(3, id_meeting);
			// Update the row
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MeetingDAOException("error en update en meeting table " + e.getMessage());
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

}