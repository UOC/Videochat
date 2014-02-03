package org.uoc.red5.videoconference.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uoc.red5.videoconference.dao.MeetingDAOException;
import org.uoc.red5.videoconference.pojo.RemoteApplication;
import org.uoc.red5.videoconference.servlets.Front;

public class MysqlRemoteApplicationDAO implements org.uoc.red5.videoconference.dao.RemoteApplicationDAO {

	protected static Log log = LogFactory.getLog(Front.class.getName());

	public String INSERT_REMOTEAPPLICATION_STATEMENT
	  = "Insert into remote_application (toolurl, name, description, resourcekey, launchinpopup, debugmode, registered, updated)"
	  + " values (?, ?, ?, ?, ?, ?, ?, ?)";
	public String UPDATE_REMOTEAPPLICATION_STATEMENT
	  = "UPDATE remote_application SET toolurl = ?, name = ?, description = ?, resourcekey = ?, launchinpopup = ?, debugmode = ?, updated = ? "
	  + " WHERE id = ? ";
	public String FIND_BY_ID_REMOTEAPPLICATION_STATEMENT
	  = "select id, toolurl, name, description, launchinpopup, debugmode, registered, updated from remote_application where id=?"; 
	public String FIND_REMOTEAPPLICATION_BY_SEARCH_STATEMENT
	  = "select id, toolurl, name, description, launchinpopup, debugmode, registered, updated from remote_application where id not in (select id_tool from remote_disabled_application_context where id_context = ?)"; 
	public String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";

	
	public void delete(String id) throws MeetingDAOException {
		
	}

	/**
	 * Cerca les eines
	 */
	public List<RemoteApplication> findBySearch(String context, String name) throws MeetingDAOException {

		RemoteApplication app = null;
		List<RemoteApplication> remotes = new ArrayList<RemoteApplication>();
		

		log.info("context ------> " + context);
		log.info("name ------> " + name);
		
		// monta la cadena de cerca i fa la cerca
		if (context != null) {
			PreparedStatement pstmt;
			Connection connection = null;
			try {
				if (name != null && !"".equals(name))
					FIND_REMOTEAPPLICATION_BY_SEARCH_STATEMENT = FIND_REMOTEAPPLICATION_BY_SEARCH_STATEMENT + " and name like ? ";
				
				FIND_REMOTEAPPLICATION_BY_SEARCH_STATEMENT = FIND_REMOTEAPPLICATION_BY_SEARCH_STATEMENT + " order by registered desc, name";
				log.info("FIND_MEETING_BY_SEARCH_STATEMENT ------> " + FIND_REMOTEAPPLICATION_BY_SEARCH_STATEMENT);
				
				connection = MysqlDAOFactory.createConnection();
				pstmt = connection.prepareStatement(FIND_REMOTEAPPLICATION_BY_SEARCH_STATEMENT);
				int pos = 1;
				pstmt.setString(pos++, context);
				if (name != null && !"".equals(name))
					pstmt.setString(pos++, "%" + name +"%");
					
				// obtain the meeting
			    ResultSet rs = pstmt.executeQuery();
			    while (rs.next()) {
			    	app = createAppFromRS(rs);
			    	remotes.add(app);
			    }
			} catch (SQLException e) {
				throw new MeetingDAOException("error en findBySearch en LTIApplication table: " + e.getMessage());
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
		return remotes;
	}

	/**
	 * Retorna un obj LTIApplication from ResultSet
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private RemoteApplication createAppFromRS(ResultSet rs) throws SQLException {
		RemoteApplication app = new RemoteApplication();
    	app.setId(rs.getInt("id"));
    	app.setToolurl(rs.getString("toolurl"));
    	app.setName(rs.getString("name"));
    	app.setDescription(rs.getString("description"));
    	app.setLaunchinpopup(rs.getShort("launchinpopup"));
    	app.setDebugmode(rs.getShort("debugmode")==1?true:false);
    	app.setRegistered(rs.getTimestamp("registered"));
    	app.setUpdated(rs.getTimestamp("updated"));
    	return app;
	}
	
	/**
	 * Busca un objecte per ID
	 */
	public RemoteApplication findByPrimaryKey(int id) throws MeetingDAOException {
		
		RemoteApplication app = null;
		PreparedStatement pstmt;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(FIND_BY_ID_REMOTEAPPLICATION_STATEMENT);
			pstmt.setInt(1, id);
			// obtain the meeting
		    ResultSet rs = pstmt.executeQuery();
		    if (rs.next()) {
		    	app = createAppFromRS(rs);
		    }
		} catch (SQLException e) {
			throw new MeetingDAOException("error en findByPrimaryKey en RemoteApplication table " + e.getMessage());
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
		return app;
	}

	/**
	 * Inserta un objecte a la BD
	 */
	public void insert(RemoteApplication app) throws MeetingDAOException {
		
		PreparedStatement pstmt;
		Connection connection = null;
		try {
			long time = new Date().getTime();
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(INSERT_REMOTEAPPLICATION_STATEMENT);
			pstmt.setString(1, app.getToolurl());
			pstmt.setString(2, app.getName());
			pstmt.setString(3, app.getDescription());
			pstmt.setShort(4, app.getLaunchinpopup());
			pstmt.setShort(5, (short)(app.isDebugmode()?1:0));
			pstmt.setTimestamp(6, new java.sql.Timestamp (time));
			pstmt.setTimestamp(7, new java.sql.Timestamp (time));
			// Insert the row
		    if (pstmt.executeUpdate()>0) {
		    	app.setId(getLastInsertId());
		    }
		} catch (SQLException e) {
			throw new MeetingDAOException("error en insert en remote_application table " + e.getMessage());
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
	
	/**
	 * Get thge las inserted id
	 * @return
	 * @throws MeetingDAOException
	 */
	private int getLastInsertId() throws MeetingDAOException {
		
		int id = -1;
		Statement stmt;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			stmt = connection.createStatement();
			// obtain the meeting
		    ResultSet rs = stmt.executeQuery(SELECT_LAST_INSERT_ID);
		    if (rs.next()) {
		    	id = rs.getInt(1);
		    }
		} catch (SQLException e) {
			throw new MeetingDAOException("error en getLastInsertId en RemoteApplication table " + e.getMessage());
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
		return id;
	}

	/**
	 * Update LTIApplication
	 */
	public void update(RemoteApplication app) throws MeetingDAOException {
		
		PreparedStatement pstmt;
		Connection connection = null;
		try {
			long time = new Date().getTime();
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(UPDATE_REMOTEAPPLICATION_STATEMENT);
			pstmt.setString(1, app.getToolurl());
			pstmt.setString(2, app.getName());
			pstmt.setString(3, app.getDescription());
			pstmt.setShort(4, app.getLaunchinpopup());
			pstmt.setShort(5, (short)(app.isDebugmode()?1:0));
			pstmt.setTimestamp(6, new java.sql.Timestamp (time));
			pstmt.setInt(7, app.getId());

			// update the row
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MeetingDAOException("error en update en remote_application table " + e.getMessage());
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