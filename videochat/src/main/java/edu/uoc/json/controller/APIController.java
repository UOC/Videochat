/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.json.controller;

import edu.uoc.common.controller.UserController;
import edu.uoc.dao.ChatDao;
import edu.uoc.dao.CourseDao;
import edu.uoc.dao.MeetingRoomDao;
import edu.uoc.dao.RoomDao;
import edu.uoc.dao.UserCourseDao;
import edu.uoc.dao.UserDao;
import edu.uoc.dao.UserMeetingDao;
import edu.uoc.model.Chat;
import edu.uoc.model.Course;
import edu.uoc.model.json.JSONResponseCourse;
import edu.uoc.model.json.JSONResponseList;
import edu.uoc.model.json.JSONResponseSetting;
import edu.uoc.model.json.JSONResponseUser;
import edu.uoc.model.json.JSONResponseVideochat;
import edu.uoc.model.json.JSONResponseVideochatChat;
import edu.uoc.model.json.JSONResponseCourseMeeting;
import edu.uoc.model.json.JSONResponseVideochatParticipant;
import edu.uoc.model.MeetingRoom;
import edu.uoc.model.MeetingRoomExtended;
import edu.uoc.model.Participant;
import edu.uoc.model.Room;
import edu.uoc.model.SearchMeeting;
import edu.uoc.model.User;
import edu.uoc.model.UserCourse;
import edu.uoc.model.UserMeeting;
import edu.uoc.model.UserMeetingId;
import edu.uoc.model.json.JSONResponse;
import edu.uoc.model.json.JSONResponseMeeting;
import edu.uoc.model.json.JSONResponseTokenEmail;
import edu.uoc.model.json.JSONUserPassword;
import edu.uoc.util.Constants;
import edu.uoc.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.json.*;
/**
 *
 * @author antonibertranbellido
 */
@Controller
public class APIController {
    //get log4j handler
    private static final Logger logger = Logger.getLogger(APIController.class);

    
    @Autowired
    private UserMeetingDao userMeetingDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private MeetingRoomDao meetingDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private UserCourseDao userCourseDao;
    @Autowired
    private MeetingRoomDao meetingRoomDao;
    @Autowired
    private ChatDao chatDao;
    @Value( "${wowza.url.server}" )
    private String wowzaUrl;
    @Value( "${wowza.port.server}" )
    private String wowzaPort;
    @Value( "${wowza.application.server}" )
    private String wowzaApplication;
    @Value( "${wowza.vod_application.server}" )
    private String wowzaVODApplication;
    @Value( "${user.publisher.username}" )
    private String publisherUsername;
    @Value( "${user.publisher.password}" )
    private String publisherPassword;
    @Value( "${enabled.mobile.data.connection}" )
    private String enabled_mobile_data_connection;
    @Value( "${authentication_system}" )
    private String authentication_system;
    @Value( "${moodle_site_url}" )
    private String moodle_site_url;
    @Value( "${moodle_service_name}" )
    private String moodle_service_name;
    @Value( "${moodle_login_resource}" )
    private String moodle_login_resource;
    @Value( "${moodle_webservice_resource}" )
    private String moodle_webservice_resource;
    @Value( "${moodle_oauth_consumer_key}" )
    private String moodle_oauth_consumer_key;
    @Value( "${moodle_custom_username}" )
    private String moodle_custom_username;
    private StringBuilder responseStr;
    //Wowza API
    @Value( "${wowza.url.server}" )
    private String wowzaServer;
    @Value( "${user.api.port}" )
    private String apiPort;
    @Value( "${user.api.schema}" )
    private String apiSchema;
    @Value( "${user.api.application}" )
    private String apiApplication;
    @Value( "${user.api.application}" )
    private String liveStreamApplication;
    @Value( "${user.api.username}" )
    private String apiUsername;
    @Value( "${user.api.password}" )
    private String apiPassword;

    
    /**
     * Gets the user
     * @param email
     * @param token
     * @return
     * @throws Exception 
     */
    private User getUser(String email, String token) throws Exception {
        
        return userDao.findByUserToken(email, token);
            
    }
    /*@RequestMapping(value = "/get_token/{username}/{password}", method = RequestMethod.POST)
    public @ResponseBody JSONResponse getToken(@PathVariable String username, @PathVariable String password) {
        JSONUserPassword userPwd = new JSONUserPassword();
        userPwd.setUsername(username);
        userPwd.setPassword(password);
        return getTokenByUserPwd(userPwd);
    }*/
    @RequestMapping(value = "/get_token", method = RequestMethod.POST)
    public @ResponseBody JSONResponseTokenEmail getToken(@RequestBody JSONUserPassword userPwd) {
        JSONResponseTokenEmail response = new JSONResponseTokenEmail();
        if (Constants.AUTENTICATION_METHOD_REMOTE_MOODLE.equals(authentication_system)) {
            if (moodle_site_url!=null && moodle_site_url.length()>0 &&
                moodle_login_resource!=null && moodle_login_resource.length()>0 &&
                moodle_webservice_resource!=null && moodle_webservice_resource.length()>0) {
                if (moodle_service_name!=null && moodle_service_name.length()>0) {
                    try {
                        String url = moodle_site_url + moodle_login_resource + "?" +
                                Constants.USERNAME_MOODLE_PARAM_NAME
                                + "=" +
                                URLEncoder.encode(userPwd.getUsername(), Constants.CHARSET_URL_PARAMS)
                                + "&" +
                                Constants.PASSWORD_MOODLE_PARAM_NAME
                                + "=" +
                                URLEncoder.encode(userPwd.getPassword(), Constants.CHARSET_URL_PARAMS)
                                + "&" +
                                Constants.SERVICE_MOODLE_PARAM_NAME
                                + "=" +
                                URLEncoder.encode(moodle_service_name, Constants.CHARSET_URL_PARAMS)
                                ;

                        JSONObject jsonobj = doRequest(url);
                        if (jsonobj.has("token")) {
                            //get the email
                            String username = userPwd.getUsername();
                            if (moodle_custom_username==null || !"1".equals(moodle_custom_username)) {
                                username = ""+getUserMoodleToken(jsonobj.getString("token"));
                            }
                            username = Util.sanitizeString(moodle_oauth_consumer_key+":"+username);
                            User user = userDao.findByUserName(username);
                            if (user==null || user.getUsername()==null) {
                                //Create user
                                user = new User();
                                user.setUsername(username);
                                user.setToken_access(jsonobj.getString("token"));
                                userDao.save(user);
                            }
                            response.setEmail(user.getEmail());
                            response.setOk(true);
                            if (user.getToken_access()==null 
                                    || !jsonobj.getString("token").equals(user.getToken_access())) {
                                user.setToken_access(jsonobj.getString("token"));
                                userDao.save(user);
                            }                    
                            response.setResponse(jsonobj.getString("token"));
                        } else {
                            response.setOk(false);
                            if (jsonobj.has("error")) {
                                response.setErrorCode(jsonobj.getString("error"));
                                response.setErrorMessage(jsonobj.getString("error"));
                            } else {
                                response.setErrorMessage(responseStr.toString());
                            }
                        }
                    } catch (IOException e) {
                        logger.error("There is IOException an error in Moodle authentication ", e);
                        response.setOk(false);
                        response.setErrorCode(Constants.ERROR_CODE_AUTENTICATION_METHOD_MOODLE_INTERNAL_ERROR);
                        response.setErrorMessage(Constants.ERROR_AUTENTICATION_METHOD_MOODLE_INTERNAL_ERROR+" "+e.getMessage());
                        
                    } catch (JSONException e) {
                        logger.error("There is JSONException an error in Moodle authentication ", e);
                        response.setOk(false);
                        response.setErrorCode(Constants.ERROR_CODE_AUTENTICATION_METHOD_MOODLE_INTERNAL_ERROR);
                        response.setErrorMessage(Constants.ERROR_AUTENTICATION_METHOD_MOODLE_INTERNAL_ERROR+" "+e.getMessage());
                    } catch (Exception e) {
                        logger.error("There is JSONException an error in Moodle authentication ", e);
                        response.setOk(false);
                        response.setErrorCode(Constants.ERROR_CODE_AUTENTICATION_METHOD_MOODLE_INTERNAL_ERROR);
                        response.setErrorMessage(Constants.ERROR_AUTENTICATION_METHOD_MOODLE_INTERNAL_ERROR+" "+e.getMessage());
                    }
                } else {
                    response.setOk(false);
                    response.setErrorCode(Constants.ERROR_CODE_AUTENTICATION_METHOD_MOODLE_SERVICE_INVALID);
                    response.setErrorMessage(Constants.ERROR_AUTENTICATION_METHOD_MOODLE_SERVICE_INVALID);
                }
                                    
            } else {
                response.setOk(false);
                response.setErrorCode(Constants.ERROR_CODE_AUTENTICATION_METHOD_MOODLE_URL_INVALID);
                response.setErrorMessage(Constants.ERROR_AUTENTICATION_METHOD_MOODLE_URL_INVALID);
            }
        } else {
            response.setOk(false);
            response.setErrorCode(Constants.ERROR_CODE_AUTENTICATION_METHOD_NOT_ENABLED);
            response.setErrorMessage(Constants.ERROR_AUTENTICATION_METHOD_NOT_ENABLED);
        }
        return response;
    }    
    
    private int getUserMoodleToken(String moodleToken) throws Exception {
        if (Constants.AUTENTICATION_METHOD_REMOTE_MOODLE.equals(authentication_system)) {
            if (moodle_site_url!=null && moodle_site_url.length()>0 &&
                moodle_login_resource!=null && moodle_login_resource.length()>0 &&
                moodle_webservice_resource!=null && moodle_webservice_resource.length()>0) {
                if (moodle_service_name!=null && moodle_service_name.length()>0) {
                    try {
                        //http://moodle.test.speakapps.org/webservice/rest/server.php?
                        //wstoken=XXX&wsfunction=moodle_webservice_get_siteinfo&=json
                        String url = moodle_site_url + moodle_webservice_resource + "?" +
                                Constants.TOKEN_MOODLE_PARAM_NAME
                                + "=" +
                                URLEncoder.encode(moodleToken, Constants.CHARSET_URL_PARAMS)
                                + "&" +
                                Constants.FUNCTION_MOODLE_PARAM_NAME
                                + "=" +
                                URLEncoder.encode(Constants.FUNCTION_GET_SITEINFO_MOODLE_PARAM_VALUE, Constants.CHARSET_URL_PARAMS)
                                + "&" +
                                Constants.FUNCTION_RESTFORMAT_MOODLE_PARAM_NAME
                                + "=" +
                                URLEncoder.encode(Constants.FUNCTION_RESTFORMAT_MOODLE_PARAM_VALUE, Constants.CHARSET_URL_PARAMS)
                                ;

                        JSONObject jsonobj = doRequest(url);
                        if (jsonobj.has("userid")) {
                            return jsonobj.getInt("userid");
                        } else {
                            Exception e = null;
                            if (jsonobj.has("exception")) {
                                e = new Exception (jsonobj.getString("message"));
                                
                            } else {
                                e = new Exception (responseStr.toString());
                            }
                            throw e;
                        }
                    } catch (IOException e) {
                        logger.error("There is IOException an error in Moodle authentication ", e);
                        throw e;
                    } catch (JSONException e) {
                        logger.error("There is JSONException an error in Moodle authentication ", e);
                        throw e;
                    }
                } else {
                    throw new Exception (Constants.ERROR_AUTENTICATION_METHOD_MOODLE_SERVICE_INVALID);
                }
                                    
            } else {
                throw new Exception (Constants.ERROR_AUTENTICATION_METHOD_MOODLE_URL_INVALID);
            }
        } else {
            throw new Exception (Constants.ERROR_AUTENTICATION_METHOD_NOT_ENABLED);
        }
    }
    /**
     * Creates a json request
     * @param url
     * @return
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws IOException 
     */
    private JSONObject doRequest(String url) throws MalformedURLException, ProtocolException, IOException, JSONException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        responseStr = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            responseStr.append(inputLine);
        }
        in.close();
        logger.info("Return: "+responseStr);

        return new JSONObject(responseStr.toString().trim());
    }
    @RequestMapping(value = "/course/{email}/{token}", method = RequestMethod.GET)
    public @ResponseBody JSONResponseList getCourses(@PathVariable String email, @PathVariable String token) {
        JSONResponseList response = new JSONResponseList();
        try {
            User user = getUser(email, token);
            List <UserCourse> list = userCourseDao.findCoursesByUserId(user.getId());
            if (list==null){
                list = new ArrayList<UserCourse>() {};
            }
            Iterator<UserCourse> iterator = list.iterator();
            List<Course> listCourse = new ArrayList<Course>();
            while (iterator.hasNext()) {
                listCourse.add(courseDao.findByCourseCode(iterator.next().getPk().getCourse().getId()));
            }
            response.setOk(true);
            response.setList(listCourse);
            
        } catch (Exception e) {
            logger.error("Error getting courses ", e);
            response.setErrorCode(e.getMessage());
            response.setErrorMessage(e.getMessage());
            response.setOk(false);
        }
        return response;
    }
    
    @RequestMapping(value = "/course/{email}/{token}/{id}", method = RequestMethod.GET)
    public @ResponseBody JSONResponseCourse getCourse(@PathVariable String email, @PathVariable String token, @PathVariable int id) {
        JSONResponseCourse response = new JSONResponseCourse();
        try {
            getUser(email, token);
            Course course = courseDao.findByCourseCode(id);
            logger.info("Course with "+id+" is equal to "+course.getId());
            if (course!=null && course.getId()==id) {
                response.setOk(true);
                course.setRoom(roomDao.findByIdCourse(course.getId()));
                
                course.setUserCourse(userCourseDao.findUsersByCourse(course.getId()));
                
                response.setCourse(course);
            } else {
                response.setOk(false);
                response.setErrorCode("CourseNotFound");
                response.setErrorMessage("Course "+id+" not found");
            }
            
        } catch (Exception e) {
            logger.error("Error getting course ", e);
            response.setErrorCode(e.getMessage());
            response.setErrorMessage(e.getMessage());
            response.setOk(false);
        }
        return response;
    }
    
    @RequestMapping(value = "/course/meetings/{email}/{token}/{course_id}", method = RequestMethod.GET)
    public @ResponseBody JSONResponseCourseMeeting getMeetingsOfCourse(@PathVariable String email, @PathVariable String token, @PathVariable int course_id) {
        JSONResponseCourseMeeting response = new JSONResponseCourseMeeting();
        try {
            getUser(email, token);
            Course course = courseDao.findByCourseCode(course_id);
            logger.info("Course with "+course_id+" is equal to "+course.getId());
            if (course!=null && course.getId()==course_id) {
                response.setOk(true);
                
                List<Room> listRooms = roomDao.findByIdCourse(course.getId());

                SearchMeeting searchMeeting = new SearchMeeting();
                Room room = null;
                if (searchMeeting.getRoom_Id() > 0) {
                    searchMeeting.setRoom(roomDao.findByRoomCode(searchMeeting.getRoom_Id()));
                }
                //TODO add filters!
                List<MeetingRoom> meetingRooms = meetingDao.findbyForm(searchMeeting, listRooms);

                List<MeetingRoomExtended> listMRE = new ArrayList<MeetingRoomExtended>();
               // List<UserMeeting> meetingsByUser = new ArrayList<UserMeeting>();
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
                        //meetingsByUser.addAll(userMeetingDao.findUserByMeetingId(meeting, user.getId(), true));
                    }
                    response.setListMeetings(listMRE);
                } else {
                    logger.info("No results found!");
                }
            } else {
                response.setOk(false);
                response.setErrorCode("CourseNotFound");
                response.setErrorMessage("Course "+course_id+" not found");
            }
            
        } catch (Exception e) {
            logger.error("Error getting course ", e);
            response.setErrorCode(e.getMessage());
            response.setErrorMessage(e.getMessage());
            response.setOk(false);
        }
        return response;
    }
    
    @RequestMapping(value = "/course/meetings/{email}/{token}/{course_id}/{room_id}", method = RequestMethod.GET)
    public @ResponseBody JSONResponseCourseMeeting getMeetingsOfCourseByRoomId(@PathVariable String email, @PathVariable String token, @PathVariable int course_id, @PathVariable int room_id) {
        JSONResponseCourseMeeting response = new JSONResponseCourseMeeting();
        try {
            getUser(email, token);
            Course course = courseDao.findByCourseCode(course_id);
            logger.info("Course with "+course_id+" is equal to "+course.getId());
            if (course!=null && course.getId()==course_id) {
                response.setOk(true);
                
                List<Room> listRooms = new ArrayList<Room>();
                listRooms.add(roomDao.findByRoomCode(room_id));

                SearchMeeting searchMeeting = new SearchMeeting();
                Room room = null;
                if (searchMeeting.getRoom_Id() > 0) {
                    searchMeeting.setRoom(roomDao.findByRoomCode(searchMeeting.getRoom_Id()));
                }
                //TODO add filters!
                List<MeetingRoom> meetingRooms = meetingDao.findbyForm(searchMeeting, listRooms);

                List<MeetingRoomExtended> listMRE = new ArrayList<MeetingRoomExtended>();
               // List<UserMeeting> meetingsByUser = new ArrayList<UserMeeting>();
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
                        //meetingsByUser.addAll(userMeetingDao.findUserByMeetingId(meeting, user.getId(), true));
                    }
                    response.setListMeetings(listMRE);
                } else {
                    logger.info("No results found!");
                }
            } else {
                response.setOk(false);
                response.setErrorCode("CourseNotFound");
                response.setErrorMessage("Course "+course_id+" not found");
            }
            
        } catch (Exception e) {
            logger.error("Error getting course ", e);
            response.setErrorCode(e.getMessage());
            response.setErrorMessage(e.getMessage());
            response.setOk(false);
        }
        return response;
    }
    
    @RequestMapping(value = "/profile/{email}/{token}/{id}", method = RequestMethod.GET)
    public @ResponseBody JSONResponseUser getUserProfile(@PathVariable String email, @PathVariable String token, @PathVariable int id) {
        JSONResponseUser response = new JSONResponseUser();
        try {
            getUser(email, token);
            User user = userDao.findByUserCode(id);
            response.setUser(user);
            response.setOk(true);
        } catch (Exception e) {
            logger.error("Error getting user profile ", e);
            response.setErrorCode(e.getMessage());
            response.setErrorMessage(e.getMessage());
            response.setOk(false);
        }
        return response;
    }
    @RequestMapping(value = "/profile/{email}/{token}", method = RequestMethod.GET)
    public @ResponseBody JSONResponseUser getMyUserProfile(@PathVariable String email, @PathVariable String token) {
        JSONResponseUser response = new JSONResponseUser();
        try {
            User user = getUser(email, token);
                
            response.setUser(user);
            response.setOk(true);
        } catch (Exception e) {
            logger.error("Error getting user profile ", e);
            response.setErrorCode(e.getMessage());
            response.setErrorMessage(e.getMessage());
            response.setOk(false);
        }
        return response;
    }
    @RequestMapping(value = "/settings/{email}/{token}", method = RequestMethod.GET)
    public @ResponseBody JSONResponseSetting getSettings(@PathVariable String email, @PathVariable String token) {
        JSONResponseSetting response = new JSONResponseSetting();
        try {
            getUser(email, token);
                
            response.setStreamServerUrl(wowzaUrl);
            response.setStreamServerPort(wowzaPort);
            response.setStreamServerApplication(wowzaApplication);
            response.setStreamServerVODApplication(wowzaVODApplication);
            response.setStreamPublisherUsername(publisherUsername);
            response.setStreamPublisherPassword(publisherPassword);
            response.setOk(true);
        } catch (Exception e) {
            logger.error("Error getting user profile ", e);
            response.setErrorCode(e.getMessage());
            response.setErrorMessage(e.getMessage());
            response.setOk(false);
        }
        return response;
    }
    
    @RequestMapping(value = "/videochat/{email}/{token}/{roomId}", method = RequestMethod.GET)
    public @ResponseBody JSONResponseVideochat accessMeeting(@PathVariable String email, @PathVariable String token, @PathVariable int roomId) {
        JSONResponseVideochat response = new JSONResponseVideochat();
        try{
            boolean can_access_to_meeting = true;
            User user = getUser(email, token);
            Room room = roomDao.findByRoomCode(roomId);
            Course course = room.getId_course();
            MeetingRoom meeting = null;
            if (user != null && room.getId()==roomId) {
                String pathMeeting = course.getCoursekey() + "_" + room.getKey() + "_" + room.getId();
                boolean is_new_meeting = true;

                boolean is_reload = false;
                //Find the room and the meeting room associate to this room
                MeetingRoom mr = meetingRoomDao.findByRoomIdNotFinished(room.getId());
                UserMeeting aux = null;
                if (mr!=null) {
                    aux = userMeetingDao.findUserMeetingByPK(new UserMeetingId(user, mr));
                }
                if (!is_reload && aux !=null && aux.getPk() != null) {
                    is_reload = true;
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
                        meetingRoomDao.save(meeting);
                        String meetingIdPath = course.getCoursekey() + "_" + room.getKey() + "_" + meeting.getId();

                        UserMeetingId umId = new UserMeetingId(user, meeting);
                     
                        userMeeting = new UserMeeting(umId, new Timestamp(date.getTime()), meetingIdPath + "_" + user.getUsername(),"" );
                        userMeetingDao.save(userMeeting);
                        response.setUrlStream(userMeeting.getStreamKey());
                        response.setMeetingId(meeting.getId());
                        
                        UserController.updateHistoryUserMeetingTable(userMeeting);
                    }
                    
                } else {
                    can_access_to_meeting = false;
                }

                
                if (!can_access_to_meeting ) {
                    String error = "errorSession";
                    if (user != null && room != null && room.isIs_blocked()) {
                        error = "errorBlocked";
                        response.setErrorCode(error);
                        response.setErrorMessage("reason_max_participants "+Constants.REASON_BLOCK_MAX_PARTICIPANTS);
                    }
                }
            } else {
                can_access_to_meeting = false;
                response.setErrorCode("Room not found");
                response.setErrorMessage("Room not found "+roomId);
            }
            response.setOk(can_access_to_meeting);
        } catch (Exception e) {
            logger.error("Error getting user profile ", e);
            response.setErrorCode(e.getMessage());
            response.setErrorMessage(e.getMessage());
            response.setOk(false);
        }
        return response;
    }
    
    @RequestMapping(value = "/videochat/participants/{email}/{token}/{meetingId}", method = RequestMethod.GET)
    public @ResponseBody JSONResponseVideochatParticipant getParticipantsList(@PathVariable String email, @PathVariable String token, @PathVariable int meetingId) {
        JSONResponseVideochatParticipant response = new JSONResponseVideochatParticipant();
        try{
            boolean found = false;
            User user = getUser(email, token);
            MeetingRoom meeting = meetingDao.findById(meetingId);
            if (meeting.getId()==meetingId) {
                found = true;
                //Avoid disconnection problems to add the current user to the list of users
                List <Participant> participants = new ArrayList<Participant>();
                
                List<UserMeeting> currentUserExists = userMeetingDao.findUserByMeetingId(meeting, user.getId(), false);
                if (currentUserExists==null || currentUserExists.size()==0) {
                    //Add it
                    logger.info("There is an user "+user.getId()+" that has some problem because launch a request to get the list of participants and is not present");
                    UserMeeting current_user = new UserMeeting();
                    UserMeetingId umId = new UserMeetingId(user, meeting);
                    String meetingIdPath = meeting.getId_room().getId_course().getCoursekey() + "_" + meeting.getId_room().getKey() + "_" + meeting.getId();
                    current_user = new UserMeeting(umId, new Timestamp(new Date().getTime()), meetingIdPath + "_" + user.getUsername(),"" );
                    userMeetingDao.save(current_user);
                        
                }/* No fa falta
                else {
                    if (currentUserExists.get(0).getAccessConfirmed()!=(byte)1) {
                        logger.info("There is an user "+user.getId()+" that has some problem because launch a request to get the list of participants and is not confirmed");
                        //update it
                        currentUserExists.get(0).setAccessConfirmed((byte)1);
                        userMeetingDao.save(currentUserExists.get(0));
                    }
                }*/

                List<UserMeeting> listUserMeeting = userMeetingDao.findUsersByMeetingId(meeting, true);
                if (listUserMeeting!=null) {
                    meeting.setNumber_participants(listUserMeeting.size());
                    Iterator<UserMeeting> iterator = listUserMeeting.iterator();
                    UserMeeting userMeetingTemp = null;
                    while (iterator.hasNext()){
                        userMeetingTemp = iterator.next();
                        participants.add(new Participant(
                                userMeetingTemp.getPk().getUser().getId(),
                                userMeetingTemp.getPk().getUser().getUsername(),
                                userMeetingTemp.getPk().getUser().getFirstname(),
                                userMeetingTemp.getPk().getUser().getSurname(),
                                userMeetingTemp.getPk().getUser().getFullname(),
                                userMeetingTemp.getPk().getUser().getEmail(),
                                userMeetingTemp.getPk().getUser().getImage(),
                                userMeetingTemp.getStreamKey(),
                                userMeetingTemp.getExtra_role()
                        ));
                    }
                }

                response.setParticipants(participants);
            }

            response.setOk(found);
        } catch (Exception e) {
            logger.error("Error getting participants list", e);
            response.setErrorCode(e.getMessage());
            response.setErrorMessage(e.getMessage());
            response.setOk(false);
        }
        return response;
    }
    
    @RequestMapping(value = "/videochat/chat/{email}/{token}/{meetingId}/{lang_code}", method = RequestMethod.GET)
    public @ResponseBody JSONResponseVideochatChat getChatList(@PathVariable String email, @PathVariable String token, @PathVariable int meetingId, @PathVariable String langCode) {
        JSONResponseVideochatChat response = new JSONResponseVideochatChat();
        try{
            boolean found = false;
            getUser(email, token);
            MeetingRoom meeting = meetingDao.findById(meetingId);
            if (meeting.getId()==meetingId) {
                found = true;
                List <Chat> listChat = chatDao.findByMeetingId(meeting);

                response.setChatMessages(listChat);
            }

            response.setOk(found);
        } catch (Exception e) {
            logger.error("Error getting chat list", e);
            response.setErrorCode(e.getMessage());
            response.setErrorMessage(e.getMessage());
            response.setOk(false);
        }
        return response;
    }
    
    @RequestMapping(value = "/videochat/meeting/{email}/{token}/{meetingId}", method = RequestMethod.GET)
    public @ResponseBody JSONResponseMeeting getMeeting(@PathVariable String email, @PathVariable String token, @PathVariable int meetingId) {
        JSONResponseMeeting response = new JSONResponseMeeting();
        try{
            getUser(email, token);
            MeetingRoom meeting = meetingDao.findById(meetingId);
            response.setMeeting(meeting);
            response.setOk(true);
        } catch (Exception e) {
            logger.error("Error getting meeting ", e);
            response.setErrorCode(e.getMessage());
            response.setErrorMessage(e.getMessage());
            response.setOk(false);
        }
        return response;
    }
    
    
    @RequestMapping(value = "/videochat/access/{email}/{token}/{meetingId}", method = RequestMethod.POST)
    public @ResponseBody JSONResponse setAccessParticipant(@PathVariable String email, @PathVariable String token, @PathVariable int meetingId) {
        JSONResponse response = new JSONResponse();
        try{
            boolean found = false;
            User user = getUser(email, token);
            MeetingRoom meeting = meetingDao.findById(meetingId);
            
            if (meeting.getId()==meetingId) {
                List<UserMeeting> userMeetingList = userMeetingDao.findUserByMeetingId(meeting, user.getId(), false);
                if (userMeetingList!=null && userMeetingList.size()>0) {
                    UserMeeting userMeeting = userMeetingList.get(0);
                    found = true;
                    userMeeting.setAccessConfirmed((byte)1);
                    this.userMeetingDao.save(userMeeting);
                }
            }

            response.setOk(found);
        } catch (Exception e) {
            logger.error("Error getting participants list", e);
            response.setErrorCode(e.getMessage());
            response.setErrorMessage(e.getMessage());
            response.setOk(false);
        }
        return response;
    }
    
    @RequestMapping(value = "/videochat/access/{email}/{token}/{meetingId}", method = RequestMethod.DELETE)
    public @ResponseBody JSONResponse deleteUser(@PathVariable String email, @PathVariable String token, @PathVariable int meetingId) {
        JSONResponse response = new JSONResponse();
        try {
            boolean should_close_it = true;
            User user = getUser(email, token);
            MeetingRoom meeting = meetingDao.findById(meetingId);
            if (user!=null && meeting!=null && meeting.getId()==meetingId) {
                meeting = meetingDao.findById(meeting.getId());
                if (meeting.getFinished()!=(byte)1 && meeting.getRecorded()!=(byte)1) {
                    UserMeetingId mId = new UserMeetingId();
                    mId.setMeeting(meeting);
                    mId.setUser(user);
                    UserMeeting userMeetingDeleted = userMeetingDao.findUserMeetingByPK(mId);

                    if (userMeetingDeleted.getPk()!=null && userMeetingDeleted.getPk().getUser()!=null) {
                        userMeetingDao.delete(userMeetingDeleted);
                        UserController uController = new UserController();
                        uController.updateHistoryUserMeetingTable(userMeetingDeleted);

                        meeting.setNumber_participants(userMeetingDao.countNumberParticipants(meeting));
                        if (should_close_it && meeting.getNumber_participants()==0){
                            meeting.setFinished((byte)1);
                            meeting.setEnd_meeting(new Timestamp(new Date().getTime()));
                            /*if (meeting.getId_room()==null) {
                                Room room = this.meetingDao.findByRoomId(meetingId, should_close_it)
                                meeting.setId_room(room);
                            }*/
                            Room room = meeting.getId_room();//this.roomDao.findByRoomCode(meeting.getId_room());
                            if (room!=null) {
                                room.setIs_blocked(false);
                                room.setReason_blocked(null);
                                this.roomDao.save(room);
                            }
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
    
    /*@RequestMapping(method = RequestMethod.POST)
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
            logger.error("Lock session ", e);
            
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
    }*/
    
    @RequestMapping(value = "/videochat/start_record/{email}/{token}/{meetingId}", method = RequestMethod.POST)
    public @ResponseBody JSONResponse startRecord(@PathVariable String email, @PathVariable String token, @PathVariable int meetingId) {
        JSONResponse response = new JSONResponse();
        String url=null;
        try {
            User user = getUser(email, token);
            MeetingRoom meeting = meetingDao.findById(meetingId);
            if (user!=null && meeting!=null && meeting.getId()==meetingId) {
                http://wowza_mobile:pQkzgWVu4od69gCU@housing2-189.ilimit.es:8086/livestreamrecord?app=videochat&streamname=myStream&action=startRecording
                url = apiSchema+"://"+wowzaServer+":"+apiPort+"/"+apiApplication
                        +"?action="+Constants.API_START_RECORD_PARAMETER+
                        "&room="+meeting.getPath()+"&username="+user.getUsername();
                logger.info("calling to "+url);
                response = Util.curlJson(url, apiUsername, apiPassword, "success", response,  logger);
                
            }
        } catch (NumberFormatException e) {
            logger.error("Start Recording NumberformatExceotion and url "+url, e);
            
        } catch (Exception e) {
            logger.error("Start Recording via api and url "+url, e);
            
        }
        return response;
    }    
    
    
    @RequestMapping(value = "/videochat/stop_record/{email}/{token}/{meetingId}", method = RequestMethod.POST)
    public @ResponseBody JSONResponse stopRecord(@PathVariable String email, @PathVariable String token, @PathVariable int meetingId) {
        JSONResponse response = new JSONResponse();
        String url=null;
        try {
            User user = getUser(email, token);
            MeetingRoom meeting = meetingDao.findById(meetingId);
            if (user!=null && meeting!=null && meeting.getId()==meetingId) {
                url = apiSchema+"://"+wowzaServer+":"+apiPort+"/"+apiApplication
                        +"?action="+Constants.API_STOP_RECORD_PARAMETER+
                        "&room="+meeting.getPath()+"&username="+user.getUsername();
                logger.info("calling to "+url);
                response = Util.curlJson(url, apiUsername, apiPassword, "success", response,  logger);
                
            }
        } catch (NumberFormatException e) {
            logger.error("Stop Recording NumberformatExceotion and url "+url, e);
            
        } catch (Exception e) {
            logger.error("Stop Recording  via api and url "+url, e);
            
        }
        return response;
    }    
    

}