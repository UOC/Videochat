/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
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
    
    @Column(name="usermeeting_extra_role")
    private String extra_role;

    @Column(name="usermeeting_user_agent")
    private String user_agent;

    @Column(name="usermeeting_platform")
    private String platform;

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
    
    public void setExtraRole(String usermeeting_extra_role){
        this.extra_role = usermeeting_extra_role;
    }
    
    public String getExtraRole(){
        return this.extra_role;
    }
    
    /**
     * Set the user agent
     * @param user_agent 
     */
    public void setUserAgent(String user_agent) {
        this.user_agent = user_agent;
    }
    /**
     * Gets the user antet
     * @return 
     */
    public String getUserAgent() {
        return this.user_agent;
    }
    
    
    /**
     * Set the platform
     * @param platform
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    /**
     * Gets the platform
     * @return 
     */
    public String getPlatform() {
        return this.platform;
    }
}