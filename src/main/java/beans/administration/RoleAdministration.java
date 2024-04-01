package beans.administration;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import models.dto.BaseResponse;
import models.dto.RoleFormDTO;
import service.RoleService;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import entity.Role;

@SuppressWarnings("serial")
@Named("roleAdministrationBean")
@RequestScoped
public class RoleAdministration implements Serializable {

    private RoleService roleService = null;
    private RoleFormDTO roleForm;
    private BaseResponse response;

    
    public RoleAdministration() {
        roleForm = new RoleFormDTO();
        response = new BaseResponse();
        
        roleService = new RoleService();
    }
    
    public List<Role> getRoleList() {
        return roleService.getAllRoles();
    }


    public RoleFormDTO getRoleForm() {
        return roleForm;
    }

    public void setRoleForm(RoleFormDTO roleForm) {
        this.roleForm = roleForm;
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
    
    public String serializeRole(Role role) {
    	Jsonb jsonb = JsonbBuilder.create();
        return jsonb.toJson(role);
    }    
    
    public void saveRole(int roleId) {
        if (roleId > 0) {
            updateRole();
        } else {
        	create();
        }
    }
    
   
    public void deleteRole(int id) {

        boolean success = roleService.deleteRole(id);
        if (success) {
        	response.setResult(success);
            response.setMessage("Role has been deleted");
            roleForm = new RoleFormDTO();
        } else {
        	response.setMessage("Role deletion failed. Please try again.");
        }
    }
    
    public void create() {
        if (!validate()) {
            return;
        }

        Role roleToCreate = new Role();
        roleToCreate.setName(roleForm.getName());
        roleToCreate.setDescription(roleForm.getDescription());
        
        boolean success = roleService.createRole(roleToCreate);
        if (success) {
        	response.setResult(success);
            response.setMessage("Role has been added");
            roleForm = new RoleFormDTO();
        } else {
        	response.setMessage("Role creation failed. Please try again.");
        }
    }
    
    private void updateRole() {
        if (!validate()) {
            return;
        }

        Role roleToUpdate = new Role();
        roleToUpdate.setId(roleForm.getId());
        roleToUpdate.setName(roleForm.getName());
        roleToUpdate.setDescription(roleForm.getDescription());

        boolean success = roleService.updateRole(roleToUpdate);
        if (success) {
            response.setResult(success);
            response.setMessage("Role has been updated");
            roleForm = new RoleFormDTO();
        } else {
            response.setMessage("Role update failed. Please try again.");
        }
    }

  
    private boolean validate() {
    	if(roleForm == null) return false;
    	
    	String errMsg = "";
    	
    	if(roleService.roleExists(roleForm.getId(), roleForm.getName())) {
    		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unique field", "Name must be unique.");
            FacesContext.getCurrentInstance().addMessage("roleForm:name", message);
            errMsg = message.getDetail();
    	}
    	
    	return errMsg.isEmpty();
    	
    }
    
}