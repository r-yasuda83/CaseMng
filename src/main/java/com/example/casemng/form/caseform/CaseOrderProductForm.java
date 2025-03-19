package com.example.casemng.form.caseform;

import lombok.Data;

@Data
public class CaseOrderProductForm {

	public CaseOrderProductForm() {
	}
	
	public CaseOrderProductForm(CaseOrderProductForm form) {
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
}
