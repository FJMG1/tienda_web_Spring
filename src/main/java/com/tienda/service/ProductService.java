package com.tienda.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.tienda.model.Product;
import com.tienda.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	//Listar productos
	
	public List<Product> getAllProducts(){
		return productRepository.findAll();
	}
	
	public Product findProductById(int id ) {
		return productRepository.findById(id);
	}
	public void save(Product product) {
	    productRepository.save(product);
	}
	
	public void delete(int id) {
	    productRepository.deleteById(id);
	}
	public Page<Product> getAllProductsPaginated(int page, int size) {
	    Pageable pageable = PageRequest.of(page, size);
	    return productRepository.findAll(pageable);
	}

}
