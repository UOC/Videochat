/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.model.json;

/**
 *
 * @author antonibertranbellido
 */
public class JSONResponseSetting extends JSONResponse {
    private String streamServerUrl;
    private String streamServerPort;
    private String streamServerApplication;
    private String streamServerVODApplication;
    private String streamPublisherUsername;
    private String streamPublisherPassword;
    public String getStreamServerUrl() {
        return streamServerUrl;
    }
    public void setStreamServerUrl( String streamServerUrl ){
        this.streamServerUrl = streamServerUrl;
    }
    public String getStreamServerPort() {
        return streamServerPort;
    }
    public void setStreamServerPort( String streamServerPort ){
        this.streamServerPort = streamServerPort;
    }

    /**
     * @return the streamPublisherUsername
     */
    public String getStreamPublisherUsername() {
        return streamPublisherUsername;
    }

    /**
     * @param streamPublisherUsername the streamPublisherUsername to set
     */
    public void setStreamPublisherUsername(String streamPublisherUsername) {
        this.streamPublisherUsername = streamPublisherUsername;
    }

    /**
     * @return the streamPublisherPassword
     */
    public String getStreamPublisherPassword() {
        return streamPublisherPassword;
    }

    /**
     * @param streamPublisherPassword the streamPublisherPassword to set
     */
    public void setStreamPublisherPassword(String streamPublisherPassword) {
        this.streamPublisherPassword = streamPublisherPassword;
    }

    /**
     * @return the streamServerApplication
     */
    public String getStreamServerApplication() {
        return streamServerApplication;
    }

    /**
     * @param streamServerApplication the streamServerApplication to set
     */
    public void setStreamServerApplication(String streamServerApplication) {
        this.streamServerApplication = streamServerApplication;
    }
    /**
     * @return the streamServerVODApplication
     */
    public String getStreamServerVODApplication() {
        return streamServerVODApplication;
    }

    /**
     * @param streamServerVODApplication the streamServerApplication to set
     */
    public void setStreamServerVODApplication(String streamServerVODApplication) {
        this.streamServerVODApplication = streamServerVODApplication;
    }
}
