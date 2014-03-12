/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.model;

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
@Table(name = "VC_LTIAPPLICATION")
public class LTIApplication {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "LTI_APPLICATION_ID", length = 11)
    private int id;
    
    @Column(name="LTI_APPLICATION_TOOL_URL",length=150)
    private String toolURL;
    
    @Column(name="LTI_APPLICATION_NAME",length=150)
    private String name;    
    
    @Column(name="LTI_APPLICATION_DESCRIPTION",length=150)
    private String description;
    
    @Column(name="LTI_APPLICATION_RESOURCE_KEY",length=150)
    private String resourceKey;
    
    @Column(name="LTI_APPLICATION_PREFER_HEIGHT",length=50)
    private String preferHeight;
    
    @Column(name="LTI_APPLICATION_SEND_NAME",length=1)
    private int sendName;
    
    @Column(name="LTI_APPLICATION_SEND_EMAIL_DDR",length=1)
    private int sendEmailddr;
    
    @Column(name="LTI_APPLICATION_ACCEPT_GRADES",length=1)
    private int acceptGrades;
    
    @Column(name="LTI_APPLICATION_ALLOW_ROSTER",length=1)
    private int allowRoster;
    
    @Column(name="LTI_APPLICATION_ALLOW_SETTING",length=1)
    private int allowSetting;
    
    @Column(name="LTI_APPLICATION_CUSTOM_PARAMETERS",length=150)
    private String customParameters;
    
    @Column(name="LTI_APPLICATION_ALLOW_INSTRUCTOR_CUSTOM",length=1)
    private int allowInstructorCustom;
    
    @Column(name="LTI_APPLICATION_ORGANIZATION_ID",length=150)
    private String organizationId;
    
    @Column(name="LTI_APPLICATION_ORGANIZATION_URL",length=1)
    private String organizationURL;
    
    @Column(name="LTI_APPLICATION_LAUNCHING_POP_UP",length=1)
    private int launchingPopup;

    public LTIApplication(int id, String toolURL, String name, String description, String resourceKey, String preferHeight, int sendName, int sendEmailddr, int acceptGrades, int allowRoster, int allowSetting, String customParameters, int allowInstructorCustom, String organizationId, String organizationURL, int launchingPopup) {
        this.id = id;
        this.toolURL = toolURL;
        this.name = name;
        this.description = description;
        this.resourceKey = resourceKey;
        this.preferHeight = preferHeight;
        this.sendName = sendName;
        this.sendEmailddr = sendEmailddr;
        this.acceptGrades = acceptGrades;
        this.allowRoster = allowRoster;
        this.allowSetting = allowSetting;
        this.customParameters = customParameters;
        this.allowInstructorCustom = allowInstructorCustom;
        this.organizationId = organizationId;
        this.organizationURL = organizationURL;
        this.launchingPopup = launchingPopup;
    }

    public LTIApplication() {
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

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getPreferHeight() {
        return preferHeight;
    }

    public void setPreferHeight(String preferHeight) {
        this.preferHeight = preferHeight;
    }

    public int getSendName() {
        return sendName;
    }

    public void setSendName(int sendName) {
        this.sendName = sendName;
    }

    public int getSendEmailddr() {
        return sendEmailddr;
    }

    public void setSendEmailddr(int sendEmailddr) {
        this.sendEmailddr = sendEmailddr;
    }

    public int getAcceptGrades() {
        return acceptGrades;
    }

    public void setAcceptGrades(int acceptGrades) {
        this.acceptGrades = acceptGrades;
    }

    public int getAllowRoster() {
        return allowRoster;
    }

    public void setAllowRoster(int allowRoster) {
        this.allowRoster = allowRoster;
    }

    public int getAllowSetting() {
        return allowSetting;
    }

    public void setAllowSetting(int allowSetting) {
        this.allowSetting = allowSetting;
    }

    public String getCustomParameters() {
        return customParameters;
    }

    public void setCustomParameters(String customParameters) {
        this.customParameters = customParameters;
    }

    public int getAllowInstructorCustom() {
        return allowInstructorCustom;
    }

    public void setAllowInstructorCustom(int allowInstructorCustom) {
        this.allowInstructorCustom = allowInstructorCustom;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationURL() {
        return organizationURL;
    }

    public void setOrganizationURL(String organizationURL) {
        this.organizationURL = organizationURL;
    }

    public int getLaunchingPopup() {
        return launchingPopup;
    }

    public void setLaunchingPopup(int launchingPopup) {
        this.launchingPopup = launchingPopup;
    }
    
    
    
    
    
    

}
