package cn.ohyeah.itvgame.platform.dao.impl;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.dao.IPurchaseRelationDao;
import cn.ohyeah.itvgame.platform.model.PurchaseRelation;

public class SqlserverPurchaseRelationDaoImpl implements IPurchaseRelationDao {

	@Override
	public PurchaseRelation read(int productId, String implementor, String subscribeType, int value, int amount) {
		return QueryHelper.read(PurchaseRelation.class, 
				"select top 1 * from [PurchaseRelation] where productId=? and subscribeImplementor=? " +
				"and subscribeType=? and value=? and amount=?", 
				productId, implementor, subscribeType, value, amount);
	}

	@Override
	public void save(PurchaseRelation id) {
		QueryHelper.update("insert into [PurchaseRelation](productId, subscribeImplementor, subscribeType, amount, value, subscribeId, description) "
				+ "values(?, ?, ?, ?, ?, ?, ?)", 
				id.getProductId(), id.getSubscribeImplementor(), id.getSubscribeType(), 
				id.getAmount(), id.getValue(), id.getSubscribeId(), id.getDescription());
		Configuration.setCache("purchaseRelation", id.getPurchaseId(), id);

	}

	@Override
	public PurchaseRelation read(int purchaseId) {
		return QueryHelper.read_cache(PurchaseRelation.class, "purchaseRelation", purchaseId, "select * from [PurchaseRelation] where purchaseId=?", purchaseId);
	}

}
