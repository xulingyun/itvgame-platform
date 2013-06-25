package cn.ohyeah.itvgame.platform.dao.impl;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.dao.IAccountDao;
import cn.ohyeah.itvgame.platform.model.Account;

public class MysqlAccountDaoImpl implements IAccountDao {
	
	@Override
	public void save(Account account) {
		QueryHelper.update("insert into Account(nickName, pwdMd5, privilege, userId, scores, "
				+ "goldCoin, gameCoin, createTime, updateTime, state) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
				account.getNickName(), account.getPwdMd5(), account.getPrivilege(), 
				account.getUserId(), account.getScores(), account.getGoldCoin(), 
				account.getGameCoin(), account.getCreateTime(),	account.getUpdateTime(), 
				account.getState());
		int accountId = QueryHelper.read(long.class, "select LAST_INSERT_ID()").intValue();
		account.setAccountId(accountId);
		Configuration.setCache("accountKeyId", account.getAccountId(), account);
		Configuration.setCache("accountKeyUserId", account.getUserId(), account);
	}

	@Override
	public void update(Account account) {
		QueryHelper.update("update Account set scores=?, goldCoin=?, gameCoin=? where accountId=?", 
				account.getScores(), account.getGoldCoin(), 
				account.getGameCoin(), account.getAccountId());
	}

	@Override
	public Account readByAccountId(int accountId) {
		Account account = QueryHelper.read_cache(Account.class, "accountKeyId", accountId, 
				"select * from Account where accountId=?", accountId);
		if (account != null) {
			Configuration.setCache("accountKeyUserId", account.getUserId(), account);
		}
		return account;
	}

	@Override
	public Account readByUserId(String userId) {
		Account account = QueryHelper.read_cache(Account.class, "accountKeyUserId", userId, 
				"select * from Account where userId=?", userId);
		if (account != null) {
			Configuration.setCache("accountKeyId", account.getAccountId(), account);
		}
		return account;
	}

	@Override
	public void updateCoins(Account account) {
		QueryHelper.update("update Account set goldCoin=?, gameCoin=? where accountId=?", 
				account.getGoldCoin(), account.getGameCoin(), account.getAccountId());
	}
}
