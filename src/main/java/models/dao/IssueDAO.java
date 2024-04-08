//package models.dao;
//import util.DBConnection;
//import java.sql.Connection;
//import models.entity.IssueBoard;
//
//import java.sql.Statement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;
//
//import controller.IssueBean.States;
//
//
//public class IssueDAO extends DBConnection{
//
//	public List<IssueBoard> getAll() {
//		List<IssueBoard> list = new ArrayList<>();
//		try {
//			Statement st = connect().createStatement();
//			ResultSet rs = st.executeQuery("SELECT * FROM issues ORDER BY issue_id asc");
//			while (rs.next()) {
//
//				IssueBoard tmp = new IssueBoard(rs.getInt("issue_id"), rs.getString("title"), 
//									rs.getString("description"),rs.getInt("priority"),
//									rs.getDate("start_date"),rs.getDate("due_date"), 
//									rs.getString("status"), rs.getInt("component_id"),
//									rs.getInt("assigned_to"), rs.getInt("created_by"), 
//									rs.getDate("created_at"),rs.getInt("updated_by"), 
//									rs.getDate("updated_at"));
//				list.add(tmp);
//			}
//
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		return list;
//	}
//	
//	public List<IssueBoard> getAssignedIssues_ByUserStatus(int userId, States open) {
//		List<IssueBoard> list = new ArrayList<>();
//		try {
//			Statement st = connect().createStatement();
//			ResultSet rs = st.executeQuery("SELECT * FROM issues "
//											+ " WHERE assigned_to = " + userId 
//											+ " AND status = '" + open + "' "  
//											+ " ORDER BY issue_id asc");
//			
//			while (rs.next()) {
//
//				IssueBoard tmp = new IssueBoard(rs.getInt("issue_id"), rs.getString("title"), 
//									rs.getString("description"),rs.getInt("priority"),
//									rs.getDate("start_date"),rs.getDate("due_date"), 
//									rs.getString("status"), rs.getInt("component_id"),
//									rs.getInt("assigned_to"), rs.getInt("created_by"), 
//									rs.getDate("created_at"),rs.getInt("updated_by"), 
//									rs.getDate("updated_at"));
//				list.add(tmp);
//			}
//
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		return list;
//	}
//	
//	public List<IssueBoard> getIssues_ByStatus(States open) {
//		List<IssueBoard> list = new ArrayList<>();
//		try {
//			Statement st = connect().createStatement();
//			ResultSet rs = st.executeQuery("SELECT * FROM issues "
//											+ " WHERE status = '" + open + "' "  
//											+ " ORDER BY issue_id asc");
//			
//			while (rs.next()) {
//
//				IssueBoard tmp = new IssueBoard(rs.getInt("issue_id"), rs.getString("title"), 
//									rs.getString("description"),rs.getInt("priority"),
//									rs.getDate("start_date"),rs.getDate("due_date"), 
//									rs.getString("status"), rs.getInt("component_id"),
//									rs.getInt("assigned_to"), rs.getInt("created_by"), 
//									rs.getDate("created_at"),rs.getInt("updated_by"), 
//									rs.getDate("updated_at"));
//				list.add(tmp);
//			}
//
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		return list;
//	}
//
//	public List<IssueBoard> getSubmittedIssues_ByUser(int userId) {
//		List<IssueBoard> list = new ArrayList<>();
//		try {
//			Statement st = connect().createStatement();
//			ResultSet rs = st.executeQuery("SELECT * FROM issues "
//											+ " WHERE created_by = " + userId  
//											+ " ORDER BY issue_id asc");
//			
//			while (rs.next()) {
//
//				IssueBoard tmp = new IssueBoard(rs.getInt("issue_id"), rs.getString("title"), 
//									rs.getString("description"),rs.getInt("priority"),
//									rs.getDate("start_date"),rs.getDate("due_date"), 
//									rs.getString("status"), rs.getInt("component_id"),
//									rs.getInt("assigned_to"), rs.getInt("created_by"), 
//									rs.getDate("created_at"),rs.getInt("updated_by"), 
//									rs.getDate("updated_at"));
//				list.add(tmp);
//			}
//
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		return list;
//	}
//	
//}
