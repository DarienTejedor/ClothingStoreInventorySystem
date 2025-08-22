package com.darientejedor.demo.services.user.authentications;

import com.darientejedor.demo.domain.users.User;
import org.springframework.security.core.Authentication;

public interface IUserAuthentications {
    User authUser(Authentication authentication);

    String authRole(Authentication authentication);

}
