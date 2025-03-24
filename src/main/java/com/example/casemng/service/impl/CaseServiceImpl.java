package com.example.casemng.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.constant.Constant;
import com.example.casemng.model.OutOfStock;
import com.example.casemng.model.entity.Case;
import com.example.casemng.model.entity.CaseForList;
import com.example.casemng.model.entity.Order;
import com.example.casemng.model.entity.OrderProduct;
import com.example.casemng.model.entity.Product;
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

	public Case findById(int id) {
		Case cases = caseMapper.findById(id);
		return cases;
	}

	@Transactional
	public void caseEdit(Case cases) {

		int shipped = Constant.ShippingStatus.Shipped.getValue();
		if (cases.getShippingStatus() == shipped && cases.isShippingStockFlg() == false) {
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

	@Transactional
	public void logicalDelete(int id) {
		caseMapper.logicalDelete(id);
	}

	public List<OutOfStock> checkStock(int id) {

		Order order = orderMapper.findByCaseId(id);
		List<OrderProduct> checkList = organizeList(order.getOrderProduct());

		List<Product> productList = productMapper.findAll();
		List<OutOfStock> outOfStockList = new ArrayList<>();

		for (OrderProduct orderProduct : checkList) {
			for (Product product : productList) {
				if (orderProduct.getProductId() == product.getId()
						&& orderProduct.getQuantity() > product.getStock()) {
					OutOfStock os = new OutOfStock();
					os.setProduct(product);
					os.setRegistedQuantity(orderProduct.getQuantity());
					outOfStockList.add(os);
				}
			}
		}
		return outOfStockList;
	}
	
	public List<OrderProduct> organizeList(List<OrderProduct> list){
		
		List<OrderProduct> cloneList = new ArrayList<>();

		for (OrderProduct sub : list) {
			OrderProduct copy = new OrderProduct(sub);
			cloneList.add(copy);
		}

		List<OrderProduct> checkList = new ArrayList<>();

		for (OrderProduct product : cloneList) {
			boolean found = false;
			for (OrderProduct combinedProduct : checkList) {
				if (combinedProduct.getProductId() == product.getProductId()) {
					combinedProduct.setQuantity(combinedProduct.getQuantity() + product.getQuantity());
					found = true;
					break;
				}
			}
			if (!found) {
				checkList.add(product);
			}
		}
		return checkList;
	}
}
