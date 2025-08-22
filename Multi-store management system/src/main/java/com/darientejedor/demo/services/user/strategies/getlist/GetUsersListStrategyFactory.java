package com.darientejedor.demo.services.user.strategies.getlist;

import com.darientejedor.demo.domain.exceptions.ValidationException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class GetUsersListStrategyFactory {

    private final Map<String, IGetUserListStrategy> strategies;

    public GetUsersListStrategyFactory(Map<String, IGetUserListStrategy> strategies) {
        this.strategies = strategies;
    }

    private IGetUserListStrategy getStrategy(String key) {
        return Optional.ofNullable(strategies.get(key))
                .orElseThrow(() -> new ValidationException("Strategy not found: " + key));
    }

    public IGetUserListStrategy userListStrategy(String role, Long storeId) {
        return switch (role) {
            case "ROLE_GENERAL_ADMIN" -> (storeId != null) ?
                    getStrategy("generalAdminUsersListByStore") :
                    strategies.get("generalAdminUsersList");
            case "ROLE_STORE_ADMIN" -> strategies.get("storeAdminUsersList");
            default -> throw new ValidationException("Invalid role: " + role);
        };
    }
}