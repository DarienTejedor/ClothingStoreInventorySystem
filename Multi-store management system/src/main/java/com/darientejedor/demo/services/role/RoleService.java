package com.darientejedor.demo.services.role;

import com.darientejedor.demo.domain.roles.Role;
import com.darientejedor.demo.domain.roles.dto.RoleData;
import com.darientejedor.demo.domain.roles.dto.RoleResponse;
import com.darientejedor.demo.domain.roles.repository.IRoleService;
import com.darientejedor.demo.domain.roles.repository.RoleRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class RoleService implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

    //Funcion Get, lista de roles
    @Override
    public Page<RoleResponse> listActiveRoles(Pageable pageable) {
        return roleRepository.findByActiveTrue(pageable).map(RoleResponse::new);
    }

    //Funcion Get, rol por id
    @Override
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
    @Override
    public RoleResponse createRole(@Valid RoleData roleData) {
        if (roleRepository.findByName(roleData.name()).isPresent()) {
            throw new IllegalArgumentException("Role with this name already exists: " + roleData.name());
        }
        var role = new Role(roleData);
        roleRepository.save(role);
        return new RoleResponse(role);
    }

    @Override
    public RoleResponse updateRole(Long id, @Valid RoleData roleData) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + id));
        if (!role.isActive()){
            throw new IllegalArgumentException("Role not found or inactive with ID: " + id);
        }
        role.setName(roleData.name());
        roleRepository.save(role);
        return new RoleResponse(role);
    }

    @Override
    public void deactiveRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + id));
        if (!role.isActive()){
            throw new IllegalArgumentException("Role not found or already inactive with ID: " + id);
        }
        role.deactiveRole();
        roleRepository.save(role);
    }
}

