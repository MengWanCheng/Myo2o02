package com.imooc.o2o.service;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;

public class ShopSerivceTest extends BaseTest{
	
	@Autowired
	private ShopService shopService;
	@Test
	@Ignore
	public void testAddShop() throws FileNotFoundException{
		Shop shop = new Shop();
		Area area = new Area();
		PersonInfo owner = new PersonInfo();
		ShopCategory shopCategory = new ShopCategory();
		owner.setUserId(1L);
		shopCategory.setShopCategoryId(1L);
		area.setAreaId(1L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试的店铺1");
		shop.setShopAddr("test1");
		shop.setShopDesc("test1");
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setPhone("test1");
		shop.setCreateTime(new Date());
		shop.setAdvice("审核中...");
		File shopImg = new File("E:/java_Project/springboot项目/hanjiaren.jpg");
		InputStream is = new FileInputStream(shopImg);
		ImageHolder imageHolder = new ImageHolder("byl.jpg", is);
		ShopExecution se = shopService.addShop(shop, imageHolder);
		assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
	}
	
	@Test
	@Ignore
	public void testmodifyShop() throws FileNotFoundException {
		Shop shop = new Shop();
		shop.setShopId(18L);
		shop.setShopName("修改后的店铺名称");
		File shopImg = new File("E:/java_Project/springboot项目/byl.jpg");
		InputStream is = new FileInputStream(shopImg);
		ImageHolder imageHolder = new ImageHolder("byl.jpg", is);
		ShopExecution se = shopService.modifyShop(shop, imageHolder);
		System.out.println("新的图片地址："  + se.getShop().getShopImg());
	}
	
	@Test
	public void getShopList() {
		Shop shopCondition = new Shop();
		ShopCategory sc = new ShopCategory();
		sc.setShopCategoryId(1L);
		shopCondition.setShopCategory(sc);
		ShopExecution se = shopService.getShopList(shopCondition, 1, 2);
		System.out.println(se.getShopList().size());
		System.out.println(se.getCount());
	}
}
