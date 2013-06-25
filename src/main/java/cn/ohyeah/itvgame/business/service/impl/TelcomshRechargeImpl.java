package cn.ohyeah.itvgame.business.service.impl;

import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.IRecharge;
import cn.ohyeah.itvgame.business.service.ISubscribe;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.dao.IAuthorizationDao;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.PurchaseRelation;

public class TelcomshRechargeImpl implements IRecharge{
	private static final Log log = LogFactory.getLog(TelcomshRechargeImpl.class);
	private static final IAuthorizationDao authDao;
	private static final ISubscribe	subImpl;
	
	static {
		authDao = (IAuthorizationDao)BeanManager.getDao("authorizationDao");
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
		int newAmount = amount;
		pr.setAmount(newAmount);
		log.debug("[Expend Amount] ==> "+newAmount);
		ResultInfo info = new ResultInfo();
		if (auth.getGoldCoin() >= amount) {
			auth.decGoldCoin(amount);
			authDao.updateCoins(auth);
			info.setInfo(auth.getGoldCoin());
		}else{
			info = subImpl.subscribe(props, account, detail, auth, pr, remark, prTime);
			if(info.getInfo() != null && info.getInfo().equals("0")){
				info.setInfo(auth.getGoldCoin());
			}
		}
		return info;
	}

	@Override
	public boolean isSupportRecharge(ProductDetail detail) {
		return false;
	}

	@Override
	public String getAmountUnit(ProductDetail detail) {
		return detail.getAmountUnit();
	}

	@Override
	public int queryBalance(Map<String, Object> props, Account account,
			ProductDetail detail, Authorization auth) {
		return auth.getGoldCoin();
	}

	@Override
	public ResultInfo recharge(Map<String, Object> props, Account account,
			ProductDetail detail, Authorization auth, int amount, String remark, Date time) {
		PurchaseRelation pr = subImpl.queryPurchaseRelation(detail, "recharge", 0, amount);
		if (pr == null) {
			ResultInfo info = new ResultInfo();
			info.setErrorCode(ErrorCode.EC_INVALID_PURCHASE_ID);
			info.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PURCHASE_ID));
			return info;
		}
		ResultInfo info = subImpl.subscribe(props, account, detail, auth, pr, remark, time);
		if (info.isSuccess()) {
			int goldCoin = amount*detail.getRechargeRatio();
			auth.incGoldCoin(goldCoin);
			authDao.updateCoins(auth);
			info.setInfo(goldCoin);
		}
		return info;
	}

	@Override
	public int getCashToAmountRatio(ProductDetail detail) {
		return detail.getRechargeRatio();
	}
}
