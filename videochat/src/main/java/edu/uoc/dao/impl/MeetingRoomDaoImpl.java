/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.dao.impl;

import edu.uoc.dao.MeetingRoomDao;
import edu.uoc.model.MeetingRoom;
import edu.uoc.util.CustomHibernateDaoSupport;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Francesc Fernandez
 */
@Repository("MeetingRoomDao")
public class MeetingRoomDaoImpl extends CustomHibernateDaoSupport implements MeetingRoomDao {

    @Override
    public void save(MeetingRoom meetingRoom) {
        getHibernateTemplate().save(meetingRoom);
    }

    @Override
    public void update(MeetingRoom meetingRoom) {
        getHibernateTemplate().merge(meetingRoom);
    }

    @Override
    public void delete(MeetingRoom meetingRoom) {
       getHibernateTemplate().delete(meetingRoom);
    }
    
}
