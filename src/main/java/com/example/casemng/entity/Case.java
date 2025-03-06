package com.example.casemng.entity;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Case {

	private int id;

	private String caseName;

	private int customerId;

	private Date caseDate;

	private int shippingStatus;

	private String memo;
	
	private boolean shippingStockFlg;

	private boolean isDeleted;
	
	private Customer customer;
	
	private Order order;
	
	private List<Quotation> quotation;
	
	private List<Inquiry> inquiry;
}