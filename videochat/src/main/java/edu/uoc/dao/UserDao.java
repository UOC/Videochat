/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.dao;

import edu.uoc.model.User;

import java.util.List;


/**
 *
 * @author Diego
 */
public interface UserDao {

    void save(User user);

    //void update(User user);

    void delete(User user);

    User findByUserCode(int userId);
    
    User findByUserToken(String email, String token) throws Exception;
    
    User findByUserName(String username);



    // Operación de prueba para obtener todos los usuarios
    List<User> findAll();

}
