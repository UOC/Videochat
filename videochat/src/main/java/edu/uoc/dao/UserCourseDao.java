/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.dao;

import edu.uoc.model.UserCourse;

/**
 *
 * @author Diego
 */
public interface UserCourseDao {
    
    void save(UserCourse usercourse);

    //void update(UserCourse usercourse);

    void delete(UserCourse usercourse);

    UserCourse findByCourseCode(int courseId,int userId);

    
}
