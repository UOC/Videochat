/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.model.json;

import edu.uoc.model.Course;

/**
 *
 * @author antonibertranbellido
 */
public class JSONResponseCourse extends JSONResponse {
    private Course course;
    public Course getCourse() {
        return course;
    }
    public void setCourse( Course c ){
        this.course = c;
    }
}
