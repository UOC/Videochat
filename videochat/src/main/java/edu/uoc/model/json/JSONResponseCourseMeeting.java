/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.model.json;

import edu.uoc.model.MeetingRoomExtended;
import java.util.List;

/**
 *
 * @author antonibertranbellido
 */
public class JSONResponseCourseMeeting extends JSONResponse {
    private List<MeetingRoomExtended> listMeeting;
    public List<MeetingRoomExtended> getListMeeting() {
        return listMeeting;
    }
    public void setListMeetings( List<MeetingRoomExtended> list ){
        this.listMeeting = list;
    }
}
