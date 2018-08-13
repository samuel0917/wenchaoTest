package com.wenchao.supermarket.service.impl;


import com.wenchao.supermarket.annotation.Column;
import com.wenchao.supermarket.annotation.Id;
import com.wenchao.supermarket.annotation.Table;
import com.wenchao.supermarket.db.mapper.BaseMapper;
import com.wenchao.supermarket.exception.ServiceException;
import com.wenchao.supermarket.service.BaseService;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: pf
 * @Date: 2018/2/1 19:58
 * @Description:
 */
@Service(value = "baseService")
public class BaseServiceImpl implements BaseService{
    //分批保存阀值
    private int count = 1000;

    private static final Logger log = LoggerFactory.getLogger(BaseServiceImpl.class);

    @Autowired
    private BaseMapper baseMapper;

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    private Map<String, Object> transformObj(Object t, String type) {
        //获取表名
        if (null == t.getClass().getAnnotation(Table.class)) {
            throw new ServiceException("Error Input Object! Error @Table Annotation.");
        }
        Map<String, Object> re = new HashMap<String, Object>();
        re.put("TABLE_NAME", t.getClass().getAnnotation(Table.class).value());

        Method[] m = t.getClass().getMethods();
        if (null == m || m.length <= 0) {
            throw new ServiceException("Error Input Object! No Method.");
        }
        //insert数据结构
        if ("insert".equals(type)) {
            List k = new ArrayList();//存放列名
            List v = new ArrayList();//存放列值
            for (Method i : m) {
                //获取列名和值
                if (null != i.getAnnotation(Column.class)) {
                    k.add(i.getAnnotation(Column.class).value());
                    v.add(getInvokeValue(t, i));
                    continue;
                }
                if (null != i.getAnnotation(Id.class)) {
                    re.put("KEY_ID", i.getAnnotation(Id.class).value());
                    re.put("KEY_VALUE", getInvokeValue(t, i));
                }
            }
            if (k.size() != v.size()) {
                throw new ServiceException("Error Input Object! Internal Error.");
            }
            re.put("COLUMNS", k);
            re.put("VALUES", v);
        } else if ("update".equals(type)) {
            //update数据结构
            List d = new ArrayList();
            for (Method i : m) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (null != i.getAnnotation(Column.class) && null != getInvokeValue(t, i)) {
                    map.put("COLUMN", i.getAnnotation(Column.class).value());
                    map.put("COL_VALUE", getInvokeValue(t, i));
                    d.add(map);
                    continue;
                }
                if (null != i.getAnnotation(Id.class)) {
                    re.put("KEY_ID", i.getAnnotation(Id.class).value());
                    re.put("KEY_VALUE", getInvokeValue(t, i));
                }
            }
            re.put("DATA", d);
        } else if ("common".equals(type)) {
            //common数据结构
            for (Method i : m) {
                if (null != i.getAnnotation(Id.class)) {
                    re.put("KEY_ID", i.getAnnotation(Id.class).value());
                    re.put("KEY_VALUE", getInvokeValue(t, i));
                }
            }
        }
        return re;
    }

    private Object getInvokeValue(Object t, Method i) {
        try {
            return i.invoke(t, null);
        } catch (IllegalAccessException e) {
            throw new ServiceException("Error Input Object! Error Invoke Get Method.", e);
        } catch (InvocationTargetException e) {
            throw new ServiceException("Error Input Object! Error Invoke Get Method.", e);
        }
    }

    public int insert(Object obj) {
        Map<String, Object> params = transformObj(obj, "insert");
        log.info(new StringBuffer("Function Insert.Transformed Params:").append(params).toString());
        return baseMapper.insert(params);
    }

    public HashMap query(long id, Class c) {
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            params = transformObj(c.newInstance(), "common");
            log.info(new StringBuffer("Function Query.Transformed Params:").append(params).toString());
        } catch (InstantiationException e) {
            throw new ServiceException("Common Query Error.", e);
        } catch (IllegalAccessException e) {
            throw new ServiceException("Common Query Error.", e);
        }
        params.put("KEY_VALUE", id);
        return baseMapper.queryForObject(params);
    }

    public int update(Object obj) {
        Map<String, Object> params = transformObj(obj, "update");
        log.info(new StringBuffer("Function Update.Transformed Params:").append(params).toString());
        return baseMapper.update(params);
    }

    public int delete(Object obj) {
        Map<String, Object> params = transformObj(obj, "common");
        log.info(new StringBuffer("Function Delete.Transformed Params:").append(params).toString());
        return baseMapper.delete(params);
    }

    public int batchInsert(String sqlId, List data) {
        /*通用批量保存，每count条分批commit*/
        if (data.isEmpty() || StringUtils.isEmpty(sqlId)) {
            log.info("批量保存出错，mapperIndex或data为空");
            return 0;
        }
        int result = 0, index = 0;
        SqlSession sqlSession = this.sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH);
        int lastIndex = count;
        try {
            while (index < data.size()) {
                if (data.size() <= lastIndex) {
                    lastIndex = data.size();
                    result = result + sqlSession.insert(sqlId, data.subList(index, lastIndex));
                    sqlSession.commit();
                    log.info(new StringBuilder("已保存[").append(index).append("--").append(lastIndex - 1).append("}条数据").toString());
                    // 批量保存结束，退出循环
                    break;
                } else {
                    result = result + sqlSession.insert(sqlId, data.subList(index, lastIndex));
                    sqlSession.commit();
                    log.info(new StringBuilder("已保存[").append(index).append("--").append(lastIndex - 1).append("}条数据").toString());
                    // 设置下一批
                    index = lastIndex;
                    lastIndex = lastIndex + count;
                }
            }
        } catch (Exception e) {
            if (null != sqlSession) {
                sqlSession.rollback();
            }
            throw new ServiceException(e);
        } finally {
            if (null != sqlSession) {
                sqlSession.close();
            }
        }
        return result;
    }

}
