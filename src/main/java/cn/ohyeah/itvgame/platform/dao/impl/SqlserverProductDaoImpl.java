package cn.ohyeah.itvgame.platform.dao.impl;

import java.util.List;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.dao.IProductDao;
import cn.ohyeah.itvgame.platform.model.Product;
import cn.ohyeah.itvgame.utils.DateUtil;

public class SqlserverProductDaoImpl implements IProductDao {

	@Override
	public Product read(int productId) {
		return QueryHelper.read_cache(Product.class, "product", productId, 
			"select * from [Product] where productId=?", productId);
	}

	@Override
	public void save(Product product) {
		java.math.BigDecimal key = (java.math.BigDecimal)QueryHelper.updateReturnKey("insert into " +
				"[Product](productName, productClass, appName, appType, description, " +
				"supportDataManager, location, gameid, createTime, updateTime, providerID,state) " +
				"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)", 
				product.getProductName(), product.getProductClass(), product.getAppName(), 
				product.getAppType(), product.getDescription(), product.isSupportDataManager(),
				product.getLocation(), product.getGameid(), 
				DateUtil.convertToSqlDate(product.getCreateTime()), 
				DateUtil.convertToSqlDate(product.getUpdateTime()), 
				product.getProviderID(),
				product.getState());
		product.setProductId(key.intValue());
		Configuration.setCache("product", product.getProductId(), product);
	}

	@Override
	public void update(Product product) {
		QueryHelper.update("update [Product] set " +
				"productName=?, productClass=?, appName=?, appType=?, description=?, " +
				"supportDataManager=?, location=?, gameid=?, updateTime=?, providerID=?,state=? where productId=?", 
				product.getProductName(), product.getProductClass(), product.getAppName(), 
				product.getAppType(), product.getDescription(), product.isSupportDataManager(),
				product.getLocation(), product.getGameid(), 
				DateUtil.convertToSqlDate(product.getUpdateTime()), 
				product.getProviderID(),product.getState(), product.getProductId());
	}

	@Override
	public List<Product> readAll() {
		return QueryHelper.query(Product.class, "select * from [Product] where state=1");
	}

	@Override
	public Product readByAppName(String appName) {
		return QueryHelper.read(Product.class, "select * from [Product] where appName=?", appName);
	}

}
