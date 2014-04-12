/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.videochat;

import edu.uoc.dao.CourseDao;
import edu.uoc.dao.RoomDao;
import edu.uoc.dao.UserCourseDao;
import edu.uoc.dao.UserDao;
import edu.uoc.util.Constants;

import edu.uoc.lti.LTIEnvironment;
import edu.uoc.model.Course;
import edu.uoc.model.Room;
import edu.uoc.model.User;
import edu.uoc.model.UserCourse;
import edu.uoc.model.UserCourseId;
import edu.uoc.util.Util;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Diego
 */
@WebServlet(name = "LTIAuthenticator", urlPatterns = {"/LTIAuthenticator"})
public class LTIAuthenticator extends HttpServlet {
//get log4j handler

    private static final Logger logger = Logger.getLogger(LTIAuthenticator.class);

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("GET request is not allowed");
    }

    /**
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response) This is a template of LTI Provider
     * @author
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String nextPage = "error";
        String params = "";
        HttpSession session = request.getSession();
        //ModelAndView model = new ModelAndView();
        session.setAttribute(Constants.USER_SESSION, null);
        session.setAttribute(Constants.COURSE_SESSION, null);
        session.setAttribute(Constants.MEETING_SESSION, null);
        session.setAttribute(Constants.USER_METTING_SESSION, null);
        session.setAttribute(Constants.USER_LANGUAGE, null);

        try {
            ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
            //ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
            UserDao userDao = (UserDao) context.getBean("UserDao");
            CourseDao courseDao = context.getBean(CourseDao.class);
            UserCourseDao userCourseDao = context.getBean(UserCourseDao.class);
            RoomDao roomDao = context.getBean(RoomDao.class);

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
                     }*/
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
                    locale = locale.substring(0,2);
                    session.setAttribute(Constants.USER_LANGUAGE, locale);
                    params = "?"+Constants.PARAM_SPRING_LANG+"="+locale;
                    nextPage = redirectToPlayer ? "searchMeeting" : "videochat";

                } else {

                    Exception lastException = LTIEnvironment.getLastException();
                    logger.error("Error authenticating user LTI Exception " + lastException);
                    //Retornar excepcio
                    nextPage = "errorLTI";
                }
            } else {
                logger.warn("Error authenticating user LTI is not LTI Request ");
                nextPage = "errorNoLTIRequest";
            }
        } catch (Exception e) {
            logger.error("Error authenticating user ", e);

        }
        response.sendRedirect(request.getContextPath()+"/" + nextPage + ".htm"+params);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
