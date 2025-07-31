package com.darientejedor.demo.domain.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByDocument(Long document);

    Optional<User> findByLoginUser(String user);
}
