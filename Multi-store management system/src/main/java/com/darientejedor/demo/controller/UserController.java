package com.darientejedor.demo.controller;

import com.darientejedor.demo.domain.dtos.*;
import com.darientejedor.demo.domain.respositories.UserRepository;
import com.darientejedor.demo.domain.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
//@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> userList(@PageableDefault(size = 10)Pageable pageable){
        return ResponseEntity.ok(userService.listActiveUsers(pageable).map(UserResponse::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> userResponser (@PathVariable  Long id){
        return ResponseEntity.ok(userService.userResponse(id));
    }


    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserData userData){
        userService.createUser(userData);
        return ResponseEntity.ok().build();
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

