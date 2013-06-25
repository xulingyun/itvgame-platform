package cn.ohyeah.itvgame.platform.service;

import java.util.List;

import cn.halcyon.dao.DBException;
import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.dao.IGameRecordDao;
import cn.ohyeah.itvgame.platform.model.GameRecord;
import cn.ohyeah.itvgame.platform.model.Product;
import cn.ohyeah.itvgame.platform.viewmodel.GameRecordDesc;

/**
 * ��Ϸ��¼����ӿ�
 * @author maqian
 * @version 1.0
 */
public class GameRecordService {
	private static final IGameRecordDao recordDao;
	private static final ProductService productServ;
	
	static {
		recordDao = (IGameRecordDao)BeanManager.getDao("gameRecordDao");
		productServ = (ProductService)BeanManager.getBean("productService");
	}
	
	private String getTableName(Product product) {
		return product.getAppName()+"GR";
	}
	
	/**
	 * �����¼
	 * @param record
	 */
	public void save(GameRecord record, int productId) {
		Product product = productServ.read(productId);
		if (product == null) {
			throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PRODUCT)); 
		}
		if (!product.isSupportDataManager()) {
			throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_GAME_RECORD_NOT_SUPPORT)); 
		}
		String tableName = getTableName(product);
		try {
			recordDao.save(tableName, record);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * �������¼�¼
	 * @param record
	 */
	public void saveOrUpdate(GameRecord record, int productId) {
		Product product = productServ.read(productId);
		if (product == null) {
			throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PRODUCT)); 
		}
		
		if (!product.isSupportDataManager()) {
			throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_GAME_RECORD_NOT_SUPPORT)); 
		}
		String tableName = getTableName(product);
		try {
			recordDao.saveOrUpdate(tableName, record);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * ��ȡ��¼
	 * @param accountId
	 * @param productId
	 * @param recordId
	 * @return
	 */
	public GameRecord read(int accountId, int productId, int recordId) {
		Product product = productServ.read(productId);
		if (product == null) {
			throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PRODUCT)); 
		}
		
		if (!product.isSupportDataManager()) {
			throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_GAME_RECORD_NOT_SUPPORT)); 
		}
		String tableName = getTableName(product);
		try {
			return recordDao.read(tableName, accountId, recordId);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * ��ѯ��¼�����б�
	 * @param accountId
	 * @param productId
	 * @return
	 */
	public List<GameRecordDesc> queryRecordDescList(int accountId, int productId) {
		Product product = productServ.read(productId);
		if (product == null) {
			throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PRODUCT)); 
		}
		
		if (!product.isSupportDataManager()) {
			throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_GAME_RECORD_NOT_SUPPORT)); 
		}
		String tableName = getTableName(product);
		try {
			return recordDao.queryDescList(tableName, accountId);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * ���¼�¼
	 * @param record
	 */
	public void update(GameRecord record, int productId) {
		Product product = productServ.read(productId);
		if (product == null) {
			throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PRODUCT)); 
		}
		
		if (!product.isSupportDataManager()) {
			throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_GAME_RECORD_NOT_SUPPORT)); 
		}
		String tableName = getTableName(product);
		try {
			recordDao.update(tableName, record);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
}
