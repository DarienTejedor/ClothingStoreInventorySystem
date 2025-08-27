package com.darientejedor.demo.services.role;


import com.darientejedor.demo.domain.roles.Role;
import com.darientejedor.demo.domain.roles.dto.RoleData;
import com.darientejedor.demo.domain.roles.dto.RoleResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRoleService {

    Page<RoleResponse> listActiveRoles(Pageable pageable);

    RoleResponse roleResponse(Long id);

    RoleResponse createRole(@Valid RoleData roleData);

    RoleResponse updateRole(Long id, @Valid RoleData roleData);

    void deactiveRole(Long id);

    Role validRole(Long id);
}
