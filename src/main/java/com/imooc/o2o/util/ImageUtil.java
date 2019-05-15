package com.imooc.o2o.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.imageio.ImageIO;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.imooc.o2o.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r = new Random();

	/**
	 * 将CommonsMultipartFile转换成file
	 * 
	 * @param cFile
	 * @return
	 */
	public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
		File newFile = new File(cFile.getOriginalFilename());
		try {
			cFile.transferTo(newFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newFile;
	}

	/**
	 * 存储缩略图(路径) targetAddr 目标存储路径 thumbnail 用户传递过来的文件流，文件处理对象
	 * 
	 * @return
	 */
	public static String generateThumbnails(ImageHolder thumbnail, String targetAddr) {
		// 获取随机文件名
		String realFileName = getRandomFileName();
		// 获取文件的扩展名
		String extension = getFileExtension(thumbnail.getImageName());
		// 创建文件路径的文件夹
		makeDirPath(targetAddr);
		// 获取文件的相对路径
		String relativeAddr = targetAddr + realFileName + extension;
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getImage()).size(200, 200)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/xhr.jpg")), 0.5f)
					.outputQuality(0.8f).toFile(dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return relativeAddr;
	}

	/**
	 * 创建目标路径所涉及到的目录，即/home/xiangze/xxx.jpg, 那么home, xiangze, 的目录都要自动创建出来
	 * 
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		String realParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realParentPath);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}

	/**
	 * 获取输入文件的扩展名
	 * 
	 * @param thumbnail
	 * @return
	 */
	private static String getFileExtension(String fileName) {
//		String originalFileName = thumbnail.getName();
		// 从.开始获取子字符串
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 生成随机文件名，当前时间+五位随机数
	 * 
	 * @return
	 */
	public static String getRandomFileName() {
		// 生成五位随机数
		int ranNum = r.nextInt(89999) + 10000;
		// 生成当前时间，格式
		String nowTimeStr = sDateFormat.format(new Date());
		return nowTimeStr + ranNum;
	}
	/**
	 * 删除文件和文件路径
	 */
	public static void deleteFileOrPath(String storePath) {
		File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
		if(fileOrPath.exists()) {
			if(fileOrPath.isDirectory()) {
				File[] listFiles = fileOrPath.listFiles();
				for (File file : listFiles) {
					file.delete();
				}
			}
			fileOrPath.delete();
		}
	}
	
	public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
		// 获取不重复的随机名
		String realFileName = getRandomFileName();
		// 获取文件的扩展名如png,jpg等
		String extension = getFileExtension(thumbnail.getImageName());
		// 如果目标路径不存在，则自动创建
		makeDirPath(targetAddr);
		// 获取文件存储的相对路径(带文件名)
		String relativeAddr = targetAddr + realFileName + extension;
		// 获取文件要保存到的目标路径
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		// 调用Thumbnails生成带有水印的图片
		try {
			Thumbnails.of(thumbnail.getImage()).size(337, 640)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/xhr.jpg")), 0.25f)
					.outputQuality(0.9f).toFile(dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return relativeAddr;
	}

	public static void main(String[] args) throws IOException {

		Thumbnails.of(new File("E:/java_Project/springboot项目/hanjiaren.jpg")).size(200, 200)
				.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/xhr.jpg")), 0.5f)
				.outputQuality(0.8f).toFile("E:/java_Project/springboot项目/hanjiarennew.jpg");
		// File file = new File("");
	}
}
