package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.exceptions.ProductOperationException;

public interface ProductService {
	/**
	 * 添加商品
	 * @param product
	 * @param thumbnail
	 * @param productImgHolderList
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product, ImageHolder thumbnail, 
			List<ImageHolder> productImgHolderList)throws ProductOperationException;
	/**
	 * 通过商品ID获取查询商品
	 * @param productId
	 * @return
	 */
	Product getProductById(long productId);
	/**
	 * 修改商品信息以及图片修改
	 * @param product
	 * @param thumbnail
	 * @param productImgHolderList
	 * @return
	 */
	ProductExecution modifyProduct(Product product, 
			ImageHolder thumbnail, List<ImageHolder> productImgHolderList);
	
	ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);
}
