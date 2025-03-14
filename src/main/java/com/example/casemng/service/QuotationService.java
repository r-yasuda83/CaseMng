package com.example.casemng.service;

import java.util.List;

import com.example.casemng.entity.Quotation;
import com.example.casemng.entity.QuotationProduct;

public interface QuotationService {

	public Quotation findById(int id);
	public void quotationEdit(Quotation quotation);
	public int create(Quotation quotation);
	public void logicalDelete(int id);
	public List<QuotationProduct> generateProductList();
}