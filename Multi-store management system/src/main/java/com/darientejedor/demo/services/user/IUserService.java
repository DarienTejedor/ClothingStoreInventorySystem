package com.darientejedor.demo.services.user;

import com.darientejedor.demo.domain.users.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface IUserService {

    Page<UserResponse> listActiveUsers(Authentication authentication, Long id,Pageable pageable);

    UserResponse userResponse(Long id, Authentication authentication);

    UserResponse createUser(UserData userData, Authentication authentication);

    UserResponse updateUserInfo(Long id, UpdateUserInformation userInformation, Authentication authentication);

    UserResponse changePassword(Long id, PasswordUpdateData updatePassword, Authentication authentication);

    UserResponse updateRoleAndStore(Long id, UpdateRoleAndStoreData updateRoleAndStoreData);

    void deactiveUser(Long id, Authentication authentication);

}
