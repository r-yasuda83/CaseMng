package com.example.casemng.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Order implements Serializable{

	private int id;
	
	private int caseId;
	
	private Date orderDate;
	
	private String memo;
	
	private boolean isDeleted;
	
	private Case cases;
	
	private List<OrderProduct> orderProduct;
}
