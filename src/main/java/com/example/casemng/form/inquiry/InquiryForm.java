package com.example.casemng.form.inquiry;

import java.util.Date;

import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.example.casemng.form.register.RegisterCaseForm;

import lombok.Data;

@Component
@Data
public class InquiryForm {

	private int id;

	private int caseId;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date inquiryDate;

	private String contents;

	private boolean isDeleted;

	private RegisterCaseForm cases;
}