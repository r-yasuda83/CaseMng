package com.example.casemng.form;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class FormCaseEntry {

	private int customerId;

	@NotBlank
	private String caseName;

	private Date caseDate;

	private String memo;
}
