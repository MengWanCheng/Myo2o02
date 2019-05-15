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
import com.imooc.o2o.dao.ShopCategoryDao;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.exceptions.ShopCategoryOperationException;
import com.imooc.o2o.service.ShopCategoryService;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
	private static final Logger logger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);
	@Autowired
	private ShopCategoryDao shopCategoryDao;
	@Autowired
	private JedisUtil.Strings jedisStrings;
	@Autowired
	private JedisUtil.Keys jedisKeys;

	@Override
	public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		// 定义key
		String key = "SHOPCATEGORYLIST";
		// 定义jackson数据转换操作类
		ObjectMapper mapper = new ObjectMapper();
		// 根据mapper中的查询条件，拼装shopcategory的key
		if(shopCategoryCondition==null) {
			//如果查询条件为空，那就返回所有parentId为空的一级类别
			key = key + "_allfirstlevel";
		}else if(shopCategoryCondition != null && shopCategoryCondition.getParent() != null
				&& shopCategoryCondition.getParent().getShopCategoryId() != null) {
			//如果查询条件不为空且它有parentId，则返回该parentId下的所有子类别
			key = key + "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
		}else if(shopCategoryCondition != null) {
			//如果查询条件不为空，咋返回所有子类别
			key = key + "_allsecondlevel";
		}
		
		//判断Redis中是否有key
		if(!jedisKeys.exists(key)) {
			//如果redis没有key，则从数据库中获取
			shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
			//将相关的实体类集合转换成string
			try {
				//将相关的实体类集合转换成string
				String jsonString = mapper.writeValueAsString(shopCategoryList);
				//然后，设置进redis的key中
				jedisStrings.set(key, jsonString);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}
		}else {
			//如果redis中有key，则从redis中获取
			String jsonString = jedisStrings.get(key);
			//指定要将string转换成哪种集合类型
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
			
			try {
				//将与key相关的string转换为java实体类集合
				shopCategoryList = mapper.readValue(jsonString, javaType);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}
		}
		return shopCategoryList;
	}

}
