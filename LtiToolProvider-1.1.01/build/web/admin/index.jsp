<%--
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
      1.0.02  13-Apr-13  Minor update to add support for latest release of LtiToolProvider library
      1.1.00  18-Jun-13
--%>
<%--
  This page manages the definition of tool consumer records.  A tool consumer record is required to
  enable each VLE to securely connect to this application.

  *** IMPORTANT ***
  Access to this page should be restricted to prevent unauthorised access to the configuration of tool
  consumers (for example, using an entry in an Apache .htaccess file); access to all other pages is
  authorised by LTI.
  ***           ***
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html lang="en" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.List,
                java.util.Map,
                java.util.Iterator,
                java.util.HashMap,
                java.util.Calendar,
                java.sql.SQLException,
                java.text.SimpleDateFormat,
                org.apache.commons.lang.StringEscapeUtils,
                org.oscelot.lti.tp.ToolProvider,
                org.oscelot.lti.tp.Callback,
                org.oscelot.lti.tp.DataConnector,
                org.oscelot.lti.tp.ToolConsumer,
                org.oscelot.lti.tp.dataconnector.JDBC,
                org.oscelot.lti.tp.Config,
                org.oscelot.lti.tp.Db
                "%>
<%
// Initialise database
    Db db = null;
    boolean ok ;

 
    // Open database connection
    db = new Db();
    ok = db != null;
    
// Initialise parameters
  String key = "";
  DataConnector dataConnector = null;
  ToolProvider tool = null;
  List<ToolConsumer> consumers = null;
  ToolConsumer updateConsumer = null;
  if (ok) {
// Create LTI Tool Provider instance
    dataConnector = new JDBC(Config.DB_TABLENAME_PREFIX, db.getConnection());
    tool = new ToolProvider(request, response, new HashMap<String,Callback>(), dataConnector);
// Check for consumer key and action parameters
    String action = null;
    if (request.getParameter("key") != null) {
      key = request.getParameter("key");
    }
    action = request.getParameter("do");
// Process add consumer action
    if ("add".equals(action)) {
      updateConsumer = new ToolConsumer(key, dataConnector, false);
      updateConsumer.setName(request.getParameter("name"));
      updateConsumer.setSecret(request.getParameter("secret"));
      updateConsumer.setEnabled(request.getParameter("enabled") != null);
      Calendar cal = Calendar.getInstance();
      SimpleDateFormat dateFormat = new SimpleDateFormat("d-MMM-yyyy HH:mm");
      String date = request.getParameter("enable_from");
      if ((date == null) || (date.length() <= 0)) {
        updateConsumer.setEnableFrom(null);
      } else {
        cal.setTime(dateFormat.parse(date));
        updateConsumer.setEnableFrom(cal);
      }
      date = request.getParameter("enable_until");
      if ((date == null) || (date.length() <= 0)) {
        updateConsumer.setEnableUntil(null);
      } else {
        cal.setTime(dateFormat.parse(date));
        updateConsumer.setEnableUntil(cal);
      }
      updateConsumer.setProtect(request.getParameter("protected") != null);
// Ensure all required fields have been provided
      if ((key.length() > 0) && (updateConsumer.getName() !=  null) && (updateConsumer.getName().length() > 0) &&
          (updateConsumer.getSecret() !=  null) && (updateConsumer.getSecret().length() > 0)) {
        if (updateConsumer.save()) {
          session.setAttribute("message", "The consumer has been saved.");
        } else {
          session.setAttribute("error_message", "Unable to save the consumer; please check the data and try again.");
        }
        response.sendRedirect("./");
        return;
      }

// Process delete consumer action
    } else if ("delete".equals(action)) {
      ToolConsumer consumer = new ToolConsumer(key, dataConnector, false);
      if (consumer.delete()) {
        session.setAttribute("message", "The consumer has been deleted.");
      } else {
        session.setAttribute("error_message", "Unable to delete the consumer; please try again.");
      }
      response.sendRedirect("./");
      return;

    } else {
// Initialise an empty tool consumer instance
      updateConsumer = new ToolConsumer(null, dataConnector, false);
    }

// Fetch a list of existing tool consumer records
    consumers = tool.getConsumers();
  }

// Page header
   String title = Config.APP_NAME;
%>
<head>
<meta http-equiv="content-language" content="EN" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title><%=title%>: Consumers</title>
<link href="../css/consumers.css" media="screen" rel="stylesheet" type="text/css" />
</head>

<body>
<p><strong>El acceso a esta pagina deberia estar restringido a los administradores del tool provider.</strong></p>
<%
// Check for any messages to be displayed
  if (session.getAttribute("error_message") != null) {
%>
<p style="font-weight: bold; color: #f00;">ERROR: <%=session.getAttribute("error_message")%></p>
<%
    session.setAttribute("error_message", null);
  }
  if (session.getAttribute("message") != null) {
%>
<p style="font-weight: bold; color: #00f;"><%=session.getAttribute("message")%></p>
<%
    session.setAttribute("message", null);
  }

// Display table of existing tool consumer records
  if (consumers != null) {
    if (consumers.size() <= 0) {
%>
<p>No consumers have been added yet.</p>
<%
    } else {
%>
<table class="items" border="1" cellpadding="3">
<thead>
  <tr>
    <th>Nombre</th>
    <th>Clave</th>
    <th>Version</th>
    <th>Disponible?</th>
    <th>Protegido?</th>
    <th>Ultimo acceso</th>
    <th>Opciones</th>
  </tr>
</thead>
<tbody>
<%
      for (Iterator<ToolConsumer> iter = consumers.iterator(); iter.hasNext();) {
        ToolConsumer consumer = iter.next();
        String trKey = StringEscapeUtils.escapeHtml(consumer.getKey());
        if (key.equals(consumer.getKey())) {
          updateConsumer = consumer;
        }
        String guid = "";
        if (consumer.getConsumerGUID() != null) {
          guid = consumer.getConsumerGUID();
        }
        String version = "";
        if (consumer.getConsumerVersion() != null) {
          version = consumer.getConsumerVersion();
        }
        String available;
        String availableAlt;
        String trClass;
        if (!consumer.isAvailable()) {
          available = "cross";
          availableAlt = "Not available";
          trClass = "notvisible";
        } else {
          available = "tick";
          availableAlt = "Available";
          trClass = "";
        }
        String protect;
        String protectAlt;
        if (consumer.isProtect()) {
          protect = "tick";
          protectAlt = "Protected";
        } else {
          protect = "cross";
          protectAlt = "Not protected";
        }
        String last;
        if (consumer.getLastAccess() == null) {
          last = "None";
        } else {
          SimpleDateFormat dateFormat = new SimpleDateFormat("d-MMM-yyyy");
          last = dateFormat.format(consumer.getLastAccess().getTime());
        }
%>
  <tr class="<%=trClass%>">
    <td><%=consumer.getName()%></td>
    <td><%=consumer.getKey()%></td>
    <td><span title="<%=guid%>"><%=version%></span></td>
    <td class="aligncentre"><img src="../images/<%=available%>.gif" alt="<%=availableAlt%>" title="<%=availableAlt%>" /></td>
    <td class="aligncentre"><img src="../images/<%=protect%>.gif" alt="<%=protectAlt%>" title="<%=protectAlt%>" /></td>
    <td><%=last%></td>
    <td class="iconcolumn aligncentre">
      <a href="./?key=<%=trKey%>"><img src="../images/edit.png" title="Edit consumer" alt="Edit consumer" /></a>&nbsp;<a href="./?do=delete&amp;key=<%=trKey%>" onclick="return confirm('Delete consumer; are you sure?');"><img src="../images/delete.png" title="Delete consumer" alt="Delete consumer" /></a>
    </td>
  </tr>
<%
      }
%>
</tbody>
</table>
<%
    }

// Display form for adding/editing a tool consumer
    String mode;
    String type;
    String key1;
    String key2;
    if (updateConsumer.getCreated() != null) {
      mode = "Update";
      type = " disabled=\"disabled\"";
      key1 = "";
      key2 = "key";
    } else {
      mode = "Add new";
      type = "";
      key1 = "key";
      key2 = "";
    }
    String name = "";
    if (updateConsumer.getName() != null) {
      name = StringEscapeUtils.escapeHtml(updateConsumer.getName());
    }
    key = "";
    if (updateConsumer.getKey() != null) {
      key = StringEscapeUtils.escapeHtml(updateConsumer.getKey());
    }
    String secret = "";
    if (updateConsumer.getSecret() != null) {
      secret = StringEscapeUtils.escapeHtml(updateConsumer.getSecret());
    }
    String enabled = "";
    if (updateConsumer.isEnabled()) {
      enabled = " checked=\"checked\"";
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat("d-MMM-yyyy HH:mm");
    String enableFrom = "";
    if (updateConsumer.getEnableFrom() !=  null) {
      enableFrom = dateFormat.format(updateConsumer.getEnableFrom().getTime());
    }
    String enableUntil = "";
    if (updateConsumer.getEnableUntil() !=  null) {
      enableUntil = dateFormat.format(updateConsumer.getEnableUntil().getTime());
    }
    String protect = "";
    if (updateConsumer.isProtect()) {
      protect = " checked=\"checked\"";
    }
%>
<h2><%=mode%> consumer</h2>

<form action="./" method="post">
<div class="box">
  <span class="label">Name:<span class="required" title="required">*</span></span>&nbsp;<input name="name" type="text" size="50" maxlength="200" value="<%=name%>" /><br />
  <span class="label">Key:<span class="required" title="required">*</span></span>&nbsp;<input name="<%=key1%>" type="text" size="75" maxlength="200" value="<%=key%>"<%=type%> /><br />
  <span class="label">Secret:<span class="required" title="required">*</span></span>&nbsp;<input name="secret" type="text" size="75" maxlength="200" value="<%=secret%>" /><br />
  <span class="label">Enabled?</span>&nbsp;<input name="enabled" type="checkbox" value="1"<%=enabled%> /><br />
  <span class="label">Enable from:</span>&nbsp;<input name="enable_from" type="text" size="50" maxlength="200" value="<%=enableFrom%>" /><br />
  <span class="label">Enable until:</span>&nbsp;<input name="enable_until" type="text" size="50" maxlength="200" value="<%=enableUntil%>" /><br />
  <span class="label">Protected?</span>&nbsp;<input name="protected" type="checkbox" value="1"<%=protect%> /><br />
  <br />
  <input type="hidden" name="do" value="add" />
  <input type="hidden" name="<%=key2%>" value="<%=key%>" />
  <span class="label"><span class="required" title="required">*</span>&nbsp;=&nbsp;required field</span>&nbsp;<input type="submit" value="<%=mode%> consumer" />
<%
    if (updateConsumer.getCreated() != null) {
%>
  &nbsp;<input type="reset" value="Cancel" onclick="location.href='./';" />
<%
    }
  }

// Page footer
%>
</div>
</form>
</body>
</html>
