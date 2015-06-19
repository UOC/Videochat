/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.model.json;

import edu.uoc.model.MeetingRoom;

/**
 *
 * @author antonibertranbellido
 */
public class JSONResponseMeeting extends JSONResponse {
    private MeetingRoom meeting;
    public MeetingRoom getMeeting() {
        return meeting;
    }
    public void setMeeting( MeetingRoom meeting ){
        this.meeting = meeting;
    }
}
