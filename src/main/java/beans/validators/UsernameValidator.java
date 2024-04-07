package beans.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("usernameValidator")
public class UsernameValidator implements Validator {
	
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		String username = (String) value;
		
		if (username != null && !username.matches("^[a-zA-Z].*")) {
            FacesMessage message = new FacesMessage("Username must start with a letter");
            throw new ValidatorException(message);
        }
	}
}
