package com.example.casemng.form.cases;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class CaseEntryForm {

	private int customerId;

	@NotBlank
	private String caseName;

	private Date caseDate;

	private String memo;
}
