package com.example.casemng.service;

import java.util.List;

import com.example.casemng.entity.QuotationProduct;
import com.example.casemng.form.FormQuotationProduct;

public interface QuotationProductService {
	
	public void edit(List<FormQuotationProduct> list);
	public QuotationProduct findById(int id);
	public void addQuotationProduct(List<FormQuotationProduct> list);
	public List<QuotationProduct> findAllExport();
	public QuotationProduct findByIdAll(int id);
	public List<String> comparisonStock(List<FormQuotationProduct> list);
	public List<FormQuotationProduct> organizeList(List<FormQuotationProduct> list);
	public List<FormQuotationProduct> organizeList(List<FormQuotationProduct> list, int quotationId);
	public List<FormQuotationProduct> setQuotationId(List<FormQuotationProduct> list, int quotationId);
}