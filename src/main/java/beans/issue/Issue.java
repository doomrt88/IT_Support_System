package beans.issue;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import models.Enums.States;
import models.dto.IssueDTO;
import models.dto.RoleFormDTO;
import models.dto.UserDTO;
import models.entity.IssueBoard;
import models.entity.Role;
import service.IssueService;
import service.RoleService;
import service.UserService;

@SuppressWarnings("serial")
@Named(value="issueBean")
@RequestScoped
public class Issue implements Serializable{
	
	private IssueService issueDao;
	private UserService userService;
	private IssueDTO issueForm;
	
	@PostConstruct
    public void initialize() {
		issueDao = new IssueService();
		userService = new UserService();
		issueForm = new IssueDTO();
    }
	
	public Issue() {
		
    }

	public List<IssueBoard> getAll() {
        return this.issueDao.getAllIssues();
    }
    
    public List<IssueBoard> getAssignedIssues_ByUserStatusOpen() {
        return this.issueDao.getAssignedIssues_ByUserStatus(1, States.OPEN);
    }
    
    public List<IssueBoard> getIssues_ByStatusOpen() {
    	
    	return this.issueDao.getIssues_ByStatus(States.OPEN);
    }

    public List<IssueBoard> getSubmittedIssues_ByUser() {
    	
    	return this.issueDao.getSubmittedIssues_ByUser(1);
    }
    
    public List<IssueBoard> getIssues_ByStatusClose() {
    	
    	return this.issueDao.getIssues_ByStatus(States.CLOSED);
    }

    // Issue Create/Edit Dialog
    public void openNew() {
        this.issueForm = new IssueDTO();
    }
    
    public void editIssue(IssueBoard issue) {
		
    	this.issueForm.setId(issue.getIssueID());
        this.issueForm.setTitle(issue.getTitle());
        this.issueForm.setDescription(issue.getDescription());
        this.issueForm.setPriority(issue.getPriority());
        this.issueForm.setIssueType(issue.getIssueType());
        this.issueForm.setProjectId(issue.getProjectID());

        Date startDateSql = issue.getStartDate();
        
        if(startDateSql != null) {

            LocalDate startDateLocalDate = startDateSql.toLocalDate();
            LocalDateTime startDate = startDateLocalDate.atStartOfDay();
            this.issueForm.setStartDate(startDate);
        }
        
        // Convert java.sql.Date to java.time.LocalDateTime for due date
        Date dueDateSql = issue.getDueDate();
        LocalDate dueDateLocalDate = dueDateSql.toLocalDate();
        LocalDateTime dueDate = dueDateLocalDate.atStartOfDay();
        this.issueForm.setDueDate(dueDate);
        
        this.issueForm.setStatus(issue.getStatus());
        this.issueForm.setAssignedTo(issue.getAssignedTo());
    }

    public List<UserDTO> searchUser(String query) {
        List<UserDTO> filteredUsers = userService.searchUser(query);
        return filteredUsers;
    }
    
    public IssueDTO getIssueForm() {
        return issueForm;
    }

    public void setIssueForm(IssueDTO issueForm) {
        this.issueForm = issueForm;
    }

    public void saveIssue() {
        if (validateIssue()) {
            if (issueForm.getId() > 0) {
                updateIssue();
            } else {
                createIssue();
            }
        }
    }

    private boolean validateIssue() {
        boolean isValid = true;
        if (issueForm.getTitle() == null || issueForm.getTitle().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Title is required", "Please enter a title for the issue."));
            isValid = false;
        }

        return isValid;
    }

    private void createIssue() {
    	IssueBoard issueToCreate = new IssueBoard();
        issueToCreate.setTitle(issueForm.getTitle());
        issueToCreate.setDescription(issueForm.getDescription());
        issueToCreate.setPriority(issueForm.getPriority());
        issueToCreate.setIssueType(issueForm.getIssueType());
        issueToCreate.setProjectID(issueForm.getProjectId());
        
        // Convert LocalDateTime to Date
//        LocalDateTime startDate = issueForm.getStartDate();
//        Date startDateAsDate = Date.valueOf(startDate.toLocalDate());
//        issueToCreate.setStartDate(startDateAsDate);
        
        // Convert LocalDateTime to Date
        LocalDateTime dueDate = issueForm.getDueDate();
        Date dueDateAsDate = Date.valueOf(dueDate.toLocalDate());
        issueToCreate.setDueDate(dueDateAsDate);
        
        issueToCreate.setStatus(issueForm.getStatus());
        issueToCreate.setProjectID(issueForm.getProjectId());
        issueToCreate.setAssignedTo(issueForm.getAssignedTo());
        
        boolean success = issueDao.createIssue(issueToCreate);
        if (success) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Issue created", "The issue has been successfully created."));
            issueForm = new IssueDTO(); // Clear the form after successful creation
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error creating issue", "An error occurred while creating the issue. Please try again."));
        }
    }

    private void updateIssue() {
    	IssueBoard issueToUpdate = new IssueBoard();
    	issueToUpdate.setIssueID(issueForm.getId());
    	
    	issueToUpdate.setTitle(issueForm.getTitle());
    	issueToUpdate.setDescription(issueForm.getDescription());
    	issueToUpdate.setPriority(issueForm.getPriority());
    	issueToUpdate.setIssueType(issueForm.getIssueType());
    	issueToUpdate.setProjectID(issueForm.getProjectId());
        
    	// Convert LocalDateTime to Date
        LocalDateTime startDate = issueForm.getStartDate();
    	if(startDate != null) {
            Date startDateAsDate = Date.valueOf(startDate.toLocalDate());
            issueToUpdate.setStartDate(startDateAsDate);
    	}
        
        // Convert LocalDateTime to Date
        LocalDateTime dueDate = issueForm.getDueDate();
        Date dueDateAsDate = Date.valueOf(dueDate.toLocalDate());
        issueToUpdate.setDueDate(dueDateAsDate);
        
        issueToUpdate.setStatus(issueForm.getStatus());
        issueToUpdate.setProjectID(issueForm.getProjectId());
        issueToUpdate.setAssignedTo(issueForm.getAssignedTo());
        
        boolean success = issueDao.updateIssue(issueToUpdate);
        if (success) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Issue updated", "The issue has been successfully updated."));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error updating issue", "An error occurred while updating the issue. Please try again."));
        }
    }
}
