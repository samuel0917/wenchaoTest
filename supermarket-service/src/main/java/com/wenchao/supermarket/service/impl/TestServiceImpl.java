package com.wenchao.supermarket.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wenchao.supermarket.db.bean.Smsverify;
import com.wenchao.supermarket.db.mapper.SmsverifyMapper;
import com.wenchao.supermarket.service.TestService;

@Service
public class TestServiceImpl implements TestService{

	@Autowired
	SmsverifyMapper smsDao;
	
	public Smsverify selectInfo(){
		return smsDao.selectByPrimaryKey(Long.valueOf(1));
	}
}
