package com.darientejedor.demo.services.sale;

import com.darientejedor.demo.domain.sales.Sale;
import com.darientejedor.demo.domain.sales.dto.SaleData;
import com.darientejedor.demo.domain.sales.dto.SaleResponse;
import com.darientejedor.demo.domain.sales.repository.SaleRepository;
import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.stores.repository.StoreRepository;
import com.darientejedor.demo.domain.users.User;
import com.darientejedor.demo.domain.users.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SaleService implements ISaleService{

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<SaleResponse> listActiveSales(Pageable pageable) {
        return saleRepository.findByActiveTrue(pageable).map(SaleResponse::new);
    }

    @Override
    public SaleResponse saleResponse(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Sale not found with ID: " + id));
        if (!sale.isActive()){
            throw new IllegalArgumentException("Sale inactive");
        }
        return new SaleResponse(sale);
    }

    @Override
    public SaleResponse createSale(@Valid SaleData saleData) {
        Store store = storeRepository.findById(saleData.storeId())
                .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + saleData.storeId()));
        if (!store.isActive()){
            throw new IllegalArgumentException("Store not found or inactive with ID: " + saleData.storeId());
        }
        User user = userRepository.findById(saleData.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + saleData.userId()));
        if (!user.isActive()){
            throw new IllegalArgumentException("User not found or inactive with ID: " + saleData.userId());
        }

        Sale newSale = new Sale(store, user);
        
        saleRepository.save(newSale);
        return new SaleResponse(newSale);
    }

    @Override
    public void deactiveSale(Long id) {
        Sale sale = saleRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Sale not found with ID:" + id));
        if (!sale.isActive()){
            throw new IllegalArgumentException("Sale inactive");
        }
        sale.deactiveSale();
        saleRepository.save(sale);
    }
}






























