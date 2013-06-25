package cn.ohyeah.itvgame.platform.dao.impl;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.dao.IProductPermissionDao;
import cn.ohyeah.itvgame.platform.model.ProductPermission;

public class MysqlProductPermissionDaoImpl implements IProductPermissionDao {

	@Override
	public ProductPermission read(int accountId, int productId) {
		String cacheKey = accountId+"&"+productId;
		ProductPermission pp = QueryHelper.read_cache(ProductPermission.class, "productPermission", cacheKey, 
				"select * from ProductPermission where accountId=? and productId=?", accountId, productId);
		return pp;
	}

	@Override
	public void save(ProductPermission pp) {
		QueryHelper.update("insert into ProductPermission(accountId, productId, daySubscribeLimit, " +
				"monthSubscribeLimit, lastSubscribeTime, " +
				"daySubscribeAmount, monthSubscribeAmount, totalSubscribeAmount, " +
				"accessPermission, subscribePermission) " +
				"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
				pp.getAccountId(), pp.getProductId(),
				pp.getDaySubscribeLimit(), pp.getMonthSubscribeLimit(),
				pp.getLastSubscribeTime(), pp.getDaySubscribeAmount(),
				pp.getMonthSubscribeAmount(), pp.getTotalSubscribeAmount(),
				pp.getAccessPermission(), pp.getSubscribePermission());
		String cacheKey = pp.getAccountId()+"&"+pp.getProductId();
		Configuration.setCache("productPermission", cacheKey, pp);
	}

	@Override
	public void updateAfterSubscribe(ProductPermission pp) {
		QueryHelper.update("update ProductPermission set " +
				"lastSubscribeTime=?, daySubscribeAmount=?, monthSubscribeAmount=?, " +
				"totalSubscribeAmount=? where accountId=? and productId=?", 
				pp.getLastSubscribeTime(), 
				pp.getDaySubscribeAmount(),
				pp.getMonthSubscribeAmount(), 
				pp.getTotalSubscribeAmount(),
				pp.getAccountId(), pp.getProductId());
	}
}
