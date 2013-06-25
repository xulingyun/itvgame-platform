package cn.ohyeah.itvgame.platform.dao;

import cn.ohyeah.itvgame.platform.model.PurchaseRelation;

public interface IPurchaseRelationDao {
	public PurchaseRelation read(int productId, String implementor, String subscribeType, int period, int amount);
	public void save(PurchaseRelation id);
	public PurchaseRelation read(int purchaseId);
}
