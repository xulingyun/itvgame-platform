package cn.ohyeah.itvgame.platform.dao;

import cn.ohyeah.itvgame.platform.model.ProductPermission;

public interface IProductPermissionDao {
	public ProductPermission read(int accountId, int productId);
	public void save(ProductPermission pp);
	public void updateAfterSubscribe(ProductPermission pp);
}
