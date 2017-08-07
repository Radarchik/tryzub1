/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.controllers;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import uk.tryzub.entity.HibernateUtil;
import uk.tryzub.entity.Post;


/**
 *
 * @author tszin
 */
public class PostHelper {

    public PostHelper() {

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

}
