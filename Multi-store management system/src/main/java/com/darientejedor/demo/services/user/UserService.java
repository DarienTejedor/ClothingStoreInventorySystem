package com.darientejedor.demo.services.user;


import com.darientejedor.demo.domain.exceptions.ValidationException;
import com.darientejedor.demo.domain.roles.repository.RoleRepository;
import com.darientejedor.demo.domain.stores.repository.StoreRepository;
import com.darientejedor.demo.domain.users.dto.*;
import com.darientejedor.demo.domain.users.repository.UserRepository;
import com.darientejedor.demo.domain.roles.Role;
import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.users.User;
import com.darientejedor.demo.services.role.IRoleService;
import com.darientejedor.demo.services.store.IStoreService;
import com.darientejedor.demo.services.user.authentications.IUserAuthentications;
import com.darientejedor.demo.services.user.strategies.getlist.GeneralAdminUsersList;
import com.darientejedor.demo.services.user.strategies.getlist.GetUsersListStrategyFactory;
import com.darientejedor.demo.services.user.strategies.getlist.IGetUserListStrategy;
import com.darientejedor.demo.services.user.validations.IUserValidations;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;
    private final IStoreService storeService;
    private final IRoleService roleService;
    private final GetUsersListStrategyFactory listUsersStrategyFactory;
    private final IUserValidations userValidations;
    private final IUserAuthentications userAuthentications;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       StoreRepository storeRepository,
                       PasswordEncoder passwordEncoder,
                       IStoreService storeService,
                       IRoleService roleService,
                       GetUsersListStrategyFactory listUsersStrategyFactory,
                       IUserValidations userValidations,
                       IUserAuthentications userAuthentications) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.storeRepository = storeRepository;
        this.passwordEncoder = passwordEncoder;
        this.storeService = storeService;
        this.roleService = roleService;
        this.listUsersStrategyFactory = listUsersStrategyFactory;
        this.userValidations = userValidations;
        this.userAuthentications = userAuthentications;
    }


    @Override
    public Page<UserResponse> listActiveUsers(Authentication authentication, Long storeId,Pageable pageable){
        String authRole = userAuthentications.authRole(authentication);
        IGetUserListStrategy strategy = listUsersStrategyFactory.userListStrategy(authRole, storeId);
        if (!(strategy instanceof GeneralAdminUsersList)){
                storeService.validStore(storeId);
        }
        return strategy.listUsers(storeId, pageable);
    }

    @Override
    public UserResponse userResponse(Long id, Authentication authentication){
        User authUser = userAuthentications.authUser(authentication);
        String role = userAuthentications.authRole(authentication);
        User user = userValidations.validUser(id);

        switch (role) {
            case "ROLE_GENERAL_ADMIN":
                break;
            case "ROLE_STORE_ADMIN":
                if (!user.getStore().getId().equals(authUser.getStore().getId())) {
                    throw new AccessDeniedException("You can only view users from your own store.");
                }
                break;
            case "ROLE_CASHIER":
                if (!user.getId().equals(authUser.getId())) {
                    throw new IllegalArgumentException("You can only view your own user profile.");
                }
                break;
            default:
                throw new ValidationException("Invalid user role: " + role);
        }
        return new UserResponse(user);
    }

    @Override
    public UserResponse createUser(UserData userData, Authentication authentication){
        User authUser = userAuthentications.authUser(authentication);
        String authRole = userAuthentications.authRole(authentication);

        switch (authRole){
            case "ROLE_GENERAL_ADMIN":
                break;
            case "ROLE_STORE_ADMIN":
                if (!(authUser.getStore().getId().equals(userData.storeId()))){
                    throw new AccessDeniedException("You can only create users from your own store.");
                }
            default:
                throw new IllegalArgumentException("Invalid user role.");
        }
        if (userRepository.findByDocument(userData.document()).isPresent()){
            throw new ValidationException("User with this document already exists: " + userData.document());
        }

        var hashedPassword = passwordEncoder.encode(userData.password());
        Role role = roleService.validRole(userData.roleId());
        Store store = storeService.validStore(userData.storeId());
        var user = new User(userData, role, store, hashedPassword);
        userRepository.save(user);
        return new UserResponse(user);
    }

    @Override
    public UserResponse updateUserInfo(Long id, UpdateUserInformation userInformation, Authentication authentication) {
        User authUser = userAuthentications.authUser(authentication);
        String authRole = userAuthentications.authRole(authentication);
        User user = userValidations.validUser(id);

        switch (authRole){
            case "ROLE_GENERAL_ADMIN":
                break;
            case "ROLE_STORE_ADMIN":
                if (!(authUser.getStore().getId().equals(user.getStore().getId()))){
                    throw new AccessDeniedException("You can only update users from your own store.");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid user role.");
        }
        user.setName(userInformation.name());
        user.setDocument(userInformation.document());
        userRepository.save(user);
        return new UserResponse(user);
    }

    @Override
    public UserResponse changePassword(Long id, PasswordUpdateData updatePassword, Authentication authentication){
        User authUser = userAuthentications.authUser(authentication);
        User user = userValidations.validUser(id);

        if (!(authUser.getId().equals(id))){
            throw new AccessDeniedException("You can only update your own password.");
        }

        if (user.getPassword() == null || !(passwordEncoder.matches(updatePassword.oldPassword(), user.getPassword()))){
            throw new ValidationException("The old password provided is incorrect.");
        }

        var hashedPassword = passwordEncoder.encode(updatePassword.newPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return new UserResponse(user);
    }


    @Override
    public UserResponse updateRoleAndStore(Long id, UpdateRoleAndStoreData updateRoleAndStoreData){
        User user = userValidations.validUser(id);
        Role role = roleService.validRole(updateRoleAndStoreData.roleId());
        Store store = storeService.validStore(updateRoleAndStoreData.storeId());
        user.setRole(role);
        user.setStore(store);
        userRepository.save(user);
        return new UserResponse(user);
    }


    @Override
    public void deactiveUser(Long id, Authentication authentication) {
        User authUser = userAuthentications.authUser(authentication);
        String authRole = userAuthentications.authRole(authentication);
        User user = userValidations.validUser(id);

        switch (authRole){
            case "ROLE_GENERAL_ADMIN":
                break;
            case "ROLE_STORE_ADMIN":
                if (!(authUser.getStore().getId().equals(user.getStore().getId()))){
                    throw new AccessDeniedException("You can only deactivate users from your own store.");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid user role.");
        }

        user.deactiveUser();
        userRepository.save(user);
    }


}
