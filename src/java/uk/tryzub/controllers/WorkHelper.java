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
import javax.faces.bean.ManagedProperty;
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
import uk.tryzub.beans.LoginView;
import uk.tryzub.entity.Habitation;
import uk.tryzub.entity.Review;
import uk.tryzub.entity.User;
import uk.tryzub.entity.Work;

/**
 *
 * @author tszin
 */
@ManagedBean
@SessionScoped
public final class WorkHelper implements Serializable {

    private ArrayList<Work> currentWorkList; //заповнюється автоматично при створенні обєкту

    private Work workNew;

    public WorkHelper() {

        //делаем это (заполняем лист) при загрузке страницы
        //fillOrganizationsListAll();
    }

    /*creating new HAbitation for adding to DB*/
    @PostConstruct
    public void init() {
        workNew = new Work();
    }

    public void setNewWork() {
        this.workNew = new Work();
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

  
    

    public ArrayList<Work> getCurrentWorkList() {
        return currentWorkList;
    }

    public void fillWorksListAll(/*String section*/) {
        final Session session = HibernateUtil.getSession();

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Work");
                currentWorkList = (ArrayList<Work>) q.list();

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
    public void fillWorksByType() {
        Map<String, String> params
                = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Work where type = " + params.get("type")); // получение окончания с параметров
                currentWorkList = (ArrayList<Work>) q.list();

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

    public String addWork() {

        //get all existing value but set "editable" to false 
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {

                workNew.setIdwork(null); // добавить новую запись, а не изменить существующую
                workNew.setUser(loginView.getAuthenticatedUser());
                workNew.setType(1);

                session.save(workNew);

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

    public Work getWorkNew() {
        return workNew;
    }

    public void setWorkNew(Work workNew) {
        this.workNew = workNew;
    }
    
    
    
  
}
