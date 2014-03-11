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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Diego
 */


@Entity
@Table(name="VC_MEETING")
public class Meeting implements java.io.Serializable {
    
    
    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Column(name="MEETING_ID",length=11)
    private int id;
    
    @Column(name="MEETING_ID_COURSE",length=11)
    private int id_course;
    
    @Column(name="MEETING_TOPIC",length=255)
    private String topic;
    
    @Column(name="MEETING_DESCRIPTION",length=255)
    private String description;
    
    @Column(name="MEETING_PATH",length=255)
    private String path;
    
    @Column(name="MEETING_NUMBER_PARTICIPANTS",length=2)
    private int number_participants;
    
    @Column(name="MEETING_FINISHED")
    private byte finished;
    
    @Column(name="MEETING_START_MEETING")
    private Timestamp start_meeting;
    
    @Column(name="MEETING_END_MEETING")
    private Timestamp end_meeting;
    
    @Column(name="MEETING_RECORDED")
    private byte recorded;
    
    @Column(name="MEETING_START_RECORD")
    private Timestamp start_record;
    
    @Column(name="MEETING_END_RECORD")
    private Timestamp end_record;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pk.meeting")
    private List<UserMeeting> userMeeting;


    public Meeting() {
    }

    public Meeting(int id, int id_course, String topic, String description, String path, int number_participants, byte finished, Timestamp start_meeting, Timestamp end_meeting, byte recorded, Timestamp start_record, Timestamp end_record) {
        this.id = id;
        this.id_course = id_course;
        this.topic = topic;
        this.description = description;
        this.path = path;
        this.number_participants = number_participants;
        this.finished = finished;
        this.start_meeting = start_meeting;
        this.end_meeting = end_meeting;
        this.recorded = recorded;
        this.start_record = start_record;
        this.end_record = end_record;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_course() {
        return id_course;
    }

    public void setId_course(int id_course) {
        this.id_course = id_course;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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

    public int getNumber_participants() {
        return number_participants;
    }

    public void setNumber_participants(int number_participants) {
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
    
    
    
    
    
    
    
    
    
    
    
    
}
