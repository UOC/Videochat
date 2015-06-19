package edu.uoc.common.controller;

import edu.uoc.dao.ChatDao;
import edu.uoc.dao.CourseDao;
import edu.uoc.dao.MeetingRoomDao;
import edu.uoc.dao.RoomDao;
import edu.uoc.dao.UserCourseDao;
import edu.uoc.dao.UserDao;
import edu.uoc.dao.UserMeetingDao;
import edu.uoc.dao.UserMeetingHistoryDao;
import edu.uoc.model.Course;
import edu.uoc.model.MeetingRoom;
import edu.uoc.model.MeetingRoomExtended;
import edu.uoc.model.Room;
import edu.uoc.model.User;
import edu.uoc.model.UserMeeting;
import edu.uoc.model.UserMeetingHistory;
import edu.uoc.model.UserMeetingId;
import edu.uoc.util.Constants;
import edu.uoc.util.Util;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;

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
    @Value( "${use.jwplayer}" )
    private String useJWplayer;
    @Value( "${wowza.vod_application_player.server}" )
    private String vod_application_player;

    /**
     * Shows a session meeting
     * @param id
     * @param session
     * @return 
     */
    @RequestMapping("/player")
    public ModelAndView showPlayer(@RequestParam(value = "id") int id,
            HttpSession session) {
        return showFixedPlayer(id, 0, true, session);
    }
    
    @RequestMapping("/view_session")
    public ModelAndView viewSession(
            HttpSession session) {
        MeetingRoom meeting = (MeetingRoom) session.getAttribute(Constants.MEETING_SESSION);

        return showFixedPlayer(meeting.getId(), 0, false, session);
    }
    /**
     * Allows to select the player
     * @param id
     * @param player_id: <ul>
     *                      <li> 0 - By configuration</li>
     *                      <li> 1 - Using JWPlayer</li>
     *                      <li> 2 - VideoJS</li></ul>
     * @param session
     * @return 
     */
    @RequestMapping("/fixed_player")
    public ModelAndView showFixedPlayer(@RequestParam(value = "id") int id, @RequestParam(value = "player") int player_id, 
            boolean show_button_back, HttpSession session) {
        ModelAndView model = null;
          try {
            model =  new ModelAndView("player");
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            Course course = (Course) session.getAttribute(Constants.COURSE_SESSION);
            
            if (user != null) {
                MeetingRoom meeting = meetingroomDao.findById(id);
                 if(meeting==null || meeting.getId_room()==null || !(meeting.getId_room().getId_course().getId()==course.getId())){
                    model.setViewName("errorMeetingNotFound"); 
                 } else {
                    if (meeting.getId() > 0) {
                        model.addObject("user", user);
                        MeetingRoomExtended meeting_extended = new MeetingRoomExtended(meeting);
                        boolean get_users_from_history_if_not_found = (Boolean)session.getAttribute(Constants.GET_USERS_FROM_HISTORY_IF_NOT_FOUND);
                        int max_participants = Constants.MAX_PARTICIPANTS;
                        try{
                            if (session.getAttribute(Constants.PARAM_MAX_PARTICIPANTS_CUSTOM_LTI_PARAMETER)!=null){
                                max_participants = Integer.parseInt((String) session.getAttribute(Constants.PARAM_MAX_PARTICIPANTS_CUSTOM_LTI_PARAMETER));
                            }
                        }
                        catch (NumberFormatException nfe) {
                            //nothing
                        }

                        meeting_extended.setParticipants(userMeetingDao.findUsersByMeetingId(meeting, -1, true, get_users_from_history_if_not_found, max_participants));
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

                        meeting_extended.setTotal_time_txt(Util.substractTimestamps(meeting_extended.getEnd_record(), meeting_extended.getStart_record()));
                        meeting_extended.setChat(chatMeetingDao.findByMeetingId(meeting));
                        model.addObject("course", session.getAttribute(Constants.COURSE_SESSION));
                        model.addObject("meeting", meeting_extended);
                        model.addObject("wowza_stream_server", wowzaUrl);
                        model.addObject("vod_application_player", (vod_application_player==null?"vod":vod_application_player));
                        if (player_id==0) {
                            model.addObject("useJWplayer", "1".equals(useJWplayer));
                        } else {
                            model.addObject("useJWplayer", player_id==1);
                        }
                        if (!show_button_back) {
                            model.addObject("disable_back_button", true);
                        } else {
                            model.addObject("disable_back_button", session.getAttribute(Constants.DISABLE_BACK_BUTTON_CUSTOM_LTI_PARAMETER));
                        }
                        model.addObject("max_participants", max_participants);

                    } else {
                        model.setViewName("errorMeetingNotFound");
                    }
                 }
            } else {
                model.setViewName("errorSession");
            }
        } catch(Exception e) {
            model =  new ModelAndView("error");
            logger.error("Error in player "+e.getMessage(), e);
        }

        return model;
    }

    @RequestMapping("/videochat")
    public ModelAndView showVideochat(HttpSession session, HttpServletRequest request) {

        ModelAndView model = null;
        try {
            Room room = (Room) session.getAttribute(Constants.ROOM_SESSION);
            User user = (User) session.getAttribute(Constants.USER_SESSION);
            Course course = (Course) session.getAttribute(Constants.COURSE_SESSION);
            model = new ModelAndView("videochat");
            String extra_role = (String) session.getAttribute(Constants.EXTRA_ROLE_CUSTOM_LTI_PARAMETER);
            int max_participants = Constants.MAX_PARTICIPANTS;
            try{
                if (session.getAttribute(Constants.PARAM_MAX_PARTICIPANTS_CUSTOM_LTI_PARAMETER)!=null){
                    max_participants = Integer.parseInt((String) session.getAttribute(Constants.PARAM_MAX_PARTICIPANTS_CUSTOM_LTI_PARAMETER));
                }
            }
            catch (NumberFormatException nfe) {
                //nothing
            }

            boolean can_access_to_meeting = true;
            if (user != null && room != null && course!=null) {
                
                String pathMeeting = course.getCoursekey() + "_" + room.getKey() + "_" + room.getId(); //
                boolean is_new_meeting = true;
                MeetingRoom meeting = null;
                meeting = (MeetingRoom) session.getAttribute(Constants.MEETING_SESSION);

                boolean is_reload = false;
                //Find the room and the meeting room associate to this room
                MeetingRoom mr = meetingroomDao.findByRoomIdNotFinished(room.getId());
                if (mr!=null &&
                        mr.getRecorded()==(byte)1 && mr.getFinished()==(byte)0 
                        && mr.getStart_record()!=null && mr.getStart_record().before(new Timestamp(new Date().getTime()-(60*60*1000)))){
                    //Finalize it and 
                    mr.setEnd_meeting(new Timestamp(new Date().getTime()));
                    mr.setEnd_record(mr.getEnd_meeting());
                    mr.setFinished((byte)1);
                    meetingroomDao.save(mr);
                    mr = meetingroomDao.findByRoomIdNotFinished(room.getId());
                }
                UserMeeting aux = null;
                if (mr!=null) {
                    if (meeting!=null) {
                        is_reload = mr.getId() == meeting.getId() || mr.getNumber_participants()<=0;
                        aux = userMeetingDao.findUserMeetingByPK(new UserMeetingId(user, meeting));
                    } else {
                        aux = userMeetingDao.findUserMeetingByPK(new UserMeetingId(user, mr));
                    }
                }
                if (!is_reload){
                    if (aux !=null && aux.getPk() != null) {
                        is_reload = true;
                    }
                    else {
                        if (room.isIs_blocked() && mr!=null && mr.getFinished()==(byte)1){
                            room.setIs_blocked(false);
                            room.setReason_blocked(null);
                            roomDao.save(room);
                        }
                    }
                }

                UserMeeting userMeeting;
                //abertranb check if meeting is finsihed or not
                if (room.isIs_blocked() && !is_reload) {
                    if (mr == null || mr.getId() == 0 || mr.getRecorded()==(byte)0) {
                        room.setIs_blocked(false);
                        room.setReason_blocked(null);
                        roomDao.save(room);
                    }
                }
                if (!room.isIs_blocked() || is_reload) {

                    //If we find it, set +1 to participants and update
                    if (mr != null && mr.getId() != 0) {
                        meeting = mr;
                        
                        if (aux.getPk() == null) {
                            meeting.setNumber_participants(mr.getNumber_participants() + 1);
                        } else {
                            meeting.setNumber_participants(userMeetingDao.countNumberParticipants(meeting));
                        }
                        //If the number of participants == 6 then the room is blocked
                        if (meeting.getNumber_participants() > max_participants) {
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
                        String meetingIdPath = Util.getMeetingIdPath(course.getCoursekey(), room.getKey(), meeting.getId());

                        UserMeetingId umId = new UserMeetingId(user, meeting);
                        userMeeting = new UserMeeting(umId, new Timestamp(date.getTime()), meetingIdPath + "_" + user.getUsername(), extra_role);
                        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
                        ReadableUserAgent agent = parser.parse(request.getHeader("User-Agent"));
                        String user_agent = agent.getName()+" "+agent.getVersionNumber();
                        String platform = agent.getOperatingSystem().getName();                                                
                        userMeeting.setUserAgent(user_agent);
                        userMeeting.setPlatform(platform);
                        
                        userMeetingDao.save(userMeeting);
                        
                        
                        updateHistoryUserMeetingTable(userMeeting);
                        
                        session.setAttribute(Constants.USER_METTING_SESSION, userMeeting);
                    }
                    session.setAttribute(Constants.MEETING_SESSION, meeting);
                } else {
                    can_access_to_meeting = false;
                }

                model.addObject("max_participants", max_participants);
                model.addObject("user", user);
                model.addObject("course", course);
                model.addObject("meeting", meeting);
                model.addObject("userMeeting", session.getAttribute(Constants.USER_METTING_SESSION));
                model.addObject("wowza_stream_server", wowzaUrl);
                model.addObject("auto_recording", session.getAttribute(Constants.PARAM_AUTO_RECORDING_CUSTOM_LTI_PARAMETER));
                String url_notify_started_recording = session.getAttribute(Constants.PARAM_URL_NOTIFY_STARTED_RECORDING_CUSTOM_LTI_PARAMETER)!=null?(String)session.getAttribute(Constants.PARAM_URL_NOTIFY_STARTED_RECORDING_CUSTOM_LTI_PARAMETER):null;
                //check if the url is not null and not has the return_id parameter
                if (url_notify_started_recording!=null && url_notify_started_recording.length()>0 && !url_notify_started_recording.contains("return_id")) { 
                    String separator = "?";
                    if (url_notify_started_recording.contains("?")) {
                        separator = "&";
                    }
                    url_notify_started_recording += separator + Constants.PARAM_URL_NOTIFY_RETURN_ID+"="+meeting.getId();
                    //with close the flash can't get but with close yes!
                    url_notify_started_recording += "&" + Constants.PARAM_URL_NOTIFY_CLOSE_MEETING_SERVICE+"="+URLEncoder.encode(Util.getFullUrl(request, logger)+"rest/stop_external_meeting/"+pathMeeting, "UTF-8");
                    //url_notify_started_recording += "&" + Constants.PARAM_URL_NOTIFY_CLOSE_MEETING_SERVICE+"="+URLEncoder.encode(Util.getFullUrl(request, logger)+"rest/close_external_meeting/"+pathMeeting, "UTF-8");
                    session.setAttribute(Constants.PARAM_URL_NOTIFY_STARTED_RECORDING_CUSTOM_LTI_PARAMETER, url_notify_started_recording);
                }
                model.addObject("url_notify_started_recording", url_notify_started_recording);
                model.addObject("url_notify_ended_recording", session.getAttribute(Constants.PARAM_URL_NOTIFY_ENDED_RECORDING_CUSTOM_LTI_PARAMETER));
                model.addObject("disable_back_button", session.getAttribute(Constants.DISABLE_BACK_BUTTON_CUSTOM_LTI_PARAMETER));
                              
                model.addObject("window_focus_name", session.getAttribute(Constants.PARAM_WINDOW_NAME_FOCUS_CUSTOM_LTI_PARAMETER));

                if (meeting!=null) {
                    model.addObject("is_recorded", meeting.getRecorded() == (byte) 1);
                    //get the list of current participants
                    List<UserMeeting> participants = userMeetingDao.findUsersByMeetingId(meeting, user.getId(), true, false, 0);
                    model.addObject("participants", participants);
                }
                if (!can_access_to_meeting ) {
                    String error = "error";
                    if (room.isIs_blocked()) {
                        //nloquejada
                        model.addObject("reason_max_participants", Constants.REASON_BLOCK_MAX_PARTICIPANTS);
                        model.addObject("reason_recording", Constants.REASON_BLOCK_RECORDING);
                        model.addObject("reason", room.getReason_blocked());
                        error = "errorBlocked";
                    }
                    model.setViewName(error);
                }
            } else {
               String error = "errorSession";
               model.setViewName(error);      
            }
        } catch(Exception e) {
            model = new ModelAndView("error");
            logger.error("Error in videochat "+e.getMessage(), e);
        }

        return model;

    }
    
    public static void updateHistoryUserMeetingTable(UserMeeting userMeeting) {
        try {
            if (userMeeting!=null && userMeeting.getPk()!=null) {
                ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
                UserMeetingHistoryDao userMeetingHistoryDao = context.getBean(UserMeetingHistoryDao.class);
                UserMeetingHistory userMeetingHistory = userMeetingHistoryDao.findUserMeetingByPK(userMeeting.getPk());
                if (userMeetingHistory!=null && userMeetingHistory.getUser_id()>0) {
                    userMeetingHistory.setDeleted(new Timestamp(new Date().getTime()));
                } else {
                    if (userMeetingHistory==null) {
                        userMeetingHistory = new UserMeetingHistory();
                    }
                    //is new
                    userMeetingHistory.setUser_id(userMeeting.getPk().getUser().getId());
                    userMeetingHistory.setMeeting_id(userMeeting.getPk().getMeeting().getId());
                    userMeetingHistory.setExtraRole(userMeeting.getExtra_role());
                    userMeetingHistory.setUserAgent(userMeeting.getUserAgent());
                    userMeetingHistory.setPlatform(userMeeting.getPlatform());
                    
                }
                userMeetingHistoryDao.save(userMeetingHistory);
            }
        } catch(Exception e) {
            logger.error("Error in videochat updating updateHistoryUserMeetingTable "+e.getMessage(), e);
        }
                        
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
    
    @RequestMapping(value={"testEnvironment"})
    public ModelAndView testEnvironment() {
        Course course = new Course();
        ModelAndView model = new ModelAndView("testEnvironment");
        model.addObject("wowza_stream_server", wowzaUrl);
        model.addObject("course", course);
        return model;
    }

}