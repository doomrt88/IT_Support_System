package service;

import java.time.LocalDateTime;
import java.util.List;

import dao.ProjectRepository;
import entity.Project;

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
    
    public List<Project> getAllProjects() {
        return projectRepository.getAll();
    }
    
    public boolean projectExists(int id, String name) {
        return projectRepository.getByName(id, name) != null;
    }
}