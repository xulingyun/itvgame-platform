package cn.ohyeah.itvgame.platform.dao;

import cn.ohyeah.itvgame.platform.model.ProductProvider;

public interface IProductProviderDao {
	public void save(ProductProvider provider);
	public ProductProvider read(int providerId);
}
