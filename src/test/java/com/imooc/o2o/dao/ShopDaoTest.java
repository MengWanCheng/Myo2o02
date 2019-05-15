package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;

public class ShopDaoTest extends BaseTest{

	@Autowired
	private ShopDao shopDao;
	
	@Test
	@Ignore
	public void testInsertShop() {
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
		shop.setShopName("测试的店铺");
		shop.setShopAddr("test");
		shop.setShopDesc("test");
		shop.setShopImg("test");
		shop.setEnableStatus(1);
		shop.setPhone("test");
		shop.setCreateTime(new Date());
		shop.setAdvice("审核中...");
		int effectedNum = shopDao.insertShop(shop);
		assertEquals(1, effectedNum);
	}
	@Test
	@Ignore
	public void testUpdateShop() {
		Shop shop = new Shop();
		shop.setShopId(1L);
		shop.setShopAddr("测试地址");
		shop.setShopDesc("测试描述");
		shop.setLastEditTime(new Date());
		int effectedNum = shopDao.updateShop(shop);
		assertEquals(1, effectedNum);
	}
	@Test
	@Ignore
	public void testQueryByShopId() {
		Shop shop = shopDao.queryByShopId(19L);
		System.out.println(shop.getShopId());
		System.out.println(shop.getShopName());
		System.out.println(shop.getShopDesc());
	}
	@Test
	public void testQueryShopListAndCount() {
//		 Shop shopCondition = new Shop();
//		 PersonInfo owner = new PersonInfo();
//		 owner.setUserId(1L);
//		 shopCondition.setOwner(owner);
//		 List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 5);
//		 System.out.println(shopList.size());
//		 int i = shopDao.queryShopCount(shopCondition);
//		 System.out.println(i);
		 Shop shopCondition = new Shop();
		 ShopCategory childCategory = new ShopCategory();
		 ShopCategory parentCategory = new ShopCategory();
		 parentCategory.setParentId(12L);
		 childCategory.setParent(parentCategory);
		 shopCondition.setShopCategory(childCategory);

		 List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 6);
		 System.out.println("店铺列表大小" + shopList.size());
		 int i = shopDao.queryShopCount(shopCondition);
		 System.out.println("店铺总数" + i);
		 
	}
}
