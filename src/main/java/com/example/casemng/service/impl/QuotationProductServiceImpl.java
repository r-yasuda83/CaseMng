package com.example.casemng.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.model.entity.QuotationProduct;
import com.example.casemng.repository.ProductMapper;
import com.example.casemng.repository.QuotationProductMapper;
import com.example.casemng.service.QuotationProductService;

@Service
public class QuotationProductServiceImpl implements QuotationProductService {

	@Autowired
	QuotationProductMapper quotationMapper;

	@Autowired
	ProductMapper productMapper;

	public QuotationProduct findById(int id) {
		return quotationMapper.findById(id);
	}

	@Transactional
	public void addQuotationProduct(List<QuotationProduct> list) {

		List<QuotationProduct> updateList = new ArrayList<>();
		for (QuotationProduct item : list) {
			//商品の有無
			if (item.getProductId() <= 0) {
				continue;
			}
			//注文数の有無
			if (item.getQuantity() <= 0) {
				continue;
			}
			updateList.add(item);
		}
		quotationMapper.addQuotationProduct(updateList);
	}

	public List<QuotationProduct> findAllExport() {
		return quotationMapper.findAllExport();
	}

	public QuotationProduct findByIdAll(int id) {
		return quotationMapper.findByIdAll(id);
	}

	@Transactional
	public List<QuotationProduct> setQuotationId(List<QuotationProduct> list, int quotationId) {
		for (QuotationProduct item : list) {
			item.setQuotationId(quotationId);
		}
		return list;
	}

	@Transactional
	public void edit(List<QuotationProduct> list, int quotationId) {

		List<QuotationProduct> deleteList = new ArrayList<>();
		List<QuotationProduct> updateList = new ArrayList<>();

		for (QuotationProduct item : list) {
			//商品の有無
			if (item.getProductId() <= 0) {
				if (item.getId() <= 0) {
					continue;
				}
				deleteList.add(item);
			}
			//注文数の有無
			if (item.getQuantity() <= 0) {
				//
				if (item.getId() <= 0) {
					continue;
				}
				deleteList.add(item);
			}
			updateList.add(item);
		}

		if (deleteList.size() > 0) {
			quotationMapper.logicalDelete(deleteList);
		}

		List<QuotationProduct> editList = new ArrayList<>();
		List<QuotationProduct> createList = new ArrayList<>();

		if (updateList.size() > 0) {
			for (QuotationProduct item : updateList) {
				if (item.getId() == 0) {
					createList.add(item);
				} else {
					editList.add(item);
				}
			}
		}

		if (editList.size() > 0) {
			quotationMapper.edit(editList);
		}
		if (createList.size() > 0) {
			for (QuotationProduct item : createList) {
				if (item.getQuotationId() == 0) {
					item.setQuotationId(quotationId);
				}
			}
			quotationMapper.create(createList);
		}
	}
}