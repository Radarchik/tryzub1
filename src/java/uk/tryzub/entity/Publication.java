package uk.tryzub.entity;
// Generated 09-Sep-2017 20:54:19 by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Publication generated by hbm2java
 */
public class Publication  implements java.io.Serializable {


     private Integer idpublication;
     private User user;
     private Date date;
     private String text;
     private int tryzub;
     private String name;
     private String photo;
     private Set comments = new HashSet(0);

    public Publication() {
    }

	
    public Publication(User user, Date date, String text, int tryzub, String name) {
        this.user = user;
        this.date = date;
        this.text = text;
        this.tryzub = tryzub;
        this.name = name;
    }
    public Publication(User user, Date date, String text, int tryzub, String name, String photo, Set comments) {
       this.user = user;
       this.date = date;
       this.text = text;
       this.tryzub = tryzub;
       this.name = name;
       this.photo = photo;
       this.comments = comments;
    }
   
    public Integer getIdpublication() {
        return this.idpublication;
    }
    
    public void setIdpublication(Integer idpublication) {
        this.idpublication = idpublication;
    }
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    public Date getDate() {
        return this.date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    public String getText() {
        return this.text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    public int getTryzub() {
        return this.tryzub;
    }
    
    public void setTryzub(int tryzub) {
        this.tryzub = tryzub;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getPhoto() {
        return this.photo;
    }
    
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public Set getComments() {
        return this.comments;
    }
    
    public void setComments(Set comments) {
        this.comments = comments;
    }




}


