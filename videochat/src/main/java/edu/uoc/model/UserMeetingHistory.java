/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.model;

import edu.uoc.util.Constants;
import java.sql.Timestamp;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import org.springframework.stereotype.Component;

/**
 *
 * @author Diego
 */

@Entity

@Component
@Table(name="vc_usermeeting_history")
public class UserMeetingHistory implements java.io.Serializable{
    
      
    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Column(name="id",length=11) 
    private int id;
    
    @Column(name="user_id",length=11) 
    private int user_id;

    @Column(name="meeting_id",length=11) 
    private int meeting_id;
    
    @Column(name="usermeeting_created")

    private Timestamp created;
    
    @Column(name="usermeeting_deleted")

    private Timestamp deleted;

    public UserMeetingHistory() {
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUser_id() {
        return user_id;
    }
    
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMeeting_id() {
        return meeting_id;
    }
    
    public void setMeeting_id(int meeting_id) {
        this.meeting_id = meeting_id;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
    
    public Timestamp getDeleted() {
        return deleted;
    }

    public void setDeleted(Timestamp deleted) {
        this.deleted = deleted;
    }
}