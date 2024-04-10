package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO 
{
	private Connection connection;
	
	public UsersDAO(Connection connection)
	{
		this.connection = connection;
	}
	
	public boolean isValidLogin(final String userName, final String password)
	{
		PreparedStatement statement = null;
		boolean isValidLogin = false;
		
		try
		{
			statement = connection.prepareStatement("Select * From users Where user_name = ? and password = ?");
			
			statement.setString(1, userName);
			statement.setString(2, password);
			
			ResultSet rs = statement.executeQuery();
			
			if(rs.next())
			{
				isValidLogin = true;
			}
		}
		catch(SQLException ex)
		{
			System.out.println("Exception: "+ex);
		}
		finally
		{
			if(statement!=null)
			{
				try 
				{
					statement.close();
				}
				catch(SQLException ex)
				{
					System.out.println("Exception: "+ex);
				}
			}
		}
		
		return isValidLogin;
	}
}