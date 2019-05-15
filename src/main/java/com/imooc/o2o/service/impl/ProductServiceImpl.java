package com.imooc.o2o.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;

	@Override
	@Transactional
	// 1. 添加缩略图，获取缩略图相对路径并赋值给product
	// 2. 往tb_product写入商品信息，获取productId
	// 3. 结合productId批量添加商品详情图
	// 4. 将商品详情图列表批量插入tb_product_img中
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
			throws ProductOperationException {
			//先判断Product, Shop, shopId是否为空
			if(product != null && product.getShop() != null && product.getShop().getShopId()!= null) {
				//给商品设置默认属性
				product.setCreateTime(new Date());
				product.setLastEditTime(new Date());
				//默认为上架状态
				product.setEnableStatus(1);
				//如果缩略图不为空，则添加缩略图
				if(thumbnail != null) {
					try {
						addThumbnail(product, thumbnail);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					int effectedNum = productDao.insertProduct(product);
					if (effectedNum <= 0) {
						throw new ProductOperationException("创建商品失败");
					} 
				} catch (Exception e) {
					throw new ProductOperationException("创建商品失败" + e.toString());
				}
				//若商品详情图不为空则添加
				if(productImgHolderList!=null && productImgHolderList.size()>0) {
					addProductImgList(product, productImgHolderList);
				}
				return new ProductExecution(ProductStateEnum.SUCCESS, product);
			}else {
				// 传参为空则返回空值错误信息
				return new ProductExecution(ProductStateEnum.EMPTY);
			}
	}
	/**
	 * 批量添加详情图
	 * @param product
	 * @param productImgHolderList
	 */
	private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {
		// 获取图片存储路径，这里直接存放到相应店铺的文件夹底下
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		// 遍历图片一次去处理，并添加进productImg实体类里
		for(ImageHolder productImgHolder: productImgHolderList) {
			String imageAddr = ImageUtil.generateNormalImg(productImgHolder, dest);
			ProductImg productImg = new ProductImg();
			productImg.setImgAddr(imageAddr);
			productImg.setProductId(product.getProductId());
			productImg.setCreateTime(new Date());
			productImgList.add(productImg);
		}
		// 如果确实是有图片需要添加的，就执行批量添加操作
		if(productImgList.size()>0) {
			try {
				int effectedNum = productImgDao.batchInsertProductImg(productImgList);
				if (effectedNum <= 0) {
					throw new ProductOperationException("创建商品详情图片失败");
				} 
			} catch (Exception e) {
				throw new ProductOperationException("创建商品详情图片失败" + e.toString());
			}
		}
	}
	/**
	 * 添加缩略图
	 * @param product
	 * @param thumbnail
	 * @throws IOException
	 */
	private void addThumbnail(Product product, ImageHolder thumbnail) throws IOException{
		//获取图片的相对路径
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		//然后对图片进行压缩，添加水印
		String thumbnailAddr = ImageUtil.generateThumbnails(thumbnail, dest);
		//最后添加到product中
		product.setImgAddr(thumbnailAddr);
	}
	/**
	 * 查询商品信息
	 */
	@Override
	public Product getProductById(long productId) {
		return productDao.queryProductById(productId);
	}
	@Override
	@Transactional
	// 1.若缩略图参数有值，则处理缩略图，
    // 若原先存在缩略图则先删除再添加新图，之后获取缩略图相对路径并赋值给product
    // 2.若商品详情图列表参数有值，对商品详情图片列表进行同样的操作
    // 3.将tb_product_img下面的该商品原先的商品详情图记录全部清除
    // 4.更新tb_product_img以及tb_product的信息
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> productImgHolderList) {
		//首先判断商品、商品所属店铺，所属店铺ID是否为空，如果为空则返回商品为空，如果不为空则进行修改商品信息
		if(product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			//给商品设置上默认属性
			product.setLastEditTime(new Date());
			//判断缩略图是否为空，如果不为空则进行修改（删除原来的，添加新的）
			if(thumbnail != null) {
				// 先获取一遍原有信息，因为原来的信息里有原图片地址
				Product tempProduct = productDao.queryProductById(product.getProductId());
				//如果原来product中有缩略图，则，删除
				if(tempProduct.getImgAddr() != null) {
					ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
				}
				//删除之后进行添加
				try {
					addThumbnail(product, thumbnail);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//如果商品详情图不为空，且它的size大于零，则删除原来的，添加新的图片
			if(productImgHolderList != null && productImgHolderList.size()>0) {
				deleteProductImgList(product.getProductId());
				addProductImgList(product, productImgHolderList);
			}
			try {
				int effectedNum = productDao.updateProduct(product);
				if (effectedNum <= 0) {
					throw new ProductOperationException("更新商品信息失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS);
			} catch (Exception e) {
				throw new ProductOperationException("更新商品信息失败" + e.toString());
			}
		}else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}
	
	private void deleteProductImgList(Long productId) {
		//根据productId获取原来的图片
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		// 干掉原来的图片
		for (ProductImg productImg : productImgList) {
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		// 删除数据库里原有图片的信息
		productImgDao.deleteProductImgByProductId(productId);
	}
	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		 // 计算每页开始的页码，并调用dao层取回指定页码的商品列表
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Product> productList= productDao.queryProductList(productCondition, rowIndex, pageSize);
		// 基于同样的查询条件返回该查询条件下的商品总数
		int count = productDao.queryProductCount(productCondition);
		ProductExecution pe = new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}

}
