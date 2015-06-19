package edu.uoc.speakapps;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
//import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Owner;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.SetObjectAclRequest;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.wowza.util.FileUtils;
import com.wowza.util.SystemUtils;
import com.wowza.wms.application.*;
import com.wowza.wms.amf.*;
import com.wowza.wms.client.*;
//import com.wowza.wms.livestreamrecord.manager.IStreamRecorderConstants;
//import com.wowza.wms.livestreamrecord.manager.StreamRecorderParameters;
import com.wowza.wms.livestreamrecord.model.ILiveStreamRecord;
import com.wowza.wms.livestreamrecord.model.LiveStreamRecorderFLV;
import com.wowza.wms.livestreamrecord.model.LiveStreamRecorderMP4;
import com.wowza.wms.module.*;
import com.wowza.wms.request.*;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.IMediaWriterActionNotify;
import com.wowza.wms.stream.MediaReaderItem;
import com.wowza.wms.stream.MediaStream;
import com.wowza.wms.vhost.IVHost;

public class Videochat extends ModuleBase  {
	
	private Map<String, ILiveStreamRecord> recorders = new HashMap<String, ILiveStreamRecord>();
	//private Map<String, List<String>> list_of_streams = null;
	public static IApplicationInstance _appInstance;
	public static final int FORMAT_UNKNOWN = 0;
	public static final int FORMAT_FLV = 1;
	public static final int FORMAT_MP4 = 2;
	public static final String RECORD_FOLDER = "record";
	
	

	public void onAppStart(IApplicationInstance appInstance) {
		
		_appInstance = appInstance;
		
		//list_of_streams = new Hashtable<String, new ArrayList<String>()> ();
		
		String fullname = appInstance.getApplication().getName() + "/"
				+ appInstance.getName()+" "+appInstance;
		//getLogger().info("Videochat  - onAppStart: " + fullname);
		WMSProperties props = appInstance.getProperties();
		
		String fileMoverDestinationPath = props.getPropertyStr("fileMoverDestinationPath", null);
		String access_key_id = props.getPropertyStr("access_key_id", null);
		String bucketName = props.getPropertyStr("bucketName", null);
		String secret_access_key = props.getPropertyStr("secret_access_key", null);
		//getLogger().info("Videochat on app es bucketName "+bucketName);

		if (fileMoverDestinationPath!=null || access_key_id!=null) {
			String region = props.getPropertyStr("region", null);
			String s3_folder = props.getPropertyStr("s3_folder", null);
			String fileMoverFileExtension = props.getPropertyStr("fileMoverFileExtension", null);
			boolean fileMoverVersionFile = props.getPropertyBoolean("fileMoverVersionFile", true);
			boolean fileMoverDeleteOriginal = props.getPropertyBoolean("fileMoverDeleteOriginal", false);
			String endpoint = props.getPropertyStr("endpoint", null);
			String aws_owner_object = props.getPropertyStr("aws_owner_object", null);
			boolean set_acl_as_public = props.getPropertyBoolean("set_acl_as_public", false);
			boolean protocol_is_http = props.getPropertyBoolean("protocol_is_http", false);
			
			appInstance.addMediaWriterListener(new WriteListener( bucketName, region, s3_folder, access_key_id, secret_access_key, 
					fileMoverDestinationPath, fileMoverFileExtension, 
				fileMoverVersionFile, fileMoverDeleteOriginal, endpoint, set_acl_as_public, protocol_is_http, aws_owner_object));
		}
	}
	
	private void sendMessage() {
		
	}


	/**
	 * Invoked when a client connection is initiated
	 * @param client
	 * @param function
	 * @param params
	 */
	public void onConnect(IClient client, RequestFunction function,
			AMFDataList params) {
		//getLogger().info("Videochat  - onConnect: " + client.getClientId());
		WMSProperties props = client.getProperties();
		props.setProperty("username", params.getString(PARAM1));
		props.setProperty("room", params.getString(PARAM2));
		props.setProperty("publishName", params.getString(PARAM3));
		props.setProperty("userkey", params.getString(PARAM4));
		props.setProperty("extra_role", params.getString(PARAM5)!=null?params.getString(PARAM5):"");
		props.setProperty("connectionAccepted", "false");
		props.setProperty("startedRecording", "false");				
		/*getLogger().info("Videochat  - username: " + params.getString(PARAM1));
		getLogger().info("Videochat  - room: " + params.getString(PARAM2));
		getLogger().info("Videochat  - publishName: " + params.getString(PARAM3));*/
	}

	/**
	 * On connection Accepted the user is inside our connection
	 * @param client
	 */
	public void onConnectAccept(IClient client) {
		//TODO notify to clients
		//getLogger().info("Videochat  - onConnectAccept: " + client.getClientId());
		/*WMSProperties props = client.getProperties();
		client.call("setRoom", null, props.getPropertyStr("room"));
		client.call("setUsername", null, props.getPropertyStr("username"));
		*/
	}
	
	/*public void onHTTPSessionCreate(IHTTPStreamerSession httpSession)
	{
		IApplicationInstance appInstance = httpSession.getAppInstance();
		IApplication app = appInstance.getApplication();
		httpSession.
	}*/

	/**
	 * The connection was rejected
	 * @param client
	 */
	public void onConnectReject(IClient client) {
		getLogger().info("Videochat  - onConnectReject: " + client.getClientId());
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
		String streamName =  props.getPropertyStr("publishName");
		boolean connectionAccepted =  "true".equals(props.getProperty("connectionAccepted"));
		if (connectionAccepted) {
			getLogger().info("Videochat  - onDisconnect: " + client.getClientId());
			boolean startedRecording =  "true".equals(props.getProperty("startedRecording"));
			if (startedRecording) { 
				stopRecordingModule(streamName);
			}
			_appInstance.broadcastMsg("disconnectUserClient", new AMFDataItem(userkey), new AMFDataItem(username), new AMFDataItem(room));
		}
		else {
			getLogger().info("Videochat  - onDisconnect: user not connected " + client.getClientId()+"  connection value "+props.getProperty("connectionAccepted"));
			
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
		if ("local".equals(params.getString(PARAM1))) {
			
			WMSProperties props = client.getProperties();
			String streamName =  props.getPropertyStr("publishName");
			
			IMediaStream stream = _appInstance.getStreams().getStream(streamName);
			String dir = _appInstance.getStreamStorageDir() + File.separator + RECORD_FOLDER + File.separator;
			int format = FORMAT_MP4;
			String extension = format==FORMAT_MP4?".mp4":".flv";
			String outputPath = dir + streamName.replaceAll("_", File.separator) + extension; 
			boolean append = false;
			boolean recordData = true;
			boolean startOnKeyFrame = false;
			boolean versionFile = false;
			if (stream != null) {
				recordStream(stream, format, append, outputPath, versionFile , startOnKeyFrame , recordData );
				props.setProperty("startedRecording", "true");				
			}
			else {
				getLogger().warn("Videochat.startRecording: stream not found: "+streamName);
			}
			
			/*StreamRecorderParameters recordParams = new StreamRecorderParameters(this._appInstance);
			recordParams.segmentationType = IStreamRecorderConstants.SEGMENT_NONE;
			recordParams.versioningOption= IStreamRecorderConstants.OVERWRITE_FILE;
			recordParams.versioningOption = IStreamRecorderConstants.OVERWRITE_FILE;
			recordParams.fileFormat = IStreamRecorderConstants.FORMAT_MP4;
			//recordParams.outputPath = "ssss";
			//recordParams.recordData = IStreamRecorderConstants.PROPERTY_OUTPUT_FILE
			ILiveStreamRecordManager liveStreamRecordManager = _appInstance.getVHost().getLiveStreamRecordManager();
			getLogger().info("Videochat  - Created ILiveStreamRecordManager "+liveStreamRecordManager.toString());
			ILiveStreamRecordManagerActionNotify listenerRecorder = new ILiveStreamRecordManagerActionNotify() {
				
				@Override
				public IStreamRecorder recordFactory(String arg0,
						StreamRecorderParameters arg1) {
					getLogger().info("Videochat  - Created ILiveStreamRecordManager recordFactory ");
					return null;
				}
				
				@Override
				public void onSwitchRecord(IStreamRecorder streamRecorder, IMediaStream mediaStream) {
					getLogger().info("Videochat  - Created ILiveStreamRecordManager onSwitchRecord "+streamRecorder.getBaseFilePath()+" mediaStream "+mediaStream.getContextStr());
				}
				
				@Override
				public void onStopRecord(IStreamRecorder streamRecorder) {
					getLogger().info("Videochat  - Created ILiveStreamRecordManager onStopRecord "+streamRecorder.getBaseFilePath());
				}
				
				@Override
				public void onStartRecord(IStreamRecorder streamRecorder) {
					getLogger().info("Videochat  - Created ILiveStreamRecordManager onStartRecord "+streamRecorder.getBaseFilePath());
				}
				
				@Override
				public void onSplitRecord(IStreamRecorder streamRecorder) {
					getLogger().info("Videochat  - Created ILiveStreamRecordManager onSplitRecord "+streamRecorder.getBaseFilePath());
				}
				
				@Override
				public void onCreateRecord(IStreamRecorder streamRecorder) {
					getLogger().info("Videochat  - Created ILiveStreamRecordManager onCreateRecord "+streamRecorder.getBaseFilePath());
				}
			};
			getLogger().info("Videochat  - attached Listener "+listenerRecorder.toString());
			liveStreamRecordManager.addListener(listenerRecorder );
			getLogger().info("Videochat  - BEFORE startRecording ");
			liveStreamRecordManager.startRecording(_appInstance, recordParams);
			getLogger().info("Videochat  - AFTER startRecording ");*/
			
		} else {
			getLogger().info("Videochat  - send broad message startRecord");
			WMSProperties props = client.getProperties();
			String room =  props.getPropertyStr("room");
			String username =  props.getPropertyStr("username");
			//Try to delete the folder if we are not using versioning
			String publishName =  props.getPropertyStr("publishName");
			int pos = publishName.lastIndexOf("_");
			if (pos>0) {
				String folder_to_delete = publishName.substring(0, pos);
				folder_to_delete = _appInstance.getStreamStoragePath()+"/record/"+folder_to_delete.replaceAll("_", "/");
				getLogger().info("Videochat  - startRecord "+room+" deleting folder "+folder_to_delete);
				if (!props.getPropertyBoolean("fileMoverVersionFile", true)) {
					
					File folder = new File(folder_to_delete);
					boolean deletedFolder = deleteFolder(folder);
				
					getLogger().info("Videochat  - startRecord "+room+" deleted folder "+deletedFolder+" folder: "+
							folder_to_delete);
				}
				else {
					getLogger().info("Videochat  - startRecord "+room+" we won't delete folder "+folder_to_delete+
							" because we are using versioning");
				}
			}
			props.setProperty("startedRecording", "true");				
			_appInstance.broadcastMsg("startRecordClient", new AMFDataItem(username), new AMFDataItem(room));
		}
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
			    getLogger().info("Videochat  - startRecordClient success "+success+" deleting folder ");
				
			}
		} catch (Exception ioe) {
			getLogger().info("Videochat  Deleting folder- "+folder.getAbsolutePath(), ioe);
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
		getLogger().warn("Videochat  - stopRecordClient "+room+" username "+username+" client: "+client.getClientId());
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
		getLogger().info("Videochat  - closeSession "+room+" client: "+client.getClientId());
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
		getLogger().info("Videochat  - lockSession "+room+" client: "+client.getClientId());
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
		String extra_role =  props.getPropertyStr("extra_role");
		props.setProperty("connectionAccepted", "true");
		String publishName =  props.getPropertyStr("publishName");
		getLogger().info("Videochat  - registerUser username: "+username+" room: "+room+" publishName: "+publishName+" client: "+client.getClientId());
		_appInstance.broadcastMsg("registeredUser", new AMFDataItem(userkey), new AMFDataItem(username), new AMFDataItem(room), new AMFDataItem(publishName), new AMFDataItem(extra_role));
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
			getLogger().info("Videochat  - sendChatMessage username: "+username+" room: "+room+" message: "+message+" client: "+client.getClientId());
			_appInstance.broadcastMsg("newChatMessage", new AMFDataItem(userkey), new AMFDataItem(username), new AMFDataItem(room), new AMFDataItem(message));
			List<IClient> list = _appInstance.getClients();
			
			/*for (IClient iClient : list) {
				getLogger().info("Videochat  - sendChatMessage "+iClient.getIp());
			}*/
			
		}
	}
	// *************
	
	private void cleanupRecorders() {
		// cleanup any recorders that are still running
		synchronized (recorders)
		{
			Iterator<String> iter = recorders.keySet().iterator();
			while(iter.hasNext())
			{
				String streamName = iter.next();
				ILiveStreamRecord recorder = recorders.get(streamName);
				recorder.stopRecording();
				getLogger().info("  stopRecording: "+streamName);
			}
			recorders.clear();
		}
	}
	public void onAppStop(IApplicationInstance appInstance) {
		String fullname = appInstance.getApplication().getName() + "/"
				+ appInstance.getName();
		getLogger().info("Videochat  - onAppStop: " + fullname);
		// cleanup any recorders that are still running
		cleanupRecorders();
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
	
	public void recordStream(String streamName, int format, boolean append, String outputPath, boolean versionFile, boolean startOnKeyFrame, boolean recordData)
	{
		IMediaStream stream = _appInstance.getStreams().getStream(streamName);
		if (stream != null)
			recordStream(stream, format, append, outputPath, versionFile, startOnKeyFrame, recordData);
		else
			getLogger().warn("ModuleLiveStreamRecord.recordStream: Stream not found: "+streamName);
	}
	
	public void recordStream(IMediaStream stream, int format, boolean append, String outputPath, boolean versionFile, boolean startOnKeyFrame, boolean recordData)
	{

		String streamName = stream.getName();
		
		// if a format was not specified then check the stream prefix and choose accordingly
		if (format == FORMAT_UNKNOWN)
		{
			format = FORMAT_FLV;
			String extStr = stream.getExt();
			if (extStr.equals("mp4"))
				format = FORMAT_MP4;
		}
		
		String params = "stream:"+streamName;
		params += " format:"+(format==FORMAT_MP4?"mp4":"flv");
		params += " append:"+append;
		if (outputPath != null) {
			new File(outputPath).mkdirs();
			params += " outputPath:"+outputPath;
		}
		else
		{
			File writeFile = stream.getStreamFileForWrite(stream.getName(), (format==FORMAT_MP4?MediaStream.MP4_STREAM_EXT:MediaStream.BASE_STREAM_EXT), stream.getQueryStr());
			params += " outputPath:"+writeFile.getAbsolutePath();
		}
		params += " versionFile:"+versionFile;
		params += " startOnKeyFrame:"+startOnKeyFrame;
		params += " recordData:"+recordData;
		
		getLogger().info("Videochat.startRecording: "+params);
		
		// create a stream recorder and save it in a map of recorders
		ILiveStreamRecord recorder = null;
		
		// create the correct recorder based on format
		if (format == FORMAT_MP4)
			recorder = new LiveStreamRecorderMP4();
		else
			recorder = new LiveStreamRecorderFLV();
		
		// add it to the recorders list
		synchronized (recorders)
		{
			ILiveStreamRecord prevRecorder = recorders.get(streamName);
			if (prevRecorder != null)
				prevRecorder.stopRecording();
			recorders.put(streamName, recorder);
		}
		
		// if you want to record data packets as well as video/audio
		recorder.setRecordData(recordData);
		
		// Set to true if you want to version the previous file rather than overwrite it
		recorder.setVersionFile(versionFile);
		
		// If recording only audio set this to false so the recording starts immediately
		recorder.setStartOnKeyFrame(startOnKeyFrame);
		
		getLogger().info("Videochat.startRecording: outputPath "+outputPath);
		// start recording
		recorder.startRecording(stream, outputPath, append);
	}
	/**
	 * Stop recording
	 * @param client
	 * @param function
	 * @param params
	 */
	public void stopRecordingModule(IClient client, RequestFunction function,
			AMFDataList params) {
		WMSProperties props = client.getProperties();
		String streamName =  props.getPropertyStr("publishName");
		getLogger().info("Videochat  - stop recording "+streamName+" client: "+client.getClientId());
		ArrayList<IClient> clients = new ArrayList<IClient>();
		clients.add(client);
		_appInstance.broadcastMsg(clients, "stopedRecording", new AMFDataItem(streamName), new AMFDataItem(stopRecordingModule(streamName)));
	}

	/**
	 * Stop recording
	 * @param streamName
	 * @return
	 */
	private String stopRecordingModule(String streamName)
	{
		ILiveStreamRecord recorder = null;
		synchronized (recorders)
		{
			if (!recorders.containsKey(streamName)) {
				getLogger().info("Videochat.stopRecording: stream recorder not found: "+streamName);
				return null;
			}
			recorder = recorders.remove(streamName);
		}
		
		String outputPath  = null;
		if (recorder != null)
		{
			// grab the current path to the recorded file
			outputPath = recorder.getFilePath();
			
			// stop recording
			recorder.stopRecording();
			if (outputPath.startsWith(_appInstance.getStreamStorageDir())) {
				outputPath = outputPath.substring(_appInstance.getStreamStorageDir().length()+1);
			}
			
		}
		else {
			getLogger().warn("Videochat.stopRecording: stream recorder not found: "+streamName);
		}
		
		return outputPath;
	}
	
	/**
	 * Disable video image
	 * @param client
	 * @param function
	 * @param params
	 */
	public void disableVideo(IClient client, RequestFunction function,
			AMFDataList params) {
		WMSProperties props = client.getProperties();
		String userkey =  props.getPropertyStr("userkey");
		String room =  props.getPropertyStr("room");
		getLogger().info("Videochat  - disableVideo "+room+" userkey: "+userkey);
		_appInstance.broadcastMsg("disableVideoClient", new AMFDataItem(userkey), new AMFDataItem(room));
	}
	/**
	 * Enable Video
	 * @param client
	 * @param function
	 * @param params
	 */
	public void enableVideo(IClient client, RequestFunction function,
			AMFDataList params) {
		WMSProperties props = client.getProperties();
		String userkey =  props.getPropertyStr("userkey");
		String room =  props.getPropertyStr("room");
		getLogger().info("Videochat  - enableVideo "+room+" client: "+client.getClientId());
		_appInstance.broadcastMsg("enableVideoClient", new AMFDataItem(userkey), new AMFDataItem(room));
	}
	
	/*public void stopRecordingModule(IClient client, RequestFunction function, AMFDataList params)
	{
		String streamName = params.getString(PARAM1);
		getLogger().info("Videochat.stopRecording: "+streamName);
		
		ILiveStreamRecord recorder = null;
		synchronized (recorders)
		{
			recorder = recorders.remove(streamName);
		}
		
		if (recorder != null)
		{
			// grab the current path to the recorded file
			String outputPath = recorder.getFilePath();
			
			// stop recording
			recorder.stopRecording();
		}
		else
			getLogger().warn("Videochat.stopRecording: stream recorder not found: "+streamName);
	}*/
	
	
	//*** Per moure els fitxers 
	private static class MoverThread
    implements Runnable {
		private String fileMoverDestinationPath = null;
		private String fileMoverFileExtension = null;
		private boolean fileMoverVersionFile = true;
		private boolean fileMoverDeleteOriginal = false;
		private File file;
		private IMediaStream stream;
		private final static String  SEPARATOR = "_sFxQY_";
		
		public MoverThread(String fileMoverDestinationPath, String fileMoverFileExtension, 
				boolean fileMoverVersionFile, boolean fileMoverDeleteOriginal,
				File file, IMediaStream stream) {
			this.fileMoverDestinationPath = fileMoverDestinationPath ;
			this.fileMoverFileExtension = fileMoverFileExtension;
			this.fileMoverVersionFile = fileMoverVersionFile;
			this.fileMoverDeleteOriginal = fileMoverDeleteOriginal;
			this.file = file;
			this.stream = stream;
		}

	    public void run() {
			if (fileMoverDestinationPath != null)
			{
				String streamName = FileUtils.toValidFilename(stream.getName().replaceAll(File.separator, SEPARATOR));
				String streamExt = getExtension(stream, fileMoverFileExtension);
				
				Map<String, String> envMap = new HashMap<String, String>();
				
				IApplicationInstance appInstance = stream.getStreams().getAppInstance();
				IVHost vhost = appInstance.getVHost();
				
				envMap.put("com.wowza.wms.context.VHost", vhost.getName());
				envMap.put("com.wowza.wms.context.VHostConfigHome", vhost.getHomePath());
				envMap.put("com.wowza.wms.context.Application", appInstance.getApplication().getName());
				envMap.put("com.wowza.wms.context.ApplicationInstance", appInstance.getName());
				envMap.put("com.wowza.wms.context.StreamName", streamName);
				
				String destinationPath =  SystemUtils.expandEnvironmentVariables(fileMoverDestinationPath, envMap);
				
				
				if (fileMoverDestinationPath.indexOf("com.wowza.wms.context.StreamName") < 0)
					destinationPath += "/"+streamName+"."+streamExt;
				destinationPath = destinationPath.replaceAll(SEPARATOR, File.separator);
				if (!destinationPath.endsWith("."+streamExt)) {
					destinationPath += "."+streamExt;
				}

				File dstFile = new File(destinationPath);
				
				getLogger().info("Thread Videochat - ModuleMediaWriterFileMover.onWriteComplete: from: "+file+" to folder:"+dstFile.getParentFile()+" and to file "+dstFile);
		
				try
				{
					dstFile.getParentFile().mkdirs();
				}
				catch(Exception e)
				{
					getLogger().warn("Thread Videochat - ModuleMediaWriterFileMover.onWriteComplete[mkdir]: ["+dstFile+"]: "+e.toString());
				}
				
				try
				{
					if (dstFile.exists())
					{
						if (fileMoverVersionFile)
							FileUtils.versionFile(dstFile);
						else
							dstFile.delete();
					}
				}
				catch(Exception e)
				{
					getLogger().warn("Thread Videochat - ModuleMediaWriterFileMover.onWriteComplete[version]: ["+dstFile+"]: "+e.toString());
				}
				
				try
				{
					FileUtils.copyFile2(file, dstFile);
				}
				catch(Exception e)
				{
					getLogger().warn("Thread Videochat - ModuleMediaWriterFileMover.onWriteComplete[version]: ["+dstFile+"]: "+e.toString());
				}
		
				try
				{
					if (fileMoverDeleteOriginal && dstFile.exists())
					{
						file.delete();
					}
				}
				catch(Exception e)
				{
					getLogger().warn("Thread Videochat - ModuleMediaWriterFileMover.onWriteComplete[delOrig]: ["+dstFile+"]: "+e.toString());
				}
			}
	    }

		private static String getExtension(IMediaStream stream, String ext)
		{
			if (ext == null)
			{
				String mediaReaderType = stream.getExt().toLowerCase();
				MediaReaderItem mediaReaderItem = stream.getStreams().getVHost().getMediaReaders().getMediaReaderDef(mediaReaderType);
				return mediaReaderItem.getFileExtension();
			}
			
			return ext;
		}
	}
	private static class MoverThreadS3
    implements Runnable {
		private String bucketName = null;
		private String region = null;
		private String s3_folder = null;
		private String access_key_id = null;
		private String secret_access_key = null;
		private String fileMoverFileExtension = null;
		private File file;
		private IMediaStream stream;
		//To override the default amazon host
		private String endpoint;
		private boolean set_acl_as_public;
		private boolean protocol_is_http;
		private String owner;
		private boolean fileMoverVersionFile = true;
		
		public MoverThreadS3(String bucketName, String region, String s3_folder, String access_key_id, String secret_access_key, 
				String fileMoverFileExtension,
				File file, IMediaStream stream,
				String endpoint, boolean set_acl_as_public, boolean protocol_is_http, 
				String owner, boolean fileMoverVersionFile) {
			this.bucketName = bucketName ;
			this.region = region;
			this.s3_folder = s3_folder;
			this.access_key_id = access_key_id ;
			this.secret_access_key = secret_access_key;
			this.fileMoverFileExtension = fileMoverFileExtension;
			this.file = file;
			this.stream = stream;
			this.endpoint = endpoint;
			this.set_acl_as_public = set_acl_as_public;
			this.protocol_is_http = protocol_is_http;
			this.owner = owner;
			this.fileMoverVersionFile = fileMoverVersionFile;
		}

	    public void run() {
	    	
			if (bucketName !=null && access_key_id != null && secret_access_key !=null)
			{
				try {
					BasicAWSCredentials awsCreds = new BasicAWSCredentials(access_key_id, secret_access_key);
					AmazonS3 s3 = null;
					String streamExt = getExtension(stream, fileMoverFileExtension);
					
					String key = stream.getName();
					if (!key.endsWith("."+streamExt)) {
						key += "."+streamExt;
					}
					if (s3_folder!=null && s3_folder.length()>0){
						key = s3_folder + "/" +key;
					}
					
					ClientConfiguration clientConfiguration = new ClientConfiguration();

					if (protocol_is_http) {
						clientConfiguration.setProtocol(Protocol.HTTP);
					}
					s3 = new AmazonS3Client(awsCreds, clientConfiguration );
					if (endpoint!=null && endpoint.length()>0) {
						s3.setEndpoint(endpoint);
					}
					
					
					if (region!=null){						
						Region customRegion = Region.getRegion(Regions.fromName(region));
						//s3.setEndpoint(customRegion.getServiceEndpoint(ServiceAbbreviations.S3));
						
						s3.setRegion(customRegion);						
					}
					
					if (fileMoverVersionFile && fileExist(s3, bucketName, key)) {
						//rename previous name
						String new_key = key;
						if (new_key.endsWith("."+streamExt)) {
							new_key = new_key.substring(0, new_key.lastIndexOf("."));
						}
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						
						new_key += "_"+format.format(cal.getTime());
						new_key += "."+streamExt;
						
						s3.copyObject(bucketName, key, 
								bucketName, new_key);
					}

					s3.putObject(new PutObjectRequest(bucketName, key,
							file));
					if (set_acl_as_public && owner!=null) {						
						AccessControlList acl = new AccessControlList();
						acl.setOwner(new Owner(owner, key));
						acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
						SetObjectAclRequest acl_req = new SetObjectAclRequest(bucketName, key, acl );
						s3.setObjectAcl(acl_req);
					}
					
					boolean deleted = file.delete();

					if (deleted) {
						getLogger().info("Thread Videochat - ModuleMediaWriterFileMoverS3.onWriteComplete[version]: S3 uploaded "+key+" SUCESSFULLY and deleted!!!");
					} else {
						getLogger().warn("Thread Videochat - ModuleMediaWriterFileMoverS3.onWriteComplete[version]: S3 uploaded "+key+" SUCESSFULLY and but can't deleted it!!! "+file.getAbsolutePath());
					}
				}		
				catch(Throwable e)
				{
					getLogger().error("Thread Videochat - ERROR - ModuleMediaWriterFileMoverS3.onWriteComplete[version]: : "+e.toString(), e);
				}
			} else {
				getLogger().warn("Thread Videochat - WARN - ModuleMediaWriterFileMoverS3.onWriteComplete missing some required parameter : bucketName is not null: "+(bucketName !=null)+". access_key_id is not null: " + (access_key_id != null) + ". secret_access_key is not null: "+ (secret_access_key !=null));
			}
	    }
	    
	    private static boolean fileExist(AmazonS3 s3,
	            String bucketName,
	            String path) throws AmazonClientException, AmazonServiceException {
	        boolean fileExist = true;
	        try {
	            ObjectMetadata objectMetadata = s3.getObjectMetadata(bucketName, path);
	        } catch (AmazonS3Exception s3e) {
	            if (s3e.getStatusCode() == 404) {
	            // i.e. 404: NoSuchKey - The specified key does not exist
	            	fileExist = false;
	            }
	            else {
	                throw s3e;    // rethrow all S3 exceptions other than 404   
	            }
	        }

	        return fileExist;
	    }
	    
		private static String getExtension(IMediaStream stream, String ext)
		{
			if (ext == null)
			{
				String mediaReaderType = stream.getExt().toLowerCase();
				MediaReaderItem mediaReaderItem = stream.getStreams().getVHost().getMediaReaders().getMediaReaderDef(mediaReaderType);
				return mediaReaderItem.getFileExtension();
			}
			
			return ext;
		}
	}
	
	class WriteListener implements IMediaWriterActionNotify
	{
		private String bucketName = null;
		private String access_key_id = null;
		private String secret_access_key = null;
		private String s3_folder = null;
		private String region = null;
		private String fileMoverDestinationPath = null;
		private String fileMoverFileExtension = null;
		private boolean fileMoverVersionFile = true;
		private boolean fileMoverDeleteOriginal = false;
		//To allow external services such us ilimit
		private String endpoint = null;
		private boolean set_acl_as_public = false;
		private boolean protocol_is_http = false;
		private String aws_owner_object = null;
		
		
		public WriteListener(String bucketName, String region, String s3_folder, String access_key_id, String secret_access_key, String fileMoverDestinationPath, String fileMoverFileExtension, 
				boolean fileMoverVersionFile, boolean fileMoverDeleteOriginal, String endpoint,
				boolean set_acl_as_public, boolean protocol_is_http, String aws_owner_object) {
			this.bucketName = bucketName;
			this.region = region;
			this.s3_folder = s3_folder;
			this.access_key_id = access_key_id;
			this.secret_access_key = secret_access_key;
			this.fileMoverDestinationPath = fileMoverDestinationPath ;
			this.fileMoverFileExtension = fileMoverFileExtension;
			this.fileMoverVersionFile = fileMoverVersionFile;
			this.fileMoverDeleteOriginal = fileMoverDeleteOriginal;
			this.endpoint = endpoint;
			this.set_acl_as_public = set_acl_as_public;
			this.protocol_is_http = protocol_is_http;
			this.aws_owner_object = aws_owner_object;
		}

		
		/**
		 * Onwrite complete move the file
		 */
		public void onWriteComplete(IMediaStream stream, File file)
		{
			try {

				//using S3
				if (bucketName!=null && access_key_id != null && secret_access_key!= null) {
					Thread t = new Thread(new MoverThreadS3(bucketName, region, s3_folder, access_key_id, secret_access_key,
							fileMoverFileExtension,
							file, stream, endpoint, set_acl_as_public, protocol_is_http, aws_owner_object, fileMoverVersionFile));
					t.start();
				}
				else {
					//using file mover
					if (fileMoverDestinationPath != null)
					{
						Thread t = new Thread(new MoverThread(fileMoverDestinationPath, fileMoverFileExtension, 
								fileMoverVersionFile, fileMoverDeleteOriginal,
								file, stream));
					    t.start();
					}				
				}
			} catch (Throwable e){
				getLogger().error("onWriteComplete - ERROR - ModuleMediaWriterFileMoverS3.onWriteComplete[version]: : "+e.toString(), e);	
			}
		}
		
		public void onFLVAddMetadata(IMediaStream stream, Map<String, Object> extraMetadata)
		{
		}
	}
}
