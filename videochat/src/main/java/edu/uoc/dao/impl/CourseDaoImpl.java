/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.dao.impl;

import edu.uoc.dao.CourseDao;
import edu.uoc.model.Course;
import edu.uoc.util.CustomHibernateDaoSupport;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Diego
 */
@Repository("CourseDao")
public class CourseDaoImpl extends CustomHibernateDaoSupport implements CourseDao {

    
    @Override
    public void save(Course course) {
        getHibernateTemplate().save(course);
    }

    @Override
    public void update(Course course) {
        getHibernateTemplate().merge(course);
    }

    @Override
    public void delete(Course course) {
        getHibernateTemplate().delete(course);
    }

    @Override
    public Course findByCourseCode(int courseId) {
        List list = getHibernateTemplate().find(
                "from Course where course_id=?", courseId);
        if(list.size()>0){
            return (Course) list.get(0);
        }else{
            return new Course();
        }
        
    }
    
    public Course findByCourseKey(String courskey){
         List list = getHibernateTemplate().find(
                "from Course where course_coursekey=?", courskey);
        if(list.size()>0){
            return (Course) list.get(0);
        }else{
            return new Course();
        }
    }
        
    

}
