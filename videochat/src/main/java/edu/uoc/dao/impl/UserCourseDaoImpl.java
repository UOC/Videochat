/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.dao.impl;

import edu.uoc.dao.UserCourseDao;
import edu.uoc.model.UserCourse;
import edu.uoc.util.CustomHibernateDaoSupport;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Diego
 */
@Repository("UserCourseDao")
public class UserCourseDaoImpl extends CustomHibernateDaoSupport implements UserCourseDao {

    @Override
    public void save(UserCourse usercourse) {
        getHibernateTemplate().saveOrUpdate(usercourse);
    }

   /* @Override
    public void update(UserCourse usercourse) {
        getHibernateTemplate().merge(usercourse);
               
    }*/

    @Override
    public void delete(UserCourse usercourse) {
       getHibernateTemplate().delete(usercourse);
    }

    @Override
    public UserCourse findByCourseCode(int courseId, int userId) {
       List list = getHibernateTemplate().find("from UserCourse where user_id=? and course_id=? ",userId,courseId);
       
       if(list.size()>0){
		return (UserCourse)list.get(0);
	}else{
           
           return new UserCourse();
       }
   
    }

    @Override
    public List<UserCourse> findCoursesByUserId(int userId) {
       List list = getHibernateTemplate().find("from UserCourse where user_id=?",userId);
       
       return list;
    }

    @Override
    public List<UserCourse> findUsersByCourse(int courseId) {
        List list = getHibernateTemplate().find("from UserCourse where course_id=?",courseId);
       
       return list;
    }
    
    
}
