/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.dao;

import edu.uoc.model.MeetingRoom;
import edu.uoc.model.UserMeeting;
import edu.uoc.model.UserMeetingHistory;
import edu.uoc.model.UserMeetingId;
import java.util.List;

/**
 *
 * @author Antoni Bertran
 */
public interface UserMeetingHistoryDao {
    
    void save (UserMeetingHistory userMeeting);
    
    public UserMeetingHistory findUserMeetingByPK(UserMeetingId umID);
    
}
