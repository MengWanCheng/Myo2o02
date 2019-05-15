package com.imooc.o2o.service;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.LocalAuthStateEnum;
import com.imooc.o2o.util.MD5;

public class LocalAuthServiceTest extends BaseTest{

	@Autowired
	private LocalAuthService localAuthService;
	
	@Test
	@Ignore
	public void testABindlocalAuth() {
		LocalAuth localAuth = new LocalAuth();
		PersonInfo personInfo = new PersonInfo();
		String userName = "testusername";
		String password = "testpassword";
		personInfo.setUserId(1L);
		localAuth.setUserId(1L);
		localAuth.setPersonInfo(personInfo);
		localAuth.setUserName(userName);
		localAuth.setPassword(password);
		LocalAuthExecution lae = localAuthService.bindLocalAuth(localAuth);
		assertEquals(LocalAuthStateEnum.SUCCESS.getState(), lae.getState());
		localAuth = localAuthService.getLocalAuthByUserId(personInfo.getUserId());
		System.out.println("用户昵称：" + localAuth.getPersonInfo().getName());
		System.out.println("平台账号密码：" + localAuth.getPassword());
	}
	
	@Test
//	@Transactional
	public void testBModifyLocalAuth() {
		long userId = 1L;
		String userName = "testusername";
		String password = "testpassword";
		String newPassword = "testpasswordnew";
		LocalAuthExecution lae = localAuthService.modifyLocalAuth(userId, userName, password, newPassword);
		assertEquals(LocalAuthStateEnum.SUCCESS.getState(), lae.getState());
		LocalAuth localAuth = localAuthService.getLocalAuthByUserNameAndPwd(userName, MD5.getMd5(newPassword));
		System.out.println(localAuth.getPersonInfo().getName());
	}
}
