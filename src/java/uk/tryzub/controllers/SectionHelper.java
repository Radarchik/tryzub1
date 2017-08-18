/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.controllers;

import java.util.List;
import java.util.SortedSet;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import uk.tryzub.entity.HibernateUtil;
import uk.tryzub.entity.Post;
import uk.tryzub.entity.Topic;

/**
 *
 * @author tszin
 */
public class TopicHelper {

    public TopicHelper() {

    }

    public List getTopics(/*String section*/) {
        final Session session = HibernateUtil.getSession();
        List<Topic> topicList = null;

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Topic");
                topicList = (List<Topic>) q.list();
                
                transaction.commit();
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
        }

        return topicList;
    }
    
    

     public Post getLastPostFromTopic(Topic topic) {
        final Session session = HibernateUtil.getSession();
        Post post = null;

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                 post = (Post) topic.getPosts().toArray()[0];
                 
                transaction.commit();
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
        }

        return post;
    }


}
