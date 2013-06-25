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

public class The9SubscribeImpl extends AbstractSubscribeImpl{
	private static final Log log = LogFactory.getLog(The9SubscribeImpl.class);
	private static final IPurchaseRelationDao prDao;
	
	static {
		prDao = (IPurchaseRelationDao)BeanManager.getDao("purchaseRelationDao");
	}
	
	public The9SubscribeImpl() {
		super("the9");
	}
	
	protected The9SubscribeImpl(String implName) {
		super(implName);
	}
	
	@Override
	public ResultInfo subscribeAction(Map<String, Object> props,
			Account account, ProductDetail detail, PurchaseRelation pr,
			String remark, Date time) {
		String accountName = (String)props.get("accountName");
		int costAmount = pr.getAmount()*getCashToAmountRatio(detail);
		log.debug("[Subscribe Amount] ==> "+costAmount);
		
		if (account.isPrivilegeSuperUser()) {
			log.debug("≤‚ ‘’À∫≈∂©π∫[userId="+account.getUserId()+", amount="+costAmount+", subImpl="+getImplementorName()+"]");
			ResultInfo info = new ResultInfo();
			info.setInfo(costAmount);
			return info;
		}
		else {
			return The9SubscribeUtil.subscribe(account.getUserId(), accountName, costAmount, pr.getSubscribeId(), remark);
		}
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

	@Override
	public PurchaseRelation queryPurchaseRelation(ProductDetail detail,
			String subscribeType, int period, int amount) {
		String subType = subscribeType;
		if ("recharge".equals(subscribeType)
                && (Configuration.isRechargeManagerGame(detail.getAppName())
                || Configuration.isRechargeManagerPlatform(detail.getAppName()))) {
			subType = "expend";
		}
		PurchaseRelation pr = prDao.read(detail.getProductId(), getImplementorName(), subType, 0, 0);
		if (pr != null) {
			pr.setValue(period);
			pr.setAmount(amount);
		}
		return pr;
	}
	
}
