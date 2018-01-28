package uk.tryzub.beans;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.inject.Model;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import uk.tryzub.entity.Groupofuser;
import uk.tryzub.entity.HibernateUtil;
import uk.tryzub.entity.User;
import uk.tryzub.utils.AuthenticationUtils;

@Model
public class UserEJBImpl  implements UserEJB{

    @Override
    public void createUser(User user) {

        //get all existing value but set "editable" to false 
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {

                try {
                    user.setPassword(AuthenticationUtils.encodeSHA256(user.getPassword()));
                } catch (Exception e) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                    e.printStackTrace();
                }

                Groupofuser groupofuser = new Groupofuser();
                groupofuser.setUsername(user.getUsername());
                groupofuser.setGroupname("members");

                session.save(user);
                session.save(groupofuser);

                transaction.commit();
               
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
             ex.printStackTrace();
            }
        } finally {
            HibernateUtil.closeSession();

        }

    }

    @Override
    public User findUserByName(String username) {
        final Session session = HibernateUtil.getSession();
        User user = null;
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                user = (User) session.get(User.class, username);
                /* Query q = session.createQuery("from User where username = boriska");
                   
                   
                   Query query = session.createQuery("from User where username = :code ");
                     query.setParameter("code", username);
                     user = (User)query.list();
                   
                   //user must be only one, because constraint in DB for unique field
                   user = (User) q.list();*/
                transaction.commit();

            } catch (Exception ex) {
                // getSingleResult throws NoResultException in case there is no user in DB
                // ignore exception and return NULL for user instead
                transaction.rollback();

            }
        } finally {
            HibernateUtil.closeSession();
            return user;
        }

    }

   
}
