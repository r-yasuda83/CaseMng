package com.example.casemng.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.entity.Inquiry;
import com.example.casemng.repository.InquiryMapper;
import com.example.casemng.service.InquiryService;

@Service
public class InquiryServiceImpl implements InquiryService{
	
	@Autowired
	InquiryMapper mapper;
	
	@Autowired
	ModelMapper modelMapper;

	public List<Inquiry> findAll(){
		return mapper.findAll();
	}
	
	public Inquiry findById(int id) {
		Inquiry inqu = mapper.findById(id);
		return inqu;
	}
	
	@Transactional
	public void inquiryEdit(Inquiry inquiry) {
		mapper.inquiryEdit(inquiry);
	}
	
	@Transactional
	public int create(Inquiry inquiry) {
		mapper.create(inquiry);
		return inquiry.getId();
	}
	
	public void logicalDelete(int id) {
		mapper.logicalDelete(id);
	}
}
