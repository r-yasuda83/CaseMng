package com.example.casemng.service.impl;

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

import com.example.casemng.entity.Product;
import com.example.casemng.repository.ProductMapper;
import com.example.casemng.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductMapper mapper;
	
	@Autowired
	ModelMapper modelMapper;
	
	public List<Product> findAll(){
		return mapper.findAll();
	}
	
	public List<Product> findAllForSelect(){
		return mapper.findAllForSelect();
	}
	
	public List<Product> findAllForSelectStock(){
		return mapper.findAllForSelectStock();
	}
	
	public Product findById(int id) {
		Product product = mapper.findById(id);
		return product;
	}
	
	public Page<Product> findByKeyword(Pageable pageable, String searchKey) {
		RowBounds rowBounds = new RowBounds(
				(int) pageable.getOffset(), pageable.getPageSize());
		List<Product> product = mapper.findByKeyword(rowBounds, searchKey, pageable);
		Long total = mapper.count(searchKey);
		return new PageImpl<>(product, pageable, total);
	}
	
	@Transactional
	public void edit(Product product) {
		mapper.edit(product);
	}
	
	@Transactional
	public void create(Product product) {
		mapper.create(product);
	}
	
	public String pagenation(String searchKey, Integer displayedNum, String sortKey, String sortDirection, Pageable pageable, Model model) {
		if (searchKey == null) {
			searchKey = "";
		}
		Sort sort = null;
		if (StringUtils.hasLength(sortDirection)) {
			String sd = sortDirection.equals(Sort.Direction.ASC.name()) ? Sort.Direction.ASC.name() : Sort.Direction.DESC.name();
			String si = sortKey;
			model.addAttribute("sortKey", si);

			sort = Sort.by(Sort.Direction.fromString(sd), si);
			model.addAttribute("sortDirection", sd);
		}else {
			sort = Sort.by(Sort.Direction.ASC, "id");
		}
		if(displayedNum == null) {
			displayedNum = 5;
		}
		Pageable p = sort == null ? pageable : PageRequest.of(pageable.getPageNumber(), displayedNum, sort);
		Page<Product> product = findByKeyword(p, "%" + searchKey + "%");
		model.addAttribute("page", product);
		
		return "product/list";
	}
}