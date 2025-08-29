package com.darientejedor.demo.services.sale.validations;

import com.darientejedor.demo.domain.sales.Sale;
import com.darientejedor.demo.domain.sales.repository.SaleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class SaleValidations implements ISaleValidations{
    private final SaleRepository saleRepository;

    public SaleValidations(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
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
