package com.example.casemng.form;

import lombok.Data;

@Data
public class FormOrderProduct {
	
	private int id;

	private int ordersId;

	private int productId;
	
	private Integer quantity;
	
	private Integer discount;

	private boolean isDeleted;
	
	private FormProduct product;
}