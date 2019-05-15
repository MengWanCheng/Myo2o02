package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;

	@Autowired
	private ShopCategoryService shopCategoryServie;
	@Autowired
	private AreaService areaService;

	/**
	 * 从session中获取当前person拥有的商铺列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShopList(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 现在还没有做登录模块，因此session中并没有用户的信息，先模拟一下登录 要改造TODO
//		PersonInfo user = new PersonInfo();
//		user.setUserId(1L);
//		user.setName("小白Id");
		//先把user Set进去，然后再把它Get出来
//		request.getSession().setAttribute("user", "username");
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("user", user);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	/**
	 * 获取店铺管理信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getshopmanagementinfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShopManagementInfo(HttpServletRequest request){
		 Map<String,Object> modelMap = new HashMap<String,Object>();
		 //从前台获取shopId
		 long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		 //如果shopId不合法
		 if(shopId<=0) {
			 //尝试从当前session中获取
			 Shop currentShop= (Shop)request.getSession().getAttribute("currentShop");
			 //如果当前session中currentShop为空，那就告诉view层去重定向
			 if(currentShop == null) {
				 modelMap.put("redirect", true);
				 modelMap.put("url", "/o2o02/shopadmin/shoplist");
			 }else {
				 //如果当前session中有Shop，就进入该页面
				 modelMap.put("redirect", false);
				 modelMap.put("shopId", currentShop.getShopId());
			 }
		 }else {//如果当前shopId合法
			 Shop currentShop = new Shop();
			 currentShop.setShopId(shopId);
			 //将currentShop放到session中
			 request.getSession().setAttribute("currentShop", currentShop);
			 modelMap.put("redirect", false);
		 }
		return modelMap;
	}
	
	@RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShopById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从前台获取shopId
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		try {
			if (shopId > -1) {
				Shop shop = shopService.getByShopId(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShopInitInfo() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			shopCategoryList = shopCategoryServie.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	// 数据转化为JSON格式
	@ResponseBody
	public Map<String, Object> registershop(HttpServletRequest request) throws ServletRequestBindingException {

		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "对不起，验证码错误！");
			return modelMap;
		}
		// 接受并转化相应参数
		String shopStr = ServletRequestUtils.getStringParameter(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());// 获取上下文
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传文件不能为空");
			return modelMap;
		}
		// 注册店铺，返回结果
		if (shop != null && shopImg != null) {
			// Session
			// 店主personInfo信息，肯定要登录才能注册店铺
			// 所以这部分信息我们从session中获取，尽量不依赖前端,这里暂时时不具备条件，后续改造，先硬编码，方便单元测试
			// PersonInfo personInfo = new PersonInfo();
			// personInfo.setUserId(1L);

			// 注册店铺之前要登录，登陆成功后，约定将user这个key设置到session中，
			// 这里通过key就可以获取到personInfo的信息
			PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
			shop.setOwner(owner);
			ShopExecution se;
			try {
				ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
				se = shopService.addShop(shop, imageHolder);
				if (se.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
		}
		return modelMap;
	}

	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> modifyShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 先要验证码校验
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}

		// 1.接收并转换相应的参数，包括shop信息和图片信息
		// 1.1 shop信息
		// shopStr是和前端约定好的参数值，后端从request中获取这个值来获取shop的信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		// 使用jackson-dataind将json数据转化为pojo
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			// 将json转换为pojo (将json格式的shopStr转换为Shop对象)
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}

		// 1.2 图片信息 基于Apache Commons FileUpload的文件上传 （ 修改商铺信息 图片可以不更新）
		// SpringMVC中的图片存在于CommonsMultipartFile
		CommonsMultipartFile shopImg = null;
		// 从request本次会话中的上下文中获取图片的内容
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getServletContext());
		// 判断是否有上传的文件流
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			// shopImg是与前端约定好的变量名
			shopImg = (CommonsMultipartFile) multipartRequest.getFile("shopImg");
		}

		// 2. 修改店铺
		if (shop != null && shop.getShopId() != null) {
			// Session 部分的 PersonInfo 修改商铺是不需要设置的。
			// 修改店铺(图片可以不更新，所以首先判断图片是否为空,如果为空那就不更新图片信息，如果不为空那就改变图片)
			ShopExecution se = null;
			try {
				if (shopImg != null) {
					ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
					se = shopService.modifyShop(shop, imageHolder);
				} else {
					se = shopService.modifyShop(shop, null);
				}
				if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺Id");
			return modelMap;
		}
	}
}
