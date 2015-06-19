/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.model.json;

import edu.uoc.model.User;

/**
 *
 * @author antonibertranbellido
 */
public class JSONResponseUser extends JSONResponse {
    private User user;
    public User getUser() {
        return user;
    }
    public void setUser( User u ){
        this.user = u;
    }
}
