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

/**
 *
 * @author Diego
 */


@Entity

@Table(name = "vc_remote_application")

public class RemoteApplication {
    
    
    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Column(name="vc_remote_id",unique=true,length=11) 
    private int id;
 
    
    @Column(name="vc_remote_tool_url",length=11)
    private String toolURL;
    
    @Column(name="vc_remote_name",length=11)
    private String name;
    
    @Column(name="vc_remote_description",length=11)
    private String description;
    
    @Column(name="vc_remote_launchin_pop_up",length=11)
    private int launchinPopup;
    
    @Column(name="vc_remote_debug_mode",length=11)
    private int debugMode;
    
    @Column(name="vc_remote_registered",length=11)
    private Timestamp registered;

    public RemoteApplication() {
    }

    public RemoteApplication(int id, String toolURL, String name, String description, int launchinPopup, int debugMode, Timestamp registered) {
        this.id = id;
        this.toolURL = toolURL;
        this.name = name;
        this.description = description;
        this.launchinPopup = launchinPopup;
        this.debugMode = debugMode;
        this.registered = registered;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToolURL() {
        return toolURL;
    }

    public void setToolURL(String toolURL) {
        this.toolURL = toolURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLaunchinPopup() {
        return launchinPopup;
    }

    public void setLaunchinPopup(int launchinPopup) {
        this.launchinPopup = launchinPopup;
    }

    public int getDebugMode() {
        return debugMode;
    }

    public void setDebugMode(int debugMode) {
        this.debugMode = debugMode;
    }

    public Timestamp getRegistered() {
        return registered;
    }

    public void setRegistered(Timestamp registered) {
        this.registered = registered;
    }
    
    
    
    
    
}
