/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.videochat;

import edu.uoc.lti.LTIEnvironment;
import edu.uoc.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Diego
 */
@WebServlet(name = "LTIAuthenticator", urlPatterns = {"/LTIAuthenticator"})
public class LTIAuthenticator extends HttpServlet {

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

        request.setCharacterEncoding("UTF-8");
        //1. Check if LTI call is valid
        LTIEnvironment LTIEnvironment;
        LTIEnvironment = new LTIEnvironment();
        if (LTIEnvironment.is_lti_request(request)) {

            LTIEnvironment.parseRequest(request);
            if (LTIEnvironment.isAuthenticated()) {

                //2. Get the values of user and course  	 
                String username = LTIEnvironment.getUserName();
	        	//TODO mirar si cal posar
				/*if (username.startsWith(LTIEnvironment.getResourcekey()+":")) {
                 username = username.substring((LTIEnvironment.getResourcekey()+":").length());
                 }*/
                String full_name = LTIEnvironment.getFullName();
                String email = LTIEnvironment.getEmail();
                String user_image = LTIEnvironment.getUser_image();

                //3. Get the role
                boolean is_instructor = LTIEnvironment.isInstructor();
                boolean is_course_autz = LTIEnvironment.isCourseAuthorized();

                //4. Get course data
                String course_key = LTIEnvironment.getCourseKey();
                String course_label = LTIEnvironment.getCourseName();
                String resource_key = LTIEnvironment.getResourceKey();
                String resource_label = LTIEnvironment.getResourceTitle();

                //5. Get the locale
                String locale = LTIEnvironment.getLocale();

                boolean redirectToPlayer = LTIEnvironment.getCustomParameter("player", request)!=null;
				//6. If you need get custom parameters you can do, is not needed to add custom_ prefix to property
                //String custom_param 	= LTIEnvironment.getCustomParameter("property", request);
				//In this demo show the values received insted of that you have to
                //continue with next steps to integrate with your application
                out.println("<p><b>Username:</b> " + username + "</p>");
                out.println("<p><b>Full name:</b> " + full_name + "</p>");
                out.println("<p><b>Email:</b> " + email + "</p>");
                out.println("<p><b>User image:</b> " + user_image + "</p>");
                out.println("<p><b>Course key:</b> " + course_key + "</p>");
                out.println("<p><b>Course label:</b> " + course_label + "</p>");
                out.println("<p><b>Resource key:</b> " + resource_key + "</p>");
                out.println("<p><b>Resource label:</b> " + resource_label + "</p>");

                out.println("<p>" + full_name + " is <b>" + (is_instructor ? "Instructor" : is_course_autz ? "Student" : "Other or guest") + "</b></p>");
                
                out.println("<p><b>redirectToPlayer:</b> " + redirectToPlayer + "</p>");

                out.println("<p><b>Local:</b> " + locale + "</p>");
                
                
                

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
            } else {

                Exception lastException = LTIEnvironment.getLastException();
                //Retornar excepcio
                out.println("Error LTI authentication " + (lastException != null ? lastException.getMessage() : ""));

            }
        } else {
            out.println("Error LTI authentication is not a valid LTI request");
        }   
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

    
    public User getUser(LTIEnvironment LTIEnvironment){
        
       User user= new User();
       
       
       user.setSurname(LTIEnvironment.getFullName());
               
               
        return user;          
        
        
        
    }
}
