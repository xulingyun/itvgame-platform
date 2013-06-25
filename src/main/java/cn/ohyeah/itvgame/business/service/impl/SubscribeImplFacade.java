package cn.ohyeah.itvgame.business.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.halcyon.utils.RequestContext;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.ISubscribe;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.PurchaseRelation;

public class SubscribeImplFacade implements ISubscribe {
	private static final Log log = LogFactory.getLog(SubscribeImplFacade.class);
	
	private ISubscribe getSubscribeImpl(ProductDetail detail) {
		String impl = Configuration.getSubscribeImplementor();
		return (ISubscribe)BeanManager.getSubscribeImpl(impl);
	}
	
	@Override
	final public int getCashToAmountRatio(ProductDetail detail) {
		if (Configuration.isRechargeManagerGame(detail.getAppName())
                || Configuration.isRechargeManagerPlatform(detail.getAppName())) {
			ISubscribe subImpl = getSubscribeImpl(detail);
			return subImpl.getCashToAmountRatio(detail);
		}
		else {
			return Configuration.getTelcomOperatorCashToAmountRatio();
		}
	}

	@Override
	final public ResultInfo subscribe(Map<String, Object> props, Account account,
			ProductDetail detail, Authorization auth, PurchaseRelation pr,
			String remark, Date time) {
		log.debug("[Subscribe Amount] ==> "+pr.getAmount());
		ISubscribe subImpl = getSubscribeImpl(detail);
		return subImpl.subscribe(props, account, detail, auth, pr, remark, time);
	}

	@Override
	final public ResultInfo subscribeReq(RequestContext rc,
			Map<String, Object> props, String returnUrl, Account account,
			ProductDetail detail, Authorization auth, PurchaseRelation pr,
			Date time) throws IOException {
		log.debug("[Subscribe Amount] ==> "+pr.getAmount());
		ISubscribe subImpl = getSubscribeImpl(detail);
		return subImpl.subscribeReq(rc, props, returnUrl, account, detail, auth, pr, time);
	}

	@Override
	final public ResultInfo subscribeRsp(RequestContext rc,
			Map<String, Object> props, Account account, ProductDetail detail,
			Authorization auth, PurchaseRelation pr, String remark, Date time) {
		ISubscribe subImpl = getSubscribeImpl(detail);
		return subImpl.subscribeRsp(rc, props, account, detail, auth, pr, remark, time);
	}

	@Override
	public String getAmountUnit(ProductDetail detail) {
		if (Configuration.isRechargeManagerGame(detail.getAppName())
                || Configuration.isRechargeManagerPlatform(detail.getAppName())) {
			ISubscribe subImpl = getSubscribeImpl(detail);
			return subImpl.getAmountUnit(detail);
		}
		else {
			return Configuration.getTelcomOperatorAmountUnit();
		}
	}

	@Override
	public PurchaseRelation queryPurchaseRelation(ProductDetail detail, String subscribeType,
			int period, int amount) {
		ISubscribe subImpl = getSubscribeImpl(detail);
		PurchaseRelation pr = subImpl.queryPurchaseRelation(detail, subscribeType, period, amount);
		if (pr != null) {
			log.debug("[PurchaseRelation] ==> implName="+pr.getSubscribeImplementor()+", subType="+pr.getSubscribeType()
					+", value="+pr.getValue()+", amount="+pr.getAmount());
		}
		else {
			log.debug("[PurchaseRelation] ==> 计费ID不存在");
		}
		return pr;
	}

}
