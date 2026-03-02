package com.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tienda.model.Sale;
import com.tienda.model.SaleDetail;
import com.tienda.model.SaleDetailId;
@Repository
public interface SaleDetailRepository extends JpaRepository<SaleDetail, SaleDetailId >{

	@Query("SELECT d FROM SaleDetail d WHERE d.sale = :sale")
    List<SaleDetail> findBySale(@Param("sale") Sale sale);

}
