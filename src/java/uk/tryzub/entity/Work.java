package uk.tryzub.entity;
// Generated 09-Sep-2017 20:26:26 by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Work generated by hbm2java
 */
public class Work  implements java.io.Serializable {



     private Integer idwork;
     private User user;
     private Date date;
     private int type;
     private String contact;
     private String address;
     private String description;
     private String name;
     private Float salary;
     private String workshortname;

    public Work() {
    }

	
    public Work(User user, Date date, int type, String contact, String address, String description) {
        this.user = user;
        this.date = date;
        this.type = type;
        this.contact = contact;
        this.address = address;
        this.description = description;
    }
    public Work(User user, Date date, int type, String contact, String address, String description, String name, Float salary, String workshortname) {
       this.user = user;
       this.date = date;
       this.type = type;
       this.contact = contact;
       this.address = address;
       this.description = description;
       this.name = name;
       this.salary = salary;
       this.workshortname = workshortname;
    }
   
    public Integer getIdwork() {
        return this.idwork;
    }
    
    public void setIdwork(Integer idwork) {
        this.idwork = idwork;
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
    public int getType() {
        return this.type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    public String getContact() {
        return this.contact;
    }
    
    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public Float getSalary() {
        return this.salary;
    }
    
    public void setSalary(Float salary) {
        this.salary = salary;
    }
    public String getWorkshortname() {
        return this.workshortname;
    }
    
    public void setWorkshortname(String workshortname) {
        this.workshortname = workshortname;
    }




}


