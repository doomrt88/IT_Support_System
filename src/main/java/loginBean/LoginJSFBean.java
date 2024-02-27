package loginBean;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value="loginBean")
@RequestScoped
public class LoginJSFBean {

	private String username;
    
    private String password;
   
    public void login() {
        FacesMessage message = null;
         
        if(username != null && username.equals("admin") && password != null && password.equals("admin")) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);
        } 
        else {
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
