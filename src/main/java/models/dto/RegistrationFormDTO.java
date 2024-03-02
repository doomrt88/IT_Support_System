package models.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RegistrationFormDTO implements Serializable {
    private String username;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;

    // 
    public RegistrationFormDTO() {
    }

    public RegistrationFormDTO(String username, String password, String confirmPassword, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
