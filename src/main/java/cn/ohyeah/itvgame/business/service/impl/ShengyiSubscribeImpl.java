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
import cn.ohyeah.itvgame.utils.DateUtil;
import cn.ohyeah.itvgame.utils.ToolUtil;

public class ShengyiSubscribeImpl extends AbstractSubscribeImpl {
	private static final Log log = LogFactory.getLog(ShengyiSubscribeImpl.class);
	private static final IPurchaseRelationDao prDao;
	
	static {
		prDao = (IPurchaseRelationDao)BeanManager.getDao("purchaseRelationDao");
	}

	public ShengyiSubscribeImpl() {
		super("shengyi");
	}
	
	protected ShengyiSubscribeImpl(String implName) {
		super(implName);
	}

	@Override
	public PurchaseRelation queryPurchaseRelation(ProductDetail detail,
			String subscribeType, int period, int amount) {
		PurchaseRelation pr = null;
		String subType = subscribeType;
		if ("recharge".equals(subscribeType)
                && (Configuration.isRechargeManagerGame(detail.getAppName())
                || Configuration.isRechargeManagerPlatform(detail.getAppName()))) {
			subType = "expend";
			pr = prDao.read(detail.getProductId(), "shengyi", subType, 0, 0);
		}else if("period".equals(subscribeType)
                && (Configuration.isRechargeManagerGame(detail.getAppName())
                || Configuration.isRechargeManagerPlatform(detail.getAppName()))){
			pr = prDao.read(detail.getProductId(), "shengyi", subType, period, amount);
		}else{
			pr = prDao.read(detail.getProductId(), "shengyi", subType, 0, 0);
		}
		
		log.info("subType:"+subType);
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
		
		String str = "";
		if(props.get("subscribe")!=null){
			str = (String)props.get("subscribe");
		}
		if ("recharge".equalsIgnoreCase(pr.getSubscribeType()) || str.equals("subscribeProduct")) {
			return subscribeProuduct(props, account, detail, pr, remark);
		}
		else {
			return expend(props, account, detail, pr, remark);
		}
	}

	protected ResultInfo subscribeProuduct(Map<String, Object> props, Account account,
			ProductDetail detail, PurchaseRelation pr, String remark) {
		String userId = account.getUserId();
		int amount = pr.getAmount();
		//String buyUrl = (String) props.get("buyURL");
		String userToken = (String) props.get("userToken");
		//String checkKey = (String) props.get("checkKey");
		String shengyiCPID = (String) props.get("shengyiCPID");
		String shengyiCPPassWord = (String) props.get("shengyiCPPassWord");
		String shengyiUserIdType = (String) props.get("shengyiUserIdType");
		String shengyiProductId = (String) props.get("shengyiProductId");
		//String passWd = (String) props.get("password");
		String timeStamp = DateUtil.createTimeId(DateUtil.PATTERN_DEFAULT);
		String transactionID = shengyiCPID + timeStamp + ToolUtil.getAutoincrementValue();
		ProductDetail product = (ProductDetail) props.get("productDetail");
		String rechargeDesc = product.getProductName()+"游戏包月";
		System.out.println("timeStamp:"+timeStamp);
		System.out.println("transactionID:"+transactionID);
		System.out.println("rechargeDesc:"+rechargeDesc);
		return ShengyiSubscribeUtil.consumeCoins(userId, userToken, amount, shengyiCPID, shengyiCPPassWord, 
				shengyiUserIdType, shengyiProductId, timeStamp, transactionID, rechargeDesc);
	}
	

	protected ResultInfo expend(Map<String, Object> props, Account account,
			ProductDetail detail, PurchaseRelation pr, String remark) {
		
		String userId = account.getUserId();
		int amount = pr.getAmount();
		//String buyUrl = (String) props.get("buyURL");
		String userToken = (String) props.get("userToken");
		//String checkKey = (String) props.get("checkKey");
		String shengyiCPID = (String) props.get("shengyiCPID");
		String shengyiCPPassWord = (String) props.get("shengyiCPPassWord");
		String shengyiUserIdType = (String) props.get("shengyiUserIdType");
		String shengyiProductId = (String) props.get("shengyiProductId");
		String passWd = (String) props.get("password");
		String timeStamp = DateUtil.createTimeId(DateUtil.PATTERN_DEFAULT);
		String transactionID = shengyiCPID + timeStamp + ToolUtil.getAutoincrementValue();
		ProductDetail product = (ProductDetail) props.get("productDetail");
		String rechargeDesc = product.getProductName()+"游戏充值";
		System.out.println("timeStamp:"+timeStamp);
		System.out.println("transactionID:"+transactionID);
		System.out.println("passWd:"+passWd);
		System.out.println("rechargeDesc:"+rechargeDesc);
		ResultInfo info = ShengyiSubscribeUtil.judgAccount(userId, userToken, timeStamp, transactionID, shengyiCPID, shengyiCPPassWord);
		ResultInfo info2;
		if(info.getInfo()!=null && info.getInfo().equals("0")){
			info2 = ShengyiSubscribeUtil.checkPassword(userId, userToken, timeStamp, transactionID, passWd, shengyiCPID, shengyiCPPassWord);
			if(info2.getInfo()!=null && info2.getInfo().equals("0")){
				return ShengyiSubscribeUtil.consumeCoins(userId, userToken, amount, shengyiCPID, shengyiCPPassWord,
						shengyiUserIdType, shengyiProductId, timeStamp, transactionID, rechargeDesc);
			}else{
				return info2;
			}
		}else{
			return ShengyiSubscribeUtil.consumeCoins(userId, userToken, amount, shengyiCPID, shengyiCPPassWord, 
					shengyiUserIdType, shengyiProductId, timeStamp, transactionID, rechargeDesc);
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
	
}
