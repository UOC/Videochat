/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.dao;

import edu.uoc.model.MeetingRoom;
import edu.uoc.model.UserMeeting;
import edu.uoc.model.UserMeetingId;
import java.util.List;

/**
 *
 * @author Francesc Fernandez
 */
public interface UserMeetingDao {
    
    void save (UserMeeting userMeeting);
    
    void delete (UserMeeting userMeeting);
    
    public UserMeeting findUserMeetingByPK(UserMeetingId umID);
    
    /**
     * Allow search users of meeting, but gets the user_id to allow avoid one user.
     * This method is used in:
     * <ul>
     * <li>videochat.htm to get other participants, then the user_id is the current_user</li>
     * <li>player.htm to get all participants, then the user_id 0</li>
     * </ul>
     * @param meeting
     * @param user_id
     * @param onlyConfirmed
     * @return 
     */
    public List<UserMeeting> findUsersByMeetingId(MeetingRoom meeting, int user_id, boolean onlyConfirmed);
}
