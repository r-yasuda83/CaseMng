package com.example.casemng.model;

import com.example.casemng.model.entity.Product;

import lombok.Data;

@Data
public class OutOfStock {

	Product product;
	
	int registedQuantity;
}
