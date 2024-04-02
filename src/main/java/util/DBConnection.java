package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public abstract class DBConnection {
	
	 private Connection connection;

	    public Connection connect() {

	        Connection connection = null;
	        final String URL = "jdbc:mysql://localhost:3306/itsm?serverTimezone=UTC";
	        final String USERNAME = "root";
	        final String PASSWORD = "root";

	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	        } catch (ClassNotFoundException | SQLException e) {
	            System.out.println(e.getMessage());
	        }
	        return connection;
	    }

}
