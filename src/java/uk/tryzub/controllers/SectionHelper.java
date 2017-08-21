/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import uk.tryzub.entity.HibernateUtil;
import uk.tryzub.entity.Post;
import uk.tryzub.entity.Section;
import uk.tryzub.entity.Topic;

/**
 *
 * @author tszin
 */
@ManagedBean
@SessionScoped
public class SectionHelper implements Serializable, Converter {

    private List<SelectItem> selectItems = new ArrayList<SelectItem>();
    private List<Section> list;
    private Map<Integer, Section> map;

    public SectionHelper() {

    }

    @PostConstruct
    public void init() {
        list = getSections();
        map = new HashMap<>();

        for (Section section : list) {
            map.put(section.getSectionid(), section);
            selectItems.add(new SelectItem(section, section.getSectionname()));
        }
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public List getSections(/*String section*/) {
        final Session session = HibernateUtil.getSession();
        List<Section> sectionList = null;

        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                Query q = session.createQuery("from Section");
                sectionList = (List<Section>) q.list();

                transaction.commit();
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
        }

        return sectionList;
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

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return map.get(Integer.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Section) value).getSectionid().toString();
    }

}
