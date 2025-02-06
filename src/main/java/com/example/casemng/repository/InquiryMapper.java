package com.example.casemng.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.casemng.entity.Inquiry;

@Mapper
public interface InquiryMapper {

	public List<Inquiry> findAll();
	public Inquiry findById(int id);
	public void inquiryEdit(Inquiry inquiry);
	public void create(Inquiry inquiry);
	public void logicalDelete(int id);
}
