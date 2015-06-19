/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.model;

/**
 *
 * @author antonibertranbellido
 */
public class Participant {
    
    private int id;
    
    private String username;
    
    private String firstname;
    
    private String surname;    
    
    private String fullname;
    
    private String email;
    
    private String image;
    
    private String stream_key;
    
    private String extra_role;

    
    public Participant(int id, String username, String firstname, String surname,
            String fullname, String email, String image, String stream_key, String extra_role){
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.surname = surname;
        this.fullname = fullname;
        this.email = email;
        this.image = image;
        this.stream_key = stream_key;
        this.extra_role = extra_role;
                
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return the fullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * @param fullname the fullname to set
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return the stream_key
     */
    public String getStream_key() {
        return stream_key;
    }

    /**
     * @param stream_key the stream_key to set
     */
    public void setStream_key(String stream_key) {
        this.stream_key = stream_key;
    }
    
    /**
     * @return the extra_role
     */
    public String getExtra_role() {
        return extra_role;
    }

    /**
     * @param extra_role the extra_role to set
     */
    public void setExtra_role(String extra_role) {
        this.extra_role = extra_role;
    }
}
