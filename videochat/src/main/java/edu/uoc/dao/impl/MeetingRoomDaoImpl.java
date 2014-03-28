/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.dao.impl;

import edu.uoc.dao.MeetingRoomDao;
import edu.uoc.model.MeetingRoom;
import edu.uoc.model.Room;
import edu.uoc.model.UserCourse;
import edu.uoc.util.CustomHibernateDaoSupport;
import java.util.List;
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

    @Override
    public List<MeetingRoom> getMeetingRoomsByCourseKey(String courseKey) {

        /* SQL QUERY:
         SELECT m.meeting_room_description, m.meeting_room_number_participants, m.meeting_room_start_meeting, m.meeting_room_end_meeting
         FROM vc_meeting m, vc_room r
         WHERE m.room_id = r.room_id
         AND r.course_key =  '586' */
        String query = "SELECT m.description, m.number_participants, m.start_meeting, m.end_meeting"
                + " FROM MeetingRoom m, Room r"
                + " WHERE m.id_room = r.id AND r.key = ?";

        List list = getHibernateTemplate().find(query, courseKey);
        return list;
    }

    @Override
    public MeetingRoom findByRoomCode(int meetingroomId) {

        List list = getHibernateTemplate().find(
                "from MeetingRoom where meeting_room_id=?", meetingroomId);
        if (list.size() > 0) {
            return (MeetingRoom) list.get(0);
        } else {
            return new MeetingRoom();
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
    
}
