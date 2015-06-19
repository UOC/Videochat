/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.dao.impl;

import edu.uoc.dao.UserDao;
import edu.uoc.model.User;
import edu.uoc.util.CustomHibernateDaoSupport;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Diego
 */
@Repository("UserDao")

public class UserDaoImpl extends CustomHibernateDaoSupport implements UserDao{
  
    
    

 
        @Override
	public void save(User user){
		getHibernateTemplate().saveOrUpdate(user);
	}
 
        /*@Override
	public void update(User user){
		getHibernateTemplate().merge(user);
	}*/
 
        @Override
	public void delete(User user){
		getHibernateTemplate().delete(user);
	}
 
        @Override
	public User findByUserCode(int userId){
		List list = getHibernateTemplate().find("from User where user_id=?",userId);
		 if(list.size()>0){
            return (User)list.get(0);
        }
		return new User();
	}
 
        // Operación de prueba para obtener todos los usaurios
        @Override
            public List<User> findAll(){
                
                // Session s = getHibernateTemplate().getSessionFactory().openSession();
                List list = getHibernateTemplate().find("from User");

                return list;
            }

    @Override
    public User findByUserName(String username)  {
        List list = getHibernateTemplate().find("from User where user_username=?",username);
        if(list.size()>0){
            return (User)list.get(0);
        }
		return new User();
	}

    @Override
    public User findByUserToken(String email, String token) throws Exception {
        List list = getHibernateTemplate().find("from User where user_email=? AND token_access=?",email, token);
        if(list.size()>0){
            return (User)list.get(0);
        }	
        throw new Exception("User not found."); 
    }


}