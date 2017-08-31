package uk.tryzub.beans;

import java.io.Serializable;
import java.util.Random;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;

import uk.tryzub.entity.User;

@ManagedBean
@SessionScoped
public class RegisterView implements Serializable {

    private static final long serialVersionUID = 1685823449195612778L;

    private static Logger log = Logger.getLogger(RegisterView.class.getName());

    @Inject
    private UserEJB userEJB;

    private String username;
    private String password;
    private String confirmPassword;
    private String email;

    private int a;
    private int b;

    private String answer;

    private boolean sex = false; //true - man; false - woman 

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    private String city;
    private String realname;

    public void validateCheckBot(ComponentSystemEvent event) {

        FacesContext facesContext = FacesContext.getCurrentInstance();

        UIComponent components = event.getComponent();

        // get answer
        UIInput uiInputAnswer = (UIInput) components.findComponent("inputUserBot");
        String answer = uiInputAnswer.getLocalValue() == null ? "" : uiInputAnswer.getLocalValue().toString();
        String answerId = uiInputAnswer.getClientId();

        if (answer==null||answer.equals("")||a + b != Integer.parseInt(answer)) {
            FacesMessage msg = new FacesMessage("Неправильна відповідь");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(answerId, msg);
            facesContext.renderResponse();
        }

    }

    public void validatePassword(ComponentSystemEvent event) {

        FacesContext facesContext = FacesContext.getCurrentInstance();

        UIComponent components = event.getComponent();

        // get password
        UIInput uiInputPassword = (UIInput) components.findComponent("password");
        String password = uiInputPassword.getLocalValue() == null ? "" : uiInputPassword.getLocalValue().toString();

        // get confirm password
        UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmpassword");
        String confirmPassword = uiInputConfirmPassword.getLocalValue() == null ? ""
                : uiInputConfirmPassword.getLocalValue().toString();
        String confirmPasswordId = uiInputPassword.getClientId();
        // Let required="true" do its job.
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            return;
        }

        if (!password.equals(confirmPassword)) {
            FacesMessage msg = new FacesMessage("Confirm password does not match password");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(confirmPasswordId, msg);
            facesContext.renderResponse();
        }

    }

    public String register() {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        /*установка пола(sex)*/
        byte x = 0;
        if (sex == true) {
            x = 1;
        }
        user.setSex(x);

        /*установка аватара*/
        if (sex == true) {
            user.setAvatar("/member/boy.jpg");
        } else {
            user.setAvatar("/member/girl.jpg");
        }

        userEJB.createUser(user);
        log.info("New user created with username: " + username + " and email: " + email);
        return "member";
    }

    public void generateChecks() {
        Random r = new Random();
        a = r.nextInt(9);
        b = r.nextInt(9);

    }

  
    public Integer getA() {
        return a;
    }

    public void setA(Integer a) {
        this.a = a;
    }

    public Integer getB() {
        return b;
    }

    public void setB(Integer b) {
        this.b = b;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

}
