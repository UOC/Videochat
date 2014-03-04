<html>
<head>
  <title>IMS Basic Learning Tools Interoperability</title>
</head>
<body style="font-family:sans-serif">
<img src="http://www.sun.com/images/l2/l2_duke_java.gif" align="right">
<p><b>Tool Provider UOC</b></p>
</p>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="net.oauth.OAuth" %>
<%@ page import="net.oauth.OAuthMessage" %>
<%@ page import="net.oauth.OAuthConsumer" %>
<%@ page import="net.oauth.OAuthAccessor" %>
<%@ page import="net.oauth.OAuthValidator" %>
<%@ page import="net.oauth.SimpleOAuthValidator" %>
<%@ page import="net.oauth.signature.OAuthSignatureMethod" %>
<%@ page import="net.oauth.server.HttpRequestMessage" %>
<%@ page import="net.oauth.server.OAuthServlet" %>
<%@ page import="net.oauth.signature.OAuthSignatureMethod" %>

<%@page import="java.util.List,
                java.util.Iterator,
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


<pre>
<%

  Enumeration en = request.getParameterNames();
  while (en.hasMoreElements()) {
    String paramName = (String) en.nextElement();
    out.println(paramName + " = " + request.getParameter(paramName) );
  }

  OAuthMessage oam = OAuthServlet.getMessage(request, null);
  OAuthValidator oav = new SimpleOAuthValidator();
  String oauth_consumer_key = request.getParameter("oauth_consumer_key");
  if ( oauth_consumer_key == null ) {
    out.println("<b>Missing oauth_consumer_key</b>\n");
    return;
  }
  OAuthConsumer cons = null;
  
  /*josedcz*/
  // Open database connection
  Db db = null;
  boolean ok ;
  DataConnector dataConnector = null;
  db = new Db();
  ok = db != null;
  if(ok){
    dataConnector = new JDBC(Config.DB_TABLENAME_PREFIX, db.getConnection());
    List<ToolConsumer> consumers = null;

    String trSecret ="";
    String trKey ="";
    boolean notFound=true;
    consumers = dataConnector.getToolConsumers(); 
    
            //Search for Key and Secret Stored in BD
          for (Iterator<ToolConsumer> iter = consumers.iterator(); iter.hasNext() && notFound;) {
                ToolConsumer consumer = iter.next();
                trKey = StringEscapeUtils.escapeHtml(consumer.getKey());
                trSecret = StringEscapeUtils.escapeHtml(consumer.getSecret());


                  out.println(request.getServerName());

            if ( (request.getServerName()).equals(oauth_consumer_key) ) {
              cons = new OAuthConsumer(request.getServerName(), request.getServerName(), trSecret, null);
            } else if ( trKey.equals(oauth_consumer_key) ) {
                 cons = new OAuthConsumer(request.getServerName(), trKey, trSecret, null);
            } else {
              out.println("<b>oauth_consumer_key="+oauth_consumer_key+" no encontrada.</b>\n");
              return;
            }
           }

  OAuthAccessor acc = new OAuthAccessor(cons);

  try {
        out.println("\n<b>Mensaje original</b>\n</pre><p>\n");
        out.println(OAuthSignatureMethod.getBaseString(oam));
        out.println("<pre>\n");
        oav.validateMessage(oam,acc);
        out.println("Mensaje validado. Ambas firmas coinciden");
      } catch(Exception e) {
        out.println("<b>Error mientras se validaba el mensaje:</b>\n");
        out.println(e);
    }
  }

%>
</pre>
<hr>
