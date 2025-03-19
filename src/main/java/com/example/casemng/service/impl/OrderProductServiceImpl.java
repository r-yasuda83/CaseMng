package com.example.casemng.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.model.entity.OrderProduct;
import com.example.casemng.repository.OrderProductMapper;
import com.example.casemng.service.OrderProductService;

@Service
public class OrderProductServiceImpl implements OrderProductService {

	@Autowired
	OrderProductMapper orderProductMapper;

	public OrderProduct findById(int id) {
		return orderProductMapper.findById(id);
	}

	@Transactional
	public void addOrderProduct(List<OrderProduct> list) {

		List<OrderProduct> updateList = new ArrayList<>();
		for (OrderProduct item : list) {
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
		orderProductMapper.addOrderProduct(updateList);
	}

	public List<OrderProduct> findAllExport() {
		return orderProductMapper.findAllExport();
	}

	public OrderProduct findByIdAll(int id) {
		return orderProductMapper.findByIdAll(id);
	}

	@Transactional
	public List<OrderProduct> setOrdersId(List<OrderProduct> list, int ordersId) {
		for (OrderProduct item : list) {
			item.setOrdersId(ordersId);
		}
		return list;
	}

	@Transactional
	public void edit(List<OrderProduct> list, int orderId) {

		List<OrderProduct> deleteList = new ArrayList<>();
		List<OrderProduct> updateList = new ArrayList<>();

		for (OrderProduct item : list) {
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
			orderProductMapper.logicalDelete(deleteList);
		}

		List<OrderProduct> editList = new ArrayList<>();
		List<OrderProduct> createList = new ArrayList<>();

		if (updateList.size() > 0) {
			for (OrderProduct item : updateList) {
				if (item.getId() == 0) {
					createList.add(item);
				} else {
					editList.add(item);
				}
			}
		}

		if (editList.size() > 0) {
			orderProductMapper.edit(editList);
		}
		if (createList.size() > 0) {
			for (OrderProduct item : createList) {
				if (item.getOrdersId() == 0) {
					item.setOrdersId(orderId);
				}
			}
			orderProductMapper.create(createList);
		}
	}
}