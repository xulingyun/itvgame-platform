package cn.ohyeah.itvgame.business.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.BusinessException;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.dao.IAccountPermissionDao;
import cn.ohyeah.itvgame.platform.dao.IProductPermissionDao;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.AccountPermission;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.ProductPermission;
import cn.ohyeah.itvgame.platform.model.SubscribeRecord;

public class PermitImpl {
	private static final Log log = LogFactory.getLog(PermitImpl.class);
	private static final IAccountPermissionDao apDao;
	private static final IProductPermissionDao ppDao;
	
	static {
		apDao = (IAccountPermissionDao)BeanManager.getDao("accountPermissionDao");
		ppDao = (IProductPermissionDao)BeanManager.getDao("productPermissionDao");
	}
	
	public AccountPermission read(int accountId) {
		try {
			return apDao.read(accountId);
		}
		catch (Exception e) {
			log.error("[error in read]", e);
			throw new BusinessException(e);
		}
	}
	
	private AccountPermission createAccountPermission(Account account) {
		AccountPermission ap = new AccountPermission();
		ap.setAccessPermissionValid();
		ap.setSubscribePermissionValid();
		ap.setAccountId(account.getAccountId());
		ap.setDaySubscribeLimit(Configuration.getDaySubscribeLimit());
		ap.setMonthSubscribeLimit(Configuration.getMonthSubscribeLimit());
		return ap;
	}
	
	public AccountPermission checkAccountPermission(Account account) {
		try {
			AccountPermission ap = apDao.read(account.getAccountId());
			if (ap == null) {
				ap = createAccountPermission(account);
				apDao.save(ap);
			}
			return ap;
		}
		catch (Exception e) {
			log.error("[error in checkAccountPermission]", e);
			throw new BusinessException(e);
		}
	}

	public int updateAfterSubscribe(AccountPermission ap,  
		java.util.Date subTime, int amount) {
		try {
			int subCmd = SubscribeRecord.SUBSCRIBE_COMMAND_SUB;
			if (ap.getTotalSubscribeAmount() <= 0) {
				subCmd = SubscribeRecord.SUBSCRIBE_COMMAND_NEWSUB;
			}
			
			boolean isSameDay = ap.isSubscribeSameDay(subTime);
			boolean isSameMonth = ap.isSubscribeSameMonth(subTime);
			ap.setLastSubscribeTime(subTime);
			if (!isSameDay) {
				ap.setDaySubscribeAmount(amount);
			}
			else {
				ap.incDaySubscribeAmount(amount);
			}
			
			if (!isSameMonth) {
				ap.setMonthSubscribeAmount(amount);
			}
			else {
				ap.incMonthSubscribeAmount(amount);
			}
			ap.incTotalSubscribeAmount(amount);
			apDao.updateAfterSubscribe(ap);
			return subCmd;
		}
		catch (Exception e) {
			log.error("[error in updateAfterSubscribe]", e);
			throw new BusinessException(e);
		}
	}
	
	public ResultInfo checkSubscribePermission(AccountPermission ap, 
			java.util.Date subTime, int amount) {
		try {
			ResultInfo info = new ResultInfo();
			if (ap.isSubscribePermissionValid()) {
				if (Configuration.isSupportSubscribeLimit()) {
					boolean isSameDay = ap.isSubscribeSameDay(subTime);
					boolean isSameMonth = ap.isSubscribeSameMonth(subTime);
					if (isSameDay && ap.willExceedDayLimit(ap.getDaySubscribeLimit(), amount)) {
						info.setErrorCode(ErrorCode.EC_REACH_ACCOUNT_SUB_DAY_LIMIT);
						info.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_REACH_ACCOUNT_SUB_DAY_LIMIT));
					}
					else if (isSameMonth && ap.willExceedMonthLimit(ap.getMonthSubscribeLimit(), amount)) {
						info.setErrorCode(ErrorCode.EC_REACH_ACCOUNT_SUB_MONTH_LIMIT);
						info.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_REACH_ACCOUNT_SUB_MONTH_LIMIT));
					}
					else {
						//null
					}
				}
			}
			else {
				info.setErrorCode(ErrorCode.EC_PRODUCT_SUB_PERMISSION_DENY);
				info.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_PRODUCT_SUB_PERMISSION_DENY));
			}
			return info;
		}
		catch (Exception e) {
			log.error("[error in checkSubscribePermission]", e);
			throw new BusinessException(e);
		}
	}
	
	public ProductPermission checkProductPermission(Account account, ProductDetail detail) {
		try {
			ProductPermission pp = ppDao.read(account.getAccountId(), detail.getProductId());
			if (pp == null) {
				pp = createProductPermission(account, detail);
				ppDao.save(pp);
			}
			return pp;
		}
		catch (Exception e) {
			log.error("[error in checkProductPermission]", e);
			throw new BusinessException(e);
		}
	}
	
	public ProductPermission read(int accountId, int productId) {
		try {
			return ppDao.read(accountId, productId);
		}
		catch (Exception e) {
			log.error("[error in read]", e);
			throw new BusinessException(e);
		}
	}

	private ProductPermission createProductPermission(Account account,
			ProductDetail detail) {
		ProductPermission pp = new ProductPermission();
		pp.setAccountId(account.getAccountId());
		pp.setProductId(detail.getProductId());
		pp.setDaySubscribeLimit(detail.getDaySubscribeLimit());
		pp.setMonthSubscribeLimit(detail.getMonthSubscribeLimit());
		pp.setAccessPermissionValid();
		pp.setSubscribePermissionValid();
		return pp;
	}
	
	public int updateAfterSubscribe( ProductPermission pp,
			java.util.Date subTime, int amount) {
		try {
			int subCmd = SubscribeRecord.SUBSCRIBE_COMMAND_SUB;
			if (pp.getTotalSubscribeAmount() <= 0) {
				subCmd = SubscribeRecord.SUBSCRIBE_COMMAND_NEWSUB;
			}
			boolean isSameDay = pp.isSubscribeSameDay(subTime);
			boolean isSameMonth = pp.isSubscribeSameMonth(subTime);
			pp.setLastSubscribeTime(subTime);
			if (!isSameDay) {
				pp.setDaySubscribeAmount(amount);
			}
			else {
				pp.incDaySubscribeAmount(amount);
			}
			
			if (!isSameMonth) {
				pp.setMonthSubscribeAmount(amount);
			}
			else {
				pp.incMonthSubscribeAmount(amount);
			}
			pp.incTotalSubscribeAmount(amount);
			ppDao.updateAfterSubscribe(pp);
			return subCmd;
		}
		catch (Exception e) {
			log.error("[error in updateAfterSubscribe]", e);
			throw new BusinessException(e);
		}
	}
	
	public ResultInfo checkSubscribePermission(ProductPermission pp, 
			ProductDetail detail, java.util.Date subTime, int amount) {
		try {
			ResultInfo info = new ResultInfo();
			if (pp.isSubscribePermissionValid()) {
				if (Configuration.isSupportSubscribeLimit()) {
					boolean isSameDay = pp.isSubscribeSameDay(subTime);
					boolean isSameMonth = pp.isSubscribeSameMonth(subTime);
					if (isSameDay && pp.willExceedDayLimit(detail.getDaySubscribeLimit(), amount)) {
						info.setErrorCode(ErrorCode.EC_REACH_PRODUCT_SUB_DAY_LIMIT);
						info.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_REACH_PRODUCT_SUB_DAY_LIMIT));
					}
					else if (isSameMonth && pp.willExceedMonthLimit(detail.getMonthSubscribeLimit(), amount)) {
						info.setErrorCode(ErrorCode.EC_REACH_PRODUCT_SUB_MONTH_LIMIT);
						info.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_REACH_PRODUCT_SUB_MONTH_LIMIT));
					}
					else {
						//null
					}
				}
			}
			else {
				info.setErrorCode(ErrorCode.EC_PRODUCT_SUB_PERMISSION_DENY);
				info.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_PRODUCT_SUB_PERMISSION_DENY));
			}
			return info;
		}
		catch (Exception e) {
			log.error("[error in checkSubscribePermission]", e);
			throw new BusinessException(e);
		}
	}
}
