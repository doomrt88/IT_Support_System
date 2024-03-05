package dao;

import entity.Project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ProjectRepository extends BaseRepository<Project> {

    @Override
    protected String getTableName() {
        return "projects";
    }

    @Override
    protected Project mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Project project = new Project();
        project.setId(resultSet.getInt("project_id"));
        project.setName(resultSet.getString("name"));
        project.setDescription(resultSet.getString("description"));
        project.setStartDate(resultSet.getTimestamp("start_date").toLocalDateTime());
        project.setEndDate(resultSet.getTimestamp("end_date").toLocalDateTime());
        project.setCreatedBy(resultSet.getInt("created_by"));
        project.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        project.setUpdatedBy(resultSet.getInt("updated_by"));
        project.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        return project;
    }

    @Override
    protected String getIdColumnName() {
        return "project_id";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO projects (name, description, start_date, end_date, created_by, created_at, updated_by, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE projects SET name = ?, description = ?, start_date = ?, end_date = ?, updated_by = ?, updated_at = ? WHERE project_id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement preparedStatement, Project entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getDescription());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getStartDate()));
        preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getEndDate()));
        preparedStatement.setInt(5, entity.getCreatedBy());
        preparedStatement.setTimestamp(6, Timestamp.valueOf(entity.getCreatedAt()));
        preparedStatement.setInt(7, entity.getUpdatedBy());
        preparedStatement.setTimestamp(8, Timestamp.valueOf(entity.getUpdatedAt()));
    }

    @Override
    protected void setUpdateParameters(PreparedStatement preparedStatement, Project entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getDescription());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getStartDate()));
        preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getEndDate()));
        preparedStatement.setInt(5, entity.getUpdatedBy());
        preparedStatement.setTimestamp(6, Timestamp.valueOf(entity.getUpdatedAt()));
        preparedStatement.setInt(7, entity.getId());
    }
}
