package models.dto;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class RoleFormDTO {
    private int id;
    private String name;
    private String description;
    private List<String> permissions;
    
    public RoleFormDTO() {
    	this.permissions = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
