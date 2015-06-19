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

    private Timestamp created; 

    @Column(name="usermeeting_stream_key")

    private String stream_key;

    @Column(name="usermeeting_access_confirmed")

    private byte access_confirmed;
    
    @Column(name="usermeeting_extra_role")
    private String extra_role;

    @Column(name="usermeeting_user_agent")
    private String user_agent;

    @Column(name="usermeeting_platform")
    private String platform;

    
    public UserMeeting() {
    }

    public UserMeeting(UserMeetingId pk, Timestamp created, String stream_key, String extra_role) {
        this.pk = pk;
        this.created = created;
        this.stream_key = stream_key;
        this.extra_role = extra_role;
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
    
    public String getStreamKey() {
        return Constants.WOWZA_RECORD_FOLDER+"/"+stream_key.replaceAll("_","/");
    }

    public String getStreamKeyRecorded() {
        String stream_file = getStreamKey()+Constants.WOWZA_EXTENSION_FILE;
        if (stream_file.endsWith("/.mp4")) {
            stream_file = stream_file.replace("/.mp4",".mp4");
        }
        return stream_file;
    }

    public void setStreamKey(String stream_key) {
        this.stream_key = stream_key;
    }
    
    public byte getAccessConfirmed() {
        return access_confirmed;
    }
    
    public void setAccessConfirmed(byte access_confirmed) {
        this.access_confirmed = access_confirmed;
    }

    /**
     * @return the extra_role
     */
    public String getExtra_role() {
        return extra_role;
    }

    /**
     * @param extra_role the extra_role to set
     */
    public void setExtra_role(String extra_role) {
        this.extra_role = extra_role;
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
