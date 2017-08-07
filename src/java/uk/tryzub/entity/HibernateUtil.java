/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.entity;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author tszin
 */
public class HibernateUtil {

    private static final ThreadLocal<Session> threadLocal = new ThreadLocal();
    private static  SessionFactory sessionFactory;

    private static SessionFactory configureSessionFactory() {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.

            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        return sessionFactory;
    }

    static {
    try {
      sessionFactory = configureSessionFactory();
    } catch (Exception e) {
      System.err.println("%%%% Error Creating SessionFactory %%%%");
      e.printStackTrace();
    }
  }

  private HibernateUtil() {
  }

  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  public static Session getSession() throws HibernateException {
    Session session = threadLocal.get();

    if (session == null || !session.isOpen()) {
      if (sessionFactory == null) {
        rebuildSessionFactory();
      }
      session = (sessionFactory != null) ? sessionFactory.openSession() : null;
      threadLocal.set(session);
    }

    return session;
  }

  public static void rebuildSessionFactory() {
    try {
      sessionFactory = configureSessionFactory();
    } catch (Exception e) {
      System.err.println("%%%% Error Creating SessionFactory %%%%");
      e.printStackTrace();
    }
  }

  public static void closeSession() throws HibernateException {
    Session session = (Session) threadLocal.get();
    threadLocal.set(null);

    if (session != null) {
      session.close();
    }
  }
}