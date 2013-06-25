package cn.ohyeah.itvgame.platform.dao;

import cn.ohyeah.itvgame.platform.model.AccountPermission;

public interface IAccountPermissionDao {
	public AccountPermission read(int accountId);
	public void save(AccountPermission ap);
	public void updateAfterSubscribe(AccountPermission ap);
}
