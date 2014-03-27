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
		getLogger().error("Videochat speakapps - onAppStart: " + fullname);
	}


	public void onConnect(IClient client, RequestFunction function,
			AMFDataList params) {
		getLogger().error("Videochat speakapps - onConnect: " + client.getClientId());
		WMSProperties props = client.getProperties();
		props.setProperty("username", params.getString(PARAM1));
		props.setProperty("room", params.getString(PARAM2));
		getLogger().error("Videochat speakapps - username: " + params.getString(PARAM1));
		getLogger().error("Videochat speakapps - room: " + params.getString(PARAM2));
	}

	/**
	 * On connection Accepted the user is inside our connection
	 * @param client
	 */
	public void onConnectAccept(IClient client) {
		getLogger().error("Videochat speakapps - onConnectAccept: " + client.getClientId());
		WMSProperties props = client.getProperties();
		client.call("setRoom", null, props.getPropertyStr("room"));
		client.call("setUsername", null, props.getPropertyStr("username"));
		
	}

	/**
	 * The connection was rejected
	 * @param client
	 */
	public void onConnectReject(IClient client) {
		getLogger().error("Videochat speakapps - onConnectReject: " + client.getClientId());
	}

	/**
	 * The client is disconnected we have to notify to videochat
	 * @param client
	 */
	public void onDisconnect(IClient client) {
		getLogger().error("Videochat speakapps - onDisconnect: " + client.getClientId());
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
		getLogger().error("Videochat speakapps - startRecordClient "+room);
		_appInstance.broadcastMsg("startRecordClient", new AMFDataItem(room));
		//sendResult(client, params, "Hello Wowza");
	}
	// *************
	

	public void onAppStop(IApplicationInstance appInstance) {
		String fullname = appInstance.getApplication().getName() + "/"
				+ appInstance.getName();
		getLogger().error("Videochat speakapps - onAppStop: " + fullname);
	}

}