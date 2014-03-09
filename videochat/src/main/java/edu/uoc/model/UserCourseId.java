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
public class UserCourseId implements java.io.Serializable{
    
    
    
    @ManyToOne
    private User user;
    @ManyToOne
    private Course course;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.user != null ? this.user.hashCode() : 0);
        hash = 79 * hash + (this.course != null ? this.course.hashCode() : 0);
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
        final UserCourseId other = (UserCourseId) obj;
        if (this.user != other.user && (this.user == null || !this.user.equals(other.user))) {
            return false;
        }
        if (this.course != other.course && (this.course == null || !this.course.equals(other.course))) {
            return false;
        }
        return true;
    }

    public UserCourseId(User user, Course course) {
        this.user = user;
        this.course = course;
    }

    public UserCourseId() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    
    
    
}
