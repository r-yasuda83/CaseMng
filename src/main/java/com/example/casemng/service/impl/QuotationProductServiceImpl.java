package com.example.casemng.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.example.casemng.entity.Product;
import com.example.casemng.entity.QuotationProduct;
import com.example.casemng.repository.ProductMapper;
import com.example.casemng.repository.QuotationProductMapper;
import com.example.casemng.service.QuotationProductService;

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

	//商品個数が在庫を上回るかのチェック
	public BindingResult comparisonStock(List<QuotationProduct> list, BindingResult result) {

		List<Product> productList = productMapper.findAll();
		List<QuotationProduct> cloneList = new ArrayList<>();

		for (QuotationProduct sub : list) {
			if (sub.getProductId() == 0 || sub.getQuantity() == 0) {
				continue;
			}
			QuotationProduct copy = new QuotationProduct(sub);
			cloneList.add(copy);
		}

		List<QuotationProduct> checkList = new ArrayList<>();

		for (QuotationProduct product : cloneList) {
			boolean found = false;
			for (QuotationProduct combinedProduct : checkList) {
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
		for (QuotationProduct quotation : list) {
			for (QuotationProduct check : checkList) {
				if (quotation.getProductId() == check.getProductId()) {
					for (Product product : productList) {
						if (quotation.getProductId() == product.getId() && quotation.getQuantity() > product.getStock()) {
							result.rejectValue("quotationProduct[" + i + "].quantity", "error.quotationForm",
									product.getProductName() + "の発注数が在庫数を超えています。在庫数：" + product.getStock() + "　注文数："
											+ quotation.getQuantity());
						}
					}
				}
			}
			i++;
		}

		if (checkList.isEmpty()) {
			result.rejectValue("quotationProduct[" + i + "].quantity", "error.quotationForm",
					"商品を選択、尚且つ個数が1以上の商品を最低1件登録してください");
		}

		return result;
	}

	//値引き額が商品額を上回るかチェック
	public BindingResult checkDiscount(List<QuotationProduct> list, BindingResult result) {

		List<Product> productList = productMapper.findAll();

		int i = 0;
		for (QuotationProduct item : list) {
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
						result.rejectValue("quotationProduct[" + i + "].discount", "error.quotationForm",
								"商品の金額より値引きの金額が上回っています");
					}
				}
			}
			i++;
		}
		return result;
	}

	//登録済み商品の登録可否、ストック切れのチェック
	public BindingResult checkProduct(List<QuotationProduct> list, BindingResult result) {

		List<Product> productList = productMapper.findAll();
		int i = 0;
		for (QuotationProduct formProduct : list) {
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
						result.rejectValue("quotationProduct[" + i + "].productId", "error.quotationForm",
								product.getProductName() + "は受注できません。商品管理ページを確認してください。");
					}
				}
			}
			i++;
		}
		return result;
	}
}