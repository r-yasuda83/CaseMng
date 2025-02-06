package com.example.casemng.entity;

import lombok.Data;

@Data
public class OrderProduct {

	private int id;
	
	private int ordersId;
	
	private int productId;
	
	private int quantity;
	
	private int discount;
	
	private boolean isDeleted;
	
	private Product product;
	
	private Order orders;
}
