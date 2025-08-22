package com.darientejedor.demo.services.user.strategies.getlist;

import com.darientejedor.demo.domain.users.dto.UserResponse;
import com.darientejedor.demo.domain.users.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class GeneralAdminUsersList implements IGetUserListStrategy{
    private final UserRepository userRepository;

    public GeneralAdminUsersList(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserResponse> listUsers(Long storeId, Pageable pageable){
        return userRepository.findByActiveTrue(pageable).map(com.darientejedor.demo.domain.users.dto.UserResponse::new);
    }
}
