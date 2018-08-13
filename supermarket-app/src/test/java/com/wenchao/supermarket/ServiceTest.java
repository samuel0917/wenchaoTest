package com.wenchao.supermarket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.wenchao.supermarket.db.bean.Smsverify;
import com.wenchao.supermarket.service.TestService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

	
	@Autowired
	TestService testService;
	
	
	@Test
	public void test() {
		Smsverify sms=testService.selectInfo();
		System.out.println(JSON.toJSONString(sms));
		System.out.println("111111111111111111111");
	}

}
