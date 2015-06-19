/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.model.json;

/**
 *
 * @author antonibertranbellido
 */
public class JSONResponseTokenEmail extends JSONResponse {
    private String email;

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param userId the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
}
