/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.util;

import edu.uoc.model.json.JSONResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.imsglobal.basiclti.Base64;

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
            
            long diff = end.getTime()-start.getTime();
            long seconds = diff/1000;
            long hours = seconds/3600;
            long minutes = seconds/60 % 60;
            seconds = seconds % 60;
            
            //Date date = new Date(diff);
            //diffS = dateFormat.format(date);
            diffS = completeIt(hours) + ":" + completeIt(minutes) + ":" + completeIt(seconds);
        }
        return diffS;
    }
    
    
    private static String completeIt(long x)
    {
        String S=Long.toString(x);
        if (S.length()==1)
        {
            S="0" + S;
        }
        return S;
     }
    
    public static String formatDateString(String date ){
        
        String year;
        String month;
        String day;
        
        year= date.substring(6);
        month=date.substring(3, 5);
        day= date.substring(0,2);
        
        String fDate = year+"-"+month+"-"+day+" "+"00:00" ;
        return fDate;
    }
    
    public static Timestamp converToTimestamp(String s, Logger log) {
        Timestamp ts = null;
        try {
            if (s!=null && s.length()>0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
                java.util.Date date = sdf.parse(s); 
                ts = new java.sql.Timestamp(date.getTime()); 
            }
        }
         catch (ParseException e) {
             log.error("Error formatting to timestamp ", e);
         }
        return ts;
    }
    
    /**
     * REturns the Meeting id Path
     * @param coursekey
     * @param roomKey
     * @param meetingId
     * @return 
     */
    public static String getMeetingIdPath(String coursekey, String roomKey, int meetingId) {
        return coursekey + "_" + roomKey + "_" + meetingId;
    }
    
    /**
     * Call to external service
     * @param urlStr
     * @param str_to_check
     * @param response
     * @param logger
     * @return 
     */
    public static JSONResponse curlJson(String urlStr, String str_to_check, JSONResponse response, Logger logger) {
        try {
            if (urlStr!=null && urlStr.length()>0) {
                URL url = new URL(urlStr);
                BufferedReader reader = null;

                try {
                    reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                    for (String line; (line = reader.readLine()) != null;) {
                        if (line.contains(str_to_check)){
                            response.setOk(true);
                        } else {
                            logger.info("urlStr "+urlStr);
                            logger.info("curlJson "+line);
                        }
                    }
                    
                } finally {
                    if (reader != null) try { reader.close(); } catch (IOException ignore) {}
                }
                
            }
        } catch (Exception e) {
            logger.error("curlJson ", e);
            
        }
        return response;
    }    
    
    
    /**
     * Call to external service
     * @param urlStr
     * @param username
     * @param pwd
     * @param str_to_check
     * @param response
     * @param logger
     * @return 
     */
    public static JSONResponse curlJson(String urlStr, String username, String pwd, String str_to_check, JSONResponse response, Logger logger) {
        try {
            if (urlStr!=null && urlStr.length()>0) {
                Authenticator.setDefault(new WowzaAuthenticator(username, pwd));
                URL url = new URL(urlStr);
                URLConnection urlConnection = url.openConnection();
                String userPassword = username + ":" + pwd;
                String encoding = new String (Base64.encode(userPassword.getBytes()));
                urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
                urlConnection.connect();
                BufferedReader reader = null;

                try {
                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    int numCharsRead;
                    char[] charArray = new char[1024];
                    StringBuffer sb = new StringBuffer();
                    while ((numCharsRead = isr.read(charArray)) > 0) {
                            sb.append(charArray, 0, numCharsRead);
                    }
                    String result = sb.toString();
                    if (result.contains(str_to_check)){
                        response.setOk(true);
                    } else {
                        logger.info("urlStr "+urlStr);
                        logger.info("curlJson "+result);
                    }
                    
                } finally {
                    if (reader != null) try { reader.close(); } catch (IOException ignore) {}
                }
                
            }
        } catch (Exception e) {
            logger.error("curlJson error urlStr="+urlStr+" username: "+username+" pwd: "+pwd, e);
            
        }
        return response;
    }    
    
    public static String getFullUrl(HttpServletRequest request, Logger logger) {
         String baseUrl = null;
         try {
             baseUrl = String.format("%s://%s:%d/",request.getScheme(),  request.getServerName(), request.getServerPort());
         } catch (Exception e) {
             logger.error("Getting the baseUrl of videochat", e);
            
         }
         return baseUrl;
    }

}
 class WowzaAuthenticator extends Authenticator {
     String username;
     String pwd;
     public WowzaAuthenticator(String u, String p) {
         username = u;
         pwd = p;
     }
       
   
     public PasswordAuthentication getPasswordAuthentication() {
            // I haven't checked getRequestingScheme() here, since for NTLM
            // and Negotiate, the usrname and password are all the same.
            //System.err.println("Feeding username and password for " + getRequestingScheme()+" username "+username);
            return (new PasswordAuthentication(username, pwd.toCharArray()));
     }
     
}
