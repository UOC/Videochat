/*
    Based in rating - Rating: an example LTI tool provider
    Copyright (C) 2013  Stephen P Vickers

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

    Contact: stephen@spvsoftwareproducts.com

    Version history:
      1.0.00   4-Jan-13  Initial release
      1.0.01  17-Jan-13  Minor update
      1.0.02  13-Apr-13
      1.1.00  18-Jun-13
*/

package org.oscelot.lti.tp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.oscelot.lti.tp.DataConnector;


/**
 * This class represents a connection to a database.
 *
 * @author      Stephen P Vickers
 * @version     1.0 (4-Jan-13)
 */
public class Db {

  private Connection connection = null;

/**
 * Class constructor.
 * <p>
 * Throws an SQLException if a connection error occurs.
 *
 */
  public Db() throws SQLException {

    StringBuilder connectionString = new StringBuilder(Config.DB_NAME);
    if ((Config.DB_USERNAME.length() > 0) || (Config.DB_PASSWORD.length() > 0)) {
      connectionString.append("?");
      if (Config.DB_USERNAME.length() > 0) {
        connectionString.append("user=").append(Config.DB_USERNAME);
        if (Config.DB_PASSWORD.length() > 0) {
          connectionString.append("&");
        }
      }
      if (Config.DB_PASSWORD.length() > 0) {
        connectionString.append("password=").append(Config.DB_PASSWORD);
      }
    }
    try {
// Load driver
      Class.forName("com.mysql.jdbc.Driver").newInstance();
// Obtain connection
      this.connection = DriverManager.getConnection(connectionString.toString());
    } catch (ClassNotFoundException e) {
    } catch (InstantiationException e) {
    } catch (IllegalAccessException e) {
    }

  }

/**
 * Create any missing database tables (only for MySQL and SQLite databases)
 *
 * @return <code>true</code> if no error arises
 */
  public boolean initialise() {

    boolean ok;

    if (Config.DB_NAME.startsWith("jdbc:mysql:") || Config.DB_NAME.startsWith("jdbc:sqlite:")) {

      try {
        Statement stmt = this.connection.createStatement();

        StringBuilder sql = new StringBuilder();

        ok = stmt.executeUpdate(sql.toString()) == 0;


        if (ok) {
          sql.setLength(0);
          sql.append("CREATE TABLE IF NOT EXISTS ").append(Config.DB_TABLENAME_PREFIX).append(DataConnector.CONSUMER_TABLE_NAME).append(" (");
          sql.append("consumer_key varchar(50) NOT NULL,");
          sql.append("name varchar(45) NOT NULL,");
          sql.append("secret varchar(32) NOT NULL,");
          sql.append("lti_version varchar(12) DEFAULT NULL,");
          sql.append("consumer_name varchar(255) DEFAULT NULL,");
          sql.append("consumer_version varchar(255) DEFAULT NULL,");
          sql.append("consumer_guid varchar(255) DEFAULT NULL,");
          sql.append("css_path varchar(255) DEFAULT NULL,");
          sql.append("protected tinyint(1) NOT NULL,");
          sql.append("enabled tinyint(1) NOT NULL,");
          sql.append("enable_from datetime DEFAULT NULL,");
          sql.append("enable_until datetime DEFAULT NULL,");
          sql.append("last_access date DEFAULT NULL,");
          sql.append("created datetime NOT NULL,");
          sql.append("updated datetime NOT NULL,");
          sql.append("PRIMARY KEY (consumer_key))");
          ok = stmt.executeUpdate(sql.toString()) == 0;
        }

        if (ok) {
          sql.setLength(0);
          sql.append("CREATE TABLE IF NOT EXISTS ").append(Config.DB_TABLENAME_PREFIX).append(DataConnector.RESOURCE_LINK_TABLE_NAME).append(" (");
          sql.append("consumer_key varchar(50) NOT NULL,");
          sql.append("context_id varchar(50) NOT NULL,");
          sql.append("lti_context_id varchar(50) DEFAULT NULL,");
          sql.append("lti_resource_id varchar(50) DEFAULT NULL,");
          sql.append("title varchar(255) NOT NULL,");
          sql.append("settings text,");
          sql.append("primary_consumer_key varchar(50) DEFAULT NULL,");
          sql.append("primary_context_id varchar(50) DEFAULT NULL,");
          sql.append("share_approved tinyint(1) DEFAULT NULL,");
          sql.append("created datetime NOT NULL,");
          sql.append("updated datetime NOT NULL,");
          sql.append("PRIMARY KEY (consumer_key, context_id))");
          ok = stmt.executeUpdate(sql.toString()) == 0;
        }

        if (ok) {
          sql.setLength(0);
          sql.append("CREATE TABLE IF NOT EXISTS ").append(Config.DB_TABLENAME_PREFIX).append(DataConnector.USER_TABLE_NAME).append(" (");
          sql.append("consumer_key varchar(50) NOT NULL,");
          sql.append("context_id varchar(50) NOT NULL,");
          sql.append("user_id varchar(50) NOT NULL,");
          sql.append("lti_result_sourcedid varchar(255) NOT NULL,");
          sql.append("created datetime NOT NULL,");
          sql.append("updated datetime NOT NULL,");
          sql.append("PRIMARY KEY (consumer_key, context_id, user_id))");
          ok = stmt.executeUpdate(sql.toString()) == 0;
        }

        if (ok) {
          sql.setLength(0);
          sql.append("CREATE TABLE IF NOT EXISTS ").append(Config.DB_TABLENAME_PREFIX).append(DataConnector.NONCE_TABLE_NAME).append(" (");
          sql.append("consumer_key varchar(50) NOT NULL,");
          sql.append("value varchar(32) NOT NULL,");
          sql.append("expires datetime NOT NULL,");
          sql.append("PRIMARY KEY (consumer_key, value))");
          ok = stmt.executeUpdate(sql.toString()) == 0;
        }

        if (ok) {
          sql.setLength(0);
          sql.append("CREATE TABLE IF NOT EXISTS ").append(Config.DB_TABLENAME_PREFIX).append(DataConnector.RESOURCE_LINK_SHARE_KEY_TABLE_NAME).append(" (");
          sql.append("share_key_id varchar(32) NOT NULL,");
          sql.append("primary_consumer_key varchar(50) NOT NULL,");
          sql.append("primary_context_id varchar(50) NOT NULL,");
          sql.append("auto_approve tinyint(1) NOT NULL,");
          sql.append("expires datetime NOT NULL,");
          sql.append("PRIMARY KEY (share_key_id))");
          ok = stmt.executeUpdate(sql.toString()) == 0;
        }

      } catch (SQLException e) {
        ok = false;
      }

    } else {

      ok = true;  // always return TRUE for other database types

    }

    return ok;

  }

/**
 * Returns the database connection.
 *
 * @return database connection instance
 */
  public Connection getConnection() {
    return this.connection;
  }

}
