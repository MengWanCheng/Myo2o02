package com.imooc.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
	public static boolean checkVerifyCode(HttpServletRequest request) {
		//期望的验证码（图片中的验证码）
		String verifyCodeException = (String) request.getSession()
				.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		//实际的验证码（用户输入的验证码）
		String verifyCodeActual = HttpServletRequestUtil
				.getString(request, "verifyCodeActual");
		
		if(verifyCodeException == null || !verifyCodeException.equals(verifyCodeActual)) {
			return false;
		}
		return true;
	}
}
