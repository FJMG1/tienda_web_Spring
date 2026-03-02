package com.tienda.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {
	
	private Map<Integer, Integer> cart = new HashMap<>();
	
	public void addProduct(int productId, int quantity) {
	    cart.put(productId, quantity);
	}
	
	
	public void deleteProduct(int productId) {
		cart.remove(productId);
	}
	
	public void decreaseProduct (int productId) {
		if(!cart.containsKey(productId))return;
		
		if(cart.get(productId)<=1) {
			cart.remove(productId);
		}else {
			cart.put(productId, cart.get(productId)-1);
		}
	}
	
	public Map<Integer, Integer> getProducts(){
		return cart;
	}
	
	
	public void clearCart() {
		cart.clear();
	}
}
