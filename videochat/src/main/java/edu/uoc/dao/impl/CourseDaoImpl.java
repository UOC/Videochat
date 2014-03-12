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
        getHibernateTemplate().update(course);
    }

    @Override
    public void delete(Course course) {
        getHibernateTemplate().delete(course);
    }

    @Override
    public Course findByCourseCode(int courseId) {
        List list = getHibernateTemplate().find(
                "from VC_COURSE where COURSE_ID=?", courseId
        );
        return (Course) list.get(0);
    }

}
