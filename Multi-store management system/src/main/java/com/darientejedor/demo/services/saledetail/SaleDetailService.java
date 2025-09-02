package com.darientejedor.demo.services.saledetail;

import com.darientejedor.demo.domain.exceptions.ValidationException;
import com.darientejedor.demo.domain.inventory.Inventory;
import com.darientejedor.demo.domain.inventory.repository.InventoryRepository;
import com.darientejedor.demo.domain.products.Product;
import com.darientejedor.demo.domain.sales.Sale;
import com.darientejedor.demo.domain.sales.repository.SaleRepository;
import com.darientejedor.demo.domain.salesdetails.SaleDetail;
import com.darientejedor.demo.domain.salesdetails.dto.SaleDetailData;
import com.darientejedor.demo.domain.salesdetails.dto.SaleDetailResponse;
import com.darientejedor.demo.domain.salesdetails.repository.SaleDetailsRepository;
import com.darientejedor.demo.services.product.validations.IProductValidations;
import com.darientejedor.demo.services.sale.validations.ISaleValidations;
import org.springframework.stereotype.Service;

@Service
public class SaleDetailService implements ISaleDetailService{


    private final SaleRepository saleRepository;
    private final InventoryRepository inventoryRepository;
    private final SaleDetailsRepository saleDetailRepository;
    private final ISaleValidations saleValidations;
    private final IProductValidations productValidations;

    public SaleDetailService(SaleRepository saleRepository,
                             InventoryRepository inventoryRepository,
                             SaleDetailsRepository saleDetailRepository,
                             ISaleValidations saleValidations,
                             IProductValidations productValidations) {
        this.saleRepository = saleRepository;
        this.inventoryRepository = inventoryRepository;
        this.saleDetailRepository = saleDetailRepository;
        this.saleValidations = saleValidations;
        this.productValidations = productValidations;
    }

    @Override
    public SaleDetailResponse addSaleDetail(Long saleId,  SaleDetailData saleDetailData){
        //Validar el sale y product
        Sale sale = saleValidations.validSale(saleId);
        Product product = productValidations.validProduct(saleDetailData.productId());
        //Buscar inventory
        Inventory inventory = inventoryRepository.findByProductAndStore(product, sale.getStore())
                .orElseThrow(()-> new ValidationException("Inventory not found for this product and store."));
        //Validar stock
        if (inventory.getStock() < saleDetailData.quantity()) {
            throw new ValidationException("Not enough stock for product with ID: " + product.getId());
        }
        //Crear el SaleDetail con el precio unitario del producto.
        SaleDetail newSaleDetail = new SaleDetail(sale, product, saleDetailData.quantity());

        //Actualizar el stock en el inventario.
        inventory.setStock(inventory.getStock() - saleDetailData.quantity());

        // Recalcular el total en la venta.
        sale.getSaleDetails().add(newSaleDetail);
        sale.calculateTotal();

        //Guardar todos los cambios.
        saleDetailRepository.save(newSaleDetail); // Guarda el detalle
        inventoryRepository.save(inventory); // Guarda el inventario actualizado
        saleRepository.save(sale); // Guarda la venta con el total actualizado

        //Devolver la respuesta.
        return new SaleDetailResponse(newSaleDetail);
    }
}

