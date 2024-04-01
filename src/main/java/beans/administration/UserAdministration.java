package beans.administration;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import entity.Role;
import entity.User;
import models.dto.BaseResponse;
import models.dto.UserFormDTO;
import service.RoleService;
import service.UserService;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@SuppressWarnings("serial")
@Named("userAdministrationBean")
@RequestScoped
public class UserAdministration implements Serializable {

    private UserService userService = null;
    private RoleService roleService = null;

    private List<Role> roles;
    private UserFormDTO userForm;
    private BaseResponse response;

    
    public UserAdministration() {
        userForm = new UserFormDTO();
        response = new BaseResponse();
        
        userService = new UserService();
        roleService = new RoleService();
        
        roles = roleService.getAllRoles();
    }
    
    
    public List<User> getUserList() {
        return userService.getAllUsers();
    }

  
    public void deleteUser(int id) {

        boolean success = userService.deleteUser(id);
        if (success) {
        	response.setResult(success);
            response.setMessage("User has been deleted");
            userForm = new UserFormDTO();
        } else {
        	response.setMessage("User deletion failed. Please try again.");
        }
    }
    
    public void register() {
        if (!validate()) {
            return;
        }

        boolean success = userService.registerUser(userForm.getUsername(), userForm.getPassword(), userForm.getFirstName(), userForm.getLastName(), userForm.getRoleId());
        if (success) {
        	response.setResult(success);
            response.setMessage("User has been added");
            userForm = new UserFormDTO();
        } else {
        	response.setMessage("User creation failed. Please try again.");
        }
    }

    public UserFormDTO getUserForm() {
        return userForm;
    }

    public void setUserForm(UserFormDTO userForm) {
        this.userForm = userForm;
    }
    
    public BaseResponse getResponse() {
    	return this.response;
    }

    public void setResponse(BaseResponse response) {
        this.response = response;
    }
    
    public String getResponseJson() {
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.toJson(response);
    }
    
    public String serializeUser(User user) {
    	Jsonb jsonb = JsonbBuilder.create();
        return jsonb.toJson(user);
    }
    
    public List<Role> getRoles() {
        return roles;
    }
    
    
    public void saveUser(int userId) {
        if (userId > 0) {
            updateUser();
        } else {
            register();
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
            userForm = new UserFormDTO();
        } else {
            response.setMessage("User update failed. Please try again.");
        }
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
    
}