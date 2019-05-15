package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductImg;

public class ProductImgDaoTest extends BaseTest{
	@Autowired
	private ProductImgDao productImgDao;
	
	@Test
	@Ignore
	public void testABatchInsertProductImg() {
		ProductImg productImg1 = new ProductImg();
		productImg1.setImgAddr("/aaa/bbb");
		productImg1.setImgDesc("商品详情图片1");
		productImg1.setPriority(1);
		productImg1.setCreateTime(new Date());
		productImg1.setProductId(1L);
		ProductImg productImg2 = new ProductImg();
		productImg2.setImgAddr("/aaa2/bbb2");
		productImg2.setImgDesc("商品详情图片2");
		productImg2.setPriority(2);
		productImg2.setCreateTime(new Date());
		productImg2.setProductId(1L);
		
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		productImgList.add(productImg1);
		productImgList.add(productImg2);
		
		int effectedNum = productImgDao.batchInsertProductImg(productImgList);
		assertEquals(2, effectedNum);
	}
	@Test
	@Ignore
	public void testBQueryProductImgList() {
		// 检查productId为1的商品是否有且仅有两张商品详情图片
        List<ProductImg> productImgList = productImgDao.queryProductImgList(2L);
        assertEquals(2, productImgList.size());
	}
	@Test
	public void testCDeleteProductImgByProductId() {
		// 删除新增的两条商品详情图片记录
        long productId = 3L;
        int effectedNum = productImgDao.deleteProductImgByProductId(productId);
        assertEquals(2, effectedNum);
	}
}
