/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.adminka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;


/**
 *
 * @author tszin
 */
@ManagedBean(eager = false)
@RequestScoped
public class Administration {

    public void getRegisteredBeans() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> map = facesContext.getExternalContext().getSessionMap();

        System.out.println("Session object");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
            //Object [] arr = m.values().toArray();

        }
        
        System.out.println("Application object");
                Map<String, Object> map1 = facesContext.getExternalContext().getApplicationMap();

        for (Map.Entry<String, Object> entry : map1.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue()+ " size : ");
            //Object [] arr = m.values().toArray();

        }

    }

}
