package com.imooc.o2o.enums;

public enum ProductCategoryStateEnum {
	SUCCESS(1, "操作成功"), INNER_ERROR(-1001, "操作失败"), NULL_SHOP(-1002, "Shop信息为空"),
	EMPETY_LIST(-1003, "请输入商品目录信息");
	private int state;
	private String stateInfo;
	/**
	 * 构造函数
	 * @param state
	 * @param stateInfo
	 */
	private ProductCategoryStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getStateInfo() {
		return stateInfo;
	}
	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}
	
	public static ProductCategoryStateEnum stateOf(int index) {
		for(ProductCategoryStateEnum productCategoryStateEnum: values()) {
			if(productCategoryStateEnum.getState()==index) {
				return productCategoryStateEnum;
			}
		}
		return null;
	}
}
