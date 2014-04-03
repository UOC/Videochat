/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.model;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.stereotype.Component;

/**
 *
 * @author Diego
 */
@Component
@Entity
@Table(name = "vc_meeting")

public class MeetingRoom implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "meeting_room_id", length = 11)
    private int id;

    @ManyToOne(targetEntity = Room.class)
    @JoinColumn(name = "room_id")
    private Room id_room;

    @Column(name = "meeting_room_topic", length = 255)
    private String topic;
    
    @Column(name = "meeting_room_description", length = 255)
    private String description;

    @Column(name = "meeting_room_path", length = 255)
    private String path;

    @Column(name = "meeting_room_number_participants", length = 2)
    private String number_participants;

    @Column(name = "meeting_room_finished")
    private byte finished;

    @Column(name = "meeting_room_start_meeting")
    private Timestamp start_meeting;

    @Column(name = "meeting_room_end_meeting")
    private Timestamp end_meeting;

    @Column(name = "meeting_room_recorded")
    private byte recorded;

    @Column(name = "meeting_room_start_record")
    private Timestamp start_record;

    @Column(name = "meeting_room_end_record")
    private Timestamp end_record;
    

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pk.meeting")
    private List<UserMeeting> userMeeting;

    public MeetingRoom() {
    }

    public MeetingRoom(int id, Room id_room, int id_course, String topic, String description, String path, String number_participants, byte finished, Timestamp start_meeting, Timestamp end_meeting, byte recorded, Timestamp start_record, Timestamp end_record, List<UserMeeting> userMeeting) {
        this.id = id;
        this.id_room = id_room;
        this.description = description;
        this.path = path;
        this.number_participants = number_participants;
        this.finished = finished;
        this.start_meeting = start_meeting;
        this.end_meeting = end_meeting;
        this.recorded = recorded;
        this.start_record = start_record;
        this.end_record = end_record;
        this.userMeeting = userMeeting;
    }

    public Room getId_room() {
        return id_room;
    }

    public void setId_room(Room id_room) {
        this.id_room = id_room;
    }

    public List<UserMeeting> getUserMeeting() {
        return userMeeting;
    }

    public void setUserMeeting(List<UserMeeting> userMeeting) {
        this.userMeeting = userMeeting;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNumber_participants() {
        return number_participants;
    }

    public void setNumber_participants(String number_participants) {
        this.number_participants = number_participants;
    }

    public byte getFinished() {
        return finished;
    }

    public void setFinished(byte finished) {
        this.finished = finished;
    }

    public Timestamp getStart_meeting() {
        return start_meeting;
    }

    public void setStart_meeting(Timestamp start_meeting) {
        this.start_meeting = start_meeting;
    }

    public Timestamp getEnd_meeting() {
        return end_meeting;
    }

    public void setEnd_meeting(Timestamp end_meeting) {
        this.end_meeting = end_meeting;
    }

    public byte getRecorded() {
        return recorded;
    }

    public void setRecorded(byte recorded) {
        this.recorded = recorded;
    }

    public Timestamp getStart_record() {
        return start_record;
    }

    public void setStart_record(Timestamp start_record) {
        this.start_record = start_record;
    }

    public Timestamp getEnd_record() {
        return end_record;
    }

    public void setEnd_record(Timestamp end_record) {
        this.end_record = end_record;
    }

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
}
