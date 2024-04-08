package models.entity;
import java.sql.Date;

public class IssueBoard {

	private int issue_id;
	private String title;
	private String description;
	private int priority;
	private Date startDate;
	private Date dueDate;
	private String status;
	private int componentID;
	private int assignedTo;
	private int createdBy;
	private Date createdAt;
	private int updatedBy;
	private Date updatedAt;



	public IssueBoard() {

	}

	public IssueBoard(int issue_id, String title, String description, int priority, Date startDate, Date dueDate, String status, int componentID, int assignedTo, int createdBy, Date createdAt, int updatedBy, Date updatedAt) {
		this.issue_id = issue_id;
		this.title = title;
		this.description = description;
	}

	public int getIssueID() {
		return issue_id;
	}

	public void setIssueID(int issue_id) {
		this.issue_id = issue_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getComponentID() {
		return componentID;
	}

	public void setComponentID(int componentID) {
		this.componentID = componentID;
	}

	public int getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(int assignedTo) {
		this.assignedTo = assignedTo;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

}
