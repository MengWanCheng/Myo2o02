package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;

public class PersonInfoDaoTest extends BaseTest{
	
	@Autowired
	private PersonInfoDao personInfoDao;
	
	@Test
	public void testAInsertPersonInfo() {
		PersonInfo personInfo = new PersonInfo();
		LocalAuth localAuth = new LocalAuth();
//		personInfo.setUserId(2L);
		personInfo.setName("白浪");
		personInfo.setProfileImg("test");
		personInfo.setCreateTime(new Date());
		personInfo.setEnableStatus(1);
		personInfo.setEmail("test");
		personInfo.setGender("1");
		personInfo.setUserType(2);
		int effectedNum = personInfoDao.insertPersonInfo(personInfo);
		System.out.println(personInfo.getUserId());
		assertEquals(1, effectedNum);
	}
}
