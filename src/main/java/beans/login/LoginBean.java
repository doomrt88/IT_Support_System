package beans.login;

import service.UserService;
import entity.User;
import utility.Config;

import java.sql.SQLException;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

//import org.mindrot.jbcrypt.BCrypt;

@Named(value="loginBean")
@RequestScoped
public class LoginBean {

	private String username;
    
    private String password;
   
    public void login() {
        FacesMessage message = null;
        UserService userService = new UserService();
        try {
            //userService.setConnection(Config.getDBUrl());
            if(username != null && password != null) {
                if(userService.authenticateUser(username, password)) {
                    message = new FacesMessage("Welcome " + username);
                    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                    externalContext.redirect(externalContext.getRequestContextPath() + "/Home.xhtml");
                    
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
        } catch (Exception e) {
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
