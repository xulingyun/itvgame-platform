package cn.ohyeah.itvgame.platform.dao.impl;

import java.util.List;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.platform.dao.ISubscribeRecordDao;
import cn.ohyeah.itvgame.platform.model.SubscribeRecord;
import cn.ohyeah.itvgame.platform.viewmodel.SubscribeDesc;
import cn.ohyeah.itvgame.utils.DateUtil;

public class SqlserverSubscribeRecordDaoImpl implements ISubscribeRecordDao {

	@Override
	public void save(SubscribeRecord sr) {
		java.math.BigDecimal key = (java.math.BigDecimal)QueryHelper.updateReturnKey("insert into " +
				"[SubscribeRecord](accountId, userId, amount, productId, productName, " +
				"subscribeId, subscribeImplementor, subscribeType, accountSubscribeCommand, " +
				"productSubscribeCommand, payType, remark, time, ip) " +
				"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
				sr.getAccountId(), sr.getUserId(), sr.getAmount(), sr.getProductId(), sr.getProductName(),
				sr.getSubscribeId(), sr.getSubscribeImplementor(), sr.getSubscribeType(), 
				sr.getAccountSubscribeCommand(), sr.getProductSubscribeCommand(), sr.getPayType(),
				sr.getRemark(), DateUtil.convertToSqlDate(sr.getTime()), 
				sr.getIp());
		sr.setId(key.longValue());
	}

	@Override
	public List<SubscribeDesc> querySubscribeDescList(String userId, int productId,
			int offset, int length) {
		return QueryHelper.query(SubscribeDesc.class, 
				"select amount, remark, time from (select top "+(offset+length)+" amount, remark, time, " +
				"row_number() over(order by time desc, accountId asc) rowNumber " +
				"from [SubscribeRecord] where userId=? and productId=?) SR " +
				"where SR.rowNumber>?", userId, productId, offset);
	}

	@Override
	public long querySubscribeList(String userId, int productId) {
		return QueryHelper.read(int.class,
		"select count(*) from [SubscribeRecord] where userId=? and productId=?",userId, productId) ;
     }
}

