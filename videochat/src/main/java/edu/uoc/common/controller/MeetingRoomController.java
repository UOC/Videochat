/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.common.controller;

import edu.uoc.dao.MeetingRoomDao;
import edu.uoc.dao.UserMeetingDao;
import edu.uoc.model.MeetingRoom;
import edu.uoc.model.User;
import edu.uoc.model.UserMeeting;
import edu.uoc.util.Constants;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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
public class MeetingRoomController {
    
    @Autowired
    private MeetingRoomDao meetingDao;
    @Autowired
    private UserMeetingDao userMeetingDao;
    
    @RequestMapping("/searchMeeting")
    public ModelAndView getMeetingRooms(String courseKey){
        List<MeetingRoom> listMR = meetingDao.getMeetingRoomsByCourseKey("586");
        ModelAndView model = new ModelAndView("searchMeeting");
        model.addObject("listMR", listMR);
        return model;
    }
    
    @RequestMapping(value="/currentUserAcceptConnection", method=RequestMethod.GET,   
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)  
    @ResponseBody  
    public boolean registerUserMeeting(HttpSession session) {
        boolean registerOk = false;
        UserMeeting userMeeting = (UserMeeting) session.getAttribute(Constants.USER_METTING_SESSION);
        User user = (User) session.getAttribute(Constants.USER_SESSION);
        try {
            if (user!=null && userMeeting!=null) {
                userMeeting.setAccessConfirmed((byte)1);
                this.userMeetingDao.save(userMeeting);
                registerOk = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return registerOk;
    }
}
