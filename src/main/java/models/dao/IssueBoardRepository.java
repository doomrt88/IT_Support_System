package models.dao;

import models.Enums.States;
import models.entity.IssueBoard;
import models.entity.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class IssueBoardRepository extends BaseRepository<IssueBoard> {

    @Override
    protected String getTableName() {
        return "issues";
    }

    @Override
    protected IssueBoard mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        IssueBoard issue = new IssueBoard();
        issue.setIssueID(resultSet.getInt("issue_id"));
        issue.setTitle(resultSet.getString("title"));
        issue.setDescription(resultSet.getString("description"));
        issue.setPriority(resultSet.getInt("priority"));
        issue.setStartDate(resultSet.getDate("start_date"));
        issue.setDueDate(resultSet.getDate("due_date"));
        issue.setStatus(resultSet.getString("status"));
        issue.setProjectID(resultSet.getInt("project_id"));
        issue.setIssueType(resultSet.getString("issue_type"));
        issue.setAssignedTo(resultSet.getInt("assigned_to"));
        issue.setCreatedBy(resultSet.getInt("created_by"));
        issue.setCreatedAt(resultSet.getDate("created_at"));
        issue.setUpdatedBy(resultSet.getInt("updated_by"));
        issue.setUpdatedAt(resultSet.getDate("updated_at"));

        return issue;
    }

    @Override
    protected String getIdColumnName() {
        return "issue_id";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO issues (title, description, priority, start_date, due_date, status, project_id, issue_type, assigned_to, created_by, created_at, updated_by, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE issues SET title = ?, description = ?, priority = ?, start_date = ?, due_date = ?, status = ?, project_id = ?, issue_type = ?, assigned_to = ?, updated_by = ?, updated_at = ? WHERE issue_id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement preparedStatement, IssueBoard entity) throws SQLException {
        preparedStatement.setString(1, entity.getTitle());
        preparedStatement.setString(2, entity.getDescription());
        preparedStatement.setInt(3, entity.getPriority());
        preparedStatement.setDate(4, entity.getStartDate());
        preparedStatement.setDate(5, entity.getDueDate());
        preparedStatement.setString(6, entity.getStatus());
        preparedStatement.setInt(7, entity.getProjectID());
        preparedStatement.setString(8, entity.getIssueType());
        preparedStatement.setInt(9, entity.getAssignedTo());
        preparedStatement.setInt(10, entity.getCreatedBy());
        preparedStatement.setTimestamp(11, new java.sql.Timestamp(System.currentTimeMillis()));
        preparedStatement.setInt(12, entity.getUpdatedBy());
        preparedStatement.setTimestamp(13, new java.sql.Timestamp(System.currentTimeMillis()));
    }

    @Override
    protected void setUpdateParameters(PreparedStatement preparedStatement, IssueBoard entity) throws SQLException {
        preparedStatement.setString(1, entity.getTitle());
        preparedStatement.setString(2, entity.getDescription());
        preparedStatement.setInt(3, entity.getPriority());
        preparedStatement.setDate(4, entity.getStartDate());
        preparedStatement.setDate(5, entity.getDueDate());
        preparedStatement.setString(6, entity.getStatus());
        preparedStatement.setInt(7, entity.getProjectID());
        preparedStatement.setString(8, entity.getIssueType());
        preparedStatement.setInt(9, entity.getAssignedTo());
        preparedStatement.setInt(10, entity.getUpdatedBy());
        preparedStatement.setTimestamp(11, new java.sql.Timestamp(System.currentTimeMillis()));
        preparedStatement.setInt(12, entity.getIssueID());
    }
    
    // moved Issue DAO here
    public List<IssueBoard> getAssignedIssues_ByUserStatus(int userId, States open) {
        List<IssueBoard> list = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName() + " WHERE assigned_to = ? AND status = ? ORDER BY issue_id ASC";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = DbContext.getInstance().getConnection();
        	preparedStatement = connection.prepareStatement(sql);
        	preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, open.toString());
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
            	while (resultSet.next()) {
                    list.add(mapResultSetToEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                DbContext.getInstance().releaseConnection(connection);
            }
        }
        
        return list;
    }
    
    public List<IssueBoard> getIssues_ByStatus(States open) {
        List<IssueBoard> list = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName() + " WHERE status = ? ORDER BY issue_id ASC";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = DbContext.getInstance().getConnection();
        	preparedStatement = connection.prepareStatement(sql);
        	preparedStatement.setString(1, open.toString());
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
            	while (resultSet.next()) {
                    list.add(mapResultSetToEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                DbContext.getInstance().releaseConnection(connection);
            }
        }
        
        return list;
    }

    public List<IssueBoard> getSubmittedIssues_ByUser(int userId) {
        List<IssueBoard> list = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName() + " WHERE created_by = ? ORDER BY issue_id ASC";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = DbContext.getInstance().getConnection();
        	preparedStatement = connection.prepareStatement(sql);
        	preparedStatement.setInt(1, userId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
            	while (resultSet.next()) {
                    list.add(mapResultSetToEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                DbContext.getInstance().releaseConnection(connection);
            }
        }
        
        return list;
    }
    
    public IssueBoard getByAssignedId(int userId) {
        String sql = "SELECT * FROM issues WHERE assigned_to= ? ORDER BY assigned_to LIMIT 1";
        Connection connection = null;
        try {
        	connection = DbContext.getInstance().getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToEntity(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                DbContext.getInstance().releaseConnection(connection);
            }
        }
        
        return null;
    }
    
    public IssueBoard getByProjectId(int projectId) {
        String sql = "SELECT * FROM issues WHERE project_id= ? ORDER BY project_id LIMIT 1";
        Connection connection = null;
        try {
        	connection = DbContext.getInstance().getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, projectId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToEntity(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                DbContext.getInstance().releaseConnection(connection);
            }
        }
        
        return null;
    }
    
//    public List<IssueBoard> getAll() {
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
}
