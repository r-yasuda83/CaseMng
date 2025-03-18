package com.example.casemng.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.casemng.model.entity.QuotationProduct;

@Mapper
public interface QuotationProductMapper {

	public List<QuotationProduct> findByQuotationId(int id);
	public void edit(List<QuotationProduct> quotationProduct);
	public void logicalDelete(List<QuotationProduct> quotationProduct);
	public QuotationProduct findById(int id);
	public void addQuotationProduct(List<QuotationProduct> list);
	public List<QuotationProduct> findAllExport();
	public void create(List<QuotationProduct> quotationProduct);
	public QuotationProduct findByIdAll(int id);
}