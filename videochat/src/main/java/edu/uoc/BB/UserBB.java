/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.BB;

import edu.uoc.bo.UserBo;
import javax.faces.bean.ManagedBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Diego
 */

@Component
@ManagedBean(name="UserBB")
@Scope("session")
public class UserBB {
    
    @Autowired
    UserBo userBo;
    
    
    
    

    public void setUserBo(UserBo userBo) {
        this.userBo = userBo;
    }
 
    
    
    
    
    
    
    
    
    
    
}
