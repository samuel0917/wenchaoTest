package com.wenchao.supermarket.service;


import java.util.List;

import com.wenchao.supermarket.db.bean.Demo;

public interface DemoService {
    public List<Demo> getDemos();
    
    public boolean addDemo(Demo info);
}
