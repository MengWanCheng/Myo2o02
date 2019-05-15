package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.PersonInfo;

public interface PersonInfoDao {

	/**
	 * 分页查询PersonInfo列表
	 * @param personInfoCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<PersonInfo> queryPersonInfoList(
			@Param("personInfoCondition") PersonInfo personInfoCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	/**
	 * 查询PersonInfo总数
	 * @param personInfoCondition
	 * @return
	 */
	int queryPersonInfoCount(
			@Param("personInfoCondition") PersonInfo personInfoCondition);

	/**
	 * 根据userId查询PersonInfo信息
	 * @param userId
	 * @return
	 */
	PersonInfo queryPersonInfoById(long userId);

	/**
	 * 插入一条PersonInfo信息
	 * @param wechatAuth
	 * @return
	 */
	int insertPersonInfo(PersonInfo personInfo);

	/**
	 * 更新PersonInfo信息
	 * @param wechatAuth
	 * @return
	 */
	int updatePersonInfo(PersonInfo personInfo);

	/**
	 * 删除PersonInfo信息
	 * @param wechatAuth
	 * @return
	 */
	int deletePersonInfo(long userId);
}
