package cn.ohyeah.itvgame.platform.dao;

import cn.ohyeah.itvgame.platform.model.ProductClass;

public interface IProductClassDao {
	public void save(ProductClass productClass);
	public ProductClass read(int classId);
}
