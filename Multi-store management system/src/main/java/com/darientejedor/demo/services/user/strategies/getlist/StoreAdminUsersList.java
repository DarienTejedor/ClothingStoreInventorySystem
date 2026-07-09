package com.darientejedor.demo.services.user.strategies.getlist;

import com.darientejedor.demo.domain.users.dto.UserResponse;
import com.darientejedor.demo.domain.users.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component("storeAdminUsersList")
public class StoreAdminUsersList implements IGetUserListStrategy{
    private final UserRepository userRepository;

    public StoreAdminUsersList(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserResponse> listUsers(Long storeId,String searchTerm, Pageable pageable){

        if (searchTerm == null || searchTerm.isBlank()) {
            return userRepository.findByActiveTrueAndStoreId(storeId, pageable)
                    .map(UserResponse::new);
        }

        return userRepository.searchUsersByStores(searchTerm, storeId, pageable).map(UserResponse::new);
    }
}
