package com.darientejedor.demo.domain.salesdetails.repository;

import com.darientejedor.demo.domain.salesdetails.SaleDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleDetailsRepository extends JpaRepository<SaleDetail, Long> {
    List<SaleDetail> findBySaleId(Long saleId);
}
