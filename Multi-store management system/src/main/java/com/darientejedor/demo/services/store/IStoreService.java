package com.darientejedor.demo.services.store;

import com.darientejedor.demo.domain.stores.dto.StoreData;
import com.darientejedor.demo.domain.stores.dto.StoreResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface IStoreService {
    Page<StoreResponse> listActiveStores(Pageable pageable);

    StoreResponse storeResponse(Long id, Authentication authentication);

    StoreResponse createStore(@Valid StoreData storeData);

    StoreResponse updateStore(Long id, @Valid StoreData storeData);

    void deactiveStore(Long id);

}
