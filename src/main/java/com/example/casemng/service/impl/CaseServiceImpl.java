package com.example.casemng.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.model.entity.Case;
import com.example.casemng.model.entity.CaseForList;
import com.example.casemng.model.entity.Order;
import com.example.casemng.repository.CaseMapper;
import com.example.casemng.repository.OrderMapper;
import com.example.casemng.repository.ProductMapper;
import com.example.casemng.service.CaseService;

@Service
public class CaseServiceImpl implements CaseService {

	@Autowired
	CaseMapper caseMapper;

	@Autowired
	OrderMapper orderMapper;

	@Autowired
	ProductMapper productMapper;

	public List<Case> findAll() {
		return caseMapper.findAll();
	}

	public Page<CaseForList> findByKeyword(Pageable pageable, String searchKey) {
		RowBounds rowBounds = new RowBounds(
				(int) pageable.getOffset(), pageable.getPageSize());
		List<CaseForList> cases = caseMapper.findByKeyword(rowBounds, searchKey, pageable);
		Long total = caseMapper.count(searchKey);

		return new PageImpl<>(cases, pageable, total);
	}

	@Autowired
	ModelMapper modelMapper;

	public Case findById(int id) {
		Case cases = caseMapper.findById(id);
		return cases;
	}

	@Transactional
	public void caseEdit(Case cases) {

		if (cases.getShippingStatus() == 3 && cases.isShippingStockFlg() == false) {
			Order order = orderMapper.findByCaseId(cases.getId());
			//送付済み案件の在庫を反映
			productMapper.editStock(order.getOrderProduct());
			//送付済みフラグを立てる
			caseMapper.editShippingStockFlg(cases.getId());
			caseMapper.caseEdit(cases);
		} else {
			caseMapper.caseEdit(cases);
		}
	}

	@Transactional
	public int create(Case cases) {
		caseMapper.create(cases);
		return cases.getId();
	}

	public void logicalDelete(int id) {
		caseMapper.logicalDelete(id);
	}
}
