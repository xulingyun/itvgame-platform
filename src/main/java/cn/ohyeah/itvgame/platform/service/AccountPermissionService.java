package cn.ohyeah.itvgame.platform.service;

import java.util.Map;

import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.BusinessException;
import cn.ohyeah.itvgame.business.service.impl.PermitImpl;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.AccountPermission;

public class AccountPermissionService {
	private static final PermitImpl permitImpl;
	
	static {
		permitImpl = BeanManager.getPermitImpl();
	}
	
	public AccountPermission read(int accountId) {
		try {
			return permitImpl.read(accountId);
		}
		catch (BusinessException e) {
			throw new ServiceException(e);
		}
	}

	public int updateAfterSubscribe(AccountPermission ap,  
			java.util.Date subTime, int amount) {
		try {
			return permitImpl.updateAfterSubscribe(ap, subTime, amount);
		}
		catch (BusinessException e) {
			throw new ServiceException(e);
		}
	}
	
	public ResultInfo checkSubscribePermission(AccountPermission ap, 
			java.util.Date subTime, int amount) {
		try {
			return permitImpl.checkSubscribePermission(ap, subTime, amount);
		}
		catch (BusinessException e) {
			throw new ServiceException(e);
		}
	}
	
	public AccountPermission validateExist(Map<String, Object> props, Account account) {
		try {
			AccountPermission ap = (AccountPermission)props.get("accountPermission");
			if (ap == null) {
				ap = permitImpl.checkAccountPermission(account);
				props.put("accountPermission", ap);
			}
			return ap;
		}
		catch (BusinessException e) {
			throw new ServiceException(e);
		}
	}
	
	public AccountPermission validateExist(Account account) {
		try {
			return permitImpl.checkAccountPermission(account);
		}
		catch (BusinessException e) {
			throw new ServiceException(e);
		}
	}
}
