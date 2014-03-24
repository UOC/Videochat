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
import org.springframework.stereotype.Component;

/**
 *
 * @author Diego
 */



@Component
@Entity
@Table(name="VC_USER")
public class User implements java.io.Serializable{
    
    
    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Column(name="USER_ID",unique=true,length=11) 
    private int id;
    
    @Column(name="USER_USERNAME",length=70)
    private String username;
    
    
    @Column(name="USER_PASSWORD",length=255)
    private String password;
    
    @Column(name="USER_FIRSTNAME",length=50)
    private String firstname;
    
    @Column(name="USER_SURNAME",length=75)
    private String surname;
    
    
    @Column(name="USER_FULLNAME",length=150)
    private String fullname;
    
    @Column(name="USER_EMAIL",length=50)
    private String email;
    
    @Column(name="USER_IMAGE",length=150)
    private String image;
    
    @Column(name="USER_BLOCKED")
    private byte blocked;
    
    @Column(name="USER_TIMESTAMP")
    private Timestamp created;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pk.user")
    private List<UserCourse> course;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pk.user")
    private List<UserMeeting> meeting;
    
    
    public User() {
    }

    public User(int id, String username, String password, String firstname, String surname, String fullname, String email, String image, byte blocked, Timestamp created, List<UserCourse> course, List<UserMeeting> meeting) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.surname = surname;
        this.fullname = fullname;
        this.email = email;
        this.image = image;
        this.blocked = blocked;
        this.created = created;
        this.course = course;
        this.meeting = meeting;
    }

    

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public byte getBlocked() {
        return blocked;
    }

    public void setBlocked(byte blocked) {
        this.blocked = blocked;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public List<UserCourse> getCourse() {
        return course;
    }

    public void setCourse(List<UserCourse> course) {
        this.course = course;
    }
    
    
    
    
    
    
    
    
    
    
}
