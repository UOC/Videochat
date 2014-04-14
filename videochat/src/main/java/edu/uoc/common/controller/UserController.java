package edu.uoc.common.controller;

import edu.uoc.dao.ChatDao;
import edu.uoc.dao.CourseDao;
import edu.uoc.dao.MeetingRoomDao;
import edu.uoc.dao.RoomDao;
import edu.uoc.dao.UserCourseDao;
import edu.uoc.dao.UserDao;
import edu.uoc.dao.UserMeetingDao;
//import edu.uoc.lti.LTIEnvironment;
import edu.uoc.model.Course;
import edu.uoc.model.MeetingRoom;
import edu.uoc.model.MeetingRoomExtended;
import edu.uoc.model.Room;
import edu.uoc.model.User;
import edu.uoc.model.UserMeeting;
import edu.uoc.model.UserMeetingId;
import edu.uoc.util.Constants;
import edu.uoc.util.Util;
import java.sql.Timestamp;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.support.SessionStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

@Controller
//@Scope("session")
//@SessionAttributes({"sUser", "sCourse", "sMeeting", "sUserMeeting"})
public class UserController {
    //get log4j handler
    private static final Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private MeetingRoomDao meetingroomDao;
    @Autowired
    private UserMeetingDao userMeetingDao;
    @Autowired
    private ChatDao chatMeetingDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private UserCourseDao userCourseDao;
    @Autowired
    private RoomDao roomDao;
    @Value( "${wowza.url.server}" )
    private String wowzaUrl;

    @RequestMapping("/player")
    public ModelAndView handleRequestInternal(@RequestParam(value = "id") int id,
            HttpSession session) {
        ModelAndView model = new ModelAndView("player");
          try {

            Room room = (Room) session.getAttribute(Constants.ROOM_SESSION);
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            Course course = (Course) session.getAttribute(Constants.COURSE_SESSION);
            
            if (user != null && room != null) {
                MeetingRoom meeting = meetingroomDao.findById(id);
                if (meeting.getId() > 0) {
                    model.addObject("user", user);
                    MeetingRoomExtended meeting_extended = new MeetingRoomExtended(meeting);
                    meeting_extended.setParticipants(userMeetingDao.findUsersByMeetingId(meeting, -1, true));
                    meeting_extended.setEnd_meeting_txt(Util.getTimestampFormatted(meeting_extended.getEnd_meeting(), Constants.FORMAT_DATETIME));
                    meeting_extended.setStart_meeting_txt(Util.getTimestampFormatted(meeting_extended.getStart_meeting(), Constants.FORMAT_DATETIME));
                    meeting_extended.setEnd_record_txt(Util.getTimestampFormatted(meeting_extended.getEnd_record(), Constants.FORMAT_DATETIME));
                    meeting_extended.setStart_record_txt(Util.getTimestampFormatted(meeting_extended.getStart_record(), Constants.FORMAT_DATETIME));

                    meeting_extended.setEnd_meeting_date_txt(Util.getTimestampFormatted(meeting_extended.getEnd_meeting(), Constants.FORMAT_DATE));
                    meeting_extended.setStart_meeting_date_txt(Util.getTimestampFormatted(meeting_extended.getStart_meeting(), Constants.FORMAT_DATE));
                    meeting_extended.setEnd_record_date_txt(Util.getTimestampFormatted(meeting_extended.getEnd_record(), Constants.FORMAT_DATE));
                    meeting_extended.setStart_record_date_txt(Util.getTimestampFormatted(meeting_extended.getStart_record(), Constants.FORMAT_DATE));

                    meeting_extended.setEnd_meeting_time_txt(Util.getTimestampFormatted(meeting_extended.getEnd_meeting(), Constants.FORMAT_TIME));
                    meeting_extended.setStart_meeting_time_txt(Util.getTimestampFormatted(meeting_extended.getStart_meeting(), Constants.FORMAT_TIME));
                    meeting_extended.setEnd_record_time_txt(Util.getTimestampFormatted(meeting_extended.getEnd_record(), Constants.FORMAT_TIME));
                    meeting_extended.setStart_record_time_txt(Util.getTimestampFormatted(meeting_extended.getStart_record(), Constants.FORMAT_TIME));

                    meeting_extended.setTotal_time_txt(Util.substractTimestamps(meeting_extended.getEnd_meeting(), meeting_extended.getStart_meeting()));
                    meeting_extended.setChat(chatMeetingDao.findByMeetingId(meeting));
                    model.addObject("course", session.getAttribute(Constants.COURSE_SESSION));
                    model.addObject("room", room);
                    model.addObject("meeting", meeting_extended);
                    model.addObject("wowza_stream_server", wowzaUrl);
                    
                } else {
                    model.setViewName("errorMeetingNotFound");
                }
            } else {
                model.setViewName("errorSession");
            }
        } catch(Exception e) {
            model.setViewName("error");
            logger.error("Error in player "+e.getMessage(), e);
        }

        return model;
    }

    @RequestMapping("/videochat")
    public ModelAndView showVideochat(HttpSession session) {

        ModelAndView model = new ModelAndView("videochat");
        try {
            Room room = (Room) session.getAttribute(Constants.ROOM_SESSION);
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            Course course = (Course) session.getAttribute(Constants.COURSE_SESSION);
            boolean can_access_to_meeting = true;
            if (user != null && room != null) {
                String pathMeeting = course.getCoursekey() + "_" + room.getKey() + "_" + room.getId();
                boolean is_new_meeting = true;
                MeetingRoom meeting = null;
                meeting = (MeetingRoom) session.getAttribute(Constants.MEETING_SESSION);

                boolean is_reload = false;
                //Find the room and the meeting room associate to this room
                MeetingRoom mr = meetingroomDao.findByRoomIdNotFinished(room.getId());
                UserMeeting aux = null;
                if (mr!=null) {
                    if (meeting!=null) {
                        is_reload = mr.getId() == meeting.getId() || mr.getNumber_participants()<=0;
                        aux = userMeetingDao.findUserMeetingByPK(new UserMeetingId(user, meeting));
                    } else {
                        aux = userMeetingDao.findUserMeetingByPK(new UserMeetingId(user, mr));
                    }
                }
                if (!is_reload && aux !=null && aux.getPk() != null) {
                    is_reload = true;
                }

                UserMeeting userMeeting;
                if (!room.isIs_blocked() || is_reload) {

                    //If we find it, set +1 to participants and update
                    if (mr != null && mr.getId() != 0) {
                        meeting = mr;
                        
                        if (aux.getPk() == null) {
                            meeting.setNumber_participants(mr.getNumber_participants() + 1);
                        }
                        //If the number of participants == 6 then the room is blocked
                        if (meeting.getNumber_participants() > Constants.MAX_PARTICIPANTS) {
                            room.setIs_blocked(true);
                            room.setReason_blocked(Constants.REASON_BLOCK_MAX_PARTICIPANTS);
                            can_access_to_meeting = false;
                        }
                        roomDao.save(room);
                        is_new_meeting = false;
                        //if there is no meeting to this rooom, create a new meeting room
                    }

                    if (can_access_to_meeting) {
                        java.util.Date date = new java.util.Date();

                        if (is_new_meeting) {
                            meeting = new MeetingRoom();
                            meeting.setId_room(room);
                            meeting.setNumber_participants(1);
                            meeting.setPath(pathMeeting);
                            meeting.setRecorded((byte) 0);
                            meeting.setTopic(room.getLabel());
                            meeting.setDescription(null);
                            meeting.setStart_meeting(new Timestamp(date.getTime()));
                            meeting.setStart_record(null);
                            meeting.setEnd_record(null);
                            meeting.setEnd_meeting(null);
                        }
                        meetingroomDao.save(meeting);
                        String meetingIdPath = course.getCoursekey() + "_" + room.getKey() + "_" + meeting.getId();

                        UserMeetingId umId = new UserMeetingId(user, meeting);
                        userMeeting = new UserMeeting(umId, new Timestamp(date.getTime()), meetingIdPath + "_" + user.getUsername());
                        userMeetingDao.save(userMeeting);
                        session.setAttribute(Constants.USER_METTING_SESSION, userMeeting);
                    }
                    session.setAttribute(Constants.MEETING_SESSION, meeting);
                } else {
                    can_access_to_meeting = false;
                }

                model.addObject("user", user);
                model.addObject("course", course);
                model.addObject("meeting", meeting);
                model.addObject("userMeeting", session.getAttribute(Constants.USER_METTING_SESSION));
                model.addObject("wowza_stream_server", wowzaUrl);
                if (meeting!=null) {
                    model.addObject("is_recorded", meeting.getRecorded() == (byte) 1);
                    //get the list of current participants
                    List<UserMeeting> participants = userMeetingDao.findUsersByMeetingId(meeting, user.getId(), true);
                    model.addObject("participants", participants);
                }
            }
            if (!can_access_to_meeting ) {
                String error = "errorSession";
                if (user != null && room != null && room.isIs_blocked()) {
                    //nloquejada
                    model.addObject("reason_max_participants", Constants.REASON_BLOCK_MAX_PARTICIPANTS);
                    model.addObject("reason_recording", Constants.REASON_BLOCK_RECORDING);
                    model.addObject("reason", room.getReason_blocked());
                    error = "errorBlocked";
                }
                model.setViewName(error);
            }
        } catch(Exception e) {
            model.setViewName("error");
            logger.error("Error in videochat "+e.getMessage(), e);
        }

        return model;

    }

    //Codification problems with Accents ans ntilde
   /* @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginLTI(HttpSession session, HttpServletRequest request) {
        String nextPage = "error";
        //ModelAndView model = new ModelAndView();
        session.setAttribute(Constants.USER_SESSION, null);
        session.setAttribute(Constants.COURSE_SESSION, null);
        session.setAttribute(Constants.MEETING_SESSION, null);
        session.setAttribute(Constants.USER_METTING_SESSION, null);
        session.setAttribute(Constants.USER_LANGUAGE, null);

        try {
            request.setCharacterEncoding("UTF-8");
            //1. Check if LTI call is valid
            LTIEnvironment LTIEnvironment;
            LTIEnvironment = new LTIEnvironment();
            if (LTIEnvironment.is_lti_request(request)) {

                LTIEnvironment.parseRequest(request);
                if (LTIEnvironment.isAuthenticated()) {

                    //2. Get the values of user and course  	 
                    String username = Util.sanitizeString(LTIEnvironment.getUserName());
                    //TODO mirar si cal posar
				/*if (username.startsWith(LTIEnvironment.getResourcekey()+":")) {
                     username = username.substring((LTIEnvironment.getResourcekey()+":").length());
                     }* /
                    String full_name = LTIEnvironment.getFullName();

                    String first_name = LTIEnvironment.getParameter(Constants.FIRST_NAME_LTI_PARAMETER);
                    String last_name = LTIEnvironment.getParameter(Constants.LAST_NAME_LTI_PARAMETER);

                    String email = LTIEnvironment.getEmail();
                    String user_image = LTIEnvironment.getUser_image();

                    //3. Get the role
                    boolean is_instructor = LTIEnvironment.isInstructor();
                    boolean is_course_autz = LTIEnvironment.isCourseAuthorized();

                    //4. Get course data
                    String course_key = Util.sanitizeString(LTIEnvironment.getCourseKey());
                    String course_label = LTIEnvironment.getCourseName();
                    String resource_key = Util.sanitizeString(LTIEnvironment.getResourceKey());
                    String resource_label = LTIEnvironment.getResourceTitle();

                    //LTIEnvironment.getParameter(lis_person_name_given);
                    //5. Get the locale
                    String locale = LTIEnvironment.getLocale();

                    java.util.Date date = new java.util.Date();

                    //Steps to integrate with your applicationa
                    boolean redirectToPlayer = LTIEnvironment.getCustomParameter(Constants.PLAYER_CUSTOM_LTI_PARAMETER, request) != null;
                    boolean is_debug = LTIEnvironment.getCustomParameter(Constants.DEBUG_CUSTOM_LTI_PARAMETER, request) != null;

                    // System.out.println("ID:" + userDao.findByUserCode(1));
                    User user = userDao.findByUserName(username);
                    user.setUsername(username);
                    user.setFirstname(first_name);
                    user.setSurname(last_name);
                    user.setFullname(full_name);
                    user.setEmail(email);
                    user.setImage(user_image);
                    userDao.save(user);

                    Course course = courseDao.findByCourseKey(course_key);
                    course.setTitle(course_label);
                    course.setLang(locale);

                    if (course.getId() <= 0) {
                        course.setCreated(new Timestamp(date.getTime()));
                        course.setCoursekey(course_key);
                    }
                    courseDao.save(course);

                    UserCourse userCourse = userCourseDao.findByCourseCode(course.getId(), user.getId());
                    userCourse.setIs_instructor(is_instructor);
                    //TODO change it
                    userCourse.setRole("admin");

                    UserCourseId userCourseId = new UserCourseId(user, course);
                    userCourse.setPk(userCourseId);

                    userCourseDao.save(userCourse);

                    Course courseRoom = courseDao.findByCourseKey(course_key);

                    Room room = roomDao.findByRoomKey(resource_key);
                    if (room == null) {
                        room = new Room();
                    }
                    room.setId_course(courseRoom);
                    room.setLabel(resource_label);
                    if (room.getId() <= 0) {
                        //if there is no room, create a new room and meeting room
                        room.setIs_blocked(false);
                        room.setKey(resource_key);
                        room.setReason_blocked(null);
                        roomDao.save(room);
                    }

                    session.setAttribute(Constants.ROOM_SESSION, room);
                    //Steps to integrate with your applicationa
                    //6. Check if username exists in system
                    //6.1 If doesn't exist you have to create user using Tool Api
                    //TODO create_user
                    //6.2 If exists you can update the values of user (if you want)
                    //TODO update_user
                    //7. Check if course exists in system (you can set the locale of course)
                    //7.1 If doesn't exist you have to create course using Tool Api
                    //TODO create_course
                    //7.2 If exists you can update the values of course (if you want)
                    //TODO update_course
                    //8. Register user in course 
                    session.setAttribute(Constants.USER_SESSION, user);
                    session.setAttribute(Constants.COURSE_SESSION, course);
                    session.setAttribute(Constants.USER_LANGUAGE, locale);
                    nextPage = redirectToPlayer ? "searchMeeting" : "videochat";

                } else {

                    Exception lastException = LTIEnvironment.getLastException();
                    logger.error("Error authenticating user LTI Exception "+lastException );
                    //Retornar excepcio
                    nextPage = "errorLTI";
                }
            } else {
                logger.warn("Error authenticating user LTI is not LTI Request " );
                nextPage = "errorNoLTIRequest";
            }
        } catch (Exception e) {
            logger.error("Error authenticating user ", e);

        }
        return "redirect:" + nextPage + ".htm";
    }*/

    @RequestMapping("/getUsers")
    public ModelAndView getAllUsers(HttpServletRequest request,
            HttpServletResponse response) {
        List<User> listUsers = userDao.findAll();

        ModelAndView model = new ModelAndView("getUsers");
        request.setAttribute("list", listUsers);
        model.addObject("userList", listUsers);
        return model;
    }

    @RequestMapping("/logout")
    public String logoutSession(SessionStatus status) {
        status.setComplete();
        return "logout";

    }

}
