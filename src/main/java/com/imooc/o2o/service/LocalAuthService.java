package com.imooc.o2o.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;

public interface LocalAuthService {
	/**
	 * 根据用户名和用户密码获取LocalAuth信息
	 * @param userName
	 * @param password
	 * @return
	 */
	LocalAuth getLocalAuthByUserNameAndPwd(String userName, String password);
	/**
	 * 根据用户Id获取LocalAuth信息
	 * @param userId
	 * @return
	 */
	LocalAuth getLocalAuthByUserId(long userId);
	/**
	 * 绑定账号
	 * @param localAuth
	 * @return
	 */
	LocalAuthExecution bindLocalAuth(LocalAuth localAuth);
	/**
	 * 更新LocalAuth信息
	 * @param userId
	 * @param userName
	 * @param password
	 * @param newPassword
	 * @return
	 */
	LocalAuthExecution modifyLocalAuth(Long userId, String userName, String password, String newPassword);
	/**
	 * 注册账号
	 * @param localAuth
	 * @param profileImg
	 * @return
	 */
	LocalAuthExecution register(LocalAuth localAuth, ImageHolder profileImg);
}
