package com.example.casemng.entity;

import lombok.Data;

@Data
public class QuotationProduct {

	private int id;

	private int quotationId;

	private int productId;

	private int quantity;

	private int discount;

	private boolean isDeleted;

	private Product product;
	
	private Quotation quotation;
}
