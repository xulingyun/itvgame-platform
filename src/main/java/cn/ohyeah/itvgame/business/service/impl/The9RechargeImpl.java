package cn.ohyeah.itvgame.business.service.impl;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.ISubscribe;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.PurchaseRelation;

public class The9RechargeImpl extends NullRechargeImpl {
	private static final Log log = LogFactory.getLog(The9RechargeImpl.class);
	private static final ISubscribe	subImpl;
	
	static  {
		subImpl = BeanManager.getSubscribeImplFacade();
	}
	
	@Override
	public ResultInfo expend(Map<String, Object> props, Account account, ProductDetail detail, Authorization auth, int amount) {
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
		int newAmount = amount/detail.getRechargeRatio();
		pr.setAmount(newAmount);
		log.debug("[Expend Amount] ==> "+newAmount);
		return subImpl.subscribe(props, account, detail, auth, pr, remark, prTime);
	}

	@Override
	public int queryBalance(Map<String, Object> props, Account account, ProductDetail detail, Authorization auth) {
		String accountName = (String)props.get("accountName");
		if (account.isPrivilegeSuperUser()) {
			int balance = 1000;
			log.debug("≤‚ ‘’À∫≈”‡∂Ó[userId="+account.getUserId()+", balance="+balance+", rechargeImpl=the9]");
			return balance;
		}
		else {
			return The9SubscribeUtil.queryBalance(accountName);
		}
	}

}
