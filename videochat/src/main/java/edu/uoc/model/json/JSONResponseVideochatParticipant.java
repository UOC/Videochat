/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.model.json;

import edu.uoc.model.Participant;
import java.util.List;

/**
 *
 * @author antonibertranbellido
 */
public class JSONResponseVideochatParticipant extends JSONResponse {
    private List<Participant> participants;
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
    public List<Participant> getParticipants() {
        return this.participants;
    }
}
