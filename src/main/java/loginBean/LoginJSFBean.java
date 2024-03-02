package loginBean;

import service.UserService;
import entity.User;
import utility.Config;

import java.sql.SQLException;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.mindrot.jbcrypt.BCrypt;

@Named(value="loginBean")
@RequestScoped
public class LoginJSFBean {

	private String username;
    
    private String password;
   
    public void login() {
        FacesMessage message = null;
        UserService userService = new UserService();
        try {
            userService.setConnection(Config.getDBUrl());
            if(username != null) {
                User user = userService.getUserByUsername(username);
                if(user != null && BCrypt.checkpw(password, user.getPassword())) {
                    message = new FacesMessage("Welcome " + username);
                }else {
                    message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Your username or password are invalid");
                }
            }else {
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials");
            }
	        //if(username != null && username.equals("admin") && password != null && password.equals("admin")) {
	            //message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);
	        //}
	        //else {
	            //message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials");
	        //}

	        FacesContext.getCurrentInstance().addMessage("loginForm:signIn", message);
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }   
    
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }

}
