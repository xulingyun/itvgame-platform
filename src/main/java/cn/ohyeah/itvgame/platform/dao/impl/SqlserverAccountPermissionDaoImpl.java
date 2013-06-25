package cn.ohyeah.itvgame.platform.dao.impl;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.dao.IAccountPermissionDao;
import cn.ohyeah.itvgame.platform.model.AccountPermission;
import cn.ohyeah.itvgame.utils.DateUtil;

public class SqlserverAccountPermissionDaoImpl implements IAccountPermissionDao {

	@Override
	public AccountPermission read(int accountId) {
		AccountPermission ap = QueryHelper.read_cache(AccountPermission.class, "accountPermission", accountId, 
				"select * from [AccountPermission] where accountId=?", accountId);
		return ap;
	}

	@Override
	public void save(AccountPermission ap) {
		QueryHelper.update("insert into [AccountPermission](accountId, daySubscribeLimit, " +
				"monthSubscribeLimit, lastSubscribeTime, daySubscribeAmount, " +
				"monthSubscribeAmount, totalSubscribeAmount, accessPermission, subscribePermission)" +
				"values(?, ?, ?, ?, ?, ?, ?, ?, ?)", 
				ap.getAccountId(),	ap.getDaySubscribeLimit(), ap.getMonthSubscribeLimit(),
				DateUtil.convertToSqlDate(ap.getLastSubscribeTime()), 
				ap.getDaySubscribeAmount(),	ap.getMonthSubscribeAmount(), ap.getTotalSubscribeAmount(),
				ap.getAccessPermission(), ap.getSubscribePermission());
		Configuration.setCache("accountPermission", ap.getAccountId(), ap);
	}

	@Override
	public void updateAfterSubscribe(AccountPermission ap) {
		QueryHelper.update("update [AccountPermission] set " +
				"lastSubscribeTime=?, daySubscribeAmount=?, monthSubscribeAmount=?, " +
				"totalSubscribeAmount=? where accountId=?", 
				DateUtil.convertToSqlDate(ap.getLastSubscribeTime()), 
				ap.getDaySubscribeAmount(),
				ap.getMonthSubscribeAmount(), 
				ap.getTotalSubscribeAmount(),
				ap.getAccountId());
	}

}
