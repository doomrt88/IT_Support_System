package models.dto;

import java.util.List;

import models.entity.Permission;

public class GroupedPermission {
    private String groupName;
    private List<Permission> permissions;

    public GroupedPermission() {
    }
    
    public GroupedPermission(String groupName, List<Permission> permissions) {
        this.groupName = groupName;
        this.permissions = permissions;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
