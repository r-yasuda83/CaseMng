package com.example.casemng.form;

import lombok.Data;

@Data
public class FormSearch {
	
	private String keyword;
	
	private Integer DisplayedNum;
	
	private String sortKey;
	
	private String sortDirection;
}
