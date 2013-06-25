package cn.ohyeah.itvgame.platform.service;

import java.util.List;

import cn.halcyon.dao.DBException;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.dao.ISubscribeRecordDao;
import cn.ohyeah.itvgame.platform.model.SubscribeRecord;
import cn.ohyeah.itvgame.platform.viewmodel.SubscribeDesc;

public class SubscribeRecordService {
	private static final ISubscribeRecordDao srDao;
	
	static {
		srDao = (ISubscribeRecordDao)BeanManager.getDao("subscribeRecordDao");
	}
	
	public void save(SubscribeRecord sr) {
		try {
			srDao.save(sr);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 查询充值记录
	 * @param userId
	 * @param productId
	 * @param offset
	 * @param length
	 * @return
	 */
	public List<SubscribeDesc> querySubscribeDescList(String userId, int productId, 
			int offset, int length) {
		try {
			return srDao.querySubscribeDescList(userId, productId, offset, length);
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
	public long querySubscribeRecordCount(String userId, int productId) {
		try {
		   return srDao.querySubscribeList(userId, productId);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
}
