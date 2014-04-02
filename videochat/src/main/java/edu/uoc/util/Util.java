/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    
    
    public static String getTimestampFormatted(Timestamp ts, int format){
        String s = "";
        if (ts!=null) {
        Date dayte = new Date(ts.getTime());
            String pattern = "";
            switch(format){
                case Constants.FORMAT_DATETIME: pattern = "dd/MM/yyyy HH:mm:ss";
                    break;
                case Constants.FORMAT_DATE: pattern = "dd/MM/yyyy";
                    break;
                case Constants.FORMAT_TIME:
                    pattern = "HH:mm:ss";
                    break;
                default:
                    break;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            s = sdf.format(dayte);
        }
        return s;
    }

    public static String substractTimestamps(Timestamp end, Timestamp start){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String diffS="";
        if (start!=null && end!=null) { 
            Date firstParsedDate = new Date(start.getTime());
            Date secondParsedDate = new Date(end.getTime());

            long diff = secondParsedDate.getTime() - firstParsedDate.getTime();

            diffS = dateFormat.format(new Date(diff));
        }
        return diffS;
    }

}
