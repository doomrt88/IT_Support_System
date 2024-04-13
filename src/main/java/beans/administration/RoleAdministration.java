package beans.administration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.inject.Named;

import models.dto.GroupedPermission;
import models.dto.RoleFormDTO;
import service.IssueService;
import service.RoleService;

import org.primefaces.PrimeFaces;

import models.entity.Permission;
import models.entity.Role;

@SuppressWarnings("serial")
@Named("roleAdministrationBean")
@RequestScoped
public class RoleAdministration implements Serializable {

    private RoleService roleService = null;
    private RoleFormDTO roleForm;

    private List<SelectItem> permissions;
    
    @PostConstruct
    public void initialize() {
    	roleForm = new RoleFormDTO();
        

        roleService = new RoleService();
        permissions = new ArrayList<>();
        
        List<GroupedPermission> groupedPermissions = roleService.getAllPermissionsGrouped();

        for (GroupedPermission groupedPermission : groupedPermissions) {
            SelectItemGroup group = new SelectItemGroup(groupedPermission.getGroupName());
            List<SelectItem> selectItems = new ArrayList<>();
            for (Permission permission : groupedPermission.getPermissions()) {
                selectItems.add(new SelectItem(permission.getCode(), permission.getName()));
            }
            
            group.setSelectItems(selectItems.toArray(new SelectItem[0]));
            permissions.add(group);
        }
    }    
   
    public List<SelectItem> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SelectItem> permissions) {
        this.permissions = permissions;
    }
       
    public void openNew() {
        this.roleForm = new RoleFormDTO();
    }
   
    public List<RoleFormDTO> getRoleList() {
    	return roleService.getAllRolesWithPermissions();
    }
    
    public void editRole(RoleFormDTO role) {
		
    	this.roleForm.setId(role.getId());
    	this.roleForm.setName(role.getName());
    	this.roleForm.setDescription(role.getDescription());
		
    	this.roleForm.setPermissions(role.getPermissions());
    }
    
    public RoleFormDTO getRoleForm() {
        return roleForm;
    }

    public void setRoleForm(RoleFormDTO roleForm) {
        this.roleForm = roleForm;
    }
    
    public void saveRole() {
        if (roleForm.getId() > 0) {
            updateRole();
        } else {
        	createRole();
        }
    }
    
    public void createRole() {
        if (!validate()) {
            return;
        }

        Role roleToCreate = new Role();
        roleToCreate.setName(roleForm.getName());
        roleToCreate.setDescription(roleForm.getDescription());
        
        boolean success = roleService.createRole(roleToCreate, roleForm.getPermissions());
        if (success) {
            clearForm();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Role has been added"));
            //FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('roleDialog').hide()");
        } else {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Role creation failed. Please try again."));
        }
        
        
        PrimeFaces.current().ajax().update("form:roleMessages", "form:rolesTable");
    }
    
    private void updateRole() {
        if (!validate()) {
            return;
        }

        Role roleToUpdate = new Role();
        roleToUpdate.setId(roleForm.getId());
        roleToUpdate.setName(roleForm.getName());
        roleToUpdate.setDescription(roleForm.getDescription());

        boolean success = roleService.updateRole(roleToUpdate, roleForm.getPermissions());
        if (success) {
            clearForm();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Role has been updated"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Role update failed. Please try again."));
        }
        
        PrimeFaces.current().ajax().update("form:roleMessages", "form:rolesTable");
    }
    
    public void deleteRole(int id) {

    	// check if role has users, do not delete
    	if(roleService.isAllowDelete(id)) {
	        boolean success = roleService.deleteRole(id);
	        if (success) {
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Role has been deleted"));
	            clearForm();
	        } else {
	        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Role deletion failed. Please try again."));
	        }
    	}else {
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Delete is not allowed."));
    	}
    	
        PrimeFaces.current().ajax().update("form:roleMessages", "form:rolesTable");
    }

  
    private boolean validate() {
    	if(roleForm == null) return false;
    	
    	String errMsg = "";
    	
    	if(roleService.roleExists(roleForm.getId(), roleForm.getName())) {
    		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unique field", "Name must be unique.");
            FacesContext.getCurrentInstance().addMessage("roleForm:name", message);
            errMsg = message.getDetail();
    	}
    	
    	boolean isValid = errMsg.isEmpty();
    	
    	if(!isValid) {
    		FacesContext.getCurrentInstance().validationFailed();
    	}
    	
    	return isValid;
    	
    }
    
    private void clearForm() {
    	RoleFormDTO role = new RoleFormDTO();
    	this.roleForm.setId(role.getId());
    	this.roleForm.setName(role.getName());
    	this.roleForm.setDescription(role.getDescription());
    }
    
}