/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.dao;

import edu.uoc.model.Course;
import edu.uoc.model.Room;

/**
 *
 * @author Diego
 */
public interface RoomDao {

    void save(Room room);

    void update(Room room);

    void delete(Room room);

    Room findByRoomCode(int roomId);

    public Room findByRoomKey(String roomkey);

}
