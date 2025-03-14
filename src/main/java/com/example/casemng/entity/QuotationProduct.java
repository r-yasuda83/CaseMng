package com.example.casemng.entity;

import lombok.Data;

@Data
public class QuotationProduct {
	
	public QuotationProduct() {
	}
	
	public QuotationProduct(QuotationProduct quo) {
		this.id = quo.getId();
		this.quotationId = quo.getQuotationId();
	    this.productId = quo.getProductId();
	    this.quantity = quo.getQuantity();
	    this.discount = quo.getDiscount();
	}

	private int id;

	private int quotationId;

	private int productId;

	private int quantity;

	private int discount;

	private boolean isDeleted;

	private Product product;
	
	private Quotation quotation;
}
