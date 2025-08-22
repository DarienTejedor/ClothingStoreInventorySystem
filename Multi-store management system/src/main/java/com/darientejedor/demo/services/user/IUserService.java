package com.darientejedor.demo.services.user;

import com.darientejedor.demo.domain.users.User;
import com.darientejedor.demo.domain.users.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface IUserService {

    public Page<UserResponse> listActiveUsers(Authentication authentication, Long id,Pageable pageable);

    public UserResponse userResponse(Long id, Authentication authentication);

    public UserResponse createUser(UserData userData, Authentication authentication);

    public UserResponse updateUserInfo(Long id, UpdateUserInformation userInformation, Authentication authentication);

    public UserResponse changePassword(Long id, PasswordUpdateData updatePassword, Authentication authentication);

    public UserResponse updateRoleAndStore(Long id, UpdateRoleAndStoreData updateRoleAndStoreData);

    public void deactiveUser(Long id, Authentication authentication);

}
