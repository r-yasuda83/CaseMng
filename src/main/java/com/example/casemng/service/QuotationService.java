package com.example.casemng.service;

import java.util.List;

import com.example.casemng.form.FormQuotation;
import com.example.casemng.form.FormQuotationProduct;

public interface QuotationService {

	public FormQuotation findById(int id);
	public void quotationEdit(FormQuotation form);
	public int create(FormQuotation form);
	public void logicalDelete(int id);
	public List<FormQuotationProduct> generateProductList();
}