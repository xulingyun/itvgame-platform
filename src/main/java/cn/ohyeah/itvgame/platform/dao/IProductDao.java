package cn.ohyeah.itvgame.platform.dao;

import java.util.List;

import cn.ohyeah.itvgame.platform.model.Product;

public interface IProductDao {
	public void save(Product product);
	public void update(Product product);
	public Product read(int productId);
	public Product readByAppName(String appName);
	public List<Product> readAll();
}
