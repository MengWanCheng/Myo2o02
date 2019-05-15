package com.imooc.o2o.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.cache.JedisUtil;
import com.imooc.o2o.dao.HeadLineDao;
import com.imooc.o2o.entity.HeadLine;
import com.imooc.o2o.exceptions.HeadLineOperationException;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.HeadLineService;

@Service
public class HeadLineServiceImpl implements HeadLineService{
	private static final Logger logger = LoggerFactory.getLogger(HeadLineServiceImpl.class);
	@Autowired
	private HeadLineDao headLineDao;
	@Autowired
	private JedisUtil.Strings jedisStrings;
	@Autowired
	private JedisUtil.Keys jedisKeys;
	
	@Override
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition) {
		List<HeadLine> headLineList = new ArrayList<HeadLine>();
		//定义Key
		String key = "HEADLINEKEY";
		//定义jackson数据转换操作类
		ObjectMapper mapper = new ObjectMapper();
		 // 根据mapper中的查询条件 拼装key
        // 根据不同的条件缓存不同的key值 这里有3种缓存 headline_0 headline_1 和 headline 方便管理员权限操作
		if(headLineCondition != null && headLineCondition.getEnableStatus() != null) {
			key = key + "_" + headLineCondition.getEnableStatus();
		}
		//判断key是否存在于redis中
		if(!jedisKeys.exists(key)) {
			//如果redis中不存在key，那就去数据库中取，然后生成String
			headLineList = headLineDao.queryHeadLine(headLineCondition);
			try {
				String jsonString = mapper.writeValueAsString(headLineList);
				jedisStrings.set(key, jsonString);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			}
		}else {
			//如果redis中存在此key，那就从redis中取key相对应的信息
			//首先取出key对应的信息
			String jsonString = jedisStrings.get(key);
			//然后指定一个要将string转换成的集合类型为ArrayList
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
			//最后将其转换为java对象的实体类集合
			try {
				headLineList = mapper.readValue(jsonString, javaType);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			}
		}
		return headLineList;
	}

	
}
