package com.darientejedor.demo.services.user.validations;

import com.darientejedor.demo.domain.exceptions.ValidationException;
import com.darientejedor.demo.domain.users.User;
import com.darientejedor.demo.domain.users.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserValidations implements IUserValidations{

    private final UserRepository userRepository;

    public UserValidations(UserRepository userRepository) {
        this.userRepository = userRepository;
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
