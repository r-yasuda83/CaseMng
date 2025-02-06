package com.example.casemng.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.entity.Quotation;
import com.example.casemng.entity.QuotationProduct;
import com.example.casemng.form.FormQuotation;
import com.example.casemng.form.FormQuotationProduct;
import com.example.casemng.repository.QuotationMapper;
import com.example.casemng.repository.QuotationProductMapper;

@Service
public class QuotationServiceImpl implements QuotationService{
	
	@Autowired
	QuotationMapper quotationMapper;
	
	@Autowired
	ModelMapper modelMapper;

	public FormQuotation findById(int id) {
		Quotation quotation = quotationMapper.findById(id);
		FormQuotation form = modelMapper.map(quotation, FormQuotation.class);
		return form;
	}

	@Transactional
	public void quotationEdit(FormQuotation form) {
		Quotation quotation = modelMapper.map(form, Quotation.class);
		quotationMapper.quotationEdit(quotation);
	}
	
	@Autowired
	QuotationProductMapper quotationProductMapper;
	
	@Transactional
	public int create(FormQuotation form) {
		Quotation quotation = modelMapper.map(form, Quotation.class);
		quotationMapper.create(quotation);
		
		List<QuotationProduct> validList = new ArrayList<>();
		for (QuotationProduct orpr : quotation.getQuotationProduct()) {
			if (orpr.getQuantity() <= 0) {
				continue;
			}
			orpr.setQuotationId(quotation.getId());
			validList.add(orpr);
		}
		
		if(validList.isEmpty() == false) {
			quotationProductMapper.create(validList);
		}
		
		return quotation.getId();
	}
	
	public void logicalDelete(int id) {
		quotationMapper.logicalDelete(id);
	}
	
	public List<FormQuotationProduct> generateProductList(){
		int ProductCount = 5;
		List<FormQuotationProduct> quotationProductList = new ArrayList<>();
		for (int i = 0; i < ProductCount; i++) {
			quotationProductList.add(new FormQuotationProduct());
		}
		return quotationProductList;
	}
}