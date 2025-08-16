package com.darientejedor.demo.services.saledetail;

import com.darientejedor.demo.domain.inventory.Inventory;
import com.darientejedor.demo.domain.inventory.repository.InventoryRepository;
import com.darientejedor.demo.domain.products.Product;
import com.darientejedor.demo.domain.products.repository.ProductRepository;
import com.darientejedor.demo.domain.sales.Sale;
import com.darientejedor.demo.domain.sales.repository.SaleRepository;
import com.darientejedor.demo.domain.salesdetails.SaleDetail;
import com.darientejedor.demo.domain.salesdetails.dto.SaleDetailData;
import com.darientejedor.demo.domain.salesdetails.dto.SaleDetailResponse;
import com.darientejedor.demo.domain.salesdetails.repository.SaleDetailsRepository;
import com.darientejedor.demo.domain.stores.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleDetailService implements ISaleDetailService{


    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private SaleDetailsRepository saleDetailRepository;

    @Override
    public SaleDetailResponse addSaleDetail(Long saleId,  SaleDetailData saleDetailData){
        //Validar el sale y product
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found with ID: " + saleId));
        if (!sale.isActive()){
            throw new IllegalArgumentException("Sale inactive with ID: " + saleId);
        }
        Product product = productRepository.findById(saleDetailData.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + saleDetailData.productId()));
                if (!product.isActive()){
                throw new IllegalArgumentException("Product not found or already inactive with ID: " + saleDetailData.productId());
                }
        //Buscar inventory
        Inventory inventory = inventoryRepository.findByProductAndStore(product, sale.getStore())
                .orElseThrow(()-> new IllegalArgumentException("Inventory not found for this product and store."));
        //Validar stock
        if (inventory.getStock() < saleDetailData.quantity()) {
            throw new IllegalArgumentException("Not enough stock for product with ID: " + product.getId());
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

