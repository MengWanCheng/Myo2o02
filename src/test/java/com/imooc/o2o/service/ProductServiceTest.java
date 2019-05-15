package com.imooc.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;

public class ProductServiceTest extends BaseTest {

	@Autowired
	private ProductService productService;

	@Test
	@Ignore
	public void testAAddProduct() throws FileNotFoundException {
		// 创建shopId为1且productCategoryId为1的商品实例并给其成员变量赋值
		Shop shop = new Shop();
		shop.setShopId(1L);
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(1L);
		Product product = new Product();
		product.setShop(shop);
		product.setProductCategory(productCategory);
		product.setProductName("测试商品1");
		product.setProductDesc("测试商品描述1");
		product.setPriority(20);
		product.setCreateTime(new Date());
		product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
		// 创建缩略图文件流
		File thumbnailFile = new File("E:/java_Project/springboot项目/byl.jpg");
		InputStream is = new FileInputStream(thumbnailFile);
		ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), is);
		// 创建两个详情图文件流，并将它们添加到详情图列表中
		File productImg1 = new File("E:/java_Project/springboot项目/hanjiaren.jpg");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("E:/java_Project/springboot项目/hanjiaren.jpg");
		InputStream is2 = new FileInputStream(productImg2);
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		productImgList.add(new ImageHolder(productImg1.getName(), is1));
		productImgList.add(new ImageHolder(productImg2.getName(), is2));
		// 添加商品并验证
		ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
	}

	@Test
	@Ignore
	public void testBModifyProduct() throws FileNotFoundException {
		// 创建shopId为1且productCategoryId为1的商品实例并给其成员变量赋值
		Shop shop = new Shop();
		shop.setShopId(1L);
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(1L);
		Product product = new Product();
		product.setShop(shop);
		product.setProductCategory(productCategory);
		product.setProductId(1L);
		product.setProductName("第三个奶茶产品");
		product.setProductDesc("鲍逸琳奶茶，欢迎品尝！");
		product.setPriority(40);
		product.setLastEditTime(new Date());
		product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
		// 创建缩略图文件流
		File thumbnailFile = new File("E:/java_Project/springboot项目/byl.jpg");
		InputStream is = new FileInputStream(thumbnailFile);
		ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), is);
		// 创建两个详情图文件流，并将它们添加到详情图列表中
		File productImg1 = new File("E:/java_Project/springboot项目/hanjiaren.jpg");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("E:/java_Project/springboot项目/hanjiaren.jpg");
		InputStream is2 = new FileInputStream(productImg2);
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		productImgList.add(new ImageHolder(productImg1.getName(), is1));
		productImgList.add(new ImageHolder(productImg2.getName(), is2));
		// 添加商品并验证
		ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
	}
	
	@Test
	public void testCGetProductList() {
		//从第1页开始取，每页取2条
		int pageIndex = 1;
		int pageSize = 2;
		
		Shop shop = new Shop();
		ProductCategory productCategory = new ProductCategory();
		shop.setShopId(1L);
		productCategory.setProductCategoryId(1L);
		
		Product productCondition = new Product();
		productCondition.setShop(shop);
		productCondition.setProductCategory(productCategory);
		productCondition.setProductName("奶茶");
		
		ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
		assertEquals(2, pe.getProductList().size());
		assertEquals(3, pe.getCount());
		
	}
}
