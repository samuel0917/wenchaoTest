package com.wenchao.supermarket.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wenchao.supermarket.db.bean.Demo;
import com.wenchao.supermarket.db.mapper.DemoMapper;
import com.wenchao.supermarket.service.BaseService;
import com.wenchao.supermarket.service.DemoService;

import java.util.List;

@Service(value = "demoServie")
public class DemoServiceImpl implements DemoService {
    @Autowired
    private DemoMapper demoMapper;
    
    @Autowired
    BaseService baseService;

    public List<Demo> getDemos() {
        return demoMapper.getDemos();
    }

	public boolean addDemo(Demo info) {
		if(1 == baseService.insert(info)){
            return true;
        } else {
            return false;
        }
	}
}
