package beans.administration;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import entity.User;
import service.UserService;

@SuppressWarnings("serial")
@Named("userAdministrationBean")
@RequestScoped
public class UserAdministration implements Serializable {

    private UserService userService = null;
    private List<User> userList;
    private boolean showCreateForm = false;
    
    @PostConstruct
    public void init() {
        userService = new UserService();
        userList = userService.getAllUsers();
        showCreateForm = false; 
    }
    
  
    public void setShowCreateForm(boolean showCreateForm) {
        this.showCreateForm = showCreateForm;
    }
    
    public boolean getShowCreateForm() {
        return this.showCreateForm;
    }


    public void toggleView() {
        showCreateForm = !showCreateForm;
    }

    
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
    
    
}