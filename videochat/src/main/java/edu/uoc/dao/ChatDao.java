/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.dao;

import edu.uoc.model.Chat;
import edu.uoc.model.MeetingRoom;
import java.util.List;

/**
 *
 * @author Francesc Fernandez
 */
public interface ChatDao {
    
    void save (Chat chat);
    
    void delete (Chat chat);
    
    public List<Chat> findByMeetingId(MeetingRoom umID);
    
}
