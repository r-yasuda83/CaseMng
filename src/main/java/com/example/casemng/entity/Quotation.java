package com.example.casemng.entity;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Quotation {

	private int id;
	
	private int caseId;
	
	private Date quotationDate;
	
	private String memo;
	
	private boolean isDeleted;
	
	private Case cases;
	
	private List<QuotationProduct> quotationProduct;
}
