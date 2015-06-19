package edu.uoc.speakapps;

import java.io.*;
import java.util.*;

import org.apache.mina.common.IoSession;

import com.wowza.wms.vhost.*;
import com.wowza.wms.logging.WMSLoggerFactory;
import com.wowza.wms.server.*;
import com.wowza.wms.amf.AMFDataItem;
import com.wowza.wms.application.*;
import com.wowza.wms.http.*;
import com.wowza.wms.client.*;
import com.wowza.wms.httpstreamer.model.*;
import com.wowza.wms.httpstreamer.util.*;
import com.wowza.util.*;
import com.wowza.wms.rtp.model.*;

/**
 *
 * @author antonibertranbellido
 * To test it http://localhost:8086/videochatAPI
 * An HTTPProvider is an extension to the server that can be attached to a HostPort definition 
 * in [install-dir]/conf/VHost.xml and can be used to return HTTP data quickly from the server by using a web browser or automated tool. 
 * More info http://www.wowza.com/forums/content.php?182-How-to-get-detailed-server-info-with-an-HTTPProvider
 */
public class VideochatAPI extends HTTProvider2Base
{	
	private static String ACTION_PARAMETER = "action";
	private static String LIST_ACTION = "list";
	private static String START_RECORD_ACTION = "start_record";
	private static String STOP_RECORD_ACTION = "stop_record";
	private static String CLOSE_SESSION_ACTION = "close_session";
	private static String ROOM_RECORD_PARAMETER = "room";
	private static String USERNAME_RECORD_PARAMETER = "username";
	
	public static final String DEFAULT_VHOST = "_defaultVHost_";
	public static final String DEFAULT_APPLICATION = "videochat";
	public static final String DEFAULT_APPINSTANCE = "_definst_";
	
	
	private void outputConnectionInfo(StringBuffer ret, ConnectionCounter counter)
	{
		ret.append("<ConnectionsCurrent>"+counter.getCurrent()+"</ConnectionsCurrent>");
		ret.append("<ConnectionsTotal>"+counter.getTotal()+"</ConnectionsTotal>");
		ret.append("<ConnectionsTotalAccepted>"+counter.getTotalAccepted()+"</ConnectionsTotalAccepted>");
		ret.append("<ConnectionsTotalRejected>"+counter.getTotalRejected()+"</ConnectionsTotalRejected>");
	}
	
	public String fixNull(String value)
	{
		if (value == null)
			return "";
		return value;
	}
	
	
	public void onHTTPRequest(IVHost inVhost, IHTTPRequest req, IHTTPResponse resp)
	{
		if (!doHTTPAuthentication(inVhost, req, resp))
			return;

		StringBuffer ret = new StringBuffer();
		
		long currTime = System.currentTimeMillis();
		
		try
		{
			String action = req.getParameter(ACTION_PARAMETER);
			ret.append("<?xml version=\"1.0\"?>\n<WowzaMediaServerPro>");
			
			if (action!=null) {
				if (LIST_ACTION.equals(action)) {
					List vhostNames = VHostSingleton.getVHostNames();
							
					Iterator<String> iter = vhostNames.iterator();
					while (iter.hasNext())
					{
						String vhostName = iter.next();
						IVHost vhost = (IVHost)VHostSingleton.getInstance(vhostName);
						if (vhost != null)
						{
							ret.append("<VHost>");
							ret.append("<Name>"+vhostName+"</Name>");
							ret.append("<TimeRunning>"+vhost.getTimeRunningSeconds()+"</TimeRunning>");
							ret.append("<ConnectionsLimit>"+vhost.getConnectionLimit()+"</ConnectionsLimit>");
							
							outputConnectionInfo(ret, vhost.getConnectionCounter());
							
							List appNames = vhost.getApplicationNames();
							List<String> appFolders = vhost.getApplicationFolderNames();
							Iterator<String> appNameIterator = appFolders.iterator();
							while (appNameIterator.hasNext())
							{
								String applicationName = appNameIterator.next();
								ret.append("<Application>");
								ret.append("<Name><![CDATA["+applicationName+"]]></Name>");
								boolean appExists = appNames.contains(applicationName);
								ret.append("<Status>"+(appExists?"loaded":"unloaded")+"</Status>");
		
								if (appExists)
								{
									IApplication application = vhost.getApplication(applicationName);
									if (application == null)
										continue;
									
									ret.append("<TimeRunning>"+application.getTimeRunningSeconds()+"</TimeRunning>");
									outputConnectionInfo(ret, application.getConnectionCounter());
									
									List appInstances = application.getAppInstanceNames();
									Iterator<String> iterAppInstances = appInstances.iterator();
									while (iterAppInstances.hasNext())
									{
										String appInstanceName = iterAppInstances.next();
										IApplicationInstance appInstance = application.getAppInstance(appInstanceName);
										if (appInstance == null)
											continue;
										
										ret.append("<ApplicationInstance>");
										ret.append("<Name><![CDATA["+appInstance.getName()+"]]></Name>");
										ret.append("<TimeRunning>"+appInstance.getTimeRunningSeconds()+"</TimeRunning>");
										
										outputConnectionInfo(ret, appInstance.getConnectionCounter());
										
									    List<IClient> clients = appInstance.getClients();
										List<IHTTPStreamerSession> httpSessions = appInstance.getHTTPStreamerSessions();
										List<RTPSession> rtpSessions = appInstance.getRTPSessions();
										
										ret.append("<RTMPConnectionCount>"+appInstance.getConnectionCounter(IVHost.COUNTER_RTMP).getCurrent()+"</RTMPConnectionCount>");
										ret.append("<RTPConnectionCount>"+appInstance.getConnectionCounter(IVHost.COUNTER_RTP).getCurrent()+"</RTPConnectionCount>");
										ret.append("<CupertinoConnectionCount>"+appInstance.getConnectionCounter(IVHost.COUNTER_HTTPCUPERTINO).getCurrent()+"</CupertinoConnectionCount>");
										ret.append("<SmoothConnectionCount>"+appInstance.getConnectionCounter(IVHost.COUNTER_HTTPSMOOTH).getCurrent()+"</SmoothConnectionCount>");
										ret.append("<SanJoseConnectionCount>"+appInstance.getConnectionCounter(IVHost.COUNTER_HTTPSANJOSE).getCurrent()+"</SanJoseConnectionCount>");
										//ret.append("<WebMConnectionCount>"+appInstance.getConnectionCounter(IVHost.COUNTER_HTTPWEBM).getCurrent()+"</WebMConnectionCount>");
										//ret.append("<DVRChunksConnectionCount>"+appInstance.getConnectionCounter(IVHost.COUNTER_HTTPDVRCHUNKS).getCurrent()+"</v>");
		
										ret.append("<RTMPSessionCount>"+clients.size()+"</RTMPSessionCount>");
										ret.append("<HTTPSessionCount>"+httpSessions.size()+"</HTTPSessionCount>");
										ret.append("<RTPSessionCount>"+rtpSessions.size()+"</RTPSessionCount>");
		
										Iterator<IClient> iterClient = clients.iterator();
										while(iterClient.hasNext())
										{
											IClient client = iterClient.next(); //appInstance.getClient(c);
											if (client == null)
												continue;
		
											ret.append("<Client>");
											ret.append("<ClientId>"+client.getClientId()+"</ClientId>");
											ret.append("<FlashVersion>"+client.getFlashVer()+"</FlashVersion>");
											ret.append("<IpAddress>"+client.getIp()+"</IpAddress>");
											ret.append("<Referrer><![CDATA["+fixNull(client.getReferrer())+"]]></Referrer>");
											ret.append("<QueryString><![CDATA["+fixNull(client.getQueryStr())+"]]></QueryString>");
											ret.append("<TimeRunning>"+client.getTimeRunningSeconds()+"</TimeRunning>");
											//ret.append("<Duration>"+((double)(System.currentTimeMillis()-client.getConnectTime())/1000.0)+"</Duration>");
											ret.append("<DateStarted>"+client.getDateStarted()+"</DateStarted>");
											ret.append("<URI><![CDATA["+client.getUri()+"]]></URI>");
											
											String protocolStr = "unknown";
											switch(client.getProtocol())
											{
											case RtmpSessionInfo.PROTOCOL_RTMP:
												protocolStr = client.isEncrypted()?"RTMPE":"RTMP";
												break;
											case RtmpSessionInfo.PROTOCOL_RTMPT:
												protocolStr = client.isSSL()?"RTMPS":(client.isEncrypted()?"RTMPTE":"RTMPT");
												break;
											}
											
											ret.append("<Protocol>"+protocolStr+"</Protocol>");
											ret.append("<IsSSL>"+client.isSSL()+"</IsSSL>");
											ret.append("<IsEncrypted>"+client.isEncrypted()+"</IsEncrypted>");
											ret.append("<Port>"+client.getServerHostPort().getPort()+"</Port>");
											
											long lastValidate = client.getLastValidateTime();
											if (lastValidate <= 0)
												ret.append("<LastValidateTime>-1</LastValidateTime>");
											else
											{
												long incTime = currTime - lastValidate;
												if (incTime < 0)
													incTime = 0;
												ret.append("<LastValidateTime>"+incTime+"</LastValidateTime>");
											}
		
											IoSession ioSession = ((Client)client).getIoSession();
											if (ioSession != null)
											{
												ret.append("<IoSessionBytesSent>"+ioSession.getWrittenBytes()+"</IoSessionBytesSent>");
												ret.append("<IoSessionBytesReceived>"+ioSession.getReadBytes()+"</IoSessionBytesReceived>");
												
												long lastIo = currTime - ioSession.getLastIoTime();
												if (lastIo < 0)
													lastIo = 0;
												ret.append("<IoSessionLastIo>"+lastIo+"</IoSessionLastIo>");
											}
											
											IOPerformanceCounter ioPerformance = client.getMediaIOPerformanceCounter();
											if (ioPerformance != null)
											{
												ret.append("<IoBytesSent>"+ioPerformance.getMessagesOutBytes()+"</IoBytesSent>");
												ret.append("<IoBytesReceived>"+ioPerformance.getMessagesInBytes()+"</IoBytesReceived>");
											}
											
											ret.append("</Client>");
										}
										
										Iterator<IHTTPStreamerSession> iterHttp = httpSessions.iterator();
										while(iterHttp.hasNext())
										{
											IHTTPStreamerSession httpSession = iterHttp.next();
											if (httpSession == null)
												continue;
											
											ret.append("<HTTPSession>");
											ret.append("<SessionId>"+httpSession.getSessionId()+"</SessionId>");
											ret.append("<IpAddress>"+httpSession.getIpAddress()+"</IpAddress>");
											ret.append("<Referrer><![CDATA["+fixNull(httpSession.getReferrer())+"]]></Referrer>");
											ret.append("<QueryString><![CDATA["+fixNull(httpSession.getQueryStr())+"]]></QueryString>");
											ret.append("<TimeRunning>"+httpSession.getTimeRunningSeconds()+"</TimeRunning>");
											ret.append("<DateStarted>"+httpSession.getElapsedTime().getDateString()+"</DateStarted>");
											ret.append("<URI><![CDATA["+httpSession.getUri()+"]]></URI>");
											ret.append("<Protocol>"+HTTPStreamerUtils.httpSessionProtocolToName(httpSession.getSessionProtocol())+"</Protocol>");
											ret.append("<SessionType>"+HTTPStreamerUtils.httpSessionTypeToName(httpSession.getSessionType())+"</SessionType>");
											ret.append("<Port>"+httpSession.getServerPort()+"</Port>");
											
											IOPerformanceCounter ioPerformance = httpSession.getIOPerformanceCounter();
											if (ioPerformance != null)
											{
												ret.append("<IoBytesSent>"+ioPerformance.getMessagesOutBytes()+"</IoBytesSent>");
												ret.append("<IoBytesReceived>"+ioPerformance.getMessagesInBytes()+"</IoBytesReceived>");
											}
											
											long lastIo = currTime - httpSession.getLastRequest();
											if (lastIo < 0)
												lastIo = 0;
											ret.append("<IoLastRequest>"+lastIo+"</IoLastRequest>");
		
											ret.append("</HTTPSession>");
										}
		
										Iterator<RTPSession> iterRTP = rtpSessions.iterator();
										while(iterRTP.hasNext())
										{
											RTPSession rtpSession = iterRTP.next();
											if (rtpSession == null)
												continue;
											
											ret.append("<RTPSession>");
											ret.append("<SessionId>"+rtpSession.getSessionId()+"</SessionId>");
											ret.append("<IpAddress>"+rtpSession.getIp()+"</IpAddress>");
											ret.append("<Referrer><![CDATA["+fixNull(rtpSession.getReferrer())+"]]></Referrer>");
											ret.append("<QueryString><![CDATA["+fixNull(rtpSession.getQueryStr())+"]]></QueryString>");
											ret.append("<TimeRunning>"+rtpSession.getTimeRunningSeconds()+"</TimeRunning>");
											ret.append("<DateStarted>"+rtpSession.getElapsedTime().getDateString()+"</DateStarted>");
											ret.append("<URI><![CDATA["+rtpSession.getUri()+"]]></URI>");
											ret.append("<Port>"+rtpSession.getServerPort()+"</Port>");
											
											IOPerformanceCounter ioPerformance = rtpSession.getIOPerformanceCounter();
											if (ioPerformance != null)
											{
												ret.append("<IoBytesSent>"+ioPerformance.getMessagesOutBytes()+"</IoBytesSent>");
												ret.append("<IoBytesReceived>"+ioPerformance.getMessagesInBytes()+"</IoBytesReceived>");
											}
											
											ret.append("</RTPSession>");
										}
		
										ret.append("</ApplicationInstance>");
									}
								}
								
								ret.append("</Application>");
							}
							
							ret.append("</VHost>");
						}
					}
				} else {
					if (START_RECORD_ACTION.equals(action)) {
						executeAction("start",  ret, req);
					} else {
						if (STOP_RECORD_ACTION.equals(action)) {
							executeAction("stop",  ret, req);
						} else {
							if (CLOSE_SESSION_ACTION.equals(action)) {
								executeAction("closeSessionClient",  ret, req);
							} else {
								addErrorMessage(ret, "Unknown action "+action);
							}
						}	
						
					}

				}
				
			} else {
				
				addErrorMessage(ret, "Void action");
				
			}
			ret.append("</WowzaMediaServerPro>");
		}

		catch (Exception e)
		{
			WMSLoggerFactory.getLogger(VideochatAPI.class).error("VideochatAPI.onHTTPRequest: "+e.toString());
			e.printStackTrace();
		}
					
		try
		{
			resp.setHeader("Content-Type", "text/xml");
			
			OutputStream out = resp.getOutputStream();
			byte[] outBytes = ret.toString().getBytes();
			out.write(outBytes);
		}
		catch (Exception e)
		{
			WMSLoggerFactory.getLogger(VideochatAPI.class).error("VideochatAPI.onHTTPRequest: "+e.toString());
			e.printStackTrace();
		}
		
	}
	
	private void executeAction(String action,  StringBuffer ret, IHTTPRequest req)  {
		String room = req.getParameter(ROOM_RECORD_PARAMETER);
		String username = req.getParameter(USERNAME_RECORD_PARAMETER);
		if (username==null) {
			username = ".";
		}
		if (room!=null && room.length()>0) {
			if (room!=null && room.length()>0) {
				IVHost vhost = (IVHost)VHostSingleton.getInstance(DEFAULT_VHOST);
				if (vhost != null)
				{
				
					IApplication application = vhost.getApplication(DEFAULT_APPLICATION);
					if (application != null) {
						
						IApplicationInstance appInstance = application.getAppInstance(DEFAULT_APPINSTANCE);
						if (appInstance!=null) {
							String fullname = appInstance.getApplication().getName() ;
							WMSLoggerFactory.getLogger(VideochatAPI.class).info("VideochatAPI- executeAction: " + fullname+" action "+action+" username "+username + " room "+ room);
							if (action.equals(CLOSE_SESSION_ACTION)) {
								appInstance.broadcastMsg("closeSessionClientAuto", new AMFDataItem(room));
							} else {
								appInstance.broadcastMsg(action+"RecordClient", new AMFDataItem(username), new AMFDataItem(room));
							}
							ret.append("<success code=\""+action+"\"><username>"+username+"</username><room>"+room+"</room></success>");
						} else {
							
							addErrorMessage(ret, "Can't "+action+" record, the instances application "+DEFAULT_APPINSTANCE+" not found!!");

						}
						
					} else {
						
						addErrorMessage(ret, "Can't "+action+" record, the application "+DEFAULT_APPLICATION+" not found!!");

					}
				} else {
					
					addErrorMessage(ret, "Can't "+action+" record, the vhost "+DEFAULT_VHOST+" not found!!");

				}
			} else {
				
				addErrorMessage(ret, "Can't "+action+" record, username not set!!");

			}		
		} else {
			
			addErrorMessage(ret, "Can't record, room not set!!");

		}
		
	}
	
	/**
	 * Adds an error message
	 * @param ret
	 * @param error
	 */
	private void addErrorMessage(StringBuffer ret, String error) {
		ret.append("<error>"+error+"</error>");
	}
	

	
	public void onHTTPRequest_old(IVHost inVhost, IHTTPRequest req, IHTTPResponse resp)
	{
		if (!doHTTPAuthentication(inVhost, req, resp))
			return;

		StringBuffer ret = new StringBuffer();
		
		long currTime = System.currentTimeMillis();
		
		try
		{
			List vhostNames = VHostSingleton.getVHostNames();
			ret.append("<?xml version=\"1.0\"?>\n<WowzaMediaServerPro>");
					
			Iterator<String> iter = vhostNames.iterator();
			while (iter.hasNext())
			{
				String vhostName = iter.next();
				IVHost vhost = (IVHost)VHostSingleton.getInstance(vhostName);
				if (vhost != null)
				{
					ret.append("<VHost>");
					ret.append("<Name>"+vhostName+"</Name>");
					ret.append("<TimeRunning>"+vhost.getTimeRunningSeconds()+"</TimeRunning>");
					ret.append("<ConnectionsLimit>"+vhost.getConnectionLimit()+"</ConnectionsLimit>");
					
					outputConnectionInfo(ret, vhost.getConnectionCounter());
					
					List appNames = vhost.getApplicationNames();
					List<String> appFolders = vhost.getApplicationFolderNames();
					Iterator<String> appNameIterator = appFolders.iterator();
					while (appNameIterator.hasNext())
					{
						String applicationName = appNameIterator.next();
						ret.append("<Application>");
						ret.append("<Name><![CDATA["+applicationName+"]]></Name>");
						boolean appExists = appNames.contains(applicationName);
						ret.append("<Status>"+(appExists?"loaded":"unloaded")+"</Status>");

						if (appExists)
						{
							IApplication application = vhost.getApplication(applicationName);
							if (application == null)
								continue;
							
							ret.append("<TimeRunning>"+application.getTimeRunningSeconds()+"</TimeRunning>");
							outputConnectionInfo(ret, application.getConnectionCounter());
							
							List appInstances = application.getAppInstanceNames();
							Iterator<String> iterAppInstances = appInstances.iterator();
							while (iterAppInstances.hasNext())
							{
								String appInstanceName = iterAppInstances.next();
								IApplicationInstance appInstance = application.getAppInstance(appInstanceName);
								if (appInstance == null)
									continue;
								
								ret.append("<ApplicationInstance>");
								ret.append("<Name><![CDATA["+appInstance.getName()+"]]></Name>");
								ret.append("<TimeRunning>"+appInstance.getTimeRunningSeconds()+"</TimeRunning>");
								
								outputConnectionInfo(ret, appInstance.getConnectionCounter());
								
							    List<IClient> clients = appInstance.getClients();
								List<IHTTPStreamerSession> httpSessions = appInstance.getHTTPStreamerSessions();
								List<RTPSession> rtpSessions = appInstance.getRTPSessions();
								
								ret.append("<RTMPConnectionCount>"+appInstance.getConnectionCounter(IVHost.COUNTER_RTMP).getCurrent()+"</RTMPConnectionCount>");
								ret.append("<RTPConnectionCount>"+appInstance.getConnectionCounter(IVHost.COUNTER_RTP).getCurrent()+"</RTPConnectionCount>");
								ret.append("<CupertinoConnectionCount>"+appInstance.getConnectionCounter(IVHost.COUNTER_HTTPCUPERTINO).getCurrent()+"</CupertinoConnectionCount>");
								ret.append("<SmoothConnectionCount>"+appInstance.getConnectionCounter(IVHost.COUNTER_HTTPSMOOTH).getCurrent()+"</SmoothConnectionCount>");
								ret.append("<SanJoseConnectionCount>"+appInstance.getConnectionCounter(IVHost.COUNTER_HTTPSANJOSE).getCurrent()+"</SanJoseConnectionCount>");
								//ret.append("<WebMConnectionCount>"+appInstance.getConnectionCounter(IVHost.COUNTER_HTTPWEBM).getCurrent()+"</WebMConnectionCount>");
								//ret.append("<DVRChunksConnectionCount>"+appInstance.getConnectionCounter(IVHost.COUNTER_HTTPDVRCHUNKS).getCurrent()+"</v>");

								ret.append("<RTMPSessionCount>"+clients.size()+"</RTMPSessionCount>");
								ret.append("<HTTPSessionCount>"+httpSessions.size()+"</HTTPSessionCount>");
								ret.append("<RTPSessionCount>"+rtpSessions.size()+"</RTPSessionCount>");

								Iterator<IClient> iterClient = clients.iterator();
								while(iterClient.hasNext())
								{
									IClient client = iterClient.next(); //appInstance.getClient(c);
									if (client == null)
										continue;

									ret.append("<Client>");
									ret.append("<ClientId>"+client.getClientId()+"</ClientId>");
									ret.append("<FlashVersion>"+client.getFlashVer()+"</FlashVersion>");
									ret.append("<IpAddress>"+client.getIp()+"</IpAddress>");
									ret.append("<Referrer><![CDATA["+fixNull(client.getReferrer())+"]]></Referrer>");
									ret.append("<QueryString><![CDATA["+fixNull(client.getQueryStr())+"]]></QueryString>");
									ret.append("<TimeRunning>"+client.getTimeRunningSeconds()+"</TimeRunning>");
									//ret.append("<Duration>"+((double)(System.currentTimeMillis()-client.getConnectTime())/1000.0)+"</Duration>");
									ret.append("<DateStarted>"+client.getDateStarted()+"</DateStarted>");
									ret.append("<URI><![CDATA["+client.getUri()+"]]></URI>");
									
									String protocolStr = "unknown";
									switch(client.getProtocol())
									{
									case RtmpSessionInfo.PROTOCOL_RTMP:
										protocolStr = client.isEncrypted()?"RTMPE":"RTMP";
										break;
									case RtmpSessionInfo.PROTOCOL_RTMPT:
										protocolStr = client.isSSL()?"RTMPS":(client.isEncrypted()?"RTMPTE":"RTMPT");
										break;
									}
									
									ret.append("<Protocol>"+protocolStr+"</Protocol>");
									ret.append("<IsSSL>"+client.isSSL()+"</IsSSL>");
									ret.append("<IsEncrypted>"+client.isEncrypted()+"</IsEncrypted>");
									ret.append("<Port>"+client.getServerHostPort().getPort()+"</Port>");
									
									long lastValidate = client.getLastValidateTime();
									if (lastValidate <= 0)
										ret.append("<LastValidateTime>-1</LastValidateTime>");
									else
									{
										long incTime = currTime - lastValidate;
										if (incTime < 0)
											incTime = 0;
										ret.append("<LastValidateTime>"+incTime+"</LastValidateTime>");
									}

									IoSession ioSession = ((Client)client).getIoSession();
									if (ioSession != null)
									{
										ret.append("<IoSessionBytesSent>"+ioSession.getWrittenBytes()+"</IoSessionBytesSent>");
										ret.append("<IoSessionBytesReceived>"+ioSession.getReadBytes()+"</IoSessionBytesReceived>");
										
										long lastIo = currTime - ioSession.getLastIoTime();
										if (lastIo < 0)
											lastIo = 0;
										ret.append("<IoSessionLastIo>"+lastIo+"</IoSessionLastIo>");
									}
									
									IOPerformanceCounter ioPerformance = client.getMediaIOPerformanceCounter();
									if (ioPerformance != null)
									{
										ret.append("<IoBytesSent>"+ioPerformance.getMessagesOutBytes()+"</IoBytesSent>");
										ret.append("<IoBytesReceived>"+ioPerformance.getMessagesInBytes()+"</IoBytesReceived>");
									}
									
									ret.append("</Client>");
								}
								
								Iterator<IHTTPStreamerSession> iterHttp = httpSessions.iterator();
								while(iterHttp.hasNext())
								{
									IHTTPStreamerSession httpSession = iterHttp.next();
									if (httpSession == null)
										continue;
									
									ret.append("<HTTPSession>");
									ret.append("<SessionId>"+httpSession.getSessionId()+"</SessionId>");
									ret.append("<IpAddress>"+httpSession.getIpAddress()+"</IpAddress>");
									ret.append("<Referrer><![CDATA["+fixNull(httpSession.getReferrer())+"]]></Referrer>");
									ret.append("<QueryString><![CDATA["+fixNull(httpSession.getQueryStr())+"]]></QueryString>");
									ret.append("<TimeRunning>"+httpSession.getTimeRunningSeconds()+"</TimeRunning>");
									ret.append("<DateStarted>"+httpSession.getElapsedTime().getDateString()+"</DateStarted>");
									ret.append("<URI><![CDATA["+httpSession.getUri()+"]]></URI>");
									ret.append("<Protocol>"+HTTPStreamerUtils.httpSessionProtocolToName(httpSession.getSessionProtocol())+"</Protocol>");
									ret.append("<SessionType>"+HTTPStreamerUtils.httpSessionTypeToName(httpSession.getSessionType())+"</SessionType>");
									ret.append("<Port>"+httpSession.getServerPort()+"</Port>");
									
									IOPerformanceCounter ioPerformance = httpSession.getIOPerformanceCounter();
									if (ioPerformance != null)
									{
										ret.append("<IoBytesSent>"+ioPerformance.getMessagesOutBytes()+"</IoBytesSent>");
										ret.append("<IoBytesReceived>"+ioPerformance.getMessagesInBytes()+"</IoBytesReceived>");
									}
									
									long lastIo = currTime - httpSession.getLastRequest();
									if (lastIo < 0)
										lastIo = 0;
									ret.append("<IoLastRequest>"+lastIo+"</IoLastRequest>");

									ret.append("</HTTPSession>");
								}

								Iterator<RTPSession> iterRTP = rtpSessions.iterator();
								while(iterRTP.hasNext())
								{
									RTPSession rtpSession = iterRTP.next();
									if (rtpSession == null)
										continue;
									
									ret.append("<RTPSession>");
									ret.append("<SessionId>"+rtpSession.getSessionId()+"</SessionId>");
									ret.append("<IpAddress>"+rtpSession.getIp()+"</IpAddress>");
									ret.append("<Referrer><![CDATA["+fixNull(rtpSession.getReferrer())+"]]></Referrer>");
									ret.append("<QueryString><![CDATA["+fixNull(rtpSession.getQueryStr())+"]]></QueryString>");
									ret.append("<TimeRunning>"+rtpSession.getTimeRunningSeconds()+"</TimeRunning>");
									ret.append("<DateStarted>"+rtpSession.getElapsedTime().getDateString()+"</DateStarted>");
									ret.append("<URI><![CDATA["+rtpSession.getUri()+"]]></URI>");
									ret.append("<Port>"+rtpSession.getServerPort()+"</Port>");
									
									IOPerformanceCounter ioPerformance = rtpSession.getIOPerformanceCounter();
									if (ioPerformance != null)
									{
										ret.append("<IoBytesSent>"+ioPerformance.getMessagesOutBytes()+"</IoBytesSent>");
										ret.append("<IoBytesReceived>"+ioPerformance.getMessagesInBytes()+"</IoBytesReceived>");
									}
									
									ret.append("</RTPSession>");
								}

								ret.append("</ApplicationInstance>");
							}
						}
						
						ret.append("</Application>");
					}
					
					ret.append("</VHost>");
				}
			}
			
			ret.append("</WowzaMediaServerPro>");
		}
		catch (Exception e)
		{
			WMSLoggerFactory.getLogger(VideochatAPI.class).error("VideochatAPI.onHTTPRequest: "+e.toString());
			e.printStackTrace();
		}
				
		try
		{
			resp.setHeader("Content-Type", "text/xml");
			
			OutputStream out = resp.getOutputStream();
			byte[] outBytes = ret.toString().getBytes();
			out.write(outBytes);
		}
		catch (Exception e)
		{
			WMSLoggerFactory.getLogger(VideochatAPI.class).error("VideochatAPI.onHTTPRequest: "+e.toString());
			e.printStackTrace();
		}
		
	}
}