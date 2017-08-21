/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
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
import uk.tryzub.entity.Review;
import uk.tryzub.entity.User;

/**
 *
 * @author tszin
 */
@ManagedBean(eager = true)
@SessionScoped
public final class OrganizationHelper implements Serializable {
    
    private ArrayList<Organization> currentOrganizationList; //заповнюється автоматично при створенні обєкту
    private Organization selectedOrgStrd;
    private String selectedSection;

    private boolean editable = false;
    private boolean addable = false;
    
    public OrganizationHelper() {
        getOrganizations();
    }
    
    /*При выборе "показать выдгуки" организации currentOrganizationList заповнюэться selectedOrgStrd
    for rendering 
    */
    public boolean isReviewShown(){
        return currentOrganizationList.size()==1
       && Objects.equals(currentOrganizationList.get(0).getIdorganization(), selectedOrgStrd.getIdorganization());
    }

    public ArrayList<Organization> getCurrentOrganizationList() {
        return currentOrganizationList;
    }
    
    /*For review - show only current organization*/
     public void setSelectedOrgInCurrentList() {
         currentOrganizationList.clear();
         currentOrganizationList.add(selectedOrgStrd);
     
     }
    
    public void getOrganizations(/*String section*/) {
        final Session session = HibernateUtil.getSession();
        
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Organization");
                currentOrganizationList = (ArrayList<Organization>) q.list();
                
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
    public void fillOrganizationsBySection() {
        Map<String, String> params
                = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Organization where section " + params.get("section")); // получение окончания с параметров
                currentOrganizationList = (ArrayList<Organization>) q.list();
                
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
    
    public void getOrganizations(String y) {
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Organization where section " + y);
                currentOrganizationList = (ArrayList<Organization>) q.list();
                
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
    
    public void setNewPhoto(String newPhoto) {
        if (selectedOrgStrd != null) {
            selectedOrgStrd.setPhoto(newPhoto);
        }        
    }

    public String saveAction1() {

        //get all existing value but set "editable" to false 
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                /*       // The real work is here
                Organization org = (Organization) session.get(Organization.class, selectedOrgStrd.getIdorganization());

                // org.setAddress(selectedOrgExt.getAddress());
                //org.setContact(selectedOrgExt.getContact());
                org.setDescription(selectedOrgExt.getDescription());*/
                
                session.merge(selectedOrgStrd);
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
    
    public String addOrganization() {

        //get all existing value but set "editable" to false 
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
               
                
                /*получать надо будет автора из сессии (кто добавил). Пока moderator */
                User user = (User) session.get(User.class, "moderator");
         
                selectedOrgStrd.setUser(user);
                
                selectedOrgStrd.setIdorganization(null); // добавить новую запись, а не изменить существующую
                session.save(selectedOrgStrd);
                
                       
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
    
    public void setNewOrg() {        
        selectedOrgStrd = new Organization();
        
    }

    /*
   public String editAction() {
   selectedOrgExt = new OrganizationExt();
   for (Organization organ : currentOrganizationList) {
   if (organ.getIdorganization()==selectedOrgStrd.getIdorganization()) {
   selectedOrgExt.setAddress(organ.getAddress());
   selectedOrgExt.setContact(organ.getContact());
   selectedOrgExt.setDescription(organ.getDescription());
   selectedOrgExt.setPhoto(organ.getPhoto());
   selectedOrgExt.setSection(organ.getSection());
   selectedOrgExt.setName(organ.getName());
   }
   }
   
   selectedOrgExt.setEditable(true);
   return null;
   }*/
    public String getSelectedSection() {
        return selectedSection;
    }
    
    public void setSelectedSection(String selectedSection) {
        this.selectedSection = selectedSection;
    }
    
    public Organization getSelectedOrgStrd() {
        return selectedOrgStrd;
    }
    
    public void setSelectedOrgStrd(Organization selectedOrgStrd) {
        this.selectedOrgStrd = selectedOrgStrd;
    }
    
    
    public boolean isEditable() {
        return editable;
    }
    
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
    public boolean isAddable() {
        return addable;
    }
    
    public void setAddable(boolean addable) {
        this.addable = addable;
    }
}
