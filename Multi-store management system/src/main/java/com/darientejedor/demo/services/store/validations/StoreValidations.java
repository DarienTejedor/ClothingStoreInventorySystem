package com.darientejedor.demo.services.store.validations;

import com.darientejedor.demo.domain.exceptions.ValidationException;
import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.stores.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;

public class StoreValidations implements IStoreValidations{

    private final StoreRepository storeRepository;

    public StoreValidations(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
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
