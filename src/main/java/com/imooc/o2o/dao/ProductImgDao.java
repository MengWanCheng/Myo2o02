package com.imooc.o2o.dao;

import java.util.List;

import com.imooc.o2o.entity.ProductImg;

public interface ProductImgDao {
	/**
	 * 批量插入店铺详情图片
	 * @param productImg
	 * @return
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);
	/**
	 * 查询ProductList
	 * @param productId
	 * @return
	 */
	List<ProductImg> queryProductImgList(long productId);
	/**
	 * 删除指定商品下的所有详情图
	 * @param productId
	 * @return
	 */
	int deleteProductImgByProductId(long productId);
}
