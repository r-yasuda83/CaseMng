package com.example.casemng.form.cases;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CaseForm {

	private int id;
	
	@NotBlank
	private String caseName;
	
	private int customerId;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date caseDate;
	
	private int shippingStatus;
	
	private String memo;
	
	private boolean shippingStockFlg;
	
	private CaseOrderForm order;
}