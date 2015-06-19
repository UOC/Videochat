/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.json.controller;

import edu.uoc.dao.MeetingRoomDao;
import edu.uoc.dao.RoomDao;
import edu.uoc.dao.UserMeetingDao;
import edu.uoc.model.json.JSONRequestExtraParam;
import edu.uoc.model.json.JSONResponse;
import edu.uoc.model.MeetingRoom;
import edu.uoc.model.Room;
import edu.uoc.model.User;
import edu.uoc.util.Constants;
import java.sql.Timestamp;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
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
public class MeetingSessionController {
    //get log4j handler
    private static final Logger logger = Logger.getLogger(MeetingSessionController.class);

    
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private MeetingRoomDao meetingDao;
    
    @RequestMapping(value = "/topic", method = RequestMethod.POST)
    public @ResponseBody JSONResponse setTopicMeeting(HttpServletRequest request,
            HttpSession session) {
        JSONResponse response = new JSONResponse();
        try {
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            MeetingRoom meeting = (MeetingRoom) session.getAttribute(Constants.MEETING_SESSION);
            String value = request.getParameter("value");
            logger.info("topic "+value);
            if (user!=null && meeting!=null && value!=null && value.length()>0 ) {
                meeting = meetingDao.findById(meeting.getId());
                meeting.setTopic(value);
                meetingDao.save(meeting);
                response.setOk(true);
                session.setAttribute(Constants.MEETING_SESSION, meeting);
            }
        } catch (Exception e) {
            logger.error("Save session name ", e);
            
        }
        return response;    
    }

    @RequestMapping(value = "/description", method = RequestMethod.POST)
    public @ResponseBody JSONResponse setDescriptionMeeting(HttpServletRequest request,
            HttpSession session) {
        JSONResponse response = new JSONResponse();
        try {
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            MeetingRoom meeting = (MeetingRoom) session.getAttribute(Constants.MEETING_SESSION);
            String value = request.getParameter("value");
            logger.info("description "+value);
            if (user!=null && meeting!=null) {
                meeting = meetingDao.findById(meeting.getId());
                meeting.setDescription(value);
                meetingDao.save(meeting);
                response.setOk(true);
                session.setAttribute(Constants.MEETING_SESSION, meeting);
            }
        } catch (Exception e) {
            logger.error("Save session description ", e);            
        }
        return response;    
    }    

    
    @RequestMapping(value = "/meetingsession", method = RequestMethod.POST)
    public @ResponseBody JSONResponse saveSession(@RequestBody JSONRequestExtraParam request, HttpSession session) {
        JSONResponse response = new JSONResponse();
        try {
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            MeetingRoom meeting = (MeetingRoom) session.getAttribute(Constants.MEETING_SESSION);
            Room room = (Room) session.getAttribute(Constants.ROOM_SESSION);
            logger.info("topic "+request.getRequest()+" desc "+request.getExtraParam());
            if (user!=null && meeting!=null && request!=null && request.getRequest()!=null && request.getRequest().length()>0 ) {
                meeting = meetingDao.findById(meeting.getId());
                meeting.setTopic(request.getRequest());
                meeting.setDescription(request.getExtraParam());
                logger.info("Meeting Saved topic "+request.getRequest()+" desc "+request.getExtraParam());
                meetingDao.save(meeting);
                room = this.roomDao.findByRoomCode(room.getId());
                room.setIs_blocked(false);
                room.setReason_blocked(null);
                this.roomDao.save(room);
            
                response.setOk(true);
            }
        } catch (Exception e) {
            logger.error("Save session name ", e);
            
        }
        return response;
    }
    
    
    @RequestMapping(value = "/meetingsession", method = RequestMethod.PUT)
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
            logger.error("Lock session ", e);
            
        }
        return response;
    }
    
    @RequestMapping(value = "/meetingsession", method = RequestMethod.DELETE)
    public @ResponseBody JSONResponse closeSession(HttpSession session) {
        JSONResponse response = new JSONResponse();
        try {
            Room room = (Room) session.getAttribute(Constants.ROOM_SESSION);
            MeetingRoom meeting = (MeetingRoom) session.getAttribute(Constants.MEETING_SESSION);
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            if (user!=null && meeting!=null) {
                room = this.roomDao.findByRoomCode(room.getId());
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
                response.setOk(true);
            }
        } catch (Exception e) {
            logger.error("Error closing record ", e);
            
        }
        return response;
    }

}