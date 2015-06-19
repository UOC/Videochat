/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.model.json;

import edu.uoc.model.Chat;
import java.util.List;

/**
 *
 * @author antonibertranbellido
 */
public class JSONResponseVideochat extends JSONResponse {
    private String urlStream;
    private int meetingId;
    private List<Chat> chatMessages;
    public String getUrlStream() {
        return urlStream;
    }
    public void setUrlStream( String c ){
        this.urlStream = c;
    }
    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }
    public int getMeetingId() {
        return this.meetingId;
    }
}
