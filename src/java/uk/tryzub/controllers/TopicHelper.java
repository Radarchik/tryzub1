/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import uk.tryzub.entity.HibernateUtil;
import uk.tryzub.entity.Organization;
import uk.tryzub.entity.Post;
import uk.tryzub.entity.Topic;
import uk.tryzub.entity.User;

/**
 *
 * @author tszin
 */
@ManagedBean
@SessionScoped
public final class TopicHelper implements Serializable {

    private ArrayList<Topic> currentTopicList; //заповнюється автоматично при створенні обєкту

    private Topic selectedTopic;
    private String topicName;
    private String text;
    private User authentUser;

    public TopicHelper() {
        fillTopics();
    }

    public void fillTopics(/*String section*/) {
        final Session session = HibernateUtil.getSession();

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Topic");
                currentTopicList = (ArrayList<Topic>) q.list();

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

    public void fillSelectedTopic(String id) {
        final Session session = HibernateUtil.getSession();

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                int topicid = Integer.parseInt(id);
                selectedTopic = (Topic) session.get(Topic.class, topicid);

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

    public String addTopic() {

        //get all existing value but set "editable" to false 
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                Topic topic = new Topic();
                topic.setName(topicName);
                topic.setUser(authentUser);
                String text40 = text.substring(0, 40);
                topic.setLast(text40);
                topic.setTopicid(null);

               session.save(topic);
                /*    transaction.commit();
                transaction.begin();*/

                //сохранить первый пост
                Post post = new Post();
                post.setText(text);
                post.setTopic(topic);
                post.setUser(authentUser);
                post.setPostid(null);
                session.save(post);

                transaction.commit();
                fillTopics(); //for uploading new topic

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

    public ArrayList<Topic> getCurrentTopicList() {
        return currentTopicList;
    }

    public void setCurrentTopicList(ArrayList<Topic> currentTopicList) {
        this.currentTopicList = currentTopicList;
    }

    public Topic getSelectedTopic() {
        return selectedTopic;
    }

    public void setSelectedTopic(Topic selectedTopic) {
        this.selectedTopic = selectedTopic;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
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

}
