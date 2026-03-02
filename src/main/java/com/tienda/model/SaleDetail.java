package com.tienda.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "detalle_venta")
@IdClass(value=SaleDetailId.class)
public class SaleDetail {
	
	@Id
	@ManyToOne
	@JoinColumn(name="venta_id")
	private Sale sale;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "producto_id")
	private Product product;
	
	@Column(name = "cantidad")
	private int quantity;
	
	@Column(name = "precio_unitario")	
	private float unitPrice;
	
	public Sale getSale() {
		return sale;
	}
	public void setSale(Sale sale) {
		this.sale = sale;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public float getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}	
}
