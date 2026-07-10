package com.darientejedor.demo.domain.users.dto;

import com.darientejedor.demo.domain.users.User;

public record UserResponse(
        Long id,
        String loginUser,
        String name,
        Long document,
        Long roleId,
        String roleName,
        Long storeId,
        String storeName
) {
    public UserResponse(User user) {
        this(user.getId(),
                user.getLoginUser(),
                user.getName(),
                user.getDocument(),
                user.getRole().getId(),
                user.getRole().getName(),
                user.getStore().getId(),
                user.getStore().getName());
    }
}
