package com.darientejedor.demo.domain.roles.repository;

import com.darientejedor.demo.domain.roles.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    Page<Role> findByActiveTrue(Pageable pageable);
}
