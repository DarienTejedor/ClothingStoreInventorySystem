package com.darientejedor.demo.domain.users.repository;

import com.darientejedor.demo.domain.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.active = true AND (" +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.loginUser) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(CAST(u.document AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(CAST(u.id AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<User> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.active = true AND u.store.id = :storeId AND (" +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.loginUser) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(CAST(u.document AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(CAST(u.id AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<User> searchUsersByStores(@Param("searchTerm") String searchTerm, @Param("storeId") Long storeId, Pageable pageable);

    Optional<User> findByDocument(Long document);

    Optional<User> findByLoginUser(String user);

    Optional<User> findById(Long id);

    Page<User> findByActiveTrue(Pageable pageable);

    Page<User> findByActiveTrueAndStoreId(Long id, Pageable pageable);

}
