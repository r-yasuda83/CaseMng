package com.example.casemng.service;

import java.util.List;

import com.example.casemng.entity.Case;
import com.example.casemng.form.FormCase;
import com.example.casemng.form.FormCaseEntry;

public interface CaseService {

	public List<Case> findAll();
	public FormCase findById(int id);
	public void caseEdit(FormCase form);
	public int create(FormCaseEntry form);
	public void logicalDelete(int id);
}
