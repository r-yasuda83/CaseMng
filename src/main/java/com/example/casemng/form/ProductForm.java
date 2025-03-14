package com.example.casemng.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.Data;

@Data
public class ProductForm {

	private int id;

	@NotBlank
	private String productName;
	
	@NotNull
	@PositiveOrZero
	private Integer price;

	@NotNull
	@PositiveOrZero
	private Integer stock;

	private boolean choose;
}
