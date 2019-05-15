package com.imooc.o2o.exceptions;

public class ProductOperationException extends RuntimeException{
	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 5076172298827469013L;

	//构造函数
	public ProductOperationException(String msg) {
		super(msg);
	}
}
