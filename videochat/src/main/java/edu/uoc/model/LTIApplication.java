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

@Table(name = "vc_ltiapplication")

public class LTIApplication {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "lti_application_id", length = 11)
    private int id;
    
    @Column(name="lti_application_tool_url",length=150)
    private String toolURL;
    
    @Column(name="lti_application_name",length=150)
    private String name;    
    
    @Column(name="lti_application_description",length=150)
    private String description;
    
    @Column(name="lti_application_resource_key",length=150)
    private String resourceKey;
    
    @Column(name="lti_application_prefer_height",length=50)
    private String preferHeight;
    
    @Column(name="lti_application_send_name",length=1)
    private int sendName;
    
    @Column(name="lti_application_send_email_ddr",length=1)
    private int sendEmailddr;
    
    @Column(name="lti_application_accept_grades",length=1)
    private int acceptGrades;
    
    @Column(name="lti_application_allow_roster",length=1)
    private int allowRoster;
    
    @Column(name="lti_application_allow_setting",length=1)
    private int allowSetting;
    
    @Column(name="lti_application_custom_parameters",length=150)
    private String customParameters;
    
    @Column(name="lti_application_allow_instructor_custom",length=1)
    private int allowInstructorCustom;
    
    @Column(name="lti_application_organization_id",length=150)
    private String organizationId;
    
    @Column(name="lti_application_organization_url",length=1)
    private String organizationURL;
    
    @Column(name="lti_application_launching_pop_up",length=1)

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
