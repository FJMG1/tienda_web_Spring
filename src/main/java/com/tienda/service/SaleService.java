package com.tienda.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tienda.model.Product;
import com.tienda.model.Sale;
import com.tienda.model.Sale.Status;
import com.tienda.model.SaleDetail;
import com.tienda.model.User;
import com.tienda.repository.SaleRepository;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleDetailService saleDetailService;
    private final ProductService productService;
    private final UserService userService;

    public SaleService(
            SaleRepository saleRepository,
            SaleDetailService saleDetailService,
            ProductService productService,
            UserService userService) {

        this.saleRepository = saleRepository;
        this.saleDetailService = saleDetailService;
        this.productService = productService;
        this.userService = userService;
    }

    public void createSale(Map<Integer, Integer> carrito) {

        // 1. Obtener usuario logueado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Spring Security devuelve getUsername()
        User user = userService.findByName(username); // <-- AJUSTA si tu método se llama distinto

        // 2. Crear compra vacía
        Sale sale = new Sale();
        sale.setUser(user);
        sale.setDateSale(LocalDateTime.now());
        sale.setStatus(Sale.Status.PENDIENTE);
        sale.setTotal(0);

        saleRepository.save(sale);

        double total = 0;

        // 3. Recorrer carrito
        for (Map.Entry<Integer, Integer> entry : carrito.entrySet()) {

            int productId = entry.getKey();
            int quantity = entry.getValue();

            Product product = productService.findProductById(productId);

            // 4. Crear detalle de compra
            SaleDetail detail = new SaleDetail();
            detail.setSale(sale);
            detail.setProduct(product);
            detail.setQuantity(quantity);
            detail.setUnitPrice(product.getPrice());

            saleDetailService.save(detail);

            // 5. Restar stock
            product.setStock(product.getStock() - quantity);
            productService.save(product);


            // 6. Sumar al total
            total += product.getPrice() * quantity;
        }

        // 7. Actualizar total de la compra
        sale.setTotal(total);
        saleRepository.save(sale);
    }
    public List<Sale> findByUser(User user) {
        return saleRepository.findByUser(user);
    }
    public Sale findById(int id) {
        return saleRepository.findById(id);
    }
   

    public List<Sale> findAllSales() {
        return saleRepository.findAllSales();
    }
    
    public void updateStatus(int id, Status newStatus) {
        Sale sale = saleRepository.findById(id);
        sale.setStatus(newStatus);
        saleRepository.save(sale);
    }
    
    public Page<Sale> getAllSalesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return saleRepository.findAll(pageable);
    }
    
   
}