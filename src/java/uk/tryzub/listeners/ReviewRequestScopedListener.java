/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.listeners;

/**
 *
 * @author tszin
 */
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.bytecode.stackmap.BasicBlock;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import uk.tryzub.controllers.OrganizationHelper;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.omnifaces.cdi.GraphicImageBean;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import uk.tryzub.beans.LoginView;
import uk.tryzub.entity.HibernateUtil;
import uk.tryzub.entity.Organization;
import uk.tryzub.entity.Review;
import uk.tryzub.entity.User;

@ManagedBean
@RequestScoped
public class ReviewRequestScopedListener implements Serializable {

    private boolean isshown = false;
    private Organization orgForReview;
    private String newComment;
    private User user;


    public String getNewComment() {
        return newComment;
    }

    public void setNewComment(String newComment) {
        this.newComment = newComment;
    }

    

   
   
   public String addReview() {

        //get all existing value but set "editable" to false 
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
               
                   
                
              //  Organization organ = (Organization) session.get(Organization.class, 2);
                Review review = new Review();
                review.setComment(newComment);
              //  review.setOrganization(organ);
                review.setUser(user);
                review.setOrganization(orgForReview);
                review.setIdreview(null); // добавить новую запись, а не изменить существующую
                session.save(review);
                transaction.commit();
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
        }

        return "";
        
    }

   
   public Organization getOrgForReview() {
        return orgForReview;
    }

    public void setOrgForReview(Organization orgForReview) {
        this.orgForReview = orgForReview;
    }
   
    public boolean isIsshown() {
        return isshown;
    }

    public void setIsshown(boolean isshown) {
        this.isshown = isshown;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
