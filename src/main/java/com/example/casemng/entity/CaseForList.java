package com.example.casemng.entity;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class CaseForList {

	private int id;

	private String caseName;

	private int customerId;

	private Date caseDate;

	private String status;
	
	private boolean shippingStockFlg;
	
	private Customer customer;
	
	private Order order;
	
	private List<Quotation> quotation;
	
	private List<Inquiry> inquiry;
	
	private int orderStatus;
}
