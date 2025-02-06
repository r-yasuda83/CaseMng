package com.example.casemng.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.Data;

@Data
public class FormProduct {

	private int id;

	@NotBlank
	private String productName;

	private int productCategoryId;
	
	@NotNull
	@PositiveOrZero
	private Integer price;

	@NotNull
	@PositiveOrZero
	private Integer stock;

	private boolean choose;
}
