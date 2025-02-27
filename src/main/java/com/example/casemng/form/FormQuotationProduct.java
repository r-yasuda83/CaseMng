package com.example.casemng.form;

import lombok.Data;

@Data
public class FormQuotationProduct {

	private int id;

	private int quotationId;

	private Integer productId;

	private Integer quantity;

	private Integer discount;

	private boolean isDeleted;

	private FormProduct product;
}
