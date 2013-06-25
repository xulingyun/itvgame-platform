package cn.ohyeah.itvgame.platform.service;

import java.util.Map;

import cn.halcyon.dao.DBException;
import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.dao.IAccountDao;
import cn.ohyeah.itvgame.platform.model.Account;

public class AccountService {
	private static final IAccountDao accDao;
	
	static {
		accDao = (IAccountDao)BeanManager.getDao("accountDao");
	}

	public Account read(int accountId) {
		try {
			return accDao.readByAccountId(accountId);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public Account read(String userId) {
		try {
			return accDao.readByUserId(userId);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public void save(Account account) {
		try {
			accDao.save(account);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public Account addNewStbUser(String userId) {
		try {
			Account account = new Account();
			account.setUserId(userId);
			account.setPrivilegeMember();
			account.setCreateTime(new java.util.Date());
			account.setUpdateTime(account.getCreateTime());
			account.setStateActivation();
			accDao.save(account);
			return account;
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public Account userLogin(String userId) {
		Account account = read(userId);
		if (account == null) {
			account = addNewStbUser(userId);
		}
		return account;
	}
	
	public void update(Account account) {
		try {
			accDao.update(account);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public void updateRecharge(Account account) {
		try {
			accDao.updateCoins(account);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public Account validateExist(Map<String, Object> props, int accountId) {
		Account account = (Account)props.get("account");
		if (account == null) {
			account = read(accountId);
			if (account == null) {
				throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_ACCOUNT));
			}
			props.put("account", account);
		}
		return account;
	}
	
	public Account validateExist(int accountId) {
		Account account = read(accountId);
		if (account == null) {
			throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_ACCOUNT));
		}
		return account;
	}
}
