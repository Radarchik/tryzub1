package uk.tryzub.entity;
// Generated 10-Aug-2017 20:22:17 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Section generated by hbm2java
 */
public class Section  implements java.io.Serializable {


     private Integer sectionid;
     private String sectionname;
     private Set organizations = new HashSet(0);

    public Section() {
    }

	
    public Section(String sectionname) {
        this.sectionname = sectionname;
    }
    public Section(String sectionname, Set organizations) {
       this.sectionname = sectionname;
       this.organizations = organizations;
    }
   
    public Integer getSectionid() {
        return this.sectionid;
    }
    
    public void setSectionid(Integer sectionid) {
        this.sectionid = sectionid;
    }
    public String getSectionname() {
        return this.sectionname;
    }
    
    public void setSectionname(String sectionname) {
        this.sectionname = sectionname;
    }
    public Set getOrganizations() {
        return this.organizations;
    }
    
    public void setOrganizations(Set organizations) {
        this.organizations = organizations;
    }




}


