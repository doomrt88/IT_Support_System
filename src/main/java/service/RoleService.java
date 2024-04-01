package service;

import java.time.LocalDateTime;
import java.util.List;

import dao.RoleRepository;
import entity.Role;

public class RoleService{
	private RoleRepository roleRepository;
	
	public RoleService() {
        this.roleRepository = new RoleRepository();
    }
	
	
	public boolean createRole(Role role) {
        // we can add more business rules here such as any validations. Front end validations should be done in the bean or controller
		role.setCreatedAt(LocalDateTime.now());
		role.setUpdatedAt(LocalDateTime.now());
        return roleRepository.insert(role);
    }

    public boolean updateRole(Role role) {
        // we can add more business rules here such as any validations
    	role.setUpdatedAt(LocalDateTime.now());
    	return roleRepository.update(role);
    }

    public boolean deleteRole(int roleId) {
        // we can add more business rules here such as any validations
        return roleRepository.delete(roleId);
    }
    
    public List<Role> getAllRoles() {
        return roleRepository.getAll();
    }
    
    public boolean roleExists(int id, String name) {
        return roleRepository.getByName(id, name) != null;
    }
}