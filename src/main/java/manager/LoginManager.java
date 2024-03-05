package manager;

import java.sql.Connection;

import dao.UsersDAO;

public class LoginManager 
{
	
	public static boolean validateLogin(final String userName, final String password)
	{
		Connection connection = ConnectionManager.getConnection(); 
		UsersDAO usersDAO = new UsersDAO(connection);
		
		return usersDAO.isValidLogin(userName, password);
	}
}
