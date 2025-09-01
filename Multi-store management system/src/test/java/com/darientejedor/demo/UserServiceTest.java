package com.darientejedor.demo;

import com.darientejedor.demo.domain.roles.Role;
import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.users.User;
import com.darientejedor.demo.domain.users.dto.UserResponse;
import com.darientejedor.demo.domain.users.repository.UserRepository;
import com.darientejedor.demo.services.store.validations.StoreValidations;
import com.darientejedor.demo.services.user.UserService;
import com.darientejedor.demo.services.user.authentications.UserAuthentications;
import com.darientejedor.demo.services.user.strategies.getlist.GetUsersListStrategyFactory;
import com.darientejedor.demo.services.user.strategies.getlist.IGetUserListStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserAuthentications userAuthentications;
    @Mock
    private GetUsersListStrategyFactory listUsersStrategyFactory;
    @Mock
    private StoreValidations storeValidations;
    @Mock
    private Authentication authentication;
    @Mock
    private Pageable pageable;
    @Mock
    private IGetUserListStrategy userListStrategy;


    @InjectMocks
    private UserService userService;


    @Test
    @DisplayName("List active users, it should return a users page when is general admin role")
    void listActiveUsers(){
        //Crear datos prueba
        Role role = new Role();
        role.setId(1L);

        Store store = new Store();
        store.setId(2L);

        User user1 = new User();
        user1.setId(1L);
        user1.setLoginUser("user1");
        user1.setRole(role);
        user1.setStore(store);

        User user2 = new User();
        user2.setId(2L);
        user2.setLoginUser("user2");
        user2.setRole(role);
        user2.setStore(store);

        List<User> userList = Arrays.asList(user1, user2);

        // Convertir a UserResponse para el mock
        List<UserResponse> userResponseList = userList.stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(0, 10);
        Page<UserResponse> userResponsePage = new PageImpl<>(userResponseList, pageable, userResponseList.size());

        // Mockear el comportamiento
        when(userAuthentications.authRole(authentication)).thenReturn("ROLE_GENERAL_ADMIN");

        when(listUsersStrategyFactory.userListStrategy("ROLE_GENERAL_ADMIN", null, authentication)).thenReturn(userListStrategy);

        when(userListStrategy.listUsers(any(), eq(pageable))).thenReturn(userResponsePage);

        //  Llamada al metodo a probar
        Page<UserResponse> responses = userService.listActiveUsers(authentication, null, pageable);

        // Afirmación del resultado
        assertEquals(2L,responses.getTotalElements(), "El número de elementos totales no es el esperado.");
    }


}
