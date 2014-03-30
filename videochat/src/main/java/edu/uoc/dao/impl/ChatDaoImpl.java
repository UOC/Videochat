/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.dao.impl;

import edu.uoc.dao.ChatDao;
import edu.uoc.model.Chat;
import edu.uoc.model.MeetingRoom;
import edu.uoc.model.UserMeeting;
import edu.uoc.util.CustomHibernateDaoSupport;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Francesc Fernandez
 */
@Repository("ChatDao")
public class ChatDaoImpl extends CustomHibernateDaoSupport implements ChatDao{
    
    @Override
    public void save(Chat chat){
        getHibernateTemplate().saveOrUpdate(chat);
    }

    @Override
    public void delete(Chat chat) {
        getHibernateTemplate().delete(chat);
    }

    @Override
    public List<UserMeeting> findByMeetingId(MeetingRoom meeting) {
        List list = getHibernateTemplate().find("from Chat where meeting_id = ?", meeting.getId());
        return list;
    }
    
}
