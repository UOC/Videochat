/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.dao.impl;

import edu.uoc.dao.UserMeetingHistoryDao;
import edu.uoc.model.MeetingRoom;
import edu.uoc.model.UserMeeting;
import edu.uoc.model.UserMeetingHistory;
import edu.uoc.model.UserMeetingId;
import edu.uoc.util.CustomHibernateDaoSupport;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Antoni Bertran
 */
@Repository("UserMeetingHistoryDao")
public class UserMeetingDaoHistoryImpl extends CustomHibernateDaoSupport implements UserMeetingHistoryDao,java.io.Serializable{
    
    @Override
    public void save(UserMeetingHistory userMeeting){
        getHibernateTemplate().saveOrUpdate(userMeeting);
    }

    @Override
    public UserMeetingHistory findUserMeetingByPK(UserMeetingId umID) {
        Criteria criteria;
        criteria = this.getSession().createCriteria(UserMeetingHistory.class, "usermeetinghistory");
        criteria.add(Restrictions.eq("usermeetinghistory.user_id",umID.getUser().getId()));
        criteria.add(Restrictions.eq("usermeetinghistory.meeting_id",umID.getMeeting().getId()));
        criteria.add(Restrictions.isNull("usermeetinghistory.deleted"));
        logger.info("Criteria "+criteria.toString());
       
        List list = criteria.list();
        
        if(list.size() > 0) return (UserMeetingHistory) list.get(0);
        else return new UserMeetingHistory();
    }

}