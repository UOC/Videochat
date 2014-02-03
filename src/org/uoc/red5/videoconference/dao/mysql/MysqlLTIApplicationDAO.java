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
import edu.uoc.lti.LTIApplication;
import org.uoc.red5.videoconference.servlets.Front;

public class MysqlLTIApplicationDAO implements org.uoc.red5.videoconference.dao.LTIApplicationDAO {

	protected static Log log = LogFactory.getLog(Front.class.getName());

	public String INSERT_LTIAPPLICATION_STATEMENT
	  = "Insert into lti_application (toolurl, name, description, resourcekey, password, preferheight, sendname, sendemailaddr, acceptgrades, allowroster, allowsetting, customparameters, allowinstructorcustom, organizationid, organizationurl, launchinpopup, debugmode, registered, updated)"
	  + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public String UPDATE_LTIAPPLICATION_STATEMENT
	  = "UPDATE lti_application SET toolurl = ?, name = ?, description = ?, resourcekey = ?, password = ?, preferheight = ?, sendname = ?, sendemailaddr = ?, acceptgrades = ?, allowroster = ?, allowsetting = ?, customparameters = ?, allowinstructorcustom = ?, organizationid = ?, organizationurl = ?, launchinpopup = ?, debugmode = ?, updated = ? "
	  + " WHERE id = ? ";
	public String FIND_BY_ID_LTIAPPLICATION_STATEMENT
	  = "select id, toolurl, name, description, resourcekey, password, preferheight, sendname, sendemailaddr, acceptgrades, allowroster, allowsetting, customparameters, allowinstructorcustom, organizationid, organizationurl, launchinpopup, debugmode, registered, updated from lti_application where id=?"; 
	public String FIND_LTIAPPLICATION_BY_SEARCH_STATEMENT
	  = "select id, toolurl, name, description, resourcekey, password, preferheight, sendname, sendemailaddr, acceptgrades, allowroster, allowsetting, customparameters, allowinstructorcustom, organizationid, organizationurl, launchinpopup, debugmode, registered, updated from lti_application where id not in (select id_tool from lti_disabled_application_context where id_context = ?)"; 
	public String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";

	
	public void delete(String id) throws MeetingDAOException {
		
	}

	/**
	 * Cerca les eines
	 */
	public List<LTIApplication> findBySearch(String context, String name) throws MeetingDAOException {

		Connection connection = null;
		LTIApplication lti = null;
		List<LTIApplication> ltis = new ArrayList<LTIApplication>();
		
		log.info("context ------> " + context);
		log.info("name ------> " + name);
		log.info("connection ------> " + connection);
		
		// monta la cadena de cerca i fa la cerca
		if (context != null) {
			PreparedStatement pstmt;
			try {
				if (name != null && !"".equals(name))
					FIND_LTIAPPLICATION_BY_SEARCH_STATEMENT = FIND_LTIAPPLICATION_BY_SEARCH_STATEMENT + " and name like ? ";
				
				FIND_LTIAPPLICATION_BY_SEARCH_STATEMENT = FIND_LTIAPPLICATION_BY_SEARCH_STATEMENT + " order by registered desc, name";
				log.info("FIND_MEETING_BY_SEARCH_STATEMENT ------> " + FIND_LTIAPPLICATION_BY_SEARCH_STATEMENT);
				
				connection = MysqlDAOFactory.createConnection();
				pstmt = connection.prepareStatement(FIND_LTIAPPLICATION_BY_SEARCH_STATEMENT);
				int pos = 1;
				pstmt.setString(pos++, context);
				if (name != null && !"".equals(name))
					pstmt.setString(pos++, "%" + name +"%");
					
				// obtain the meeting
			    ResultSet rs = pstmt.executeQuery();
			    while (rs.next()) {
			    	lti = createLTIFromRS(rs);
			    	ltis.add(lti);
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
		return ltis;
	}

	/**
	 * Retorna un obj LTIApplication from ResultSet
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private LTIApplication createLTIFromRS(ResultSet rs) throws SQLException {
		LTIApplication lti = new LTIApplication();
    	lti.setId(rs.getInt("id"));
    	lti.setToolurl(rs.getString("toolurl"));
    	lti.setName(rs.getString("name"));
    	lti.setDescription(rs.getString("description"));
    	lti.setResourcekey(rs.getString("resourcekey"));
    	lti.setPassword(rs.getString("password"));
    	lti.setPreferheight(rs.getString("preferheight"));
    	lti.setSendname(rs.getShort("sendname"));
    	lti.setSendemailaddr(rs.getShort("sendemailaddr"));
    	lti.setAcceptgrades(rs.getShort("acceptgrades"));
    	lti.setAllowroster(rs.getShort("allowroster"));
    	lti.setAllowsetting(rs.getShort("allowsetting"));
    	lti.setCustomparameters(rs.getString("customparameters"));
    	lti.setAllowinstructorcustom(rs.getShort("allowinstructorcustom"));
    	lti.setOrganizationid(rs.getString("organizationid"));
    	lti.setOrganizationurl(rs.getString("organizationurl"));
    	lti.setLaunchinpopup(rs.getShort("launchinpopup"));
    	lti.setDebugmode(rs.getShort("debugmode")==1?true:false);
    	lti.setRegistered(rs.getTimestamp("registered"));
    	lti.setUpdated(rs.getTimestamp("updated"));
    	return lti;
	}
	
	/**
	 * Busca un objecte per ID
	 */
	public LTIApplication findByPrimaryKey(int id) throws MeetingDAOException {
		
		LTIApplication lti = null;
		PreparedStatement pstmt;
		Connection connection = null;
		try {
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(FIND_BY_ID_LTIAPPLICATION_STATEMENT);
			pstmt.setInt(1, id);
			// obtain the meeting
		    ResultSet rs = pstmt.executeQuery();
		    if (rs.next()) {
		    	lti = createLTIFromRS(rs);
		    }
		} catch (SQLException e) {
			throw new MeetingDAOException("error en findByPrimaryKey en LTIApplication table " + e.getMessage());
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
		return lti;
	}

	/**
	 * Inserta un objecte a la BD
	 */
	public void insert(LTIApplication lti) throws MeetingDAOException {
		
		PreparedStatement pstmt;
		Connection connection = null;
		try {
			long time = new Date().getTime();
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(INSERT_LTIAPPLICATION_STATEMENT);
			pstmt.setString(1, lti.getToolurl());
			pstmt.setString(2, lti.getName());
			pstmt.setString(3, lti.getDescription());
			pstmt.setString(4, lti.getResourcekey());
			pstmt.setString(5, lti.getPassword());
			pstmt.setString(6, lti.getPreferheight());
			pstmt.setShort(7, lti.getSendname());
			pstmt.setShort(8, lti.getSendemailaddr());
			pstmt.setShort(9, lti.getAcceptgrades());
			pstmt.setShort(10, lti.getAllowroster());
			pstmt.setShort(11, lti.getAllowsetting());
			pstmt.setString(12, lti.getCustomparameters());
			pstmt.setShort(13, lti.getAllowinstructorcustom());
			pstmt.setString(14, lti.getOrganizationid());
			pstmt.setString(15, lti.getOrganizationurl());
			pstmt.setShort(16, lti.getLaunchinpopup());
			pstmt.setShort(17, (short)(lti.isDebugmode()?1:0));
			pstmt.setTimestamp(18, new java.sql.Timestamp (time));
			pstmt.setTimestamp(19, new java.sql.Timestamp (time));
			// Insert the row
		    if (pstmt.executeUpdate()>0) {
		    	lti.setId(getLastInsertId());
		    }
		} catch (SQLException e) {
			throw new MeetingDAOException("error en insert en lti_application table " + e.getMessage());
		} finally {
			try {
		    connection.close();
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
			throw new MeetingDAOException("error en getLastInsertId en LTIApplication table " + e.getMessage());
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
	public void update(LTIApplication lti) throws MeetingDAOException {
		
		PreparedStatement pstmt;
		Connection connection = null;
		try {
			long time = new Date().getTime();
			connection = MysqlDAOFactory.createConnection();
			pstmt = connection.prepareStatement(UPDATE_LTIAPPLICATION_STATEMENT);
			pstmt.setString(1, lti.getToolurl());
			pstmt.setString(2, lti.getName());
			pstmt.setString(3, lti.getDescription());
			pstmt.setString(4, lti.getResourcekey());
			pstmt.setString(5, lti.getPassword());
			pstmt.setString(6, lti.getPreferheight());
			pstmt.setShort(7, lti.getSendname());
			pstmt.setShort(8, lti.getSendemailaddr());
			pstmt.setShort(9, lti.getAcceptgrades());
			pstmt.setShort(10, lti.getAllowroster());
			pstmt.setShort(11, lti.getAllowsetting());
			pstmt.setString(12, lti.getCustomparameters());
			pstmt.setShort(13, lti.getAllowinstructorcustom());
			pstmt.setString(14, lti.getOrganizationid());
			pstmt.setString(15, lti.getOrganizationurl());
			pstmt.setShort(16, lti.getLaunchinpopup());
			pstmt.setShort(17, (short)(lti.isDebugmode()?1:0));
			pstmt.setTimestamp(18, new java.sql.Timestamp (time));
			pstmt.setInt(19, lti.getId());

			// update the row
		    pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MeetingDAOException("error en insert en lti_application table " + e.getMessage());
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
}