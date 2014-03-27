/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.model;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import org.springframework.stereotype.Component;

/**
 *
 * @author Diego
 */

    
@Entity
@Component
@Table(name="vc_usercourse")
@AssociationOverrides({
		@AssociationOverride(name = "pk.user", 
			joinColumns = @JoinColumn(name = "user_id")),
		@AssociationOverride(name = "pk.course", 
			joinColumns = @JoinColumn(name = "course_id")) })

public class UserCourse implements java.io.Serializable {
    
    
    
    @EmbeddedId
    private UserCourseId pk = new UserCourseId();
    
    @Column(name="usercourse_role")

    private String role;
    
    
    

    

    @Column(name="usercourse_is_instructor")

    private boolean is_instructor;

    public UserCourse() {
    }

    public UserCourse(UserCourseId pk, String role, boolean is_instructor) {
        this.pk = pk;
        this.role = role;
        this.is_instructor = is_instructor;
    }

    public UserCourseId getPk() {
        return pk;
    }

    public void setPk(UserCourseId pk) {
        this.pk = pk;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isIs_instructor() {
        return is_instructor;
    }

    public void setIs_instructor(boolean is_instructor) {
        this.is_instructor = is_instructor;
    }
    
    
    
    
}
