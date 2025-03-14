package com.example.casemng.service;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.example.casemng.entity.QuotationProduct;

public interface QuotationProductService {
	
	public void edit(List<QuotationProduct> list, int quotationId);
	public QuotationProduct findById(int id);
	public void addQuotationProduct(List<QuotationProduct> list);
	public List<QuotationProduct> findAllExport();
	public QuotationProduct findByIdAll(int id);
	public BindingResult comparisonStock(List<QuotationProduct> list, BindingResult result);
	public List<QuotationProduct> setQuotationId(List<QuotationProduct> list, int quotationId);
	public BindingResult checkDiscount(List<QuotationProduct> list, BindingResult result);
	public BindingResult checkProduct(List<QuotationProduct> list, BindingResult result);
}