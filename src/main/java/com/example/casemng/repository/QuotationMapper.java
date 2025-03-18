package com.example.casemng.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.casemng.model.entity.Quotation;

@Mapper
public interface QuotationMapper {

	public Quotation findById(int id);
	public void quotationEdit(Quotation quotation);
	public void create(Quotation quotation);
	public void logicalDelete(int id);
}
