package com.example.casemng.form.order;

import java.util.List;

import com.example.casemng.form.RegisterProductForm;

import lombok.Data;

@Data
public class OrderProductListForm {

	private List<RegisterProductForm> orderProductList;
}
