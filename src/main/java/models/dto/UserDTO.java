package models.dto;

@SuppressWarnings("serial")
public class UserDTO extends RegistrationFormDTO {
    private int roleId;
    private int id;

    // 
    public UserDTO() {
    }

    public UserDTO(String username, String password, String confirmPassword, String firstName, String lastName, int roleId) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleId = roleId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
