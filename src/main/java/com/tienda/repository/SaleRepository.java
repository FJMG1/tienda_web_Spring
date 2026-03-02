package com.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tienda.model.Sale;
import com.tienda.model.User;
@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {

	//compras del usuario
    @Query("SELECT s FROM Sale s WHERE s.user = :user ORDER BY s.dateSale DESC")
    List<Sale> findByUser(@Param("user") User user);

    //buscar compra por ID
    @Query("SELECT s FROM Sale s WHERE s.id = :id")
    Sale findById(@Param("id") int id);

    //listar todas las compras (admin)
    @Query("SELECT s FROM Sale s ORDER BY s.dateSale DESC")
    List<Sale> findAllSales();

}
