/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.beans;

/**
 *
 * @author tszin
 */

import javax.ejb.Remote;
import uk.tryzub.entity.User;
 
@Remote
public interface UserEJB {
    public void createUser(User user);
    public User findUserByName(String username);
   //add business method declarations
}