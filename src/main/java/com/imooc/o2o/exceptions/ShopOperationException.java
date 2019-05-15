package com.imooc.o2o.exceptions;

public class ShopOperationException extends RuntimeException{
	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 2361446884822298905L;

	//构造函数
	public ShopOperationException(String msg) {
		super(msg);
	}
}
