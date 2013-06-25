package cn.ohyeah.itvgame.platform.dao.impl;

import java.util.List;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.platform.dao.IPurchaseRecordDao;
import cn.ohyeah.itvgame.platform.model.PurchaseRecord;
import cn.ohyeah.itvgame.platform.viewmodel.PurchaseDesc;
import cn.ohyeah.itvgame.utils.DateUtil;

public class SqlserverPurchaseRecordDaoImpl implements IPurchaseRecordDao {
	@Override
	public void save(PurchaseRecord pr) {
		java.math.BigDecimal key = (java.math.BigDecimal)QueryHelper.updateReturnKey("insert into [PurchaseRecord]" 
				+"(accountId, userId, productId, productName, propId, propName, " 
				+"propCount, amount, remark, time, ip) " 
				+"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
				pr.getAccountId(), pr.getUserId(), pr.getProductId(), pr.getProductName(), 
				pr.getPropId(), pr.getPropName(), pr.getPropCount(), pr.getAmount(), 
				pr.getRemark(), 
				DateUtil.convertToSqlDate(pr.getTime()), 
				pr.getIp());
		pr.setId(key.longValue());
	}

	@Override
	public List<PurchaseDesc> queryPurchaseDescList(int accountId, int productId, int offset, int length) {
		return QueryHelper.query(PurchaseDesc.class, 
				"select propId, propName, propCount, amount, remark, time from " +
				"(select top "+ (offset+length) +" propId, propName, propCount, amount, remark, time, " +
				"row_number() over(order by time desc, accountid asc, productId asc) rowNumber " +
				"from [PurchaseRecord] where accountId=? and productId=?) PR " +
				"where PR.rowNumber>?", accountId, productId, offset);
	}

	@Override
	public long queryPurchaseRecordCount(int accountId, int productId) {
		return QueryHelper.read(int.class,
				"select count(*) from [PurchaseRecord] where accountId=? and productId=?", accountId, productId) ;
	}

}
