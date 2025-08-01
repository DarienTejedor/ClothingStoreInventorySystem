package com.darientejedor.demo.domain.dtos;

import com.darientejedor.demo.domain.users.User;

public record UserResponse(
        Long id,
        String loginUser,
        String name,
        Long document,
        Long roleId,
        Long storeId
) {
    public UserResponse(User user) {
        this(user.getId(),
                user.getLoginUser(),
                user.getName(),
                user.getDocument(),
                user.getRole().getId()
                , user.getStore().getId());
    }
}
