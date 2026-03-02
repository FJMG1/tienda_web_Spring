package com.tienda.model;

import java.io.Serializable;
import java.util.Objects;

public class SaleDetailId implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int sale;
	
	private int product;

	@Override
	public int hashCode() {
		return Objects.hash(product, sale);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SaleDetailId other = (SaleDetailId) obj;
		return product == other.product && sale == other.sale;
	}
	
	
	
}
