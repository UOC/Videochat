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
import edu.uoc.model.SearchMeeting;
import edu.uoc.model.User;
import edu.uoc.model.UserCourse;
import edu.uoc.model.UserMeeting;
import edu.uoc.util.Constants;
import edu.uoc.util.Util;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Francesc Fernandez
 */
@Controller
public class MeetingRoomController {

    //get log4j handler
    private static final Logger logger = Logger.getLogger(MeetingRoomController.class);

    @Autowired
    private MeetingRoomDao meetingDao;
    @Autowired
    private UserMeetingDao userMeetingDao;
    @Autowired
    private RoomDao roomDao;
    @Value( "${enabled.mobile.data.connection}" )
    private String enabled_mobile_data_connection;
    @Value( "${authentication_system}" )
    private String authentication_system;

    /* @RequestMapping("/searchMeeting")
     public ModelAndView getMeetingRooms(@ModelAttribute("meeting") MeetingRoom meetingRoom, BindingResult bindingResult, HttpSession session) {
     ModelAndView model = new ModelAndView("searchMeeting");
     try {

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

     List<MeetingRoom> listMR = new ArrayList<MeetingRoom>();
     //If the form is filled then search by form, otherwise default search
     if ((meetingRoom.getTopic() != null) && (meetingRoom.getStart_meeting() != null) && (meetingRoom.getEnd_meeting() != null)) {

     listMR = meetingDao.findbyForm(meetingRoom);
     } else {
     //get the list of current participants
     listMR = meetingDao.findByCourseId(idsRoom, true);
     }

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
     model.addObject("meeting", new MeetingRoom());

     } else {
     model.setViewName("errorSession");
     }
     } catch (Exception ex) {
     logger.error("Error in getMeetingRooms: " + ex.getMessage(), ex);
     model.setViewName("error");
     }

     return model;
     }*/
    @RequestMapping(value = "/searchMeeting")
    public ModelAndView getMeetingRoomsForm(@ModelAttribute("searchMeeting") SearchMeeting searchMeeting, BindingResult bindingResult, HttpSession session) {
        ModelAndView model = null;
        try {
            model = new ModelAndView("searchMeeting");
            Course course = (Course) session.getAttribute(Constants.COURSE_SESSION);
            //Room room = (Room)session.getAttribute(Constants.ROOM_SESSION);
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            UserCourse userCourse = (UserCourse) session.getAttribute(Constants.USER_COURSE_SESSION);
            if (user != null && course != null) {
                model.addObject("user", user);
                model.addObject("course", session.getAttribute(Constants.COURSE_SESSION));
                model.addObject("course", course);
                model.addObject("user_is_instructor", userCourse.isIs_instructor());
                List<Room> listRooms = roomDao.findByIdCourse(course.getId());

                Room room = null;
                if (searchMeeting.getRoom_Id() > 0) {
                    searchMeeting.setRoom(roomDao.findByRoomCode(searchMeeting.getRoom_Id()));
                }

                model.addObject("listRooms", listRooms);

                List<MeetingRoom> meetingRooms = meetingDao.findbyForm(searchMeeting, listRooms);

                List<MeetingRoomExtended> listMRE = new ArrayList<MeetingRoomExtended>();
                List<UserMeeting> meetingsByUser = new ArrayList<UserMeeting>();
                MeetingRoom meeting;
                if (meetingRooms != null) {
                    //logger.info("Found results " + meetingRooms.size());
                    MeetingRoomExtended meeting_extended;
                    for (int i = 0; i < meetingRooms.size(); i++) {

                        meeting = meetingRooms.get(i);
                        meeting_extended = new MeetingRoomExtended(meeting);
                        meeting_extended.setEnd_meeting_txt(Util.getTimestampFormatted(meeting_extended.getEnd_meeting(), Constants.FORMAT_DATETIME));
                        meeting_extended.setStart_meeting_txt(Util.getTimestampFormatted(meeting_extended.getStart_meeting(), Constants.FORMAT_DATETIME));
                        meeting_extended.setEnd_record_txt(Util.getTimestampFormatted(meeting_extended.getEnd_record(), Constants.FORMAT_DATETIME));
                        meeting_extended.setStart_record_txt(Util.getTimestampFormatted(meeting_extended.getStart_record(), Constants.FORMAT_DATETIME));
                        meeting_extended.setTotal_time_txt(Util.substractTimestamps(meeting_extended.getEnd_record(), meeting_extended.getStart_record()));
                        meeting_extended.setParticipants(userMeetingDao.findUsersByMeetingId(meeting, true));
                       if(meeting.getDeleted()==0){
                           if (userCourse.isIs_instructor() || meeting_extended.contains(user)) {
                               listMRE.add(meeting_extended);
                           }
                       }
                        meetingsByUser.addAll(userMeetingDao.findUserByMeetingId(meeting, user.getId(), true));
                    }
                } else {
                    logger.info("No results found!");
                }
                model.addObject("listMR", listMRE);
                model.addObject("meeting", new MeetingRoom());
                model.addObject("searchMeeting", searchMeeting);
                model.addObject("listUM", meetingsByUser);
                model.addObject("show_mobile_connection", "1".equals(enabled_mobile_data_connection));
                
            } else {
                model.setViewName("errorSession");
            }
        } catch (Exception ex) {
            logger.error("Error in getMeetingRoomsForm: " + ex.getMessage(), ex);
            model = new ModelAndView("error");
        }
        return model;
    }

    @RequestMapping(value = "/searchMeetingModified")
    public ModelAndView getMeetingRoomsFormModified(@ModelAttribute("searchMeeting") SearchMeeting searchMeeting, @RequestParam(value = "id") int id, BindingResult bindingResult, HttpSession session) {
        ModelAndView model = null;
        try {
            model = new ModelAndView("searchMeeting");
            Course course = (Course) session.getAttribute(Constants.COURSE_SESSION);
            //Room room = (Room)session.getAttribute(Constants.ROOM_SESSION);
            User user = (User) session.getAttribute(Constants.USER_SESSION);

            if (user != null && course != null) {
                MeetingRoom meeting = meetingDao.findById(id);
                
                
                meeting.setDeleted((byte) 1);
                java.util.Date date = new java.util.Date();

                meeting.setTime_deleted(new Timestamp(date.getTime()));
                meeting.setUser_id_deleted(user.getId());
                meetingDao.deleteMeetingById(meeting);
            }

            if (user != null && course != null) {
                model.addObject("user", user);
                model.addObject("course", session.getAttribute(Constants.COURSE_SESSION));
                model.addObject("course", course);
                List<Room> listRooms = roomDao.findByIdCourse(course.getId());

                Room room = null;
                if (searchMeeting.getRoom_Id() > 0) {
                    searchMeeting.setRoom(roomDao.findByRoomCode(searchMeeting.getRoom_Id()));
                }

                model.addObject("listRooms", listRooms);

                List<MeetingRoom> meetingRooms = meetingDao.findbyForm(searchMeeting, listRooms);
                List<UserMeeting> meetingsByUser = new ArrayList<UserMeeting>();
                List<MeetingRoomExtended> listMRE = new ArrayList<MeetingRoomExtended>();
                MeetingRoom meeting;
                if (meetingRooms != null) {
                    logger.info("Found results " + meetingRooms.size());
                    MeetingRoomExtended meeting_extended;
                    for (int i = 0; i < meetingRooms.size(); i++) {

                        meeting = meetingRooms.get(i);
                        meeting_extended = new MeetingRoomExtended(meeting);
                        meeting_extended.setEnd_meeting_txt(Util.getTimestampFormatted(meeting_extended.getEnd_meeting(), Constants.FORMAT_DATETIME));
                        meeting_extended.setStart_meeting_txt(Util.getTimestampFormatted(meeting_extended.getStart_meeting(), Constants.FORMAT_DATETIME));
                        meeting_extended.setEnd_record_txt(Util.getTimestampFormatted(meeting_extended.getEnd_record(), Constants.FORMAT_DATETIME));
                        meeting_extended.setStart_record_txt(Util.getTimestampFormatted(meeting_extended.getStart_record(), Constants.FORMAT_DATETIME));
                        meeting_extended.setTotal_time_txt(Util.substractTimestamps(meeting_extended.getEnd_record(), meeting_extended.getStart_record()));
                        meeting_extended.setParticipants(userMeetingDao.findUsersByMeetingId(meeting, true));
                        if(meeting.getDeleted()==0){
                        listMRE.add(meeting_extended);
                       }
                        meetingsByUser.addAll(userMeetingDao.findUserByMeetingId(meeting, user.getId(), true));
                    }
                } else {
                    logger.info("No results found!");
                }
                model.addObject("listMR", listMRE);
                model.addObject("meeting", new MeetingRoom());
                model.addObject("searchMeeting", searchMeeting);
                model.addObject("listUM", meetingsByUser);
                model.addObject("show_mobile_connection", Constants.AUTENTICATION_METHOD_INTERNAL.equals(authentication_system) && "1".equals(enabled_mobile_data_connection));                 
                
            } else {
                model.setViewName("errorSession");
            }
        } catch (Exception ex) {
            logger.error("Error in getMeetingRoomsFormModified: " + ex.getMessage(), ex);
            model = new ModelAndView("error");
        }
        return model;
    }

    /*@RequestMapping(value = "/currentUserAcceptConnection", method = RequestMethod.GET,
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
     }*/
}
