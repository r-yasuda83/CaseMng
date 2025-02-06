package com.example.casemng.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.entity.OrderProduct;
import com.example.casemng.entity.Product;
import com.example.casemng.form.FormOrderProduct;
import com.example.casemng.repository.OrderProductMapper;
import com.example.casemng.repository.ProductMapper;

@Service
public class OrderProductServiceImpl implements OrderProductService {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	OrderProductMapper orderProductMapper;

	@Autowired
	ProductMapper productMapper;

	@Transactional
	public void orderProductEdit(List<FormOrderProduct> list) {

		List<FormOrderProduct> deleteList = new ArrayList<>();
		List<FormOrderProduct> updateList = new ArrayList<>();

		for (FormOrderProduct item : list) {
			if (item.getQuantity() == 0) {
				deleteList.add(item);
			} else {
				updateList.add(item);
			}
		}

		List<OrderProduct> delete = List.of(modelMapper.map(deleteList, OrderProduct[].class));
		List<OrderProduct> update = List.of(modelMapper.map(updateList, OrderProduct[].class));

		if (delete.size() > 0) {
			orderProductMapper.logicalDelete(delete);
		}

		if (update.size() > 0) {
			orderProductMapper.orderProductEdit(update);
		}
	}

	public OrderProduct findById(int id) {
		return orderProductMapper.findById(id);
	}

	@Transactional
	public void addOrderProduct(List<FormOrderProduct> list) {
		orderProductMapper.addOrderProduct(list);
	}

	public List<OrderProduct> findAllExport() {
		return orderProductMapper.findAllExport();
	}

	public OrderProduct findByIdAll(int id) {
		return orderProductMapper.findByIdAll(id);
	}

	public List<String> comparisonStock(List<FormOrderProduct> list) {
		List<String> quantityErrMsgs = new ArrayList<String>();
		List<Product> productList = productMapper.findAll();
		int zeroQuantityCount = 0;
		for (FormOrderProduct order : list) {
			if(order.getQuantity()==0) {
				zeroQuantityCount++;
			}
			for (Product product : productList) {
				if (order.getProductId() == product.getId() && order.getQuantity() > product.getStock()) {
					String msg = product.getProductName() + "の発注数が在庫数を超えています。在庫数：" + product.getStock();
					quantityErrMsgs.add(msg);
				}
			}
		}
		if(zeroQuantityCount==list.size()) {
			quantityErrMsgs.add("発注数が1以上の商品を最低1件登録してください");
		}
		
		return quantityErrMsgs;
	}
}