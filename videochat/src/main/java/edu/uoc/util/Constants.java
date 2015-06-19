/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.util;

/**
 *
 * @author antonibertranbellido
 */
public class Constants {

    public static final String FIRST_NAME_LTI_PARAMETER = "lis_person_name_given";
    public static final String LAST_NAME_LTI_PARAMETER = "lis_person_name_family";

    public static final int MAX_PARTICIPANTS = 6;
    public static final String PLAYER_CUSTOM_LTI_PARAMETER = "player";
    public static final String SESSION_ID_CUSTOM_LTI_PARAMETER = "session_id";
    public static final String DEBUG_CUSTOM_LTI_PARAMETER = "debug";
    public static final String EXTRA_ROLE_CUSTOM_LTI_PARAMETER = "show_user_role";
    public static final String DISABLE_BACK_BUTTON_CUSTOM_LTI_PARAMETER = "disable_back_button";
    
    //when you want to play session then tries to get from table vc_usermeeting and if not found then get from vc_usermeeting_history
    public static final String GET_USERS_FROM_HISTORY_IF_NOT_FOUND = "get_users_from_history_if_not_found"; 

    public static final String REASON_BLOCK_MAX_PARTICIPANTS = "REASON_MAX_PARTICIPANTS";
    public static final String REASON_BLOCK_RECORDING = "REASON_ROOM_RECORDING";
    public static final String REASON_BLOCK_BY_USER = "REASON_ROOM_BLOCK_BY_USER";
    
    /********* SESSION OBJS */
    public static final String USER_SESSION = "sUser";
    public static final String COURSE_SESSION = "sCoruse";
    public static final String MEETING_SESSION = "sMeeting";
    public static final String ROOM_SESSION = "sRoom";
    public static final String USER_METTING_SESSION = "sUserMeeting";
    public static final String USER_COURSE_SESSION = "sUserCourse";
    public static final String USER_LANGUAGE = "sLang";
    public static final String PARAM_SPRING_LANG = "lang";
    public static final String PARAM_MAX_PARTICIPANTS_CUSTOM_LTI_PARAMETER = "max_participants";
    public static final String PARAM_AUTO_RECORDING_CUSTOM_LTI_PARAMETER = "auto_recording";
    public static final String PARAM_URL_NOTIFY_STARTED_RECORDING_CUSTOM_LTI_PARAMETER = "url_notify_started_recording";
    public static final String PARAM_URL_NOTIFY_ENDED_RECORDING_CUSTOM_LTI_PARAMETER = "url_notify_ended_recording";
    public static final String PARAM_URL_NOTIFY_SESSION_AVAILABLE_CUSTOM_LTI_PARAMETER = "url_notify_session_available";
    public static final String PARAM_WINDOW_NAME_FOCUS_CUSTOM_LTI_PARAMETER = "window_focus_name";
    public static final String PARAM_URL_NOTIFY_RETURN_ID = "return_id";
    public static final String PARAM_URL_NOTIFY_CLOSE_MEETING_SERVICE = "end_external_service";
    
    public static final String WOWZA_RECORD_FOLDER = "record";
    public static final String WOWZA_EXTENSION_FILE = ".mp4";
    
    public static final int FORMAT_DATETIME = 1;
    public static final int FORMAT_DATE = 2;
    public static final int FORMAT_TIME = 3;
    
    public static final String API_CLOSE_SESSION_PARAMETER = "close_session";
    public static final String API_STOP_RECORD_PARAMETER = "stop_record";
    public static final String API_START_RECORD_PARAMETER = "start_record";

    //Authentication System the values are:
    // remote_moodle: this kind of authentication is prepared to use an external service from Moodle
    //                read documentation here and to manage the authentication tokens you have to go MOODLE_SITE_URL/admin/settings.php?section=webservicetokens
    // internal:      this kind of authentication allows to use an internal authentication using email and token genrated by the Videochat    
    public static final String AUTENTICATION_METHOD_INTERNAL = "internal";
    public static final String AUTENTICATION_METHOD_REMOTE_MOODLE = "remote_moodle";
    public static final String TOKEN_MOODLE_PARAM_NAME = "wstoken";
    public static final String FUNCTION_MOODLE_PARAM_NAME = "wsfunction";
    public static final String FUNCTION_GET_SITEINFO_MOODLE_PARAM_VALUE = "moodle_webservice_get_siteinfo";
    public static final String FUNCTION_RESTFORMAT_MOODLE_PARAM_NAME = "moodlewsrestformat";
    public static final String FUNCTION_RESTFORMAT_MOODLE_PARAM_VALUE = "json";
    public static final String USERNAME_MOODLE_PARAM_NAME = "username";
    public static final String PASSWORD_MOODLE_PARAM_NAME = "password";
    public static final String SERVICE_MOODLE_PARAM_NAME = "service";
    public static final String CHARSET_URL_PARAMS = "UTF-8";
    public static final String ERROR_CODE_AUTENTICATION_METHOD_NOT_ENABLED = "AUTHENTICATION_METHOD_NOT_ENABLED";
    public static final String ERROR_AUTENTICATION_METHOD_NOT_ENABLED = "Authentication method not enabled";
    public static final String ERROR_CODE_AUTENTICATION_METHOD_MOODLE_URL_INVALID = "AUTHENTICATION_METHOD_MOODLE_URL_INVALID";
    public static final String ERROR_AUTENTICATION_METHOD_MOODLE_URL_INVALID = "The URL is void for the Moodle Authentication method";
    public static final String ERROR_CODE_AUTENTICATION_METHOD_MOODLE_SERVICE_INVALID = "AUTHENTICATION_METHOD_MOODLE_SERVICE_INVALID";
    public static final String ERROR_AUTENTICATION_METHOD_MOODLE_SERVICE_INVALID = "The SERVICE is void for the Moodle Authentication method";
    public static final String ERROR_CODE_AUTENTICATION_METHOD_MOODLE_INTERNAL_ERROR = "AUTHENTICATION_METHOD_MOODLE_INTERNAL_ERROR";
    public static final String ERROR_AUTENTICATION_METHOD_MOODLE_INTERNAL_ERROR = "There is an internal error in Moodle Authentication method";
    public static final String ERROR_CODE_AUTENTICATION_METHOD_MOODLE_REMOTE_ERROR = "AUTHENTICATION_METHOD_MOODLE_REMOTE_ERROR";
}
