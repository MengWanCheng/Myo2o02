package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.entity.Shop;

public class ProductDaoTest extends BaseTest{
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;
	@Test
	@Ignore
	public void testAInsertProduct() {
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(1L);
		Shop shop = new Shop();
		shop.setShopId(1L);
		Product product = new Product();
		product.setProductCategory(productCategory);
		product.setShop(shop);
		product.setProductName("test_product1");
		product.setProductDesc("test_product_desc1");
		product.setImgAddr("/aaa/bbb");
		product.setNormalPrice("100");
		product.setPromotionPrice("80");
		product.setPriority(10);
		product.setCreateTime(new Date());
		product.setLastEditTime(new Date());
		product.setEnableStatus(1);
		
		int effectedNum = productDao.insertProduct(product);
		assertEquals(1, effectedNum);
	}
	
	@Test
	@Ignore
	public void testBQueryProductById() {
//		// 查询productId为1的商品信息
//		long productId =1L;
//		Product product = productDao.queryProductById(productId);
//		System.out.println(product.getProductName());
		long productId = 1;
        // 初始化两个商品详情图实例作为productId为1的商品下的详情图片
        // 批量插入到商品详情图表中
        ProductImg productImg1 = new ProductImg();
        productImg1.setImgAddr("图片1");
        productImg1.setImgDesc("测试图片1");
        productImg1.setPriority(1);
        productImg1.setCreateTime(new Date());
        productImg1.setProductId(productId);
        ProductImg productImg2 = new ProductImg();
        productImg2.setImgAddr("图片2");
        productImg2.setPriority(1);
        productImg2.setCreateTime(new Date());
        productImg2.setProductId(productId);
        List<ProductImg> productImgList = new ArrayList<ProductImg>();
        productImgList.add(productImg1);
        productImgList.add(productImg2);
        int effectedNum = productImgDao.batchInsertProductImg(productImgList);
        assertEquals(2, effectedNum);
        // 查询productId为1的商品信息并校验返回的详情图实例列表size是否为2
        Product product = productDao.queryProductById(productId);
        assertEquals(2, product.getProductImgList().size());
        // 删除新增的这两个商品详情图实例
        effectedNum = productImgDao.deleteProductImgByProductId(productId);
        assertEquals(2, effectedNum);
	}
	
	@Test
	@Ignore
	public void testCUpdateProduct() {
		Shop shop = new Shop();
		ProductCategory productCategory = new ProductCategory();
		Product product = new Product();
		shop.setShopId(1L);
		productCategory.setProductCategoryId(2L);
		product.setShop(shop);
		product.setProductCategory(productCategory);
		product.setProductId(3L);
		product.setProductName("第二个奶茶产品");
		int effectedNum = productDao.updateProduct(product);
		assertEquals(1, effectedNum);
	}
	
	 @Test
	    public void testEUpdateProductCategoryToNull() {
	        // 将productCategoryId为2的商品类别下面的商品的商品类别置为空
	        int effectedNum = productDao.updateProductCategoryToNull(2L);
	        assertEquals(1, effectedNum);
	    }
}
