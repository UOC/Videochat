/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.dao;

import edu.uoc.model.User;

/**
 *
 * @author Diego
 */
public interface UserDao {

    void save(User user);

    void update(User user);

    void delete(User user);

    User findByUserCode(int userId);

}
