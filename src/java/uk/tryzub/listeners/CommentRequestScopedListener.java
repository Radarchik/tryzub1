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
import uk.tryzub.entity.Comment;
import uk.tryzub.entity.HibernateUtil;
import uk.tryzub.entity.Organization;
import uk.tryzub.entity.Publication;
import uk.tryzub.entity.Review;
import uk.tryzub.entity.User;

@ManagedBean
@RequestScoped
public class CommentRequestScopedListener implements Serializable {

    private Publication publForComment;
    private String message;
    private User user;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    

   
   
   public String addComment() {

        //get all existing value but set "editable" to false 
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
               
                Comment comment = new Comment();
                comment.setMessage(message);
             
                comment.setUser(user);
                comment.setPublication(publForComment);
                comment.setIdcomment(null); // добавить новую запись, а не изменить существующую
                session.save(comment);
                transaction.commit();
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
        }

        return null;
        
    }

   
   public Publication getPublForComment() {
        return publForComment;
    }

    public void setPublForComment(Publication publForComment) {
        this.publForComment = publForComment;
    }
   
   
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
