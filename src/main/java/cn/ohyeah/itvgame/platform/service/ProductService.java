package cn.ohyeah.itvgame.platform.service;

import java.util.List;
import java.util.Map;

import cn.halcyon.dao.DBException;
import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.dao.IProductClassDao;
import cn.ohyeah.itvgame.platform.dao.IProductDao;
import cn.ohyeah.itvgame.platform.dao.IProductDetailDao;
import cn.ohyeah.itvgame.platform.dao.IProductProviderDao;
import cn.ohyeah.itvgame.platform.model.Product;
import cn.ohyeah.itvgame.platform.model.ProductClass;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.ProductProvider;

public class ProductService {
	private static final IProductClassDao	pcDao;
	private static final IProductProviderDao ppDao;
	private static final IProductDao productDao;
	private static final IProductDetailDao detailDao;
	
	static {
		pcDao = (IProductClassDao)BeanManager.getDao("productClassDao");
		ppDao = (IProductProviderDao)BeanManager.getDao("productProviderDao");
		productDao = (IProductDao)BeanManager.getDao("productDao");
		detailDao = (IProductDetailDao)BeanManager.getDao("productDetailDao");
	}
	
	public ProductProvider readProductProvider(int providerId) {
		try {
			return ppDao.read(providerId);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public void saveProductProvider(ProductProvider provider) {
		try {
			ppDao.save(provider);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public ProductClass readProductClass(int classId) {
		try {
			return pcDao.read(classId);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public void saveProductClass(ProductClass productClass) {
		try {
			pcDao.save(productClass);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public Product read(int productId) {
		try {
			return productDao.read(productId);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public Product readByAppName(String appName) {
		try {
			return productDao.readByAppName(appName);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<Product> readAll() {
		try {
			return productDao.readAll();
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public void save(Product product) {
		try {
			productDao.save(product);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public ProductDetail readProductDetail(int productId) {
		try {
			return detailDao.read(productId);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public void saveProductDetail(ProductDetail detail) {
		try {
			detailDao.save(detail);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public ProductDetail validateExistProductDetail(Map<String, Object> props, int productId) {
		ProductDetail detail = (ProductDetail)props.get("productDetail");
		if (detail == null) {
			detail = readProductDetail(productId);
			if (detail == null) {
				throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PRODUCT));
			}
			props.put("productDetail", detail);
		}
		return detail;
	}
	public ProductDetail validateExistProductDetail(int productId) {
		ProductDetail detail = readProductDetail(productId);
		if (detail == null) {
			throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PRODUCT));
		}
		return detail;
	}
	
	public Product validateExistProduct(Map<String, Object> props, int productId) {
		Product product = (Product)props.get("product");
		if (product == null) {
			product = read(productId);
			if (product == null) {
				throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PRODUCT));
			}
			props.put("product", product);
		}
		return product;
	}
	
	public Product validateExistProduct(int productId) {
		Product product = read(productId);
		if (product == null) {
			throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PRODUCT));
		}
		return product;
	}
}
