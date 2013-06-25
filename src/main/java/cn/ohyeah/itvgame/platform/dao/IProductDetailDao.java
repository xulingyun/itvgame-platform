package cn.ohyeah.itvgame.platform.dao;

import cn.ohyeah.itvgame.platform.model.ProductDetail;

public interface IProductDetailDao {
	public void save(ProductDetail detail);
	public void update(ProductDetail detail);
	public ProductDetail read(int productId);
}
