package com.imooc.o2o.exceptions;

public class ProductCategoryOperationException extends RuntimeException{
	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1182563719599527969L;

	//构造函数
	public ProductCategoryOperationException(String msg) {
		super(msg);
	}
}
