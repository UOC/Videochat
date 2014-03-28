/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.model;

import java.sql.Timestamp;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import org.springframework.stereotype.Component;

/**
 *
 * @author Diego
 */

@Entity

@Component
@Table(name="vc_usermeeting")
@AssociationOverrides({
		@AssociationOverride(name = "pk.user", 
			joinColumns = @JoinColumn(name = "user_id")),
		@AssociationOverride(name = "pk.meeting", 
			joinColumns = @JoinColumn(name = "meeting_id")) })


public class UserMeeting implements java.io.Serializable{
    
      
    
    @EmbeddedId
    private UserMeetingId pk;
    
    @Column(name="usermeeting_created")

    Timestamp created;

    public UserMeeting() {
    }

    public UserMeeting(UserMeetingId pk, Timestamp created) {
        this.pk = pk;
        this.created = created;
    }

    public UserMeetingId getPk() {
        return pk;
    }

    public void setPk(UserMeetingId pk) {
        this.pk = pk;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
    
    
    
}
