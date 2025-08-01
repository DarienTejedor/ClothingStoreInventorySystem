package com.darientejedor.demo.domain.respositories;

import com.darientejedor.demo.domain.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByDocument(Long document);

    Optional<User> findByLoginUser(String user);

    Optional<User> findById(Long id);

    Page<User> findByActivoTrue(Pageable pageable);
}
