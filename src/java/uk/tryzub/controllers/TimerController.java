/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.controllers;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


/**
 *
 * @author tszin
 */
@ManagedBean
@SessionScoped
public final class TimerController implements Serializable {


    public TimerController() {
        //fillTopics(); - this one fill on view (metadata)
    }

}
