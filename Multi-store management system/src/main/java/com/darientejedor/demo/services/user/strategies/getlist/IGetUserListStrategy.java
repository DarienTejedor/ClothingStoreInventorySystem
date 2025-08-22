package com.darientejedor.demo.services.user.strategies.getlist;

import com.darientejedor.demo.domain.users.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IGetUserListStrategy {
    Page<UserResponse> listUsers(Long storeId, Pageable pageable);

}
