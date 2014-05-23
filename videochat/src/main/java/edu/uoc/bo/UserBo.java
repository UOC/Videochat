/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.bo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Diego
 */
public interface UserBo {
 
    
    
    
    
    public ModelAndView handleRequestInternal(HttpServletRequest request,
		HttpServletResponse response);
        
        
        
        
        
    
    
    
    
    
    
}
