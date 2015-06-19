/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.model.json;

import edu.uoc.model.Chat;
import java.util.List;

/**
 *
 * @author antonibertranbellido
 */
public class JSONResponseVideochatChat extends JSONResponse {
    private List<Chat> chatMessages;
    public void setChatMessages(List<Chat> chatMessages) {
        this.chatMessages = chatMessages;
    }
    public List<Chat> getChatMessages() {
        return this.chatMessages;
    }
}
