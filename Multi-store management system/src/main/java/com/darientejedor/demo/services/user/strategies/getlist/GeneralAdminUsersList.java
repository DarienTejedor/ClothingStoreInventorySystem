package com.darientejedor.demo.services.user.strategies.getlist;

import com.darientejedor.demo.domain.users.dto.UserResponse;
import com.darientejedor.demo.domain.users.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component("generalAdminUsersList")
public class GeneralAdminUsersList implements IGetUserListStrategy{
    private final UserRepository userRepository;

    public GeneralAdminUsersList(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserResponse> listUsers(Long storeId, String searchTerm,Pageable pageable){

        //Sin filtrar por tienda
        if (storeId == null){
            if (searchTerm == null || searchTerm.isBlank()) {
                return userRepository.findByActiveTrue(pageable)
                        .map(UserResponse::new);
            }
             return userRepository.searchUsers(searchTerm.trim(), pageable).map(com.darientejedor.demo.domain.users.dto.UserResponse::new);

        }
        //con filtro por tienda
        if (searchTerm == null || searchTerm.isBlank()){
            return userRepository.findByActiveTrueAndStoreId(storeId, pageable)
                    .map(UserResponse::new);
        }
        return userRepository.searchUsersByStores(searchTerm.trim(), storeId, pageable).map(UserResponse::new);

    }
}
