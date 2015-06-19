/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.util;

import edu.uoc.model.UserMeeting;
import java.util.ArrayList;

/**
 *
 * @author Diego
 */
public class UtilJSP {
    
    
     public static boolean contains(ArrayList list, Object o) {
         boolean encontrado=false;
         int id = (Integer)o;
         ArrayList<UserMeeting> lUM = list;
         int i=0;
         while(!encontrado && i<lUM.size()){
             encontrado=lUM.get(i).getPk().getMeeting().getId()==id;
             if(!encontrado){
                 i++;
             }
             
         }
         
         
     return encontrado;
   }
    
}
