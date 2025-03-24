package com.example.casemng.form.cases;

import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.casemng.form.register.RegisterProductForm;

import lombok.Data;

@Data
public class CaseOrderForm {

	private int id;

	private int caseId;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date orderDate;

	private String memo;

	private boolean isDeleted;
	
	@Valid
	private List<RegisterProductForm> orderProduct;
}
