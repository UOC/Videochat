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
import edu.uoc.model.JSONResponse;
import edu.uoc.model.MeetingRoom;
import edu.uoc.model.Room;
import edu.uoc.model.User;
import edu.uoc.model.UserMeeting;
import edu.uoc.model.UserMeetingId;
import edu.uoc.util.Constants;
import javax.servlet.http.HttpSession;
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
            e.printStackTrace();
            
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
            e.printStackTrace();
            
        }
        return response;
    }
    
    
    
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody JSONResponse saveSession(@RequestBody JSONRequest topic, @RequestBody JSONRequest description, HttpSession session) {
        JSONResponse response = new JSONResponse();
        try {
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            MeetingRoom meeting = (MeetingRoom) session.getAttribute(Constants.MEETING_SESSION);
            
            if (user!=null && meeting!=null && topic!=null && topic.getRequest().length()>0) {
                meeting = meetingDao.findById(meeting.getId());
                meeting.setTopic(topic.getRequest());
                meeting.setDescription(description.getRequest());
                response.setOk(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return response;
    }
}
