package cn.ohyeah.itvgame.business.service.impl;

import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.BusinessException;
import cn.ohyeah.itvgame.business.service.IRecharge;
import cn.ohyeah.itvgame.business.service.ISubscribe;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.PurchaseRelation;

public class DijoyRechargeImpl implements IRecharge {
	private static final Log log = LogFactory.getLog(DijoyRechargeImpl.class);
	private static final ISubscribe subImpl;
	private static final String amountUnit;
	private static final int cashToAmountRatio;
	
	static  {
		subImpl = BeanManager.getSubscribeImplFacade();
		amountUnit = Configuration.getAmountUnit("dijoy");
		cashToAmountRatio = Configuration.getCashToAmountRatio("dijoy");
	}

	@Override
	public ResultInfo recharge(Map<String, Object> props, Account account,
			ProductDetail detail, Authorization auth, int amount,
			String remark, Date time) {
		throw new BusinessException("not supported");
	}

	@Override
	public int queryBalance(Map<String, Object> props, Account account,
			ProductDetail detail, Authorization auth) {
		return 0;
	}

	@Override
	public ResultInfo expend(Map<String, Object> props, Account account,
			ProductDetail detail, Authorization auth, int amount) {
		String remark = (String)props.get("remark");
		java.util.Date prTime = (java.util.Date)props.get("purchaseTime");
		PurchaseRelation pr = subImpl.queryPurchaseRelation(detail, "expend", 0, 0);
		if (pr == null) {
			ResultInfo info = new ResultInfo();
			info.setErrorCode(ErrorCode.EC_INVALID_PURCHASE_ID);
			info.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PURCHASE_ID));
			return info;
		}
		props.put("purchaseRelation", pr);
		int newAmount = amount;
		pr.setAmount(newAmount);
		log.debug("[Expend Amount] ==> "+newAmount);
		return subImpl.subscribe(props, account, detail, auth, pr, remark, prTime);
	}

	@Override
	public boolean isSupportRecharge(ProductDetail detail) {
		return false;
	}

	@Override
	public String getAmountUnit(ProductDetail detail) {
		return amountUnit;
	}

	@Override
	public int getCashToAmountRatio(ProductDetail detail) {
		return cashToAmountRatio;
	}

}
