/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.bo.impl;

import edu.uoc.bo.UserBo;
import edu.uoc.lti.LTIEnvironment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Diego
 */

@Controller
@RequestMapping("/")
public class UserBoImpl implements UserBo{

    
    
    
    
    @RequestMapping(method = RequestMethod.GET)
    @Override
    public String getUserName(Model model){
      
        LTIEnvironment lti = new LTIEnvironment();
        
        
        model.addAttribute("user", lti.getUserName());
        return "player";
        
        
        
    }
    
}
