package edu.uoc.speakapps;

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
		getLogger().info("Videochat speakapps - onDisconnect: " + client.getClientId());
	}
	

	// Client call functions ******************
	/**
	 * Send a message to a client
	 * @param client
	 * @param function
	 * @param params
	 */
	public void startRecord(IClient client, RequestFunction function,
			AMFDataList params) {
		//WMSProperties props = client.getProperties();
		String room = params.getString(PARAM1);
		getLogger().info("Videochat speakapps - startRecordClient "+room);
		_appInstance.broadcastMsg("startRecordClient", new AMFDataItem(room));
		//sendResult(client, params, "Hello Wowza");
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
		String username =  props.getPropertyStr("username");
		String room =  props.getPropertyStr("room");
		String publishName =  props.getPropertyStr("publishName");
		getLogger().info("Videochat speakapps - registerUser username: "+username+" room: "+room+" publishName: "+publishName);
		_appInstance.broadcastMsg("registeredUser", new AMFDataItem(username), new AMFDataItem(room), new AMFDataItem(publishName));
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
		String username =  props.getPropertyStr("username");
		String room =  props.getPropertyStr("room");
		String message = params.getString(PARAM1);
		if (message!=null && message.length()>0) {
			getLogger().info("Videochat speakapps - sendChatMessage username: "+username+" room: "+room+" message: "+message);
			_appInstance.broadcastMsg("newChatMessage", new AMFDataItem(username), new AMFDataItem(room), new AMFDataItem(message));
		}
	}
	// *************
	

	public void onAppStop(IApplicationInstance appInstance) {
		String fullname = appInstance.getApplication().getName() + "/"
				+ appInstance.getName();
		getLogger().info("Videochat speakapps - onAppStop: " + fullname);
	}

}