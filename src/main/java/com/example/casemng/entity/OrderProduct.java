package com.example.casemng.entity;

import lombok.Data;

@Data
public class OrderProduct {
	
	public OrderProduct() {
	}
	
	public OrderProduct(OrderProduct order) {
		this.id = order.getId();
		this.ordersId = order.getOrdersId();
	    this.productId = order.getProductId();
	    this.quantity = order.getQuantity();
	    this.discount = order.getDiscount();
	}

	private int id;
	
	private int ordersId;
	
	private int productId;
	
	private int quantity;
	
	private int discount;
	
	private boolean isDeleted;
	
	private Product product;
	
	private Order orders;
}
