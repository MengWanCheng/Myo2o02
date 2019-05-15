package com.imooc.o2o.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.LocalAuthDao;
import com.imooc.o2o.dao.PersonInfoDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.LocalAuthStateEnum;
import com.imooc.o2o.exceptions.LocalAuthOperationException;
import com.imooc.o2o.exceptions.PersonInfoOperationException;
import com.imooc.o2o.service.LocalAuthService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.MD5;
import com.imooc.o2o.util.PathUtil;

@Service
public class LocalAuthServiceImpl implements LocalAuthService{

	@Autowired
	private LocalAuthDao localAuthDao;
	@Autowired
	private PersonInfoDao personInfoDao;

	@Override
	public LocalAuth getLocalAuthByUserNameAndPwd(String userName, String password) {
		// TODO Auto-generated method stub
		return localAuthDao.queryLocalAuthByUserNameAndPwd(userName, password);
	}

	@Override
	public LocalAuth getLocalAuthByUserId(long userId) {
		// TODO Auto-generated method stub
		return localAuthDao.queryLocalAuthByUserId(userId);
	}

	@Override
	public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) {
		// TODO Auto-generated method stub
		if(localAuth == null || localAuth.getPassword() == null
				|| localAuth.getUserName() == null || localAuth.getUserId() == null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		LocalAuth tempAuth = localAuthDao.queryLocalAuthByUserId(localAuth.getPersonInfo().getUserId());
		//如果此用户已绑定过平台账号，则退出，以保证账号的唯一性
		if(tempAuth != null) {
			return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
		}
		try {
			//如果用户没有绑定过账号，则进行绑定
			localAuth.setCreateTime(new Date());
			localAuth.setLastEditTime(new Date());
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			int effectedNum = localAuthDao.insertLocalAuth(localAuth);
			if (effectedNum <= 0) {
				throw new LocalAuthOperationException("账号绑定失败");
			} else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
			} 
		} catch (Exception e) {
			throw new LocalAuthOperationException("insert LocalAuth error: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public LocalAuthExecution modifyLocalAuth(Long userId, String userName, String password, String newPassword) {
		// 
		if(userId != null && userName != null && password != null && newPassword != null 
				&& !password.equals(newPassword)) {
			try {
				int effectedNum = localAuthDao.updateLocalAuth(userId, userName, MD5.getMd5(password),
						MD5.getMd5(newPassword), new Date());
				if (effectedNum <= 0) {
					throw new LocalAuthOperationException("更新密码失败");
				} else {
					return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
				} 
			} catch (Exception e) {
				throw new LocalAuthOperationException("更新密码失败：" + e.getMessage());
			}
		}else {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
	}
 
	@Override
	@Transactional
	public LocalAuthExecution register(LocalAuth localAuth, ImageHolder profileImg) {
		if(localAuth == null || localAuth.getUserName() == null 
				|| localAuth.getPassword() == null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		try {
			//s设置基本信息
			localAuth.setCreateTime(new Date());
			localAuth.setLastEditTime(new Date());
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			
			//判断PersonInfo和userId是否为空
			if (localAuth.getPersonInfo() != null) {
				//判断PersonInfo中profileImg是否为空
				if (profileImg != null) {
					localAuth.getPersonInfo().setCreateTime(new Date());
					localAuth.getPersonInfo().setLastEditTime(new Date());
					localAuth.getPersonInfo().setEnableStatus(1);
					localAuth.getPersonInfo().setGender("1");
					localAuth.getPersonInfo().setUserType(2);
					try {
						addProfileImg(localAuth, profileImg);
					} catch (Exception e) {
						throw new PersonInfoOperationException("addProfileImg error" + e.getMessage());
					}
					try {
						PersonInfo personInfo = localAuth.getPersonInfo();
						int effectedNum = personInfoDao.insertPersonInfo(personInfo);
						localAuth.setUserId(personInfo.getUserId());
						if (effectedNum <= 0) {
							throw new PersonInfoOperationException("添加用户信息失败");
						}
					} catch (Exception e) {
						throw new PersonInfoOperationException("insert PersonInfo error: " + e.getMessage());
					}
				}
			} 
			//将添加的用户信息插入到本地账户中
			int effectedNum = localAuthDao.insertLocalAuth(localAuth);
			if (effectedNum <= 0) {
				throw new LocalAuthOperationException("账号创建失败");
			} else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
			}
		} catch (Exception e) {
			throw new LocalAuthOperationException("insert LocalAuth error: " + e.getMessage());
		}
	}

	private void addProfileImg(LocalAuth localAuth, ImageHolder profileImg) {
		String dest = PathUtil.getPersonInfoImagePath();
		String profileImgAddr = ImageUtil.generateThumbnails(profileImg, dest);
		localAuth.getPersonInfo().setProfileImg(profileImgAddr);
	}
}
