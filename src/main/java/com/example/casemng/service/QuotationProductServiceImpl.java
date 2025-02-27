package com.example.casemng.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.entity.Product;
import com.example.casemng.entity.QuotationProduct;
import com.example.casemng.form.FormQuotationProduct;
import com.example.casemng.repository.ProductMapper;
import com.example.casemng.repository.QuotationProductMapper;

@Service
public class QuotationProductServiceImpl implements QuotationProductService {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	QuotationProductMapper quotationMapper;

	@Autowired
	ProductMapper productMapper;

	public QuotationProduct findById(int id) {
		return quotationMapper.findById(id);
	}

	@Transactional
	public void addQuotationProduct(List<FormQuotationProduct> list) {
		
		List<FormQuotationProduct> updateList = new ArrayList<>();
		for (FormQuotationProduct item : list) {
			//商品の有無
			if(item.getProductId() == null || item.getProductId() <= 0) {
				continue;
			}
			//注文数の有無
			if (item.getQuantity() == null || item.getQuantity() <= 0) {
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

	public List<FormQuotationProduct> setQuotationId(List<FormQuotationProduct> list, int quotationId) {
		for (FormQuotationProduct item : list) {
			item.setQuotationId(quotationId);
		}
		return list;
	}

	@Transactional
	public void edit(List<FormQuotationProduct> list, int quotationId) {

		List<FormQuotationProduct> deleteList = new ArrayList<>();
		List<FormQuotationProduct> updateList = new ArrayList<>();

		for (FormQuotationProduct item : list) {
			//商品の有無
			if(item.getProductId() == null || item.getProductId() <= 0) {
				if(item.getId() <= 0) {
					continue;
				}
				deleteList.add(item);
			}
			//注文数の有無
			if (item.getQuantity() == null || item.getQuantity() <= 0) {
				//
				if(item.getId() <= 0) {
					continue;
				}
				deleteList.add(item);
			}
			updateList.add(item);
		}

		List<QuotationProduct> delete = List.of(modelMapper.map(deleteList, QuotationProduct[].class));
		List<QuotationProduct> update = List.of(modelMapper.map(updateList, QuotationProduct[].class));

		if (delete.size() > 0) {
			quotationMapper.logicalDelete(delete);
		}

		List<QuotationProduct> editList = new ArrayList<>();
		List<QuotationProduct> createList = new ArrayList<>();

		if (update.size() > 0) {
			for (QuotationProduct item : update) {
				if (item.getId() == 0) {
					createList.add(item);
				} else {
					editList.add(item);
				}
			}
		}

		if(editList.size() > 0) {
			quotationMapper.edit(editList);
		}
		if(createList.size() > 0) {
			for (QuotationProduct item : createList) {
				if(item.getQuotationId() == 0) {
					item.setQuotationId(quotationId);
				}
			}
			quotationMapper.create(createList);
		}
	}

	//商品個数が在庫を上回るかのチェック
	public List<String> comparisonStock(List<FormQuotationProduct> list) {
		
		List<String> quantityErrMsgs = new ArrayList<String>();
		List<Product> productList = productMapper.findAll();
		int zeroQuantityCount = 0;

		for (FormQuotationProduct quotation : list) {
			if (quotation.getProductId() == null || quotation.getProductId() <= 0) {
				zeroQuantityCount++;
				continue;
			}
			if (quotation.getQuantity() == null || quotation.getQuantity() <= 0) {
				zeroQuantityCount++;
				continue;
			}
			for (Product product : productList) {
				if (quotation.getProductId() == product.getId() && quotation.getQuantity() > product.getStock()) {
					String msg = product.getProductName() + "の見積個数が在庫数を超えています。在庫数：" + product.getStock();
					quantityErrMsgs.add(msg);
				}
			}
		}

		if (zeroQuantityCount == list.size()) {
			quantityErrMsgs.add("個数が1以上の商品を最低1件登録してください");
		}

		return quantityErrMsgs;
	}
}