package com.imooc.o2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer{

	// 需要加密的字段数组
	private String[] encryptPropNames = {"jdbc.username", "jdbc.password"};
	
	protected String convertProperty(String propertyName, String propertyValue) {
		//判断属性是否加过密
		if(isEncryptProp(propertyName)) {
			//如果已加过密，则进行解密
			String decryptValue = DESUtils.getDecryptString(propertyValue);
			return decryptValue;
		}else {
			return propertyValue;
		}
	}

	private boolean isEncryptProp(String propertyName) {
		//若等于加密的field， 则进行解密
		for(String encryptpropertyName : encryptPropNames) {
			if(encryptpropertyName.equals(propertyName)) {
				return true;
			}
		}
		return false;
	}
}
