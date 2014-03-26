package edu.uoc.common.controller;

import edu.uoc.dao.UserDao;
import edu.uoc.lti.LTIEnvironment;
import edu.uoc.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;

@Controller

public class UserController {
    
    
    @Autowired
    private UserDao userDao;
    
@RequestMapping("/player")    
//@RequestMapping(method = RequestMethod.GET)
public ModelAndView handleRequestInternal(HttpServletRequest request,
		HttpServletResponse response)  {
    
//LTIAuthenticator lti = new LTIAuthenticator();
        
        
    ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
                User user = context.getBean(User.class);
		ModelAndView model = new ModelAndView("player");
                
                user.setPassword("");
                user.setUsername(user.getFullname());
                user.setSurname(user.getFullname());
                user.setBlocked((byte)0);
                
		model.addObject("user", user.getFullname());
                userDao.save(user);
 
		return model;        
    }

@RequestMapping("/getUsers")    
    public ModelAndView getAllUsers(HttpServletRequest request,
		HttpServletResponse response){
        List<User> listUsers = userDao.findAll();
        
        ModelAndView model = new ModelAndView("getUsers");
        request.setAttribute("list",listUsers);
        model.addObject("userList", listUsers);
        return model;
    }
    
}