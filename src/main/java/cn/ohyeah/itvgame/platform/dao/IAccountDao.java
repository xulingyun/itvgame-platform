package cn.ohyeah.itvgame.platform.dao;

import cn.ohyeah.itvgame.platform.model.Account;

public interface IAccountDao {
	public void save(Account account);
	public void update(Account account);
	public Account readByAccountId(int accountId);
	public Account readByUserId(String userId);
	public void updateCoins(Account account);
}
