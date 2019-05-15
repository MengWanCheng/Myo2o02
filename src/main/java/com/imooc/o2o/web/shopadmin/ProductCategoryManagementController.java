package com.imooc.o2o.web.shopadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.dto.Result;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;

@Controller
@RequestMapping(value = "/shopadmin")
public class ProductCategoryManagementController {

	@Autowired
	private ProductCategoryService productCategoryService;

	@RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> removeProductCategory(Long productCategoryId, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (productCategoryId != null && productCategoryId > 0) {
			// 从session中获取shop的信息
			Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
			//判断session中shop是否为空
			if(currentShop !=null && currentShop.getShopId() !=null) {
				try {
					ProductCategoryExecution pe = productCategoryService.deleteProductCategory(productCategoryId,
							currentShop.getShopId());
					if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
						modelMap.put("success", true);
					} else {
						modelMap.put("success", false);
						modelMap.put("errMsg", pe.getStateInfo());
					} 
				} catch (ProductCategoryOperationException e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.getMessage());
				}
			}else {
				modelMap.put("success", false);
				modelMap.put("errMsg", ProductCategoryStateEnum.NULL_SHOP.getStateInfo());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请选择商品类别");
		}
		return modelMap;
	}

	@RequestMapping(value = "/addproductcategorys", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		// 给每一个商品类别设置shopId
		for (ProductCategory pc : productCategoryList) {
			pc.setShopId(currentShop.getShopId());
		}
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				ProductCategoryExecution pe = productCategoryService.addProductCategory(productCategoryList);
				if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("sucsess", false);
			modelMap.put("errMsg", "请输入至少一个商品类别");
		}

		return modelMap;
	}

	@RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
	@ResponseBody
	public Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		List<ProductCategory> list = null;
		ProductCategoryStateEnum ps;
		// 如果当前Session中有shop，你就取出商品类比列表
		if (currentShop != null && currentShop.getShopId() > 0) {
			try {
				list = productCategoryService.getProductCategory(currentShop.getShopId());
				return new Result<List<ProductCategory>>(true, list);
			} catch (Exception e) {
				e.printStackTrace();
				ps = ProductCategoryStateEnum.INNER_ERROR;
				return new Result<List<ProductCategory>>(false, ps.getStateInfo(), ps.getState());
			}
		} else {
			// 如果没有shop，那就报错(没有shop)
			ps = ProductCategoryStateEnum.NULL_SHOP;
			return new Result<List<ProductCategory>>(false, ps.getStateInfo(), ps.getState());
		}
	}
}
