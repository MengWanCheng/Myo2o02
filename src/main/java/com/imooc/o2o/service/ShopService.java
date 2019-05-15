package com.imooc.o2o.service;

import java.io.InputStream;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;

public interface ShopService {
	/**
	 * 根据shopCondition分页返回店铺列表
	 * @param shopCondition 
	 * @param pageIndex 页面索引
	 * @param pageSize 页面大小
	 * @return
	 */
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);
	/**
	 * 根据店铺ID查询店铺
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(long shopId);
	/**
	 * 修改店铺
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 */
	ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;
	/**
	 * 添加店铺
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 */
	ShopExecution addShop(Shop shop, ImageHolder thumbnail);
}
