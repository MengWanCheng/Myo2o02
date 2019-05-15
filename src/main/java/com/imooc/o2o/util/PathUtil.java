package com.imooc.o2o.util;

public class PathUtil {
	private static String seperator = System.getProperty("file.separator");
	/**
	 * 返回项目图片的根路径
	 * @return
	 */
	public static String getImgBasePath() {
		String os = System.getProperty("os.name");
		String  basePath = "";
		if(os.toLowerCase().startsWith("win")) {
			basePath = "E:/java_Project/springboot项目/Image/";
		}else {
			basePath = "/home/xiangze/image/";
		}
		basePath = basePath.replace("/", seperator);
 		return basePath;
	}
	/**
	 * 返回项目子路径
	 * @param shopId
	 * @return
	 */
	public static String getShopImagePath(long shopId) {
		String imagePath = "/upload/item/shop" + shopId + "/";
		return imagePath.replace("/", seperator);
	}
	/**
	 * 返回PerosnInfoImg路径
	 * @return
	 */
	public static String getPersonInfoImagePath() {
		String personInfoImagePath = "/upload/item/personinfo";
		return personInfoImagePath.replace("/", seperator);
	}
}
