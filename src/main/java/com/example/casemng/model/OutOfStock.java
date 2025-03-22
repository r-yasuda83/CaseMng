package com.example.casemng.model;

import lombok.Data;

@Data
public class OutOfStock {

	String productName;
	
	int stock;
	
	int registedQuantity;
}
