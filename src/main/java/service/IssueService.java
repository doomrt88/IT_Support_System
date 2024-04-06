package service;

import models.dao.CommentRepository;
import models.dao.IssueRepository;
import models.entity.Comment;
import models.entity.Issue;

public class IssueService{
	private IssueRepository issueRepository;
	private CommentRepository commentRepository;
	
	public IssueService() {
        this.issueRepository = new IssueRepository();
        this.commentRepository = new CommentRepository();
    }
	
	// Issues
	public boolean createIssue(Issue issue) {
        // we can add more business rules here such as any validations. Front end validations should be done in the bean or controller
        return issueRepository.insert(issue);
    }

    public boolean updateIssue(Issue issue) {
        // we can add more business rules here such as any validations
    	return issueRepository.update(issue);
    }

    public boolean deleteIssue(int issueId) {
        // we can add more business rules here such as any validations
        return issueRepository.delete(issueId);
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