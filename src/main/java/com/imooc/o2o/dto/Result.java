package com.imooc.o2o.dto;

/**
 * 一个泛型类，用于封装json对象，所有返回结果都使用它
 * 
 * @author Administrator
 *
 * @param <T>
 */
public class Result<T> {
	// 是否成功标志
	private boolean success;
	// 成功时返回的数据
	private T data;
	// 错误信息
	private String errMsg;
	// 错误码
	private int errCode;

	// 无参构造器
	public Result() {

	}

	// 成功时的构造器
	public Result(boolean success, T data) {
		this.success = success;
		this.data = data;
	}

	// 失败时的构造器
	public Result(boolean success, String errMsg, int errCode) {
		this.success = success;
		this.errMsg = errMsg;
		this.errCode = errCode;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}
}
