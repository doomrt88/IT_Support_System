package beans.login;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import manager.LoginManager;

@Named(value="loginBean")
@RequestScoped
public class LoginBean 
{
	private String username;
    private String password;
   
    public void login() 
    {
        FacesMessage message = null;
        boolean isValidLogin = LoginManager.validateLogin(getUsername(), getPassword());
         
        if(isValidLogin) 
        {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", "Welcome "+getUsername());
        } 
        else 
        {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials");
        }
         
        FacesContext.getCurrentInstance().addMessage("loginForm:signIn", message);
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