/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.descriptor.java.DataHelper;
import org.primefaces.context.RequestContext;
import uk.tryzub.entity.HibernateUtil;
import uk.tryzub.entity.Organization;
import org.primefaces.event.CellEditEvent;
import uk.tryzub.beans.LoginView;
import uk.tryzub.entity.Comment;
import uk.tryzub.entity.Event;
import uk.tryzub.entity.Habitation;
import uk.tryzub.entity.Publication;
import uk.tryzub.entity.Review;
import uk.tryzub.entity.User;
import uk.tryzub.entity.Work;

/**
 *
 * @author tszin
 */
@ManagedBean
@SessionScoped
public final class EventHelper implements Serializable {

    private ArrayList<Event> currentEventList; //заповнюється автоматично при створенні обєкту

    private String message;

    /*this field used for adding publication and for selection publication and for adding comments */
    private Event selectedEvent;

    public EventHelper() {

        //делаем это (заполняем лист) при загрузке страницы
        //fillOrganizationsListAll();
    }

    /*creating new HAbitation for adding to DB*/
    public void initNewEvent() {
        selectedEvent = new Event();
        message = "";
    }

    @ManagedProperty(value = "#{loginView}")
    private LoginView loginView;

    public LoginView getLoginView() {
        return loginView;
    }
//Обязательный сеттер для инъекции

    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }

    public void fillEventListAll(/*String section*/) {
        final Session session = HibernateUtil.getSession();

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Event");
                currentEventList = (ArrayList<Event>) q.list();

                transaction.commit();
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
           
        }

    }

    public String addEvent() {

        //get all existing value but set "editable" to false 
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {

                selectedEvent.setId(null); // добавить новую запись, а не изменить существующую
                selectedEvent.setUser(loginView.getAuthenticatedUser());

                session.save(selectedEvent);

                transaction.commit();
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
        }

        //return to current page
        return null;

    }

    public void setChozenEvent(String id) {
        final Session session = HibernateUtil.getSession();

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                int idEvent = Integer.parseInt(id);
                Event selectedPubl = (Event) session.get(Event.class, idEvent);

                this.currentEventList.clear();
                this.currentEventList.add(selectedEvent);
               

                transaction.commit();

                message = "";
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
            
        }

    }

    public void setNewPhoto(String newPhoto) {
        if (selectedEvent != null) {
            selectedEvent.setPhoto(newPhoto);
        }
    }


    public ArrayList<Event> getCurrentEventList() {
        return currentEventList;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedPublication(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    
}
