package com.darientejedor.demo.domain.roles.dto;

import com.darientejedor.demo.domain.roles.Role;
import com.darientejedor.demo.domain.users.User;

public record RoleResponse(
        Long id,
        String name
) {
    public RoleResponse(Role role){
        this(role.getId(),
                role.getName());
    }
}

