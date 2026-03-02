package com.tienda.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tienda.model.Product;
import com.tienda.model.Sale;
import com.tienda.model.SaleDetail;
import com.tienda.repository.SaleDetailRepository;

@Service
public class SaleDetailService {

	private final SaleDetailRepository saleDetailRepository;
	
	public SaleDetailService(SaleDetailRepository saleDetailRepository) {
		this.saleDetailRepository = saleDetailRepository;
				
	}
	public void save(SaleDetail detail){
		saleDetailRepository.save(detail);
	}
	public List<SaleDetail> findBySale(Sale sale) {
	    return saleDetailRepository.findBySale(sale);
	}	
}
