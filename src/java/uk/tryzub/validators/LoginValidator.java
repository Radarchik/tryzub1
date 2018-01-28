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
import org.hibernate.Session;
import org.hibernate.Transaction;
import uk.tryzub.beans.UserEJBImpl;
import uk.tryzub.entity.HibernateUtil;
import uk.tryzub.entity.User;

@FacesValidator("uk.tryzub.validators.LoginValidator")
public class LoginValidator implements Validator {

    private static final String LOGIN_PATTERN = "^[a-zA-Z0-9_-]{2,10}$";

   /** We begin by telling the parser to find the beginning of the string (^), 
    * followed by any lowercase letter (a-z), number (0-9), an underscore, or a hyphen. 
    * Next, {3,10} makes sure that are at least 3 of those characters, but no more than 10. 
    * Finally, we want the end of the string ($).
    */
	private final Pattern pattern;
	private Matcher matcher;
        
 

	public LoginValidator(){
		  pattern = Pattern.compile(LOGIN_PATTERN);
	}
    
    
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
     //System.out.println(value.toString()+" "+value.toString().length());
      
      matcher = pattern.matcher(value.toString());
      
        if (value.toString().length() < 5) {
            
            ResourceBundle bundle = ResourceBundle.getBundle("uk.tryzub.nls.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
            FacesMessage message = new FacesMessage(bundle.getString("login_length_error"));
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
        
      else if(!matcher.matches()){
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                                "Login must consist of 3-10 symbols: any letter (a-z, A-Z), number (0-9), an underscore, or a hyphen.", null));
		}
        
       else if (findUserByName(value.toString()) != null) {
            FacesMessage message = new FacesMessage("User with this login already exists");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
             throw new ValidatorException(message);
        }
    }
    
    
    
    private static User findUserByName(String username) {
        final Session session = HibernateUtil.getSession();
        User user = null;
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                user = (User) session.get(User.class, username);
                /* Query q = session.createQuery("from User where username = boriska");
                   
                   
                   Query query = session.createQuery("from User where username = :code ");
                     query.setParameter("code", username);
                     user = (User)query.list();
                   
                   //user must be only one, because constraint in DB for unique field
                   user = (User) q.list();*/
                transaction.commit();

            } catch (Exception ex) {
                // getSingleResult throws NoResultException in case there is no user in DB
                // ignore exception and return NULL for user instead
                transaction.rollback();

            }
        } finally {
            HibernateUtil.closeSession();
            return user;
        }

    }

    
}
