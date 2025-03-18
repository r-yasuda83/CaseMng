package com.example.casemng.model.entity;

import java.util.Date;

import lombok.Data;

@Data
public class Inquiry {

	private int id;
	
	private int caseId;
	
	private Date inquiryDate;
	
	private String contents;
	
	private boolean isDeleted;
	
	private Case cases;
}
