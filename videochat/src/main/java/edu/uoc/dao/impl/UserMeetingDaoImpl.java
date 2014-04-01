/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.dao.impl;

import edu.uoc.dao.UserMeetingDao;
import edu.uoc.model.MeetingRoom;
import edu.uoc.model.User;
import edu.uoc.model.UserMeeting;
import edu.uoc.model.UserMeetingId;
import edu.uoc.util.CustomHibernateDaoSupport;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Francesc Fernandez
 */
@Repository("UserMeetingDao")
public class UserMeetingDaoImpl extends CustomHibernateDaoSupport implements UserMeetingDao{
    
    @Override
    public void save(UserMeeting userMeeting){
        getHibernateTemplate().saveOrUpdate(userMeeting);
    }

    @Override
    public void delete(UserMeeting userMeeting) {
        getHibernateTemplate().delete(userMeeting);
    }

    @Override
    public UserMeeting findUserMeetingByPK(UserMeetingId umID) {
        List list = getHibernateTemplate().find("from UserMeeting where user_id = ? and meeting_id = ?", umID.getUser().getId(), umID.getMeeting().getId());
        
        if(list.size() > 0) return (UserMeeting) list.get(0);
        else return new UserMeeting();
    }

    @Override
    public List<UserMeeting> findUsersByMeetingId(MeetingRoom meeting, int user_id, boolean onlyConfimed) {
        String extraSQL = "";
        if (onlyConfimed) {
            extraSQL = " and usermeeting_access_confirmed = 1 ";
        }
        List list = getHibernateTemplate().find("from UserMeeting where user_id != ? and meeting_id = ?"+extraSQL, user_id, meeting.getId());
        return list;
    }
    
    @Override
    public List<UserMeeting> findUsersByMeetingId(MeetingRoom meeting){
        List list = getHibernateTemplate().find("from UserMeeting where meeting_id = ?",meeting.getId());
        
        return list;
    }
}
