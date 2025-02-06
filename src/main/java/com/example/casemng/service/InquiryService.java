package com.example.casemng.service;

import java.util.List;

import com.example.casemng.entity.Inquiry;
import com.example.casemng.form.FormInquiry;

public interface InquiryService {

	public List<Inquiry> findAll();
	public FormInquiry findById(int id);
	public void inquiryEdit(FormInquiry form);
	public int create(FormInquiry form);
	public void logicalDelete(int id);
}
