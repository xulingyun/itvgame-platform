package cn.ohyeah.itvgame.platform.service;

import java.util.Map;

import cn.halcyon.dao.DBException;
import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.service.impl.AuthorizeImpl;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.dao.IAuthorizationDao;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.PurchaseRelation;

public class AuthorizationService {
	private static final IAuthorizationDao authDao;
	private static final AccountService accServ;
	private static final ProductService productServ;
	private static final AuthorizeImpl authImpl;
	
	static {
		authDao = (IAuthorizationDao)BeanManager.getDao("authorizationDao");
		accServ = (AccountService)BeanManager.getBean("accountService");
		productServ = (ProductService)BeanManager.getBean("productService");
		authImpl = (AuthorizeImpl)BeanManager.getAuthorizeImpl();
	}
	
	public Authorization read(int accountId, int productId) {
		try {
			return authDao.read(accountId, productId);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public void save(Authorization auth) {
		try {
			authDao.save(auth);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public void update(Authorization auth) {
		try {
			authDao.update(auth);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public void updateRecharge(Authorization auth) {
		try {
			authDao.updateCoins(auth);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}

	public Authorization checkAuthorization(Account account, 
			ProductDetail productDetail) {
		try {
			return authImpl.checkAuthorization(account, productDetail);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public Authorization checkAuthorization(int accountId, int productId) {
		Account account = accServ.read(accountId);
		if (account == null) {
			throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_ACCOUNT));
		}
		ProductDetail detail = productServ.readProductDetail(productId);
		if (detail == null) {
			throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PRODUCT));
		}
		try {
			return authImpl.checkAuthorization(account, detail);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public Authorization updateAuthorizationAfterTry(Account account, 
			ProductDetail productDetail) {
		try {
			return authImpl.updateAuthorizationAfterTry(account, productDetail);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public Authorization updateAuthorizationAfterSubscribe(Account account, 
			ProductDetail productDetail, PurchaseRelation pr, java.util.Date time){
		try {
			return authImpl.updateAuthorizationAfterSubscribe(account, productDetail, pr, time);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public Authorization updateAuthorizationAfterUnsubscribe(Account account, 
			ProductDetail productDetail) {
		try {
			return authImpl.updateAuthorizationAfterUnsubscribe(account, productDetail);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public Authorization validateExist(Map<String, Object> props, Account account, ProductDetail detail) {
		try {
			Authorization auth = (Authorization)props.get("authorization");
			if (auth == null) {
				auth = checkAuthorization(account, detail);
				props.put("authorization", auth);
			}
			return auth;
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public Authorization validateExist(Account account, ProductDetail detail) {
		try {
			return checkAuthorization(account, detail);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
}
