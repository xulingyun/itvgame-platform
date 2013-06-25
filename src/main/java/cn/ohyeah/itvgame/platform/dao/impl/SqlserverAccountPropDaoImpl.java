package cn.ohyeah.itvgame.platform.dao.impl;

import java.util.List;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.dao.IAccountPropDao;
import cn.ohyeah.itvgame.platform.model.AccountProp;
import cn.ohyeah.itvgame.platform.viewmodel.OwnPropDesc;
import cn.ohyeah.itvgame.utils.DateUtil;

public class SqlserverAccountPropDaoImpl implements IAccountPropDao {

	@Override
	public AccountProp read(int accountId, int productId, int propId) {
		return QueryHelper.read_cache(AccountProp.class, "accountProp", accountId+"&"+productId+"&"+propId, 
				"select * from [AccountProp] where accountId=? and productId=? and propId=?", 
				accountId, productId, propId);
	}

	@Override
	public void save(AccountProp accountProp) {
		QueryHelper.update("insert into [AccountProp]"
				+ "(accountId, propId, productId, count, expiryDate) "
				+ "values(?, ?, ?, ?, ?)", 
				accountProp.getAccountId(), accountProp.getPropId(), 
				accountProp.getProductId(), accountProp.getCount(),
				DateUtil.convertToSqlDate(accountProp.getExpiryDate()));
		String cacheId = accountProp.getAccountId()+"&"+accountProp.getProductId()+"&"+accountProp.getPropId();
		Configuration.setCache("accountProp", cacheId, accountProp);
	}

	@Override
	public void update(AccountProp accountProp) {
		QueryHelper.update("update [AccountProp] set count=?, expiryDate=? where accountId=? and productId=? and propId=?",
				accountProp.getCount(), 
				DateUtil.convertToSqlDate(accountProp.getExpiryDate()),
				accountProp.getAccountId(), 
				accountProp.getProductId(),	accountProp.getPropId());
	}

	@Override
	public List<OwnPropDesc> queryAccountPropList(int accountId, int productId) {
		return QueryHelper.query(OwnPropDesc.class, 
				"select propId, count from [AccountProp] where " +
				"accountId=? and productId=? and " +
				"((expiryDate is null and count>0) or (expiryDate is not null and expiryDate>?)) "+
				"order by accountId asc, productId asc", 
				accountId, productId,
				DateUtil.convertToSqlDate(new java.util.Date()));
	}
	
	@Override
	public void synProps(int accountId, int productId, int[] propIds, int[] counts) {
		Object[][] params = new Object[propIds.length][5];
		for (int i = 0; i < params.length; ++i) {
			params[i][0] = propIds[i];
			params[i][1] = propIds[i];
			params[i][2] = counts[i];
			params[i][3] = counts[i];
			params[i][4] = propIds[i];
		}
		//String sql = "update [AccountProp] set count=? where accountId="+accountId+" and productId="+productId+" and propId=?";
		String sql = "if not exists(select top 1 1 from [AccountProp] where accountId="+accountId+" and productId="+productId+" and propId=?) " +
				"insert into [AccountProp](accountId, productId, propId, count) values("+accountId+", "+productId+", ?, ?); else " +
				"update [AccountProp] set count=? where accountId="+accountId+" and productId="+productId+" and propId=?;";
		QueryHelper.batch(sql, params);
		/*ÐÞ¸Ä»º´æ*/
		for (int i = 0; i < propIds.length; ++i) {
			String cacheId = accountId+"&"+productId+"&"+propIds[i];
			AccountProp ap = (AccountProp)Configuration.getCache("accountProp", cacheId);
			if (ap != null) {
				ap.setCount((Integer)counts[i]);
			}
		}
	}

	@Override
	public void useProps(int accountId, int productId, int[] propIds, int[] nums) {
		Object[][] params = new Object[propIds.length][2];
		for (int i = 0; i < params.length; ++i) {
			params[i][0] = nums[i];
			params[i][1] = propIds[i];
		}
		String sql = "update [AccountProp] set count=count-? where accountId="+accountId+" and productId="+productId+" and propId=?";
		QueryHelper.batch(sql, params);
		/*ÐÞ¸Ä»º´æ*/
		for (int i = 0; i < propIds.length; ++i) {
			String cacheId = accountId+"&"+productId+"&"+propIds[i];
			AccountProp ap = (AccountProp)Configuration.getCache("accountProp", cacheId);
			if (ap != null) {
				ap.setCount(ap.getCount()-(Integer)nums[i]);
			}
		}
	}

}
