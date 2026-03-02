package com.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tienda.model.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

	@Query("SELECT product FROM Product product WHERE product.id = :id")
	Product findById(@Param("id") int id);
}
