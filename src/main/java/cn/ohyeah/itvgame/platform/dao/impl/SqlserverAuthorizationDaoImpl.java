package cn.ohyeah.itvgame.platform.dao.impl;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.dao.IAuthorizationDao;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.utils.DateUtil;

public class SqlserverAuthorizationDaoImpl implements IAuthorizationDao {

	@Override
	public Authorization read(int accountId, int productId) {
		String cacheId = accountId+"&"+productId;
		return QueryHelper.read_cache(Authorization.class, "authorization", cacheId, 
				"select * from [Authorization] where accountId=? and productId=?", 
				accountId, productId);
	}

	@Override
	public void save(Authorization auth) {
		QueryHelper.update("insert into [Authorization](accountId, productID, " 
				+ "authorizationType, leftTryNumber, leftValidSeconds, leftValidCount, " 
				+ "goldCoin, gameCoin, authorizationStartTime, authorizationEndTime) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
				auth.getAccountId(), auth.getProductId(), auth.getAuthorizationType(), 
				auth.getLeftTryNumber(), auth.getLeftValidSeconds(), auth.getLeftValidCount(), 
				auth.getGoldCoin(), auth.getGameCoin(),
				DateUtil.convertToSqlDate(auth.getAuthorizationStartTime()), 
				DateUtil.convertToSqlDate(auth.getAuthorizationEndTime()));
		String cacheId = auth.getAccountId()+"&"+auth.getProductId();
		Configuration.setCache("authorization", cacheId, auth);
	}

	@Override
	public void update(Authorization auth) {
		QueryHelper.update("update [Authorization] set " +
				"authorizationType=?, leftTryNumber=?, leftValidSeconds=?, leftValidCount=?, " +
				"goldCoin=?, gameCoin=?, authorizationStartTime=?, authorizationEndTime=? " +
				"where accountId=? and productId=?", 
				auth.getAuthorizationType(), auth.getLeftTryNumber(), auth.getLeftValidSeconds(), 
				auth.getLeftValidCount(), auth.getGoldCoin(), auth.getGameCoin(),
				DateUtil.convertToSqlDate(auth.getAuthorizationStartTime()), 
				DateUtil.convertToSqlDate(auth.getAuthorizationEndTime()), 
				auth.getAccountId(), auth.getProductId());
	}

	@Override
	public void updateCoins(Authorization auth) {
		QueryHelper.update("update [Authorization] set " +
				"goldCoin=?, gameCoin=? where accountId=? and productId=?", 
				auth.getGoldCoin(),	auth.getGameCoin(),
				auth.getAccountId(), auth.getProductId());
		
	}

}
