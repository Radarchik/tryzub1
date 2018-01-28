package uk.tryzub.beans;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

import uk.tryzub.entity.User;

@ManagedBean(eager = true)
@SessionScoped
public class LoginView implements Serializable {

    private static final long serialVersionUID = 3254181235309041386L;

    private final static Logger log = Logger.getLogger(LoginView.class.getName());

    @Inject
    private UserEJB userEJB;

    private String username;
    private String password;

    private User user;

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.login(username, password);
        } catch (ServletException e) {
            context.addMessage("LoginForm:LoginPanelGrid:password", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed!", null));
            return "signin";
        }

        Principal principal = request.getUserPrincipal();
        this.user = userEJB.findUserByName(principal.getName());
        log.info("Authentication done for user: " + principal.getName());

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("User", user);

        if (request.isUserInRole("member")) {
            try {
                //  RequestContext context1 = RequestContext.getCurrentInstance();
                //  context1.execute("PF('dlgWork').show();");
                //RequestContext.getCurrentInstance().update("formLogin");
                
                //перезагрузка страницы, чтобы обновились все доступы для зарегистр. юзера
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
               
            } catch (IOException ex) {
                Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
            } 
            return "/?faces-redirect=true";
        } else if (request.isUserInRole("moderator")) {
          //  RequestContext context1 = RequestContext.getCurrentInstance();
          //  context1.execute("PF('dlgWork').show();");
            return "/moderka/moderatorka?faces-redirect=true";
        } else {
            return "/forum"; // потом перенаправлять соотв на страницу админа и модератора
        }
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            this.user = null;
            request.logout();
            // clear the session
            ((HttpSession) context.getExternalContext().getSession(false)).invalidate();
        } catch (ServletException e) {
            log.log(Level.SEVERE, "Failed to logout user!", e);
        }

        return "/index?faces-redirect=true";
    }

    public User getAuthenticatedUser() {
        return user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
