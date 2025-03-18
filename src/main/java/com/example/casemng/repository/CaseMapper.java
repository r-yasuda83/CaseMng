package com.example.casemng.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Pageable;

import com.example.casemng.model.entity.Case;
import com.example.casemng.model.entity.CaseForList;

@Mapper
public interface CaseMapper {

	public List<Case> findAll();
	public List<CaseForList> findByKeyword(RowBounds rowBounds, String searchKey, Pageable pageable);
	public Long count(String searchKey);
	public Case findById(int id);
	public void caseEdit(Case cases);
	public void create(Case cases);
	public void logicalDelete(int id);
	public void editShippingStockFlg(int id);
	public void whenDeleteOrder(int id);
}
