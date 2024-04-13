package beans.administration;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import models.entity.Role;
import models.entity.User;
import models.dto.UserDTO;
import service.RoleService;
import service.UserService;
import utility.Config;

import org.primefaces.PrimeFaces;

@SuppressWarnings("serial")
@Named("userAdministrationBean")
@RequestScoped
public class UserAdministration implements Serializable {

    private UserService userService = null;
    private RoleService roleService = null;

    private List<Role> roles;
    private UserDTO userForm;

    @PostConstruct
    public void initialize() {
    	userForm = new UserDTO();
        
        userService = new UserService();
        roleService = new RoleService();
        
        roles = roleService.getAllRoles();
    }

    public void openNew() {
        this.userForm = new UserDTO();
    }
    
    public void editUser(UserDTO user) {
		
    	this.userForm.setId(user.getId());
    	this.userForm.setUsername(user.getUsername());
    	this.userForm.setPassword(user.getPassword());
    	this.userForm.setFirstName(user.getFirstName());
    	this.userForm.setLastName(user.getLastName());
    	this.userForm.setRoleId(user.getRoleId());
        
    }

    public UserDTO getUserForm() {
        return userForm;
    }

    public void setUserForm(UserDTO userForm) {
        this.userForm = userForm;
    }
    
    public List<Role> getRoles() {
        return roles;
    }
    
    public List<UserDTO> getUserList() {
        return userService.getAllUsers();
    }
    
    public void saveUser(boolean isRegistration) {
    	if (userForm.getId() > 0) {
    		updateUser();
        } else {
        	createUser(isRegistration);
        }
    }
    
    public void deleteUser(int id) {

    	if(userService.isAllowDelete(id)) {
	        boolean success = userService.deleteUser(id);
	        if (success) {
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User has been deleted"));
	            clearForm();
	        } else {
	        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User deletion failed. Please try again."));
	        }
    	}else {
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Delete is not allowed."));
    	}
    	
        PrimeFaces.current().ajax().update("form:userMessages", "form:usersTable");
    }
    
    public void createUser(boolean isRegistration) {
        if (!validate(isRegistration)) {
            return;
        }

        int roleId = isRegistration ? Config.getDefaultRoleId() : userForm.getRoleId();
        boolean success = userService.registerUser(userForm.getUsername(), userForm.getPassword(), userForm.getFirstName(), userForm.getLastName(), roleId);
        if (success) {
            
            if(isRegistration) {
            	String msg = "Registration successful! Welcome, " + userForm.getFirstName() + " " + userForm.getLastName() + "!";
            	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg));
            }else{
            	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User has been added"));
                //FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('userDialog').hide()");
            }
            
            clearForm();
        } else {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User"));
        }
    }
    
    private void updateUser() {
        if (!validate(false)) {
            return;
        }

        User userToUpdate = new User();
        userToUpdate.setId(userForm.getId());
        userToUpdate.setUsername(userForm.getUsername());
        userToUpdate.setPassword(userForm.getPassword());
        userToUpdate.setFirstName(userForm.getFirstName());
        userToUpdate.setLastName(userForm.getLastName());
        userToUpdate.setRoleId(userForm.getRoleId());

        boolean success = userService.updateUser(userToUpdate);
        if (success) {
            clearForm();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User has been updated"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User update failed. Please try again."));
        }
        
        PrimeFaces.current().ajax().update("form:userMessages", "form:usersTable");
    }

  
    private boolean validate(boolean isRegistration) {
    	if(userForm == null) return false;
    	
    	String errMsg = "";
    	if (userForm.getUsername() != null && userForm.getUsername().indexOf(" ") > -1) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Required field", "Username must not have spaces.");
            FacesContext.getCurrentInstance().addMessage("userForm:username", message);
            errMsg = message.getDetail();
        }
    	
    	if (userForm.getPassword() == null || userForm.getPassword().trim().isEmpty()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Required field", "Password is required.");
            FacesContext.getCurrentInstance().addMessage("userForm:password", message);
            errMsg = message.getDetail();
        }
    	
    	if (userForm.getPassword() == null || !userForm.getPassword().equals(userForm.getConfirmPassword())) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password mismatch", "The passwords do not match.");
            FacesContext.getCurrentInstance().addMessage("userForm:confirmPassword", message);
            errMsg = message.getDetail();
        }
    	
    	if(userService.userExists(userForm.getId(), userForm.getUsername())) {
    		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unique field", "Username is not available.");
            FacesContext.getCurrentInstance().addMessage("userForm:username", message);
            errMsg = message.getDetail();
    	}
    	
    	if (!isRegistration && userForm.getRoleId() < 1) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Required field", "Role is required.");
            FacesContext.getCurrentInstance().addMessage("userForm:roleId", message);
            errMsg = message.getDetail();
        }
    	
		boolean isValid = errMsg.isEmpty();
    	
    	if(!isValid) {
    		FacesContext.getCurrentInstance().validationFailed();
    	}
    	
    	return isValid;
    	
    }
    
    private void clearForm() {
    	UserDTO user = new UserDTO();
    	this.userForm.setId(user.getId());
    	this.userForm.setUsername(user.getUsername());
    	this.userForm.setPassword(user.getPassword());
    	this.userForm.setFirstName(user.getFirstName());
    	this.userForm.setLastName(user.getLastName());
    	this.userForm.setRoleId(user.getRoleId());
    }
    
}