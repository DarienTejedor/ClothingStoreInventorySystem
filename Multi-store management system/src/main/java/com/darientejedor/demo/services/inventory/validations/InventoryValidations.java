package com.darientejedor.demo.services.inventory.validations;

import com.darientejedor.demo.domain.inventory.Inventory;
import com.darientejedor.demo.domain.inventory.repository.InventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class InventoryValidations implements IInventoryValidations{

    private final InventoryRepository inventoryRepository;

    public InventoryValidations(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Inventory validInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Inventory not found with ID: " + id));
        if (!inventory.isActive()){
            throw new ValidationException("Inventory not found or inactive with ID: " + id);
        }
        return inventory;
    }

}
