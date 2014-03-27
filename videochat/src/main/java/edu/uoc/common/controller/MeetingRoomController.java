/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.common.controller;

import edu.uoc.dao.MeetingRoomDao;
import edu.uoc.model.MeetingRoom;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Francesc Fernandez
 */

@Controller

public class MeetingRoomController {
    
    @Autowired
    private MeetingRoomDao meetingDao;
    
    @RequestMapping("/searchMeeting")
    public ModelAndView getMeetingRooms(String courseKey){
        List<MeetingRoom> listMR = meetingDao.getMeetingRoomsByCourseKey("586");
        ModelAndView model = new ModelAndView("searchMeeting");
        model.addObject("listMR", listMR);
        return model;
    }
}
