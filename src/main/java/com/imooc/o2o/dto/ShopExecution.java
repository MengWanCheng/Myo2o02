package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;

public class ShopExecution {

	// 结果状态
	private int state;
	// 状态标识
	private String stateInfo;
	// 店铺的数量
	private int count;
	// 操作的shop（增删改查时候用）
	private Shop shop;
	// 获取shop的列表
	private List<Shop> shopList;

	public ShopExecution() {

	}

	// 店铺操作失败时候的构造器
	public ShopExecution(ShopStateEnum stateNum) {
		this.state = stateNum.getState();
		this.stateInfo = stateNum.getStateInfo();
	}

	// 店铺操作成功时候的构造器
	public ShopExecution(ShopStateEnum stateEnum, Shop shop) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shop = shop;
	}

	// 店铺操作成功时候的构造器（返回店铺列表）
	public ShopExecution(ShopStateEnum stateEnum, List<Shop> shopList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shopList = shopList;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public List<Shop> getShopList() {
		return shopList;
	}

	public void setShopList(List<Shop> shopList) {
		this.shopList = shopList;
	}
}
