/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.model;

/**
 *
 * @author antonibertranbellido
 */
public class SearchMeeting {
    
    private String topic;
    private String participants;
    private String start_meeting;
    private String end_meeting;
    private Room room;
    private int room_id;

    /**
     * @return the topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * @param topic the topic to set
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * @return the participants
     */
    public String getParticipants() {
        return participants;
    }

    /**
     * @param participants the participants to set
     */
    public void setParticipants(String participants) {
        this.participants = participants;
    }

    /**
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @param room the room_id to set
     */
    public void setRoom(Room room) {
        this.room = room;
    }
   
    /**
     * @return the room_id
     */
    public int getRoom_Id() {
        return room_id;
    }

    /**
     * @param room_id the room_id to set
     */
    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }


    /**
     * @return the start_meeting
     */
    public String getStart_meeting() {
        return start_meeting;
    }

    /**
     * @param start_meeting the start_meeting to set
     */
    public void setStart_meeting(String start_meeting) {
        this.start_meeting = start_meeting;
    }

    /**
     * @return the end_meeting
     */
    public String getEnd_meeting() {
        return end_meeting;
    }

    /**
     * @param end_meeting the end_meeting to set
     */
    public void setEnd_meeting(String end_meeting) {
        this.end_meeting = end_meeting;
    }
    
}
