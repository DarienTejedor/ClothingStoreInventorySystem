package com.darientejedor.demo.services.store.validations;

import com.darientejedor.demo.domain.stores.Store;

public interface IStoreValidations {
    Store validStore(Long storeId);
}
