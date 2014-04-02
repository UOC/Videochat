/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.model;

import edu.uoc.util.Constants;
import edu.uoc.util.Util;
import java.sql.Timestamp;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.stereotype.Component;

/**
 *
 * @author Diego
 */
@Component
@Entity
@Table(name="vc_chat")
@AssociationOverrides({
		@AssociationOverride(name = "pk.user", 
			joinColumns = @JoinColumn(name = "chat_user_id")),
		@AssociationOverride(name = "pk.meeting", 
			joinColumns = @JoinColumn(name = "chat_meeting_room_id")) })

public class Chat implements java.io.Serializable{
    
    
    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Column(name="chat_id",length=11)
    private int id;
    
    @ManyToOne(targetEntity = MeetingRoom.class)
    @JoinColumn(name = "chat_meeting_room_id")
    private MeetingRoom meeting_room;
    
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "chat_user_id")
    private User user;
    
    @Column(name = "chat_sent_time")
    private Timestamp chat_sent_time;

    @Column(name="chat_message",length=255)
    private String chat_message;

  

    public Chat() {
    }

    public Chat(int id, MeetingRoom meeting_room, User user, Timestamp chat_sent_time, String chat_message) {
        this.id = id;
        this.meeting_room = meeting_room;
        this.user = user;
        this.chat_sent_time = chat_sent_time;
        this.chat_message = chat_message;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the meeting_room
     */
    public MeetingRoom getMeeting_room() {
        return meeting_room;
    }

    /**
     * @param meeting_room the meeting_room to set
     */
    public void setMeeting_room(MeetingRoom meeting_room) {
        this.meeting_room = meeting_room;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the chat_sent_time
     */
    public Timestamp getChat_sent_time() {
        return chat_sent_time;
    }

    /**
     * @param chat_sent_time the chat_sent_time to set
     */
    public void setChat_sent_time(Timestamp chat_sent_time) {
        this.chat_sent_time = chat_sent_time;
    }

    /**
     * @return the chat_message
     */
    public String getChat_message() {
        return chat_message;
    }

    /**
     * @param chat_message the chat_message to set
     */
    public void setChat_message(String chat_message) {
        this.chat_message = chat_message;
    }
    
    public String getChat_sent_time_txt() {
        return Util.getTimestampFormatted(chat_sent_time, Constants.FORMAT_TIME);
    }
    
    
    
    
    
    
}
