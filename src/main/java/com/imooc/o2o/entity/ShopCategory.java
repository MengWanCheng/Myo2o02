package com.imooc.o2o.entity;

import java.util.Date;

public class ShopCategory {
	private String shopCategoryName;
	private Long shopCategoryId;
	private String shopCategoryDesc;
	private String shopCategoryImg;
	private Integer priority;
	private Long parentId;
	private Date createTime;
	private Date lastEditTime;
	private ShopCategory parent;
	
	@Override
	public String toString() {
		return "ShopCategory [shopCategoryName=" + shopCategoryName + ", shopCategoryId=" + shopCategoryId
				+ ", shopCategoryDesc=" + shopCategoryDesc + ", shopCategoryImg=" + shopCategoryImg + ", priority="
				+ priority + ", parentId=" + parentId + ", createTime=" + createTime + ", lastEditTime=" + lastEditTime
				+ ", parent=" + parent + "]";
	}
	public ShopCategory getParent() {
		return parent;
	}
	public void setParent(ShopCategory parent) {
		this.parent = parent;
	}
	public String getShopCategoryName() {
		return shopCategoryName;
	}
	public void setShopCategoryName(String shopCategoryName) {
		this.shopCategoryName = shopCategoryName;
	}
	public Long getShopCategoryId() {
		return shopCategoryId;
	}
	public void setShopCategoryId(Long shopCategoryId) {
		this.shopCategoryId = shopCategoryId;
	}
	public String getShopCategoryDesc() {
		return shopCategoryDesc;
	}
	public void setShopCategoryDesc(String shopCategoryDesc) {
		this.shopCategoryDesc = shopCategoryDesc;
	}
	public String getShopCategoryImg() {
		return shopCategoryImg;
	}
	public void setShopCategoryImg(String shopCategoryImg) {
		this.shopCategoryImg = shopCategoryImg;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	
}
