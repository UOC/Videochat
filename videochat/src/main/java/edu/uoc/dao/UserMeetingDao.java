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
     * @param get_users_from_history_if_not_found when you want to play session then tries to get from table vc_usermeeting and if not found then get from vc_usermeeting_history
     * @param number_user_to_show if get_users_from_history_if_not_found has to try to get the number of users 
     * @return 
     */
    public List<UserMeeting> findUsersByMeetingId(MeetingRoom meeting, int user_id, boolean onlyConfirmed, boolean get_users_from_history_if_not_found, int number_user_to_show);
    
    public List<UserMeeting> findUsersByMeetingId(MeetingRoom meeting, boolean only_accepted);
    
    /**
     * Return the total number of participants
     * @param meeting
     * @return 
     */
    public int countNumberParticipants(MeetingRoom meeting);
    
    public List<UserMeeting> findUserByMeetingId(MeetingRoom meeting, int user_id, boolean onlyConfimed);
}
