package com.example.casemng.service;

import java.util.List;

import com.example.casemng.model.entity.QuotationProduct;

public interface QuotationProductService {
	
	public void edit(List<QuotationProduct> list, int quotationId);
	public QuotationProduct findById(int id);
	public void addQuotationProduct(List<QuotationProduct> list);
	public List<QuotationProduct> findAllExport();
	public QuotationProduct findByIdAll(int id);
	public List<QuotationProduct> setQuotationId(List<QuotationProduct> list, int quotationId);
}