package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.entity.HeadLine;

public interface HeadLineService {
	public static final String HEADLINEKEY = "headline";
	/**
	 * 获取头条列表
	 * @param headLineCondition
	 * @return
	 */
	List<HeadLine> getHeadLineList(HeadLine headLineCondition);
}
