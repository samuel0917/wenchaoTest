package com.wenchao.supermarket.exception;

/**
 * dao层异常基类
 * 
 * @author Li Ji
 *
 */
public class DaoException extends BaseException { 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1569750247319692464L;

	 /**
     * 数据库操作,insert返回0
     */
    public static final DaoException DB_INSERT_RESULT_0 = new DaoException(
            10040001, "数据库操作,insert返回0");

    /**
     * 数据库操作,update返回0
     */
    public static final DaoException DB_UPDATE_RESULT_0 = new DaoException(
            10040002, "数据库操作,update返回0");

    /**
     * 数据库操作,selectOne返回null
     */
    public static final DaoException DB_SELECTONE_IS_NULL = new DaoException(
            10040003, "数据库操作,selectOne返回null");

    /**
     * 数据库操作,list返回null
     */
    public static final DaoException DB_LIST_IS_NULL = new DaoException(
            10040004, "数据库操作,list返回null");

    /**
     * Token 验证不通过
     */
    public static final DaoException TOKEN_IS_ILLICIT = new DaoException(
            10040005, "Token 验证非法");
    /**
     * 会话超时　获取session时，如果是空，throws 下面这个异常 拦截器会拦截爆会话超时页面
     */
    public static final DaoException SESSION_IS_OUT_TIME = new DaoException(
            10040006, "会话超时");

    /**
     * 生成序列异常时
     */
    public static final DaoException DB_GET_SEQ_NEXT_VALUE_ERROR = new DaoException(
            10040007, "序列生成超时");
	
	public DaoException(int ret) {
		super(ret);
	}

	public DaoException(int ret, Throwable throwable) {
		super(ret, throwable);
	}

	
	public DaoException(int ret, String messageArgs) {
		super(ret, messageArgs);
	}
	public DaoException(Throwable throwable) {
		super(throwable);
	}
	
	
}
