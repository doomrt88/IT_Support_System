package models.dao;

import models.entity.Permission;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PermissionRepository extends BaseRepository<Permission> {

    @Override
    protected String getTableName() {
        return "permissions";
    }

    @Override
    protected Permission mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Permission permission = new Permission();
        permission.setCode(resultSet.getString("code"));
        permission.setName(resultSet.getString("name"));
        permission.setGroupName(resultSet.getString("group_name"));
        return permission;
    }

    @Override
    protected String getIdColumnName() {
        return null;
    }

    @Override
    protected String getInsertSql() {
        return null; 
    }

    @Override
    protected String getUpdateSql() {
        return null; 
    }

    @Override
    protected void setInsertParameters(PreparedStatement preparedStatement, Permission entity) throws SQLException {
        
    }

    @Override
    protected void setUpdateParameters(PreparedStatement preparedStatement, Permission entity) throws SQLException {
        
    }
}
