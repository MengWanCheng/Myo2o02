package com.imooc.o2o.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.o2o.cache.JedisUtil;
import com.imooc.o2o.service.CacheService;

@Service
public class CacheServiceImpl implements CacheService{

	@Autowired
	private CacheService cacheService;
	@Autowired
	private JedisUtil.Keys jedisKeys;
	@Override
	public void removeFromCache(String keyPrefix) {
		//获取redis中有keyPrefix的key放入Set，然后遍历进行删除
		Set<String> keySet = jedisKeys.keys(keyPrefix);
		for (String key : keySet) {
			jedisKeys.del(key);
		}
	}

	
}
