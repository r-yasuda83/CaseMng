package com.example.casemng.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.casemng.model.OutOfStock;
import com.example.casemng.model.entity.Case;
import com.example.casemng.model.entity.CaseForList;

public interface CaseService {

	public List<Case> findAll();
	public Page<CaseForList> findByKeyword(Pageable pageable, String serachKey);
	public Case findById(int id);
	public List<OutOfStock> checkStock(int id);
	public void caseEdit(Case cases);
	public int create(Case cases);
	public void logicalDelete(int id);
}