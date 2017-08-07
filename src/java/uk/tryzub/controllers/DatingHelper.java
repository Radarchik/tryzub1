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
import uk.tryzub.entity.Dating;
import uk.tryzub.entity.HibernateUtil;

/**
 *
 * @author tszin
 */
public class DatingHelper {

    public DatingHelper() {

    }

  
    public List getDatings(/*String section*/) {
        final Session session = HibernateUtil.getSession();
        List<Dating> datingList = null;

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Dating order by date desc");
                datingList = (List<Dating>) q.list();

                transaction.commit();
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
        }

        return datingList;
    }

    //INSERT INTO `tryzub`.`dating` (`section`, `date`, `name`, `email`, `city`, `message`) 
    //VALUES ('one', '2017-07-01', 'Petya', 'ptryk@mail.ru', 'Paris', 'Я хочу познайомитися з телочкою');
    public void addDatings(Dating dating) {
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                dating.setId(null); // добавить новую запись, а не изменить существующую
                //session.save(dating);

                Integer i = (Integer) session.save(dating);
                System.out.println("Generated Identifier:" + i);
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

}
