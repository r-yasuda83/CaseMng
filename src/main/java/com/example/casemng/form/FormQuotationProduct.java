package com.example.casemng.form;

import com.example.casemng.entity.Product;

import lombok.Data;

@Data
public class FormQuotationProduct {

	private int id;

	private int quotationId;

	private int productId;

	private Integer quantity;

	private Integer discount;

	private boolean isDeleted;

	private Product product;
}
