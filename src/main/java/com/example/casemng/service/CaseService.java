package com.example.casemng.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.casemng.entity.Case;
import com.example.casemng.entity.CaseForList;

public interface CaseService {

	public Page<Case> findAll(Pageable pageable);

	public Page<CaseForList> findByKeyword(Pageable pageable, String serachKey);

	public Case findById(int id);

	public BindingResult caseEdit(Case cases, BindingResult result);

	public int create(Case cases);

	public void logicalDelete(int id);

	public String pagenation(String searchKey, Integer displayedNum, String sortKey, String sortDirection, Pageable pageable, Model model);
}