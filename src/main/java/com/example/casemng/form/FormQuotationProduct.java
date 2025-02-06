package com.example.casemng.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.example.casemng.entity.Product;

import lombok.Data;

@Data
public class FormQuotationProduct {

	private int id;

	private int quotationId;

	private int productId;

	@NotNull
	@Min(0)
	private Integer quantity;

	private int discount;

	private boolean isDeleted;

	private Product product;
}
