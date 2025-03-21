package com.example.casemng.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.model.entity.Quotation;
import com.example.casemng.model.entity.QuotationProduct;
import com.example.casemng.repository.QuotationMapper;
import com.example.casemng.service.QuotationService;

@Service
public class QuotationServiceImpl implements QuotationService{
	
	@Autowired
	QuotationMapper quotationMapper;

	public Quotation findById(int id) {
		Quotation quotation = quotationMapper.findById(id);
		return quotation;
	}

	@Transactional
	public void quotationEdit(Quotation quotation) {
		quotationMapper.quotationEdit(quotation);
	}
	
	@Transactional
	public int create(Quotation quotation) {
		quotationMapper.create(quotation);		
		return quotation.getId();
	}
	
	@Transactional
	public void logicalDelete(int id) {
		quotationMapper.logicalDelete(id);
	}
	
	public List<QuotationProduct> generateProductList(){
		int ProductCount = 1;
		List<QuotationProduct> quotationProductList = new ArrayList<>();
		for (int i = 0; i < ProductCount; i++) {
			quotationProductList.add(new QuotationProduct());
		}
		return quotationProductList;
	}
}