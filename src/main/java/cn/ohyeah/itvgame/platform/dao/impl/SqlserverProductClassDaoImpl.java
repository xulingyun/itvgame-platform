package cn.ohyeah.itvgame.platform.dao.impl;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.dao.IProductClassDao;
import cn.ohyeah.itvgame.platform.model.ProductClass;

public class SqlserverProductClassDaoImpl implements IProductClassDao {

	@Override
	public ProductClass read(int classId) {
		return QueryHelper.read_cache(ProductClass.class, "productClass", classId,
				"select * from [ProductClass] where classId=?", classId);
	}

	@Override
	public void save(ProductClass productClass) {
		java.math.BigDecimal key = (java.math.BigDecimal)QueryHelper.updateReturnKey("insert into [ProductClass](className) values(?)", 
				productClass.getClassName());
		productClass.setClassID(key.intValue());
		Configuration.setCache("productClass", productClass.getClassID(), productClass);

	}

}
