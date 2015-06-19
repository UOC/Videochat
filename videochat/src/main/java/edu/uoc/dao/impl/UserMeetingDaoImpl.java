/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.dao.impl;

import edu.uoc.dao.UserDao;
import edu.uoc.dao.UserMeetingDao;
import edu.uoc.model.MeetingRoom;
import edu.uoc.model.User;
import edu.uoc.model.UserMeeting;
import edu.uoc.model.UserMeetingHistory;
import edu.uoc.model.UserMeetingId;
import edu.uoc.util.CustomHibernateDaoSupport;
import edu.uoc.util.Util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Francesc Fernandez
 */
@Repository("UserMeetingDao")
public class UserMeetingDaoImpl extends CustomHibernateDaoSupport implements UserMeetingDao,java.io.Serializable{
    @Autowired
    private UserDao userDao;

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

    /**
     * 
     * @param meeting
     * @param user_id
     * @param onlyConfimed
     * @param get_users_from_history_if_not_found when you want to play session then tries to get from table vc_usermeeting and if not found then get from vc_usermeeting_history
     * @param number_user_to_show if get_users_from_history_if_not_found has to try to get the number of users 
     * @return 
     */
    @Override
    public List<UserMeeting> findUsersByMeetingId(MeetingRoom meeting, int user_id, boolean onlyConfimed, boolean get_users_from_history_if_not_found, int number_user_to_show) {
        String extraSQL = "";
        if (onlyConfimed) {
            extraSQL = " and usermeeting_access_confirmed = 1 ";
        }
        List list = getHibernateTemplate().find("from UserMeeting where user_id != ? and meeting_id = ?"+extraSQL, user_id, meeting.getId());
        if (get_users_from_history_if_not_found && (list==null || list.size()<number_user_to_show)) {
            //try to get from history
            if (list==null){
                list = new ArrayList<UserMeeting>();
            }
            String user_id_not_equals = ""+user_id;
            Iterator<UserMeeting> iterator = list.iterator();
            while (iterator.hasNext()) {
                user_id_not_equals += ","+iterator.next().getPk().getUser().getId();
            }
            List<UserMeetingHistory> listHistory = getHibernateTemplate().find("from UserMeetingHistory where user_id not in ("+user_id_not_equals+") and meeting_id = ?", meeting.getId());
            Iterator<UserMeetingHistory> iteratorHistory = listHistory.iterator();
            UserMeeting um;
            UserMeetingHistory umHistory;
            User user;
            while (iteratorHistory.hasNext()) {
                umHistory = iteratorHistory.next();
                //converting to UserMeeting
                user = userDao.findByUserCode(umHistory.getUser_id());
                UserMeetingId umId = new UserMeetingId(user, meeting);
                String meetingIdPath = Util.getMeetingIdPath(meeting.getId_room().getId_course().getCoursekey(), meeting.getId_room().getKey(), meeting.getId());

                um = new UserMeeting(umId, umHistory.getCreated(), meetingIdPath + "_" + user.getUsername(), umHistory.getExtraRole());
                
                list.add(um);
            }
            
        }
        return list;
    }
    
    @Override
    public List<UserMeeting> findUserByMeetingId(MeetingRoom meeting, int user_id, boolean onlyConfimed) {
        String extraSQL = "";
        if (onlyConfimed) {
            extraSQL = " and usermeeting_access_confirmed = 1 ";
        }
        List list = getHibernateTemplate().find("from UserMeeting where user_id = ? and meeting_id = ?"+extraSQL, user_id, meeting.getId());
        return list;
    }
    
    @Override
    public List<UserMeeting> findUsersByMeetingId(MeetingRoom meeting, boolean only_accepted){
        String extra_sql = "";
        if (only_accepted) {
            extra_sql = " AND usermeeting_access_confirmed = 1";
        }
        List list = getHibernateTemplate().find("from UserMeeting where meeting_id = ?"+extra_sql,meeting.getId());
        
        return list;
    }
    
    /**
     * Return the total number of participants
     * @param meeting
     * @return 
     */
    @Override
    public int countNumberParticipants(MeetingRoom meeting) {
        List list = this.findUsersByMeetingId(meeting, true);
        int count = 0;
        if (list!=null){
            count = list.size();
        }
        return count;
    }
}