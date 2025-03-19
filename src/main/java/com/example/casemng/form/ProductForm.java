package com.example.casemng.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
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
