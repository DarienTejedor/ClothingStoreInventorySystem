package com.darientejedor.demo.config;


import com.darientejedor.demo.domain.address.Address;
import com.darientejedor.demo.domain.roles.repository.RoleRepository;
import com.darientejedor.demo.domain.stores.repository.StoreRepository;
import com.darientejedor.demo.domain.users.repository.UserRepository;
import com.darientejedor.demo.domain.roles.Role;
import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.users.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitializerRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;

    public InitializerRunner(UserRepository userRepository, RoleRepository roleRepository, StoreRepository storeRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.storeRepository = storeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
    //Verificar si no existen users
        if (userRepository.count() == 0){
            //Crear el rol, una vez creado no creara mas
            Role adminRole = roleRepository.findByName("ADMIN_GENERAL").orElseGet(()->{
                Role newRole = new Role();
                newRole.setName("ADMIN_GENERAL");
                return roleRepository.save(newRole);
            });

            //Asegura que una tienda por defecto existe.
            // 2. Asegura que una tienda por defecto existe.
            Store defaultStore = storeRepository.findById(1L)
                    .orElseGet(() -> {
                        Store newStore = new Store();
                        newStore.setName("Principal Store");
                        newStore.setPhoneNumber("3110001510");
                        newStore.setEmail("principal_store@clothes_store.com");

                        // Crear y asignar una dirección por defecto
                        Address defaultAddress = new Address("Bogota","Chapinero","Calle 100");
                        newStore.setAddress(defaultAddress);
                        return storeRepository.save(newStore);
                    });

            //Crea el usuario con el rol
            User firstAdmin = new User();
            firstAdmin.setLoginUser("ADMIN");
            firstAdmin.setActive(true);
            firstAdmin.setPassword(passwordEncoder.encode("admin123"));
            firstAdmin.setDocument(1234567890L);
            firstAdmin.setName("Darien");
            firstAdmin.setStore(defaultStore);
            firstAdmin.setRole(adminRole);

            userRepository.save(firstAdmin);
            System.out.println("First user created, user: 'ADMIN_GENERAL', password: 'admin123'");

            //

        }
    }
}
