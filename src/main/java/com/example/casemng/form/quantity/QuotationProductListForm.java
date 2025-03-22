package com.example.casemng.form.quantity;

import java.util.List;

import com.example.casemng.form.RegisterProductForm;

import lombok.Data;

@Data
public class QuotationProductListForm {

	private List<RegisterProductForm> quotationProductList;
}
