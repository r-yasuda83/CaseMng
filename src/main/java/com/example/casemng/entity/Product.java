package com.example.casemng.entity;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class Product {

	private int id;
	
	@NotBlank
	private String productName;
	
	private int price;
	
	private int stock;
	
	private boolean choose;
}