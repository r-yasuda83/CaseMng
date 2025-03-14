package com.example.casemng.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.entity.Quotation;
import com.example.casemng.entity.QuotationProduct;
import com.example.casemng.repository.QuotationMapper;
import com.example.casemng.repository.QuotationProductMapper;
import com.example.casemng.service.QuotationService;

@Service
public class QuotationServiceImpl implements QuotationService{
	
	@Autowired
	QuotationMapper quotationMapper;
	
	@Autowired
	ModelMapper modelMapper;

	public Quotation findById(int id) {
		Quotation quotation = quotationMapper.findById(id);
		return quotation;
	}

	@Transactional
	public void quotationEdit(Quotation quotation) {
		quotationMapper.quotationEdit(quotation);
	}
	
	@Autowired
	QuotationProductMapper quotationProductMapper;
	
	@Transactional
	public int create(Quotation quotation) {
		quotationMapper.create(quotation);		
		return quotation.getId();
	}
	
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