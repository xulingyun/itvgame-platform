package cn.ohyeah.itvgame.business.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import cn.ohyeah.itvgame.global.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.halcyon.utils.RequestContext;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.BusinessException;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.dao.IPurchaseRelationDao;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.PurchaseRelation;

public class DijoySubscribeImpl extends AbstractSubscribeImpl {
	private static final Log log = LogFactory.getLog(DijoySubscribeImpl.class);
	private static final IPurchaseRelationDao prDao;
	
	static {
		prDao = (IPurchaseRelationDao)BeanManager.getDao("purchaseRelationDao");
	}

	public DijoySubscribeImpl() {
		super("dijoy");
	}
	
	protected DijoySubscribeImpl(String implName) {
		super(implName);
	}

	@Override
	public PurchaseRelation queryPurchaseRelation(ProductDetail detail,
			String subscribeType, int period, int amount) {
		String subType = subscribeType;
		if ("recharge".equals(subscribeType)
                && (Configuration.isRechargeManagerGame(detail.getAppName())
                || Configuration.isRechargeManagerPlatform(detail.getAppName()))) {
			subType = "expend";
		}
		log.info("subType:"+subType);
		PurchaseRelation pr = prDao.read(detail.getProductId(), "dijoy", subType, 0, 0);
		if (pr != null) {
			pr.setValue(period);
			pr.setAmount(amount);
		}
		return pr;
	}

	@Override
	public ResultInfo subscribeAction(Map<String, Object> props,
			Account account, ProductDetail detail, PurchaseRelation pr,
			String remark, Date time) {
		if ("recharge".equalsIgnoreCase(pr.getSubscribeType())) {
			return recharge(props, account, detail, pr, remark);
		}
		else {
			return expend(props, account, detail, pr, remark);
		}
	}

	protected ResultInfo recharge(Map<String, Object> props, Account account,
			ProductDetail detail, PurchaseRelation pr, String remark) {
		throw new BusinessException("not supported");
	}
	

	protected ResultInfo expend(Map<String, Object> props, Account account,
			ProductDetail detail, PurchaseRelation pr, String remark) {
		String userId = account.getUserId();
		String appId = (String) props.get("appId");
		int feeCode = (Integer) props.get("feeCode");
		int number = 1; 
		//int amount = pr.getAmount();
		String returnUrl = "";
		String notifyUrl = "";
		String buyUrl = (String) props.get("buyURL");
		String platformExt = (String) props.get("platformExt");
		String appExt = "appExt";
		String payKey = (String) props.get("checkKey");
		return DijoySubscribeUtil.consumeCoins(userId, appId, number, feeCode, returnUrl, notifyUrl, platformExt, appExt, payKey, buyUrl);
	}
	
	@Override
	public ResultInfo subscribeReqAction(RequestContext rc,
			Map<String, Object> props, String returnUrl, Account account,
			ProductDetail detail, PurchaseRelation pr, Date time)
			throws IOException {
		throw new BusinessException("not supported");
	}

	@Override
	public ResultInfo subscribeRspAction(RequestContext rc,
			Map<String, Object> props, Account account, ProductDetail detail,
			PurchaseRelation pr, String remark, Date time) {
		throw new BusinessException("not supported");
	}
	
	/*private int convertFeecode(int amount){
		for(int i=0;i<amounts.length;i++){
			int a = Integer.parseInt(amounts[i]);
			if(amount==a){
				return Integer.parseInt(feecodes[i]);
			}
		}
		return -1;
	}*/

}
