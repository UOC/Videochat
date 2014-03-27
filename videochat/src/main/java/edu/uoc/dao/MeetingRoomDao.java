/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.dao;

import edu.uoc.model.MeetingRoom;
import java.util.List;

/**
 *
 * @author Francesc Fernandez
 */
public interface MeetingRoomDao {
    
    void save(MeetingRoom meetingRoom);
    
    void update(MeetingRoom meetingRoom);
    
    void delete(MeetingRoom meetingRoom);
   
    List<MeetingRoom> getMeetingRoomsByCourseKey(String courseKey);
}

