package com.darientejedor.demo.domain.roles;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "roles")
@Entity(name = "Role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;

    public Role(RoleData rolesData) {
        this.name = rolesData.name();
    }
}