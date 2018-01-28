package uk.tryzub.validators;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("uk.tryzub.validators.PasswordValidator")
public class PasswordValidator implements Validator {

    private static final String PASSWORD_PATTERN = "^([a-zA-Z0-9@*#]{5,15})$";

    /**
     * Password matching expression. Match all alphanumeric character and predefined wild characters. 
   * Password must consists of at least 6 characters and not more than 15 characters..
     */
    private final Pattern pattern;
    private Matcher matcher;

    public PasswordValidator() {
        pattern = Pattern.compile(PASSWORD_PATTERN);

    }


    /* inputPassword initialise this field, after confirmPass use it*/
    private String password = null;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        // System.out.println(value.toString()+" "+value.toString().length());
        matcher = pattern.matcher(value.toString());

        if (value.toString().length() < 6) {

            ResourceBundle bundle = ResourceBundle.getBundle("uk.tryzub.nls.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
            FacesMessage message = new FacesMessage("Пароль занадто короткий");
            //FacesMessage message = new FacesMessage(bundle.getString("login_length_error")); 
            //достать с локали. Также локаль можно достать на жсф странице validatorMessage
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        } else if (!matcher.matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Пароль не відповідає вказаним вимогам безпеки", null));
        }

        
        /**
         * if password ==null it means password not yet initialised
         * else !=null it means password was initialised, and confirmPasword can compare with it.
         * After "procedures" set password to null for next cycle.
         * So this validation we can use either for password or for confirmPasword
         * Подумати!!!/
        
        
        /* if (password == null) {
        setPassword(value.toString());
        } else {
        if (!password.equals(value.toString())) {
        FacesMessage message = new FacesMessage("Confirm password does not match password");
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        setPassword(null);
        throw new ValidatorException(message);
        }
        setPassword(null);
        
        }   */
  
    }

    
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
