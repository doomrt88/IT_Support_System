package service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import models.dao.ProjectRepository;
import models.entity.Project;
import models.entity.User;
import models.entity.UserRole;
import models.dto.ProjectDTO;
import models.dto.UserDTO;

public class ProjectService{
	private ProjectRepository projectRepository;
	
	public ProjectService() {
        this.projectRepository = new ProjectRepository();
    }
	
	
	public boolean createProject(Project project) {
        // we can add more business rules here such as any validations. Front end validations should be done in the bean or controller
		project.setCreatedAt(LocalDateTime.now());
		project.setUpdatedAt(LocalDateTime.now());
        return projectRepository.insert(project);
    }

    public boolean updateProject(Project project) {
        // we can add more business rules here such as any validations
    	project.setUpdatedAt(LocalDateTime.now());
    	return projectRepository.update(project);
    }

    public boolean deleteProject(int projectId) {
        // we can add more business rules here such as any validations
        return projectRepository.delete(projectId);
    }
    
    public List<ProjectDTO> getAllProjects() {
    	List<Project> projects = projectRepository.getAll();
    	List<ProjectDTO> projectDTO = new ArrayList<>();
    	
        for (Project project : projects) {
        	projectDTO.add(mapEntityToDTO(project));
        }
        
        return projectDTO;
    }
    
    public boolean projectExists(int id, String name) {
        return projectRepository.getByName(id, name) != null;
    }
    
    private ProjectDTO mapEntityToDTO(Project user){
    	ProjectDTO projectDTO = new ProjectDTO();
        
    	projectDTO.setId(user.getId());
    	projectDTO.setName(user.getName());
    	projectDTO.setDescription(user.getDescription());
    	projectDTO.setStartDate(user.getStartDate());
    	projectDTO.setEndDate(user.getEndDate());
        
        return projectDTO;
    }
}