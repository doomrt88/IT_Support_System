package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import entity.User;

public class UserRepository extends BaseRepository<User> {

	@Override
    protected String getTableName() {
        return "users";
    }

    @Override
    protected User mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setUsername(resultSet.getString("user_name"));
        user.setPassword(resultSet.getString("password"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setCreatedBy(resultSet.getInt("created_by"));
        user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedBy(resultSet.getInt("updated_by"));
        user.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        return user;
    }

    @Override
    protected String getIdColumnName() {
        return "user_id";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO users (user_name, password, first_name, last_name, created_by, created_at, updated_by, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE users SET user_name = ?, password = ?, first_name = ?, last_name = ?, updated_by = ?, updated_at = ? WHERE user_id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement preparedStatement, User entity) throws SQLException {
        preparedStatement.setString(1, entity.getUsername());
        preparedStatement.setString(2, entity.getPassword());
        preparedStatement.setString(3, entity.getFirstName());
        preparedStatement.setString(4, entity.getLastName());
        preparedStatement.setInt(5, entity.getCreatedBy());
        preparedStatement.setTimestamp(6, Timestamp.valueOf(entity.getCreatedAt()));
        preparedStatement.setInt(7, entity.getUpdatedBy());
        preparedStatement.setTimestamp(8, Timestamp.valueOf(entity.getUpdatedAt()));
    }

    @Override
    protected void setUpdateParameters(PreparedStatement preparedStatement, User entity) throws SQLException {
        preparedStatement.setString(1, entity.getUsername());
        preparedStatement.setString(2, entity.getPassword());
        preparedStatement.setString(3, entity.getFirstName());
        preparedStatement.setString(4, entity.getLastName());
        preparedStatement.setInt(5, entity.getUpdatedBy());
        preparedStatement.setTimestamp(6, Timestamp.valueOf(entity.getUpdatedAt()));
        preparedStatement.setInt(7, entity.getId());
    }
    
    public User getByUsername(int userId, String username) {
        String sql = "SELECT * FROM users WHERE user_name= ? AND user_id <> ?";
        Connection connection = null;
        try {
        	connection = DbContext.getInstance().getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, userId);
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
}
