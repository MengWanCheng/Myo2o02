package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;

public class ProductCategoryDaoTest extends BaseTest {

	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Test
	@Ignore
	public void testQueryProductCategory() {
		Long shopId = 1L;
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
		System.out.println(productCategoryList.size());
	}

	@Test
	@Ignore
	public void testBatchInsertProductCategory() {
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryName("商品类别1");
		productCategory.setPriority(21);
		productCategory.setCreateTime(new Date());
		productCategory.setShopId(1L);
		ProductCategory productCategory2 = new ProductCategory();
		productCategory2.setProductCategoryName("商品类别2");
		productCategory2.setPriority(22);
		productCategory2.setCreateTime(new Date());
		productCategory2.setShopId(1L);
		List<ProductCategory> productCategorylist = new ArrayList<ProductCategory>();
		productCategorylist.add(productCategory);
		productCategorylist.add(productCategory2);

		int effectedNum = productCategoryDao.batchInsertProductCategory(productCategorylist);
		assertEquals(2, effectedNum);
	}

	@Test
	public void testDeleteProductCategory() {
		Long shopId = 1L;
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
		for (ProductCategory pc : productCategoryList) {
			if ("商品类别1".equals(pc.getProductCategoryName()) || "商品类别2".equals(pc.getProductCategoryName())) {
				int effectedNum = productCategoryDao.deleteProductCategory(pc.getProductCategoryId(), shopId);
				assertEquals(1, effectedNum);
			}
		}
	}
}
