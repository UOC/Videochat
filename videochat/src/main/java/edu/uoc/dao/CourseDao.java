/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.dao;

import edu.uoc.model.Course;

/**
 *
 * @author Diego
 */
public interface CourseDao {

    void save(Course course);

    void update(Course course);

    void delete(Course course);

    Course findByCourseCode(int courseId);

}
