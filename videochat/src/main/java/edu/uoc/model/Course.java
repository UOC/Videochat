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


@Table(name="vc_course")

public class Course implements java.io.Serializable{
    
    
    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Column(name="COURSE_ID",length=11)
    private int id;
    
    @Column(name="COURSE_COURSEKEY",length=255)
    private String coursekey;
    
    @Column(name="COURSE_TITLE",length=255)
    private String title;
    
    @Column(name="COURSE_LANG",length=10)
    private String lang;
    
    @Column(name="COURSE_CREATED")
    private Timestamp created;
    
    
   
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pk.course")
    private List<UserCourse> userCourse;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id_course")
    private List<Room> room;


    public Course() {
    }

    public Course(int id, String coursekey, String title, String lang, Timestamp created, List<UserCourse> userCourse, List<Room> room) {
        this.id = id;
        this.coursekey = coursekey;
        this.title = title;
        this.lang = lang;
        this.created = created;
        this.userCourse = userCourse;
        this.room = room;
    }

    public List<Room> getRoom() {
        return room;
    }

    public void setRoom(List<Room> room) {
        this.room = room;
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoursekey() {
        return coursekey;
    }

    public void setCoursekey(String coursekey) {
        this.coursekey = coursekey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public List<UserCourse> getUserCourse() {
        return userCourse;
    }

    public void setUserCourse(List<UserCourse> userCourse) {
        this.userCourse = userCourse;
    }
    
    
    
    
    
    
    
    
    
    
}
