package com.example.casemng.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.example.casemng.entity.OrderProduct;
import com.example.casemng.entity.Product;
import com.example.casemng.repository.OrderProductMapper;
import com.example.casemng.repository.ProductMapper;
import com.example.casemng.service.OrderProductService;

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

	//商品個数が在庫数を上回るかのチェック
	public BindingResult comparisonStock(List<OrderProduct> list, BindingResult result) {

		List<Product> productList = productMapper.findAll();
		List<OrderProduct> cloneList = new ArrayList<>();

		//無効な受注商品の削除
		for (OrderProduct sub : list) {
			if (sub.getProductId() == 0 || sub.getQuantity() == 0) {
				continue;
			}
			OrderProduct copy = new OrderProduct(sub);
			cloneList.add(copy);
		}

		List<OrderProduct> checkList = new ArrayList<>();

		//商品毎にまとめる
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

		int i = 0;
		for (OrderProduct order : list) {
			for (OrderProduct check : checkList) {
				if (order.getProductId() == check.getProductId()) {
					for (Product product : productList) {
						if (order.getProductId() == product.getId() && order.getQuantity() > product.getStock()) {
							result.rejectValue("orderProduct[" + i + "].quantity", "error.orderForm",
									product.getProductName() + "の発注数が在庫数を超えています。在庫数：" + product.getStock() + "　注文数："
											+ order.getQuantity());
						}
					}
				}
			}
			i++;
		}

		if (checkList.isEmpty()) {
			result.rejectValue("orderProduct[" + i + "].quantity", "error.orderForm",
					"商品を選択、尚且つ個数が1以上の商品を最低1件登録してください");
		}

		return result;
	}

	//値引き額が商品額を上回るかチェック
	public BindingResult checkDiscount(List<OrderProduct> list, BindingResult result) {

		List<Product> productList = productMapper.findAll();

		int i = 0;
		for (OrderProduct item : list) {
			if (item.getProductId() == 0) {
				i++;
				continue;
			}
			if (item.getDiscount() == 0) {
				i++;
				continue;
			}
			if (item.getQuantity() == 0) {
				i++;
				continue;
			}
			for (Product product : productList) {
				if (item.getProductId() == product.getId()) {
					if (item.getQuantity() * product.getPrice() < item.getDiscount()) {
						result.rejectValue("orderProduct[" + i + "].discount", "error.orderForm",
								"商品の金額より値引きの金額が上回っています");
					}
				}
			}
			i++;
		}
		return result;
	}

	//登録済み商品の登録可否、ストック切れのチェック
	public BindingResult checkProduct(List<OrderProduct> list, BindingResult result) {

		List<Product> productList = productMapper.findAll();
		int i = 0;
		for (OrderProduct formProduct : list) {
			if (formProduct.getProductId() == 0) {
				i++;
				continue;
			}
			if (formProduct.getQuantity() == 0) {
				i++;
				continue;
			}
			for (Product product : productList) {
				if (product.getStock() <= 0 || product.isChoose() == true) {
					if (product.getId() == formProduct.getProductId()) {
						result.rejectValue("orderProduct[" + i + "].productId", "error.orderForm",
								product.getProductName() + "は受注できません。商品管理ページを確認してください。");
					}
				}
			}
			i++;
		}
		return result;
	}
}