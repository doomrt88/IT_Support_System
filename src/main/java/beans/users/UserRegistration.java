package beans.users;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import models.dto.BaseResponse;
import models.dto.RegistrationFormDTO;
import service.UserService;
import utility.Config;

@Named("userRegistrationBean")
@RequestScoped
public class UserRegistration {

    private UserService userService = null;
    private RegistrationFormDTO registrationForm;
    private BaseResponse response;
    
    public UserRegistration() {
        userService = new UserService(); 
        registrationForm = new RegistrationFormDTO();
        response = new BaseResponse();
    }

    public void register() {
        if (!validate()) {
            return;
        }

        boolean success = userService.registerUser(registrationForm.getUsername(), registrationForm.getPassword(), registrationForm.getFirstName(), registrationForm.getLastName(), Config.getDefaultRoleId());
        if (success) {
        	response.setResult(success);
            response.setMessage("Registration successful! Welcome, " + registrationForm.getFirstName() + " " + registrationForm.getLastName() + "!");
            registrationForm = new RegistrationFormDTO();
        } else {
        	response.setMessage("Registration failed. Please try again.");
        }
    }

    public RegistrationFormDTO getRegistrationForm() {
        return registrationForm;
    }

    public void setRegistrationForm(RegistrationFormDTO registrationForm) {
        this.registrationForm = registrationForm;
    }
    
    public BaseResponse getResponse() {
        return response;
    }

    public void setResponse(BaseResponse response) {
        this.response = response;
    }
    
    private boolean validate() {
    	String errMsg = "";
    	if (registrationForm.getUsername() != null && registrationForm.getUsername().indexOf(" ") > -1) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Required field", "Username must not have spaces.");
            FacesContext.getCurrentInstance().addMessage("registrationForm:username", message);
            errMsg = message.getDetail();
        }
    	
    	if (registrationForm.getPassword() == null || registrationForm.getPassword().trim().isEmpty()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Required field", "Password is required.");
            FacesContext.getCurrentInstance().addMessage("registrationForm:password", message);
            errMsg = message.getDetail();
        }
    	
    	if (registrationForm.getPassword() == null || !registrationForm.getPassword().equals(registrationForm.getConfirmPassword())) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password mismatch", "The passwords do not match.");
            FacesContext.getCurrentInstance().addMessage("registrationForm:confirmPassword", message);
            errMsg = message.getDetail();
        }
    	
    	if(userService.userExists(0, registrationForm.getUsername())) {
    		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unique field", "Username is not available.");
            FacesContext.getCurrentInstance().addMessage("registrationForm:username", message);
            errMsg = message.getDetail();
    	}
    	
    	return errMsg.isEmpty();
    	
    }
}