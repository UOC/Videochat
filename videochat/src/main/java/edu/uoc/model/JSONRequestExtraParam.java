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
public class JSONRequestExtraParam  extends JSONRequest {
    private String extraParam;
    
    public String getExtraParam() {
        return extraParam;
    }
    
    public void setExtraParam(String extraParam) {
        this.extraParam = extraParam;
    }
}
