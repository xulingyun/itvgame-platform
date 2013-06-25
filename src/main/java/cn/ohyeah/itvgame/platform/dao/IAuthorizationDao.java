package cn.ohyeah.itvgame.platform.dao;

import cn.ohyeah.itvgame.platform.model.Authorization;

public interface IAuthorizationDao {
	public void save(Authorization auth);
	public void update(Authorization auth);
	public Authorization read(int accountId, int productId);
	public void updateCoins(Authorization auth);
}
