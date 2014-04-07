/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.json.controller;

import edu.uoc.dao.MeetingRoomDao;
import edu.uoc.dao.RoomDao;
import edu.uoc.dao.UserDao;
import edu.uoc.dao.UserMeetingDao;
import edu.uoc.model.JSONRequest;
import edu.uoc.model.JSONRequestExtraParam;
import edu.uoc.model.JSONResponse;
import edu.uoc.model.MeetingRoom;
import edu.uoc.model.Room;
import edu.uoc.model.User;
import edu.uoc.model.UserMeeting;
import edu.uoc.model.UserMeetingId;
import edu.uoc.util.Constants;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 *
 * @author antonibertranbellido
 */
@Controller
@RequestMapping("/usermeeting")
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
    

    
    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody JSONResponse deleteUser(@RequestBody JSONRequest username, HttpSession session) {
        JSONResponse response = new JSONResponse();
        try {
            Room room = (Room) session.getAttribute(Constants.ROOM_SESSION);
            MeetingRoom meeting = (MeetingRoom) session.getAttribute(Constants.MEETING_SESSION);
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            User userDeleted = userDao.findByUserName(username.getRequest());
            if (user!=null && meeting!=null) {
                meeting = meetingDao.findById(meeting.getId());
                if (meeting.getFinished()!=(byte)1 && meeting.getRecorded()!=(byte)1) {
                    UserMeetingId mId = new UserMeetingId();
                    mId.setMeeting(meeting);
                    mId.setUser(userDeleted);
                    UserMeeting userMeetingDeleted = userMeetingDao.findUserMeetingByPK(mId);

                    if (userMeetingDeleted.getPk()!=null && userMeetingDeleted.getPk().getUser()!=null) {
                        userMeetingDao.delete(userMeetingDeleted);
                        meeting.setNumber_participants(meeting.getNumber_participants()-1);
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

    
    
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody JSONResponse lockSession(HttpSession session) {
        JSONResponse response = new JSONResponse();
        try {
            Room room = (Room) session.getAttribute(Constants.ROOM_SESSION);
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            MeetingRoom meeting = (MeetingRoom) session.getAttribute(Constants.MEETING_SESSION);
            
            if (user!=null && room!=null && meeting!=null) {
                room = roomDao.findByRoomCode(room.getId());
                boolean new_block = !room.isIs_blocked();
                meeting = meetingDao.findById(meeting.getId());
                if (meeting.getFinished()==(byte)1) {
                    new_block = false;
                }
                if (meeting.getFinished()==(byte)1 ||
                        !Constants.REASON_BLOCK_RECORDING.equals(room.getReason_blocked())) {
                    room.setIs_blocked(new_block);
                    if (!new_block) {
                        room.setReason_blocked(null);
                    } else {
                        room.setReason_blocked(Constants.REASON_BLOCK_BY_USER);
                    }
                }
                roomDao.save(room);
                response.setOk(true);
            }
        } catch (Exception e) {
            logger.error("Deleting lock session ", e);
            
        }
        return response;
    }
    
    
    
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody JSONResponse saveSession(@RequestBody JSONRequestExtraParam request, HttpSession session) {
        JSONResponse response = new JSONResponse();
        try {
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            MeetingRoom meeting = (MeetingRoom) session.getAttribute(Constants.MEETING_SESSION);
            logger.info("topic "+request.getRequest()+" desc "+request.getExtraParam());
            if (user!=null && meeting!=null && request!=null && request.getRequest()!=null && request.getRequest().length()>0 ) {
                meeting = meetingDao.findById(meeting.getId());
                meeting.setTopic(request.getRequest());
                meeting.setDescription(request.getExtraParam());
                logger.info("Meeting Saved topic "+request.getRequest()+" desc "+request.getExtraParam());
                meetingDao.save(meeting);
                response.setOk(true);
            }
        } catch (Exception e) {
            logger.error("Save session name ", e);
            
        }
        return response;
    }
}
