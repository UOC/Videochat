package edu.uoc.speakapps;

import java.io.File;
import java.io.IOException;

import com.wowza.wms.application.*;
import com.wowza.wms.amf.*;
import com.wowza.wms.client.*;
import com.wowza.wms.module.*;
import com.wowza.wms.request.*;
import com.wowza.wms.stream.*;
import com.wowza.wms.rtp.model.*;
import com.wowza.wms.httpstreamer.model.*;
import com.wowza.wms.httpstreamer.cupertinostreaming.httpstreamer.*;
import com.wowza.wms.httpstreamer.smoothstreaming.httpstreamer.*;

public class Videochat extends ModuleBase {
	
	public static IApplicationInstance _appInstance;

	public void onAppStart(IApplicationInstance appInstance) {
		
		_appInstance = appInstance;

		String fullname = appInstance.getApplication().getName() + "/"
				+ appInstance.getName();
		getLogger().info("Videochat speakapps - onAppStart: " + fullname);
	}


	public void onConnect(IClient client, RequestFunction function,
			AMFDataList params) {
		getLogger().info("Videochat speakapps - onConnect: " + client.getClientId());
		WMSProperties props = client.getProperties();
		props.setProperty("username", params.getString(PARAM1));
		props.setProperty("room", params.getString(PARAM2));
		props.setProperty("publishName", params.getString(PARAM3));
		props.setProperty("userkey", params.getString(PARAM4));
		getLogger().info("Videochat speakapps - username: " + params.getString(PARAM1));
		getLogger().info("Videochat speakapps - room: " + params.getString(PARAM2));
		getLogger().info("Videochat speakapps - publishName: " + params.getString(PARAM3));
	}

	/**
	 * On connection Accepted the user is inside our connection
	 * @param client
	 */
	public void onConnectAccept(IClient client) {
		getLogger().info("Videochat speakapps - onConnectAccept: " + client.getClientId());
		/*WMSProperties props = client.getProperties();
		client.call("setRoom", null, props.getPropertyStr("room"));
		client.call("setUsername", null, props.getPropertyStr("username"));
		*/
	}

	/**
	 * The connection was rejected
	 * @param client
	 */
	public void onConnectReject(IClient client) {
		getLogger().info("Videochat speakapps - onConnectReject: " + client.getClientId());
	}

	/**
	 * The client is disconnected we have to notify to videochat
	 * @param client
	 */
	public void onDisconnect(IClient client) {
		WMSProperties props = client.getProperties();
		String userkey =  props.getPropertyStr("userkey");
		String username =  props.getPropertyStr("username");
		String room =  props.getPropertyStr("room");
		getLogger().error("Videochat speakapps - onDisconnect: " + client.getClientId());
		_appInstance.broadcastMsg("disconnectUserClient", new AMFDataItem(userkey), new AMFDataItem(username), new AMFDataItem(room));
	}
	

	// Client call functions ******************
	/**
	 * Start Record
	 * @param client
	 * @param function
	 * @param params
	 */
	public void startRecord(IClient client, RequestFunction function,
			AMFDataList params) {
		WMSProperties props = client.getProperties();
		String username =  props.getPropertyStr("username");
		String room =  props.getPropertyStr("room");
		//Delete the folder 
		String publishName =  props.getPropertyStr("publishName");
		int pos = publishName.lastIndexOf("_");
		if (pos>0) {
			String folder_to_delete = publishName.substring(0, pos);
			folder_to_delete = _appInstance.getStreamStoragePath()+"/record/"+folder_to_delete.replaceAll("_", "/");
			getLogger().info("Videochat speakapps - startRecord "+room+" deleting folder "+folder_to_delete);
			
			File folder = new File(folder_to_delete);
			boolean deletedFolder = deleteFolder(folder);
			
			getLogger().info("Videochat speakapps - startRecord "+room+" deleted folder "+deletedFolder);
		} 
		_appInstance.broadcastMsg("startRecordClient", new AMFDataItem(username), new AMFDataItem(room));
	}
	
	private static boolean deleteFolder(File folder) {
		boolean success = false; 
		try {
			if (folder.exists()) {				
			    File[] files = folder.listFiles();
			    if(files!=null) { //some JVMs return null for empty dirs
			        for(File f: files) {
			            if(f.isDirectory()) {
			            	success = deleteFolder(f);
			            } else {
			            	success = f.delete();
			            }
			        }
			    }
			    success = folder.delete();
			    getLogger().info("Videochat speakapps - startRecordClient success "+success+" deleting folder ");
				
			}
		} catch (Exception ioe) {
			getLogger().error("Videochat speakapps Deleting folder- "+folder.getAbsolutePath(), ioe);
		}
	    return success;
	}
	/**
	 * Stop Record
	 * @param client
	 * @param function
	 * @param params
	 */
	public void stopRecord(IClient client, RequestFunction function,
			AMFDataList params) {
		WMSProperties props = client.getProperties();
		String username =  props.getPropertyStr("username");
		String room =  props.getPropertyStr("room");
		getLogger().info("Videochat speakapps - stopRecordClient "+room+" username "+username);
		_appInstance.broadcastMsg("stopRecordClient", new AMFDataItem(username), new AMFDataItem(room));
	}
	/**
	 * Close
	 * @param client
	 * @param function
	 * @param params
	 */
	public void closeSession(IClient client, RequestFunction function,
			AMFDataList params) {
		WMSProperties props = client.getProperties();
		String username =  props.getPropertyStr("username");
		String room =  props.getPropertyStr("room");
		getLogger().info("Videochat speakapps - closeSession "+room);
		_appInstance.broadcastMsg("closeSessionClient", new AMFDataItem(username), new AMFDataItem(room));
	}

	/**
	 * Lock
	 * @param client
	 * @param function
	 * @param params
	 */
	public void lockMeeting(IClient client, RequestFunction function,
			AMFDataList params) {
		WMSProperties props = client.getProperties();
		String username =  props.getPropertyStr("username");
		String room =  props.getPropertyStr("room");
		getLogger().info("Videochat speakapps - lockSession "+room);
		_appInstance.broadcastMsg("lockSessionClient", new AMFDataItem(username), new AMFDataItem(room));
	}
	/**
	 * Register a new user
	 * @param client
	 * @param function
	 * @param params
	 */
	public void registerUser(IClient client, RequestFunction function,
			AMFDataList params) {
		WMSProperties props = client.getProperties();
		String userkey =  props.getPropertyStr("userkey");
		String username =  props.getPropertyStr("username");
		String room =  props.getPropertyStr("room");
		String publishName =  props.getPropertyStr("publishName");
		getLogger().info("Videochat speakapps - registerUser username: "+username+" room: "+room+" publishName: "+publishName);
		_appInstance.broadcastMsg("registeredUser", new AMFDataItem(userkey), new AMFDataItem(username), new AMFDataItem(room), new AMFDataItem(publishName));
	}

	/**
	 * Send Message
	 * @param client
	 * @param function
	 * @param params
	 */
	public void sendChatMessage(IClient client, RequestFunction function,
			AMFDataList params) {
		WMSProperties props = client.getProperties();
		String userkey =  props.getPropertyStr("userkey");
		String username =  props.getPropertyStr("username");
		String room =  props.getPropertyStr("room");
		String message = params.getString(PARAM1);
		if (message!=null && message.length()>0) {
			getLogger().info("Videochat speakapps - sendChatMessage username: "+username+" room: "+room+" message: "+message);
			_appInstance.broadcastMsg("newChatMessage", new AMFDataItem(userkey), new AMFDataItem(username), new AMFDataItem(room), new AMFDataItem(message));
		}
	}
	// *************
	

	public void onAppStop(IApplicationInstance appInstance) {
		String fullname = appInstance.getApplication().getName() + "/"
				+ appInstance.getName();
		getLogger().info("Videochat speakapps - onAppStop: " + fullname);
	}

}