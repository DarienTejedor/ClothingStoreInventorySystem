package com.darientejedor.demo.services.store;
import com.darientejedor.demo.domain.address.Address;
import com.darientejedor.demo.domain.exceptions.ValidationException;
import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.stores.dto.StoreData;
import com.darientejedor.demo.domain.stores.dto.StoreResponse;
import com.darientejedor.demo.domain.stores.repository.StoreRepository;
import com.darientejedor.demo.domain.users.User;
import com.darientejedor.demo.services.store.validations.IStoreValidations;
import com.darientejedor.demo.services.user.authentications.IUserAuthentications;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class StoreService implements IStoreService {

    private final StoreRepository storeRepository;
    private final IUserAuthentications userAuthentications;
    private final IStoreValidations storeValidations;

    public StoreService(StoreRepository storeRepository,
                        IUserAuthentications userAuthentications,
                        IStoreValidations storeValidations) {
        this.storeRepository = storeRepository;
        this.userAuthentications = userAuthentications;
        this.storeValidations = storeValidations;
    }

    //Funcion Get, lista de stores
    @Override
    public Page<StoreResponse> listActiveStores(Pageable pageable) {
        return storeRepository.findByActiveTrue(pageable).map(StoreResponse::new);
    }

    @Override
    public StoreResponse storeResponse(Long id, Authentication authentication) {
        User authUser = userAuthentications.authUser(authentication);
        String authRole = userAuthentications.authRole(authentication);
        switch (authRole) {
            case "ROLE_GENERAL_ADMIN":
                break;
            case "ROLE_STORE_ADMIN", "ROLE_CASHIER":
                if (!authUser.getStore().getId().equals(id)) {
                    throw new AccessDeniedException("You can only view your own store.");
                }
                break;
            default:
                throw new ValidationException("Invalid user role: " + authRole);
        }
        Store store = storeValidations.validStore(id);
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getAddress(),
                store.getPhoneNumber(),
                store.getEmail()
        );
    }

    @Override
    public StoreResponse createStore(@Valid StoreData storeData) {
        if (storeRepository.findByName(storeData.name()).isPresent()) {
            throw new ValidationException("Store with this name already exists: " + storeData.name());
        }
        var store = new Store(storeData);
        storeRepository.save(store);
        return new StoreResponse(store);
    }

    @Override
    public StoreResponse updateStore(Long id, @Valid StoreData storeData) {
        Store store = storeValidations.validStore(id);
        store.setName(storeData.name());
        store.setEmail(storeData.email());
        store.setPhoneNumber(storeData.phoneNumber());
        store.setAddress(new Address(storeData.address()));
        storeRepository.save(store);
        return new StoreResponse(store);
    }

    @Override
    public void deactiveStore(Long id) {
        Store store = storeValidations.validStore(id);
        store.deactiveStore();
        storeRepository.save(store);
    }

}