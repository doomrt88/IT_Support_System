package controller;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import dao.IssueDAO;
import dao.UserRepository;
import entity.IssueBoard;

@Named(value="issueBean")
@SessionScoped
public class IssueBean implements Serializable{
	
	private IssueDAO issueDao;
	private IssueBoard issue;
	public enum States{
		OPEN, CLOSE;
	}
	
	public IssueBean() {
    }

    public IssueDAO getIssueDao() {
        if(this.issueDao == null)
            this.issueDao = new IssueDAO();
        return issueDao;
    }

    public void setIssueDao(IssueDAO dao) {
        this.issueDao = dao;
    }

    public IssueBoard getEntity() {
        if(this.issue == null)
            this.issue = new IssueBoard();
        return issue;
    }

    public void setEntity(IssueBoard entity) {
    }
    
    public List<IssueBoard> getAll() {
        return this.getIssueDao().getAll();
    }
    
    public List<IssueBoard> getAssignedIssues_ByUserStatusOpen() {
        return this.getIssueDao().getAssignedIssues_ByUserStatus(1, States.OPEN);
    }
    
    public List<IssueBoard> getIssues_ByStatusOpen() {
    	
    	return this.getIssueDao().getIssues_ByStatus(States.OPEN);
    }

    public List<IssueBoard> getSubmittedIssues_ByUser() {
    	
    	return this.getIssueDao().getSubmittedIssues_ByUser(1);
    }
    
    public List<IssueBoard> getIssues_ByStatusClose() {
    	
    	return this.getIssueDao().getIssues_ByStatus(States.CLOSE);
    }

    
}
