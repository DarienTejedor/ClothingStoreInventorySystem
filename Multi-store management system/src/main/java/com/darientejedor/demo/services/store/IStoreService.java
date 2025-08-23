package com.darientejedor.demo.services.store;

import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.stores.dto.StoreData;
import com.darientejedor.demo.domain.stores.dto.StoreResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface IStoreService {
    public Page<StoreResponse> listActiveStores(Pageable pageable);

    public StoreResponse storeResponse(Long id, Authentication authentication);

    public StoreResponse createStore(@Valid StoreData storeData);

    public StoreResponse updateStore(Long id, @Valid StoreData storeData);

    public void deactiveStore(Long id);

    Store validStore(Long storeId);

}
