package com.imooc.o2o.service;

import java.util.List;
import com.imooc.o2o.entity.ShopCategory;

public interface ShopCategoryService {
	public static final String SHOPCATEGORYLIST = "shopcategorylist";
	/**
	 * 获取商铺类别信息
	 * @param shopCategoryCondition
	 * @return
	 */
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
