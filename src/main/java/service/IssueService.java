package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Enums.States;
import models.dao.CommentRepository;
import models.dao.IssueBoardRepository;
import models.entity.Comment;
import models.entity.IssueBoard;

public class IssueService{
	private IssueBoardRepository issueBoardRepository;
	private CommentRepository commentRepository;
	
	public IssueService() {
        this.issueBoardRepository = new IssueBoardRepository();
        this.commentRepository = new CommentRepository();
    }
	
	// Issues
	public boolean createIssue(IssueBoard issue) {
        // we can add more business rules here such as any validations. Front end validations should be done in the bean or controller
        return issueBoardRepository.insert(issue);
    }

    public boolean updateIssue(IssueBoard issue) {
        // we can add more business rules here such as any validations
    	return issueBoardRepository.update(issue);
    }

    public boolean deleteIssue(int issueId) {
        // we can add more business rules here such as any validations
        return issueBoardRepository.delete(issueId);
    }
    
    public List<IssueBoard> getAllIssues() {
    	Map<String, String> orderByColumns = new HashMap<>();
    	orderByColumns.put("issue_id", "ASC");
    	
        return issueBoardRepository.getAllOrderedBy(orderByColumns);
    }
    
    public List<IssueBoard> getAssignedIssues_ByUserStatus(int userId, States open) {
        return issueBoardRepository.getAssignedIssues_ByUserStatus(userId, open);
    }
    
    public List<IssueBoard> getIssues_ByStatus(States open) {
        return issueBoardRepository.getIssues_ByStatus(open);
    }
    
    public List<IssueBoard> getSubmittedIssues_ByUser(int userId) {
        return issueBoardRepository.getSubmittedIssues_ByUser(userId);
    }
    
    // Comments
    public boolean createComment(Comment comment) {
        // we can add more business rules here such as any validations. Front end validations should be done in the bean or controller
        return commentRepository.insert(comment);
    }
    
    public boolean updateComment(Comment comment) {
        // we can add more business rules here such as any validations
    	return commentRepository.update(comment);
    }

    public boolean deleteComment(int commentId) {
        // we can add more business rules here such as any validations
        return commentRepository.delete(commentId);
    }
}