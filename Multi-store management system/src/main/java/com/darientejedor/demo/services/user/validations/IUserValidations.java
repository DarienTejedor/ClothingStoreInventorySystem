package com.darientejedor.demo.services.user.validations;

import com.darientejedor.demo.domain.users.User;

public interface IUserValidations {
    User validUser(Long userId);
}
