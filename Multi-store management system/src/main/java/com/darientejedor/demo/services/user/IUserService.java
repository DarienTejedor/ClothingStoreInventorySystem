package com.darientejedor.demo.services.user;

import com.darientejedor.demo.domain.users.User;
import com.darientejedor.demo.domain.users.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {

    public UserResponse createUser(UserData userData);

    public Page<UserResponse> listActiveUsers(Pageable pageable);

    public UserResponse userResponse(Long id);

    public UserResponse updateUserInfo(Long id, UpdateUserInformation userInformation);

    public UserResponse updateRoleAndStore(Long id, UpdateRoleAndStoreData updateRoleAndStoreData);

    public UserResponse changePassword(Long id, PasswordUpdateData updatePassword);

    public void deactiveUser(Long id);
}
