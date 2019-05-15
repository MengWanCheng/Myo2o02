package com.imooc.o2o.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.LocalAuth;

public interface LocalAuthDao {

	/**
	 * 通过用户名＆密码查询本地账户
	 * @param userName
	 * @param password
	 * @return
	 */
	LocalAuth queryLocalAuthByUserNameAndPwd(@Param("userName") String userName, 
			@Param("password") String password);
	/**
	 * 通过用户Id查询本地账户
	 * @param userId
	 * @return
	 */
	LocalAuth queryLocalAuthByUserId(@Param("userId") long userId);
	/**
	 * 插入一条本地账户
	 * @param localAuth
	 * @return
	 */
	int insertLocalAuth(LocalAuth localAuth);
	/**
	 * 更新密码
	 * @param userId
	 * @param userName
	 * @param password
	 * @param newPassword
	 * @param lastEditTime
	 * @return
	 */
	int updateLocalAuth(@Param("userId") long userId, @Param("userName") String userName,
			@Param("password") String password, @Param("newPassword") String newPassword,
			@Param("lastEditTime") Date lastEditTime);
}
