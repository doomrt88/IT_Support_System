package dao;

import entity.Issue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class IssueRepository extends BaseRepository<Issue> {

    @Override
    protected String getTableName() {
        return "issues";
    }

    @Override
    protected Issue mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Issue issue = new Issue();
        issue.setId(resultSet.getInt("issue_id"));
        issue.setTitle(resultSet.getString("title"));
        issue.setDescription(resultSet.getString("description"));
        issue.setPriority(resultSet.getInt("priority"));
        issue.setStartDate(resultSet.getTimestamp("start_date").toLocalDateTime());
        issue.setDueDate(resultSet.getTimestamp("due_date").toLocalDateTime());
        issue.setStatus(resultSet.getString("status"));
        issue.setComponentId(resultSet.getInt("component_id"));
        issue.setAssignedTo(resultSet.getInt("assigned_to"));
        issue.setCreatedBy(resultSet.getInt("created_by"));
        issue.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        issue.setUpdatedBy(resultSet.getInt("updated_by"));
        issue.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        return issue;
    }

    @Override
    protected String getIdColumnName() {
        return "issue_id";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO issues (title, description, priority, start_date, due_date, status, component_id, assigned_to, created_by, created_at, updated_by, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE issues SET title = ?, description = ?, priority = ?, start_date = ?, due_date = ?, status = ?, component_id = ?, assigned_to = ?, updated_by = ?, updated_at = ? WHERE issue_id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement preparedStatement, Issue entity) throws SQLException {
        preparedStatement.setString(1, entity.getTitle());
        preparedStatement.setString(2, entity.getDescription());
        preparedStatement.setInt(3, entity.getPriority());
        preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getStartDate()));
        preparedStatement.setTimestamp(5, Timestamp.valueOf(entity.getDueDate()));
        preparedStatement.setString(6, entity.getStatus());
        preparedStatement.setInt(7, entity.getComponentId());
        preparedStatement.setInt(8, entity.getAssignedTo());
        preparedStatement.setInt(9, entity.getCreatedBy());
        preparedStatement.setTimestamp(10, Timestamp.valueOf(entity.getCreatedAt()));
        preparedStatement.setInt(11, entity.getUpdatedBy());
        preparedStatement.setTimestamp(12, Timestamp.valueOf(entity.getUpdatedAt()));
    }

    @Override
    protected void setUpdateParameters(PreparedStatement preparedStatement, Issue entity) throws SQLException {
        preparedStatement.setString(1, entity.getTitle());
        preparedStatement.setString(2, entity.getDescription());
        preparedStatement.setInt(3, entity.getPriority());
        preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getStartDate()));
        preparedStatement.setTimestamp(5, Timestamp.valueOf(entity.getDueDate()));
        preparedStatement.setString(6, entity.getStatus());
        preparedStatement.setInt(7, entity.getComponentId());
        preparedStatement.setInt(8, entity.getAssignedTo());
        preparedStatement.setInt(9, entity.getUpdatedBy());
        preparedStatement.setTimestamp(10, Timestamp.valueOf(entity.getUpdatedAt()));
        preparedStatement.setInt(11, entity.getId());
    }
}
