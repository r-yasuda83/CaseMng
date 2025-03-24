package com.example.casemng.form.cases;

import jakarta.validation.Valid;

import com.example.casemng.form.register.RegisterCaseForm;

import lombok.Data;

@Data
public class CaseForm {
	
	@Valid
	private RegisterCaseForm cases;
	
	private CaseOrderForm order;
}