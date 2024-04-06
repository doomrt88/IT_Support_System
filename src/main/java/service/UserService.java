package service;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import models.dao.DbContext;
import models.dao.UserRepository;
import models.dao.UserRoleRepository;

import java.sql.ResultSet;


import models.entity.User;
import models.entity.UserRole;
import models.dto.UserDTO;

public class UserService {

	private UserRepository userRepository;
	private UserRoleRepository userRoleRepository;
	public UserService() {
        this.userRepository = new UserRepository();
        this.userRoleRepository = new UserRoleRepository();
    }
	
	public boolean createUser(User user) {
        // we can add more business rules here such as any validations. Front end validations should be done in the bean or controller
		// for now, just hash the password
		String hashedPassword = hashPassword(user.getPassword());
        System.out.println("Hashed Password: " + hashedPassword);
        user.setPassword(hashedPassword);
        return userRepository.insert(user);
    }

    public boolean updateUser(User user) {
        // we can add more business rules here such as any validations
    	DbContext dbContextPool = DbContext.getInstance();
        
        // Start a new transaction
        Connection connection = null;
        try {
            connection = dbContextPool.getConnection();
            connection.setAutoCommit(false); // Start transaction
            String hashedPassword = hashPassword(user.getPassword());
            user.setPassword(hashedPassword);
            user.setUpdatedAt(LocalDateTime.now());
            
            boolean updateSuccess = userRepository.update(user, connection);
            if (updateSuccess) {
            	userRoleRepository.deleteUserRoles(user.getId(), connection);
                if (userRoleRepository.insertUserRole(user.getId(), user.getRoleId(), connection)) {
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;
        } catch (SQLException e) {
	        e.printStackTrace();
	        if (connection != null) {
	            try {
	                connection.rollback();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	        return false;
	    } finally {
	        // Release connection
	        if (connection != null) {
	            try {
	                connection.setAutoCommit(true);
	                dbContextPool.releaseConnection(connection);
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
    }

    public boolean deleteUser(int userId) {
        DbContext dbContext = DbContext.getInstance();
        Connection connection = null;
        try {
            connection = dbContext.getConnection();
            connection.setAutoCommit(false);

            String deleteUserRolesSql = "DELETE FROM user_roles WHERE user_id = ?";
            try (PreparedStatement deleteUserRolesStatement = connection.prepareStatement(deleteUserRolesSql)) {
                deleteUserRolesStatement.setInt(1, userId);
                deleteUserRolesStatement.executeUpdate();
            }

            boolean userDeleted = userRepository.delete(userId, connection);

            connection.commit();
            return userDeleted;
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    dbContext.releaseConnection(connection);
                } catch (SQLException releaseException) {
                    releaseException.printStackTrace();
                }
            }
        }
    }

    public boolean changePassword(User user) {
        // we can add more business rules here such as any validations
    	// Get the user's current password from the DB
        User existingUser = userRepository.getById(user.getId());
        
        // Check if the provided current password matches the existing password from the DB
        if (existingUser == null || !BCrypt.checkpw(user.getPassword(), existingUser.getPassword())) {
            return false;
        }
        
        return userRepository.update(user);
    }

    
    public List<UserDTO> getAllUsers() {
    	List<User> users = userRepository.getAll();
    	List<UserDTO> usersDTO = new ArrayList<>();
    	
        for (User user : users) {
            UserRole userRole = userRoleRepository.getByUserId(user.getId());
            if(userRole != null) {
                user.setRoleId(userRole.getRoleId());
            }
            
            usersDTO.add(mapUserEntityToDTO(user));
        }
        
        return usersDTO;
    }

    public User getUserById(int userId) {
        return userRepository.getById(userId);
    }
    
    public boolean userExists(int userId, String username) {
        return userRepository.getByUsername(userId, username) != null;
    }
	
	public boolean authenticateUser(String username, String password) {
        DbContext dbContextPool = DbContext.getInstance();
        String sql = "SELECT password FROM users WHERE user_name = ?";
        Connection connection = null; 
        
        try {
        		connection = dbContextPool.getConnection();
        		PreparedStatement preparedStatement = connection.prepareStatement(sql);
        	    preparedStatement.setString(1, username);
        	    try (ResultSet resultSet = preparedStatement.executeQuery()) {
        	        
        	        if (resultSet.next()) {
        	            String hashedPassword = resultSet.getString("password");
        	            return BCrypt.checkpw(password, hashedPassword);
        	        } else {
        	            return false; // User not found
        	        }
        	    }
        	} catch (SQLException e) {
        	    e.printStackTrace();
        	    return false;
        	} finally {
                if (connection != null) {
                    DbContext.getInstance().releaseConnection(connection);
                }
            }
    }
	
	public boolean registerUser(String username, String password, String firstName, String lastName, int roleId) {
        String hashedPassword = hashPassword(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRoleId(roleId);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        
        DbContext dbContextPool = DbContext.getInstance();
        
        // Start a new transaction
        Connection connection = null;
        try {
            connection = dbContextPool.getConnection();
            connection.setAutoCommit(false); // Start transaction

            int userId = userRepository.insert(user, connection);
            if (userId != -1) {
                if (userRoleRepository.insertUserRole(userId, roleId, connection)) {
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;
        } catch (SQLException e) {
	        e.printStackTrace();
	        if (connection != null) {
	            try {
	                connection.rollback();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	        return false;
	    } finally {
	        // Release connection
	        if (connection != null) {
	            try {
	                connection.setAutoCommit(true);
	                dbContextPool.releaseConnection(connection);
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
    }
	
	
	private String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt(10));
	}
	
	private UserDTO mapUserEntityToDTO(User user){
		UserDTO userDTO = new UserDTO();
        
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setRoleId(user.getRoleId());
        
        return userDTO;
    }
}
