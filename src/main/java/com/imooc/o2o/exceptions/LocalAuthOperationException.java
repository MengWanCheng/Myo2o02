package com.imooc.o2o.exceptions;

public class LocalAuthOperationException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8260236137099919700L;

	//构造函数
	public LocalAuthOperationException(String msg) {
		super(msg);
	}
}
