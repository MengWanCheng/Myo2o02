package com.imooc.o2o.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.HeadLine;

public class HeadLineDaoTest extends BaseTest{
	@Autowired
	private HeadLineDao headLineDao;
	
	@Test
	public void testAQueryHeadLine() {
		List<HeadLine> headLineList = headLineDao.queryHeadLine(new HeadLine());
		System.out.println(headLineList.size());
		
	}
}
