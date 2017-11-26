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
public final class PublicationHelper implements Serializable {

    private ArrayList<Publication> currentPublicationList; //заповнюється автоматично при створенні обєкту
    private ArrayList<Comment> currentCommentList;

    private String message;

    /*this field used for adding publication and for selection publication and for adding comments */
    private Publication selectedPublication;

    public PublicationHelper() {

        //делаем это (заполняем лист) при загрузке страницы
        //fillOrganizationsListAll();
    }

    /*creating new HAbitation for adding to DB*/
    public void initNewPublication() {
        selectedPublication = new Publication();
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

    public void fillPublicationsListAll(/*String section*/) {
        final Session session = HibernateUtil.getSession();

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Publication");
                currentPublicationList = (ArrayList<Publication>) q.list();

                transaction.commit();
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
            currentCommentList = null;
        }

    }

    public String addPublication() {

        //get all existing value but set "editable" to false 
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {

                selectedPublication.setIdpublication(null); // добавить новую запись, а не изменить существующую
                selectedPublication.setUser(loginView.getAuthenticatedUser());

                session.save(selectedPublication);

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

    public void setChozenPublication(String id) {
        final Session session = HibernateUtil.getSession();

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                int idpublication = Integer.parseInt(id);
                Publication selectedPubl = (Publication) session.get(Publication.class, idpublication);

                this.currentPublicationList.clear();
                this.currentPublicationList.add(selectedPubl);
                this.selectedPublication = selectedPubl;

                transaction.commit();

                message = "";
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
            fillComments(id);   //fill commentsList for this publication
        }

    }

    public void setNewPhoto(String newPhoto) {
        if (selectedPublication != null) {
            selectedPublication.setPhoto(newPhoto);
        }
    }

    public String addComment() throws IOException {

        //get all existing value but set "editable" to false 
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {

                Comment comment = new Comment();
                comment.setMessage(message);

                comment.setUser(loginView.getAuthenticatedUser());
                comment.setPublication(selectedPublication);
                comment.setIdcomment(null); // добавить новую запись, а не изменить существующую
                session.save(comment);

                transaction.commit();

                //for reload comments
                //selectedPublication = (Publication) session.get(Publication.class, selectedPublication.getIdpublication());
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect(context.getRequestContextPath() + "/index.xhtml?id=" + selectedPublication.getIdpublication());

        }
        return null;
    }

    public void fillComments(String idPublication) {
        final Session session = HibernateUtil.getSession();

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Comment where idpublication = " + idPublication);
                currentCommentList = (ArrayList<Comment>) q.list();

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

    public void changeCommentRating() {
        final Session session = HibernateUtil.getSession();

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                String nameUserValuer = loginView.getAuthenticatedUser().getUsername();
                User userValuer = (User) session.get(User.class, nameUserValuer);

                if (userValuer.getQuantity() > 0) {

                    Map<String, String> params
                            = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

                    int commentId = Integer.parseInt(params.get("commentid"));

                    String username = params.get("username");
                    int rating = Integer.parseInt(params.get("rating"));

                    Comment comment = (Comment) session.get(Comment.class, commentId);
                    comment.setRating(comment.getRating() + rating);

                    User user = (User) session.get(User.class, username);
                    user.setReputation(user.getReputation() + rating);
                    
                    userValuer.setQuantity(userValuer.getQuantity()-1);

                    session.update(comment);
                    session.update(user);
                    session.update(userValuer);
                    transaction.commit();

                    /*для обновления аджаксового */
                    fillComments(comment.getPublication().getIdpublication().toString());
                    // fillSelectedPosts(comment.getPublication().getTopicid().toString());                
                } else {
                    FacesMessage message1 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Упсс", "Кількість лайків на сьогодні вичерпано :-(");
                    RequestContext.getCurrentInstance().showMessageInDialog(message1);

                }
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();

        }

    }

    public ArrayList<Publication> getCurrentPublicationList() {
        return currentPublicationList;
    }

    public Publication getSelectedPublication() {
        return selectedPublication;
    }

    public void setSelectedPublication(Publication selectedPublication) {
        this.selectedPublication = selectedPublication;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Comment> getCurrentCommentList() {
        return currentCommentList;
    }

    public void setCurrentCommentList(ArrayList<Comment> currentCommentList) {
        this.currentCommentList = currentCommentList;
    }

}
