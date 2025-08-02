package com.darientejedor.demo.services.user;


import com.darientejedor.demo.domain.roles.repository.RoleRepository;
import com.darientejedor.demo.domain.stores.repository.StoreRepository;
import com.darientejedor.demo.domain.users.dto.*;
import com.darientejedor.demo.domain.users.repository.UserRepository;
import com.darientejedor.demo.domain.roles.Role;
import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //Funcion POST
    public void createUser(UserData userData){
        if (userRepository.findByDocument(userData.document()).isPresent()){
            throw new IllegalArgumentException("User with this document already exists: " + userData.document());
        }
        var hashedPassword = passwordEncoder.encode(userData.password());
        Long roleId = userData.roleId();
        Long storeId = userData.storeId();

        Role role = roleRepository.findById(userData.roleId()).orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + userData.roleId()));

        Store store = storeRepository.findById(userData.storeId()).orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + userData.storeId()));

        var user = new User(userData, role, store, hashedPassword);
        userRepository.save(user);
    }




    public Page<User> listActiveUsers(Pageable pageable){

        return userRepository.findByActivoTrue(pageable);
    }

    public UserResponse userResponse(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        if (!user.isActivo()) {
            throw new IllegalArgumentException("User not found or inactive with ID: " + id);
        }
        return new UserResponse(user.getId(),
                user.getLoginUser(),
                user.getName(),
                user.getDocument(),
                user.getRole().getId(),
                user.getStore().getId());
    }

    public UserResponse updateUserInfo(Long id, UpdateUserInformation userInformation) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        user.setName(userInformation.name());
        user.setDocument(userInformation.document());
        userRepository.save(user);
        return new UserResponse(user);
    }

    public UserResponse updateRoleAndStore(Long id, UpdateRoleAndStoreData updateRoleAndStoreData){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        var roleId = updateRoleAndStoreData.roleId();
        var role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: "));

        var storeId = updateRoleAndStoreData.storeId();
        var store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: "));

        user.setRole(role);
        user.setStore(store);
        userRepository.save(user);

        return new UserResponse(user);
    }


    public UserResponse changePassword(Long id, PasswordUpdateData updatePassword){
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        var password = user.getPassword();
        var passwordCorrect = passwordEncoder.matches(updatePassword.oldPassword(), password);
        if (!passwordCorrect){
            throw new IllegalArgumentException("passwords do not match");
        }
        var hashedPassword = passwordEncoder.encode(updatePassword.newPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return new UserResponse(user);
    }

    public void deactiveUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        user.deactiveUser();
        userRepository.save(user);
    }



}
