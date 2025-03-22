package com.example.casemng.form.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.casemng.form.order.OrderForm;
import com.example.casemng.form.register.RegisterProductForm;
import com.example.casemng.model.entity.Product;
import com.example.casemng.service.ProductService;

@Component
public class OrderProductListValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return OrderForm.class.equals(clazz);
	}

	@Autowired
	ProductService productService;

	@Override
	public void validate(Object target, Errors errors) {
		OrderForm form = (OrderForm) target;

		List<Product> productList = productService.findAll();
		List<RegisterProductForm> cloneList = new ArrayList<>();

		//無効な受注商品の削除
		for (RegisterProductForm sub : form.getOrderProduct()) {
			if (sub.getProductId() == null || sub.getQuantity() == null) {
				continue;
			}
			if (sub.getProductId() == 0 || sub.getQuantity() == 0) {
				continue;
			}
			RegisterProductForm copy = new RegisterProductForm(sub);
			cloneList.add(copy);
		}

		List<RegisterProductForm> checkList = new ArrayList<>();

		//商品毎にまとめる
		for (RegisterProductForm product : cloneList) {
			boolean found = false;
			for (RegisterProductForm combinedProduct : checkList) {
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

		int i = 0;
		for (RegisterProductForm order : form.getOrderProduct()) {
			for (RegisterProductForm check : checkList) {
				if (order.getProductId() == check.getProductId()) {
					for (Product product : productList) {
						if (order.getProductId() == product.getId() && order.getQuantity() > product.getStock()) {
							errors.rejectValue("orderProduct[" + i + "].quantity", "error.orderForm",
									product.getProductName() + "の発注数が在庫数を超えています。在庫数：" + product.getStock() + "　注文数："
											+ order.getQuantity());
						}
					}
				}
			}
			i++;
		}

		if (checkList.isEmpty()) {
			errors.rejectValue("orderProduct[" + i + "].quantity", "error.orderForm",
					"商品を選択、尚且つ個数が1以上の商品を最低1件登録してください");
		}

		//値引き額が商品額を上回るかチェック
		int j = 0;
		for (RegisterProductForm item : form.getOrderProduct()) {
			if (item.getProductId() == null || item.getProductId() == 0) {
				j++;
				continue;
			}
			if (item.getDiscount() == null || item.getDiscount() == 0) {
				j++;
				continue;
			}
			if (item.getQuantity() == null || item.getQuantity() == 0) {
				j++;
				continue;
			}
			for (Product product : productList) {
				if (item.getProductId() == product.getId()) {
					if (item.getQuantity() * product.getPrice() < item.getDiscount()) {
						errors.rejectValue("orderProduct[" + j + "].discount", "error.orderForm",
								"商品の金額より値引きの金額が上回っています");
					}
				}
			}
			j++;
		}

		//登録済み商品の登録可否、ストック切れのチェック
		int k = 0;
		for (RegisterProductForm formProduct : form.getOrderProduct()) {
			if (formProduct.getProductId() == null || formProduct.getProductId() == 0) {
				k++;
				continue;
			}
			if (formProduct.getQuantity() == null || formProduct.getQuantity() == 0) {
				k++;
				continue;
			}
			for (Product product : productList) {
				if (product.getStock() <= 0 || product.isChoose() == true) {
					if (product.getId() == formProduct.getProductId()) {
						errors.rejectValue("orderProduct[" + k + "].productId", "error.orderForm",
								product.getProductName() + "は受注できません。商品管理ページを確認してください。");
					}
				}
			}
			k++;
		}
	}
}
