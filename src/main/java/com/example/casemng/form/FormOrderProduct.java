package com.example.casemng.form;

import java.io.Serializable;

import lombok.Data;

@Data
public class FormOrderProduct implements Serializable{

	public FormOrderProduct() {
	}
	
	public FormOrderProduct(FormOrderProduct form) {
		this.id = form.getId();
		this.ordersId = form.getOrdersId();
	    this.productId = form.getProductId();
	    this.quantity = form.getQuantity();
	    this.discount = form.getDiscount();
	}

	private int id;

	private int ordersId;
	
	private Integer productId;
	
	private Integer quantity;
	
	private Integer discount;

	private boolean isDeleted;
	
	private FormProduct product;
}