package com.darientejedor.demo.services.user;


import com.darientejedor.demo.exceptions.ValidationException;
import com.darientejedor.demo.domain.users.dto.*;
import com.darientejedor.demo.domain.users.repository.UserRepository;
import com.darientejedor.demo.domain.roles.Role;
import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.users.User;
import com.darientejedor.demo.services.role.IRoleService;
import com.darientejedor.demo.services.store.validations.IStoreValidations;
import com.darientejedor.demo.services.user.authentications.IUserAuthentications;
import com.darientejedor.demo.services.user.strategies.getlist.GetUsersListStrategyFactory;
import com.darientejedor.demo.services.user.strategies.getlist.IGetUserListStrategy;
import com.darientejedor.demo.services.user.validations.IUserValidations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;


@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleService roleService;
    private final GetUsersListStrategyFactory listUsersStrategyFactory;
    private final IUserValidations userValidations;
    private final IUserAuthentications userAuthentications;
    private final IStoreValidations storeValidations;

    private static final String GENERAL_ADMIN = "GENERAL_ADMIN";

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       IRoleService roleService,
                       GetUsersListStrategyFactory listUsersStrategyFactory,
                       IUserValidations userValidations,
                       IUserAuthentications userAuthentications, IStoreValidations storeValidations) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.listUsersStrategyFactory = listUsersStrategyFactory;
        this.userValidations = userValidations;
        this.userAuthentications = userAuthentications;
        this.storeValidations = storeValidations;
    }


    @Override
    public Page<UserResponse> listActiveUsers(Authentication authentication,
                                              Long storeId,
                                              String searchTerm,
                                              Pageable pageable){
        String authRole = userAuthentications.authRole(authentication);
        IGetUserListStrategy strategy = listUsersStrategyFactory.userListStrategy(authRole);
        Long effectiveStoreId = listUsersStrategyFactory.resolveStoreId(authRole, storeId, authentication);

        return strategy.listUsers(effectiveStoreId, searchTerm, pageable);
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

        if (userRepository.findByDocument(userData.document()).isPresent()) {
            throw new ValidationException(
                    "User with this document already exists: " + userData.document()
            );
        }

        var hashedPassword = passwordEncoder.encode(userData.password());

        Role role = roleService.validRole(userData.roleId());

        // No se puede crear otro General Admin
        if (role.getName().equals(GENERAL_ADMIN)) {
            throw new ValidationException(
                    "Only one General Admin can exist in the system."
            );
        }

        Store store;

        switch (authRole) {

            case "ROLE_GENERAL_ADMIN":
                // El General Admin puede elegir la tienda
                store = storeValidations.validStore(userData.storeId());
                break;
            case "ROLE_STORE_ADMIN":
                // Un Store Admin solo puede crear cajeros
                if (!role.getName().equals("CASHIER")) {
                    throw new AccessDeniedException(
                            "Store Admin can only create Cashier users."
                    );
                }
                // La tienda siempre será la del Store Admin autenticado
                store = authUser.getStore();
                break;
            default:
                throw new IllegalArgumentException("Invalid user role.");
        }

        User user = new User(userData, role, store, hashedPassword);
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

        if(passwordEncoder.matches(updatePassword.newPassword(), user.getPassword())){
            throw new ValidationException("The new password must be different.");
        }

        var hashedPassword = passwordEncoder.encode(updatePassword.newPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return new UserResponse(user);
    }

    @Override
    public TemporaryPasswordResponse resetPassword(Long id, Authentication authentication) {
        User authUser = userAuthentications.authUser(authentication);
        User user = userValidations.validUser(id);
        String authRole = userAuthentications.authRole(authentication);

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
        if (authUser.getId().equals(user.getId())) {
            throw new ValidationException("You cannot reset your own password.");
        }
        System.out.println(user.getRole().getName());
        if (authRole.equals("ROLE_STORE_ADMIN")
                && !user.getRole().getName().equals("CASHIER")) {

            throw new AccessDeniedException("Store Admin can only reset Cashier passwords.");
        }
        String temporaryPassword = generateTemporaryPassword();
        String hashedPassword = passwordEncoder.encode(temporaryPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return new TemporaryPasswordResponse(user.getLoginUser(), temporaryPassword);
    }

    @Override
    public UserResponse updateRoleAndStore(Long id,
                                           UpdateRoleAndStoreData data,
                                           Authentication authentication){

        User authUser = userAuthentications.authUser(authentication);
        User user = userValidations.validUser(id);

        if ((authUser.getId().equals(id))){
            throw new AccessDeniedException("You cannot change your own role");
        }

        if(user.getRole().getName().equals(GENERAL_ADMIN)){
            throw new ValidationException(
                    "General Admin permissions cannot be modified."
            );
        }

        Role role = roleService.validRole(data.roleId());
        if (role.getName().equals(GENERAL_ADMIN)){
            throw new ValidationException("A new General Admin cannot be assigned.");
        }

        Store store = storeValidations.validStore(data.storeId());
        user.setRole(role);
        user.setStore(store);
        userRepository.save(user);
        return new UserResponse(user);
    }

    @Override
    public UserResponse updateRole(Long id, UpdateRole updateRole, Authentication authentication) {
        User authUser = userAuthentications.authUser(authentication);
        User user = userValidations.validUser(id);

        if (!(authUser.getStore().getId().equals(user.getStore().getId()))){
            throw new IllegalArgumentException("You only can modify users from your own store");
        }

        if(user.getRole().getName().equals("GENERAL_ADMIN")){
            throw new IllegalArgumentException("General Admin can't be modified.");
        }

        Role role = roleService.validRole(updateRole.roleId());
        if (!role.getName().equals("CASHIER")) {
            throw new IllegalArgumentException("Invalid role.");
        }

        user.setRole(role);
        userRepository.save(user);
        return new UserResponse(user);
    }

    @Override
    public void deactiveUser(Long id, Authentication authentication) {
        User authUser = userAuthentications.authUser(authentication);
        String authRole = userAuthentications.authRole(authentication);
        User user = userValidations.validUser(id);

        if(user.getRole().getName().equals(GENERAL_ADMIN)){
            throw new ValidationException(
                    "The General Admin cannot be deactivated."
            );
        }

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

    private String generateTemporaryPassword(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }

        return password.toString();
    }
}
