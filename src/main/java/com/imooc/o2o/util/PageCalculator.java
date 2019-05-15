package com.imooc.o2o.util;

/**
 * 分页工具类Util
 * @author Administrator
 *
 */
public class PageCalculator {
	public static int calculateRowIndex(int pageIndex, int pageSize) {
		//如果页数大于0，则返回开始条数(pageIndex-1)，否则返回0，
		//比如每页5条，第一页，返回从0条开始，第二页，返回从5开始
		return (pageIndex>0)?(pageIndex-1)*pageSize:0;
	}
}
