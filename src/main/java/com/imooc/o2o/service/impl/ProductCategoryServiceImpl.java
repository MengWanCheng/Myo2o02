package com.imooc.o2o.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{
	@Autowired
	private ProductCategoryDao productCategoryDao;
	@Autowired
	private ProductDao productDao;
	public List<ProductCategory> getProductCategory(Long shopId){
		return productCategoryDao.queryProductCategoryList(shopId);
	}
	@Override
	@Transactional
	public ProductCategoryExecution addProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		if(productCategoryList!=null && productCategoryList.size()>0) {
			int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
			if(effectedNum<=0) {
				throw new ProductCategoryOperationException("店铺类别创建失败");
			}else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		}else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPETY_LIST);
		}
	}
	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		// TODO 第一步，需要先将商品目录下的商品的类别Id置空
		try {
            int effectNum = productDao.updateProductCategoryToNull(productCategoryId);
            if (effectNum < 0) {
                throw new ProductCategoryOperationException("商品类别更新失败");
            }
        } catch (Exception e) {
            throw new ProductCategoryOperationException("deleteProductCategory error: " + e.getMessage());
        }
		//删除该商品目录
		try {
			int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if (effectedNum >= 0) {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			} else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.INNER_ERROR);
			} 
		} catch (Exception e) {
			throw new ProductCategoryOperationException(e.getMessage());
		}
	}
}
