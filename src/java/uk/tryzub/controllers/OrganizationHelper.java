/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import uk.tryzub.entity.HibernateUtil;
import uk.tryzub.entity.Organization;

/**
 *
 * @author tszin
 */
@ManagedBean(eager = true)
@SessionScoped
public class OrganizationHelper implements Serializable {

    private ArrayList<Organization> currentOrganizationList;
    // private int totalBooksCount = currentOrganizationList.size(); // общее кол-во книг (не на текущей странице, а всего), нажно для постраничности
    private int booksOnPage = 2;
    private ArrayList<Integer> pageNumbers = new ArrayList<Integer>(); // общее кол-во книг (не на текущей странице, а всего), нужно для постраничности

    public OrganizationHelper() {
        getOrganizations();
    }

    public ArrayList<Organization> getCurrentOrganizationList() {
        return currentOrganizationList;
    }

    public void getOrganizations(/*String section*/) {
        final Session session = HibernateUtil.getSession();

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Organization");
                currentOrganizationList = (ArrayList<Organization>) q.list();

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

    public void getOrganizations(String y) {
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Organization where section = " + y);
                currentOrganizationList = (ArrayList<Organization>) q.list();

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

    private void fillPageNumbers(int totalBooksCount, int booksCountOnPage) {

        int pageCount = booksCountOnPage > 0 ? ((totalBooksCount / booksCountOnPage) + 1) : 0;

        pageNumbers.clear();
        for (int i = 1; i <= pageCount; i++) {
            pageNumbers.add(i);
        }

    }

    public ArrayList<Integer> getPageNumbers() {
        return pageNumbers;
    }

    public void setPageNumbers(ArrayList<Integer> pageNumbers) {
        this.pageNumbers = pageNumbers;
    }

    public void fillOrganizationsByGenre() {

        getOrganizations("\"kafe\"");

    }

    public void fillOrganizationsByGenre1() {

        getOrganizations("\"church\"");

    }

    public void fillOrganizationsByGenre3() {

        getOrganizations("\"embassy\"");

    }


}
