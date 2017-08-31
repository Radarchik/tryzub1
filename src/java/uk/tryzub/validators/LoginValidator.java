package uk.tryzub.validators;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import uk.tryzub.beans.UserEJB;

@FacesValidator("uk.tryzub.validators.LoginValidator")
public class LoginValidator implements Validator {

    private static final String LOGIN_PATTERN = "^(?![0-9]{6})[0-9a-zA-Z]{4,10}$";

   /** matches a six character  
    * that has to consist of numbers and letters with at least one letter in it.
    */
	private Pattern pattern;
	private Matcher matcher;
        
    @Inject
    private UserEJB userEJB;

	public LoginValidator(){
		  pattern = Pattern.compile(LOGIN_PATTERN);
	}
    
    
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
      // System.out.println(value.toString()+" "+value.toString().length());
      
      matcher = pattern.matcher(value.toString());
      
        if (value.toString().length() < 5) {
            
            ResourceBundle bundle = ResourceBundle.getBundle("uk.tryzub.nls.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
            FacesMessage message = new FacesMessage(bundle.getString("login_length_error"));
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
        
      else if(!matcher.matches()){
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Логін не відповідає вказаним рамкам", null));
		}
        
       else if (userEJB.findUserByName(value.toString()) != null) {
            FacesMessage message = new FacesMessage("User with this login already exists");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
             throw new ValidatorException(message);
        }
    }
    
}
