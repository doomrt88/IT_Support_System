package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.UserRole;

public class UserRoleRepository extends BaseRepository<UserRole> {

    @Override
    protected String getTableName() {
        return "user_roles";
    }

    @Override
    protected UserRole mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        UserRole userRole = new UserRole();
        userRole.setUserId(resultSet.getInt("user_id"));
        userRole.setRoleId(resultSet.getInt("role_id"));
        return userRole;
    }

    @Override
    protected String getIdColumnName() {
        return null; // composite key
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return null; // not needed as it will be a hard delete
    }

    @Override
    protected void setInsertParameters(PreparedStatement preparedStatement, UserRole entity) throws SQLException {
        preparedStatement.setInt(1, entity.getUserId());
        preparedStatement.setInt(2, entity.getRoleId());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement preparedStatement, UserRole entity) throws SQLException {
        
    }

    public boolean insertUserRole(int userId, int roleId, Connection connection) throws SQLException {
        String sql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, roleId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }
}
