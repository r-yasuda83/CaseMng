package com.example.casemng.form;

import lombok.Data;

@Data
public class FormQuotationProduct {
	
	public FormQuotationProduct() {
	}
	
	public FormQuotationProduct(FormQuotationProduct form) {
		this.id = form.getId();
		this.quotationId = form.getQuotationId();
	    this.productId = form.getProductId();
	    this.quantity = form.getQuantity();
	    this.discount = form.getDiscount();
	}

	private int id;

	private int quotationId;

	private Integer productId;

	private Integer quantity;

	private Integer discount;

	private boolean isDeleted;

	private FormProduct product;
}
