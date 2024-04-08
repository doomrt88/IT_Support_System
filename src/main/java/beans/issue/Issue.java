package beans.issue;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import models.Enums.States;
import models.entity.IssueBoard;
import service.IssueService;

@SuppressWarnings("serial")
@Named(value="issueBean")
@SessionScoped
public class Issue implements Serializable{
	
	private IssueService issueDao;
	private IssueBoard issue;

	public Issue() {
		issueDao = new IssueService();
    }

    public IssueBoard getEntity() {
        if(this.issue == null)
            this.issue = new IssueBoard();
        return issue;
    }

    public void setEntity(IssueBoard entity) {
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

    
}
