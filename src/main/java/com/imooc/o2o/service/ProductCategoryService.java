package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;

//店铺商品类别接口(Service层)
public interface ProductCategoryService {
	/**
	 * 获取商品类别列表
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> getProductCategory(Long shopId);
	/**
	 * 批量插入商品类别
	 * @param productCategoryList
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution addProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException;
	/**
	 * 需要先将商品目录下的商品的类别Id置为空，然后再删除该商品目录，需要用到事务
	 * 删除商品类别
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException;
}
