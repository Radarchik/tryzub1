/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.controllers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import uk.tryzub.entity.HibernateUtil;
import uk.tryzub.entity.Post;
import uk.tryzub.entity.User;
import uk.tryzub.controllers.DB_Update_Quantity_Likes;

/**
 *
 * @author tszin
 */
@ManagedBean(eager = true)
@ApplicationScoped
public final class TimerController implements Serializable {

    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/tryzub?zeroDateTimeBehavior=convertToNull";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
   // private Date currentDate = new Date();
    private TimerTask timerSetQuantityOfLikes;
    private Timer timerForLikesQuantityUpdates;

    public TimerController() {

        //currentDate = DateUtils.addMinutes(currentDate, 3);
        System.out.println( "Timer for likes is started!!!");

        timerSetQuantityOfLikes = new TimerTask() {
            @Override
            public void run() {
                renewQuantityOfLikesHandle();
            }
        };

        timerForLikesQuantityUpdates = new Timer();
        timerForLikesQuantityUpdates.schedule(timerSetQuantityOfLikes,  86400000);
    }

    //for handle renewing
    public void renewQuantityOfLikesHandle() {
        try {

            DB_Update_Quantity_Likes.renewQuantityOfLikes();
        } catch (SQLException ex) {
            Logger.getLogger(TimerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public TimerTask getTimerSetQuantityOfLikes() {
        return timerSetQuantityOfLikes;
    }

    public void setTimerSetQuantityOfLikes(TimerTask timerSetQuantityOfLikes) {
        this.timerSetQuantityOfLikes = timerSetQuantityOfLikes;
    }

    public Timer getTimerForLikesQuantityUpdates() {
        return timerForLikesQuantityUpdates;
    }

    public void setTimerForLikesQuantityUpdates(Timer timerForLikesQuantityUpdates) {
        this.timerForLikesQuantityUpdates = timerForLikesQuantityUpdates;
    }

}
