/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.controllers;

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
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
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
import uk.tryzub.entity.Dating;
import uk.tryzub.entity.Habitation;
import uk.tryzub.entity.Review;
import uk.tryzub.entity.User;

/**
 *
 * @author tszin
 */
@ManagedBean
@SessionScoped
public final class DatingHelper implements Serializable {

    private ArrayList<Dating> currentDatingList; //заповнюється автоматично при створенні обєкту

    private Dating dating;

    public DatingHelper() {

       
    
    }

    /*creating new HAbitation for adding to DB*/
    @PostConstruct
    public void init() {
        dating = new Dating();
    }

    public void setNewDating() {
        this.dating = new Dating();
    }

    public ArrayList<Dating> getCurrentDatingList() {
        return currentDatingList;
    }

    public void fillDatingsListAll(/*String section*/) {
        final Session session = HibernateUtil.getSession();

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Dating order by date desc");
                currentDatingList = (ArrayList<Dating>) q.list();

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

    /*
    In jsf can not to send parameters in method signature, only in params
     */
    public void fillDatingsByType() {
        Map<String, String> params
                = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Dating where section = " + params.get("section")); // получение окончания с параметров
                currentDatingList = (ArrayList<Dating>) q.list();

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

    public String addDating() {

        //get all existing value but set "editable" to false 
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {

                dating.setId(null); // добавить новую запись, а не изменить существующую
                dating.setSection(1);

                session.save(dating);

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
    
      
   
    public Dating getDating() {
        return dating;
    }

    public void setDating(Dating dating) {
        this.dating = dating;
    }
}
