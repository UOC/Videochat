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
		getHibernateTemplate().save(user);
	}
 
        @Override
	public void update(User user){
		getHibernateTemplate().update(user);
	}
 
        @Override
	public void delete(User user){
		getHibernateTemplate().delete(user);
	}
 
        @Override
	public User findByUserCode(int userId){
		List list = getHibernateTemplate().find(
                     "from VC_USER where USER_ID=?",userId
                );
		return (User)list.get(0);
	}
 


}