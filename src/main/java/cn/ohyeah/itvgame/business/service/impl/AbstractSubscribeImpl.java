package cn.ohyeah.itvgame.business.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.halcyon.utils.RequestContext;
import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.ISubscribe;
import cn.ohyeah.itvgame.business.service.SubscribeException;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.dao.ISubscribeRecordDao;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.AccountPermission;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.ProductPermission;
import cn.ohyeah.itvgame.platform.model.PurchaseRelation;
import cn.ohyeah.itvgame.platform.model.SubscribePayType;
import cn.ohyeah.itvgame.platform.model.SubscribeRecord;

public abstract class AbstractSubscribeImpl implements ISubscribe {
	private static final Log log = LogFactory.getLog(AbstractSubscribeImpl.class);
	private static final ISubscribeRecordDao srDao;
	private static final PermitImpl permitImpl;
	private String implementorName;
	private int cashToAmountRatio;
	private String amountUnit;
	
	static {
		srDao = (ISubscribeRecordDao)BeanManager.getDao("subscribeRecordDao");
		permitImpl = (PermitImpl)BeanManager.getBean("permitImpl");
	}
	
	protected AbstractSubscribeImpl(String implName) {
		implementorName = implName;
		cashToAmountRatio = Configuration.getCashToAmountRatio(implName);
		amountUnit = Configuration.getAmountUnit(implName);
	}
	
	abstract public ResultInfo subscribeAction(Map<String, Object> props, Account account,
			ProductDetail detail, PurchaseRelation pr, String remark, Date time);
	
	@Override
	final public ResultInfo subscribe(Map<String, Object> props, Account account,
			ProductDetail detail, Authorization auth, PurchaseRelation pr, String remark, Date time) {
		String ip = (String)props.get("ip");
		int amount = pr.getAmount();
		int payType = SubscribePayType.PAY_TYPE_BILL;
		try {
			payType = (Integer)props.get("payType");
		}
		catch (Exception e) {
			//null
		}
		AccountPermission ap = (AccountPermission)props.get("accountPermission");
		ResultInfo info = permitImpl.checkSubscribePermission(ap, time, amount);
		if (info.isSuccess()) {
			String subscribeId = pr.getSubscribeId();
			ProductPermission pp = (ProductPermission)props.get("productPermission");
			info = permitImpl.checkSubscribePermission(pp, detail, time, amount);
			if (info.isSuccess()) {
				if (StringUtils.isNotEmpty(subscribeId)) {
					try {
						info = subscribeAction(props, account, detail, pr, remark, time);
					}
					catch (SubscribeException e) {
						log.error("¶©¹ºÊ§°Ü", e);
						info.setErrorCode(ErrorCode.EC_SUBSCRIBE_FAILED);
						info.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SUBSCRIBE_FAILED));
					}
				}
				else {
					info.setErrorCode(ErrorCode.EC_INVALID_SUBSCRIBE_ID);
					info.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_SUBSCRIBE_ID));
				}
				
				if (info.isSuccess()) {
					int productSubCmd = permitImpl.updateAfterSubscribe(pp, time, amount);
					int accountSubCmd = permitImpl.updateAfterSubscribe(ap, time, amount);
					if (account.isPrivilegeMember()) {
						SubscribeRecord sr = new SubscribeRecord();
						sr.setAccountId(account.getAccountId());
						sr.setUserId(account.getUserId());
						sr.setProductId(detail.getProductId());
						sr.setProductName(detail.getProductName());
						sr.setSubscribeImplementor(pr.getSubscribeImplementor());
						sr.setSubscribeType(pr.getSubscribeType());
						sr.setAccountSubscribeCommand(accountSubCmd);
						sr.setProductSubscribeCommand(productSubCmd);
						sr.setPayType(payType);
						sr.setSubscribeId(subscribeId);
						sr.setAmount(amount);
						sr.setRemark(remark);
						sr.setTime(time);
						sr.setIp(ip);
						srDao.save(sr);
					}
				}
			}
		}
		return info;
	}

	abstract public ResultInfo subscribeReqAction(RequestContext rc,
			Map<String, Object> props, String returnUrl, Account account,
			ProductDetail detail, PurchaseRelation pr, Date time) throws IOException;
	
	@Override
	final public ResultInfo subscribeReq(RequestContext rc,
			Map<String, Object> props, String returnUrl, Account account,
			ProductDetail detail, Authorization auth, PurchaseRelation pr, Date time)
			throws IOException {
		int amount = pr.getAmount();
		AccountPermission ap = (AccountPermission)props.get("accountPermission");
		ResultInfo info = permitImpl.checkSubscribePermission(ap, time, amount);
		if (info.isSuccess()) {
			ProductPermission pp = (ProductPermission)props.get("productPermission");
			info = permitImpl.checkSubscribePermission(pp, detail, time, amount);
			if (info.isSuccess()) {
				String subscribeId = pr.getSubscribeId();
				if (StringUtils.isNotEmpty(subscribeId)) {
					subscribeReqAction(rc, props, returnUrl, account, detail, pr, time);
				}
				else {
					info.setErrorCode(ErrorCode.EC_INVALID_SUBSCRIBE_ID);
					info.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_SUBSCRIBE_ID));
				}
			}
		}
		return info;
	}
	
	abstract public ResultInfo subscribeRspAction(RequestContext rc,
			Map<String, Object> props, Account account, ProductDetail detail,
			PurchaseRelation pr, String remark, Date time);

	@Override
	final public ResultInfo subscribeRsp(RequestContext rc,
			Map<String, Object> props, Account account, ProductDetail detail, Authorization auth, 
			PurchaseRelation pr, String remark, Date time) {
		ResultInfo info = subscribeRspAction(rc, props, account, detail, pr, remark, time);
		if (info.isSuccess()) {
			String ip = (String)props.get("ip");
			int amount = pr.getAmount();
			int payType = SubscribePayType.PAY_TYPE_BILL;
			try {
				payType = (Integer)props.get("payType");
			}
			catch (Exception e) {
				//null
			}
			ProductPermission pp = (ProductPermission)props.get("productPermission");
			int productSubCmd = permitImpl.updateAfterSubscribe(pp, time, amount);
			AccountPermission ap = (AccountPermission)props.get("accountPermission");
			int accountSubCmd = permitImpl.updateAfterSubscribe(ap, time, amount);
			if (account.isPrivilegeMember()) {
				SubscribeRecord sr = new SubscribeRecord();
				sr.setAccountId(account.getAccountId());
				sr.setUserId(account.getUserId());
				sr.setProductId(detail.getProductId());
				sr.setProductName(detail.getProductName());
				sr.setSubscribeImplementor(pr.getSubscribeImplementor());
				sr.setSubscribeType(pr.getSubscribeType());
				sr.setAccountSubscribeCommand(accountSubCmd);
				sr.setProductSubscribeCommand(productSubCmd);
				sr.setSubscribeId(pr.getSubscribeId());
				sr.setPayType(payType);
				sr.setAmount(amount);
				sr.setRemark(remark);
				sr.setTime(time);
				sr.setIp(ip);
				srDao.save(sr);
			}
		}
		return info;
	}
	
	@Override
	public int getCashToAmountRatio(ProductDetail detail) {
		return cashToAmountRatio;
	}

	@Override
	public String getAmountUnit(ProductDetail detail) {
		return amountUnit;
	}

	public String getImplementorName() {
		return implementorName;
	}

}
