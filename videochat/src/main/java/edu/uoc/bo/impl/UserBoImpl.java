/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.bo.impl;

import edu.uoc.bo.UserBo;
import edu.uoc.lti.LTIEnvironment;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Diego
 */

@Controller
@Scope("session")
@RequestMapping("/videochat/player")
public class UserBoImpl implements UserBo{

    
    
    
    
    @RequestMapping(method = RequestMethod.GET)
    @Override
    public ModelAndView handleRequestInternal(HttpServletRequest request,
		HttpServletResponse response)  {
        LTIEnvironment lti = new LTIEnvironment();
   
        
        
		ModelAndView model = new ModelAndView("player");
		model.addObject("user", "Diego");
 
		return model;
         
		
 
		    
        
        
    }
    
}
