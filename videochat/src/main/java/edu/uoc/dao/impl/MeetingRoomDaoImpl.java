/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.dao.impl;

import edu.uoc.model.SearchMeeting;
import edu.uoc.dao.MeetingRoomDao;
import edu.uoc.model.MeetingRoom;
import edu.uoc.model.Room;
import edu.uoc.model.User;
import edu.uoc.model.UserMeeting;
import edu.uoc.util.CustomHibernateDaoSupport;
import edu.uoc.util.Util;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Francesc Fernandez
 */
@Repository("MeetingRoomDao")
public class MeetingRoomDaoImpl extends CustomHibernateDaoSupport implements MeetingRoomDao,java.io.Serializable {
     //get log4j handler
    private static final Logger logger = Logger.getLogger(MeetingRoomDaoImpl.class);

    @Override
    public void save(MeetingRoom meetingRoom) {
        getHibernateTemplate().saveOrUpdate(meetingRoom);
    }

    /*@Override
    public void update(MeetingRoom meetingRoom) {
        getHibernateTemplate().merge(meetingRoom);
    }*/

    @Override
    public void delete(MeetingRoom meetingRoom) {
        getHibernateTemplate().delete(meetingRoom);
    }

    @Override
    public List<MeetingRoom> findByRoomId(int roomId, boolean onlyRecorded) {

        String extraSQL = "";
        if (onlyRecorded) {
            extraSQL = " and meeting_room_recorded = 1 ";
        }
        List list = getHibernateTemplate().find(
                "from MeetingRoom where room_id=?" + extraSQL, roomId);
        if (list.size() > 0) {
            return list;
        } else {
            return new ArrayList<MeetingRoom>();
        }
    }
    
    /*@Override
    public List<MeetingRoom> findByCourseId(String courseId, boolean onlyRecorded) {

        String extraSQL = "";
        if (onlyRecorded) {
            extraSQL = " meeting_room_recorded = 1 and ";
        }
        List list = getHibernateTemplate().find(
                //"from MeetingRoom as meeting, Room as room where room.id_course=?" + extraSQL, courseId);
                "from MeetingRoom  WHERE "+extraSQL+" room_id in ("+courseId+")" );
        if (list.size() > 0) {
            return list;
        } else {
            return new ArrayList<MeetingRoom>();
        }
    }*/
    
    @Override
    public MeetingRoom findByRoomIdNotFinished(int roomId) {

        List list = getHibernateTemplate().find(
                "from MeetingRoom where room_id=? and meeting_room_finished=?", roomId, 0);
        if (list.size() > 0) {
            return (MeetingRoom)list.get(0);
        } else {
            return null;
        }
    }
    
    @Override
    public MeetingRoom findById(int id) {

        List list = getHibernateTemplate().find(
                "from MeetingRoom where id=? ", id);
        if (list.size() > 0) {
            return (MeetingRoom)list.get(0);
        } else {
            return null;
        }
    }


    @Override
    public MeetingRoom findbyPath(String path) {
       List list = getHibernateTemplate().find(
                "from MeetingRoom where meeting_room_path=?", path);
       
       if(list.size()>0){
		return (MeetingRoom)list.get(0);
	}else{
           
           return new MeetingRoom();
       }
    }
    
       
   @Override
    public List<MeetingRoom> findbyForm(SearchMeeting searchMeeting, List<Room> ids_room) {
       
       String topic = searchMeeting.getTopic();
       Room room = searchMeeting.getRoom();
       
       //Convert TimeStamp to Date
       Timestamp tsStart = Util.converToTimestamp(searchMeeting.getStart_meeting(), logger);
       Timestamp tsEnd = Util.converToTimestamp(searchMeeting.getEnd_meeting(), logger);
       
        
        Criteria criteria;
        criteria = this.getSession().createCriteria(MeetingRoom.class, "meeting");
        criteria.add(Restrictions.eq("meeting.recorded",(byte)1));
        if(tsStart!=null){
            criteria.add(Restrictions.ge("meeting.start_meeting",tsStart));
	}
        if(tsEnd!=null){
            criteria.add(Restrictions.le("meeting.end_meeting",tsEnd));
	}
	if(topic!=null && topic.length()>0){
            criteria.add(Restrictions.like("meeting.topic","%"+topic+"%"));
	}
	if(room!=null && room.getId()>0){
            criteria.add(Restrictions.eq("meeting.id_room",room));
	} else {
            criteria.add(Restrictions.in("meeting.id_room", ids_room));
        }
        if (false && searchMeeting.getParticipants()!=null && searchMeeting.getParticipants().length()>0) {
            DetachedCriteria subCriteria = DetachedCriteria.forClass(UserMeeting.class, "userMeeting");
                subCriteria.createAlias("userMeeting.pk.meeting", "meeting_id");
                subCriteria.setFetchMode("User", FetchMode.JOIN);
                subCriteria.add(Restrictions.like("user.fullname", "%"+searchMeeting.getParticipants()+"%"));
                //subCriteria.add(Restrictions.eqProperty("userMeeting.meeting_id",""));
            criteria.add(Subqueries.in("meeting.id", subCriteria));
        }
        logger.info("Criteria "+criteria.toString());
       
           return criteria.list();
       }
   
    
    
}
