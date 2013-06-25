package cn.ohyeah.itvgame.business.service.impl;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ohyeah.itvgame.business.service.BusinessException;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.dao.IAuthorizationDao;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.PurchaseRelation;

public class AuthorizeImpl {
	private static final Log log = LogFactory.getLog(AuthorizeImpl.class);
	private static final IAuthorizationDao authDao= (IAuthorizationDao)BeanManager.getDao("authorizationDao");
	
	private Authorization initAuthorization(Authorization auth, 
			Account account, ProductDetail productDetail) {
		try {
			auth.setAccountId(account.getAccountId());
			auth.setProductId(productDetail.getProductId());
			auth.setLeftTryNumber(productDetail.getTryNumber());
			if (productDetail.isPurchaseTypeFree()) {
				auth.setAuthorizationFree();
			}
			else {
				if (auth.getLeftTryNumber()>0) {
					auth.setAuthorizationTry();
				}
				else {
					auth.setAuthorizationInvalid();
				}
			}
			return auth;
		}
		catch (Exception e) {
			log.error("[error in initAuthorization]", e);
			throw new BusinessException(e);
		}
	}
	
	private boolean updateAuthorization(Authorization auth, 
			Account account, ProductDetail productDetail) {
		try {
			boolean needUpdate = false;
			if (auth.isAuthorizationInvalid()) {
				if (productDetail.isPurchaseTypeFree()) {
					auth.setAuthorizationFree();
					needUpdate = true;
				}
				else {
					if (auth.getLeftTryNumber() > 0) {
						auth.setAuthorizationTry();
						needUpdate = true;
					}
				}
			}
			else if (auth.isAuthorizationFree()) {
				if (!productDetail.isPurchaseTypeFree()) {
					needUpdate = true;
					if (auth.getLeftTryNumber() > 0) {
						auth.setAuthorizationTry();
					}
					else {
						auth.setAuthorizationInvalid();
					}
				}
			}
			else if (auth.isAuthorizationTry()) {
				if (auth.getLeftTryNumber() <= 0) {
					needUpdate = true;
					if (productDetail.isPurchaseTypeFree()) {
						auth.setAuthorizationFree();
					}
					else {
						auth.setAuthorizationInvalid();
					}
				}
			}
			else if (auth.isAuthorizationCount()) {
				if (auth.getLeftValidCount() <= 0) {
					needUpdate = true;
					if (productDetail.isPurchaseTypeFree()) {
						auth.setAuthorizationFree();
					}
					else {
						if (auth.getLeftTryNumber() > 0) {
							auth.setAuthorizationTry();
						}
						else {
							auth.setAuthorizationInvalid();
						}
					}
				}
			}
			else if (auth.isAuthorizationTime()) {
				if (auth.getLeftValidSeconds() <= 0) {
					needUpdate = true;
					if (productDetail.isPurchaseTypeFree()) {
						auth.setAuthorizationFree();
					}
					else {
						if (auth.getLeftTryNumber() > 0) {
							auth.setAuthorizationTry();
						}
						else {
							auth.setAuthorizationInvalid();
						}
					}
				}
			}
			else if (auth.isAuthorizationPeriod()) {
				if (auth.getAuthorizationEndTime() != null
						&& auth.getAuthorizationEndTime().before(new java.util.Date())) {
					needUpdate = true;
					if (productDetail.isPurchaseTypeFree()) {
						auth.setAuthorizationFree();
					}
					else {
						if (auth.getLeftTryNumber() > 0) {
							auth.setAuthorizationTry();
						}
						else {
							auth.setAuthorizationInvalid();
						}
					}
				}
			}
			else {
			}
			return needUpdate;
		}
		catch (Exception e) {
			log.error("[error in updateAuthorization]", e);
			throw new BusinessException(e);
		}
	}
	
	public Authorization checkAuthorization(Account account, ProductDetail productDetail) {
		try {
			Authorization auth = authDao.read(account.getAccountId(), productDetail.getProductId());
			if (auth == null) {
				auth = new Authorization();
				initAuthorization(auth, account, productDetail);
				authDao.save(auth);
			}
			else {
				if (updateAuthorization(auth, account, productDetail)) {
					authDao.update(auth);
				}
			}
			return auth;
		}
		catch (Exception e) {
			log.error("[error in checkAuthorization]", e);
			throw new BusinessException(e);
		}
	}
	
	public Authorization updateAuthorizationAfterSubscribe(Account account, 
			ProductDetail productDetail, PurchaseRelation pr, java.util.Date date) {
		try {
			Authorization auth = authDao.read(account.getAccountId(), productDetail.getProductId());
			String subscribeType = pr.getSubscribeType(); 
			if (ProductDetail.PURCHASE_PERIOD.equalsIgnoreCase(subscribeType)) {
				auth.setAuthorizationPeriod();
				auth.setAuthorizationStartTime(date);
				auth.setAuthorizationEndTime(DateUtils.addSeconds(date, pr.getValue()));
			}
			else if (ProductDetail.PURCHASE_TIME.equalsIgnoreCase(subscribeType)) {
				auth.setAuthorizationTime();
				auth.setLeftValidSeconds(pr.getValue());
			}
			else if (ProductDetail.PURCHASE_COUNT.equalsIgnoreCase(subscribeType)) {
				auth.setAuthorizationCount();
				auth.setLeftValidCount(pr.getValue());
			}
			else if (subscribeType == ProductDetail.PURCHASE_MONTH) {
				auth.setAuthorizationMonth();
				auth.setAuthorizationStartTime(date);
				auth.setAuthorizationEndTime(null);
			}
			authDao.update(auth);
			return auth;
		}
		catch (Exception e) {
			log.error("[error in updateAuthorizationAfterSubscribe]", e);
			throw new BusinessException(e);
		}
	}

	public Authorization updateAuthorizationAfterUnsubscribe(Account account, 
			ProductDetail productDetail) {
		try {
		Authorization auth = authDao.read(account.getAccountId(), productDetail.getProductId());
		auth.setAuthorizationInvalid();
		authDao.update(auth);
		return auth;
		}
		catch (Exception e) {
			log.error("[error in updateAuthorizationAfterUnsubscribe]", e);
			throw new BusinessException(e);
		}
	}

	public Authorization updateAuthorizationAfterTry(Account account,
			ProductDetail productDetail) {
		try {
			Authorization auth = authDao.read(account.getAccountId(), productDetail.getProductId());
			if (auth.isAuthorizationTry()) {
				int leftTryNumber = auth.getLeftTryNumber();
				if (leftTryNumber > 0) {
					--leftTryNumber;
					if (leftTryNumber > 0) {
						auth.setLeftTryNumber(leftTryNumber);
					}
					else {
						auth.setLeftTryNumber(0);
						auth.setAuthorizationInvalid();
					}
				}
				else {
					auth.setLeftTryNumber(0);
					auth.setAuthorizationInvalid();
				}
			}
			authDao.update(auth);
			return auth;
		}
		catch (Exception e) {
			log.error("[error in updateAuthorizationAfterTry]", e);
			throw new BusinessException(e);
		}
	}
}
