package com.tienda.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ventas")
public class Sale {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
			
	@Column(name = "fecha_venta")
	private LocalDateTime dateSale;
	
	@Column(name = "total")
	private double total;
	
	public enum Status {PENDIENTE, PAGADA, CANCELADA}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "estado")
	private Status status;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private User user;
	
	@OneToMany(mappedBy = "sale")
	private List<SaleDetail> details;

	
	
	public List<SaleDetail> getDetails() {
		return details;
	}

	public void setDetails(List<SaleDetail> details) {
		this.details = details;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getDateSale() {
		return dateSale;
	}

	public void setDateSale(LocalDateTime dateSale) {
		this.dateSale = dateSale;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}	
}
