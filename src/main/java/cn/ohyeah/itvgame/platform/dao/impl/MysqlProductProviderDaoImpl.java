package cn.ohyeah.itvgame.platform.dao.impl;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.dao.IProductProviderDao;
import cn.ohyeah.itvgame.platform.model.ProductProvider;

public class MysqlProductProviderDaoImpl implements IProductProviderDao {

	@Override
	public ProductProvider read(int providerId) {
		return QueryHelper.read_cache(ProductProvider.class, "productProvider", providerId,
				"select * from ProductProvider where providerId=?", providerId);
	}

	@Override
	public void save(ProductProvider provider) {
		QueryHelper.update("insert into ProductProvider(providerName, type) values(?, ?)", 
				provider.getProviderName(), provider.getType());
		int providerId =  QueryHelper.read(long.class, "select LAST_INSERT_ID()").intValue();
		provider.setProviderID(providerId);
		Configuration.setCache("productProvider", provider.getProviderID(), provider);

	}

}
