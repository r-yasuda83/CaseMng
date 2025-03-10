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
			if (item.getProductId() == null || item.getProductId() <= 0) {
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
			if (item.getProductId() == null || item.getProductId() <= 0) {
				if (item.getId() <= 0) {
					continue;
				}
				deleteList.add(item);
			}
			//注文数の有無
			if (item.getQuantity() == null || item.getQuantity() <= 0) {
				//
				if (item.getId() <= 0) {
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
	public List<String> comparisonStock(List<FormQuotationProduct> list) {

		List<String> quantityErrMsgs = new ArrayList<String>();
		List<Product> productList = productMapper.findAll();

		List<FormQuotationProduct> cloneList = new ArrayList<>();

		for (FormQuotationProduct sub : list) {
			if (sub.getProductId() == null || sub.getQuantity() == null) {
				continue;
			}
			if (sub.getProductId() == 0 || sub.getQuantity() == 0) {
				continue;
			}
			FormQuotationProduct copy = new FormQuotationProduct(sub);
			cloneList.add(copy);
		}

		List<FormQuotationProduct> checkList = new ArrayList<>();

		for (FormQuotationProduct product : cloneList) {
			boolean found = false;
			for (FormQuotationProduct combinedProduct : checkList) {
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

		for (FormQuotationProduct quotation : checkList) {
			for (Product product : productList) {
				if (quotation.getProductId() == product.getId() && quotation.getQuantity() > product.getStock()) {
					String msg = product.getProductName() + "の見積個数が在庫数を超えています。在庫数：" + product.getStock();
					quantityErrMsgs.add(msg);
				}
			}
		}

		if (checkList.isEmpty()) {
			quantityErrMsgs.add("商品を選択、尚且つ個数が1以上の商品を最低1件登録してください");
		}

		return quantityErrMsgs;
	}

	//値引き額が商品額を上回るかチェック
	public String checkDiscount(List<FormQuotationProduct> list) {

		String msg = "";
		List<Product> productList = productMapper.findAll();

		for (FormQuotationProduct item : list) {
			if (item.getProductId() == null || item.getProductId() == 0) {
				continue;
			}
			if (item.getDiscount() == null || item.getDiscount() == 0) {
				continue;
			}
			if (item.getQuantity() == null || item.getQuantity() == 0) {
				continue;
			}
			for (Product product : productList) {
				if (item.getProductId() == product.getId()) {
					if (item.getQuantity() * product.getPrice() < item.getDiscount()) {
						msg = "商品の金額より値引きの金額が上回っています";
					}
				}
			}
		}
		return msg;
	}

	//登録済み商品の登録可否、ストック切れのチェック
	public String checkProduct(List<FormQuotationProduct> list) {
		String msg = "";
		List<Product> productList = productMapper.findAll();
		for (FormQuotationProduct formProduct : list) {
			for (Product product : productList) {
				if (product.getStock() <= 0 || product.isChoose() == true) {
					if (product.getId() == formProduct.getProductId()) {
						msg = ("受注できない商品が含まれています。商品管理ページを確認してください。");
						break;
					}
				}
			}
		}
		return msg;
	}
}