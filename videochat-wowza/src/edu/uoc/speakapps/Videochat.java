package edu.uoc.speakapps;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.wowza.util.FLVUtils;
import com.wowza.wms.application.*;
import com.wowza.wms.amf.*;
import com.wowza.wms.client.*;
import com.wowza.wms.module.*;
import com.wowza.wms.request.*;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.MediaStreamMap;
import com.wowza.wms.vhost.IVHost;

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
		props.setProperty("connectionAccepted", "false");
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
		boolean connectionAccepted =  "true".equals(props.getProperty("connectionAccepted"));
		if (connectionAccepted) {
			getLogger().info("Videochat speakapps - onDisconnect: " + client.getClientId());
			_appInstance.broadcastMsg("disconnectUserClient", new AMFDataItem(userkey), new AMFDataItem(username), new AMFDataItem(room));
		}
		else {
			getLogger().info("Videochat speakapps - onDisconnect: user not connected " + client.getClientId()+"  connection value "+props.getProperty("connectionAccepted"));
			
		}
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
			getLogger().info("Videochat speakapps Deleting folder- "+folder.getAbsolutePath(), ioe);
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
		getLogger().info("Videochat speakapps - stopRecordClient "+room+" username "+username+" client: "+client.getClientId());
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
		getLogger().info("Videochat speakapps - closeSession "+room+" client: "+client.getClientId());
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
		getLogger().info("Videochat speakapps - lockSession "+room+" client: "+client.getClientId());
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
		props.setProperty("connectionAccepted", "true");
		String publishName =  props.getPropertyStr("publishName");
		getLogger().info("Videochat speakapps - registerUser username: "+username+" room: "+room+" publishName: "+publishName+" client: "+client.getClientId());
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
			getLogger().info("Videochat speakapps - sendChatMessage username: "+username+" room: "+room+" message: "+message+" client: "+client.getClientId());
			_appInstance.broadcastMsg("newChatMessage", new AMFDataItem(userkey), new AMFDataItem(username), new AMFDataItem(room), new AMFDataItem(message));
		}
	}
	// *************
	

	public void onAppStop(IApplicationInstance appInstance) {
		String fullname = appInstance.getApplication().getName() + "/"
				+ appInstance.getName();
		getLogger().error("Videochat speakapps - onAppStop: " + fullname);
	}

	public void createSnapshotLiveVideochat(IClient client, RequestFunction function, AMFDataList params) 
	{
		//No l'usem perque necessitem ffmpeg http://www.wowza.com/forums/showthread.php?577-Custom-module-to-create-single-frame-snapshots-of-live-and-VOD-stream
		//ffmpeg -i /Users/antonibertranbellido/Downloads/uocelearncourt/speakApps_2_speakApps_10_162_speakApps_2_10431.flv -vcodec png -vframes 1 -an -f rawvideo -s 240x160 /Users/antonibertranbellido/Downloads/uocelearncourt/speakApps_2_speakApps_10_162_speakApps_2_10431.png
		/*
		String streamName = params.getString(PARAM1);
		 
		String fileName = "";
		IApplicationInstance appInstance = client.getAppInstance();
		MediaStreamMap streams = appInstance.getStreams();
		IMediaStream stream = streams.getStream(streamName);
		if (stream != null)
		{
			AMFPacket packet = stream.getLastKeyFrame();
			if (packet != null)
			{
				fileName = streamName + "_" + packet.getAbsTimecode() + ".flv";
				File newFile = stream.getStreamFileForWrite(streamName, null, null);
				
				String filePath = newFile.getPath().substring(0, newFile.getPath().length()-4) + "_" + packet.getAbsTimecode() + ".flv";
								
				try
				{
					synchronized(lock)
					{
						BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(filePath), false));
						FLVUtils.writeHeader(out, 0, null);
						
						AMFPacket codecConfig = stream.getVideoCodecConfigPacket(packet.getAbsTimecode());
						if (codecConfig != null)
							FLVUtils.writeChunk(out, codecConfig.getDataBuffer(), codecConfig.getSize(), 0, (byte)codecConfig.getType());
						
						FLVUtils.writeChunk(out, packet.getDataBuffer(), packet.getSize(), 0, (byte)packet.getType());
						out.close();
					}
					
					getLogger().info("snapshot created: "+filePath);
				}
				catch (Exception e)
				{
					getLogger().error("createSnapshot: "+e.toString());
				}
			}
		}
		
		sendResult(client, params, fileName);*/
	}
	
	//Object lock = new Object(); 
	 

	
	public void createSnapshotVOD(IClient client, RequestFunction function, AMFDataList params) 
	{
		/*String streamName = params.getString(PARAM1);
		int timecode = params.getInt(PARAM2);
		
		String fileName = "";
		IApplicationInstance appInstance = client.getAppInstance();
		
		String flvFilePath = appInstance.getStreamStoragePath() + "/" + streamName + ".flv";
		File flvFile = new File(flvFilePath);
		
		if (flvFile.exists())
		{
			AMFPacket lastVideoKeyFrame = null;
			try
			{
				BufferedInputStream is = new BufferedInputStream(new FileInputStream(flvFile));
				FLVUtils.readHeader(is);
				AMFPacket amfPacket;
				while ((amfPacket = FLVUtils.readChunk(is)) != null)
				{
					if (lastVideoKeyFrame != null && amfPacket.getTimecode() > timecode)
						break;
					if (amfPacket.getType() != IVHost.CONTENTTYPE_VIDEO)
						continue;
					if (FLVUtils.isVideoKeyFrame(amfPacket)) //if (FLVUtils.getFrameType(amfPacket.getFirstByte()) == FLVUtils.FLV_KFRAME)
						lastVideoKeyFrame = amfPacket;
				}
				is.close();
			}
			catch (Exception e)
			{
				getLogger().error("Error: createSnapshotVOD: reading flv: "+e.toString());
			}
			
			if (lastVideoKeyFrame != null)
			{
				try
				{
					fileName = streamName + "_" + timecode;
					String filePath = appInstance.getStreamStoragePath() + "/" + fileName + ".flv";
					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(filePath), false));
					FLVUtils.writeHeader(out, 0, null);
					FLVUtils.writeChunk(out, lastVideoKeyFrame.getDataBuffer(), lastVideoKeyFrame.getSize(), 0, (byte)lastVideoKeyFrame.getType());
					out.close();
					
					getLogger().info("snapshot created: "+filePath);
				}
				catch (Exception e)
				{
					getLogger().error("Error: createSnapshotVOD: writing flv: "+e.toString());
				}
			}
		}
		
		sendResult(client, params, fileName);
		*/
	}
}