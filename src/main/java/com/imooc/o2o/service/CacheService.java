package com.imooc.o2o.service;

public interface CacheService {
	/**
	 * 根据缓存的前缀清理匹配的全部缓存
	 * @param keyPrefix
	 */
	void removeFromCache(String keyPrefix);
}
