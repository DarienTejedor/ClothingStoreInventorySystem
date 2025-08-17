package com.darientejedor.demo.services.store;
import com.darientejedor.demo.domain.address.Address;
import com.darientejedor.demo.domain.exceptions.ValidationException;
import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.stores.dto.StoreData;
import com.darientejedor.demo.domain.stores.dto.StoreResponse;
import com.darientejedor.demo.domain.stores.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StoreService implements IStoreService {

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    //Funcion Get, lista de stores
    @Override
    public Page<StoreResponse> listActiveStores(Pageable pageable) {
        return storeRepository.findByActiveTrue(pageable).map(StoreResponse::new);
    }

    @Override
    public StoreResponse storeResponse(Long id) {
        Store store = validStore(id);
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
        Store store = validStore(id);
        store.setName(storeData.name());
        store.setEmail(storeData.email());
        store.setPhoneNumber(storeData.phoneNumber());
        store.setAddress(new Address(storeData.address()));
        storeRepository.save(store);
        return new StoreResponse(store);
    }

    @Override
    public void deactiveStore(Long id) {
        Store store = validStore(id);
        store.deactiveStore();
        storeRepository.save(store);
    }

    @Override
    public Store validStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with ID: " + storeId));
        if (!store.isActive()) {
            throw new ValidationException("Store not found or already inactive with ID: " + storeId);
        }
        return store;
    }

}