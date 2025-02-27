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

	public OrderProduct findById(int id) {
		return orderProductMapper.findById(id);
	}

	@Transactional
	public void addOrderProduct(List<FormOrderProduct> list) {
		
		List<FormOrderProduct> updateList = new ArrayList<>();
		for (FormOrderProduct item : list) {
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
		orderProductMapper.addOrderProduct(updateList);
	}

	public List<OrderProduct> findAllExport() {
		return orderProductMapper.findAllExport();
	}

	public OrderProduct findByIdAll(int id) {
		return orderProductMapper.findByIdAll(id);
	}

	public List<FormOrderProduct> setOrdersId(List<FormOrderProduct> list, int ordersId) {
		for (FormOrderProduct item : list) {
			item.setOrdersId(ordersId);
		}
		return list;
	}

	@Transactional
	public void edit(List<FormOrderProduct> list, int orderId) {

		List<FormOrderProduct> deleteList = new ArrayList<>();
		List<FormOrderProduct> updateList = new ArrayList<>();

		for (FormOrderProduct item : list) {
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

		List<OrderProduct> delete = List.of(modelMapper.map(deleteList, OrderProduct[].class));
		List<OrderProduct> update = List.of(modelMapper.map(updateList, OrderProduct[].class));
		
		if (delete.size() > 0) {
			orderProductMapper.logicalDelete(delete);
		}

		List<OrderProduct> editList = new ArrayList<>();
		List<OrderProduct> createList = new ArrayList<>();
		
		if (update.size() > 0) {
			for(OrderProduct item : update) {
				if(item.getId() == 0) {
					createList.add(item);
				}else {
					editList.add(item);
				}
			}
		}
		
		if(editList.size() > 0) {
			orderProductMapper.edit(editList);
		}
		if(createList.size() > 0) {
			for (OrderProduct item : createList) {
				if(item.getOrdersId() == 0) {
					item.setOrdersId(orderId);
				}
			}
			orderProductMapper.create(createList);
		}
	}
	
	//商品個数が在庫数を上回るかのチェック
	public List<String> comparisonStock(List<FormOrderProduct> list) {
		
		List<String> quantityErrMsgs = new ArrayList<String>();
		List<Product> productList = productMapper.findAll();
		int zeroQuantityCount = 0;

		for (FormOrderProduct order : list) {
			if (order.getProductId() == null || order.getProductId() <= 0) {
				zeroQuantityCount++;
				continue;
			}
			if (order.getQuantity() == null || order.getQuantity() <= 0) {
				zeroQuantityCount++;
				continue;
			}
			for (Product product : productList) {
				if (order.getProductId() == product.getId() && order.getQuantity() > product.getStock()) {
					String msg = product.getProductName() + "の発注数が在庫数を超えています。在庫数：" + product.getStock();
					quantityErrMsgs.add(msg);
				}
			}
		}

		if (zeroQuantityCount == list.size()) {
			quantityErrMsgs.add("商品を選択、尚且つ個数が1以上の商品を最低1件登録してください");
		}

		return quantityErrMsgs;
	}
}