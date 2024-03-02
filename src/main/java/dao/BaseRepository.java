package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

        try (Connection connection = DbContext.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                entities.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    public T getById(int id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";
        try (Connection connection = DbContext.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToEntity(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(T entity) {
        String sql = getInsertSql();
        try (Connection connection = DbContext.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            setInsertParameters(preparedStatement, entity);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

	public int insert(T entity, Connection connection) throws SQLException {
		String sql = getInsertSql();
	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	        setInsertParameters(preparedStatement, entity);
	        int rowsAffected = preparedStatement.executeUpdate();
	        if (rowsAffected == 1) {
	            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                return generatedKeys.getInt(1);
	            }
	        }
	        return -1; 
	    }
	}

    public boolean update(T entity) {
        String sql = getUpdateSql();
        try (Connection connection = DbContext.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            setUpdateParameters(preparedStatement, entity);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";
        try (Connection connection = DbContext.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}