package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.DbContext;
import dao.PermissionRepository;
import dao.RolePermissionRepository;
import dao.RoleRepository;
import entity.Permission;
import entity.Role;
import entity.RolePermission;
import models.dto.GroupedPermission;
import models.dto.RoleFormDTO;

public class RoleService{
	private RoleRepository roleRepository;
	private PermissionRepository permissionRepository;
	private RolePermissionRepository rolePermissionRepository;
	
	public RoleService() {
        this.roleRepository = new RoleRepository();
        this.permissionRepository = new PermissionRepository();
        this.rolePermissionRepository = new RolePermissionRepository();
    }
	
	
	public boolean createRole(Role role, List<String> permissions) {
	    DbContext dbContext = DbContext.getInstance();
	    Connection connection = null;

	    try {
	        connection = dbContext.getConnection();
	        connection.setAutoCommit(false);

	        LocalDateTime currentDateTime = LocalDateTime.now();
	        role.setCreatedAt(currentDateTime);
	        role.setUpdatedAt(currentDateTime);
	        
	        int roleId = roleRepository.insert(role, connection);
	        if (roleId != -1) {
	            rolePermissionRepository.bulkInsert(roleId, permissions, connection);
	            connection.commit();
	            return true;
	        } else {
	            connection.rollback();
	            return false;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        if (connection != null) {
	            try {
	                connection.rollback();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	        return false;
	    } finally {
	        // Release connection
	        if (connection != null) {
	            try {
	                connection.setAutoCommit(true);
	                dbContext.releaseConnection(connection);
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}


	public boolean updateRole(Role role, List<String> permissions) {
		// we can add more business rules here such as any validations
    	DbContext dbContext = DbContext.getInstance();
        
        // Start a new transaction
        Connection connection = null;
        try {
            connection = dbContext.getConnection();
            connection.setAutoCommit(false); // Start transaction
            role.setUpdatedAt(LocalDateTime.now());

	        // Update the role
	        boolean roleUpdated = roleRepository.update(role, connection);

	        if (roleUpdated) {
	            rolePermissionRepository.deleteRolePermissions(role.getId(), connection);
	            
	            // Insert new role permissions
	            rolePermissionRepository.bulkInsert(role.getId(), permissions, connection);

	            // Commit transaction
	            connection.commit();
	            return true;
	        } else {
	            connection.rollback();
	            return false;
	        }
        } catch (SQLException e) {
	        e.printStackTrace();
	        if (connection != null) {
	            try {
	                connection.rollback();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	        return false;
	    } finally {
	        // Release connection
	        if (connection != null) {
	            try {
	                connection.setAutoCommit(true);
	                dbContext.releaseConnection(connection);
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}


    public boolean deleteRole(int roleId) {
        // we can add more business rules here such as any validations
    	DbContext dbContext = DbContext.getInstance();
        
        // Start a new transaction
        Connection connection = null;
        try {
            connection = dbContext.getConnection();
            connection.setAutoCommit(false); // Start transaction
            
            // delete permissions first
            rolePermissionRepository.deleteRolePermissions(roleId, connection);
            
	        // Delete the role
	        boolean roleDeleted = roleRepository.delete(roleId, connection);

	        if (roleDeleted) {
	            

	            // Commit transaction
	            connection.commit();
	            return true;
	        } else {
	            connection.rollback();
	            return false;
	        }
        } catch (SQLException e) {
	        e.printStackTrace();
	        if (connection != null) {
	            try {
	                connection.rollback();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	        return false;
	    } finally {
	        // Release connection
	        if (connection != null) {
	            try {
	                connection.setAutoCommit(true);
	                dbContext.releaseConnection(connection);
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
    }
    
    public List<Role> getAllRoles() {
        return roleRepository.getAll();
    }
    
    public List<RoleFormDTO> getAllRolesWithPermissions() {
    	List<RoleFormDTO> roles = new ArrayList<>();
    	List<RolePermission> allRolePermissions = rolePermissionRepository.getAll();
    	
    	for (Role role: getAllRoles()) {
    		RoleFormDTO roleForm = new RoleFormDTO();
    		roleForm.setId(role.getId());
    		roleForm.setName(role.getName());
    		roleForm.setDescription(role.getDescription());
    		
    		List<String> rolePermissions = new ArrayList<>();
            for (RolePermission rp : allRolePermissions) {
                if (rp.getRoleId() == role.getId()) {
                    rolePermissions.add(rp.getCode());
                }
            }
            roleForm.setPermissions(rolePermissions);
            
            roles.add(roleForm);
        }
    	
        return roles;
    }
    
    public boolean roleExists(int id, String name) {
        return roleRepository.getByName(id, name) != null;
    }
    
    public List<GroupedPermission> getAllPermissionsGrouped() {
        List<Permission> allPermissions = permissionRepository.getAll();
        Map<String, List<Permission>> permissionGroups = new HashMap<>();

        for (Permission permission : allPermissions) {
            String groupName = permission.getGroupName();
            if (!permissionGroups.containsKey(groupName)) {
                permissionGroups.put(groupName, new ArrayList<>());
            }
            permissionGroups.get(groupName).add(permission);
        }


        List<GroupedPermission> groupedPermissions = new ArrayList<>();
        for (Map.Entry<String, List<Permission>> entry : permissionGroups.entrySet()) {
            GroupedPermission groupedPermission = new GroupedPermission(entry.getKey(), entry.getValue());
            groupedPermissions.add(groupedPermission);
        }

        return groupedPermissions;
    }
}