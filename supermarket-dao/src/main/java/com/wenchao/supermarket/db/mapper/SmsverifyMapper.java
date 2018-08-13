package com.wenchao.supermarket.db.mapper;

import com.wenchao.supermarket.db.bean.Smsverify;

public interface SmsverifyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Smsverify record);

    int insertSelective(Smsverify record);

    Smsverify selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Smsverify record);

    int updateByPrimaryKey(Smsverify record);
}