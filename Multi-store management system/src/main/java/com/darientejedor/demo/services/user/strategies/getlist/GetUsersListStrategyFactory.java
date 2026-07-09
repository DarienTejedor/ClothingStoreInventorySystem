package com.darientejedor.demo.services.user.strategies.getlist;

import com.darientejedor.demo.exceptions.ValidationException;
import com.darientejedor.demo.services.user.authentications.UserAuthentications;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class GetUsersListStrategyFactory {

    private final Map<String, IGetUserListStrategy> strategies;
    private final UserAuthentications userAuthentications;

    public GetUsersListStrategyFactory(Map<String, IGetUserListStrategy> strategies, UserAuthentications userAuthentications) {
        this.strategies = strategies;
        this.userAuthentications = userAuthentications;
    }

    private IGetUserListStrategy getStrategy(String key) {
        return Optional.ofNullable(strategies.get(key))
                .orElseThrow(() -> new ValidationException("Strategy not found: " + key));
    }

    public IGetUserListStrategy userListStrategy(String role) {
        return switch (role) {
            case "ROLE_GENERAL_ADMIN" -> getStrategy("generalAdminUsersList");
            case "ROLE_STORE_ADMIN" -> getStrategy("storeAdminUsersList");
            default -> throw new ValidationException("Invalid role: " + role);
        };
    }

//   devuelve el storeId "efectivo" que debe usar la estrategia
    public Long resolveStoreId(String role, Long storeId, Authentication authentication) {
        return switch (role) {
            case "ROLE_GENERAL_ADMIN" -> storeId;
            case "ROLE_STORE_ADMIN" -> userAuthentications.authUser(authentication).getStore().getId();
            default -> throw new ValidationException("Invalid role: " + role);
        };
    }
}