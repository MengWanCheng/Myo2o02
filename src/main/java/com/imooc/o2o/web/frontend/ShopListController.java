package com.imooc.o2o.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "/frontend")
public class ShopListController {

	@Autowired
    private AreaService areaService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private ShopService shopService;
    /**
     * 返回商品列表页里的ShopCategory列表(二级或者一级)，以及区域信息列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/listshopspageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShopsPageInfo(HttpServletRequest request){
    	Map<String, Object> modelMap = new HashMap<String, Object>();
    	// 试着从前端请求中获取parentId
    	long parentId = HttpServletRequestUtil.getLong(request, "parentId");
    	List<ShopCategory> shopCategoryList = null;
    	if(parentId != -1) {
    		try {
				// 如果parentId存在，则取出该一级ShopCategory下的二级ShopCategory列表
    			ShopCategory childShopCategory = new ShopCategory();
				ShopCategory parentShopCategory = new ShopCategory();
				parentShopCategory.setShopCategoryId(parentId);
				childShopCategory.setParent(parentShopCategory);
				shopCategoryList = shopCategoryService.getShopCategoryList(childShopCategory);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
    				
    	}else {
    		// 如果parentId不存在，则取出所有一级ShopCategory(用户在首页选择的是全部商店列表)
    		shopCategoryList = shopCategoryService.getShopCategoryList(null);
    	}
    	modelMap.put("shopCategoryList", shopCategoryList);
    	List<Area> areaList = null;
    	try {
			areaList = areaService.getAreaList();
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
    }
    
    /**
	 * 获取指定查询条件的店铺列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listshops", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShops(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// 获取一页需要显示的数据条数
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		//进行非空判断
		if(pageIndex > -1 && pageSize > -1) {
			//获取一级类别ID
			long parentId = HttpServletRequestUtil.getLong(request, "parentId");
			//获取指定的二级类别Id
			long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
			//获取区域Id
			long areaId = HttpServletRequestUtil.getLong(request, "areaId");
			//获取获取模糊查询的名字
			String shopName = HttpServletRequestUtil.getString(request, "shopName");
			//获取组合之后的查询条件
			Shop shopCondition = compactShopCondition4Search(parentId, shopCategoryId, areaId, shopName);
			//根据查询条件获取店铺列表，并返回总数
			ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("count", se.getCount());
			modelMap.put("success", true);
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}
		return modelMap;
	}
	
	private Shop compactShopCondition4Search(long parentId, long shopCategoryId, long areaId, String shopName) {
		Shop shopCondition = new Shop();
		if(parentId != -1L) {
			// 查询某个一级ShopCategory下面的所有二级ShopCategory里面的店铺列表
			ShopCategory childCategory = new ShopCategory();
			ShopCategory parentCategory = new ShopCategory();
			parentCategory.setParentId(parentId);
			childCategory.setParent(parentCategory);
			shopCondition.setShopCategory(childCategory);
		} 
		if(shopCategoryId != -1L) {
			// 查询某个二级ShopCategory下面的店铺列表
			ShopCategory shopCategory = new ShopCategory();
			shopCategory.setShopCategoryId(shopCategoryId);
			shopCondition.setShopCategory(shopCategory);
		}
		if(areaId != -1L) {
			// 查询位于某个区域Id下的店铺列表
			Area area  = new Area();
			area.setAreaId(areaId);
			shopCondition.setArea(area);
		}
		if(shopName != null) {
			// 查询名字里包含shopName的店铺列表
			shopCondition.setShopName(shopName);
		}
		// 前端展示的店铺都是审核成功的店铺
		shopCondition.setEnableStatus(1);
		return shopCondition;
	}

}
