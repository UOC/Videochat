/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.dao;

import edu.uoc.model.UserMeeting;
import edu.uoc.model.UserMeetingId;

/**
 *
 * @author Francesc Fernandez
 */
public interface UserMeetingDao {
    
    void save (UserMeeting userMeeting);
    
    void delete (UserMeeting userMeeting);
    
    public UserMeeting findUserMeetingByPK(UserMeetingId umID);
}
