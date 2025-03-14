package com.example.casemng.service;

import java.util.List;

import com.example.casemng.entity.Inquiry;

public interface InquiryService {

	public List<Inquiry> findAll();
	public Inquiry findById(int id);
	public void inquiryEdit(Inquiry inquiry);
	public int create(Inquiry inquiry);
	public void logicalDelete(int id);
}
