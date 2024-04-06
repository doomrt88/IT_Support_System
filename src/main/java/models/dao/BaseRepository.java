package models.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utility.DbUtils;

public abstract class BaseRepository<T> {

    protected abstract String getTableName();
    protected abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;
    protected abstract String getIdColumnName();

    protected abstract String getInsertSql();
    protected abstract String getUpdateSql();
    protected abstract void setInsertParameters(PreparedStatement preparedStatement, T entity) throws SQLException;
    protected abstract void setUpdateParameters(PreparedStatement preparedStatement, T entity) throws SQLException;
    
    public List<T> getAll() {
        List<T> entities = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();

        Connection connection = null; 
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DbContext.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            		
            while (resultSet.next()) {
            	//test.add(resultSet);
                entities.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                DbContext.getInstance().releaseConnection(connection);
            }
            
            DbUtils.closeResultSet(resultSet);
            DbUtils.closePreparedStatement(preparedStatement);
        }
        return entities;
    }

    public T getById(int id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";
        Connection connection = null; 
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DbContext.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToEntity(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                DbContext.getInstance().releaseConnection(connection);
            }
            
            DbUtils.closeResultSet(resultSet);
            DbUtils.closePreparedStatement(preparedStatement);
        }
        return null;
    }

    public boolean insert(T entity) {
        String sql = getInsertSql();
        Connection connection = null; 
        PreparedStatement preparedStatement = null;

        try {
            connection = DbContext.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            setInsertParameters(preparedStatement, entity);
            
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                DbContext.getInstance().releaseConnection(connection);
            }
            
            DbUtils.closePreparedStatement(preparedStatement);
        }
    }


    public int insert(T entity, Connection connection) throws SQLException {
        String sql = getInsertSql();
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        int generatedId = -1;

        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setInsertParameters(preparedStatement, entity);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 1) {
                generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                }
            }
            return generatedId;
        } finally {
            if (generatedKeys != null) {
                generatedKeys.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }


    public boolean update(T entity) {
        String sql = getUpdateSql();
        Connection connection = null; 
        PreparedStatement preparedStatement = null;
        try {
            connection = DbContext.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);

            setUpdateParameters(preparedStatement, entity);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            
            if (connection != null) {
                DbContext.getInstance().releaseConnection(connection);
            }
            
            DbUtils.closePreparedStatement(preparedStatement);
        }
    }

    public boolean update(T entity, Connection connection) throws SQLException {
        String sql = getUpdateSql();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            setUpdateParameters(preparedStatement, entity);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    
    public boolean delete(int id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";
        Connection connection = null; 
        PreparedStatement preparedStatement = null;
        try {
            connection = DbContext.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            
            if (connection != null) {
                DbContext.getInstance().releaseConnection(connection);
            }
            
            DbUtils.closePreparedStatement(preparedStatement);
        }
    }

    public boolean delete(int id, Connection connection) throws SQLException {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

}