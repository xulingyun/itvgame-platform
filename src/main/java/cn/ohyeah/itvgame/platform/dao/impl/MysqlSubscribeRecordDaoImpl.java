package cn.ohyeah.itvgame.platform.dao.impl;

import java.util.List;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.platform.dao.ISubscribeRecordDao;
import cn.ohyeah.itvgame.platform.model.SubscribeRecord;
import cn.ohyeah.itvgame.platform.viewmodel.SubscribeDesc;

public class MysqlSubscribeRecordDaoImpl implements ISubscribeRecordDao {

	@Override
	public void save(SubscribeRecord sr) {
		QueryHelper.update("insert into " +
				"SubscribeRecord(accountId, userId, amount, productId, productName, " +
				"subscribeId, subscribeImplementor, subscribeType, accountSubscribeCommand, " +
				"productSubscribeCommand, payType, remark, time, ip) " +
				"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
				sr.getAccountId(), sr.getUserId(), sr.getAmount(), sr.getProductId(), sr.getProductName(),
				sr.getSubscribeId(), sr.getSubscribeImplementor(), sr.getSubscribeType(), 
				sr.getAccountSubscribeCommand(), sr.getProductSubscribeCommand(),
				sr.getPayType(), sr.getRemark(), sr.getTime(), sr.getIp());
		long id = QueryHelper.read(long.class, "select LAST_INSERT_ID()");
		sr.setId(id);
	}

	@Override
	public List<SubscribeDesc> querySubscribeDescList(String userId, int productId,
			int offset, int length) {
		return QueryHelper.query(SubscribeDesc.class, 
				"select amount, remark, time from SubscribeRecord where userId=? and productId=? " +
				"order by time desc, accountId asc, productId asc limit ?, ?" ,
				userId, productId, offset, length);
	}

	@Override
	public long querySubscribeList(String userId, int productId) {
		return QueryHelper.read(long.class,
		"select count(*) from SubscribeRecord where userId=? and productId=?", userId, productId) ;
    }
}
