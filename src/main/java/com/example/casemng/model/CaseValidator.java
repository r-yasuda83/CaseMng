package com.example.casemng.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.casemng.form.CaseForm;
import com.example.casemng.model.entity.Order;
import com.example.casemng.model.entity.OrderProduct;
import com.example.casemng.model.entity.Product;
import com.example.casemng.service.OrderService;
import com.example.casemng.service.ProductService;

@Component
public class CaseValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return CaseForm.class.equals(clazz);
	}
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	ProductService productService;
	
	
	public void validate(Object target, Errors errors) {
		CaseForm form = (CaseForm) target;
		
		if (form.getShippingStatus() == 3 && form.isShippingStockFlg() == false) {
			Order order = orderService.findByCaseId(form.getId());

			List<OrderProduct> cloneList = new ArrayList<>();

			for (OrderProduct sub : order.getOrderProduct()) {
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

			List<Product> productList = productService.findAll();

			int i = 0;
			for (OrderProduct orderProduct : checkList) {
				for (Product product : productList) {
					if (orderProduct.getProductId() == product.getId()
							&& orderProduct.getQuantity() > product.getStock()) {

						errors.rejectValue("order.orderProduct[" + i + "].quantity", "error.caseForm",
								product.getProductName() + "の発注数が在庫数を超えています。在庫数：" + product.getStock() + "　注文数："
										+ orderProduct.getQuantity());
					}
				}
				i++;
			}
		} 
	}
}
