/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.json.controller;

import edu.uoc.common.controller.UserController;
import edu.uoc.dao.MeetingRoomDao;
import edu.uoc.dao.RoomDao;
import edu.uoc.dao.UserDao;
import edu.uoc.dao.UserMeetingDao;
import edu.uoc.model.Course;
import edu.uoc.model.MeetingRoom;
import edu.uoc.model.Room;
import edu.uoc.model.User;
import edu.uoc.model.UserMeeting;
import edu.uoc.model.UserMeetingId;
import edu.uoc.model.json.JSONRequest;
import edu.uoc.model.json.JSONRequestExtraParam;
import edu.uoc.model.json.JSONResponse;
import edu.uoc.util.Constants;
import edu.uoc.util.Util;
import java.sql.Timestamp;
import java.util.Date;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 *
 * @author antonibertranbellido
 */
@Controller

public class UserMeetingController {
    //get log4j handler
    private static final Logger logger = Logger.getLogger(UserMeetingController.class);

    @Autowired
    private UserMeetingDao userMeetingDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private MeetingRoomDao meetingDao;
    @Autowired
    private UserDao userDao;
    @Value( "${wowza.url.server}" )
    private String wowzaServer;
    @Value( "${user.api.port}" )
    private String apiPort;
    @Value( "${user.api.schema}" )
    private String apiSchema;
    @Value( "${user.api.application}" )
    private String apiApplication;
    @Value( "${user.api.username}" )
    private String apiUsername;
    @Value( "${user.api.password}" )
    private String apiPassword;

    
    @RequestMapping(value="usermeeting", method = RequestMethod.POST)
     public @ResponseBody JSONResponse addUser(@RequestBody JSONRequest username, HttpSession session) {
        JSONResponse response = new JSONResponse();
        try {
            Room room = (Room) session.getAttribute(Constants.ROOM_SESSION);
            MeetingRoom meeting = (MeetingRoom) session.getAttribute(Constants.MEETING_SESSION);
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            User userToAdd = userDao.findByUserName(username.getRequest());
            if (user!=null && meeting!=null) {
                meeting = meetingDao.findById(meeting.getId());
                if (meeting.getRecorded()!=(byte)1) {
                    UserMeetingId mId = new UserMeetingId();
                    mId.setMeeting(meeting);
                    mId.setUser(userToAdd);
                    UserMeeting userMeetingCheck = userMeetingDao.findUserMeetingByPK(mId);

                    if (userMeetingCheck.getPk()==null || userMeetingCheck.getPk().getUser()==null) {
                        Course course = (Course) session.getAttribute(Constants.COURSE_SESSION);
                        String meetingIdPath = course.getCoursekey() + "_" + room.getKey() + "_" + meeting.getId();
                        Date date = new Date();
                        String extra_role = (String) session.getAttribute(Constants.EXTRA_ROLE_CUSTOM_LTI_PARAMETER);
                        UserMeeting userMeeting = new UserMeeting(mId, new Timestamp(date.getTime()), meetingIdPath + "_" + user.getUsername(), extra_role);
                        userMeetingDao.save(userMeeting);
                        UserController uController = new UserController();
                        uController.updateHistoryUserMeetingTable(userMeeting);
                
                        
                    }
                    meeting.setNumber_participants(userMeetingDao.countNumberParticipants(meeting));
                    if (meeting.getNumber_participants()>0 && meeting.getFinished()==(byte)1){
                        meeting.setFinished((byte)0);
                        meeting.setEnd_meeting(null);
                    }
                    meetingDao.save(meeting);
                }
                response.setOk(true);
            }
        } catch (Exception e) {
            logger.error("Adding user ", e);
            
        }
        return response;
    }    
    
    @RequestMapping(value = "/usermeeting_notify", method = RequestMethod.DELETE)
    public @ResponseBody JSONResponse deleteUserNotficationExternalTool(@RequestBody JSONRequestExtraParam username, HttpSession session) {
        JSONResponse response = new JSONResponse();
        try {
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            if (user!=null) {
                String url = (String)session.getAttribute(Constants.PARAM_URL_NOTIFY_ENDED_RECORDING_CUSTOM_LTI_PARAMETER);
                response = Util.curlJson(url, "ok", response,  logger);
                String urlSessionAvailable = (String)session.getAttribute(Constants.PARAM_URL_NOTIFY_SESSION_AVAILABLE_CUSTOM_LTI_PARAMETER);
                response = Util.curlJson(urlSessionAvailable, "ok", response,  logger);

            }
        } catch (Exception e) {
            logger.error("deleteUserNotficationExternalTool user ", e);
            
        }
        return response;
    }    
    @RequestMapping(value = "/stop_external_meeting/{room_name}", method = RequestMethod.GET)
    public @ResponseBody JSONResponse stopRecord(@PathVariable String room_name) {
        JSONResponse response = new JSONResponse();
        String url=null;
        try {
            if (room_name!=null && room_name.length()>0) {
                url = apiSchema+"://"+wowzaServer+":"+apiPort+"/"+apiApplication+"?action="+Constants.API_STOP_RECORD_PARAMETER+"&room="+room_name+"&username=--vc-system-auto--";
                logger.info("calling to "+url);
                response = Util.curlJson(url, apiUsername, apiPassword, "success", response,  logger);
                if (response.isOk()) {
                    //close session
                    MeetingRoom meeting = this.meetingDao.findbyPath(room_name);
                    if (meeting!=null && meeting.getId()>0) {
                        Room room = meeting.getId_room();
                        room.setIs_blocked(false);
                        room.setReason_blocked(null);
                        this.roomDao.save(room);
                        meeting = meetingDao.findById(meeting.getId());
                        if (meeting.getRecorded() == (byte)1 && meeting.getEnd_record()==null) {
                            meeting.setEnd_record(new Timestamp(new Date().getTime()));
                        }
                        meeting.setEnd_meeting(new Timestamp(new Date().getTime()));
                        meeting.setFinished((byte)1);
                        this.meetingDao.save(meeting);

                    }
                    else {
                        logger.error("closeMeeting Meeting not found "+room_name);
                    }
                }
            }
        } catch (NumberFormatException e) {
            logger.error("closeMeeting NumberformatExceotion and url "+url, e);
            
        } catch (Exception e) {
            logger.error("closeMeeting via api and url "+url, e);
            
        }
        return response;
    }    
    @RequestMapping(value = "/close_external_meeting/{room_name}", method = RequestMethod.GET)
    public @ResponseBody JSONResponse closeMeeting(@PathVariable String room_name) {
        JSONResponse response = new JSONResponse();
        String url=null;
        try {
            if (room_name!=null && room_name.length()>0) {
                url = apiSchema+"://"+wowzaServer+":"+apiPort+"/"+apiApplication+"?action="+Constants.API_CLOSE_SESSION_PARAMETER+"&room="+room_name+"&username=--vc-system-auto--";
                response = Util.curlJson(url, apiUsername, apiPassword, "success", response,  logger);
            }
        } catch (NumberFormatException e) {
            logger.error("closeMeeting NumberformatExceotion and url "+url, e);
            
        } catch (Exception e) {
            logger.error("closeMeeting via api and url "+url, e);
            
        }
        return response;
    }    
    
    @RequestMapping(value = "/usermeeting_notify", method = RequestMethod.POST)
    public @ResponseBody JSONResponse setUserConnectedNotficationExternalTool(@RequestBody JSONRequestExtraParam username, HttpSession session) {
        JSONResponse response = new JSONResponse();
        try {
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            if (user!=null) {
                String url = (String)session.getAttribute(Constants.PARAM_URL_NOTIFY_STARTED_RECORDING_CUSTOM_LTI_PARAMETER);
                response = Util.curlJson(url, "ok", response, logger);
                
            }
            
        } catch (Exception e) {
            logger.error("setUserConnectedNotficationExternalTool user ", e);
            
        }
        return response;
    }    
    
    @RequestMapping(value="usermeeting", method = RequestMethod.DELETE)
    public @ResponseBody JSONResponse deleteUser(@RequestBody JSONRequestExtraParam username, HttpSession session) {
        JSONResponse response = new JSONResponse();
        try {
            Room room = (Room) session.getAttribute(Constants.ROOM_SESSION);
            MeetingRoom meeting = (MeetingRoom) session.getAttribute(Constants.MEETING_SESSION);
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            User userDeleted = userDao.findByUserName(username.getRequest());
            boolean should_close_it = "true".equals(username.getExtraParam());
            if (user!=null && meeting!=null) {
                meeting = meetingDao.findById(meeting.getId());
                if (meeting.getFinished()!=(byte)1 && meeting.getRecorded()!=(byte)1) {
                    UserMeetingId mId = new UserMeetingId();
                    mId.setMeeting(meeting);
                    mId.setUser(userDeleted);
                    UserMeeting userMeetingDeleted = userMeetingDao.findUserMeetingByPK(mId);

                    if (userMeetingDeleted.getPk()!=null && userMeetingDeleted.getPk().getUser()!=null) {
                        userMeetingDao.delete(userMeetingDeleted);
                        UserController uController = new UserController();
                        uController.updateHistoryUserMeetingTable(userMeetingDeleted);

                        meeting.setNumber_participants(userMeetingDao.countNumberParticipants(meeting));
                        if (should_close_it && meeting.getNumber_participants()==0){
                            meeting.setFinished((byte)1);
                            meeting.setEnd_meeting(new Timestamp(new Date().getTime()));
                            room = this.roomDao.findByRoomCode(room.getId());
                            room.setIs_blocked(false);
                            room.setReason_blocked(null);
                            this.roomDao.save(room);
                        }
                        meetingDao.save(meeting);
                    }
                }
                response.setOk(true);
            }
        } catch (Exception e) {
            logger.error("Deleting user ", e);
            
        }
        return response;
    }   
    
}