package dao;

import entity.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ComponentRepository extends BaseRepository<Component> {

    @Override
    protected String getTableName() {
        return "components";
    }

    @Override
    protected Component mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Component component = new Component();
        component.setId(resultSet.getInt("component_id"));
        component.setName(resultSet.getString("name"));
        component.setDescription(resultSet.getString("description"));
        component.setType(resultSet.getString("type"));
        component.setStartDate(resultSet.getTimestamp("start_date").toLocalDateTime());
        component.setEndDate(resultSet.getTimestamp("end_date").toLocalDateTime());
        component.setProjectId(resultSet.getInt("project_id"));
        component.setCreatedBy(resultSet.getInt("created_by"));
        component.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        component.setUpdatedBy(resultSet.getInt("updated_by"));
        component.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        return component;
    }

    @Override
    protected String getIdColumnName() {
        return "component_id";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO components (name, description, type, start_date, end_date, project_id, created_by, created_at, updated_by, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE components SET name = ?, description = ?, type = ?, start_date = ?, end_date = ?, project_id = ?, updated_by = ?, updated_at = ? WHERE component_id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement preparedStatement, Component entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getDescription());
        preparedStatement.setString(3, entity.getType());
        preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getStartDate()));
        preparedStatement.setTimestamp(5, Timestamp.valueOf(entity.getEndDate()));
        preparedStatement.setInt(6, entity.getProjectId());
        preparedStatement.setInt(7, entity.getCreatedBy());
        preparedStatement.setTimestamp(8, Timestamp.valueOf(entity.getCreatedAt()));
        preparedStatement.setInt(9, entity.getUpdatedBy());
        preparedStatement.setTimestamp(10, Timestamp.valueOf(entity.getUpdatedAt()));
    }

    @Override
    protected void setUpdateParameters(PreparedStatement preparedStatement, Component entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getDescription());
        preparedStatement.setString(3, entity.getType());
        preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getStartDate()));
        preparedStatement.setTimestamp(5, Timestamp.valueOf(entity.getEndDate()));
        preparedStatement.setInt(6, entity.getProjectId());
        preparedStatement.setInt(7, entity.getUpdatedBy());
        preparedStatement.setTimestamp(8, Timestamp.valueOf(entity.getUpdatedAt()));
        preparedStatement.setInt(9, entity.getId());
    }
}
