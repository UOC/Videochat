/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.common.controller;

import edu.uoc.dao.MeetingRoomDao;
import edu.uoc.dao.RoomDao;
import edu.uoc.dao.UserMeetingDao;
import edu.uoc.model.Course;
import edu.uoc.model.MeetingRoom;
import edu.uoc.model.MeetingRoomExtended;
import edu.uoc.model.Room;
import edu.uoc.model.User;
import edu.uoc.model.UserMeeting;
import edu.uoc.util.Constants;
import edu.uoc.util.Util;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Francesc Fernandez
 */
@Controller

@Scope("session")
public class MeetingRoomController implements java.io.Serializable {

    @Autowired
    private MeetingRoomDao meetingDao;
    @Autowired
    private UserMeetingDao userMeetingDao;
    @Autowired
    private RoomDao roomDao;

    @RequestMapping("/searchMeeting")
    public ModelAndView getMeetingRooms(HttpSession session) {
        ModelAndView model = new ModelAndView("searchMeeting");
        try {
            Room room = (Room) session.getAttribute(Constants.ROOM_SESSION);
            User user = (User) session.getAttribute(Constants.USER_SESSION);
        } catch (IllegalStateException ISE) {
            System.err.println("IllegalStateException: " + ISE.getMessage());
        }
        Course course = (Course) session.getAttribute(Constants.COURSE_SESSION);
        //Room room = (Room)session.getAttribute(Constants.ROOM_SESSION);
        User user = (User) session.getAttribute(Constants.USER_SESSION);
        if (user != null && course != null) {
            model.addObject("user", user);
            model.addObject("course", course);
            List<Room> listRooms = roomDao.findByIdCourse(course.getId());
            // List<MeetingRoom> mRoom = meetingDao.findbyForm(new MeetingRoom());
            String idsRoom = "";
            for (Room room : listRooms) {
                idsRoom += (idsRoom.length() > 0 ? "," : "") + room.getId();
            }

            model.addObject("listRooms", listRooms);
            //get the list of current participants
            List<MeetingRoom> listMR = meetingDao.findByCourseId(idsRoom, true);
            List<MeetingRoomExtended> listMRE = new ArrayList<MeetingRoomExtended>();
            MeetingRoomExtended meeting_extended;
            MeetingRoom meeting;
            for (int i = 0; i < listMR.size(); i++) {
                meeting = listMR.get(i);
                meeting_extended = new MeetingRoomExtended(meeting);
                meeting_extended.setEnd_meeting_txt(Util.getTimestampFormatted(meeting_extended.getEnd_meeting(), Constants.FORMAT_DATETIME));
                meeting_extended.setStart_meeting_txt(Util.getTimestampFormatted(meeting_extended.getStart_meeting(), Constants.FORMAT_DATETIME));
                meeting_extended.setEnd_record_txt(Util.getTimestampFormatted(meeting_extended.getEnd_record(), Constants.FORMAT_DATETIME));
                meeting_extended.setStart_record_txt(Util.getTimestampFormatted(meeting_extended.getStart_record(), Constants.FORMAT_DATETIME));
                meeting_extended.setTotal_time_txt(Util.substractTimestamps(meeting_extended.getEnd_meeting(), meeting_extended.getStart_meeting()));
                meeting_extended.setParticipants(userMeetingDao.findUsersByMeetingId(meeting));
                listMRE.add(meeting_extended);
            }
            model.addObject("listMR", listMRE);

        } else {
            model.setViewName("errorSession");
        }

        return model;
    }

    @RequestMapping(value = "/searchMeetingForm", method = RequestMethod.POST)
    public ModelAndView getMeetingRoomsForm(@ModelAttribute("meeting") MeetingRoom meetingRoom, HttpSession session) {
        ModelAndView model = new ModelAndView("searchMeetingform");
        try {
            Room room = (Room) session.getAttribute(Constants.ROOM_SESSION);
            User user = (User) session.getAttribute(Constants.USER_SESSION);
        } catch (IllegalStateException ISE) {
            System.err.println("IllegalStateException: " + ISE.getMessage());
        }
        Course course = (Course) session.getAttribute(Constants.COURSE_SESSION);
        //Room room = (Room)session.getAttribute(Constants.ROOM_SESSION);
        User user = (User) session.getAttribute(Constants.USER_SESSION);
        if (user != null && course != null) {
            model.addObject("user", user);
            model.addObject("course", session.getAttribute(Constants.COURSE_SESSION));
            model.addObject("course", course);
            List<MeetingRoom> meetingRooms = meetingDao.findbyForm(meetingRoom);
           
            
            List<MeetingRoomExtended> listMRE = new ArrayList<MeetingRoomExtended>();
            MeetingRoomExtended meeting_extended;
            MeetingRoom meeting;
            for (int i = 0; i < meetingRooms.size(); i++) {
                meeting = meetingRooms.get(i);
                meeting_extended = new MeetingRoomExtended(meeting);
                meeting_extended.setEnd_meeting_txt(Util.getTimestampFormatted(meeting_extended.getEnd_meeting(), Constants.FORMAT_DATETIME));
                meeting_extended.setStart_meeting_txt(Util.getTimestampFormatted(meeting_extended.getStart_meeting(), Constants.FORMAT_DATETIME));
                meeting_extended.setEnd_record_txt(Util.getTimestampFormatted(meeting_extended.getEnd_record(), Constants.FORMAT_DATETIME));
                meeting_extended.setStart_record_txt(Util.getTimestampFormatted(meeting_extended.getStart_record(), Constants.FORMAT_DATETIME));
                meeting_extended.setTotal_time_txt(Util.substractTimestamps(meeting_extended.getEnd_meeting(), meeting_extended.getStart_meeting()));
                meeting_extended.setParticipants(userMeetingDao.findUsersByMeetingId(meeting));
                listMRE.add(meeting_extended);
            }
            model.addObject("listMR", listMRE);

        } else {
            model.setViewName("errorSession");
        }

        return model;
    }

    @RequestMapping(value = "/currentUserAcceptConnection", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean registerUserMeeting(HttpSession session) {
        boolean registerOk = false;
        UserMeeting userMeeting = (UserMeeting) session.getAttribute(Constants.USER_METTING_SESSION);
        User user = (User) session.getAttribute(Constants.USER_SESSION);
        try {
            if (user != null && userMeeting != null) {
                userMeeting.setAccessConfirmed((byte) 1);
                this.userMeetingDao.save(userMeeting);
                registerOk = true;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return registerOk;
    }
}
