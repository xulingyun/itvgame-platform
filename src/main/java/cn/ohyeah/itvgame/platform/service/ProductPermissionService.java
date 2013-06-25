package cn.ohyeah.itvgame.platform.service;

import java.util.Map;

import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.BusinessException;
import cn.ohyeah.itvgame.business.service.impl.PermitImpl;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.ProductPermission;

public class ProductPermissionService {
	private static final PermitImpl permitImpl;
	
	static {
		permitImpl = BeanManager.getPermitImpl();
	}
	
	public ProductPermission read(int accountId, int productId) {
		try {
			return permitImpl.read(accountId, productId);
		}
		catch (BusinessException e) {
			throw new ServiceException(e);
		}
	}
	
	private ProductPermission checkProductPermission(Account account, ProductDetail detail) {
		try {
			return permitImpl.checkProductPermission(account, detail);
		}
		catch (BusinessException e) {
			throw new ServiceException(e);
		}
	}
	
	public int updateAfterSubscribe( ProductPermission pp,
			java.util.Date subTime, int amount) {
		try {
			return permitImpl.updateAfterSubscribe(pp, subTime, amount);
		}
		catch (BusinessException e) {
			throw new ServiceException(e);
		}
	}
	
	public ResultInfo checkSubscribePermission(ProductPermission pp, 
			ProductDetail detail, java.util.Date subTime, int amount) {
		try {
			return permitImpl.checkSubscribePermission(pp, detail, subTime, amount);
		}
		catch (BusinessException e) {
			throw new ServiceException(e);
		}
	}
	
	public ProductPermission validateExist(Map<String, Object> props, Account account, ProductDetail detail) {
		try {
			ProductPermission pp = (ProductPermission)props.get("productPermission");
			if (pp == null) {
				pp = checkProductPermission(account, detail);
				props.put("productPermission", pp);
			}
			return pp;
		}
		catch (BusinessException e) {
			throw new ServiceException(e);
		}
	}
	
	public ProductPermission validateExist(Account account, ProductDetail detail) {
		try {
			return checkProductPermission(account, detail);
		}
		catch (BusinessException e) {
			throw new ServiceException(e);
		}
	}
}
