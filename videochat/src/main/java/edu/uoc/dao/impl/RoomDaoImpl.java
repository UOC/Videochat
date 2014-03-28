/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.dao.impl;

import edu.uoc.dao.CourseDao;
import edu.uoc.dao.RoomDao;
import edu.uoc.model.Course;
import edu.uoc.model.Room;
import edu.uoc.util.CustomHibernateDaoSupport;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Diego
 */
@Repository("RoomDao")
public class RoomDaoImpl  extends CustomHibernateDaoSupport implements RoomDao {

     
    @Override
    public void save(Room room) {
        getHibernateTemplate().save(room);
    }

    @Override
    public void update(Room room) {
        getHibernateTemplate().merge(room);
    }

    @Override
    public void delete(Room room) {
        getHibernateTemplate().delete(room);
    }

    @Override
    public Room findByRoomCode(int roomId) {
        List list = getHibernateTemplate().find(
                "from Room where room_id=?", roomId);
        if(list.size()>0){
            return (Room) list.get(0);
        }else{
            return new Room();
        }
        
    }
    
    public Room findByRoomKey(String roomKey){
         List list = getHibernateTemplate().find(
                "from Room where room_key=?", roomKey);
        if(list.size()>0){
            return (Room) list.get(0);
        }else{
            return new Room();
        }
    }
        
    
}
