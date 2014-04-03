/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.dao;

import edu.uoc.model.Room;
import java.util.List;

/**
 *
 * @author Diego
 */
public interface RoomDao {

    public void save(Room room);

    //void update(Room room);

    public void delete(Room room);

    public Room findByRoomCode(int roomId);

    public Room findByRoomKey(String roomkey);

    public List<Room> findByIdCourse(int id);

}
