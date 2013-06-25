package cn.ohyeah.itvgame.business.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.halcyon.utils.RequestContext;
import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.dao.IPurchaseRelationDao;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.PurchaseRelation;

public class TelcomshSubscribeImpl extends AbstractSubscribeImpl {
	private static final Log log = LogFactory.getLog(TelcomshSubscribeImpl.class);
	private static final IPurchaseRelationDao prDao;
	
	static {
		prDao = (IPurchaseRelationDao)BeanManager.getDao("purchaseRelationDao");
	}
	
	public TelcomshSubscribeImpl() {
		super("telcomsh");
	}
	
	protected TelcomshSubscribeImpl(String implName) {
		super(implName);
	}
	
	@Override
	public ResultInfo subscribeAction(Map<String, Object> props,
			Account account, ProductDetail detail, PurchaseRelation pr,
			String remark, Date time) {
		log.debug("[Subscribe Amount] ==> " + pr.getAmount());
		String userToken = (String)props.get("userToken");
		
		if (account.isPrivilegeSuperUser()) {
			log.debug("≤‚ ‘’À∫≈∂©π∫[userId="+account.getUserId()+", amount="+ pr.getAmount()+", subImpl="+getImplementorName()+"]");
			return new ResultInfo();
		}
		else {
			return TelcomshSubscribeUtil.subscribe(account.getUserId(), pr.getSubscribeId(), userToken, 
					Configuration.getSpid(), Integer.toString((Integer)props.get("payType")));
		}
	}

	@Override
	public ResultInfo subscribeReqAction(RequestContext rc,
			Map<String, Object> props, String returnUrl, Account account,
			ProductDetail detail, PurchaseRelation pr, Date time)
			throws IOException {
		log.debug("[Subscribe Amount] ==> " + pr.getAmount());
		ResultInfo info = new ResultInfo();
		String userToken = (String)props.get("userToken");
		String subUrl = Configuration.formatSubscribeUrl(rc, pr.getSubscribeImplementor(), 
				account.getUserId(), pr.getSubscribeId(), userToken, returnUrl);
		rc.redirect(subUrl);
		return info;
	}

	@Override
	public ResultInfo subscribeRspAction(RequestContext rc,
			Map<String, Object> props, Account account, ProductDetail detail,
			PurchaseRelation pr, String remark, Date time) {
		ResultInfo info = new ResultInfo();
		
		int result = -1;
		Enumeration<String> params = rc.params();
		while (params.hasMoreElements()) {
			String name = params.nextElement();
			if ("result".equalsIgnoreCase(name)) {
				result = rc.param(name, -1);
			}
		}
		if (result != 0) {
			info.setErrorCode(ErrorCode.EC_SUBSCRIBE_FAILED);
			info.setMessage(TelcomshSubscribeUtil.getErrorMessage(result));
		}
		return info;
	}

	@Override
	public PurchaseRelation queryPurchaseRelation(ProductDetail detail,
			String subscribeType, int period, int amount) {
		return prDao.read(detail.getProductId(), "telcomsh", subscribeType, period, amount);
	}
}
