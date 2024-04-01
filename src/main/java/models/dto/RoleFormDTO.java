package models.dto;

@SuppressWarnings("serial")
public class RoleFormDTO {
    private int id;
    private String name;
    private String description;
    
    public RoleFormDTO() {
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
}
