package com.example.casemng.form.order;

import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.example.casemng.form.register.RegisterCaseForm;
import com.example.casemng.form.register.RegisterProductForm;

import lombok.Data;

@Component
@Data
public class OrderForm {

	private int id;

	private int caseId;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date orderDate;

	private String memo;

	private boolean isDeleted;

	private RegisterCaseForm cases;
	@Valid
	private List<RegisterProductForm> orderProduct;
}