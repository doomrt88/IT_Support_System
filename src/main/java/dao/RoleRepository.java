package dao;

import entity.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RoleRepository extends BaseRepository<Role> {

    @Override
    protected String getTableName() {
        return "roles";
    }

    @Override
    protected Role mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getInt("role_id"));
        role.setName(resultSet.getString("name"));
        role.setDescription(resultSet.getString("description"));
        role.setCreatedBy(resultSet.getInt("created_by"));
        role.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        role.setUpdatedBy(resultSet.getInt("updated_by"));
        role.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        return role;
    }

    @Override
    protected String getIdColumnName() {
        return "role_id";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO roles (name, description, created_by, created_at, updated_by, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE roles SET name = ?, description = ?, updated_by = ?, updated_at = ? WHERE role_id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement preparedStatement, Role entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getDescription());
        preparedStatement.setInt(3, entity.getCreatedBy());
        preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getCreatedAt()));
        preparedStatement.setInt(5, entity.getUpdatedBy());
        preparedStatement.setTimestamp(6, Timestamp.valueOf(entity.getUpdatedAt()));
    }

    @Override
    protected void setUpdateParameters(PreparedStatement preparedStatement, Role entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getDescription());
        preparedStatement.setInt(3, entity.getUpdatedBy());
        preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getUpdatedAt()));
        preparedStatement.setInt(5, entity.getId());
    }
}
