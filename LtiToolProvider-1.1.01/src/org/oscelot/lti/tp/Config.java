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

/*
 * This page contains the configuration settings for the application.
 */

package org.oscelot.lti.tp;


/**
 * This class defines the configuration settings for the application.
 *
 * @author      Stephen P Vickers
 * @version     1.0 (4-Jan-13)
 */
public class Config {

//
///  Application setting
//
  public static final String APP_NAME = "TMF_UOC";

//
///  Database connection settings
//
  public static final String DB_NAME = "jdbc:mysql://localhost/uoc"; // jdbc:mysql://localhost/MyDb"
  public static final String DB_USERNAME = "uoc";
  public static final String DB_PASSWORD = "SnFbL6p4crUCt9ue";
  public static final String DB_TABLENAME_PREFIX = "";

}
