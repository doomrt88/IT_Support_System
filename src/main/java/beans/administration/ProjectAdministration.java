package beans.administration;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import models.dto.BaseResponse;
import models.dto.ProjectFormDTO;
import service.ProjectService;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import entity.Project;

@SuppressWarnings("serial")
@Named("projectAdministrationBean")
@RequestScoped
public class ProjectAdministration implements Serializable {

    private ProjectService projectService = null;
    private ProjectFormDTO projectForm;
    private BaseResponse response;

    
    public ProjectAdministration() {
        projectForm = new ProjectFormDTO();
        response = new BaseResponse();
        
        projectService = new ProjectService();
    }
    
    public List<Project> getProjectList() {
        return projectService.getAllProjects();
    }


    public ProjectFormDTO getProjectForm() {
        return projectForm;
    }

    public void setProjectForm(ProjectFormDTO projectForm) {
        this.projectForm = projectForm;
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
    
    public String serializeProject(Project project) {
    	Jsonb jsonb = JsonbBuilder.create();
        return jsonb.toJson(project);
    }    
    
    public void saveProject(int projectId) {
        if (projectId > 0) {
            updateProject();
        } else {
        	create();
        }
    }
    
   
    public void deleteProject(int id) {

        boolean success = projectService.deleteProject(id);
        if (success) {
        	response.setResult(success);
            response.setMessage("Project has been deleted");
            projectForm = new ProjectFormDTO();
        } else {
        	response.setMessage("Project deletion failed. Please try again.");
        }
    }
    
    public void create() {
        if (!validate()) {
            return;
        }

        Project projectToCreate = new Project();
        projectToCreate.setName(projectForm.getName());
        projectToCreate.setDescription(projectForm.getDescription());
        projectToCreate.setStartDate(projectForm.getStartDate());
        projectToCreate.setEndDate(projectForm.getEndDate());
        
        boolean success = projectService.createProject(projectToCreate);
        if (success) {
        	response.setResult(success);
            response.setMessage("Project has been added");
            projectForm = new ProjectFormDTO();
        } else {
        	response.setMessage("Project creation failed. Please try again.");
        }
    }
    
    private void updateProject() {
        if (!validate()) {
            return;
        }

        Project projectToUpdate = new Project();
        projectToUpdate.setId(projectForm.getId());
        projectToUpdate.setName(projectForm.getName());
        projectToUpdate.setDescription(projectForm.getDescription());
        projectToUpdate.setStartDate(projectForm.getStartDate());
        projectToUpdate.setEndDate(projectForm.getEndDate());
        
        boolean success = projectService.updateProject(projectToUpdate);
        if (success) {
            response.setResult(success);
            response.setMessage("Project has been updated");
            projectForm = new ProjectFormDTO();
        } else {
            response.setMessage("Project update failed. Please try again.");
        }
    }

  
    private boolean validate() {
    	if(projectForm == null) return false;
    	
    	String errMsg = "";
    	
    	if(projectService.projectExists(projectForm.getId(), projectForm.getName())) {
    		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unique field", "Name must be unique.");
            FacesContext.getCurrentInstance().addMessage("projectForm:name", message);
            errMsg = message.getDetail();
    	}
    	
    	LocalDateTime startDate = projectForm.getStartDate();
        LocalDateTime endDate = projectForm.getEndDate();

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid dates", "Start date cannot be after end date.");
            FacesContext.getCurrentInstance().addMessage("projectForm:startDate", message);
            FacesContext.getCurrentInstance().addMessage("projectForm:endDate", message);
            errMsg = message.getDetail();
        }
        
    	return errMsg.isEmpty();
    	
    }
    
}