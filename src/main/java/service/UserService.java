package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.ResultSet;

import entity.User;

public class UserService {

	//private String url = System.getenv("ITSM_MYSQL_URL");
	private Connection connection;
	
	public void setConnection(String url) throws SQLException {
		System.out.println("DB URL"+url);
		this.connection = DriverManager.getConnection(url);
	}
	
	public User getUserByUsername(String username) throws SQLException {
		
		//DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
		//con = DriverManager.getConnection(url);
		String query = "select * from users where user_name = ?";
		
		PreparedStatement selectUserbyUsername = connection.prepareStatement(query);
		selectUserbyUsername.setString(1, username);
		ResultSet rs= selectUserbyUsername.executeQuery();
		if(rs.next()) {
			User user = new User();
			user.setId(rs.getInt("user_id"));
			user.setUsername(rs.getString("user_name"));
			user.setPassword(rs.getString("password"));
			user.setFirstName(rs.getString("first_name"));
			user.setLastName(rs.getString("last_name"));
			user.setCreatedBy(rs.getInt("created_by"));
			return user;
		}
		return null;
	}
}
