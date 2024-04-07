package beans.administration;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import models.entity.Project;
import models.dto.ProjectDTO;
import service.ProjectService;

import org.primefaces.PrimeFaces;

@SuppressWarnings("serial")
@Named("projectAdministrationBean")
@RequestScoped
public class ProjectAdministration implements Serializable {

    private ProjectService projectService = null;

    private ProjectDTO projectForm;

    @PostConstruct
    public void initialize() {
    	projectForm = new ProjectDTO();
        
        projectService = new ProjectService();
    }

    public void editProject(ProjectDTO project) {
		
    	this.projectForm.setId(project.getId());
    	this.projectForm.setName(project.getName());
    	this.projectForm.setDescription(project.getDescription());
    	this.projectForm.setStartDate(project.getStartDate());
    	this.projectForm.setEndDate(project.getEndDate());
        
    }

    public ProjectDTO getProjectForm() {
        return projectForm;
    }

    public void setProjectForm(ProjectDTO projectForm) {
        this.projectForm = projectForm;
    }
    
    public List<ProjectDTO> getProjectList() {
        return projectService.getAllProjects();
    }
    
    public String formatDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else {
            return "";
        }
    }
    
    public void saveProject() {
    	if (projectForm.getId() > 0) {
    		updateProject();
        } else {
        	createProject();
        }
    }
    
    public void deleteProject(int id) {

        boolean success = projectService.deleteProject(id);
        if (success) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Project has been deleted"));
            clearForm();
        } else {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Project deletion failed. Please try again."));
        }
        
        PrimeFaces.current().ajax().update("form:projectMessages", "form:projectsTable");
    }
    
	public void createProject() {
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
		      clearForm();
		      
		      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Project has been added"));
	          FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('projectDialog').hide()");
	          
		  } else {
	      	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Project creation failed. Please try again."));
	      }
	      
	      
	      PrimeFaces.current().ajax().update("form:projectMessages", "form:projectsTable");
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
            clearForm();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Project has been updated"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Project update failed. Please try again."));
        }
        
        PrimeFaces.current().ajax().update("form:projectMessages", "form:projectsTable");
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
	        FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('projectDialog').show()");
	        
	        errMsg = message.getDetail();
	    }
	    
    	boolean isValid = errMsg.isEmpty();
    	
    	if(!isValid) {
    		FacesContext.getCurrentInstance().validationFailed();
    	}
    	
    	return isValid;
		
	}
    
    private void clearForm() {
    	ProjectDTO project = new ProjectDTO();
    	
    	project.setName(projectForm.getName());
    	project.setDescription(projectForm.getDescription());
    	project.setStartDate(projectForm.getStartDate());
    	project.setEndDate(projectForm.getEndDate());
    }
    
}