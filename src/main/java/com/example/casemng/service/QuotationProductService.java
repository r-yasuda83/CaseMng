package com.example.casemng.service;

import java.util.List;

import com.example.casemng.entity.QuotationProduct;
import com.example.casemng.form.FormQuotationProduct;

public interface QuotationProductService {
	
	public void quotationProductEdit(List<FormQuotationProduct> list);
	public QuotationProduct findById(int id);
	public void addQuotationProduct(List<FormQuotationProduct> list);
	public List<QuotationProduct> findAllExport();
	public QuotationProduct findByIdAll(int id);
	public List<String> comparisonStock(List<FormQuotationProduct> list);
}