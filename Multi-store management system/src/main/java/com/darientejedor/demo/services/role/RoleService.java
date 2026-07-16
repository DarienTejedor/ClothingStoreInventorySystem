package com.darientejedor.demo.services.role;

import com.darientejedor.demo.exceptions.ValidationException;
import com.darientejedor.demo.domain.roles.Role;
import com.darientejedor.demo.domain.roles.dto.RoleData;
import com.darientejedor.demo.domain.roles.dto.RoleResponse;
import com.darientejedor.demo.domain.roles.repository.RoleRepository;
import com.darientejedor.demo.services.user.authentications.IUserAuthentications;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;
    private final IUserAuthentications userAuthentications;

    public RoleService(RoleRepository roleRepository, IUserAuthentications userAuthentications) {
        this.roleRepository = roleRepository;
        this.userAuthentications = userAuthentications;
    }

    //Funcion Get, lista de roles
    @Override
    public Page<RoleResponse> listActiveRoles(Pageable pageable) {
        return roleRepository.findByActiveTrue(pageable).map(RoleResponse::new);
    }

    @Override
    public Page<RoleResponse> listAssignableRoles(Authentication authentication, Pageable pageable) {
        String role = userAuthentications.authRole(authentication);

        return switch (role) {
            case "ROLE_GENERAL_ADMIN" -> roleRepository
                    .findByNameNotAndActiveTrue("GENERAL_ADMIN", pageable)
                    .map(RoleResponse::new);
            case "ROLE_STORE_ADMIN" -> roleRepository
                    .findByNameAndActiveTrue("CASHIER", pageable)
                    .map(RoleResponse::new);
            default -> throw new AccessDeniedException("You don't have permission to assign roles.");
        };
    }

    //Funcion Get, rol por id
    @Override
    public RoleResponse roleResponse(Long id) {
        Role role = validRole(id);
        return new RoleResponse(
                role.getId(),
                role.getName());
    }

    //Funcion POST
    @Override
    public RoleResponse createRole(@Valid RoleData roleData) {
        if (roleRepository.findByName(roleData.name()).isPresent()) {
            throw new ValidationException("Role with this name already exists: " + roleData.name());
        }
        var role = new Role(roleData);
        roleRepository.save(role);
        return new RoleResponse(role);
    }

    @Override
    public RoleResponse updateRole(Long id, @Valid RoleData roleData) {
        Role role = validRole(id);
        role.setName(roleData.name());
        roleRepository.save(role);
        return new RoleResponse(role);
    }

    @Override
    public void deactiveRole(Long id) {
        Role role = validRole(id);
        role.deactiveRole();
        roleRepository.save(role);
    }

    @Override
    public Role validRole(Long id){
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + id));
        if (!role.isActive()){
            throw new ValidationException("Role not found or already inactive with ID: " + id);
        }
        return role;
    }


}

