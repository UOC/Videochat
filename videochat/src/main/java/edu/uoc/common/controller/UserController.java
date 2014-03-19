package edu.uoc.common.controller;

import edu.uoc.lti.LTIApplication;
import edu.uoc.lti.LTIEnvironment;
import edu.uoc.videochat.LTIAuthenticator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/player")
public class UserController {
@RequestMapping(method = RequestMethod.GET)
    


public ModelAndView handleRequestInternal(HttpServletRequest request,
		HttpServletResponse response)  {
        
   
        //LTIAuthenticator lti = new LTIAuthenticator();
        LTIEnvironment lti = new LTIEnvironment();
        
		ModelAndView model = new ModelAndView("player");
		model.addObject("user", lti.getFullName());
 
		return model;
         
		
 
		    
        
        
    }
    
}