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
import java.util.ArrayList;
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
    
}
