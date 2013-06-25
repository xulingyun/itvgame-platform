package cn.ohyeah.itvgame.platform.service;

import java.util.List;

import cn.halcyon.dao.DBException;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.dao.IPurchaseRecordDao;
import cn.ohyeah.itvgame.platform.viewmodel.PurchaseDesc;

public class PurchaseRecordService {
	private static final IPurchaseRecordDao prDao;
	static {
		prDao = (IPurchaseRecordDao)BeanManager.getDao("purchaseRecordDao");
	}
	
	/**
	 * 查询消费记录
	 * @param userId
	 * @param productId
	 * @param offset
	 * @param length
	 * @return
	 */
	public List<PurchaseDesc> queryPurchaseList(int accountId, 
			int productId, int offset, int length) {
		try {
			return prDao.queryPurchaseDescList(accountId, productId, offset, length);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	/**
	 * 查询所有消费记录
	 * @param userId
	 * @param productId
	 * @return
	 */
	public long queryPurchaseRecordCount(int accountId, int productId) {
		try {
		   return prDao.queryPurchaseRecordCount(accountId, productId);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
}
