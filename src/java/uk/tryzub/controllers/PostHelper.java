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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import uk.tryzub.entity.HibernateUtil;
import uk.tryzub.entity.Post;
import uk.tryzub.entity.Topic;
import uk.tryzub.entity.User;

/**
 *
 * @author tszin
 */
@ManagedBean
@SessionScoped
public class PostHelper implements Serializable {

    public PostHelper() {

    }

    private ArrayList<Post> currentPostList;

    private String text;

    private User authentUser;
    private Topic chosenTopic;

    @ManagedProperty(value = "#{topicHelper}")
    private TopicHelper topicHelper;

    //Обязательный сеттер для инъекции
    public void setTopicHelper(TopicHelper topicHelper) {
        this.topicHelper = topicHelper;
    }

    public String createQuote(Post p) {

        StringBuilder quote = new StringBuilder("<blockquote><em><strong>");
        quote.append(p.getUser().getUsername());
        quote.append(" </strong>");
        quote.append("пише:<br/>");
        quote.append(p.getText());
        quote.append("</em></blockquote>");

        return quote.toString();
    }

    public void changeRating() {
        final Session session = HibernateUtil.getSession();

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Map<String, String> params
                        = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
                
                int postid = Integer.parseInt(params.get("postid"));
                String username = params.get("username");
                int rating = Integer.parseInt(params.get("rating"));

                Post post = (Post) session.get(Post.class, postid);                
                post.setRating(post.getRating()+rating);
                
                User user = (User) session.get(User.class, username);
                user.setReputation(user.getReputation()+rating);
                session.update(post);
                session.update(user);
                transaction.commit();
                
                /*для обновления аджаксового */
                fillSelectedPosts(post.getTopic().getTopicid().toString());
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
        }

    }

    public void fillSelectedPosts(String topicid) {
        final Session session = HibernateUtil.getSession();

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here

                Query q = session.createQuery("from Post where topicid = " + topicid);

                currentPostList = (ArrayList<Post>) q.list();

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

    public String addPost() {

        //get all existing value but set "editable" to false 
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                Post post = new Post();

                post.setPostid(null);
                post.setTopic(chosenTopic);
                post.setUser(authentUser);
                post.setText(text);

                session.save(post);

                transaction.commit();

                /*для обновления списка постов*/
                fillSelectedPosts(chosenTopic.getTopicid().toString());

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

    public List getPosts(String y) {
        final Session session = HibernateUtil.getSession();
        List<Post> postList = null;
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Post where topicid = " + y);
                postList = (List<Post>) q.list();

                transaction.commit();
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
        }
        return postList;
    }

    public List getPosts(/*String section*/) {
        final Session session = HibernateUtil.getSession();
        List<Post> postList = null;

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Post");
                postList = (List<Post>) q.list();

                transaction.commit();
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
        }

        return postList;
    }

    public Topic getChosenTopic() {
        return chosenTopic;
    }

    public void setChosenTopic(Topic chosenTopic) {
        this.chosenTopic = chosenTopic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthentUser() {
        return authentUser;
    }

    public void setAuthentUser(User authentUser) {
        this.authentUser = authentUser;
    }

    public ArrayList<Post> getCurrentPostList() {
        return currentPostList;
    }

    public void setCurrentPostList(ArrayList<Post> currentPostList) {
        this.currentPostList = currentPostList;
    }
}
