package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.LocalAuthStateEnum;
import com.imooc.o2o.exceptions.LocalAuthOperationException;
import com.imooc.o2o.service.LocalAuthService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.MD5;

@Controller
@RequestMapping(value = "/shopadmin")
public class LocalAuthController {
	@Autowired
	private LocalAuthService localAuthService;
	
	@RequestMapping(value = "/localauthlogincheck", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> localAuthLoginCheck(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//是否需要校验的标志
		boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
		if(needVerify && !CodeUtil.checkVerifyCode(request)){
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		//获取用户输入的用户名和密码
		String userName = HttpServletRequestUtil.getString(request, "userName");
		
		String password = HttpServletRequestUtil.getString(request, "password");
		//判断用户名和密码是否为空
		if(userName != null && password != null) {
			//不为空，则对密码进行MD5加密
			password = MD5.getMd5(password);
			//然后查询LocalAuth信息
			LocalAuth localAuth = localAuthService.getLocalAuthByUserNameAndPwd(userName, password);
			if(localAuth != null) {
				//将personInfo写入session中
				request.getSession().setAttribute("user", localAuth.getPersonInfo());
				modelMap.put("success", true);
				modelMap.put("errMsg", "登录成功");
			}else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "用户名或密码不能为空");
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入用户名和密码");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/localauthregister", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> localAuthRegister(HttpServletRequest request) throws IOException{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//验证码校验
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		//接受前端传过来的参数
		String localAuthStr = HttpServletRequestUtil.getString(request, "localAuthStr");
		String personInfoStr = HttpServletRequestUtil.getString(request, "personInfoStr");
		ObjectMapper mapper = new ObjectMapper();
		LocalAuth localAuth = null;
		PersonInfo personInfo = null;
		MultipartHttpServletRequest multipartRequest = null;
		CommonsMultipartFile profileImg = null;
		//获取上下文
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		//若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
		if(multipartResolver.isMultipart(request)) {
			multipartRequest = (MultipartHttpServletRequest) request;
			profileImg =  (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "图片不能为空");
			return modelMap;
		}
		try {
			//将json串转为java对象(出现了问题，localAuth==null)
			localAuth = mapper.readValue(localAuthStr, LocalAuth.class);
			personInfo = mapper.readValue(personInfoStr, PersonInfo.class);
		} catch (Exception e) { 
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if(localAuth != null && localAuth.getPassword() != null 
				&& localAuth.getUserName() != null
				&& personInfo.getEmail() != null && personInfo.getName() != null) {
			//如果判断都有的话，则进行注册
//			PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
//			PersonInfo user = new PersonInfo();
//			if(localAuth.getPersonInfo() != null) {
//				localAuth.getPersonInfo().setUserId(user.getUserId());
//			}
			localAuth.setPersonInfo(personInfo);
			LocalAuthExecution le;
			try {
				ImageHolder imageHolder = new ImageHolder(profileImg.getOriginalFilename(), 
						profileImg.getInputStream());
				le = localAuthService.register(localAuth, imageHolder);
				if(le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", le.getStateInfo());
				}
			} catch (LocalAuthOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入注册信息");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> logout(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		request.getSession().setAttribute("user", null);
		request.getSession().setAttribute("shopList", null);
		request.getSession().setAttribute("currentShop", null);
		modelMap.put("success", true);
		return modelMap;
	}
	//Bug：进不了页面,是因为类下的RequestMethod设置了GET和POST
	@RequestMapping(value = "/changepwd", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> changepwd(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		//从前端获取userName、password、newPassword
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String password = HttpServletRequestUtil.getString(request, "password");
		String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
		long userId = 0;
		//从Session中获取PersonInfo信息
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		if(user != null && user.getUserId() != null) {
			userId = user.getUserId();
		}
		if(userName != null && password != null && newPassword != null
				&& userId > 0 && !password.equals(newPassword)) {
			try {
				LocalAuthExecution le = localAuthService.modifyLocalAuth(userId, userName, password, newPassword);
				if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", le.getStateInfo());
				} 
			} catch (LocalAuthOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}else { 
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入重置密码相关信息");
		}
		return modelMap;
	}
}
