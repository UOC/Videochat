/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.model;

import javax.persistence.ManyToOne;

/**
 *
 * @author Diego
 */
public class UserMeetingId implements java.io.Serializable{

    @ManyToOne
    private User user;
    @ManyToOne
    private Meeting meeting;

    public UserMeetingId() {
    }

    public UserMeetingId(User user, Meeting meeting) {
        this.user = user;
        this.meeting = meeting;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.user != null ? this.user.hashCode() : 0);
        hash = 17 * hash + (this.meeting != null ? this.meeting.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserMeetingId other = (UserMeetingId) obj;
        if (this.user != other.user && (this.user == null || !this.user.equals(other.user))) {
            return false;
        }
        if (this.meeting != other.meeting && (this.meeting == null || !this.meeting.equals(other.meeting))) {
            return false;
        }
        return true;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

}
