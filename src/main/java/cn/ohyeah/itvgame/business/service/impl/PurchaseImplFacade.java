package cn.ohyeah.itvgame.business.service.impl;

import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.BusinessException;
import cn.ohyeah.itvgame.business.service.IPurchase;
import cn.ohyeah.itvgame.business.service.IRecharge;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.dao.IAccountPropDao;
import cn.ohyeah.itvgame.platform.dao.IPurchaseRecordDao;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.AccountProp;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.GameProp;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.PurchaseRecord;

public class PurchaseImplFacade implements IPurchase {
	private static final Log log = LogFactory.getLog(PurchaseImplFacade.class);
	private static final IAccountPropDao accPropDao;
	private static final IPurchaseRecordDao prDao;
	private static final IRecharge rechargeImpl;
	
	static {
		accPropDao = (IAccountPropDao)BeanManager.getDao("accountPropDao");
		prDao = (IPurchaseRecordDao)BeanManager.getDao("purchaseRecordDao");
		rechargeImpl = BeanManager.getRechargeImplFacade();
	}
	
	@Override
	public ResultInfo expend(Map<String, Object> props, Account account, ProductDetail detail, Authorization auth, 
			int amount, String remark) {
		try {
			log.debug("[Expend Amount] ==> "+amount);
			
			java.util.Date now = new java.util.Date();
			props.put("purchaseTime", now);
			props.put("remark", remark);
			ResultInfo info = rechargeImpl.expend(props, account, detail, auth, amount);
			
			if (info.isSuccess()) {
				PurchaseRecord pr = new PurchaseRecord();
				pr.setAccountId(account.getAccountId());
				pr.setUserId(account.getUserId());
				pr.setPropId(0);
				pr.setPropName("用户消费");
				pr.setPropCount(1);
				pr.setProductId(detail.getProductId());
				pr.setProductName(detail.getProductName());
				pr.setAmount(amount);
				pr.setRemark(remark);
				pr.setTime(now);
				pr.setIp((String)props.get("ip"));
				prDao.save(pr);
			}
			return info;
		}
		catch (Exception e) {
			log.error("[error in expend]", e);
			throw new BusinessException(e);
		}
	}

	@Override
	public ResultInfo purchase(Map<String, Object> props, Account account, ProductDetail detail, Authorization auth, 
			GameProp prop, int propCount, String remark) {
		try {
			int amount = prop.getPrice()*propCount;
			log.debug("[purchase Amount] ==> "+amount);
			
			
			java.util.Date now = new java.util.Date();
			props.put("purchaseTime", now);
			props.put("remark", remark);
			ResultInfo info = rechargeImpl.expend(props, account, detail, auth, amount);
			
			if (info.isSuccess()) {
				boolean existAccProp = false;
				AccountProp accProp = accPropDao.read(account.getAccountId(), detail.getProductId(), prop.getPropId());
				if (accProp == null) {
					existAccProp = false;
					accProp = new AccountProp();
					accProp.setAccountId(account.getAccountId());
					accProp.setProductId(detail.getProductId());
					accProp.setPropId(prop.getPropId());
					if (prop.getValidPeriod() == 0) {
						accProp.setCount(propCount);
					}
					else {
						accProp.setCount(1);
						accProp.setExpiryDate(DateUtils.addSeconds(now, prop.getValidPeriod()*propCount));
					}
				}
				else {
					existAccProp = true;
					if (prop.getValidPeriod() == 0) {
						accProp.incCount(propCount);
					}
					else {
						if (accProp.getExpiryDate().before(now)) {
							accProp.setCount(1);
							accProp.setExpiryDate(DateUtils.addSeconds(now, prop.getValidPeriod()*propCount));
						}
						else {
							java.util.Date expiryDate = DateUtils.addSeconds(accProp.getExpiryDate(), prop.getValidPeriod()*propCount);
							accProp.setExpiryDate(expiryDate);
						}
					}
				}
				if (existAccProp) {
					accPropDao.update(accProp);
				}
				else {
					accPropDao.save(accProp);
				}
				PurchaseRecord pr = new PurchaseRecord();
				pr.setAccountId(account.getAccountId());
				pr.setUserId(account.getUserId());
				pr.setPropId(prop.getPropId());
				pr.setPropName(prop.getPropName());
				pr.setPropCount(propCount);
				pr.setProductId(detail.getProductId());
				pr.setProductName(detail.getProductName());
				pr.setAmount(amount);
				pr.setRemark(remark);
				pr.setTime(now);
				pr.setIp((String)props.get("ip"));
				prDao.save(pr);
			}
			return info;
		}
		catch (Exception e) {
			log.error("[error in purchase]", e);
			throw new BusinessException(e);
		}
	}
}
