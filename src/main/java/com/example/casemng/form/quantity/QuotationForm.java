package com.example.casemng.form.quantity;

import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.example.casemng.form.RegisterProductForm;

import lombok.Data;

@Component
@Data
public class QuotationForm {

	private int id;

	private int caseId;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date quotationDate;

	private String memo;

	private boolean isDeleted;
	
	private QuantityCaseForm cases;
	@Valid
	private List<RegisterProductForm> quotationProduct;
}
