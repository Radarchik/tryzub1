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
import uk.tryzub.entity.Habitation;
import uk.tryzub.entity.Review;
import uk.tryzub.entity.User;

/**
 *
 * @author tszin
 */
@ManagedBean
@SessionScoped
public final class HabitationHelper implements Serializable {

    private ArrayList<Habitation> currentHabitationList; //заповнюється автоматично при створенні обєкту

    private Habitation habit;

    public HabitationHelper() {

        //делаем это (заполняем лист) при загрузке страницы
        //fillOrganizationsListAll();
    }

    /*creating new HAbitation for adding to DB*/
    @PostConstruct
    public void init() {
        habit = new Habitation();
    }

    public void setNewHabitation() {
        this.habit = new Habitation();
    }

    public ArrayList<Habitation> getCurrentHabitationList() {
        return currentHabitationList;
    }

    public void fillHabitationsListAll(/*String section*/) {
        final Session session = HibernateUtil.getSession();

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Habitation");
                currentHabitationList = (ArrayList<Habitation>) q.list();

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
    public void fillHabitationsByType() {
        Map<String, String> params
                = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Habitation where type = " + params.get("type")); // получение окончания с параметров
                currentHabitationList = (ArrayList<Habitation>) q.list();

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

    public String addHabitation() {

        //get all existing value but set "editable" to false 
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {

                habit.setId(null); // добавить новую запись, а не изменить существующую
                habit.setType(1);

                session.save(habit);

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
    
    
    public ArrayList<String> getImagesForHabitations (String imagesPaths) {
       ArrayList <String> images = new ArrayList<>();
       String[] arr = imagesPaths.split(" ");
       
       Collections.addAll(images, arr); 
        return images;
    }
    
    
     public void setNewPhotosPath(String paths) {
        this.habit.setPhoto(paths);
    }

    public Habitation getHabit() {
        return habit;
    }

    public void setHabit(Habitation habit) {
        this.habit = habit;
    }
}
