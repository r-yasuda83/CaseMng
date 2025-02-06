package com.example.casemng.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.casemng.entity.Case;

@Mapper
public interface CaseMapper {

	public List<Case> findAll();
	public Case findById(int id);
	public void caseEdit(Case cases);
	public void create(Case cases);
	public void logicalDelete(int id);
	public void editShippingStockFlg(int id);
}
