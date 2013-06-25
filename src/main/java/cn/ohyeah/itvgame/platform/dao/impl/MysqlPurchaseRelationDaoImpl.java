package cn.ohyeah.itvgame.platform.dao.impl;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.dao.IPurchaseRelationDao;
import cn.ohyeah.itvgame.platform.model.PurchaseRelation;

public class MysqlPurchaseRelationDaoImpl implements IPurchaseRelationDao {

	@Override
	public PurchaseRelation read(int productId, String implementor, String subscribeType, int value, int amount) {
		return QueryHelper.read(PurchaseRelation.class, 
				"select * from PurchaseRelation where productId=? and subscribeImplementor=? " +
				"and subscribeType=? and value=? and amount=? limit 1", 
				productId, implementor, subscribeType, value, amount);
	}

	@Override
	public void save(PurchaseRelation pr) {
		QueryHelper.update("insert into PurchaseRelation(productId, subscribeImplementor, subscribeType, amount, value, subscribeId, description) "
				+ "values(?, ?, ?, ?, ?, ?, ?)", 
				pr.getProductId(), pr.getSubscribeImplementor(), pr.getSubscribeType(), 
				pr.getAmount(), pr.getValue(), pr.getSubscribeId(), pr.getDescription());
		Configuration.setCache("purchaseRelation", pr.getPurchaseId(), pr);

	}

	@Override
	public PurchaseRelation read(int purchaseId) {
		return QueryHelper.read_cache(PurchaseRelation.class, "purchaseRelation", purchaseId, "select * from PurchaseRelation where purchaseId=?", purchaseId);
	}

}
