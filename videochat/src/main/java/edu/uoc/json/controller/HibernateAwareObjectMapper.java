/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.json.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate3.Hibernate3Module;

/**
 *
 * @author antonibertranbellido
 */
public class HibernateAwareObjectMapper extends ObjectMapper {
    
     public HibernateAwareObjectMapper() {
        registerModule(new Hibernate3Module());
    }
    
}
