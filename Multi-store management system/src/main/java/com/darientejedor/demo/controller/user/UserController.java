package com.darientejedor.demo.controller.user;

import com.darientejedor.demo.domain.users.User;
import com.darientejedor.demo.domain.users.dto.*;
import com.darientejedor.demo.domain.users.repository.UserRepository;
import com.darientejedor.demo.services.user.IUserService;
import com.darientejedor.demo.services.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
//@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> userList(@PageableDefault(size = 10)Pageable pageable){
        return ResponseEntity.ok(userService.listActiveUsers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> userResponser (@PathVariable  Long id){
        return ResponseEntity.ok(userService.userResponse(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserData userData){
        UserResponse user = userService.createUser(userData);
        URI uri = URI.create("/users/" + user.id());
        return ResponseEntity.created(uri).body(user);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UpdateUserInformation userInformation, @PathVariable Long id){
        UserResponse userResponse = userService.updateUserInfo(id, userInformation);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/{id}/password")
    @Transactional
    public ResponseEntity<UserResponse> updatePassword(@PathVariable Long id, @RequestBody @Valid PasswordUpdateData updatePassword){
        UserResponse userResponse = userService.changePassword(id, updatePassword);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/{id}/role-store")
    @Transactional
    @PreAuthorize("hasRole('ADMIN_GENERAL')")
    public ResponseEntity<UserResponse> updateRoleAndStore(@PathVariable Long id, @RequestBody @Valid UpdateRoleAndStoreData updateRoleAndStore){
        UserResponse userResponse = userService.updateRoleAndStore(id, updateRoleAndStore);
        return  ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deactiveUser(id);
        return ResponseEntity.noContent().build();
    }
}

