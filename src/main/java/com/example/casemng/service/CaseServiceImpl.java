package com.example.casemng.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import com.example.casemng.entity.Case;
import com.example.casemng.entity.CaseForList;
import com.example.casemng.entity.Order;
import com.example.casemng.entity.OrderProduct;
import com.example.casemng.entity.Product;
import com.example.casemng.form.FormCase;
import com.example.casemng.form.FormCaseEntry;
import com.example.casemng.repository.CaseMapper;
import com.example.casemng.repository.OrderMapper;
import com.example.casemng.repository.ProductMapper;

@Service
public class CaseServiceImpl implements CaseService {

	@Autowired
	CaseMapper caseMapper;

	@Autowired
	OrderMapper orderMapper;

	@Autowired
	ProductMapper productMapper;

	public Page<Case> findAll(Pageable pageable) {
		RowBounds rowBounds = new RowBounds(
				(int) pageable.getOffset(), pageable.getPageSize());
		List<Case> cases = caseMapper.findAll(rowBounds);

		Long total = caseMapper.count("");
		return new PageImpl<>(cases, pageable, total);
	}

	public Page<CaseForList> findByKeyword(Pageable pageable, String searchKey) {
		RowBounds rowBounds = new RowBounds(
				(int) pageable.getOffset(), pageable.getPageSize());
		List<CaseForList> cases = caseMapper.findByKeyword(rowBounds, searchKey, pageable);
		Long total = caseMapper.count(searchKey);

		return new PageImpl<>(cases, pageable, total);
	}

	@Autowired
	ModelMapper modelMapper;

	public FormCase findById(int id) {
		Case cases = caseMapper.findById(id);
		FormCase form = modelMapper.map(cases, FormCase.class);
		return form;
	}

	@Transactional
	public List<String> caseEdit(FormCase form) {
		List<String> errMsgs = new ArrayList<String>();
		if (form.getShippingStatus() == 3 && form.isShippingStockFlg() == false) {
			Order order = orderMapper.findByCaseId(form.getId());

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

			List<Product> productList = productMapper.findAll();

			for (OrderProduct orderProduct : checkList) {
				for (Product product : productList) {
					if (orderProduct.getProductId() == product.getId()
							&& orderProduct.getQuantity() > product.getStock()) {
						String msg = product.getProductName() + "の発注数が在庫数を超えています。在庫数：" + product.getStock() + "　注文数：" + orderProduct.getQuantity();
						errMsgs.add(msg);
					}
				}
			}
			if (errMsgs.isEmpty()) {
				//送付済み案件の在庫を反映
				productMapper.editStock(order.getOrderProduct());
				//送付済みフラグを立てる
				caseMapper.editShippingStockFlg(form.getId());
				Case cases = modelMapper.map(form, Case.class);
				caseMapper.caseEdit(cases);
			}
		}
		return errMsgs;
	}

	@Transactional
	public int create(FormCaseEntry form) {
		Case cases = modelMapper.map(form, Case.class);
		caseMapper.create(cases);
		return cases.getId();
	}

	public void logicalDelete(int id) {
		caseMapper.logicalDelete(id);
	}

	public String pagenation(String searchKey, Integer displayedNum, String sortKey, String sortDirection,
			Pageable pageable, Model model) {
		if (searchKey == null) {
			searchKey = "";
		}
		Sort sort = null;
		if (StringUtils.hasLength(sortDirection)) {
			String sd = sortDirection.equals(Sort.Direction.ASC.name()) ? Sort.Direction.ASC.name()
					: Sort.Direction.DESC.name();
			String si = sortKey;
			model.addAttribute("sortKey", si);

			sort = Sort.by(Sort.Direction.fromString(sd), si);
			model.addAttribute("sortDirection", sd);
		} else {
			sort = Sort.by(Sort.Direction.ASC, "id");
		}
		if (displayedNum == null) {
			displayedNum = 5;
		}
		Pageable p = sort == null ? pageable : PageRequest.of(pageable.getPageNumber(), displayedNum, sort);
		Page<CaseForList> cases = findByKeyword(p, "%" + searchKey + "%");
		model.addAttribute("page", cases);

		return "case/list";
	}
}
