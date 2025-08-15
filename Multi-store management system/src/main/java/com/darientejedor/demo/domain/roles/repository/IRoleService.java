package com.darientejedor.demo.domain.roles.repository;


import com.darientejedor.demo.domain.roles.dto.RoleData;
import com.darientejedor.demo.domain.roles.dto.RoleResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRoleService {

    public Page<RoleResponse> listActiveRoles(Pageable pageable);

    public RoleResponse roleResponse(Long id);

    public RoleResponse createRole(@Valid RoleData roleData);

    public RoleResponse updateRole(Long id, @Valid RoleData roleData);

    public void deactiveRole(Long id);
}
