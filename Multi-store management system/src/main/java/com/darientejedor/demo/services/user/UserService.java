package com.darientejedor.demo.services.user;


import com.darientejedor.demo.domain.exceptions.ValidationException;
import com.darientejedor.demo.domain.roles.repository.RoleRepository;
import com.darientejedor.demo.domain.stores.repository.StoreRepository;
import com.darientejedor.demo.domain.users.dto.*;
import com.darientejedor.demo.domain.users.repository.UserRepository;
import com.darientejedor.demo.domain.roles.Role;
import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.users.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //Funcion POST
    @Override
    public UserResponse createUser(UserData userData){
        if (userRepository.findByDocument(userData.document()).isPresent()){
            throw new ValidationException("User with this document already exists: " + userData.document());
        }
        var hashedPassword = passwordEncoder.encode(userData.password());
        Long roleId = userData.roleId();
        Long storeId = userData.storeId();

        Role role = roleRepository.findById(userData.roleId()).orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + userData.roleId()));

        Store store = storeRepository.findById(userData.storeId()).orElseThrow(() -> new ValidationException("Store not found with ID: " + userData.storeId()));

        var user = new User(userData, role, store, hashedPassword);
        userRepository.save(user);
        return new UserResponse(user);
    }

    @Override
    public Page<UserResponse> listActiveUsers(Pageable pageable){
        return userRepository.findByActiveTrue(pageable).map(UserResponse::new);
    }

    @Override
    public UserResponse userResponse(Long id){
        User user = validUser(id).user;
        return new UserResponse(user.getId(),
                user.getLoginUser(),
                user.getName(),
                user.getDocument(),
                user.getRole().getId(),
                user.getStore().getId());
    }

    @Override
    public UserResponse updateUserInfo(Long id, UpdateUserInformation userInformation) {
        User user = validUser(id).user;
        user.setName(userInformation.name());
        user.setDocument(userInformation.document());
        userRepository.save(user);
        return new UserResponse(user);
    }

    @Override
    public UserResponse updateRoleAndStore(Long id, UpdateRoleAndStoreData updateRoleAndStoreData){
        User user = validUser(id).user;

        var roleId = updateRoleAndStoreData.roleId();
        var role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: "));

        var storeId = updateRoleAndStoreData.storeId();
        var store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with ID: "));

        user.setRole(role);
        user.setStore(store);
        userRepository.save(user);

        return new UserResponse(user);
    }

    @Override
    public UserResponse changePassword(Long id, PasswordUpdateData updatePassword){
        var user = validUser(id).user;
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
        User user = validUser(id).user;
        user.deactiveUser();
        userRepository.save(user);
    }

    private ValidUser validUser(Long userId){
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        if (!user.isActive()) {
            throw new ValidationException("User not found or already inactive with ID: " + userId);
        }
        return new ValidUser(user);
    }

    private record ValidUser(User user){}

}
