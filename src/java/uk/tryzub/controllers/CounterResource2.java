/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.controllers;

import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

@PushEndpoint("/updateGame")
public class CounterResource2 {

    @OnMessage(encoders = {JSONEncoder.class})
    public String onMessage(String message) {
        return message;
    }
}
