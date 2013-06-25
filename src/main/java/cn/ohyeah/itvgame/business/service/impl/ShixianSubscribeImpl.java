package cn.ohyeah.itvgame.business.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import cn.ohyeah.itvgame.global.Configuration;

import cn.halcyon.utils.RequestContext;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.BusinessException;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.dao.IPurchaseRelationDao;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.PurchaseRelation;

public class ShixianSubscribeImpl extends AbstractSubscribeImpl {
	//private static final Log log = LogFactory.getLog(ShixianSubscribeImpl.class);
	private static final IPurchaseRelationDao prDao;
	
	static {
		prDao = (IPurchaseRelationDao)BeanManager.getDao("purchaseRelationDao");
	}
	
	public ShixianSubscribeImpl() {
		super("shixian");
	}
	
	protected ShixianSubscribeImpl(String implName) {
		super(implName);
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
		props.put("amount", pr.getAmount());
		props.put("userId", account.getUserId());
		return ShixianSubscribeUtil.consumeCoins(props);
	}
	
	protected ResultInfo expend(Map<String, Object> props, Account account,
			ProductDetail detail, PurchaseRelation pr, String remark) {
		props.put("amount", pr.getAmount());
		props.put("userId", account.getUserId());
		return ShixianSubscribeUtil.consumeCoins(props);
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
		PurchaseRelation pr = prDao.read(detail.getProductId(), "shixian", subType, 0, 0);
		if (pr != null) {
			pr.setValue(period);
			pr.setAmount(amount);
		}
		return pr;
	}
}
