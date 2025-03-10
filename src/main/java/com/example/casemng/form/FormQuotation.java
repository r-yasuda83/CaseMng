package com.example.casemng.form;

import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class FormQuotation {

	private int id;

	private int caseId;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date quotationDate;

	private String memo;

	private boolean isDeleted;
	
	private FormCase cases;
	@Valid
	private List<FormQuotationProduct> quotationProduct;
}
