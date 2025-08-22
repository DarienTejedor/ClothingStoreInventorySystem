package com.darientejedor.demo.services.user.authentications;

import com.darientejedor.demo.domain.exceptions.ValidationException;
import com.darientejedor.demo.domain.users.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class UserAuthentications implements IUserAuthentications{

    @Override
    public User authUser(Authentication authentication){
        return (User) authentication.getPrincipal();
    }

    @Override
    public String authRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new ValidationException("User role not  found."));
    }

}
