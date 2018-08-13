package com.wenchao.supermarket;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.wenchao.supermarket.db.bean.Demo;
import com.wenchao.supermarket.db.bean.Smsverify;
import com.wenchao.supermarket.db.mapper.DemoMapper;
import com.wenchao.supermarket.db.mapper.SmsverifyMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootMyServiceApplicationTests {

	@Autowired
	SmsverifyMapper smsDao;
	@Autowired 
	DemoMapper demoDao;
	
	@Test
	public void contextLoads() {
		Smsverify sms=	smsDao.selectByPrimaryKey(Long.valueOf(1));
		System.out.println(JSON.toJSONString(sms));
		System.out.println("111111111111111111111");
	}

	
	@Test
	public void getDemos() {
		List<Demo> sms=	demoDao.getDemos();
		System.out.println(JSON.toJSONString(sms));
		System.out.println("222222222222222222");
		
	}
}
