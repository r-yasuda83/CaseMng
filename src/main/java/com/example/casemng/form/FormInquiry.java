package com.example.casemng.form;

import java.util.Date;

import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class FormInquiry {

	private int id;

	private int caseId;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date inquiryDate;

	private String contents;

	private boolean isDeleted;

	private FormCase cases;
}