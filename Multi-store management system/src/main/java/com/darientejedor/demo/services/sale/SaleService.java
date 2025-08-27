package com.darientejedor.demo.services.sale;

import com.darientejedor.demo.domain.inventory.dto.InventoryResponse;
import com.darientejedor.demo.domain.sales.Sale;
import com.darientejedor.demo.domain.sales.dto.SaleData;
import com.darientejedor.demo.domain.sales.dto.SaleResponse;
import com.darientejedor.demo.domain.sales.repository.SaleRepository;
import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.stores.repository.StoreRepository;
import com.darientejedor.demo.domain.users.User;
import com.darientejedor.demo.domain.users.repository.UserRepository;
import com.darientejedor.demo.services.store.IStoreService;
import com.darientejedor.demo.services.user.IUserService;
import com.darientejedor.demo.services.user.authentications.IUserAuthentications;
import com.darientejedor.demo.services.user.validations.IUserValidations;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SaleService implements ISaleService{

    private final SaleRepository saleRepository;
    private final IUserAuthentications userAuthentications;
    private final IUserValidations userValidations;
    private final IStoreService storeService;

    public SaleService(SaleRepository saleRepository, IUserService userService, IUserAuthentications userAuthentications, IUserValidations userValidations, IStoreService storeService) {
        this.saleRepository = saleRepository;
        this.userAuthentications = userAuthentications;
        this.userValidations = userValidations;
        this.storeService = storeService;
    }

    @Override
    public Page<SaleResponse> listActiveSales(Authentication authentication, Pageable pageable) {
        User authUser = userAuthentications.authUser(authentication);
        String authRole = userAuthentications.authRole(authentication);
        if ("ROLE_GENERAL_ADMIN".equals(authRole)) {
            return saleRepository.findByActiveTrue(pageable).map(SaleResponse::new);
        } else {
            Long storeId = authUser.getStore().getId();
            return saleRepository.findByStoreId(storeId, pageable).map(SaleResponse::new);
        }
    }

    @Override
    public SaleResponse saleResponse(Long id, Authentication authentication) {
        User authUser = userAuthentications.authUser(authentication);
        String authRole = userAuthentications.authRole(authentication);
        switch (authRole) {
            case "ROLE_GENERAL_ADMIN":
                break;
            case "ROLE_STORE_ADMIN", "ROLE_CASHIER":
                if (!authUser.getStore().getId().equals(id)) {
                    throw new AccessDeniedException("You can only view sales from your own store.");
                }
                break;
            default:
                throw new ValidationException("Invalid user role: " + authRole);
        }


        Sale sale = validSale(id);
        return new SaleResponse(sale);
    }

    @Override
    public SaleResponse createSale(@Valid SaleData saleData) {
        Store store = storeService.validStore(saleData.storeId());
        User user = userValidations.validUser(saleData.userId());
        Sale newSale = new Sale(store, user);
        saleRepository.save(newSale);
        return new SaleResponse(newSale);
    }

    @Override
    public void deactiveSale(Long id) {
        Sale sale = validSale(id);
        sale.deactiveSale();
        saleRepository.save(sale);
    }

    @Override
    public Sale validSale(Long id){
        Sale sale = saleRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Sale not found with ID:" + id));
        if (!sale.isActive()){
            throw new ValidationException("Sale inactive");
        }
        return sale;
    }

}






























