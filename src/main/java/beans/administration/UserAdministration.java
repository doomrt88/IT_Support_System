package beans.administration;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import entity.Role;
import entity.User;
import models.dto.BaseResponse;
import models.dto.UserDTO;
import service.RoleService;
import service.UserService;

import org.primefaces.PrimeFaces;

@SuppressWarnings("serial")
@Named("userAdministrationBean")
@RequestScoped
public class UserAdministration implements Serializable {

    private UserService userService = null;
    private RoleService roleService = null;

    private List<Role> roles;
    private UserDTO userForm;
    private BaseResponse response;

    @PostConstruct
    public void initialize() {
    	userForm = new UserDTO();
        response = new BaseResponse();
        
        userService = new UserService();
        roleService = new RoleService();
        
        roles = roleService.getAllRoles();
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
    
    public void saveUser() {
    	if (userForm.getId() > 0) {
    		updateUser();
        } else {
        	register();
        }
    }
    
    public void deleteUser(int id) {

        boolean success = userService.deleteUser(id);
        if (success) {
        	response.setResult(success);
            response.setMessage("User has been deleted");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User has been deleted"));
            clearForm();
        } else {
        	response.setMessage("User deletion failed. Please try again.");
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User deletion failed. Please try again."));
        }
        
        PrimeFaces.current().ajax().update("form:userMessages", "form:usersTable");
    }
    
    public void register() {
        if (!validate()) {
            return;
        }

        boolean success = userService.registerUser(userForm.getUsername(), userForm.getPassword(), userForm.getFirstName(), userForm.getLastName(), userForm.getRoleId());
        if (success) {
        	response.setResult(success);
            response.setMessage("User has been added");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User has been added"));
            FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('userDialog').hide()");
            
            clearForm();
        } else {
        	response.setMessage("User creation failed. Please try again.");
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User"));
        }
    }
    
    private void updateUser() {
        if (!validate()) {
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
            response.setResult(success);
            response.setMessage("User has been updated");
            clearForm();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User has been updated"));
        } else {
            response.setMessage("User update failed. Please try again.");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User update failed. Please try again."));
        }
        
        PrimeFaces.current().ajax().update("form:userMessages", "form:usersTable");
    }

  
    private boolean validate() {
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
    	
    	if (userForm.getRoleId() < 1) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Required field", "Role is required.");
            FacesContext.getCurrentInstance().addMessage("userForm:roleId", message);
            errMsg = message.getDetail();
        }
    	
    	return errMsg.isEmpty();
    	
    }
    
    private void clearForm() {
    	UserDTO user = new UserDTO();
    	user.setId(user.getId());
    	user.setUsername(user.getUsername());
    	user.setPassword(user.getPassword());
    	user.setFirstName(user.getFirstName());
    	user.setLastName(user.getLastName());
    	user.setRoleId(user.getRoleId());
    }
    
}