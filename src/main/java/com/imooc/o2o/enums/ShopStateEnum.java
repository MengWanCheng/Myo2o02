package com.imooc.o2o.enums;

public enum ShopStateEnum {

	CHECK(0, "审核中"), OFFLINE(-1, "非法商铺"), SUCCESS(1, "操作成功"), PASS(2, "认证通过"),
	INNER_ERROR(-1001, "内部系统错误"), NULL_SHOPID(-1002, "shopId为空"), 
	NULL_SHOP_INFO(-1003, "店铺为空");
	private int state;
	private String stateInfo;
	
	private ShopStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	
	public static ShopStateEnum stateOf(int state) {
		for(ShopStateEnum stateEnum: values()) {
			if(stateEnum.getState() == state) {
				return stateEnum;
			}
		}
		return null;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}
}
