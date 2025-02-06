package com.example.casemng.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.entity.Case;
import com.example.casemng.entity.Order;
import com.example.casemng.form.FormCase;
import com.example.casemng.form.FormCaseEntry;
import com.example.casemng.repository.CaseMapper;
import com.example.casemng.repository.OrderMapper;
import com.example.casemng.repository.ProductMapper;

@Service
public class CaseServiceImpl implements CaseService{

	@Autowired
	CaseMapper caseMapper;
	
	@Autowired
	OrderMapper orderMapper;
	
	@Autowired
	ProductMapper productMapper;
	
	public List<Case> findAll() {
		return caseMapper.findAll();
	}
	
	@Autowired
	ModelMapper modelMapper;

	public FormCase findById(int id) {
		Case cases = caseMapper.findById(id);
		FormCase form = modelMapper.map(cases, FormCase.class);
		return form;
	}
	
	@Transactional
	public void caseEdit(FormCase form) {
		if(form.getShippingStatus() == 3 && form.isShippingStockFlg() == false) {
			//送付済みフラグを立てる
			caseMapper.editShippingStockFlg(form.getId());
			
			Order order = orderMapper.findByCaseId(form.getId());
			//送付済み案件の在庫を反映
			productMapper.editStock(order.getOrderProduct());
		}
		Case cases = modelMapper.map(form, Case.class);
		caseMapper.caseEdit(cases);
	}
	
	@Transactional
	public int create(FormCaseEntry form) {
		Case cases = modelMapper.map(form, Case.class);
		caseMapper.create(cases);
		return cases.getId();
	}
	
	public void logicalDelete(int id) {
		caseMapper.logicalDelete(id);
	}
}
