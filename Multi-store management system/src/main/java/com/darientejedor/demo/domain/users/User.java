package com.darientejedor.demo.domain.users;

import com.darientejedor.demo.domain.roles.Role;
import com.darientejedor.demo.domain.roles.RoleData;
import com.darientejedor.demo.domain.stores.Store;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "users")
@Entity(name = "User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String loginUser;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private Long document;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public User(UserData userData, Role role, Store store) {
        this.loginUser = userData.loginUser();
        this.name = userData.name();
        this.password = userData.password();
        this.document = userData.document();
        this.role = role;
        this.store = store;
    }

}
