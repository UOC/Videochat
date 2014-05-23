/**
* RED5 Open Source Flash Server - http://www.osflash.org/red5
*
* Copyright (c) 2006-2007 by respective authors (see below). All rights reserved.
*
* This library is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License as published by the Free Software
* Foundation; either version 2.1 of the License, or (at your option) any later
* version.
*
* This library is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
* PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License along
* with this library; if not, write to the Free Software Foundation, Inc.,
* 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/

// ** AUTO-UI IMPORT STATEMENTS **String
//import com.metaliq.controls.ColorPicker;
import mx.controls.TextInput;
import org.red5.ui.controls.IconButton;
import mx.controls.TextArea;
// ** END AUTO-UI IMPORT STATEMENTS **
import org.red5.samples.livestream.videoconference.Connection;
import org.red5.samples.livestream.videoconference.GlobalObject;
import org.red5.samples.livestream.videoconference.TextFilter;
import com.blitzagency.util.LSOUserPreferences;
import org.red5.utils.Delegate;
import org.red5.samples.livestream.videoconference.Broadcaster;
import org.red5.samples.livestream.videoconference.VideoConference;
import com.gskinner.events.GDispatcher; 
import org.red5.samples.livestream.videoconference.Connector;

class org.red5.samples.livestream.videoconference.Chat extends MovieClip {
// Constants:
	public static var CLASS_REF = org.red5.samples.livestream.videoconference.Chat;
	public static var LINKAGE_ID:String = "org.red5.samples.livestream.videoconference.Chat";
// Public Properties:
	public var connection:Connection;
	public var addEventListener:Function
	public var removeEventListener:Function
// Private Properties:
	private var dispatchEvent:Function
	private var connected:Boolean;
	private var so:GlobalObject;
	private var history:Array;
	private var chatID:String;
    public static var defaultUserName:String = "Looser User"
	private var broadcaster:Broadcaster;
	private var si:Number;
	private var controller:VideoConference;
// UI Elements:

// ** AUTO-UI ELEMENTS **
	private var chatBody:TextArea;
	private var clearChat:IconButton;
	private var lockRoom:IconButton;
	private var clr:MovieClip;
	private var message:TextInput;
	private var userName:TextInput;
// ** END AUTO-UI ELEMENTS **

// Initialization:
	private function Chat() {GDispatcher.initialize(this);}
	private function onLoad():Void 
	{ 
		//configUI(); 
	}

// Public Methods:
	public function registerConnection(p_connection:Connection):Void
	{
		connection = p_connection;
	}
	
	public function registerBroadcaster(p_broadcaster:Broadcaster):Void
	{
		broadcaster = p_broadcaster;
	}
	
	public function registerController(p_controller:VideoConference):Void
	{
		controller = p_controller;
	}
	
	public function connectSO(p_soName:String):Void
	{
		// parms
		// @ SO name
		// @ Connection reference
		// @ persistance
		if(p_soName == undefined) p_soName = "SampleChat";
		chatID = p_soName;
		connected = so.connect(p_soName, connection, false);
	}
	
	public function getNames():Void
	{
		// should put out a call to all connected to update the names
		so.sendGetNames();
	}
// Semi-Private Methods:
// Private Methods:
	public function configUI():Void 
	{
		clr.addEventListener("change", Delegate.create(this, colorChange));
		
		userName.addEventListener("change", Delegate.create(this, userNameChange));
		
		// instantiate history object
		history = new Array();
		
		// add key listener for enter key
		Key.addListener(this);
		
		// create GlobalObject
		so = new GlobalObject();
		so.addEventListener("onNewMessage", Delegate.create(this, newMessageHandler));
		so.addEventListener("onNewName", Delegate.create(this, newNameHandler));
		so.addEventListener("onGetName", Delegate.create(this, getNameHandler));
		
		//so.so.newMessage = Delegate.create(this, newMessageHandler);
		//_global.tt("newMessage created?", so.so.newMessage);
		
		// add listener for sync events
		so.addEventListener("onSync", Delegate.create(this, onSyncHandler));
		
		// setup the clearChat button
		clearChat.addEventListener("click", Delegate.create(this, clear));
		clearChat.tooltip = "Clear Chat";
		
		
		lockRoom.addEventListener("click", Delegate.create(this, lockedRoom));
		
		// get preferences
		loadProfile("videoConference");
	}	
	
	
	private function lockedRoom():Void{
		var msg;
		if(_root.locked=="unLocked") msg = TextFilter.encodeText("-- Locked Room --");
		else msg = TextFilter.encodeText("-- unLocked Room --");
		msg = getUserName() + msg;
		so.sendMessage(msg);
		message.text = "";
	}


	private function userNameChange(evtObj:Object):Void
	{
		LSOUserPreferences.setPreference("userName",userName.text, true);
		
		// we put an interval on it since this is basically every keystroke.  For a local shared object, that's fine, but for Red5, that's a bit wordy
		clearInterval(si);
		si = setInterval(this, "updateName", 1000);
	}
	
	private function loadProfile(p_profile):Void
	{
		
		
		LSOUserPreferences.load(p_profile);
		
		if(LSOUserPreferences.getPreference("userName") != undefined)
		{
			//userName.text = defaultUserName;
			
			userName.text=Connector.fullName;
			//cmoyas
			//userName.text = LSOUserPreferences.getPreference("userName");
			
			
		}else
		{
			userName.text = defaultUserName;
			//cmoyas
			//userName.text=Connector.fullName;
		}
		
		var htmlColor = LSOUserPreferences.getPreference("htmlColor");
		if(htmlColor != undefined)
		{
			clr.palette.cLabel.text = htmlColor;
			clr.value = LSOUserPreferences.getPreference("color")
		}
		
		clr.value = LSOUserPreferences.getPreference("color");
		
		// tell everyone what your name is
		clearInterval(si);
		si = setInterval(this, "updateName", 250);
	}
	
	private function colorChange(evtObj:Object):Void
	{
		LSOUserPreferences.setPreference("color",evtObj.value, true);
		LSOUserPreferences.setPreference("htmlColor",evtObj.target.palette.cLabel.text , true);
	}
	
	private function updateName():Void
	{
		clearInterval(si);
		// tell everyone what your name is		
		so.sendName(userName.text, broadcaster.videoID);
	}
	
	private function onKeyUp():Void
	{
		if(Key.getCode() == 13 && message.length > 0){
			var msg = TextFilter.encodeText(message.text);
			msg = getUserName() + msg;
			so.sendMessage(msg);
			message.text = "";
		}
	}	
	
	private function getUserName():String
	{
		var name:String = userName.text.length > 0 ? userName.text : defaultUserName;
		var value:String = "<font color=\"" + clr.palette.cLabel.text + "\"><b>" + name + "</b></font> - ";
		return value;
	}
	
	private function clear():Void
	{
		// clear chat
		chatBody.text = "";
		history = [];
		// clear doesn't work on Red5 yet
		//so.clear();
	}
	
	private function onSyncHandler(evtObj:Object):Void
	{
		//
	}	
	
	private function newNameHandler(evtObj:Object):Void
	{
		//_global.tt("new Name received", evtObj.name, evtObj.videoID);
		dispatchEvent({type:"onNewName", name:evtObj.name, videoID:evtObj.videoID});
	}
	
	
	private function getNameHandler(evtObj:Object):Void
	{
		_global.tt("chat.getNameHandler called");
		clearInterval(si);
		si = setInterval(this, "updateName", 250);
	}
	
	private function newMessageHandler(evtObj:Object):Void
	{		
		// we've been notified that there's a new message, go get it
		var newChat:String = evtObj.message; //so.getData(chatID);
		//_global.tt("newMessage", newChat);
		
		// return if newChat is null
		if(newChat == null) return;
		
		//UOC
        var resul:Array = newChat.split(" ");
		resul.reverse();
		var newChat2:String = resul[0];
		
		var result:Array = newChat.split(" Room");
		var newChat3pp:String = result[0];
		var newChat3p:Array = newChat3pp.split("-- ");
		var newChat3:String = newChat3p[1]; 
		
		
		// 
		//var newChat2:String = newChat.substring(newChat.length-13,newChat.length-16);
		
		//Recording started
		if(newChat2 == "(ON)") {
			
			_root.All.IniciRec.gotoAndPlay("Grabar");
			_root.All.IniciRec.close_gravacio.gotoAndPlay("des");
		}
		
		// Recording stopped
		if(newChat2 == "(OFF)") {
				
				// 1- Controlar variable global segona vegada
				_global.Segona_Vegada="true";
				_level0.Segona_Vegada="true";
				_root.conyo.text=_global.Segona_Vegada;
				
				// 2- activar archive/close
				_root.All.IniciRec.close_gravacio.gotoAndPlay("act");
				
				_root.All.IniciRec.gotoAndStop(1);}
		
		if(newChat3 == "Locked") {_root.lockRoomNC();}
		if(newChat3 == "unLocked") {_root.unlockRoomNC();}
	     
		//Closed Session
		if(newChat2 == "(CLOSED)"){
			_root.conyo.text="closed"; // Debug Text only.
			_root.All.IniciRec.gotoAndPlay("Closed"); // Show up the Closed and Archived button and hide other buttons.
			
			// Disable reload button once the session it's been archived and closed 
			//_root.All.reloadCameras.enabled=false;
			//_root.All.reloadCameras.gotoAndStop(2);
					}		
		////
		
		// push to history 
		history.push(newChat);
		
		// show in chat
		chatBody.text = history.join("\n");
		
		// scroll the chat window
		chatBody.vPosition = chatBody.maxVPosition;
		
		dispatchEvent({type:"newChat"});
	}
}