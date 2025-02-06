package com.example.casemng.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class FormOrderProduct {
	
	private int id;

	private int ordersId;

	private int productId;
	
	@NotNull
	@Min(0)
	private Integer quantity;
	
	private int discount;

	private boolean isDeleted;
	
	private FormProduct product;
}