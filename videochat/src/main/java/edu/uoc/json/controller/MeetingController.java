/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.json.controller;

import edu.uoc.dao.MeetingRoomDao;
import edu.uoc.dao.RoomDao;
import edu.uoc.dao.UserMeetingDao;
import edu.uoc.model.JSONResponse;
import edu.uoc.model.MeetingRoom;
import edu.uoc.model.Room;
import edu.uoc.model.User;
import edu.uoc.model.UserMeeting;
import edu.uoc.util.Constants;
import java.sql.Timestamp;
import java.util.Date;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 *
 * @author antonibertranbellido
 */
@Controller
@RequestMapping("/meeting")
public class MeetingController {
    
    @Autowired
    private UserMeetingDao userMeetingDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private MeetingRoomDao meetingDao;
    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody JSONResponse currentUserAcceptConnection(HttpSession session) {
        JSONResponse response = new JSONResponse();
        try {
            UserMeeting userMeeting = (UserMeeting) session.getAttribute(Constants.USER_METTING_SESSION);
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            if (user!=null && userMeeting!=null) {
                userMeeting.setAccessConfirmed((byte)1);
                this.userMeetingDao.save(userMeeting);
                response.setOk(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return response;
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody JSONResponse startRecord(HttpSession session) {
        JSONResponse response = new JSONResponse();
        try {
            Room room = (Room) session.getAttribute(Constants.ROOM_SESSION);
            MeetingRoom meeting = (MeetingRoom) session.getAttribute(Constants.MEETING_SESSION);
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            if (user!=null && meeting!=null) {
                room.setIs_blocked(true);
                room.setReason_blocked(Constants.REASON_BLOCK_RECORDING);
                this.roomDao.save(room);
                meeting.setRecorded((byte)1);
                //meeting.setPath(meeting.getPath().replaceAll("_", "/"));
                meeting.setStart_record(new Timestamp(new Date().getTime()));
                this.meetingDao.save(meeting);
                response.setOk(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return response;
    }

    
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody JSONResponse stopRecord(HttpSession session) {
        JSONResponse response = new JSONResponse();
        try {
            Room room = (Room) session.getAttribute(Constants.ROOM_SESSION);
            MeetingRoom meeting = (MeetingRoom) session.getAttribute(Constants.MEETING_SESSION);
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            if (user!=null && meeting!=null) {
                room.setIs_blocked(false);
                room.setReason_blocked(null);
                this.roomDao.save(room);
                meeting.setRecorded((byte)1);
                meeting.setEnd_record(new Timestamp(new Date().getTime()));
                meeting.setFinished((byte)0);
                this.meetingDao.save(meeting);
                response.setOk(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return response;
    }
    
    
    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody JSONResponse closeSession(HttpSession session) {
        JSONResponse response = new JSONResponse();
        try {
            Room room = (Room) session.getAttribute(Constants.ROOM_SESSION);
            MeetingRoom meeting = (MeetingRoom) session.getAttribute(Constants.MEETING_SESSION);
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            if (user!=null && meeting!=null) {
                room.setIs_blocked(false);
                room.setReason_blocked(null);
                this.roomDao.save(room);
                if (meeting.getRecorded() == (byte)1 && meeting.getEnd_record()==null) {
                    meeting.setEnd_record(new Timestamp(new Date().getTime()));
                }
                meeting.setEnd_meeting(new Timestamp(new Date().getTime()));
                meeting.setFinished((byte)1);
                this.meetingDao.save(meeting);
                response.setOk(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return response;
    }

}
