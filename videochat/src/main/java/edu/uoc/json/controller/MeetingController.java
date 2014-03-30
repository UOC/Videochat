/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.json.controller;

import edu.uoc.dao.UserMeetingDao;
import edu.uoc.model.JSONResponse;
import edu.uoc.model.User;
import edu.uoc.model.UserMeeting;
import edu.uoc.util.Constants;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 *
 * @author antonibertranbellido
 */
@Controller
@RequestMapping("/meeting")
public class MeetingController {
    
    @Autowired
    private UserMeetingDao userMeetingDao;
    
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody JSONResponse currentUserAcceptConnection(HttpSession session) {
        JSONResponse response = new JSONResponse();
        UserMeeting userMeeting = (UserMeeting) session.getAttribute(Constants.USER_METTING_SESSION);
        User user = (User) session.getAttribute(Constants.USER_SESSION);
        try {
            if (user!=null && userMeeting!=null) {
                userMeeting.setAccessConfirmed((byte)1);
                this.userMeetingDao.save(userMeeting);
                response.setOk(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return response;
    }

}
