/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.util;

/**
 *
 * @author antonibertranbellido
 */
public class Util {
    
    public static String sanitizeString(String s) {
        if (s!=null) {
            s = s.replaceAll(" ", "_");
            s = s.replaceAll(":", "_");
        }
        return s;
    }
    
}
