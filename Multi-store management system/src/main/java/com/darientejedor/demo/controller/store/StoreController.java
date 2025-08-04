package com.darientejedor.demo.controller.store;


import com.darientejedor.demo.domain.stores.dto.StoreData;
import com.darientejedor.demo.domain.stores.dto.StoreResponse;
import com.darientejedor.demo.services.store.StoreService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stores    ")
public class StoreController {


    @Autowired
    private StoreService storeService;

    @GetMapping
    private ResponseEntity<Page<StoreResponse>> storeList(@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(storeService.listActiveStores(pageable).map(StoreResponse::new));
    }

    @GetMapping("/{id}")
    private ResponseEntity<StoreResponse> storeResponse(@PathVariable Long id){
        return ResponseEntity.ok(storeService.storeResponse(id));
    }

    @PostMapping
    public ResponseEntity<Void> createStore(@RequestBody @Valid StoreData storeData){
        storeService.createStore(storeData);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<StoreResponse> updateStore(@PathVariable Long id, @RequestBody @Valid StoreData storeData){
        StoreResponse storeResponse = storeService.updateStore(id, storeData);
        return ResponseEntity.ok(storeResponse);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteStore(@PathVariable Long id){
        storeService.deactiveStore(id);
        return ResponseEntity.noContent().build();
    }

}
