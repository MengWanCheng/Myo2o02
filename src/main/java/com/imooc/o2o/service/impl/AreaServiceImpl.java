package com.imooc.o2o.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.cache.JedisUtil;
import com.imooc.o2o.dao.AreaDao;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.exceptions.AreaOperationException;
import com.imooc.o2o.service.AreaService;

@Service
public class AreaServiceImpl implements AreaService{

	@Autowired
	private AreaDao areaDao;
	@Autowired
	private JedisUtil.Strings jedisStrings;
	@Autowired
	private JedisUtil.Keys jedisKeys;
	
	private static String AREALISTKEY = "arealist";
	private static Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);
	@Override
	public List<Area> getAreaList() {
		//定义redis中的key
		String key = AREALISTKEY;
		//定义接受对象
		List<Area> areaList = null;
		//定义jackson数据转换操作类
		ObjectMapper mapper = new ObjectMapper();
		//判断key是否存在
		if(!jedisKeys.exists(key)) {
			//如果不存在，则从数据库里取出相应数据
			areaList = areaDao.queryArea();
			// 将相关的实体类集合转换成string,存入redis里面对应的key中
			String jsonString;
			try {
				jsonString = mapper.writeValueAsString(areaList);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			}
			//存入redis里面对应的key中
			jedisStrings.set(key, jsonString);
		}else {
			// 若存在，则直接从redis里面取出相应数据
			String jsonString = jedisStrings.get(key);
			//将string转换成的集合类型
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
			// 将相关key对应的value里的的string转换成对象的实体类集合
			try {
				areaList = mapper.readValue(jsonString, javaType);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			}
		}
		
		return areaList;
	}
	
}
