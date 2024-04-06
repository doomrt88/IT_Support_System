package models.entity;

public class RolePermission {
    private String code;
    private int roleId;

    public RolePermission() {}
    public RolePermission(int roleId, String code) {
    	this.roleId = roleId;
    	this.code = code;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
