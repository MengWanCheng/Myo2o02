package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.util.MD5;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthDaoTest extends BaseTest{

	@Autowired
	private LocalAuthDao localAuthDao;
	private static final String username = "testusername";
	private static final String password = "s06l5bl5s05602ly2syls6556q56sb6e";
	private static final String newPassword = "s06l5bl5s05602ly2syls6556q56sb6e";
	
	@Test
	@Ignore
	public void testAQueryLocalAuthByUserNameAndPwd() {
		String userName = "张三";
		String password = "zhangsan";
		LocalAuth localAuth = localAuthDao.queryLocalAuthByUserNameAndPwd(userName, password);
		System.out.println(localAuth.getUserName());
	}
	
	@Test
	@Ignore
	public void testBInsertLocalAuth() {
		LocalAuth localAuth = new LocalAuth();
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(1L);
		localAuth.setPersonInfo(personInfo);
		localAuth.setUserId(1L);
		localAuth.setUserName(username);
		localAuth.setPassword(password);
		localAuth.setCreateTime(new Date());
		int effectedNum = localAuthDao.insertLocalAuth(localAuth);
		assertEquals(1, effectedNum);
	}
	
	@Test
	@Ignore
	public void testDQueryLocalAuthByUserNameAndPwd() {
		LocalAuth localAuth = localAuthDao.queryLocalAuthByUserNameAndPwd(username, password);
		assertEquals("张三", localAuth.getPersonInfo().getName());
	}
	
	@Test
	@Ignore
	public void testEQueryLocalAuthByUserId() {
		LocalAuth localAuth = localAuthDao.queryLocalAuthByUserId(1L);
		assertEquals("张三", localAuth.getPersonInfo().getName());
	}
	
	@Test
	
	public void testFUpdateLocalAuth() {
		Date now = new Date();
		int effectedNum = localAuthDao.updateLocalAuth(1L, username, password, newPassword, now);
		assertEquals(1, effectedNum);
		LocalAuth localAuth = localAuthDao.queryLocalAuthByUserId(1L);
		System.out.println(localAuth.getPassword() );
	}
}
