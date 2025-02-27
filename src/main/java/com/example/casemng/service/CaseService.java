package com.example.casemng.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import com.example.casemng.entity.Case;
import com.example.casemng.entity.CaseForList;
import com.example.casemng.form.FormCase;
import com.example.casemng.form.FormCaseEntry;

public interface CaseService {

	public Page<Case> findAll(Pageable pageable);

	public Page<CaseForList> findByKeyword(Pageable pageable, String serachKey);

	public FormCase findById(int id);

	public void caseEdit(FormCase form);

	public int create(FormCaseEntry form);

	public void logicalDelete(int id);

	public String pagenation(String searchKey, Integer displayedNum, String sortKey, String sortDirection, Pageable pageable, Model model);
}