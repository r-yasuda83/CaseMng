package com.example.casemng.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.entity.Inquiry;
import com.example.casemng.form.FormInquiry;
import com.example.casemng.repository.InquiryMapper;

@Service
public class InquiryServiceImpl implements InquiryService{
	
	@Autowired
	InquiryMapper mapper;
	
	@Autowired
	ModelMapper modelMapper;

	public List<Inquiry> findAll(){
		return mapper.findAll();
	}
	
	public FormInquiry findById(int id) {
		Inquiry inqu = mapper.findById(id);
		FormInquiry form = modelMapper.map(inqu, FormInquiry.class);
		return form;
	}
	
	@Transactional
	public void inquiryEdit(FormInquiry form) {
		Inquiry inquiry = modelMapper.map(form, Inquiry.class);
		mapper.inquiryEdit(inquiry);
	}
	
	@Transactional
	public int create(FormInquiry form) {
		Inquiry inquiry = modelMapper.map(form, Inquiry.class);
		mapper.create(inquiry);
		return inquiry.getId();
	}
	
	public void logicalDelete(int id) {
		mapper.logicalDelete(id);
	}
}
