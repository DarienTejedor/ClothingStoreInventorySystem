package com.darientejedor.demo.services.role;

import com.darientejedor.demo.domain.roles.Role;
import com.darientejedor.demo.domain.roles.dto.RoleData;
import com.darientejedor.demo.domain.roles.dto.RoleResponse;
import com.darientejedor.demo.domain.roles.repository.RoleRepository;
import com.darientejedor.demo.domain.users.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    //Funcion Get, lista de roles
    public Page<Role> listActiveRoles(Pageable pageable) {
        return roleRepository.findByActivoTrue(pageable);
    }

    //Funcion Get, rol por id
    public RoleResponse roleResponse(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Role not found with ID: " + id));
        if (!role.isActive()){
            throw new IllegalArgumentException("Role not found or inactive with ID: " + id);
        }
        return new RoleResponse(
                role.getId(),
                role.getName());
    }


    //Funcion POST
    public void createRole(@Valid RoleData roleData) {
        if (roleRepository.findByName(roleData.name()).isPresent()) {
            throw new IllegalArgumentException("Role with this name already exists: " + roleData.name());
        }
        var role = new Role(roleData);
        roleRepository.save(role);
    }




    public RoleResponse updateRole(Long id, @Valid RoleData roleData) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + id));
        role.setName(roleData.name());
        roleRepository.save(role);
        return new RoleResponse(role);
    }

    public void deactiveRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + id));
        role.deactiveRole();
        roleRepository.save(role);
    }
}

