package cn.ohyeah.itvgame.platform.service;

import java.util.Map;

import cn.halcyon.dao.DBException;
import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.dao.IPurchaseRelationDao;
import cn.ohyeah.itvgame.platform.model.PurchaseRelation;

public class PurchaseRelationService {
	private static final IPurchaseRelationDao prDao;
	static {
		prDao = (IPurchaseRelationDao)BeanManager.getDao("purchaseRelationDao");
	}
	
	public PurchaseRelation read(int productId, String subscribeImplementor, String subscribeType, int value, int amount) {
		try {
			return prDao.read(productId, subscribeImplementor, subscribeType, value, amount);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public PurchaseRelation read(int purchaseId) {
		try {
			return prDao.read(purchaseId);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public void save(PurchaseRelation id) {
		try {
			prDao.save(id);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public PurchaseRelation validateExist(Map<String, Object> props, int purchaseId) {
		PurchaseRelation pr = (PurchaseRelation)props.get("purchaseRelation");
		if (pr == null) {
			pr = read(purchaseId);
			if (pr == null) {
				throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PURCHASE_ID));
			}
			props.put("purchaseRelation", pr);
		}
		return pr;
	}
	/*
	public PurchaseRelation validateExist(Map<String, Object> props, int productId, String subscribeImplementor, String subscribeType, int value, int amount) {
		PurchaseRelation pr = (PurchaseRelation)props.get("purchaseRelation");
		if (pr == null) {
			pr = read(productId, subscribeImplementor, subscribeType, value, amount);
			if (pr == null) {
				throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PURCHASE_ID));
			}
			props.put("purchaseRelation", pr);
		}
		return pr;
	}*/
}
