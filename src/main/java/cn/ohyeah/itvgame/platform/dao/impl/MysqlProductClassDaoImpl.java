package cn.ohyeah.itvgame.platform.dao.impl;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.dao.IProductClassDao;
import cn.ohyeah.itvgame.platform.model.ProductClass;

public class MysqlProductClassDaoImpl implements IProductClassDao {

	@Override
	public ProductClass read(int classId) {
		return QueryHelper.read_cache(ProductClass.class, "productClass", classId,
				"select * from ProductClass where classId=?", classId);
	}

	@Override
	public void save(ProductClass productClass) {
		QueryHelper.update("insert into ProductClass(className) values(?)", 
				productClass.getClassName());
		int classId =  QueryHelper.read(long.class, "select LAST_INSERT_ID()").intValue();
		productClass.setClassID(classId);
		Configuration.setCache("productClass", productClass.getClassID(), productClass);

	}

}
