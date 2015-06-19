/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.model;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author antonibertranbellido
 */
public class MeetingRoomExtended extends MeetingRoom {

    public MeetingRoomExtended(MeetingRoom meeting) {
        this.setDescription(meeting.getDescription());
        this.setEnd_meeting(meeting.getEnd_meeting());
        this.setEnd_record(meeting.getEnd_record());
        this.setFinished(meeting.getFinished());
        this.setId(meeting.getId());
        this.setId_room(meeting.getId_room());
        this.setNumber_participants(meeting.getNumber_participants());
        this.setPath(meeting.getPath());
        this.setRecorded(meeting.getRecorded());
        this.setStart_meeting(meeting.getStart_meeting());
        this.setStart_record(meeting.getStart_record());
        this.setTopic(meeting.getTopic());
        this.setUserMeeting(meeting.getUserMeeting());        
    }

    private String start_meeting_txt;

    private String end_meeting_txt;

    private String start_record_txt;

    private String end_record_txt;

    private String start_meeting_date_txt;

    private String end_meeting_date_txt;

    private String start_record_date_txt;

    private String end_record_date_txt;
    
    private String start_meeting_time_txt;

    private String end_meeting_time_txt;

    private String start_record_time_txt;

    private String end_record_time_txt;
    
    private String total_time_txt;
    
    private List<UserMeeting> participants;
    
    private List<Chat> chat;
    
    /**
     * @return the start_meeting_txt
     */
    public String getStart_meeting_txt() {
        return start_meeting_txt;
    }

    /**
     * @param start_meeting_txt the start_meeting_txt to set
     */
    public void setStart_meeting_txt(String start_meeting_txt) {
        this.start_meeting_txt = start_meeting_txt;
    }

    /**
     * @return the end_meeting_txt
     */
    public String getEnd_meeting_txt() {
        return end_meeting_txt;
    }

    /**
     * @param end_meeting_txt the end_meeting_txt to set
     */
    public void setEnd_meeting_txt(String end_meeting_txt) {
        this.end_meeting_txt = end_meeting_txt;
    }

    /**
     * @return the start_record_txt
     */
    public String getStart_record_txt() {
        return start_record_txt;
    }

    /**
     * @param start_record_txt the start_record_txt to set
     */
    public void setStart_record_txt(String start_record_txt) {
        this.start_record_txt = start_record_txt;
    }

    /**
     * @return the end_record_txt
     */
    public String getEnd_record_txt() {
        return end_record_txt;
    }

    /**
     * @param end_record_txt the end_record_txt to set
     */
    public void setEnd_record_txt(String end_record_txt) {
        this.end_record_txt = end_record_txt;
    }

    /**
     * @return the total_time_txt
     */
    public String getTotal_time_txt() {
        return total_time_txt;
    }

    /**
     * @param total_time_txt the total_time_txt to set
     */
    public void setTotal_time_txt(String total_time_txt) {
        this.total_time_txt = total_time_txt;
    }

    /**
     * @return the participants
     */
    public List<UserMeeting> getParticipants() {
        return participants;
    }

    /**
     * @param participants the participants to set
     */
    public void setParticipants(List<UserMeeting> participants) {
        this.participants = participants;
    }
    
    /**
     * @return the chat
     */
    public List<Chat> getChat() {
        return chat;
    }

    /**
     * @param chat the participants to set
     */
    public void setChat(List<Chat> chat) {
        this.chat = chat;
    }

    /**
     * @return the start_meeting_date_txt
     */
    public String getStart_meeting_date_txt() {
        return start_meeting_date_txt;
    }

    /**
     * @param start_meeting_date_txt the start_meeting_date_txt to set
     */
    public void setStart_meeting_date_txt(String start_meeting_date_txt) {
        this.start_meeting_date_txt = start_meeting_date_txt;
    }

    /**
     * @return the end_meeting_date_txt
     */
    public String getEnd_meeting_date_txt() {
        return end_meeting_date_txt;
    }

    /**
     * @param end_meeting_date_txt the end_meeting_date_txt to set
     */
    public void setEnd_meeting_date_txt(String end_meeting_date_txt) {
        this.end_meeting_date_txt = end_meeting_date_txt;
    }

    /**
     * @return the start_record_date_txt
     */
    public String getStart_record_date_txt() {
        return start_record_date_txt;
    }

    /**
     * @param start_record_date_txt the start_record_date_txt to set
     */
    public void setStart_record_date_txt(String start_record_date_txt) {
        this.start_record_date_txt = start_record_date_txt;
    }

    /**
     * @return the end_record_date_txt
     */
    public String getEnd_record_date_txt() {
        return end_record_date_txt;
    }

    /**
     * @param end_record_date_txt the end_record_date_txt to set
     */
    public void setEnd_record_date_txt(String end_record_date_txt) {
        this.end_record_date_txt = end_record_date_txt;
    }

    /**
     * @return the start_meeting_time_txt
     */
    public String getStart_meeting_time_txt() {
        return start_meeting_time_txt;
    }

    /**
     * @param start_meeting_time_txt the start_meeting_time_txt to set
     */
    public void setStart_meeting_time_txt(String start_meeting_time_txt) {
        this.start_meeting_time_txt = start_meeting_time_txt;
    }

    /**
     * @return the end_meeting_time_txt
     */
    public String getEnd_meeting_time_txt() {
        return end_meeting_time_txt;
    }

    /**
     * @param end_meeting_time_txt the end_meeting_time_txt to set
     */
    public void setEnd_meeting_time_txt(String end_meeting_time_txt) {
        this.end_meeting_time_txt = end_meeting_time_txt;
    }

    /**
     * @return the start_record_time_txt
     */
    public String getStart_record_time_txt() {
        return start_record_time_txt;
    }

    /**
     * @param start_record_time_txt the start_record_time_txt to set
     */
    public void setStart_record_time_txt(String start_record_time_txt) {
        this.start_record_time_txt = start_record_time_txt;
    }

    /**
     * @return the end_record_time_txt
     */
    public String getEnd_record_time_txt() {
        return end_record_time_txt;
    }

    /**
     * @param end_record_time_txt the end_record_time_txt to set
     */
    public void setEnd_record_time_txt(String end_record_time_txt) {
        this.end_record_time_txt = end_record_time_txt;
    }
    
    /**
     * Returns if session contains a user
     * @param u
     * @return 
     */
    public boolean contains(User u) {
        boolean contains = false;
        for (Iterator<UserMeeting> iterator = participants.iterator(); iterator.hasNext();) {
            UserMeeting next = iterator.next();
            contains = next.getPk().getUser().getId()==u.getId();
            if (contains) {
                break;
            }
        }
        return contains;
    }
}
