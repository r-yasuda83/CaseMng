package com.example.casemng.form.orderform;

import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

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

	private OrderCaseForm cases;
	@Valid
	private List<OrderProductForm> orderProduct;
}