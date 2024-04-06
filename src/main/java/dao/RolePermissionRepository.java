package dao;

import entity.RolePermission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RolePermissionRepository extends BaseRepository<RolePermission> {

    @Override
    protected String getTableName() {
        return "role_permissions";
    }

    @Override
    protected RolePermission mapResultSetToEntity(ResultSet resultSet) throws SQLException {
    	RolePermission rolePermission = new RolePermission();
    	rolePermission.setCode(resultSet.getString("code"));
    	rolePermission.setRoleId(resultSet.getInt("role_id"));
    	return rolePermission;
    }

    @Override
    protected String getIdColumnName() {
        return null; 
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO role_permissions (role_id, code) VALUES (?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return null; 
    }

    @Override
    protected void setInsertParameters(PreparedStatement preparedStatement, RolePermission entity) throws SQLException {
        preparedStatement.setInt(1, entity.getRoleId());
        preparedStatement.setString(2, entity.getCode());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement preparedStatement, RolePermission entity) throws SQLException {

    }
    
    public void bulkInsert(int roleId, List<String> permissions, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(getInsertSql());

            for (String permission : permissions) {
            	RolePermission rolePermission = new RolePermission(roleId, permission);
                setInsertParameters(preparedStatement, rolePermission);
                preparedStatement.addBatch(); // Add the prepared statement to the batch
            }

            preparedStatement.executeBatch();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }
    
    public boolean deleteRolePermissions(int roleId, Connection connection) throws SQLException {
        String sql = "DELETE FROM role_permissions WHERE role_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, roleId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

}
