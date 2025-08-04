package com.darientejedor.demo.domain.roles.dto;

import com.darientejedor.demo.domain.roles.Role;

public record RoleResponse(
        Long id,
        String name
) {
    public RoleResponse(Role role){
        this(role.getId(),
                role.getName());
    }
}

