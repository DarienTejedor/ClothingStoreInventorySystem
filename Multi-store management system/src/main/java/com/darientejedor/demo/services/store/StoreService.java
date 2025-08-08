package com.darientejedor.demo.services.store;
import com.darientejedor.demo.domain.address.Address;
import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.stores.dto.StoreData;
import com.darientejedor.demo.domain.stores.dto.StoreResponse;
import com.darientejedor.demo.domain.stores.repository.StoreRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    //Funcion Get, lista de stores
    public Page<Store> listActiveStores(Pageable pageable) {
        return storeRepository.findByActiveTrue(pageable);
    }


    public StoreResponse storeResponse(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Store not found with ID: " + id));
        if (!store.isActive()){
            throw new IllegalArgumentException("Store not found or inactive with ID: " + id);
        }
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getAddress(),
                store.getPhoneNumber(),
                store.getEmail()
        );
    }


    public void createStore(@Valid StoreData storeData) {
        if (storeRepository.findByName(storeData.name()).isPresent()){
            throw new IllegalArgumentException("Store with this name already exists: " + storeData.name());
        }
        var store = new Store(storeData);
        storeRepository.save(store);
    }

    public StoreResponse updateStore(Long id, @Valid StoreData storeData) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + id));
        if (!store.isActive()){
            throw new IllegalArgumentException("Store not found or inactive with ID: " + id);
        }
        store.setName(storeData.name());
        store.setEmail(storeData.email());
        store.setPhoneNumber(storeData.phoneNumber());
        store.setAddress(new Address(storeData.address()));
        storeRepository.save(store);
        return new StoreResponse(store);
    }


    public void deactiveStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + id));
        if (!store.isActive()){
            throw new IllegalArgumentException("Store not found or already inactive with ID: " + id);
        }
        store.deactiveStore();
        storeRepository.save(store);

    }
}
