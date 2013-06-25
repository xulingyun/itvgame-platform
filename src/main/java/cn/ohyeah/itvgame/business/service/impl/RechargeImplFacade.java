package cn.ohyeah.itvgame.business.service.impl;

import java.util.Map;

import cn.ohyeah.itvgame.global.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.BusinessException;
import cn.ohyeah.itvgame.business.service.IRecharge;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.ProductDetail;

public class RechargeImplFacade implements IRecharge {
	private static final Log log = LogFactory.getLog(RechargeImplFacade.class);
	
	private IRecharge getRechargeImpl(ProductDetail detail) {
		String impl = Configuration.getRechargeImplementor(detail.getAppName());
		return (IRecharge)BeanManager.getRechargeImpl(impl);
	}
	
	@Override
	public int queryBalance(Map<String, Object> props, Account account, ProductDetail detail, Authorization auth) {
		try {
			IRecharge rechargeImpl = getRechargeImpl(detail);
			return rechargeImpl.queryBalance(props, account, detail, auth);
		}
		catch (Exception e) {
			log.error("[error in queryBalance]", e);
			throw new BusinessException(e);
		}
	}

	@Override
	public ResultInfo recharge(Map<String, Object> props, Account account, ProductDetail detail, Authorization auth, 
			int amount, String remark, java.util.Date time) {
		try {
			log.debug("[Recharge Amount] ==> "+amount);
			IRecharge rechargeImpl = getRechargeImpl(detail);
			return rechargeImpl.recharge(props, account, detail, auth, amount, remark, time);
		}
		catch (Exception e) {
			log.error("[error in recharge]", e);
			throw new BusinessException(e);
		}
	}

	@Override
	public ResultInfo expend(Map<String, Object> props, Account account,
			ProductDetail detail, Authorization auth, int amount) {
		try {
			log.debug("[Expend Amount] ==> "+amount);
			IRecharge rechargeImpl = getRechargeImpl(detail);
			return rechargeImpl.expend(props, account, detail, auth, amount);
		}
		catch (Exception e) {
			log.error("[error in expend]", e);
			throw new BusinessException(e);
		}
	}

	@Override
	public String getAmountUnit(ProductDetail detail) {
		try {
			IRecharge rechargeImpl = getRechargeImpl(detail);
			return rechargeImpl.getAmountUnit(detail);
		}
		catch (Exception e) {
			log.error("[error in getExpendAmountUnit]", e);
			throw new BusinessException(e);
		}
	}

	@Override
	public boolean isSupportRecharge(ProductDetail detail) {
		try {
			IRecharge rechargeImpl = getRechargeImpl(detail);
			return rechargeImpl.isSupportRecharge(detail);
		}
		catch (Exception e) {
			log.error("[error in isSupportRecharge]", e);
			throw new BusinessException(e);
		}
	}

	@Override
	public int getCashToAmountRatio(ProductDetail detail) {
		try {
			IRecharge rechargeImpl = getRechargeImpl(detail);
			return rechargeImpl.getCashToAmountRatio(detail);
		}
		catch (Exception e) {
			log.error("[error in getCashToAmountRatio]", e);
			throw new BusinessException(e);
		}
	}

}
