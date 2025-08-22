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
import com.darientejedor.demo.services.user.strategies.getlist.GeneralAdminUsersList;
import com.darientejedor.demo.services.user.strategies.getlist.GetUsersListStrategyFactory;
import com.darientejedor.demo.services.user.strategies.getlist.IGetUserListStrategy;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       StoreRepository storeRepository,
                       PasswordEncoder passwordEncoder,
                       IStoreService storeService,
                       IRoleService roleService,
                       GetUsersListStrategyFactory listUsersStrategyFactory) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.storeRepository = storeRepository;
        this.passwordEncoder = passwordEncoder;
        this.storeService = storeService;
        this.roleService = roleService;
        this.listUsersStrategyFactory = listUsersStrategyFactory;
    }


    @Override
    public Page<UserResponse> listActiveUsers(String role, Long storeId,Pageable pageable){
        IGetUserListStrategy strategy = listUsersStrategyFactory.userListStrategy(role, storeId);
        if (!(strategy instanceof GeneralAdminUsersList)){
                storeService.validStore(storeId);
        }
        return strategy.listUsers(storeId, pageable);
//        return userRepository.findByActiveTrue(pageable).map(UserResponse::new);
    }










    //Funcion POST
    @Override
    public UserResponse createUser(UserData userData){
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
    public UserResponse userResponse(Long id){
        User user = validUser(id);
        return new UserResponse(user.getId(),
                user.getLoginUser(),
                user.getName(),
                user.getDocument(),
                user.getRole().getId(),
                user.getStore().getId());
    }

    @Override
    public UserResponse updateUserInfo(Long id, UpdateUserInformation userInformation) {
        User user = validUser(id);
        user.setName(userInformation.name());
        user.setDocument(userInformation.document());
        userRepository.save(user);
        return new UserResponse(user);
    }

    @Override
    public UserResponse updateRoleAndStore(Long id, UpdateRoleAndStoreData updateRoleAndStoreData){
        User user = validUser(id);
        Role role = roleService.validRole(updateRoleAndStoreData.roleId());
        Store store = storeService.validStore(updateRoleAndStoreData.storeId());
        user.setRole(role);
        user.setStore(store);
        userRepository.save(user);
        return new UserResponse(user);
    }

    @Override
    public UserResponse changePassword(Long id, PasswordUpdateData updatePassword){
        var user = validUser(id);
        var password = user.getPassword();
        var passwordCorrect = passwordEncoder.matches(updatePassword.oldPassword(), password);
        if (!passwordCorrect){
            throw new ValidationException("passwords do not match");
        }
        var hashedPassword = passwordEncoder.encode(updatePassword.newPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return new UserResponse(user);
    }

    @Override
    public void deactiveUser(Long id) {
        User user = validUser(id);
        user.deactiveUser();
        userRepository.save(user);
    }

    @Override
    public User validUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        if (!user.isActive()) {
            throw new ValidationException("User not found or already inactive with ID: " + userId);
        }
        return user;
    }

}
