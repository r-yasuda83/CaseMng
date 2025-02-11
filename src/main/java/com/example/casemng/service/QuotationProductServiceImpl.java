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
public class QuotationProductServiceImpl implements QuotationProductService{

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
		quotationMapper.addQuotationProduct(list);
	}
	
	public List<QuotationProduct> findAllExport(){
		return quotationMapper.findAllExport();
	}
	
	public QuotationProduct findByIdAll(int id) {
		return quotationMapper.findByIdAll(id);
	}
	
	public List<FormQuotationProduct> setQuotationId(List<FormQuotationProduct> list, int quotationId){
		for(FormQuotationProduct item : list) {
			item.setQuotationId(quotationId);
		}
		return list;
	}
	
	//個数が1以上のものは更新、0以下のものは論理削除
	@Transactional
	public void edit(List<FormQuotationProduct> list) {
		
		List<FormQuotationProduct> deleteList = new ArrayList<>();
		List<FormQuotationProduct> updateList = new ArrayList<>();;
		
		for(FormQuotationProduct item : list) {
			if(item.getQuantity() == null || item.getQuantity() <= 0) {
				deleteList.add(item);
			}else {
				updateList.add(item);
			}
		}
		
		List<QuotationProduct> delete = List.of(modelMapper.map(deleteList, QuotationProduct[].class));
		List<QuotationProduct> update = List.of(modelMapper.map(updateList, QuotationProduct[].class));
		
		if(delete.size() > 0) {
			quotationMapper.logicalDelete(delete);
		}
		
		if(update.size() > 0) {
			quotationMapper.edit(update);
		}
	}
	
	public List<FormQuotationProduct> organizeList(List<FormQuotationProduct> list) {

		List<FormQuotationProduct> validQuotationProductList = new ArrayList<>();
		for (FormQuotationProduct fop : list) {
			if (fop.getQuantity() == null || fop.getQuantity() <= 0) {
				continue;
			}
			validQuotationProductList.add(fop);
		}
		return validQuotationProductList;
	}

	public List<FormQuotationProduct> organizeList(List<FormQuotationProduct> list, int quotationId) {
		if (quotationId > 0) {
			for (FormQuotationProduct item : list) {
				item.setQuotationId(quotationId);
			}
		}
		List<FormQuotationProduct> validQuotationProductList = new ArrayList<>();
		for (FormQuotationProduct fop : list) {
			if (fop.getQuantity() == null || fop.getQuantity() <= 0) {
				continue;
			}
			validQuotationProductList.add(fop);
		}
		return validQuotationProductList;
	}
	
	//商品個数が在庫を上回るかのチェック
	public List<String> comparisonStock(List<FormQuotationProduct> list) {
		List<String> quantityErrMsgs = new ArrayList<String>();
		List<Product> productList = productMapper.findAll();
		int zeroQuantityCount = 0;
		
		for (FormQuotationProduct quotation : list) {
			if(quotation.getQuantity() == null  || quotation.getQuantity() <= 0) {
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
		
		if(zeroQuantityCount==list.size()) {
			quantityErrMsgs.add("個数が1以上の商品を最低1件登録してください");
		}
		
		return quantityErrMsgs;
	}
}