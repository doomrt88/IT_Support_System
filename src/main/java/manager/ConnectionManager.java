package manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import constants.Constants;

public class ConnectionManager 
{
	public static final String DB_URL = Constants.DB_URL;
	private static Connection connection;
	
	public static Connection getConnection()
	{
		try 
		{
			if(connection == null || connection.isClosed())
			{
				DriverManager.registerDriver (new com.mysql.cj.jdbc.Driver());
				connection = DriverManager.getConnection(DB_URL);
			}
		}
		catch(SQLException ex)
		{
			System.out.println("Exception: "+ex);
		}
		
		return connection;
	}

}
